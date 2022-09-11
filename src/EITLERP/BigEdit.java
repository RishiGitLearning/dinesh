/*
 * BigEdit.java
 *
 * Created on December 3, 2004, 10:59 AM
 */

package EITLERP;

/**
 * 
 * @author  root
 */
import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.net.*;


public class BigEdit extends javax.swing.JApplet {
    
    /** Initializes the applet BigEdit */
    /*public void init() {
        initComponents();
    }*/
    
    public JTextField theText=new JTextField();
    
    private JDialog aDialog;

    public BigEdit()
    {
      setSize(305, 506);  
      System.gc();  
      initComponents();  
    }
    
    public boolean ShowEdit() {
        try {
            
            
            txtEdit.setText(theText.getText());
            txtEdit.setEditable(theText.isEnabled());
            cmdSave.setEnabled(theText.isEnabled());
            txtEdit.requestFocus();
            
            
            setSize(506,330);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Edit Text",true);
            
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
        catch(Exception e) {
        }
        return true;
    }
    
    
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEdit = new javax.swing.JTextArea();
        cmdClose = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        txtEdit.setLineWrap(true);
        jScrollPane1.setViewportView(txtEdit);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(4, 7, 392, 291);

        cmdClose.setText("Exit");
        cmdClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClose);
        cmdClose.setBounds(408, 49, 88, 25);

        cmdSave.setText("Save");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSave);
        cmdSave.setBounds(406, 11, 88, 25);

    }//GEN-END:initComponents

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        theText.setText(txtEdit.getText());
        aDialog.dispose();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseActionPerformed
        // TODO add your handling code here:
        aDialog.dispose();
    }//GEN-LAST:event_cmdCloseActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdClose;
    private javax.swing.JButton cmdSave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtEdit;
    // End of variables declaration//GEN-END:variables
 
//Recurses through the hierarchy of classes 
//until it finds Frame 
private Frame findParentFrame(JApplet pApplet)
{ 
    Container c = (Container) pApplet; 
    while(c != null){ 
      if (c instanceof Frame) 
        return (Frame)c; 

      c = c.getParent(); 
    } 
    return (Frame)null; 
  } 
    
    
}
