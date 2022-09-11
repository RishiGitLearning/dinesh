/*
 * frmSupplierFind.java
 *
 * Created on June 16, 2004, 09:46 AM
 */

package EITLERP.Sales;

/** 
 *
 * @author  jadave
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*; 
  
/*<APPLET CODE=frmSupplierFind.class HEIGHT=382 WIDTH=560></APPLET>*/

public class frmLCOpenerFind extends javax.swing.JApplet {
    
     private EITLComboModel cmbStatusModel;
     public boolean Cancelled=false;
     public String strQuery;
    
    /** Creates new form frmSupplierFind */
     public frmLCOpenerFind()
    {
        setSize(600,400);
        initComponents();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtLCOpenerName = new javax.swing.JTextField();
        txtLCPartyCode = new javax.swing.JTextField();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Records");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 12, 170, 15);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setText("LC Opener Code");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 16, 90, 15);

        jLabel3.setText("Opener Name");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 45, 90, 15);

        jPanel1.add(txtLCOpenerName);
        txtLCOpenerName.setBounds(100, 45, 130, 19);

        jPanel1.add(txtLCPartyCode);
        txtLCPartyCode.setBounds(100, 14, 130, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 38, 472, 260);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(490, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(490, 70, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(490, 110, 70, 25);

    }//GEN-END:initComponents

    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        // TODO add your handling code here:
         Cancelled=false;
      
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID); 
       
       if(!txtLCPartyCode.getText().equals(""))
       {
         strQuery=strQuery+" AND LCO_OPENER_CODE='"+txtLCPartyCode.getText()+"' ";
       }
       

       if(!txtLCOpenerName.getText().equals(""))
       {
         strQuery=strQuery+" AND LCO_OPENER_NAME LIKE '%"+txtLCOpenerName.getText()+"%' ";
       }
       
               
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        strQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        // strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID ;  
         Cancelled=false;
         getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdClearActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtLCOpenerName;
    private javax.swing.JTextField txtLCPartyCode;
    // End of variables declaration//GEN-END:variables
 
      

}
