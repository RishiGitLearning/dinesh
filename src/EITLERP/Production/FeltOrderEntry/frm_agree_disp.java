/*
 * frmItemMapping.java
 *
 * Created on June 23, 2005, 12:20 PM
 */

package EITLERP.Production.FeltOrderEntry;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.UtilFunctions;
import java.sql.*;


public class frm_agree_disp extends javax.swing.JApplet {
    
    private EITLTableModel DataModel=new EITLTableModel();
    
    /** Initializes the applet frmItemMapping */
    public void init() {
        setSize(665,440);
        initComponents();
        FormatGrid();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdGenerate = new javax.swing.JButton();
        txtPieceNo = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setText("Changes in Requested, Committed ,Agreed Date Tracker");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(7, 8, 450, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 2, 679, 32);

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
        jScrollPane1.setBounds(5, 133, 643, 266);

        cmdGenerate.setText("Generate the list");
        cmdGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGenerateActionPerformed(evt);
            }
        });

        getContentPane().add(cmdGenerate);
        cmdGenerate.setBounds(10, 96, 187, 25);

        getContentPane().add(txtPieceNo);
        txtPieceNo.setBounds(20, 50, 150, 19);

        lblStatus.setText("jLabel2");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(230, 100, 120, 15);

    }//GEN-END:initComponents

    private void cmdGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGenerateActionPerformed
        // TODO add your handling code here:
         new Thread() {            
            public void run() {
                try {
                    String strSQL="SELECT AGD_PIECE_NO,AGD_REQ_DATE,AGD_COMM_DATE,AGD_AGREED_DATE,AGD_UPD_DATE FROM PRODUCTION.TMP_AGREED_DATE ";
                    strSQL+="";  
                    if(!txtPieceNo.getText().trim().equals("")) {
                        strSQL+="WHERE AGD_PIECE_NO='"+txtPieceNo.getText().trim()+"' ";
                    }
                   strSQL+=" ORDER BY AGD_UPD_DATE";
                   lblStatus.setText("Fetching Records ... ");
                    System.out.println(strSQL);
                    ResultSet rsTmp=data.getResult(strSQL);
                    rsTmp.first();
                    
                    FormatGrid();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {                                                        
                            Object[] rowData=new Object[10];
                            DataModel.addRow(rowData);                            
                            int NewRow=Table.getRowCount()-1;
                            lblStatus.setText("Generating Table "+NewRow);
                            DataModel.setValueByVariable("SR_NO",Integer.toString(NewRow+1), NewRow);
                            DataModel.setValueByVariable("AGD_PIECE_NO",UtilFunctions.getString(rsTmp,"AGD_PIECE_NO",""),NewRow);
                            DataModel.setValueByVariable("AGD_REQ_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"AGD_REQ_DATE","0000-00-00")),NewRow);
                            DataModel.setValueByVariable("AGD_COMM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"AGD_COMM_DATE","0000-00-00")),NewRow);
                            DataModel.setValueByVariable("AGD_AGREED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"AGD_AGREED_DATE","0000-00-00")),NewRow);                            
                            DataModel.setValueByVariable("AGD_UPD_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"AGD_UPD_DATE","0000-00-00"))+ UtilFunctions.getString(rsTmp,"AGD_UPD_DATE","0000-00-00").substring(10,16),NewRow);                            
                            rsTmp.next();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
        
   

        
        
        

    }//GEN-LAST:event_cmdGenerateActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
                    
        public void FindEx(int pCompanyID,String pPartyCode,String pPieceNo) {
        System.out.println(pPartyCode);
        System.out.println(pPieceNo);
        txtPieceNo.setText(pPieceNo);
    //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        //ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
      //  ObjSalesParty.MoveFirst();
       // DisplayData();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdGenerate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtPieceNo;
    // End of variables declaration//GEN-END:variables
    
    private void FormatGrid()  {
        try {
            DataModel=new EITLTableModel();
            Table.removeAll();
            
            Table.setModel(DataModel);
         //   Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DataModel.addColumn("SR."); //0
        DataModel.addColumn("PIECE_NO"); //1
        DataModel.addColumn("REQ. DATE"); //2
        DataModel.addColumn("COMMITED DATE"); //3
        DataModel.addColumn("AGREED DATE"); //4
        DataModel.addColumn("UPDATE DATE");//5
        
        
         DataModel.SetVariable(0,""); //0 - Read Only
         DataModel.SetVariable(1,"AGD_PIECE_NO"); //0 - Read Only
         DataModel.SetVariable(2,"AGD_REQ_DATE"); //0 - Read Only
         DataModel.SetVariable(3,"AGD_COMM_DATE"); //0 - Read Only
         DataModel.SetVariable(4,"AGD_AGREED_DATE"); //0 - Read Only
         DataModel.SetVariable(5,"AGD_UPD_DATE"); //0 - Read Only

       DataModel.TableReadOnly(false);
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
   
            Table.getColumnModel().getColumn(0).setMaxWidth(30);
            Table.getColumnModel().getColumn(1).setMaxWidth(100);
            Table.getColumnModel().getColumn(2).setMinWidth(90);
            Table.getColumnModel().getColumn(2).setMaxWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(90);
            Table.getColumnModel().getColumn(3).setMaxWidth(100);
          //  Table.getColumnModel().getColumn(2).setMinWidth(90);
        //    Table.getColumnModel().getColumn(3).setMinWidth(90);
          //  Table.getColumnModel().getColumn(4).setMaxWidth(90);
          //  Table.getColumnModel().getColumn(5).setMaxWidth(90);
        } 
    
    catch(Exception e) 
    {
            e.printStackTrace();
        }
        //Table formatting completed
    }
    
   
    
    
}
