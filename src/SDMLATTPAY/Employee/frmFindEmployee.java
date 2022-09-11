/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package SDMLATTPAY.Employee;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.LOV;
import EITLERP.clsSales_Party;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.MaskFormatter;


public class frmFindEmployee extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(390,210);
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
        jPanel1 = new javax.swing.JPanel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find :: EMPLOYEE");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 190, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(70, 120, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(190, 120, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        txtDocNo.setText("BRD");
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(110, 10, 160, 27);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setText("EMPLOYEE NO");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 130, 40);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 290, 70);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        
        String docNo = txtDocNo.getText().trim();
        boolean flag=false;
        if(!docNo.equals("")) {
            stringFindQuery=" EMP_NO ='"+ docNo +"'";
            flag=true;
        }
        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    // End of variables declaration//GEN-END:variables
    
}