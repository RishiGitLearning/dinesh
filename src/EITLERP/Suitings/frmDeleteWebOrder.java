/*
 * frmDeleteWebOrder.java
 *
 * Created on January 21, 2014, 3:04 PM
 */

package EITLERP.Suitings;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;

/**
 *
 * @author  Vivek Kumar
 */
public class frmDeleteWebOrder extends javax.swing.JApplet {
    
    /** Initializes the applet frmKhakiReport */
    public void init() {
        initComponents();
        setSize(340, 130);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        cmdDeleteOrder = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtOrderNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("DELETE SUITINGS WEB ORDER");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 2, 340, 25);

        cmdDeleteOrder.setText("Delete Order");
        cmdDeleteOrder.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdDeleteOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteOrderActionPerformed(evt);
            }
        });

        getContentPane().add(cmdDeleteOrder);
        cmdDeleteOrder.setBounds(180, 48, 90, 25);

        jLabel1.setText("Order No.");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 50, 59, 20);

        getContentPane().add(txtOrderNo);
        txtOrderNo.setBounds(73, 50, 100, 20);

    }//GEN-END:initComponents
    
    private void cmdDeleteOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteOrderActionPerformed
        String orderNo=txtOrderNo.getText().trim();
        if(orderNo.length()>=7){
            if(data.IsRecordExist("SELECT ORDER_NO FROM D_SAL_ORDER_HEADER WHERE ORDER_NO='"+orderNo+"'")){
                if(data.IsRecordExist("SELECT ORDER_NO FROM D_SAL_ORDER_HEADER WHERE ORDER_NO='"+orderNo+"' AND STATUS<>'S'")){
                    if(JOptionPane.showConfirmDialog(this,"Are you sure want to delete this ORDER ?","DELETE ORDER",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                        data.Execute("DELETE FROM D_SAL_ORDER_HEADER WHERE ORDER_NO='"+orderNo+"'");
                        data.Execute("DELETE FROM D_SAL_ORDER_DETAIL WHERE ORDER_NO='"+orderNo+"'");
                        txtOrderNo.setText("");
                    }
                }else JOptionPane.showMessageDialog(this,"Sale Note Already Generated for this Order.","ERROR",JOptionPane.ERROR_MESSAGE);
            }else JOptionPane.showMessageDialog(this,"Order No. Not Exists.","ERROR",JOptionPane.ERROR_MESSAGE);
        }else JOptionPane.showMessageDialog(this,"Enter Correct Order No.","ERROR",JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_cmdDeleteOrderActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdDeleteOrder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtOrderNo;
    // End of variables declaration//GEN-END:variables
    
}