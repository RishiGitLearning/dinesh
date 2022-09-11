/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.Production.FeltRateMaster;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;


public class frmFindFeltRate extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(335,145);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Felt Rate");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 190, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find Felt Rate by press ALT+F");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(240, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(240, 60, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setText("Quality No");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(5, 11, 70, 15);

        jPanel1.add(txtProductCode);
        txtProductCode.setBounds(90, 10, 120, 19);

        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(90, 40, 120, 19);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setLabelFor(txtProductCode);
        jLabel3.setText("Document No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(3, 41, 85, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 220, 70);

    }//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String productCode = txtProductCode.getText().trim();
        String docNo = txtDocNo.getText().trim();
        
        if(!productCode.equals("")) {
            stringFindQuery=" PRODUCT_CODE ='"+ productCode +"'";
        }
        
        if(!docNo.equals("")) {
            stringFindQuery=" DOC_NO ='"+ docNo +"'";
        }
        
        if(!productCode.equals("") && !docNo.equals("")) {
            stringFindQuery=" PRODUCT_CODE ='"+ productCode +"' AND DOC_NO ='"+ docNo +"'";
        }
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtProductCode;
    // End of variables declaration//GEN-END:variables
    
}