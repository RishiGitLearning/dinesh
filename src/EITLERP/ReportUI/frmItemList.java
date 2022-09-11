/*
 * frmItemList.java
 *
 * Created on August 19, 2006, 3:39 PM
 */

package EITLERP.ReportUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.applet.*;


public class frmItemList extends javax.swing.JApplet {
    
    /** Initializes the applet frmItemList */
    public void init() {
        setSize(350,200);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        cmdPreview = new javax.swing.JButton();
        txtFromItem = new javax.swing.JTextField();
        txtToItem = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmdExit = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setText("From Item");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(16, 51, 64, 15);

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(57, 142, 110, 25);

        txtFromItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFromItemActionPerformed(evt);
            }
        });

        getContentPane().add(txtFromItem);
        txtFromItem.setBounds(91, 51, 100, 19);

        getContentPane().add(txtToItem);
        txtToItem.setBounds(91, 81, 100, 19);

        jLabel3.setText("To Item");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(35, 80, 48, 20);

        jPanel2.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(102, 102, 255));
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Item List Report");
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel2.add(jLabel4);
        jLabel4.setBounds(6, 7, 118, 17);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 0, 460, 30);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(180, 143, 130, 25);

    }//GEN-END:initComponents

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
       ((JFrame)getParent().getParent().getParent().getParent()).dispose();
       
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtFromItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFromItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromItemActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
          
       // TODO add your handling code here:

        String Condition="";
        if(!txtFromItem.getText().equals("")) {
            
            Condition+=" AND ITEM_ID>='"+txtFromItem.getText()+"' ";
        }
        
        if(!txtToItem.getText().equals("")) {
            Condition+=" AND ITEM_ID<='"+txtToItem.getText()+"' ";
        }
        
        if(!Condition.trim().equals("")) {
        
            Condition=" WHERE "+Condition.substring(5);
        
        }
        
        
        try {
            
            URL ReportFile= new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptItemList.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&Condition="+Condition);
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        
        catch(Exception e) {
        
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        
        }  
        
        
        
    }//GEN-LAST:event_cmdPreviewActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtFromItem;
    private javax.swing.JTextField txtToItem;
    // End of variables declaration//GEN-END:variables
    
}