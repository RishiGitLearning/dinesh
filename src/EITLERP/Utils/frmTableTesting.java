/*
 * frmTableTesting.java
 *
 * Created on July 6, 2004, 4:19 PM
 */ 

package EITLERP.Utils;
 
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import javax.swing.JTable.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import java.net.*;
 
/**
 *
 * @author  root
 */
public class frmTableTesting extends javax.swing.JApplet {
   
    private EITLTableModel DataModelL;
    
    String cellLastValue="";
    
    /** Initializes the applet frmTableTesting */
    public void init() {
        
        //System.setProperty("javaplugin.vm.options","-DtrustProxy=true -Xverify:remote -Djava.class.path=/usr/java/jre/classes -Xms128m -Djava.protocol.handler.pkgs=sun.plugin.net.protocol -Xbootclasspath/a:/usr/java/jre/lib/plugin.jar:/usr/java/jre/lib/javaplugin_l10n.jar -Djavaplugin.lib=/usr/java/jre/lib/i386/libjavaplugin_jni.so -Dmozilla.workaround=true -Djavaplugin.nodotversion=142_03 -Djavaplugin.version=1.4.2_03");
        //System.setProperty("javaplugin.jre.params","-Xms128m");
        
        initComponents();
        FormatGrid();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyType(evt);
            }
        });

        getAccessibleContext().setAccessibleParent(this);
        jLabel1.setText("Table Testing");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 11, 89, 15);

        TableL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TableL);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(9, 54, 381, 170);

        lblStatus.setText("jLabel2");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(17, 252, 221, 15);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(195, 263, 88, 25);

    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        getDataBaseInfo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Focus");
    }//GEN-LAST:event_formFocusGained

    private void KeyType(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyType
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Key Typed");
    }//GEN-LAST:event_KeyType

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Key Pressed");
    }//GEN-LAST:event_formKeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableL;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
    
private void FormatGrid()
{
 
        DataModelL=new EITLTableModel();
        TableL.removeAll();
        
        TableL.setModel(DataModelL);
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //Add Columns to it
        DataModelL.addColumn("Sr."); //0 - Read Only
        DataModelL.addColumn("Item Code"); //1
        DataModelL.addColumn("Item Description"); //2 //Read Only
        
        
        //------- Install Table List Selection Listener ------//
        TableL.getColumnModel().getSelectionModel().addListSelectionListener(
        new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int last=TableL.getSelectedColumn();
                String strVar=DataModelL.getVariable(last);

                
              //=============== Cell Editing Routine =======================//  
              try
              {
              cellLastValue=(String)TableL.getValueAt(TableL.getSelectedRow(),TableL.getSelectedColumn());
                            
              TableL.editCellAt(TableL.getSelectedRow(),TableL.getSelectedColumn());
              if(TableL.getEditorComponent() instanceof JTextComponent)
              {
                  ((JTextComponent)TableL.getEditorComponent()).selectAll();
              }
              }
              catch(Exception cell){}
              //============= Cell Editing Routine Ended =================//
              
                
            }
        }
        );
        //===================================================//
        
        
        //----- Install Table Model Event Listener -------//
        TableL.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    
                    //=========== Cell Update Prevention Check ===========//
                    String curValue=(String)TableL.getValueAt(TableL.getSelectedRow(), e.getColumn());
                    if(curValue.equals(cellLastValue))
                    {
                      return;
                    }
                    //====================================================//
                    
                    
                    int col = e.getColumn();
                }
            }
        });
    

        Object[] rowData=new Object[3];
        rowData[0]="";
        rowData[1]="";
        rowData[2]="";
        DataModelL.addRow(rowData);
        DataModelL.addRow(rowData);
        DataModelL.addRow(rowData);
}

private void getDataBaseInfo()
{
   
   try
   {
   Connection tmpConn;
   tmpConn=data.getConn();
       
   DatabaseMetaData dbInfo=tmpConn.getMetaData();

  String[] names = {"TABLE"}; 
  HashMap Tables=new HashMap();
  int Counter=0;
  ResultSet tableNames = dbInfo.getTables(null,"%", "%", names); 
  ResultSetMetaData Info=tableNames.getMetaData();
  
  while (tableNames.next()) { 
      Counter++;
      Tables.put(Integer.toString(Counter),tableNames.getString("TABLE_NAME"));
      String TableName=tableNames.getString("TABLE_NAME");
      
      
      ResultSet rsPrimary=dbInfo.getPrimaryKeys(null,null, TableName);
      
      while(rsPrimary.next())
      {
        String Column=rsPrimary.getString("COLUMN_NAME");  
        
      }
      
   }
   
   }
   catch(Exception e)
   {
       
   }
   
   
}

}