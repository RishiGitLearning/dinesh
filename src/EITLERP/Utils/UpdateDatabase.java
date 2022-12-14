/*
 * UpdateDatabase.java
 *
 * Created on July 11, 2004, 12:44 PM
 */

package EITLERP.Utils;
  
/**
 * 
 * @author  root
 */
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;

public class UpdateDatabase extends javax.swing.JApplet {
    
    /** Initializes the applet UpdateDatabase */
    public void init() {
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cmdUpdate = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        cmdUpdate.setText("Update");
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });

        getContentPane().add(cmdUpdate);
        cmdUpdate.setBounds(25, 26, 140, 25);

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(32, 109, 63, 25);

    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        UpdateMRSrNo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        // TODO add your handling code here:

        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try
        {
          tmpConn=data.getConn();
          stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
          rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_SUPP_MASTER");
          rsTmp.first();
          
          int Counter=0;
          
          while(!rsTmp.isAfterLast())
          {
              Counter++;
              rsTmp.updateInt("SUPP_ID",Counter);  
              rsTmp.updateRow();
              rsTmp.next();
          }
          
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }//GEN-LAST:event_cmdUpdateActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    
    private void UpdateMRSrNo() {
        
                ResultSet rsSource;
                ResultSet rsDest;
                ResultSet rsItem;
                ResultSet rsTemp;
                
                try {
                
                    Connection tmpConn=data.getConn();
                    Statement stTmp=tmpConn.createStatement();
                    
                    
                    rsItem=stTmp.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE MR_NO<>'' AND MR_SR_NO=0 ");
                    rsItem.first();
                    
                    if(rsItem.getRow()>0) {
                        while(!rsItem.isAfterLast()) {
                            
                            String ReqNo=rsItem.getString("MR_NO");
                            String ItemID=rsItem.getString("ITEM_CODE");
                            String IndentNo=rsItem.getString("INDENT_NO");
                            int IndentSrNo=rsItem.getInt("SR_NO");
                            
                            
                            Statement stDest=tmpConn.createStatement();
                            rsDest=stDest.executeQuery("SELECT SR_NO FROM D_INV_REQ_DETAIL WHERE REQ_NO='"+ReqNo+"' AND ITEM_CODE='"+ItemID+"'");
                            rsDest.first();
                            
                            if(rsDest.getRow()>0) {
                                int ReqSrNo=rsDest.getInt("SR_NO");
                                
                                data.Execute("UPDATE D_INV_INDENT_DETAIL SET MR_SR_NO="+ReqSrNo+" WHERE INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                            }
                            
                            
                            rsItem.next();
                        }
                        
                        
                    }
                    
                    JOptionPane.showMessageDialog(null,"Done ...");
                    
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
        
    }
    
}
