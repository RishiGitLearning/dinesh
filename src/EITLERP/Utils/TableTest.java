/*
 * TableTest.java
 *
 * Created on November 28, 2007, 11:26 AM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import EITLERP.*;
import javax.swing.*;


public class TableTest extends javax.swing.JApplet {

    EITLTableModel TableModel=new EITLTableModel();
    
    /** Initializes the applet TableTest */
    public void init() {
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        Table.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(30, 55, 390, 160);

        jButton1.setText("Format Table");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(42, 235, 160, 25);

        jButton2.setText("Add Row");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(43, 269, 160, 25);

        jButton3.setText("Read Month");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton3);
        jButton3.setBounds(216, 238, 140, 25);

    }//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        //Read column 'MONTH', row Selected Row
        String Month=TableModel.getValueByVariable("MONTH", Table.getSelectedRow());
        JOptionPane.showMessageDialog(null," Month selected is "+Month);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try
        {
            
            //Adding a new row to the table
            Object[] rowData=new Object[1]; //Create a new Object array
            TableModel.addRow(rowData); //Add row in array format to Table Model 
            
            int NewRow=Table.getRowCount()-1; //Gives last row (newly added) in the table
            
            TableModel.setValueByVariable("MONTH","January", NewRow); //Set value 'January' in column 'MONTH', row NewRow
            TableModel.setValueByVariable("CREDIT","100.00", NewRow);
            TableModel.setValueByVariable("DEBIT","200.00", NewRow);

            
            //#2 Adding another row
            
            TableModel.addRow(rowData);
            
            NewRow=Table.getRowCount()-1;
            
            TableModel.setValueByVariable("MONTH","February", NewRow);
            TableModel.setValueByVariable("CREDIT","300.00", NewRow);
            TableModel.setValueByVariable("DEBIT","250.00", NewRow);
            
            
            
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        FormatTable();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
 
    private void FormatTable()
    {
      try
      {
        
        Table.removeAll(); //Remove everything from the table
        TableModel=new EITLTableModel(); //Create a new instance of Model for Table
        
        Table.setModel(TableModel); //Set Table Model to the Actual Table
        
        //Define Columns
        TableModel.addColumn("Month"); //Add column 
        TableModel.addColumn("Debit"); //Add column 
        TableModel.addColumn("Credit"); //Add column 
        
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //Turn off Automatic Resizing of Columns
        
        TableModel.SetVariable(0,"MONTH"); //Give identity 'MONTH' to column no. 0
        TableModel.SetVariable(1,"DEBIT");
        TableModel.SetVariable(2,"CREDIT");
          
        TableModel.TableReadOnly(true);
        
      }
      catch(Exception e)
      {
          
      }
    }
    
}
