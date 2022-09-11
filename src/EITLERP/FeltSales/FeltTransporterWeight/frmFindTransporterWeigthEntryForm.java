/*
 * frmFindFeltPackingSlip.java
 * This form is used for searching  the details of Felt Packing
 * 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.FeltTransporterWeight;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.FeltSales.FeltPacking.*;
import EITLERP.EITLERPGLOBAL;


public class frmFindTransporterWeigthEntryForm extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(400,145);
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
        jLabel2 = new javax.swing.JLabel();
        txtPackingNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPackingDate = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Transporter Weight Find Form");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 360, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find Packing Details by press ALT+F");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(290, 30, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(290, 60, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(txtPackingNo);
        jLabel2.setText("Doc No");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(40, 10, 50, 17);
        jPanel1.add(txtPackingNo);
        txtPackingNo.setBounds(100, 10, 130, 27);

        jLabel4.setText("Invoice No");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 40, 70, 17);
        jPanel1.add(txtPackingDate);
        txtPackingDate.setBounds(100, 40, 130, 27);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 280, 70);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        String packingNo = txtPackingNo.getText();
        String packingDate = txtPackingDate.getText();       
        
        if(!packingNo.equals("")) {
            stringFindQuery=" DOC_NO ='"+ packingNo +"'";
        }
        
        if(!packingDate.equals("")) {
            stringFindQuery=" PROCESSING_DATE='"+EITLERPGLOBAL.formatDateDB(packingDate)+"' ";
        }
        
        if(!packingNo.equals("") && !packingDate.equals("")) {
            stringFindQuery=" DOC_NO ='"+ packingNo +"' AND PROCESSING_DATE='"+EITLERPGLOBAL.formatDateDB(packingDate)+"' ";
        }       
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtPackingDate;
    private javax.swing.JTextField txtPackingNo;
    // End of variables declaration//GEN-END:variables
    
}
