/*
 * frmInquiry.java
 *
 * Created on June 05, 2004, 11:58 AM
 */

package EITLERP.Stores;

/**
 *
 * @author  Prathmesh Shah
 */
/*<APPLET CODE=SelectIndentNo.Class HEIGHT=400 WIDTH=700></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Purchase.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.sql.*;
import java.net.*;

public class SelectIndentNo extends javax.swing.JApplet {
    
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    public boolean Cancelled=true;
    public HashMap colSelItems=new HashMap();
    private JDialog aDialog;
    //public int ModuleID=0;
    private String SelINQNo="";
    
    public boolean CopyHeader=true;
    public clsPOGen ObjPO;
    
    public String SelIndentNo;
    public int SelIndentSrNo;
    public int SelPOType;
    public int SelPODept;
    public String ItemID="";
    
    private EITLComboModel cmbDeptModel;
    private int SelDeptID=0;
    
    public SelectIndentNo() {
        setSize(692,375);
        initComponents();
        FormatGrid();
        GenerateGrid();
    }
    
    public void init() {
        setSize(692,375);
        initComponents();
        FormatGrid();
        GenerateGrid();
    }
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.TableReadOnly(true);
        
        //Add Columns to it
        DataModel.addColumn("Indent No.");
        DataModel.addColumn("Indent Date");
        DataModel.addColumn("Indent Sr.");
        DataModel.addColumn("Item Code");
        DataModel.addColumn("Item Name");
        DataModel.addColumn("Qty");
        DataModel.addColumn("Department");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtIndentNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Select the Indent");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 115, 15);

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
        jScrollPane1.setBounds(9, 91, 660, 218);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(508, 317, 78, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(592, 317, 79, 25);

        jLabel2.setText("Indent No.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 60, 76, 15);

        txtIndentNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIndentNoKeyTyped(evt);
            }
        });

        getContentPane().add(txtIndentNo);
        txtIndentNo.setBounds(88, 59, 134, 20);

    }//GEN-END:initComponents
    
    private void txtIndentNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIndentNoKeyTyped
        // TODO add your handling code here:
        SearchRow(evt.getKeyChar());
    }//GEN-LAST:event_txtIndentNoKeyTyped
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        SelIndentNo="";
        SelIndentSrNo=0;
        SelPOType=0;
        
        Cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        SelIndentNo="";
        SelIndentSrNo=0;
        if(Table.getRowCount()<=0) {
            Cancelled=true;
        }
        else {
            SetList();
            Cancelled=false;
        }
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtIndentNo;
    // End of variables declaration//GEN-END:variables
    
    private void SetList() {
        
        if(Table.getSelectedRow()>=0) {
            SelIndentNo=(String)Table.getValueAt(Table.getSelectedRow(),0);
            SelIndentSrNo=Integer.parseInt((String)Table.getValueAt(Table.getSelectedRow(),2));
        }
        else {
            SelIndentNo="";
            SelIndentSrNo=0;
            SelPOType=0;
            SelDeptID=0;
        }
    }
    
    private void GenerateGrid() {
        
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL;
        
        try {
            
            strSQL=" SELECT A.INDENT_NO,A.INDENT_DATE,SR_NO,ITEM_CODE,ITEM_DESCRIPTION,DEPT_DESC,QTY ";
            strSQL+=" FROM D_INV_INDENT_HEADER A ";
            strSQL+=" LEFT JOIN D_COM_DEPT_MASTER D ON (D.DEPT_ID=A.FOR_DEPT_ID), ";
            strSQL+=" D_INV_INDENT_DETAIL B LEFT JOIN D_INV_ITEM_MASTER I ON (B.ITEM_CODE=I.ITEM_ID) ";
            strSQL+=" WHERE A.INDENT_NO=B.INDENT_NO AND A.APPROVED=1 AND A.CANCELED=0 ";
            
            
            if(!ItemID.trim().equals("")) {
                
                strSQL+=" AND B.ITEM_CODE='"+ItemID+"'";
            }
            
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            FormatGrid();
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Object[] rowData=new Object[7];
                    rowData[0]=rsTmp.getString("INDENT_NO");
                    rowData[1]=EITLERPGLOBAL.formatDate(rsTmp.getString("INDENT_DATE"));
                    rowData[2]=rsTmp.getString("SR_NO");
                    rowData[3]=rsTmp.getString("ITEM_CODE");
                    rowData[4]=rsTmp.getString("ITEM_DESCRIPTION");
                    rowData[5]=Double.toString(rsTmp.getDouble("QTY"));
                    rowData[6]=rsTmp.getString("DEPT_DESC");
                    DataModel.addRow(rowData);
                    
                    rsTmp.next();
                }
                
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            
        }
        catch(Exception e) {
            
        }
    }
    
    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    public boolean ShowList() {
        try {
            FormatGrid();
            GenerateGrid();
            
            setSize(700,430);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select Indent No.",true);
            
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
        return !Cancelled;
    }
    
    
    private void SearchRow(char RecentKey) {
        //All Columns will have String Data
        String txtData=txtIndentNo.getText()+RecentKey;
        String txtTableData="";
        
        int Rows=Table.getRowCount();
        if(txtData.equals("")) {
            Table.changeSelection(0,0, false, false);
            return;
        }
        
        //Loop through Table
        for(int i=0;i<Rows;i++) {
            //Read the table data
            txtTableData=(String) Table.getValueAt(i ,0);
            
            //Compare with partial search
            if(txtData.length()>txtTableData.length()) {
            }
            else {
                if(txtTableData.substring(0,txtData.length()).equals(txtData)) {
                    //Move the row pointer to selected row
                    int row = i;
                    int col = 1;
                    boolean toggle = false;
                    boolean extend = false;
                    Table.changeSelection(row, col, toggle, extend);
                    
                    //Exit the loop
                    i=Table.getModel().getRowCount();
                }
            }
        }
    }
    
    
}
