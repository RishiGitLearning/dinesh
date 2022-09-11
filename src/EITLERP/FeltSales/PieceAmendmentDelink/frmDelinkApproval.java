/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PieceAmendmentDelink;

import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import EITLERP.*;
import java.sql.Connection;
import javax.swing.JOptionPane;
/**
 *
 * @author root
 */
public class frmDelinkApproval extends javax.swing.JApplet {

    
     private EITLTableModel DataModel;
     private EITLTableModel DataModelA;
     private EITLTableModel DataModelR;
    /**
     * Initializes the applet frmDelinkApproval
     */
    @Override
    public void init() {
        initComponents();
        setSize(660,484);
        FormatGrid();
        getDatafromTable();
        getDataApproved();
        getDataRejected();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        btnDelink = new javax.swing.JButton();
        btnRejectDelink = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableApproved = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableRejected = new javax.swing.JTable();

        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

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

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 640, 350);

        btnDelink.setText("DELINK");
        btnDelink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelinkActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelink);
        btnDelink.setBounds(0, 350, 150, 29);

        btnRejectDelink.setText("REJECT FROM LIST");
        btnRejectDelink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectDelinkActionPerformed(evt);
            }
        });
        jPanel1.add(btnRejectDelink);
        btnRejectDelink.setBounds(490, 350, 150, 29);

        jTabbedPane1.addTab("Pending Delink Approval", jPanel1);

        jPanel3.setLayout(null);

        TableApproved.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TableApproved);

        jPanel3.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 640, 430);

        jTabbedPane1.addTab("Delink Approved", jPanel3);

        TableRejected.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableRejected);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 648, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 4, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 4, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 3, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 4, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Delink Rejected", jPanel2);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(2, 2, 660, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDelinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelinkActionPerformed
        // TODO add your handling code here:
        if(EITLERPGLOBAL.gNewUserID!=243 && EITLERPGLOBAL.gNewUserID !=262)
        {
            JOptionPane.showMessageDialog(this, "Not allowed to delink! Only Aditya sir can DELINK");
            return;
        }
        int flag_1 = JOptionPane.showConfirmDialog(this, "Are you sure, You want to execute DELINK Process?");
        if(flag_1==0)
        {
                try{
                        for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                                if (!DataModel.getValueAt(i, 1).toString().equals("")) {

                                    boolean flag =(boolean) DataModel.getValueAt(i, 0);
                                    if(flag)
                                    {
                                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_DELINK='OBSOLETE',PR_DELINK_REASON='WIP MM-UPDATION' WHERE PR_PIECE_NO='"+DataModel.getValueAt(i, 1).toString()+"'");
                                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMEND_DELINK SET APPROVAL_STATUS='A',APPROVED_BY='"+EITLERPGLOBAL.gNewUserID+"',APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateTimeDB()+"' WHERE PIECE_NO='"+DataModel.getValueAt(i, 1).toString()+"' AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(DataModel.getValueAt(i, 4).toString())+"'");
                                    }

                                }
                        }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                FormatGrid();
                getDatafromTable();
                getDataApproved();
                getDataRejected();
        }
    }//GEN-LAST:event_btnDelinkActionPerformed

    private void btnRejectDelinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectDelinkActionPerformed
        // TODO add your handling code here:
        if(EITLERPGLOBAL.gNewUserID!=243 && EITLERPGLOBAL.gNewUserID !=262)
        {
            JOptionPane.showMessageDialog(this, "Not allowed to reject delink! Only Aditya sir can REJECT DELINK");
            return;
        }
        int flag_1 = JOptionPane.showConfirmDialog(this, "Are you sure, You want to execute Reject Process?");
        if(flag_1==0)
        {
                for (int i = 0; i <= Table.getRowCount() - 1; i++) {
                        if (!DataModel.getValueAt(i, 1).toString().equals("")) {

                            boolean flag =(boolean) DataModel.getValueAt(i, 0);
                            if(flag)
                            {
                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_AMEND_DELINK SET APPROVAL_STATUS='R',APPROVED_BY='"+EITLERPGLOBAL.gNewUserID+"',APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateTimeDB()+"' WHERE PIECE_NO='"+DataModel.getValueAt(i, 1).toString()+"' AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(DataModel.getValueAt(i, 1).toString())+"'");
                            }

                        }
                }
                FormatGrid();
                getDatafromTable();
                getDataApproved();
                getDataRejected();
        }
    }//GEN-LAST:event_btnRejectDelinkActionPerformed
    
    private void getDatafromTable()
    {
        
        try{
            ResultSet  resultSet;
            Connection connection = data.getConn();
            Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_DELINK where APPROVAL_STATUS='P'");

            while(resultSet.next()) 
            {
                Object[] rowData = new Object[6];
                
                rowData[0] = false;
                rowData[1] = resultSet.getString("PIECE_NO"); 
                String PartyCode = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+resultSet.getString("PIECE_NO")+"'");
                rowData[2] = PartyCode; 
                rowData[3] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PartyCode); 
                rowData[4] = EITLERPGLOBAL.formatDate(resultSet.getString("DOC_DATE"));
                rowData[5] = resultSet.getString("DELINK_FROM"); 

                DataModel.addRow(rowData);   
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void getDataApproved()
    {
        try{
            ResultSet  resultSet;
            Connection connection = data.getConn();
            Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_DELINK where APPROVAL_STATUS='A'");

            while(resultSet.next()) 
            {
                Object[] rowData = new Object[6];
                
                rowData[0] = resultSet.getString("PIECE_NO"); 
                String PartyCode = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+resultSet.getString("PIECE_NO")+"'");
                rowData[1] = PartyCode; 
                rowData[2] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PartyCode); 
                rowData[3] = EITLERPGLOBAL.formatDate(resultSet.getString("DOC_DATE"));
                
                DataModelA.addRow(rowData);   
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void getDataRejected()
    {
        try{
            ResultSet  resultSet;
            Connection connection = data.getConn();
            Statement statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_AMEND_DELINK where APPROVAL_STATUS='R'");

            while(resultSet.next()) 
            {
                Object[] rowData = new Object[6];
                
                rowData[0] = resultSet.getString("PIECE_NO"); 
                String PartyCode = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='"+resultSet.getString("PIECE_NO")+"'");
                rowData[1] = PartyCode; 
                rowData[2] = clsSales_Party.getPartyName(EITLERPGLOBAL.gCompanyID, PartyCode); 
                rowData[3] = EITLERPGLOBAL.formatDate(resultSet.getString("DOC_DATE"));
                
                DataModelR.addRow(rowData);   
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void FormatGrid() {
        try {

            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            

            DataModel.addColumn("SELECTED"); //0 - Read Only
            DataModel.addColumn("PIECE NO"); //1 - 
            DataModel.addColumn("PARTY CODE"); //2 - 
            DataModel.addColumn("PARTY NAME"); //3 - 
            DataModel.addColumn("RECIEVED DATE"); //4 - 
            DataModel.addColumn("DELINK FROM"); //5 -
            

            DataModel.SetVariable(0, "SELECTED");
            DataModel.SetVariable(1, "PIECE_NO");
            DataModel.SetVariable(2, "PARTY_CODE");
            DataModel.SetVariable(3, "PARTY_NAME");
            DataModel.SetVariable(4, "RECIEVED_DATE");
            DataModel.SetVariable(5, "DELINK_FROM");
            

            EITLTableCellRenderer Renderer1 = new EITLTableCellRenderer();
            int ImportCol2 = 0;
            Renderer1.setCustomComponent(ImportCol2, "CheckBox");
            JCheckBox aCheckBox2 = new JCheckBox();
            aCheckBox2.setVisible(true);
            aCheckBox2.setEnabled(true);
            aCheckBox2.setSelected(false);
            Table.getColumnModel().getColumn(ImportCol2).setCellEditor(new DefaultCellEditor(aCheckBox2));
            Table.getColumnModel().getColumn(ImportCol2).setCellRenderer(Renderer1);
            
            //DataModel.SetReadOnly(0);
            Table.getColumnModel().getColumn(0).setMinWidth(20);
            Table.getColumnModel().getColumn(1).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMinWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(150);
            Table.getColumnModel().getColumn(4).setMinWidth(100);
            Table.getColumnModel().getColumn(5).setMinWidth(100);
            
            
            
            DataModelA = new EITLTableModel();
            TableApproved.removeAll();

            TableApproved.setModel(DataModelA);
            
            DataModelA.addColumn("PIECE NO"); //1 - 
            DataModelA.addColumn("PARTY CODE"); //2 - 
            DataModelA.addColumn("PARTY NAME"); //3 - 
            DataModelA.addColumn("RECIEVED DATE"); //4 - 
            
            
            DataModelA.SetVariable(1, "PIECE_NO");
            DataModelA.SetVariable(2, "PARTY_CODE");
            DataModelA.SetVariable(3, "PARTY_NAME");
            DataModelA.SetVariable(4, "RECIEVED_DATE");
            
            
            
            
            
            DataModelR = new EITLTableModel();
            TableRejected.removeAll();

            TableRejected.setModel(DataModelR);
            
            DataModelR.addColumn("PIECE NO"); //1 - 
            DataModelR.addColumn("PARTY CODE"); //2 - 
            DataModelR.addColumn("PARTY NAME"); //3 - 
            DataModelR.addColumn("RECIEVED DATE"); //4 - 
            
            DataModelR.SetVariable(1, "PIECE_NO");
            DataModelR.SetVariable(2, "PARTY_CODE");
            DataModelR.SetVariable(3, "PARTY_NAME");
            DataModelR.SetVariable(4, "RECIEVED_DATE");
            DataModelR.SetVariable(5, "DELINK_FROM");
            
        } catch (Exception e) {

            System.out.println("Error in FormateGrid : " + e.getMessage());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JTable TableApproved;
    private javax.swing.JTable TableRejected;
    private javax.swing.JButton btnDelink;
    private javax.swing.JButton btnRejectDelink;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
