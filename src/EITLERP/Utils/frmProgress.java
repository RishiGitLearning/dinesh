/*
 * frmProgress.java
 *
 * Created on August 29, 2006, 3:34 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.net.*;
import java.awt.Frame;

public class frmProgress extends javax.swing.JApplet {
    
    private JDialog aDialog;

    public void Initialize()
    {
        setSize(400,88);
        initComponents();
        
    }
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblStatus = new javax.swing.JLabel();
        Bar = new javax.swing.JProgressBar();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lblStatus.setText(".");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(12, 7, 370, 15);

        Bar.setStringPainted(true);
        getContentPane().add(Bar);
        Bar.setBounds(16, 47, 360, 20);

    }//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
    
    public void SetMax(int Value) {
        try
        {
        Bar.setMaximum(Value);    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void SetMin(int Value) {
        Bar.setMinimum(Value);
    }
    
    public void SetText(String Text)
    {
       lblStatus.setText(Text); 
    }
    public void SetValue(int Value) {
        try {
            Bar.setValue(Value);
        }
        catch(Exception e) {
            
        }
    }
    

    public void Hide()
    {
        aDialog.dispose();
    }
    
    public void ShowDialog() {
        try {
            
            setSize(400,120);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Progress",false );
            
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
            
        }
        catch(Exception e) {
        }
        
        
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
