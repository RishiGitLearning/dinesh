/*
 * frmChangePassword.java
 *
 * Created on July 3, 2004, 3:36 PM
 */
package SDMLATTPAY.IncrementProposal;

import EITLERP.*;
import javax.swing.*;
import java.awt.*;

/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author nrpithva
 */
public class frmChangeSuperPassword extends javax.swing.JApplet {

    public boolean forceToChange = false;
    public boolean cancelled = false;

    private JDialog aDialog;

    /**
     * Initializes the applet frmChangePassword
     */
    public void init() {
        setSize(430, 250);
        initComponents();
        lblUser.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
    }

    /*public frmChangePassword() {
     setSize(430,250);
     initComponents();
     lblUser.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID));
     }*/
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("SUPER CHANGE PASSWORD");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 400, 25);

        jLabel2.setText("User ");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(12, 50, 25, 14);

        jLabel3.setText("Enter new Super password");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(14, 108, 184, 14);

        lblUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblUser);
        lblUser.setBounds(48, 48, 192, 22);

        cmdOK.setText("OK");
        cmdOK.setNextFocusableComponent(cmdCancel);
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOK);
        cmdOK.setBounds(314, 42, 78, 23);

        cmdCancel.setText("Cancel");
        cmdCancel.setNextFocusableComponent(txtPassword);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(314, 72, 78, 23);

        jLabel4.setText("Confirm new Super password");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(14, 165, 184, 14);
        getContentPane().add(txtPassword);
        txtPassword.setBounds(14, 130, 220, 20);
        getContentPane().add(txtConfirmPassword);
        txtConfirmPassword.setBounds(14, 190, 220, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        //Change the Password

        if (txtPassword.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter the password");
            return;
        }

        if (!txtPassword.getText().trim().equals(txtConfirmPassword.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Password and Confirm password does not match. Please verify");
            return;
        }

        if (txtPassword.getText().trim().length() < 4) {
            JOptionPane.showMessageDialog(null, "Password must be of 4 character in length and must contain atleast one number");
            return;
        }

        String CurrentPassword = data.getStringValueFromDB("SELECT HOD_PASSWORD FROM SDMLATTPAY.HOD WHERE HOD_USER_ID=" + EITLERPGLOBAL.gNewUserID);

        if (CurrentPassword.trim().equals(txtPassword.getText().trim())) {
            JOptionPane.showMessageDialog(null, "New password must be different from your old password");
            return;
        }

        try {

            data.Execute("UPDATE SDMLATTPAY.HOD SET HOD_PASSWORD_YEAR=YEAR('"+EITLERPGLOBAL.FinFromDateDB+"'),HOD_PASSWORD='" + MBMencode.MBMen(txtPassword.getText().trim().getBytes()) + "' WHERE HOD_USER_ID=" + EITLERPGLOBAL.gNewUserID);

            try {
                ((JFrame) getParent().getParent().getParent().getParent()).dispose();
            } catch (Exception p) {
            }

            try {
                aDialog.dispose();
            } catch (Exception p) {
            }

            cancelled = false;
        } catch (Exception e) {

        }
    }//GEN-LAST:event_cmdOKActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:

        cancelled = true;

        try {
            ((JFrame) getParent().getParent().getParent().getParent()).dispose();
        } catch (Exception e) {

        }

        try {
            aDialog.dispose();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_cmdCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables

    public boolean ShowDialog() {
        try {

            setSize(430, 250);
            init();

            Frame f = findParentFrame(this);

            aDialog = new JDialog(f, "Change Password", true);

            aDialog.getContentPane().add("Center", this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(true);

            //Place it to center of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int) (screenSize.width - appletSize.getWidth()) / 2, (int) (screenSize.height - appletSize.getHeight()) / 2);

            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        } catch (Exception e) {
        }
        return true;
    }

    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while (c != null) {
            if (c instanceof Frame) {
                return (Frame) c;
            }

            c = c.getParent();
        }
        return (Frame) null;
    }

}
