/*
 * frmLogin.java
 *
 * Created on April 6, 2004, 11:10 AM
 */
package EITLERP;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import EITLERP.Messaging.*;
import java.text.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  NITIN PITHVA
 *
 */

public class frmrptUserwise_hierarchyrights extends javax.swing.JApplet {
    
    /**
     * Initialize the <i>Login</i> Form
     * @see This is sample
     *
     */
    private EITLComboModel cmbUseModule;
    private Vector ListVector ;
    
    public void init() {
        //System.setProperty("javaplugin.maxHeapSize", "400m");
        System.gc();
        setSize(320,200);
        ListVector = new Vector(1000);
        //GenerateList();
        GenerateCombo();
        //GenerateList();
    }
    
    
    
    /** Creates new form frmLogin */
    public frmrptUserwise_hierarchyrights() {
        System.gc();
        initComponents();
        
        //Chaning Look and Feel
        if(GUIManager.isAvailableLookAndFeel(GUIManager.Windows)) {
            GUIManager.SetLookFeel(GUIManager.Windows);
            GUIManager.UpdateComponents(this);
        }
        
        
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cmbUser = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("User Hierarchy");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 10, 320, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jPanel1.add(cmbUser);
        cmbUser.setBounds(100, 20, 190, 24);

        jLabel1.setText("User Name:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 20, 90, 20);

        jButton1.setText("Perview");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);
        jButton1.setBounds(200, 80, 88, 25);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 50, 310, 130);

    }//GEN-END:initComponents
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(cmbUser.getSelectedIndex() <=0) {
            JOptionPane.showMessageDialog(null,"Please Select User");
            return;
        }
        PreviewReport();
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbUser;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
    
    
    
    private void PreviewReport() {
        
        String userId=String.valueOf(EITLERPGLOBAL.getComboCode(cmbUser));
        String userName=cmbUser.getSelectedItem().toString();
        HashMap Params=new HashMap();
        
        Params.put("company_id", new Integer(EITLERPGLOBAL.gCompanyID));
        Params.put("user_id",userId);
        Params.put("user_name",userName);
        
        try {
            
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptUserwise_hierarchyrights.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&UserID="+userId+"&UserName="+userName);
            System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptUserwise_hierarchyrights.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&UserID="+userId+"&UserName="+userName);
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
    }
    
    private void GenerateCombo() {
        
        String strCondition="";
        
        //----- Generate Department Combo ------- //
        cmbUseModule=new EITLComboModel();
        cmbUser.removeAllItems();
        cmbUser.setModel(cmbUseModule);
        
        ComboData aData=new ComboData();
        aData.Code=0;
        aData.Text="Select User Name";
        cmbUseModule.addElement(aData);
        
        
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_USER_MASTER WHERE COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND USER_ID > 1 ORDER BY USER_NAME ");
            rsTmp.first();
            while(!rsTmp.isAfterLast()) {
                aData=new ComboData();
                aData.Code=rsTmp.getLong("USER_ID");
                aData.Text=rsTmp.getString("USER_NAME");
                cmbUseModule.addElement(aData);
                
                rsTmp.next();
            }
            rsTmp.close();
            tmpStmt.close();
        }
        catch(Exception e) {
        }
    }
    
    
}