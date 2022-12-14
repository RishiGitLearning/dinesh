/*
 * frmSelectMIRJob.java
 *
 * Created on November 17, 2008, 1:30 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import java.util.*;
import java.sql.*;

public class frmSelectMIRJob extends javax.swing.JApplet {
    
    private EITLComboModel cmbCompanyModel;
    public boolean cancelled=false;
    
    public String DocNoLabel="";
    public String DocNo="";
    public int CompanyID=0;
    
    private JDialog aDialog;
    
    /** Initializes the applet frmSelectMIRJob */
    public void init() {
       setSize(497,161);
        initComponents();
    }

    public frmSelectMIRJob()
    {
       setSize(497,161);
       initComponents();
       generateCombo();
    }
    
    private void generateCombo()
    {
       try
       {
           
        //----- Generate cmbType ------- //
        cmbCompanyModel=new EITLComboModel();
        cmbCompany.removeAllItems();
        cmbCompany.setModel(cmbCompanyModel);
        
        ResultSet rsCompany=data.getResult("SELECT * FROM D_COM_COMPANY_MASTER ");
        rsCompany.first();
        
        if(rsCompany.getRow()>0)
        {
           while(!rsCompany.isAfterLast())
           {
               ComboData objData=new ComboData();
               objData.Code=UtilFunctions.getInt(rsCompany,"COMPANY_ID",0);
               objData.Text=UtilFunctions.getString(rsCompany,"COMPANY_NAME","");
               
               cmbCompanyModel.addElement(objData);
               
               rsCompany.next();
           }
        }
        
        cmbCompany.setSelectedItem(clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
           
       }
       catch(Exception e)
       {
           
       }
    }
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        lblDocNo = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbCompany = new javax.swing.JComboBox();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" Select Reference Document");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 1, 666, 25);

        lblDocNo.setText("Document No.");
        getContentPane().add(lblDocNo);
        lblDocNo.setBounds(19, 103, 100, 15);

        getContentPane().add(txtDocNo);
        txtDocNo.setBounds(112, 100, 120, 19);

        jLabel1.setText("Company");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(14, 34, 70, 15);

        getContentPane().add(cmbCompany);
        cmbCompany.setBounds(16, 50, 300, 24);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(382, 47, 80, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(383, 82, 80, 25);

    }//GEN-END:initComponents

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        if(txtDocNo.getText().trim().equals(""))
        {
           JOptionPane.showMessageDialog(null,"Please enter the document no");
           return;
        }
        
        DocNo=txtDocNo.getText().trim().toUpperCase();
        CompanyID=EITLERPGLOBAL.getComboCode(cmbCompany);
        
        cancelled=false;
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbCompany;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDocNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtDocNo;
    // End of variables declaration//GEN-END:variables
    
    public boolean ShowDialog() {
        try {
            
            lblDocNo.setText(DocNoLabel);
            
            setSize(497,180);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select Refernce Document",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(true);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
            
        }
        catch(Exception e) {
        }
        return true;
    }
    
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
}
