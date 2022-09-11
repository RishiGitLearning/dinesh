/*
 * frmTRmapping.java
 *
 * Created on December 14, 2007, 1:03 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */

import EITLERP.* ;
import java.sql.*;
import javax.swing.* ;

public class frmTRMapping extends javax.swing.JApplet {
    
    
    EITLTableModel TableModelT;
    /** Initializes the applet frmTRmapping */
    public void init() {
        setSize(451,386);
        initComponents();
        FormatGrid();
        GenerateGrid();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtMainAccountCode = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableT = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

        jLabel1.setBackground(new java.awt.Color(102, 102, 255));
        jLabel1.setText("Item Code");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 46, 70, 20);

        jLabel2.setText("Main A/C Code");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(21, 72, 100, 20);

        jPanel1.add(txtItemCode);
        txtItemCode.setBounds(125, 49, 110, 19);

        jPanel1.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(127, 74, 110, 19);

        jScrollPane1.setBorder(new javax.swing.border.EtchedBorder());
        TableT.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableT);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(8, 108, 330, 240);

        cmdAdd.setText("Add  ");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        jPanel1.add(cmdAdd);
        cmdAdd.setBounds(347, 110, 90, 25);

        cmdRemove.setText("Remove");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        jPanel1.add(cmdRemove);
        cmdRemove.setBounds(348, 139, 90, 25);

        jPanel2.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));
        jPanel2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("ITEM CODE - ACCOUNT HEAD MAPPING");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(9, 8, 280, 15);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(1, 2, 800, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 480, 360);

    }//GEN-END:initComponents

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        int ans;
        ans = JOptionPane.showConfirmDialog(null,"Are you Sure to Remove?","Confirmation",JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION){
         data.Execute("DELETE FROM D_FIN_TR_MAPPING WHERE ITEM_CODE ="+TableModelT.getValueAt(TableT.getSelectedRow(),0)+" AND COMPANY_ID ="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
        }
        
         FormatGrid();
         GenerateGrid();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        try{
        Connection Conn;
        Statement Stmt;
        ResultSet rsInsertTR;
        int srno = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_TR_MAPPING",FinanceGlobal.FinURL);        
        Conn=data.getConn(FinanceGlobal.FinURL);
        Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
        rsInsertTR =Stmt.executeQuery("SELECT * FROM D_FIN_TR_MAPPING");
        rsInsertTR.moveToInsertRow();
        rsInsertTR.updateLong("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
        rsInsertTR.updateLong("SR_NO",srno+1);
        rsInsertTR.updateString("ITEM_CODE",txtItemCode.getText().trim());
        rsInsertTR.updateString("MAIN_ACCOUNT_CODE",txtMainAccountCode.getText().trim());
        rsInsertTR.updateString("CHANGED",Integer.toString(1));
        rsInsertTR.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        rsInsertTR.updateString("CREATED_BY","");
        rsInsertTR.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        rsInsertTR.updateString("MODIFIED_BY","");
        rsInsertTR.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        rsInsertTR.insertRow();
        }
        catch(Exception e){
        e.printStackTrace();
        }
        FormatGrid();
        GenerateGrid();
    }//GEN-LAST:event_cmdAddActionPerformed
    
    
    private void FormatGrid()
    {
        TableT.removeAll();
        TableModelT = new EITLTableModel();
        TableT.setModel(TableModelT);
        TableModelT.addColumn("Item Code");
        TableModelT.addColumn("Main A/C Code");
        TableModelT.SetVariable(0,"Item Code");
        TableModelT.SetVariable(1,"Main A/C Code");        
    }
    
    private void GenerateGrid()
    { try{
      int noOfRow = 0;
      ResultSet rsTR;      
      rsTR =data.getResult("SELECT ITEM_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_TR_MAPPING",FinanceGlobal.FinURL);
      rsTR.last();
      noOfRow = rsTR.getRow();
      rsTR.first();
      Object[] rowdata = new Object[noOfRow];
      for(int i=0;i<noOfRow;i++)
      {
         TableModelT.addRow(rowdata) ;
         TableModelT.setValueByVariable("Item Code",rsTR.getString("ITEM_CODE"),i);
         TableModelT.setValueByVariable("Main A/C Code",rsTR.getString("MAIN_ACCOUNT_CODE"),i);
         rsTR.next();
      }
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableT;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtMainAccountCode;
    // End of variables declaration//GEN-END:variables
    
}