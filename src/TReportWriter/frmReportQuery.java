/*
 * frmTextProperties.java
 *
 * Created on July 10, 2007, 3:44 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import TReportWriter.*;
import java.util.*;


public class frmReportQuery extends javax.swing.JApplet {
    
    public TReport objTReport;
    public boolean cancelled=false;
    
    private JDialog aDialog;
    
    public frmReportQuery()
    {
        setSize(481,354);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        opgAlignments = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtQuery = new javax.swing.JTextArea();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setText("Report Query");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(9, 9, 110, 15);

        txtQuery.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQueryFocusLost(evt);
            }
        });

        jScrollPane1.setViewportView(txtQuery);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(8, 27, 340, 300);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(365, 30, 88, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(365, 63, 88, 25);

    }//GEN-END:initComponents

    private void txtQueryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQueryFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQueryFocusLost

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        try {
            
            objTReport.ReportQuery=txtQuery.getText();
            
            cancelled=false;
            aDialog.dispose();
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_cmdOKActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup opgAlignments;
    private javax.swing.JTextArea txtQuery;
    // End of variables declaration//GEN-END:variables


    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null){
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    
    public void ShowDialog() {
        setSize(481,360);
        
        LoadProperties();
        
        Frame f=findParentFrame(this);
        
        aDialog=new JDialog(f,"Report Query",true);
        
        aDialog.getContentPane().add("Center",this);
        Dimension appletSize = this.getSize();
        aDialog.setSize(appletSize);
        aDialog.setResizable(false);
        
        //Place it to center of the screen
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        aDialog.setLocation((int)(screenSize.getWidth()-appletSize.getWidth())/2,(int)(screenSize.getHeight()-appletSize.getHeight())/2);
        
        aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        aDialog.show();
    }

    private void LoadProperties() {
        try {
            
            txtQuery.setText(objTReport.ReportQuery);
        }
        catch(Exception e) {
            
        }
    }
    
}