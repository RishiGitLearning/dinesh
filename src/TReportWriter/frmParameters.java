/*
 * frmGroups.java
 *
 * Created on July 9, 2007, 12:34 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class frmParameters extends javax.swing.JApplet {
    
    
    public TReport objTReport=new TReport();
    public HashMap colParamters=new HashMap();
    public boolean cancelled=false;
    private TEITLComboModel objComboModel=new TEITLComboModel();
    
    private JDialog aDialog;
    
    /** Initializes the applet frmGroups */
    public frmParameters() {
        setSize(347,239);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        GroupList = new javax.swing.JList();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(GroupList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(13, 32, 220, 190);

        cmdNew.setText("New");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdNew);
        cmdNew.setBounds(237, 127, 80, 25);

        cmdEdit.setText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        getContentPane().add(cmdEdit);
        cmdEdit.setBounds(238, 159, 80, 25);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(237, 32, 80, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(237, 59, 80, 25);

        cmdDelete.setText("Delete");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });

        getContentPane().add(cmdDelete);
        cmdDelete.setBounds(239, 192, 80, 25);

    }//GEN-END:initComponents

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        try
        {
            HashMap colBackup=(HashMap)colParamters.clone();
            
            String selVar=GroupList.getSelectedValue().toString();
            
            colParamters.clear();
            
            for(int i=1;i<=colBackup.size();i++)
            {
              TParameter objVar=(TParameter)colBackup.get(Integer.toString(i));
              
              if(!objVar.ParameterName.equals(selVar))
              {
                colParamters.put(Integer.toString(colParamters.size()+1),objVar);  
              }
            }
            
            GenerateList();
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        try
        {
           cancelled=false;
           
           objTReport.colParameters=colParamters;
            
           aDialog.dispose();
           
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_cmdOKActionPerformed
            
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        try {
            if(GroupList.getSelectedIndex()>=0) {
                
                TParameter objVar=(TParameter)colParamters.get(Integer.toString(TGLOBAL.getComboCode(GroupList)));
                
                frmParameter objVariable=new frmParameter();
                objVariable.objReport=objTReport;
                objVariable.objParam=objVar;
                objVariable.ShowDialog();
                
                if(!objVariable.cancelled) {
                    colParamters.remove(Integer.toString(TGLOBAL.getComboCode(GroupList)));
                    colParamters.put(Integer.toString(TGLOBAL.getComboCode(GroupList)),objVariable.objParam);
                }
                
                GenerateList();
            }
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        
        TParameter objNewVar=new TParameter();
        
        frmParameter objVariable=new frmParameter();
        objVariable.objReport=objTReport;
        objVariable.objParam=objNewVar;
        
        objVariable.ShowDialog();
        
        if(!objVariable.cancelled) {
            colParamters.put(Integer.toString(colParamters.size()+1),objVariable.objParam);
            GenerateList();
        }
    }//GEN-LAST:event_cmdNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList GroupList;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdOK;
    private javax.swing.JScrollPane jScrollPane1;
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
        setSize(347,260);

        GenerateList();
        
        Frame f=findParentFrame(this);
        
        aDialog=new JDialog(f,"Report Group",true);
        
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
    
    private void GenerateList() {
        try {
            
            objComboModel=new TEITLComboModel();
            GroupList.removeAll();
            GroupList.setModel(objComboModel);
            
            for(int i=1;i<=colParamters.size();i++) {
                TParameter objVar=(TParameter)colParamters.get(Integer.toString(i));
                TComboData objComboData=new TComboData();
                objComboData.Text=objVar.ParameterName;
                objComboData.Code=i;
                objComboModel.addElement(objComboData);
            }
            
        }
        catch(Exception e) {
            
        }
    }
    
    
    
    
    
    
}
