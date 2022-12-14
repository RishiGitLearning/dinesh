/*
 * frmChangeLocation.java
 *
 * Created on December 9, 2006, 12:58 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;


public class frmChangeLocation extends javax.swing.JApplet {
    
    /** Initializes the applet frmChangeLocation */
    public void init() {
        setSize(456,350);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblWarehouse = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblLocation = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNewLocation = new javax.swing.JTextField();
        cmdUpdate = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setText("Item Code");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(42, 60, 70, 15);

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setText("ITEM LOCATION CHANGE");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(8, 9, 204, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(1, 2, 480, 32);

        txtItemID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemIDFocusLost(evt);
            }
        });
        txtItemID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemIDKeyPressed(evt);
            }
        });

        getContentPane().add(txtItemID);
        txtItemID.setBounds(122, 59, 110, 19);

        txtItemName.setEditable(false);
        getContentPane().add(txtItemName);
        txtItemName.setBounds(121, 84, 290, 19);

        jLabel3.setText("Warehouse");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(39, 127, 70, 15);

        lblWarehouse.setForeground(new java.awt.Color(51, 51, 255));
        lblWarehouse.setText(".");
        getContentPane().add(lblWarehouse);
        lblWarehouse.setBounds(122, 127, 230, 15);

        jLabel5.setText("Current Location");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(7, 151, 110, 15);

        lblLocation.setForeground(new java.awt.Color(51, 51, 255));
        lblLocation.setText(".");
        getContentPane().add(lblLocation);
        lblLocation.setBounds(122, 151, 230, 15);

        jLabel7.setText("New Location");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(25, 184, 90, 15);

        txtNewLocation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNewLocationKeyPressed(evt);
            }
        });

        getContentPane().add(txtNewLocation);
        txtNewLocation.setBounds(121, 183, 110, 19);

        cmdUpdate.setText("Update Location");
        cmdUpdate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmdUpdateItemStateChanged(evt);
            }
        });
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });

        getContentPane().add(cmdUpdate);
        cmdUpdate.setBounds(57, 278, 150, 25);

        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExit);
        cmdExit.setBounds(227, 278, 150, 25);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14));
        lblStatus.setForeground(new java.awt.Color(102, 102, 255));
        lblStatus.setText(".");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(121, 227, 310, 17);

    }//GEN-END:initComponents

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        // TODO add your handling code here:
        try {
            
            if(!clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID,txtItemID.getText())) {
                JOptionPane.showMessageDialog(null,"Please specify valid item code. Press F1 for the list of items");
                return;
            }
            
            
            if(txtNewLocation.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please specify the new location");
                return;
            }
            
            
            new Thread() {
                public void run() {
                    
                    String strSQL="";
                    String OldLocation=lblLocation.getText().trim().toUpperCase();
                    String NewLocation=txtNewLocation.getText().trim().toUpperCase();
                    String ItemID=txtItemID.getText().trim();
                    
                    if(OldLocation.equals(NewLocation)) {
                        JOptionPane.showMessageDialog(null,"There is no change in location");
                        return;
                    }
                    
                    lblStatus.setText("Updating GRN Data");
                    
                    strSQL="UPDATE D_INV_GRN_DETAIL SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    
                    lblStatus.setText("Updating GRN History Data");
                    
                    strSQL="UPDATE D_INV_GRN_DETAIL_H SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Updating MIR Data");
                    
                    strSQL="UPDATE D_INV_MIR_DETAIL SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    
                    
                    lblStatus.setText("Updating MIR History Data");
                    
                    strSQL="UPDATE D_INV_MIR_DETAIL_H SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Updating Issue Data");
                    
                    strSQL="UPDATE D_INV_ISSUE_DETAIL SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_CODE='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    
                    
                    lblStatus.setText("Updating Issue History Data");
                    
                    strSQL="UPDATE D_INV_ISSUE_DETAIL_H SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_CODE='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);

                    lblStatus.setText("Updating Item Data");

                    strSQL="UPDATE D_INV_ITEM_MASTER SET CHANGED=1,CHANGED_DATE=CURDATE(),LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    
                    
                    lblStatus.setText("Updating Item History Data");

                    strSQL="UPDATE D_INV_ITEM_MASTER_H SET CHANGED=1,CHANGED_DATE=CURDATE(),LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Updating Item History Data");

                    strSQL="UPDATE D_INV_ITEM_LOT_MASTER SET CHANGED=1,CHANGED_DATE=CURDATE(),LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Updating Data . ");

                    strSQL="UPDATE D_INV_RJN_DETAIL SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Updating Data .. ");

                    strSQL="UPDATE D_INV_RJN_DETAIL_H SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    
                    lblStatus.setText("Updating Opening Stock Data . ");

                    strSQL="UPDATE D_COM_OPENING_STOCK_DETAIL SET LOCATION_ID='"+NewLocation+"' WHERE ITEM_ID='"+ItemID+"' AND LOCATION_ID='"+OldLocation+"'";
                    data.Execute(strSQL);
                    

                    lblStatus.setText("Done");
                    
                };
            }.start();
            
            
            
            
            
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdUpdateActionPerformed
    
    private void cmdUpdateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmdUpdateItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdUpdateItemStateChanged
    
    private void txtNewLocationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNewLocationKeyPressed
        // TODO add your handling code here:
        try {
            if(evt.getKeyCode()==112) //F1 Key pressed
            {
                LOV aList=new LOV();
                
                aList.SQL="SELECT LOCATION_ID,LOCATION_NAME FROM D_INV_LOCATION_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY LOCATION_ID";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                
                if(aList.ShowLOV()) {
                    txtNewLocation.setText(aList.ReturnVal);
                }
            }
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtNewLocationKeyPressed
    
    private void txtItemIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIDKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtItemID.setText(aList.ReturnVal);
                txtItemName.setText(clsItem.getItemName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
                
                
                try {
                    String WareHouseID ="";
                    String LocationID="";
                    
                    lblWarehouse.setText("");
                    lblLocation.setText("");
                    
                    
                    if(clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID,txtItemID.getText())) {
                        WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, txtItemID.getText());
                        LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, txtItemID.getText());
                        
                        lblWarehouse.setText(clsWarehouse.getWarehouseName(EITLERPGLOBAL.gCompanyID, WareHouseID));
                        lblLocation.setText(LocationID);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Item code you entered is invalid. Please check");
                        
                    }
                }
                catch(Exception e) {
                    
                }
                
            }
        }
        
    }//GEN-LAST:event_txtItemIDKeyPressed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null," W "+getWidth()+" H "+getHeight());
    }//GEN-LAST:event_formMouseClicked
    
    private void txtItemIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemIDFocusLost
        // TODO add your handling code here:
        try {
            String WareHouseID ="";
            String LocationID="";
            
            lblWarehouse.setText("");
            lblLocation.setText("");
            
            if(txtItemID.getText().trim().equals("")) {
                return;
            }
            
            if(clsItem.IsValidItemID(EITLERPGLOBAL.gCompanyID,txtItemID.getText())) {
                WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, txtItemID.getText());
                LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, txtItemID.getText());
                
                lblWarehouse.setText(clsWarehouse.getWarehouseName(EITLERPGLOBAL.gCompanyID, WareHouseID));
                lblLocation.setText(LocationID);
            }
            else {
                JOptionPane.showMessageDialog(null,"Item code you entered is invalid. Please check");
                
            }
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtItemIDFocusLost
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblWarehouse;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtNewLocation;
    // End of variables declaration//GEN-END:variables
    
}
