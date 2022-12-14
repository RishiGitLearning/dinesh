/*
 * frmFindFeltOrderUpd.java
 * This form is used for searching  the details of Felt Order Piece No.
 * 
 * Created on November 30, 2014, 
 */

package EITLERP.FeltSales.PieceUpdation;
/**
 *
 * @author  DAXESH PRAJAPATI
 */

import EITLERP.EITLERPGLOBAL;


public class frmFindFeltPieceUpd extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(355,169);
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
        jTextFieldAmendDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        party_code = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldAmendID = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find Felt Order Updation Details");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 280, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find Felt Order Updation/Amendment details by Amendment date");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(265, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(265, 67, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(jTextFieldAmendDate);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClear);
        cmdClear.setBounds(265, 104, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(jTextFieldAmendDate);
        jLabel2.setText("Amend Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 10, 80, 17);
        jPanel1.add(jTextFieldAmendDate);
        jTextFieldAmendDate.setBounds(110, 10, 130, 27);

        jLabel4.setText("Party Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 40, 80, 17);
        jPanel1.add(party_code);
        party_code.setBounds(110, 40, 130, 27);

        jLabel5.setText("AmendID");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 70, 80, 17);
        jPanel1.add(jTextFieldAmendID);
        jTextFieldAmendID.setBounds(110, 70, 130, 27);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 247, 100);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        stringFindQuery="";        
        jTextFieldAmendDate.setText("");        
        party_code.setText("");
        jTextFieldAmendID.setText("");
    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String amendDate = jTextFieldAmendDate.getText();       
        String partycode = party_code.getText();
        String amendID = jTextFieldAmendID.getText();        
        
        if(!amendDate.equals("")) {
            stringFindQuery=stringFindQuery+" PU_DATE='"+EITLERPGLOBAL.formatDateDB(amendDate)+"' ";
        }
        
        if(!partycode.equals("")) {
            stringFindQuery=stringFindQuery+" PU_PARTY_CODE+0 ='"+ partycode +"'";
        }
        
        if(!amendID.equals("")) {
            stringFindQuery=stringFindQuery+" PU_NO='"+ amendID +"'";
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldAmendDate;
    private javax.swing.JTextField jTextFieldAmendID;
    private javax.swing.JTextField party_code;
    // End of variables declaration//GEN-END:variables
    
}
