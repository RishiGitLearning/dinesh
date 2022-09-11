/*
 * frmFindMonthlyAttendance.java
 * This form is used for searching  the details of Felt Production Finishing
 * 
 * Created on August 22, 2013, 11:20 AM
 */

package SDMLATTPAY.DailyRokdiAttData;
/**
 *
 * @author  Vivek Kumar
 */

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import SDMLATTPAY.Employee.clsMaster;
import java.util.HashMap;


public class frmFindDailyRokdiAttData extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";
    private EITLComboModel modelDept = new EITLComboModel(); 
   
    public void init() {
        System.gc();
        setSize(450, 180);
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
        jTextFieldDocNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAttDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbDept = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find Daily Rokadi Attendance Details (Regular)");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 390, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find felt finishing details by production date");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(350, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(350, 70, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(jTextFieldDocNo);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClear);
        cmdClear.setBounds(350, 110, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(jTextFieldDocNo);
        jLabel2.setText("Doc No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 10, 100, 20);
        jPanel1.add(jTextFieldDocNo);
        jTextFieldDocNo.setBounds(110, 10, 120, 19);

        jLabel4.setText("Rokdi Date.");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 40, 100, 20);
        jPanel1.add(txtAttDate);
        txtAttDate.setBounds(110, 40, 120, 19);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setLabelFor(jTextFieldDocNo);
        jLabel3.setText("Rokdi Dept.");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 70, 100, 20);

        cmbDept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Dept", "WEAVING", "YARN STORE", "MENDING", "NEEDLING", "FINISHING", "CARDING", "WAREHOUSE", "ENGINEERING", "RMG", "SECURITY", "STORES", "PEONS" }));
        jPanel1.add(cmbDept);
        cmbDept.setBounds(110, 70, 210, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 330, 110);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        stringFindQuery="";        
        jTextFieldDocNo.setText("");        
        txtAttDate.setText("");
        cmbDept.setSelectedIndex(0);
    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String docNo = jTextFieldDocNo.getText();       
        String attDate = txtAttDate.getText();
//        String attDept = String.valueOf(cmbDept.getSelectedIndex());
        String attDept = cmbDept.getSelectedItem().toString();
        
        if(!docNo.equals("")) {
            stringFindQuery=stringFindQuery+" AND DOC_NO='"+ docNo +"' ";
        }
        
//        if(!attDate.equals("") && !attDept.equals("")) {
//            stringFindQuery=stringFindQuery+" AND ATT_DATE ='"+ attDate +"' AND ATT_DEPT ='"+ attDept +"' ";
//        }
        
        if(!attDate.equals("")) {
            stringFindQuery=stringFindQuery+" AND ROKDI_ATT_DATE ='"+ attDate +"' ";
        }
        
        if(!attDept.equals("")) {
            stringFindQuery=stringFindQuery+" AND ROKDI_ATT_DEPT ='"+ attDept +"' ";
        }
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldDocNo;
    private javax.swing.JTextField txtAttDate;
    // End of variables declaration//GEN-END:variables
    
}
