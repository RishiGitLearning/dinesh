/*
 * frmFindFeltWeaving.java
 * This form is used for searching  the details of Felt Production Weaving
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltWeaveout;
/**
 *
 * @author  Vivek Kumar
 */

import EITLERP.EITLERPGLOBAL;


public class frmFindFeltWeaveout extends javax.swing.JApplet {
    
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
        jTextFieldProductionDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldPieceNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldFormNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find Felt Weaveout Details");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 230, 15);

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
        cmdFind.setBounds(320, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(320, 60, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(jTextFieldProductionDate);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        getContentPane().add(cmdClear);
        cmdClear.setBounds(320, 100, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(jTextFieldProductionDate);
        jLabel2.setText("Production Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(5, 11, 130, 16);
        jPanel1.add(jTextFieldProductionDate);
        jTextFieldProductionDate.setBounds(160, 10, 130, 28);

        jLabel4.setText("Piece No.");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(50, 41, 70, 16);
        jPanel1.add(jTextFieldPieceNo);
        jTextFieldPieceNo.setBounds(160, 40, 130, 28);

        jLabel5.setText("Shift No.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(48, 71, 80, 16);
        jPanel1.add(jTextFieldFormNo);
        jTextFieldFormNo.setBounds(160, 70, 130, 28);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 300, 100);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        stringFindQuery="";        
        jTextFieldProductionDate.setText("");        
        jTextFieldPieceNo.setText("");
        jTextFieldFormNo.setText("");
    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String productionDate = jTextFieldProductionDate.getText();       
        String pieceNo = jTextFieldPieceNo.getText();
        String formNo = jTextFieldFormNo.getText();        
        
        if(!productionDate.equals("")) {
            stringFindQuery=stringFindQuery+" AND WVOUT_DATE='"+EITLERPGLOBAL.formatDateDB(productionDate)+"' ";
        }
        
        if(!pieceNo.equals("")) {
            stringFindQuery=stringFindQuery+" AND WVOUT_PIECE_NO ='"+ pieceNo +"'";
        }
        
        if(!formNo.equals("")) {
            stringFindQuery=stringFindQuery+" AND WVOUT_SHIFT ='"+ formNo +"'";
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
    private javax.swing.JTextField jTextFieldFormNo;
    private javax.swing.JTextField jTextFieldPieceNo;
    private javax.swing.JTextField jTextFieldProductionDate;
    // End of variables declaration//GEN-END:variables
    
}
