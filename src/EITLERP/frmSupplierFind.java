/*
 * frmSupplierFind.java
 *
 * Created on June 16, 2004, 09:46 AM
 */

package EITLERP;

/** 
 *
 * @author  jadave
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*; 
  
/*<APPLET CODE=frmSupplierFind.class HEIGHT=382 WIDTH=560></APPLET>*/

public class frmSupplierFind extends javax.swing.JApplet {
    
     private EITLComboModel cmbStatusModel;
     public boolean Cancelled=false;
     public String strQuery;
    
    /** Creates new form frmSupplierFind */
    public frmSupplierFind()
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
        txtCity = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAttn = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDummyCode = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        txtCountry = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        chkReg = new javax.swing.JCheckBox();
        chkBlocked = new javax.swing.JCheckBox();
        chkOneTime = new javax.swing.JCheckBox();
        chkSlow = new javax.swing.JCheckBox();
        chkSSI = new javax.swing.JCheckBox();
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
        jLabel2.setText("Supplier Code");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 16, 90, 15);
        
        jPanel1.add(txtCity);
        txtCity.setBounds(100, 78, 132, 19);
        
        jLabel3.setText("Name");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 45, 50, 15);
        
        jPanel1.add(txtName);
        txtName.setBounds(100, 45, 130, 19);
        
        jLabel4.setText("Attn");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(290, 45, 40, 15);
        
        jPanel1.add(txtAttn);
        txtAttn.setBounds(330, 45, 130, 19);
        
        jLabel10.setText("City");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(60, 80, 40, 15);
        
        jPanel1.add(txtSuppCode);
        txtSuppCode.setBounds(100, 14, 130, 19);
        
        jLabel5.setText("Dummy Code");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(240, 16, 90, 15);
        
        jPanel1.add(txtDummyCode);
        txtDummyCode.setBounds(330, 14, 132, 19);
        
        jLabel6.setText("State");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(280, 80, 41, 15);
        
        jPanel1.add(txtState);
        txtState.setBounds(330, 78, 130, 19);
        
        jPanel1.add(txtCountry);
        txtCountry.setBounds(100, 110, 130, 19);
        
        jLabel7.setText("Country");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(40, 110, 60, 15);
        
        chkReg.setText("Registered");
        jPanel1.add(chkReg);
        chkReg.setBounds(90, 160, 130, 23);
        
        chkBlocked.setText("Blocked");
        jPanel1.add(chkBlocked);
        chkBlocked.setBounds(330, 160, 100, 23);
        
        chkOneTime.setText("One Time Supplier");
        jPanel1.add(chkOneTime);
        chkOneTime.setBounds(90, 190, 140, 23);
        
        chkSlow.setText("Slow Moving");
        chkSlow.setNextFocusableComponent(chkSSI);
        jPanel1.add(chkSlow);
        chkSlow.setBounds(330, 190, 130, 23);
        
        chkSSI.setText("SSI Supplier");
        chkSSI.setNextFocusableComponent(cmdFind);
        jPanel1.add(chkSSI);
        chkSSI.setBounds(90, 220, 130, 23);
        
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
       
       if(!txtSuppCode.getText().equals(""))
       {
         strQuery=strQuery+" AND SUPPLIER_CODE='"+txtSuppCode.getText()+"' ";
       }
       
       if(!txtDummyCode.getText().equals(""))
       {
         strQuery=strQuery+" AND DUMMY_SUPPLIER_CODE LIKE '"+txtDummyCode.getText()+"%' ";
       }

       if(!txtName.getText().equals(""))
       {
         strQuery=strQuery+" AND SUPP_NAME LIKE '%"+txtName.getText()+"%' ";
       }
      
       if(!txtAttn.getText().trim().equals(""))
       {
        strQuery=strQuery+" AND ATTN LIKE '%"+txtAttn.getText()+"%' ";
       }
       
       if(!txtCity.getText().trim().equals(""))
       {
        strQuery=strQuery+" AND CITY LIKE '%"+txtAttn.getText()+"%' ";
       }
       
       if(!txtState.getText().trim().equals(""))
       {
        strQuery=strQuery+" AND STATE LIKE '%"+txtState.getText()+"%' ";
       }
       
       if(!txtCountry.getText().trim().equals(""))
       {
        strQuery=strQuery+" AND COUNTRY LIKE '%"+txtCountry.getText()+"%' ";
       }
       
       if(chkOneTime.isSelected()) {
           strQuery=strQuery+" AND ONETIME_SUPPLIER=1";            
        }
        
       if(chkBlocked.isSelected()) {
            strQuery=strQuery+" AND BLOCKED='Y'";               
       }
       
       if(chkSlow.isSelected()) {
            strQuery=strQuery+" AND SLOW_MOVING=1";             
        }
       
        if(chkReg.isSelected()) {
            strQuery=strQuery+" AND ST35_REGISTERED=1";             
        }
       
       if(chkSSI.isSelected()) {
            strQuery=strQuery+" AND SSIREG=1";             
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
         strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID ;  
         Cancelled=false;
         getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdClearActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkBlocked;
    private javax.swing.JCheckBox chkOneTime;
    private javax.swing.JCheckBox chkReg;
    private javax.swing.JCheckBox chkSSI;
    private javax.swing.JCheckBox chkSlow;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtAttn;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtDummyCode;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtSuppCode;
    // End of variables declaration//GEN-END:variables
 
      

}