/*
 * frmFindFeltWeaving.java
 * This form is used for searching  the details of Felt Production Weaving
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package SDMLATTPAY.gatepass;
/**
 *
 * @author  Ashutosh
 */

import SDMLATTPAY.gatepass.*;
import EITLERP.EITLERPGLOBAL;


public class FrmGatepassFind extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(455,169);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtGatepassDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtGatepassNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find Gatepass Details");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 240, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find Weaving details by production date");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(340, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(340, 60, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtGatepassDate);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClear);
        cmdClear.setBounds(340, 100, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(txtGatepassDate);
        jLabel2.setText("Gatepass Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(15, 11, 140, 15);
        jPanel1.add(txtGatepassDate);
        txtGatepassDate.setBounds(170, 10, 130, 27);

        jLabel4.setText("Gatepass No.");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(19, 41, 130, 20);
        jPanel1.add(txtGatepassNo);
        txtGatepassNo.setBounds(170, 40, 130, 27);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 320, 100);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        stringFindQuery="";        
        txtGatepassDate.setText("");        
        txtGatepassNo.setText("");
 
    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String gatepassDate = txtGatepassDate.getText();
        String gatepassNo = txtGatepassNo.getText().toUpperCase();
        
        
        if(!gatepassDate.equals("") && !gatepassNo.equals("")) {
            stringFindQuery=stringFindQuery+" GP_DATE='"+EITLERPGLOBAL.formatDateDB(gatepassDate)+"' AND GP_DOC_NO ='"+ gatepassNo +"' ";
        }
        else if(!gatepassNo.equals("")){
            stringFindQuery=stringFindQuery+" GP_DOC_NO ='"+gatepassNo+"'";
        }        
        else if(!gatepassDate.equals("")) {
            stringFindQuery=stringFindQuery+" GP_DATE ='"+EITLERPGLOBAL.formatDateDB(gatepassDate)+"'";
        }        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtGatepassDate;
    private javax.swing.JTextField txtGatepassNo;
    // End of variables declaration//GEN-END:variables
    
}