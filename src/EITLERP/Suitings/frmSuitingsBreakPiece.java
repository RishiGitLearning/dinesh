/*
 * frmSuitingsComboPackets.java
 *
 * Created on March 1, 2013, 2:16 PM
 */

package EITLERP.Suitings;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement.*;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.CellView;
import jxl.write.Label;
import jxl.read.biff.BiffException;

import EITLERP.data;
import EITLERP.EITLTableModel;
import EITLERP.AppletFrame;
import EITLERP.*;
import java.text.DecimalFormat;
import java.lang.Double;
import java.lang.Thread;
import java.util.HashMap;
import sun.misc.Version;

/**
 * @author  ASHUTOSH
 */
/*<APPLET CODE="frmSuitingsComboPackets.class" width=985 height=685></APPLET> */

public class frmSuitingsBreakPiece extends javax.swing.JApplet {
    
    Connection  connection;
    Statement statement;
    ResultSet resultSet;
    String query, brand;
    DecimalFormat df = new DecimalFormat("##.##");
    Integer qualityNo, shadeNo, noOfPieces, noOfPackets;
    
    
    private EITLTableModel DataModelABDDetail = new EITLTableModel();
    private EITLTableModel DataModelABDInvoice = new EITLTableModel();
    private EITLTableModel DataModelABDInvoice1 = new EITLTableModel();
    
    
    
    private EITLComboModel cmbPartyCodeModel = new EITLComboModel();
    private EITLComboModel cmbQualityIDModel = new EITLComboModel();
    private EITLComboModel cmbShadeModel=new EITLComboModel();
    
    public void init() {
        System.gc();
        setSize(800,600);
        initComponents();
        setLocation(0, 0);
  //      generateABD();
        generateABD();
      //  generateABDODD();
      // generateBonanza();
      runBreakPieceProcess();
        //formatGridABDInvoiceTable1();
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabelTitle = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableABDDetail = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableABDInvoice = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPieceTotal = new javax.swing.JTextField();
        txtInvoicerows = new javax.swing.JTextField();
        txtbreakpiecerows = new javax.swing.JTextField();
        cmbPartyCode = new javax.swing.JComboBox();
        cmbQualityId = new javax.swing.JComboBox();
        cmbShade = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        setName("");
        jLabelTitle.setBackground(new java.awt.Color(0, 102, 153));
        jLabelTitle.setForeground(java.awt.Color.white);
        jLabelTitle.setText(" SUITINGS BREAK PIECES\t");
        jLabelTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelTitle.setOpaque(true);
        getContentPane().add(jLabelTitle);
        jLabelTitle.setBounds(0, 0, 970, 30);

        jLabelStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelStatus.setForeground(new java.awt.Color(51, 51, 255));
        jLabelStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabelStatus);
        jLabelStatus.setBounds(470, 40, 480, 20);

        jButton1.setText("ShadeWiseBreak");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(10, 40, 120, 25);

        jPanel1.setLayout(null);

        jTableABDDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableABDDetail.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTableABDDetail);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 920, 150);

        jTableABDInvoice.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableABDInvoice.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTableABDInvoice);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(10, 220, 410, 240);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(jTable1);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(460, 220, 470, 240);

        jLabel1.setText("ABD Detail");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 120, 15);

        jLabel2.setText("ABD Invoice");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 200, 120, 15);

        jPanel1.add(txtPieceTotal);
        txtPieceTotal.setBounds(329, 470, 90, 19);

        jPanel1.add(txtInvoicerows);
        txtInvoicerows.setBounds(10, 470, 100, 19);

        jPanel1.add(txtbreakpiecerows);
        txtbreakpiecerows.setBounds(470, 470, 90, 19);

        jTabbedPane1.addTab("tab1", jPanel1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 100, 950, 530);

        cmbPartyCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPartyCodeActionPerformed(evt);
            }
        });

        getContentPane().add(cmbPartyCode);
        cmbPartyCode.setBounds(10, 70, 160, 24);

        cmbQualityId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbQualityIdActionPerformed(evt);
            }
        });

        getContentPane().add(cmbQualityId);
        cmbQualityId.setBounds(220, 70, 170, 24);

        cmbShade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbShadeActionPerformed(evt);
            }
        });

        getContentPane().add(cmbShade);
        cmbShade.setBounds(440, 70, 150, 24);

        jButton2.setText("Full Process");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(250, 40, 110, 25);

        jButton3.setText("GenerateABD");
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton3);
        jButton3.setBounds(140, 40, 100, 25);

    }//GEN-END:initComponents
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        generateABD();
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //generateABD();
        runBreakPieceProcess();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void cmbShadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbShadeActionPerformed
        if(EITLERPGLOBAL.getComboCode(cmbShade)!=0){
            if(EITLERPGLOBAL.getComboCode(cmbQualityId)!=0){
                if(EITLERPGLOBAL.getComboCode(cmbPartyCode)!=0){
                    generateABDDetailTable();
                    generateABDInvoiceTable();
                    formatGridABDInvoiceTable1();
                    generateBreakPiece();
                    //deletetablerows();
                    //InsertIntoTable();
                }
            }
        }
    }//GEN-LAST:event_cmbShadeActionPerformed
    
    private void cmbQualityIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbQualityIdActionPerformed
        if(EITLERPGLOBAL.getComboCode(cmbQualityId)!=0){
            generateShadeCombo();
        }
    }//GEN-LAST:event_cmbQualityIdActionPerformed
    
    private void cmbPartyCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPartyCodeActionPerformed
        if(EITLERPGLOBAL.getComboCode(cmbPartyCode)!=0){
            generateQualityCombo();
        }
    }//GEN-LAST:event_cmbPartyCodeActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        generatePartyCombo();
        generateLeftPieces();
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
//    public static void main(String[] args) {
//        //AppletFrame.startApplet("EITLERP.SUITINGS.frmSuitingsBreakPiece.java", "Suitings Combo Packets");
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbPartyCode;
    private javax.swing.JComboBox cmbQualityId;
    private javax.swing.JComboBox cmbShade;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableABDDetail;
    private javax.swing.JTable jTableABDInvoice;
    private javax.swing.JTextField txtInvoicerows;
    private javax.swing.JTextField txtPieceTotal;
    private javax.swing.JTextField txtbreakpiecerows;
    // End of variables declaration//GEN-END:variables
    
    private void formatGridABDDetailTable(){
        DataModelABDDetail = new EITLTableModel();
        jTableABDDetail.removeAll();
        jTableABDDetail.setModel(DataModelABDDetail);
        
        DataModelABDDetail.addColumn("Sr.");
        DataModelABDDetail.addColumn("Season Id");
        DataModelABDDetail.addColumn("Lift1 Lift2 Mtr");
        DataModelABDDetail.addColumn("Lift1 Lift2 Mtr L");
        DataModelABDDetail.addColumn("Lift2 Lift3 Mtr");
        DataModelABDDetail.addColumn("Lift2 Lift3 Mtr L");
    }
    
    private void formatGridABDInvoiceTable(){
        DataModelABDInvoice = new EITLTableModel();
        jTableABDInvoice.removeAll();
        jTableABDInvoice.setModel(DataModelABDInvoice);
        
        DataModelABDInvoice.addColumn("Sr.");
        DataModelABDInvoice.addColumn("Season Id");
        DataModelABDInvoice.addColumn("Invoice No");
        DataModelABDInvoice.addColumn("Invoice Date");
        DataModelABDInvoice.addColumn("Party Code");
        DataModelABDInvoice.addColumn("Quality Id");
        DataModelABDInvoice.addColumn("Shade");
        DataModelABDInvoice.addColumn("Piece No");
        DataModelABDInvoice.addColumn("Gross Qty");
        DataModelABDInvoice.addColumn("Net Qty");
        DataModelABDInvoice.addColumn("Flag");
         DataModelABDInvoice.addColumn("Rate");
    }
    
    private void formatGridABDInvoiceTable1(){
        DataModelABDInvoice1 = new EITLTableModel();
        //jTable1.removeAll();
        jTable1.setModel(DataModelABDInvoice1);
        jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
        DataModelABDInvoice1.addColumn("Sr");
        DataModelABDInvoice1.addColumn("Season Id");
        DataModelABDInvoice1.addColumn("Invoice No");
        DataModelABDInvoice1.addColumn("Invoice Date");
        DataModelABDInvoice1.addColumn("Party Code");
        DataModelABDInvoice1.addColumn("Quality Id");
        DataModelABDInvoice1.addColumn("Shade");
        DataModelABDInvoice1.addColumn("Piece No");
        DataModelABDInvoice1.addColumn("Gross Qty");
        DataModelABDInvoice1.addColumn("Net Qty");
        DataModelABDInvoice1.addColumn("Flag");
        DataModelABDInvoice1.addColumn("1_2");
        DataModelABDInvoice1.addColumn("1_2_L");
        DataModelABDInvoice1.addColumn("2_3");
        DataModelABDInvoice1.addColumn("2_3_L");
        DataModelABDInvoice1.addColumn("Rate");
        DataModelABDInvoice1.addColumn("1_2_Amount");
        DataModelABDInvoice1.addColumn("1_2_L_Amount");
        DataModelABDInvoice1.addColumn("2_3_Amount");
        DataModelABDInvoice1.addColumn("2_3_L_Amount");
        DataModelABDInvoice1.addColumn("NetQty After Lift12");
        DataModelABDInvoice1.addColumn("NetQty After Lift12L");
        DataModelABDInvoice1.addColumn("NetQty After Lift23");
        DataModelABDInvoice1.addColumn("NetQty After Lift23L");
    }
    
    private void exportJTableToExcel(JTable table, File file) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("COMBO DATA", 0);
            //sheet.setProtected(true);
            TableModel model = table.getModel();
            
            for (int i = 0; i < model.getColumnCount(); i++) {
                Label column = new Label(i, 0, model.getColumnName(i));
                sheet.addCell(column);
            }
            int j = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                for (j = 0; j < model.getColumnCount(); j++) {
                    Label row = new Label(j, i + 1,
                    model.getValueAt(i, j).toString());
                    sheet.addCell(row);
                }
            }
            workbook.write();
            workbook.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void generateLeftPieces(){
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        String PartyCode;
        tmpConn= data.getConn();
        long Counter=0;
        try {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT DISTINCT ABDD_PARTY_CODE AS PARTY_CODE FROM SALES.TMP_ABD_DETAIL ");
            while(rsTmp.next()) {
                PartyCode=rsTmp.getString("PARTY_CODE");
                //System.out.println(PartyCode);
                
            }
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
    }
    
    private void generatePartyCombo(){
        cmbPartyCodeModel=new EITLComboModel();
        cmbPartyCodeModel.removeAllElements();
        cmbPartyCode.setModel(cmbPartyCodeModel);
        try{
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Party";
            
            cmbPartyCodeModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT DISTINCT ABDD_PARTY_CODE FROM SALES.TMP_ABD_DETAIL");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("ABDD_PARTY_CODE");
                //combodata.strCode=rs.getString("SCHEME_NAME");
                combodata.Text=rs.getString("ABDD_PARTY_CODE");
                
                cmbPartyCodeModel.addElement(combodata);
                rs.next();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateQualityCombo(){
        cmbQualityIDModel=new EITLComboModel();
        cmbQualityIDModel.removeAllElements();
        cmbQualityId.setModel(cmbQualityIDModel);
        try{
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Quality";
            
            cmbQualityIDModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT DISTINCT ABDD_QUALITY_ID FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+EITLERPGLOBAL.getComboCode(cmbPartyCode)+"");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("ABDD_QUALITY_ID");
                //combodata.strCode=rs.getString("SCHEME_NAME");
                combodata.Text=rs.getString("ABDD_QUALITY_ID");
                cmbQualityIDModel.addElement(combodata);
                rs.next();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateQualityCombo(Object element){
        cmbQualityIDModel=new EITLComboModel();
        cmbQualityIDModel.removeAllElements();
        cmbQualityId.setModel(cmbQualityIDModel);
        try{
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Quality";
            cmbQualityIDModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT DISTINCT ABDD_QUALITY_ID FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+element+"");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("ABDD_QUALITY_ID");
                //combodata.strCode=rs.getString("SCHEME_NAME");
                combodata.Text=rs.getString("ABDD_QUALITY_ID");
                cmbQualityIDModel.addElement(combodata);
                rs.next();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateShadeCombo(){
        cmbShadeModel=new EITLComboModel();
        cmbShadeModel.removeAllElements();
        cmbShade.setModel(cmbShadeModel);
        try{
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Shade";
            
            cmbShadeModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT ABDD_SHADE FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+EITLERPGLOBAL.getComboCode(cmbPartyCode)+" AND ABDD_QUALITY_ID="+EITLERPGLOBAL.getComboCode(cmbQualityId)+"");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("ABDD_SHADE");
                //combodata.strCode=rs.getString("SCHEME_NAME");
                combodata.Text=rs.getString("ABDD_SHADE");
                
                cmbShadeModel.addElement(combodata);
                rs.next();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateShadeCombo(Object element,Object element1){
        cmbShadeModel=new EITLComboModel();
        cmbShadeModel.removeAllElements();
        cmbShade.setModel(cmbShadeModel);
        try{
            
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Shade";
            cmbShadeModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT ABDD_SHADE FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+element+" AND ABDD_QUALITY_ID="+element1+"");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("ABDD_SHADE");
                //combodata.strCode=rs.getString("SCHEME_NAME");
                combodata.Text=rs.getString("ABDD_SHADE");
                cmbShadeModel.addElement(combodata);
                //cmbShadeModel.addElement(rs.getString("ABDD_SHADE"));
                rs.next();
            }
            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void generateABDDetailTable(){
        formatGridABDDetailTable();
        try{
            ResultSet rs=data.getResult("SELECT * FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+EITLERPGLOBAL.getComboCode(cmbPartyCode) +" AND ABDD_QUALITY_ID="+EITLERPGLOBAL.getComboCode(cmbQualityId)+" AND ABDD_SHADE="+EITLERPGLOBAL.getComboCode(cmbShade)+"");
            rs.first();
            if(rs.getRow()>0) {
                int cnt1=0;
                while(!rs.isAfterLast()){
                    cnt1++;
                    Object[] rowData=new Object[6];
                    rowData[0]=Integer.toString(cnt1);
                    rowData[1]=rs.getString("ABDD_SEASON_ID");
                    rowData[2]=rs.getString("ABDD_LIFT1_LIFT2_MTR");
                    rowData[3]=rs.getString("ABDD_LIFT1_LIFT2_MTR_L");
                    rowData[4]=rs.getString("ABDD_LIFT2_LIFT3_MTR");
                    rowData[5]=rs.getString("ABDD_LIFT2_LIFT3_MTR_L");
                    DataModelABDDetail.addRow(rowData);
                    rs.next();
                }
            }
            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    private void generateABDDetailTable(Object element,Object element1,Object element2){
        formatGridABDDetailTable();
        try{
            ResultSet rs=data.getResult("SELECT * FROM SALES.TMP_ABD_DETAIL WHERE ABDD_PARTY_CODE="+element+" AND ABDD_QUALITY_ID="+element1+" AND ABDD_SHADE="+element2+"");
            rs.first();
            if(rs.getRow()>0) {
                int cnt1=0;
                while(!rs.isAfterLast()){
                    cnt1++;
                    Object[] rowData=new Object[6];
                    rowData[0]=Integer.toString(cnt1);
                    rowData[1]=rs.getString("ABDD_SEASON_ID");
                    rowData[2]=rs.getString("ABDD_LIFT1_LIFT2_MTR");
                    rowData[3]=rs.getString("ABDD_LIFT1_LIFT2_MTR_L");
                    rowData[4]=rs.getString("ABDD_LIFT2_LIFT3_MTR");
                    rowData[5]=rs.getString("ABDD_LIFT2_LIFT3_MTR_L");
                    DataModelABDDetail.addRow(rowData);
                    rs.next();
                }
            }
            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateABDInvoiceTable(){
        formatGridABDInvoiceTable();
        try{
            ResultSet rs=data.getResult("SELECT * FROM SALES.TMP_ABD_INVOICE WHERE ABDI_PARTY_CODE="+EITLERPGLOBAL.getComboCode(cmbPartyCode) +" AND ABDI_QLTY_ID="+EITLERPGLOBAL.getComboCode(cmbQualityId)+" AND ABDI_SHADE="+EITLERPGLOBAL.getComboCode(cmbShade)+" ORDER BY ABDI_INVOICE_DATE");                rs.first();
            if(rs.getRow()>0) {
                int cnt=0;
                double invqty = 0.00;
                while(!rs.isAfterLast()){
                    cnt++;
                    invqty += Double.parseDouble(rs.getString("ABDI_GROSS_QTY"));
                    //txttotal.setText(Double.toString(Math.round(sum)));
                    //txttotal.setText(Double.toString(sum));
                    DecimalFormat df = new DecimalFormat("##.##");
                    txtPieceTotal.setText(df.format(invqty));
                    
                    Object[] rowData1=new Object[12];
                    rowData1[0]=Integer.toString(cnt);
                    rowData1[1]=rs.getString("ABDI_SEASON_ID");
                    rowData1[2]=rs.getString("ABDI_INVOICE_NO");
                    rowData1[3]=EITLERPGLOBAL.formatDate(rs.getString("ABDI_INVOICE_DATE"));
                    rowData1[4]=rs.getString("ABDI_PARTY_CODE");
                    rowData1[5]=rs.getString("ABDI_QLTY_ID");
                    rowData1[6]=rs.getString("ABDI_SHADE");
                    rowData1[7]=rs.getString("ABDI_PIECE_NO");
                    rowData1[8]=rs.getString("ABDI_GROSS_QTY");
                    rowData1[9]=rs.getString("ABDI_NET_QTY");
                    rowData1[10]=rs.getString("ABDI_DA_CODE");
                    rowData1[11]=rs.getString("ABDI_RATE");
                    DataModelABDInvoice.addRow(rowData1);
                    rs.next();
                }
            }
            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateABDInvoiceTable(Object element,Object element1,Object element2){
        formatGridABDInvoiceTable();
        try{
            ResultSet rs=data.getResult("SELECT * FROM SALES.TMP_ABD_INVOICE WHERE ABDI_PARTY_CODE="+element+" AND ABDI_QLTY_ID="+element1+" AND ABDI_SHADE="+element2+" ORDER BY ABDI_INVOICE_DATE");
            rs.first();
            if(rs.getRow()>0) {
                int cnt=0;
                double invqty = 0.00;
                while(!rs.isAfterLast()){
                    cnt++;
                    invqty += Double.parseDouble(rs.getString("ABDI_GROSS_QTY"));
                    //txttotal.setText(Double.toString(Math.round(sum)));
                    //txttotal.setText(Double.toString(sum));
                    DecimalFormat df = new DecimalFormat("##.##");
                    txtPieceTotal.setText(df.format(invqty));
                    
                    Object[] rowData1=new Object[12];
                    rowData1[0]=Integer.toString(cnt);
                    rowData1[1]=rs.getString("ABDI_SEASON_ID");
                    rowData1[2]=rs.getString("ABDI_INVOICE_NO");
                    rowData1[3]=EITLERPGLOBAL.formatDate(rs.getString("ABDI_INVOICE_DATE"));
                    rowData1[4]=rs.getString("ABDI_PARTY_CODE");
                    rowData1[5]=rs.getString("ABDI_QLTY_ID");
                    rowData1[6]=rs.getString("ABDI_SHADE");
                    rowData1[7]=rs.getString("ABDI_PIECE_NO");
                    rowData1[8]=rs.getString("ABDI_GROSS_QTY");
                    rowData1[9]=rs.getString("ABDI_NET_QTY");
                    rowData1[10]=rs.getString("ABDI_DA_CODE");
                    rowData1[11]=rs.getString("ABDI_RATE");
                    DataModelABDInvoice.addRow(rowData1);
                    rs.next();
                }
            }
            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void generateBreakPiece(){
        //formatGridABDInvoiceTable1();
        try{
               /*for(int i=0;i<jTableABDDetail.getRowCount();i++){
                   for(int j=2;j<jTableABDDetail.getColumnCount();j++){
                       String var1=jTableABDDetail.getValueAt(i,j).toString();
                       float f=Float.parseFloat(var1);
                       System.out.println(f);
                       float f2,temp;
                       f2=f;
                       for(int k=0;k<jTableABDInvoice.getRowCount();k++){
                           String var2=jTableABDInvoice.getValueAt(k,4).toString();
                           float f1=Float.parseFloat(var2);
                           System.out.println(f1);
                           if(f2>=f1){
                               f2=f2-f1;
                               System.out.println(f2);
                           }
                           else{
                               break;
                           }
                       }
                   }
             }*/
            /*
            for(int i=0;i<jTableABDDetail.getRowCount();i++){
                   for(int j=2;j<jTableABDDetail.getColumnCount();j++){
                       String var1=jTableABDDetail.getValueAt(i,j).toString();
                       float detail=Float.parseFloat(var1);
                       System.out.println(detail);
                       float f2,temp=0;
                       //f2=f;
                       for(int k=0;k<jTableABDInvoice.getRowCount();k++){
                           String var2=jTableABDInvoice.getValueAt(k,4).toString();
                           float invoice=Float.parseFloat(var2);
                           System.out.println(invoice);
                           temp=temp+invoice;
                           System.out.println(temp);
                           if(detail<=temp){
                               break;
                           }
                       }
                   }
             }
             */
            double temp=0;
            boolean breakloop1=true;
            boolean breakloop2=false;
            boolean breakloop3=false;
            boolean breakloop4=false;
            
            if(Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString())==0.00){
                breakloop1=false;
                breakloop2=true;
            }
            if(Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())==0.00 && Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString())==0.00){
                breakloop1=false;
                breakloop2=false;
                breakloop3=true;
            }
            if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())==0.00 && Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())==0.00 && Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString())==0.00){
                breakloop1=false;
                breakloop2=false;
                breakloop3=false;
                breakloop4=true;
            }
            /*
            if(Float.parseFloat(jTableABDDetail.getValueAt(0,5).toString())==0.00){
               breakloop1=false;
               breakloop2=true;
            }
             */
            for(int k=0;k<jTableABDInvoice.getRowCount();k++){
                String var2=jTableABDInvoice.getValueAt(k,8).toString();
                double invoice=Double.parseDouble(var2);
                System.out.println(invoice);
                temp=invoice+temp;
                String formate = df.format(temp);
                temp = Double.parseDouble(formate);
                //temp=Math.round(temp);
                System.out.println(temp);
                //L2_L3_L
                if(Double.parseDouble(jTableABDDetail.getValueAt(0,5).toString())<=temp && breakloop4==true){
                    //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                    double abd=(temp-Double.parseDouble(jTableABDDetail.getValueAt(0,5).toString()));
                    double breakpiece=temp-invoice;
                    double breakpiece1=Double.parseDouble(jTableABDDetail.getValueAt(0,5).toString())-breakpiece;
                    System.out.println(abd);
                    //float temp1=abd;
                    temp=invoice-breakpiece1;
                    breakloop4=false;
                    Object[] rowData2=new Object[25];
                    rowData2[0]=Integer.toString(k+1);
                    rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                    rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                    rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                    rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                    rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                    rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                    rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                    rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                    rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                    rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                    rowData2[14]=df.format(breakpiece1);
                    //rowData2[5]=Float.toString(temp);
                    rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                    if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                    //rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);
                      rowData2[19]= df.format((breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));  
                      rowData2[23]=df.format((breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));
                    }else{
                      rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);  
                      rowData2[23]=df.format(breakpiece1);
                    }                    
                    DataModelABDInvoice1.addRow(rowData2);
                }else{
                    if(breakloop4==true){
                        Object[] rowData2=new Object[25];
                        rowData2[0]=Integer.toString(k+1);
                        rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                        rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                        rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                        rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                        rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                        rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                        rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                        rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                        rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                        rowData2[14]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                        if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        //rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                        rowData2[19] = df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())); 
                        rowData2[23] = df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));
                        System.out.println(df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())));
                        System.out.println(df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))));
                        }else{
                        rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));    
                        rowData2[23]= df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                        System.out.println(df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())));
                        System.out.println(df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())));
                        }
                        DataModelABDInvoice1.addRow(rowData2);
                    }
                }
                //L2_L3
                if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())<=temp && breakloop3==true){
                    //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                    double abd=(temp-Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString()));
                    double breakpiece=temp-invoice;
                    double breakpiece1=Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())-breakpiece;
                    System.out.println(abd);
                    //float temp1=abd;
                    temp=invoice-breakpiece1;
                    breakloop3=false;
                    breakloop4=true;
                    Object[] rowData2=new Object[25];
                    rowData2[0]=Integer.toString(k+1);
                    rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                    rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                    rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                    rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                    rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                    rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                    rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                    rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                    rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                    rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                    rowData2[13]=df.format(breakpiece1);
                    if(Double.parseDouble(jTableABDDetail.getValueAt(0,5).toString())!=0.00){
                        //if(Double.parseDouble(jTableABDDetail.getValueAt(0,5).toString())!=0.00 && jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        rowData2[14]=df.format(temp);                                              
                        rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*temp);
                        rowData2[23]=df.format(temp);                                                
                    }
                    /*
                    else{
                        rowData2[14]=df.format(temp);                                              
                        rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*(temp-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));
                        rowData2[23]=df.format(temp-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10));                                                
                    }
                    */
                    rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                    if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        if(breakpiece1!=0.0){                        
                      //rowData2[18]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);
                        rowData2[18]=df.format((breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                        rowData2[22]=df.format((breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));                        
                        }
                    } else{
                        rowData2[18]=df.format((breakpiece1)*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                        rowData2[22]=df.format(breakpiece1);                        
                    }
                    /*
                     if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())<(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)){
                        rowData2[18]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);   
                        rowData2[22]=df.format(breakpiece1);
                        rowData2[19]=df.format((temp-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                        rowData2[23]=df.format((temp-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));  
                        
                     }
                    */
                    DataModelABDInvoice1.addRow(rowData2);
                }else{
                    if(breakloop3==true){
                        Object[] rowData2=new Object[25];
                        rowData2[0]=Integer.toString(k+1);
                        rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                        rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                        rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                        rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                        rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                        rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                        rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                        rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                        rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                        rowData2[13]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                        if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){                                                 
                        //rowData2[18]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                          rowData2[18]=df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));  
                          rowData2[22]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10));
                        }else{
                          rowData2[18]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));  
                          rowData2[22]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                        }
                        DataModelABDInvoice1.addRow(rowData2);                               }
                }
                //L1_L2_L
                if(Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())<=temp && breakloop2==true){
                    //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                    double abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,3).toString()));
                    double breakpiece=temp-invoice;
                    breakpiece=Math.round(breakpiece*100.0)/100.0;
                    double breakpiece1=Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())-breakpiece;
                    System.out.println(abd);
                    //float temp1=abd;
                    temp=invoice-breakpiece1;
                    breakloop2=false;
                    breakloop3=true;
                    Object[] rowData2=new Object[25];
                    rowData2[0]=Integer.toString(k+1);
                    rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                    rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                    rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                    rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                    rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                    rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                    rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                    rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                    rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                    rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                    rowData2[12]=df.format(breakpiece1);
                    if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())!=0.00){
                        rowData2[13]=df.format(temp);
                        rowData2[18]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*temp);
                        rowData2[22]=df.format(temp);
                    }
                    else{
                        rowData2[14]=df.format(temp);
                        rowData2[19]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*temp);
                        rowData2[23]=df.format(temp);
                    }
                    rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                    if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                    if(breakpiece1!=0){
                    //rowData2[17]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);
                      rowData2[17]= df.format((breakpiece1-Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                      rowData2[21]=df.format(breakpiece1-Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10);
                    }                    
                    }else{
                      rowData2[17]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);
                      rowData2[21]=df.format(breakpiece1);
                    }
                    if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())==0.00){
                           breakloop3=false;
                           breakloop4=true;
                        } 
                    DataModelABDInvoice1.addRow(rowData2);
                }else{
                    if(breakloop2==true){
                        Object[] rowData2=new Object[25];
                        rowData2[0]=Integer.toString(k+1);
                        rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                        rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                        rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                        rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                        rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                        rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                        rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                        rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                        rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();                        
                        rowData2[12]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                        if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        //rowData2[17]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                          rowData2[17]= df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                          rowData2[21]= df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));
                        }else{
                          rowData2[17]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));  
                          rowData2[21]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                        }
                        DataModelABDInvoice1.addRow(rowData2);
                    }
                }
                //L1_L2
                if(Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString())<=temp && breakloop1==true){
                    //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                    double abd=(temp-Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString()));
                    double breakpiece=temp-invoice;
                    double breakpiece1=Double.parseDouble(jTableABDDetail.getValueAt(0,2).toString())-breakpiece;
                    System.out.println(abd);
                    temp=invoice-breakpiece1;
                    temp=Math.round(temp*100.0)/100.0;
                    breakloop1=false;
                    breakloop2=true;
                    Object[] rowData2=new Object[25];
                    rowData2[0]=Integer.toString(k+1);
                    rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                    rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                    rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                    rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                    rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                    rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();
                    rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                    rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                    rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                    rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                    rowData2[11]=df.format(breakpiece1);
                    if(Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())!=0.00){
                        rowData2[12]=df.format(temp);
                        rowData2[17]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*temp);
                        rowData2[21]=df.format(temp);
                    }
                    else{
                        breakloop2=false;
                        breakloop3=true;
                    }
                    if(Double.parseDouble(jTableABDDetail.getValueAt(0,3).toString())==temp){
                        temp=0;
                        breakloop2=false;                        
                        breakloop3=true;
                        if(Double.parseDouble(jTableABDDetail.getValueAt(0,4).toString())==0.00){
                           breakloop3=false;
                           breakloop4=true;
                        }
                    }
                    rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();                    
                    if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        //rowData2[16]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);                    
                          rowData2[16] = df.format((breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));
                          rowData2[20] = df.format(breakpiece1-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10));
                        }else{
                          rowData2[16] = df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*breakpiece1);
                          rowData2[20] = df.format(breakpiece1);
                        }
                    
                    DataModelABDInvoice1.addRow(rowData2);
                }else{
                    if(breakloop1==true){
                        Object[] rowData2=new Object[25];
                        rowData2[0]=Integer.toString(k+1);
                        rowData2[1]=jTableABDInvoice.getValueAt(k,1).toString();
                        rowData2[2]=jTableABDInvoice.getValueAt(k,2).toString();
                        rowData2[3]=jTableABDInvoice.getValueAt(k,3).toString();
                        rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                        rowData2[5]=jTableABDInvoice.getValueAt(k,5).toString();
                        rowData2[6]=jTableABDInvoice.getValueAt(k,6).toString();                        
                        rowData2[7]=jTableABDInvoice.getValueAt(k,7).toString();
                        rowData2[8]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[9]=jTableABDInvoice.getValueAt(k,9).toString();
                        rowData2[10]=jTableABDInvoice.getValueAt(k,10).toString();
                        rowData2[11]=jTableABDInvoice.getValueAt(k,8).toString();
                        rowData2[15]=jTableABDInvoice.getValueAt(k,11).toString();
                        if(!jTableABDInvoice.getValueAt(k,8).toString().equals(jTableABDInvoice.getValueAt(k,9).toString())){
                        //rowData2[16]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                          rowData2[16] = df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString()));                          
                          rowData2[20] = df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10));
                          System.out.println(df.format((Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10))*Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())));
                          System.out.println(df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())-(Double.parseDouble(jTableABDInvoice.getValueAt(k,10).toString())/10)));
                        }else{
                          rowData2[16]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));    
                          rowData2[20]=df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString()));
                          System.out.println(df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,11).toString())*Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())));
                          System.out.println(df.format(Double.parseDouble(jTableABDInvoice.getValueAt(k,8).toString())));
                        }
                        DataModelABDInvoice1.addRow(rowData2);
                    }
                }
                          /*LOGIC 1 START
                           String var2=jTableABDInvoice.getValueAt(k,4).toString();
                           float invoice=Float.parseFloat(var2);
                           System.out.println(invoice);
                           temp=invoice+temp;
                           System.out.println(temp);
                           //L2_L3_L
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,5).toString())<=temp && breakloop4==true){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,5).toString()));
                             float breakpiece=temp-invoice;
                             float breakpiece1=Float.parseFloat(jTableABDDetail.getValueAt(0,5).toString())-breakpiece;
                             System.out.println(abd);
                             //float temp1=abd;
                             temp=invoice-breakpiece1;
                             breakloop4=false;
                             Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[4]=Float.toString(breakpiece1);
                            //rowData2[5]=Float.toString(temp);
                            DataModelABDInvoice1.addRow(rowData2);
                           }else{
                            if(breakloop4==true){
                            Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[4]=jTableABDInvoice.getValueAt(k,4).toString();
                            DataModelABDInvoice1.addRow(rowData2);
                               }
                           }
                           //L2_L3
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,4).toString())<=temp && breakloop3==true){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,4).toString()));
                             float breakpiece=temp-invoice;
                             float breakpiece1=Float.parseFloat(jTableABDDetail.getValueAt(0,4).toString())-breakpiece;
                             System.out.println(abd);
                             //float temp1=abd;
                             temp=invoice-breakpiece1;
                             breakloop3=false;
                            breakloop4=true;
                             Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[3]=Float.toString(breakpiece1);
                            rowData2[4]=Float.toString(temp);
                            DataModelABDInvoice1.addRow(rowData2);
                           }else{
                            if(breakloop3==true){
                            Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[3]=jTableABDInvoice.getValueAt(k,4).toString();
                            DataModelABDInvoice1.addRow(rowData2);
                               }
                           }
                           //L1_L2_L
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,3).toString())<=temp && breakloop2==true){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,3).toString()));
                             float breakpiece=temp-invoice;
                             float breakpiece1=Float.parseFloat(jTableABDDetail.getValueAt(0,3).toString())-breakpiece;
                             System.out.println(abd);
                             //float temp1=abd;
                             temp=invoice-breakpiece1;
                             breakloop2=false;
                             breakloop3=true;
                             Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[2]=Float.toString(breakpiece1);
                            rowData2[3]=Float.toString(temp);
                            DataModelABDInvoice1.addRow(rowData2);
                           }else{
                            if(breakloop2==true){
                            Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[3]=jTableABDInvoice.getValueAt(k,4).toString();
                            DataModelABDInvoice1.addRow(rowData2);
                               }
                           }
                           //L1_L2
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())<=temp && breakloop1==true){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString()));
                             float breakpiece=temp-invoice;
                            float breakpiece1=Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())-breakpiece;
                             System.out.println(abd);
                             temp=invoice-breakpiece1;
                             breakloop1=false;
                             breakloop2=true;
                             Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[1]=Float.toString(breakpiece1);
                            rowData2[2]=Float.toString(temp);
                            DataModelABDInvoice1.addRow(rowData2);
                           }else{
                            if(breakloop1==true){
                            Object[] rowData2=new Object[6];
                            rowData2[0]=Integer.toString(k+1);
                            rowData2[1]=jTableABDInvoice.getValueAt(k,4).toString();
                            DataModelABDInvoice1.addRow(rowData2);
                               }
                           }
                           LOGIC 1 END*/
                           /*
                           DataModelABDInvoice1.setValueAt(jTableABDInvoice.getValueAt(k,0).toString(), k, 0);
                           DataModelABDInvoice1.setValueAt(var2, k, 1);
                           jTable1.setValueAt(jTableABDInvoice.getValueAt(k,0).toString(), k, 0);
                           jTable1.setValueAt(var2, k, 1);
                            */
                //jTable2.setValueAt(jTable1.getValueAt(0, 1).toString(), 0, 1);
                //jTable2.setValueAt(jTable1.getValueAt(0, 2).toString(), 0, 2);
                           /*
                           while(!breakloop2){
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,3).toString())<=temp){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString()));
                             System.out.println(abd);
                             temp=abd;
                             breakloop1=true;
                           }
                           }
                           while(!breakloop3){
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,4).toString())<=temp){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString()));
                             System.out.println(abd);
                             temp=abd;
                             breakloop1=true;
                           }
                           }
                           while(!breakloop4){
                           if(Float.parseFloat(jTableABDDetail.getValueAt(0,5).toString())<=temp){
                             //double abd=EITLERPGLOBAL.round((temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString())),2);
                             float abd=(temp-Float.parseFloat(jTableABDDetail.getValueAt(0,2).toString()));
                             System.out.println(abd);
                             temp=abd;
                             breakloop1=true;
                           }
                           }*/
            }
            
            /*
            for(int i=0;i<jTableABDInvoice.getRowCount();i++)
            {
               String var2=jTableABDInvoice.getValueAt(i,4).toString();
                           float f1=Float.parseFloat(var2);
                           System.out.println(f1);
                           for(int j=0;j<jTableABDDetail.getRowCount();j++){
                               for(int k=2;k<jTableABDDetail.getColumnCount();k++){
             
                               }
                           }
            }*/
            
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void deletetable(){
        try{
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = "DELETE FROM SALES.TMP_ABD_BREAKPIECE";
            statement.executeUpdate(query);
            jLabelStatus.setText(" All Data Deleted From Table");
            statement.close();
            connection.close();
        }catch(Exception e) {
            e.printStackTrace();
            statement = null;
            connection = null;
        }
    }
    
    private void deletetablerows(){
        
        try{
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = "DELETE FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE="+EITLERPGLOBAL.getComboCode(cmbPartyCode)+" AND ABDI_QLTY_ID="+EITLERPGLOBAL.getComboCode(cmbQualityId)+" AND ABDI_SHADE="+EITLERPGLOBAL.getComboCode(cmbShade)+" ";
            statement.executeUpdate(query);
            jLabelStatus.setText(" All Rows Are Deleted From Table");
            statement.close();
            connection.close();
        }catch(Exception e) {
            e.printStackTrace();
            statement = null;
            connection = null;
        }
    }
    
    private void DeleteTmpSaleSchemeTable(){
        try{
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            query = "DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL";
            statement.executeUpdate(query);
            jLabelStatus.setText(" All Data Deleted From Table");
            statement.close();
            connection.close();
        }catch(Exception e) {
            e.printStackTrace();
            statement = null;
            connection = null;
        }  
    }
    
    private void InsertIntoTable(){
        /*
        for(int row = 0; row<jTable1.getRowCount(); row++){
            String seasonid = (String)jTable1.getValueAt(row, 1);
            String invoiceno = (String)jTable1.getValueAt(row, 2);
            String invoicedate =EITLERPGLOBAL.formatDateDB((String)jTable1.getValueAt(row, 3));
  //  String units[] = unit.split(" ");
  //  int q = Integer.parseInt(units[0]);
  //  String u = units[1];
            String pieceno = (String)jTable1.getValueAt(row, 4);
            float grossqty = Float.parseFloat((String)jTable1.getValueAt(row, 5));
            float netqty = Float.parseFloat((String)jTable1.getValueAt(row, 6));
            String flag = (String)jTable1.getValueAt(row, 7);
            String lift1lift2 = (String)jTable1.getValueAt(row, 8);
            String lift1lift2left = (String)jTable1.getValueAt(row, 9);
            String lift2lift3 = (String)jTable1.getValueAt(row, 10);
            String lift2lift3left = (String)jTable1.getValueAt(row, 11);
            try{
                connection = data.getConn();
                connection.setAutoCommit(false);
                String query = "INSERT INTO SALES.TMP_ABD_BREAKPIECE(ABDI_SEASON_ID,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_PIECE_NO,ABDI_GROSS_QTY,ABDI_NET_QTY,ABDI_DA_CODE,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_MTR_L) "
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
         
                PreparedStatement p = connection.prepareStatement(query);
                p.setString(1, seasonid);
                p.setString(2, invoiceno);
                p.setString(3, invoicedate);
                p.setString(4, pieceno);
                p.setFloat(5, grossqty);
                p.setFloat(6, netqty);
                p.setString(7, flag);
                p.setString(8, lift1lift2);
                p.setString(9, lift1lift2left);
                p.setString(10, lift2lift3);
                p.setString(11, lift2lift3left);
                p.addBatch();
                p.executeBatch();
                connection.commit();
            }
            catch(SQLException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot save. "+ ex);
            }
            finally{
                try{
                    connection.close();
                    connection.close();
                    connection.setAutoCommit(true);
                }
                catch(SQLException ex)
                {
                        //ex.printStackTrace();
                }
            }
        }
         */
        try{
            
            connection = data.getConn();
            connection.setAutoCommit(false);
            String query = "INSERT INTO SALES.TMP_ABD_BREAKPIECE(ABDI_SEASON_ID,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_GROSS_QTY,ABDI_NET_QTY,ABDI_DA_CODE,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_MTR_L,ABDI_RATE,ABDD_LIFT1_LIFT2_MTR_AMOUNT,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,ABDD_LIFT2_LIFT3_MTR_AMOUNT,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,ABDD_NET_QTY_AFTER_LIFT12,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_NET_QTY_AFTER_LIFT23,ABDD_NET_QTY_AFTER_LIFT23L) "
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " ;
            PreparedStatement p = connection.prepareStatement(query);
            for(int row = 0; row<jTable1.getRowCount(); row++){
                p.setString(1, (String)jTable1.getValueAt(row, 1));
                p.setString(2, (String)jTable1.getValueAt(row, 2));
                p.setString(3, EITLERPGLOBAL.formatDateDB((String)jTable1.getValueAt(row, 3)));
                p.setString(4, (String)jTable1.getValueAt(row, 4));
                p.setString(5, (String)jTable1.getValueAt(row, 5));
                p.setString(6, (String)jTable1.getValueAt(row, 6));
                p.setString(7, (String)jTable1.getValueAt(row, 7));
                p.setFloat(8, Float.parseFloat((String)jTable1.getValueAt(row, 8)));
                p.setFloat(9, Float.parseFloat((String)jTable1.getValueAt(row, 9)));
                p.setString(10, (String)jTable1.getValueAt(row, 10));
                p.setString(11, (String)jTable1.getValueAt(row, 11));
                p.setString(12, (String)jTable1.getValueAt(row, 12));
                p.setString(13, (String)jTable1.getValueAt(row, 13));
                p.setString(14, (String)jTable1.getValueAt(row, 14));
                p.setString(15, (String)jTable1.getValueAt(row, 15));
                p.setString(16, (String)jTable1.getValueAt(row, 16));
                p.setString(17, (String)jTable1.getValueAt(row, 17));
                p.setString(18, (String)jTable1.getValueAt(row, 18));
                p.setString(19, (String)jTable1.getValueAt(row, 19));
                p.setString(20, (String)jTable1.getValueAt(row, 20));
                p.setString(21, (String)jTable1.getValueAt(row, 21));
                p.setString(22, (String)jTable1.getValueAt(row, 22));
                p.setString(23, (String)jTable1.getValueAt(row, 23));
                p.addBatch();
                if((row+1)%1000==0){
                    p.executeBatch();
                    connection.commit();
                }
            }
            p.executeBatch();
            connection.commit();
        }
        catch(SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot save. "+ ex);
        }
        finally{
            try{
                connection.close();
                connection.setAutoCommit(true);
            }
            catch(SQLException ex) {
                //ex.printStackTrace();
            }
        }
        try{
           Connection conn=data.getConn();
           Statement stmt=conn.createStatement();
         //  stmt.execute("UPDATE SALES.TMP_ABD_BREAKPIECE SET ABDD_LIFT2_LIFT3_MTR_AMOUNT=79.8,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT=4788,ABDD_NET_QTY_AFTER_LIFT23=0.20,ABDD_NET_QTY_AFTER_LIFT23L=12.00 WHERE ABDD_LIFT2_LIFT3_MTR_AMOUNT=-39.9");             
           conn.close();
        }
        catch(SQLException exx) {
                exx.printStackTrace();
            }
    }
    
    private void runBreakPieceProcessW16(){
        jLabelStatus.setText("Processing ...");
        cmbPartyCode.setEnabled(false);
        cmbQualityId.setEnabled(false);
        cmbShade.setEnabled(false);
        formatGridABDInvoiceTable1();
        new Thread(){
            public void run(){
                generatePartyCombo();
                int rowinvoice=0;
                int Partyitems = cmbPartyCodeModel.getSize();
                System.out.println(Partyitems);
                for(int i=1;i<Partyitems;i++){
                    Object element = cmbPartyCodeModel.getElementAt(i);
                    generateQualityCombo(element);
                    int Qualityitems = cmbQualityIDModel.getSize();
                    System.out.println(element+"-"+Qualityitems);
                    for(int j=1;j<Qualityitems;j++){
                        Object element1=cmbQualityIDModel.getElementAt(j);
                        generateShadeCombo(element,element1);
                        int Shadeitems= cmbShadeModel.getSize();
                        System.out.println(element+"-"+element1+"-"+Shadeitems);
                        for(int k=1;k<Shadeitems;k++){
                            Object element2=cmbShadeModel.getElementAt(k);
                            generateABDDetailTable(element,element1,element2);
                            generateABDInvoiceTable(element,element1,element2);
                            rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                            generateBreakPiece();
                            txtInvoicerows.setText(Long.toString(rowinvoice));
                            txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                        }
                    }
                }
                
                deletetable();
                InsertIntoTable();
                jLabelStatus.setText(" All Data Inserted into Table");
                cmbPartyCode.setEnabled(true);
                cmbQualityId.setEnabled(true);
                cmbShade.setEnabled(true);
                
                //ABD Discount % Start
                DeleteTmpSaleSchemeTable();
                jLabelStatus.setText("Generate Scheme Detail Table Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            
          //  String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
        //    stmt.execute(sql11); 
            
          //  sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
          //  stmt.execute(sql11); 
         
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W16'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W16'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            //-----------3.5%
            String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE != 559901";
            stmt.execute(sql);  
            sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
            stmt.execute(sql);            
            //----------1.25%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE != 559901";          
            stmt.execute(sql);  
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0125),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";           
            stmt.execute(sql);
            //----------2.5%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE != 559901";
            stmt.execute(sql);  
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.025),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
            stmt.execute(sql);
            //----------0.75%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE != 559901";
            stmt.execute(sql);  
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 53,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,0.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0075),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
            stmt.execute(sql);
            //--------delete 0 value net qty
            
            
            sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
            stmt.execute(sql);
            
            //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
            sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
            stmt.execute(sql);
            
           sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
            stmt.execute(sql);
            

            
            
          
           /* 
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
             
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
             */
             /*
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
            
                        
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
            
   
             */
             
             
             
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);
       
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            
            
            */
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
            stmt.execute(sql);   
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
            stmt.execute(sql);   
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
            stmt.execute(sql); 
            
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }     
        jLabelStatus.setText("Generate Scheme Detail Table Complete");
        System.out.println("Scheme Detail Done");
        // ABD Discount % end
                
            //  System.exit(0);                                
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
            }
        }.start();
        //((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }
    
    
    
    
      private void runBreakPieceProcessOLD(){
        jLabelStatus.setText("Processing ...");
        cmbPartyCode.setEnabled(false);
        cmbQualityId.setEnabled(false);
        cmbShade.setEnabled(false);
        formatGridABDInvoiceTable1();
        new Thread(){
            public void run(){
                generatePartyCombo();
                int rowinvoice=0;
                int Partyitems = cmbPartyCodeModel.getSize();
                System.out.println(Partyitems);
                for(int i=1;i<Partyitems;i++){
                    Object element = cmbPartyCodeModel.getElementAt(i);
                    generateQualityCombo(element);
                    int Qualityitems = cmbQualityIDModel.getSize();
                    System.out.println(element+"-"+Qualityitems);
                    for(int j=1;j<Qualityitems;j++){
                        Object element1=cmbQualityIDModel.getElementAt(j);
                        generateShadeCombo(element,element1);
                        int Shadeitems= cmbShadeModel.getSize();
                        System.out.println(element+"-"+element1+"-"+Shadeitems);
                        for(int k=1;k<Shadeitems;k++){
                            Object element2=cmbShadeModel.getElementAt(k);
                            generateABDDetailTable(element,element1,element2);
                            generateABDInvoiceTable(element,element1,element2);
                            rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                            generateBreakPiece();
                            txtInvoicerows.setText(Long.toString(rowinvoice));
                            txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                        }
                    }
                }
                
                deletetable();
                InsertIntoTable();
                jLabelStatus.setText(" All Data Inserted into Table");
                cmbPartyCode.setEnabled(true);
                cmbQualityId.setEnabled(true);
                cmbShade.setEnabled(true);
                
                //ABD Discount % Start
                DeleteTmpSaleSchemeTable();
                jLabelStatus.setText("Generate Scheme Detail Table Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            
          //  String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
        //    stmt.execute(sql11); 
            
          //  sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
          //  stmt.execute(sql11); 
         
            //
         /*   
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'S18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'S18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            
            String sql12 = "UPDATE SALES.TMP_ABD_BREAKPIECE SET ABDD_LIFT2_LIFT3_MTR_L = ABDI_GROSS_QTY, ABDD_NET_QTY_AFTER_LIFT23L = ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10  ,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = (ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10 )*ABDI_RATE WHERE ABDD_NET_QTY_AFTER_LIFT23L < 0";
            stmt.execute(sql12);
        */
            
            //-----------3.5%
            String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
        //    sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
          //  stmt.execute(sql);            
            //----------1.25%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";          
            stmt.execute(sql);  
          //  sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0125),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";           
           // stmt.execute(sql);
            //----------2.5%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
           // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.025),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
            //stmt.execute(sql);
            //----------0.75%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
           // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,0.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0075),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
           // stmt.execute(sql);
            //--------delete 0 value net qty
            
            
           sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
           stmt.execute(sql);
            
            //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
           sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
           stmt.execute(sql);
            
           sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
           stmt.execute(sql);
            

        //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 3.50 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =4.5 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='S18')";
         //  stmt.execute(sql);
          
        
        //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 1.75 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =2.25 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='S18')";
        //   stmt.execute(sql);
        
           
           /* 
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
             
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_ALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
             */
             /*
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
            
                        
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
            
   
             */
             
             
             
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);
       
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            
            
            */
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
            stmt.execute(sql);   
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
            stmt.execute(sql);   
            /*
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 5.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =4.50";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 4.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =3.50";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.75 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =2.25";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.25 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =1.75";
            stmt.execute(sql); 
            
            */
            
          //  sql = "DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_PARTY_CODE IN (159991,509912) ";
          //  stmt.execute(sql); 
            
        //   sql= "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =2.25  WHERE  SCD_INVOICE_NO IN ('SU/003450','SU/003459','SU/003529','SU/003449') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0)  IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'AND SN_CANCELLED =0 AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 279965)  AND SCD_DISC_PERCENT = 4.50";
          //  stmt.execute(sql);

          
            
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/000704','SU/000705','SU/000706','SU/000707','SU/000708','SU/000709','SU/000710','SU/000711','SU/000722','SU/000723') AND SCD_PARTY_CODE IN (311912) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-01-02' AND  SN_ORDER_DATE <='2018-06-30' AND SN_PARTY_CODE = 311912) AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
           stmt.execute(sql);  
  
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/000826','SU/000827','SU/000828','SU/000829') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-01-02' AND  SN_ORDER_DATE <='2018-06-30' AND SN_PARTY_CODE = 311912) AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
           stmt.execute(sql);  
           
           
           
           
           
           
           
          //  sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/003361','SU/003362','SU/003363','SU/003364','SU/003365','SU/003366','SU/003367','SU/003368','SU/003369','SU/003370','SU/003371','SU/003372','SU/003373','SU/003374','SU/003375','SU/003376','SU/003377','SU/003378','SU/003379','SU/003380','SU/003381','SU/003382','SU/003383','SU/003384','SU/003385','SU/003386','SU/003387','SU/003388','SU/003389','SU/003390','SU/003391','SU/003392','SU/003401','SU/003506','SU/003510','SU/003511') AND SCD_PARTY_CODE IN (300153) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU' AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 300153) AND SCD_DISC_PERCENT = 3.50";
          //  stmt.execute(sql);  
            
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE SCD_PIECE_NO IN ('29225/13','12099/09','27252/07','11190/09','10229/08','26464/04','26495/05','29155/05','11931/10','11849/09','26535/07','26639/09','26680/01','26680/02','26680/07','26697/01','26697/02','26697/08','29747/09','29350/05','27211/05','25725/02','25246/04','25497/05','25497/06','25497/07','25497/09','25498/09') AND SCD_PARTY_CODE = 311912 AND SCD_DISC_PERCENT = 3.50";
            stmt.execute(sql); 
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =3.50 WHERE SCD_PIECE_NO IN ('26200/04','26199/06','26200/07','10013/04','26199/01','26202/07','26868/10','29493/11','29494/03','26872/05','12574/01','28224/05','27302/07') AND SCD_PARTY_CODE = 300153 AND SCD_DISC_PERCENT = 1.75";
            stmt.execute(sql); 
            
           */ 
            
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
            stmt.execute(sql); 
            
            
            
            
             stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_BRAND = BRAND_ID WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID= 80 AND SEASON_ID = SCD_SEASON_ID  AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5))") ;


             stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER1= SCD_DISC_PERCENT,SCD_DISC_AMT1 =SCD_DISC_AMOUNT WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID=80");


             stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_DISC_PER2 = 3 ,SCD_DISC_PER1 = SCD_DISC_PERCENT,SCD_DISC_AMT2=3*SCD_NET_AMOUNT/100 WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID= 80 AND SEASON_ID = SCD_SEASON_ID AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5)) AND SCD_PARTY_CODE = 510901 AND BRAND_ID IN ('CON','FAV','GG','TUZ','PR') AND SCD_DISC_PERCENT >0");

             stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER_CAPTION = CASE WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 = 0 THEN CONCAT(SCD_DISC_PER1,' + 0')  WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 > 0 THEN CONCAT(SCD_DISC_PER1,' + 3')  WHEN SCD_DISC_PER1 = 0 AND SCD_DISC_PER2 = 0 THEN '0 + 0' END, SCD_DISC_AMOUNT = (SCD_DISC_AMT1+ SCD_DISC_AMT2), SCD_DISC_PERCENT = (SCD_DISC_PER1+ SCD_DISC_PER2)  WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID=80");


            
            
            
            
            
            
            
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }     
        jLabelStatus.setText("Generate Scheme Detail Table Complete");
        System.out.println("Scheme Detail Done");
        // ABD Discount % end
                
            //  System.exit(0);                                
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
            }
        }.start();
        //((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }
    
     
      
      
      
      
      
      
      private void runBreakPieceProcess(){
          jLabelStatus.setText("Processing ...");
          cmbPartyCode.setEnabled(false);
          cmbQualityId.setEnabled(false);
          cmbShade.setEnabled(false);
          formatGridABDInvoiceTable1();
          new Thread(){
              public void run(){
                  generatePartyCombo();
                  int rowinvoice=0;
                  int Partyitems = cmbPartyCodeModel.getSize();
                  System.out.println(Partyitems);
                  for(int i=1;i<Partyitems;i++){
                      Object element = cmbPartyCodeModel.getElementAt(i);
                      generateQualityCombo(element);
                      int Qualityitems = cmbQualityIDModel.getSize();
                      System.out.println(element+"-"+Qualityitems);
                      for(int j=1;j<Qualityitems;j++){
                          Object element1=cmbQualityIDModel.getElementAt(j);
                          generateShadeCombo(element,element1);
                          int Shadeitems= cmbShadeModel.getSize();
                          System.out.println(element+"-"+element1+"-"+Shadeitems);
                          for(int k=1;k<Shadeitems;k++){
                              Object element2=cmbShadeModel.getElementAt(k);
                              generateABDDetailTable(element,element1,element2);
                              generateABDInvoiceTable(element,element1,element2);
                              rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                              generateBreakPiece();
                              txtInvoicerows.setText(Long.toString(rowinvoice));
                              txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                          }
                      }
                  }
                  
                  deletetable();
                  InsertIntoTable();
                  jLabelStatus.setText(" All Data Inserted into Table");
                  cmbPartyCode.setEnabled(true);
                  cmbQualityId.setEnabled(true);
                  cmbShade.setEnabled(true);
                  
                  //ABD Discount % Start
                  DeleteTmpSaleSchemeTable();
                  jLabelStatus.setText("Generate Scheme Detail Table Start");
                  try{
                      Connection conn=data.getConn();
                      Statement stmt=conn.createStatement();
                      
                      //  String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
                      //    stmt.execute(sql11);
                      
                      //  sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
                      //  stmt.execute(sql11);
                      
                      //
         /*
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11);
          
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11);
          
          
            String sql12 = "UPDATE SALES.TMP_ABD_BREAKPIECE SET ABDD_LIFT2_LIFT3_MTR_L = ABDI_GROSS_QTY, ABDD_NET_QTY_AFTER_LIFT23L = ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10  ,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = (ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10 )*ABDI_RATE WHERE ABDD_NET_QTY_AFTER_LIFT23L < 0";
            stmt.execute(sql12);
          */
                      
                      //-----------3.5%
                      String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      //    sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
                      //  stmt.execute(sql);
                      //----------1.25%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      //  sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0125),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
                      // stmt.execute(sql);
                      //----------2.5%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.025),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
                      //stmt.execute(sql);
                      //----------0.75%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,0.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0075),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
                      // stmt.execute(sql);
                      //--------delete 0 value net qty
                      sql ="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,12 AS ABDD_NET_QTY_AFTER_LIFT23L,(ABDI_RATE * 12)  AS ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND((ABDI_RATE * 12)*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_QLTY_ID = 328381 AND ABDI_PARTY_CODE = 159991 AND ABDI_SHADE = 04 UNION ALL SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,12.5 AS ABDD_NET_QTY_AFTER_LIFT23L,(ABDI_RATE * 12.5)  AS ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND((ABDI_RATE * 12.5)*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_QLTY_ID = 328381 AND ABDI_PARTY_CODE = 159991 AND ABDI_SHADE = 03 UNION ALL SELECT 101,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,'83739/03' AS ABDI_PIECE_NO,'02' AS ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,12.30 AS ABDD_NET_QTY_AFTER_LIFT23L,(ABDI_RATE * 12.30)  AS ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,'SU/003644' AS ABDI_INVOICE_NO,'2018-12-06' AS ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND((ABDI_RATE * 12.30)*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_QLTY_ID = 328381 AND ABDI_PARTY_CODE = 159991 AND ABDI_SHADE = 03";
                      stmt.execute(sql);
                      
                     
                      sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
                      stmt.execute(sql);
                      
                      //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
                      sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
                      stmt.execute(sql);
                      
                      sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
                      stmt.execute(sql);
                      
                      sql = "UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = 3.25 WHERE SCD_PARTY_CODE = 300156 AND SCD_QUALITY_ID = 310816 AND SCD_SHADE =13 AND SCD_PIECE_NO = '12982/53'";
                      stmt.execute(sql);
                   
                      //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 3.50 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =4.5 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='W18')";
                      //  stmt.execute(sql);
                      
                      
                      //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 1.75 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =2.25 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='W18')";
                      //   stmt.execute(sql);
                      
                      
           /*
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
            
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_ALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
            */
             /*
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
              
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
              
              
              */
                      
                      
                      
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
             
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
             
             
             */
                      
                      
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_GATE_PASS_NO ='NEW',SCD_GATEPASS_DATE =NW_ABD_DESP_DATE1 WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W18'   ";
                      stmt.execute(sql);
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
                      stmt.execute(sql);
                      
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
                      stmt.execute(sql);
          
                      
                      /*
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 5.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =4.50";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 4.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =3.50";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.75 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =2.25";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.25 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =1.75";
            stmt.execute(sql);
             
             */
                      
                      //  sql = "DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_PARTY_CODE IN (159991,509912) ";
                      //  stmt.execute(sql);
                      
                      //   sql= "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =2.25  WHERE  SCD_INVOICE_NO IN ('SU/003450','SU/003459','SU/003529','SU/003449') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0)  IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'AND SN_CANCELLED =0 AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 279965)  AND SCD_DISC_PERCENT = 4.50";
                      //  stmt.execute(sql);
                      
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_GATE_PASS_NO !='NEW'  AND SCD_INVOICE_DATE >='2018-08-01' AND SCD_INVOICE_DATE <='2018-12-31' AND SCD_DISC_PERCENT = 4.50";
                      stmt.execute(sql);
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT  = 1.75,SCD_DISC_AMOUNT = ROUND(((1.75 *SCD_NET_AMOUNT)/100),0) WHERE SCD_GATE_PASS_NO !='NEW' AND SCD_INVOICE_DATE >='2018-08-01' AND SCD_INVOICE_DATE <='2018-12-31' AND SCD_DISC_PERCENT = 2.25;";
                      stmt.execute(sql);
                                            
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W18'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO ='NEW' AND SCD_DISC_PERCENT IN (4.50,3.50) ";
                      stmt.execute(sql);
                      
                      
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W18'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO ='NEW' AND SCD_DISC_PERCENT IN (2.25,1.75) ";
                      stmt.execute(sql);
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W18'  AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1 AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE4 AND SCD_GATE_PASS_NO ='NEW' AND SCD_DISC_PERCENT IN (4.50,3.50) ";
                      stmt.execute(sql);
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 1.75,SCD_DISC_AMOUNT = ROUND(((1.75 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W18'  AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE4 AND SCD_GATE_PASS_NO ='NEW' AND SCD_DISC_PERCENT IN (2.25,1.75) ";
                      stmt.execute(sql);
                      
                      
                      
                      
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN  ('SU/003048','SU/002973','SU/002974','SU/002975','SU/002976','SU/002977','SU/002504','SU/002505','SU/002506','SU/002507','SU/002508','SU/002509','SU/002510','SU/002511','SU/002512','SU/002513','SU/001866','SU/001867','SU/001868','SU/001869','SU/001870') AND SCD_PARTY_CODE IN (311912) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-07-01' AND  SN_ORDER_DATE <='2018-12-31' AND SN_PARTY_CODE = 311912 )  AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
                      stmt.execute(sql);
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN  ('SU/002596','SU/002597','SU/002598','SU/002599','SU/002600','SU/002601','SU/003611','SU/003612','SU/003613','SU/003614' ) AND SCD_PARTY_CODE IN (300153) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  ( SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-07-01' AND  SN_ORDER_DATE <='2018-12-31' AND SN_PARTY_CODE = 300153 )  AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
                      stmt.execute(sql);
                      
                      
                      //  sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/003361','SU/003362','SU/003363','SU/003364','SU/003365','SU/003366','SU/003367','SU/003368','SU/003369','SU/003370','SU/003371','SU/003372','SU/003373','SU/003374','SU/003375','SU/003376','SU/003377','SU/003378','SU/003379','SU/003380','SU/003381','SU/003382','SU/003383','SU/003384','SU/003385','SU/003386','SU/003387','SU/003388','SU/003389','SU/003390','SU/003391','SU/003392','SU/003401','SU/003506','SU/003510','SU/003511') AND SCD_PARTY_CODE IN (300153) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU' AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 300153) AND SCD_DISC_PERCENT = 3.50";
                      //  stmt.execute(sql);
                      
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE SCD_PIECE_NO IN ('29225/13','12099/09','27252/07','11190/09','10229/08','26464/04','26495/05','29155/05','11931/10','11849/09','26535/07','26639/09','26680/01','26680/02','26680/07','26697/01','26697/02','26697/08','29747/09','29350/05','27211/05','25725/02','25246/04','25497/05','25497/06','25497/07','25497/09','25498/09') AND SCD_PARTY_CODE = 311912 AND SCD_DISC_PERCENT = 3.50";
            stmt.execute(sql);
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =3.50 WHERE SCD_PIECE_NO IN ('26200/04','26199/06','26200/07','10013/04','26199/01','26202/07','26868/10','29493/11','29494/03','26872/05','12574/01','28224/05','27302/07') AND SCD_PARTY_CODE = 300153 AND SCD_DISC_PERCENT = 1.75";
            stmt.execute(sql);
             
             */
                      
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
                      stmt.execute(sql);
                      
                      
                      
                      
                      stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_BRAND = BRAND_ID WHERE SCD_SEASON_ID='W18' AND SCD_SCHEME_ID= 101 AND SEASON_ID = SCD_SEASON_ID  AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5))") ;
                                           
                      stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER1= SCD_DISC_PERCENT,SCD_DISC_AMT1 =SCD_DISC_AMOUNT WHERE SCD_SEASON_ID='W18' AND SCD_SCHEME_ID=101");
                                            
                      stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_DISC_PER2 = 3 ,SCD_DISC_PER1 = SCD_DISC_PERCENT,SCD_DISC_AMT2=3*SCD_NET_AMOUNT/100 WHERE SCD_SEASON_ID='W18' AND SCD_SCHEME_ID= 101 AND SEASON_ID = SCD_SEASON_ID AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5)) AND SCD_PARTY_CODE = 510901 AND BRAND_ID IN ('CON','FAV','GG','TUZ','PR') AND SCD_DISC_PERCENT >0");
                      
                      stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER_CAPTION = CASE WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 = 0 THEN CONCAT(SCD_DISC_PER1,' + 0')  WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 > 0 THEN CONCAT(SCD_DISC_PER1,' + 3')  WHEN SCD_DISC_PER1 = 0 AND SCD_DISC_PER2 = 0 THEN '0 + 0' END, SCD_DISC_AMOUNT = (SCD_DISC_AMT1+ SCD_DISC_AMT2), SCD_DISC_PERCENT = (SCD_DISC_PER1+ SCD_DISC_PER2)  WHERE SCD_SEASON_ID='W18' AND SCD_SCHEME_ID=101");
                      
                      
                      
                      
                      
                      
                      
                      
                      
                      
                      
                  }catch(SQLException sqe){
                      sqe.printStackTrace();
                  }
                  jLabelStatus.setText("Generate Scheme Detail Table Complete");
                  System.out.println("Scheme Detail Done");
                  // ABD Discount % end
                  
                  //  System.exit(0);
                  ((JFrame)getParent().getParent().getParent().getParent()).dispose();
              }
          }.start();
          //((JFrame)getParent().getParent().getParent().getParent()).dispose();
      }
      
      
      
          
      
      
      private void runBreakPieceProcess_S18(){
          jLabelStatus.setText("Processing ...");
          cmbPartyCode.setEnabled(false);
          cmbQualityId.setEnabled(false);
          cmbShade.setEnabled(false);
          formatGridABDInvoiceTable1();
          new Thread(){
              public void run(){
                  generatePartyCombo();
                  int rowinvoice=0;
                  int Partyitems = cmbPartyCodeModel.getSize();
                  System.out.println(Partyitems);
                  for(int i=1;i<Partyitems;i++){
                      Object element = cmbPartyCodeModel.getElementAt(i);
                      generateQualityCombo(element);
                      int Qualityitems = cmbQualityIDModel.getSize();
                      System.out.println(element+"-"+Qualityitems);
                      for(int j=1;j<Qualityitems;j++){
                          Object element1=cmbQualityIDModel.getElementAt(j);
                          generateShadeCombo(element,element1);
                          int Shadeitems= cmbShadeModel.getSize();
                          System.out.println(element+"-"+element1+"-"+Shadeitems);
                          for(int k=1;k<Shadeitems;k++){
                              Object element2=cmbShadeModel.getElementAt(k);
                              generateABDDetailTable(element,element1,element2);
                              generateABDInvoiceTable(element,element1,element2);
                              rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                              generateBreakPiece();
                              txtInvoicerows.setText(Long.toString(rowinvoice));
                              txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                          }
                      }
                  }
                  
                  deletetable();
                  InsertIntoTable();
                  jLabelStatus.setText(" All Data Inserted into Table");
                  cmbPartyCode.setEnabled(true);
                  cmbQualityId.setEnabled(true);
                  cmbShade.setEnabled(true);
                  
                  //ABD Discount % Start
                  DeleteTmpSaleSchemeTable();
                  jLabelStatus.setText("Generate Scheme Detail Table Start");
                  try{
                      Connection conn=data.getConn();
                      Statement stmt=conn.createStatement();
                      
                      //  String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
                      //    stmt.execute(sql11);
                      
                      //  sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
                      //  stmt.execute(sql11);
                      
                      //
         /*
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'S18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11);
          
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'S18'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11);
          
          
            String sql12 = "UPDATE SALES.TMP_ABD_BREAKPIECE SET ABDD_LIFT2_LIFT3_MTR_L = ABDI_GROSS_QTY, ABDD_NET_QTY_AFTER_LIFT23L = ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10  ,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = (ABDI_GROSS_QTY - (ABDI_DA_CODE+0)*.10 )*ABDI_RATE WHERE ABDD_NET_QTY_AFTER_LIFT23L < 0";
            stmt.execute(sql12);
          */
                      
                      //-----------3.5%
                      String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 80,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      //    sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
                      //  stmt.execute(sql);
                      //----------1.25%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 80,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      //  sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0125),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
                      // stmt.execute(sql);
                      //----------2.5%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 80,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.025),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
                      //stmt.execute(sql);
                      //----------0.75%
                      sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 80,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
                      stmt.execute(sql);
                      // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,0.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0075),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
                      // stmt.execute(sql);
                      //--------delete 0 value net qty
                      
                      
                      sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
                      stmt.execute(sql);
                      
                      //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
                      sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
                      stmt.execute(sql);
                      
                      sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
                      stmt.execute(sql);
                      
                      
                      //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 3.50 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =4.5 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='S18')";
                      //  stmt.execute(sql);
                      
                      
                      //   sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 1.75 WHERE SCD_INVOICE_DATE >='2018-04-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =2.25 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='S18')";
                      //   stmt.execute(sql);
                      
                      
           /*
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
            
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_ALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
            */
             /*
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
              
              
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
              
              
              */
                      
                      
                      
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
             
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1";
            stmt.execute(sql);
             
             
             
             */
                      
                      
                      
                      
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
                      stmt.execute(sql);
                      
                      sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
                      stmt.execute(sql);
            /*
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 5.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =4.50";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 4.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =3.50";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.75 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =2.25";
            stmt.execute(sql);
             
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.25 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =1.75";
            stmt.execute(sql);
             
             */
                      
                      //  sql = "DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_PARTY_CODE IN (159991,509912) ";
                      //  stmt.execute(sql);
                      
                      //   sql= "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =2.25  WHERE  SCD_INVOICE_NO IN ('SU/003450','SU/003459','SU/003529','SU/003449') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0)  IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'AND SN_CANCELLED =0 AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 279965)  AND SCD_DISC_PERCENT = 4.50";
                      //  stmt.execute(sql);
                      
                      
                      
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/000704','SU/000705','SU/000706','SU/000707','SU/000708','SU/000709','SU/000710','SU/000711','SU/000722','SU/000723') AND SCD_PARTY_CODE IN (311912) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-01-02' AND  SN_ORDER_DATE <='2018-06-30' AND SN_PARTY_CODE = 311912) AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
                      stmt.execute(sql);
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/000826','SU/000827','SU/000828','SU/000829') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE >='2018-01-02' AND  SN_ORDER_DATE <='2018-06-30' AND SN_PARTY_CODE = 311912) AND SCD_DISC_PERCENT IN (4.50,3.50,2.25)";
                      stmt.execute(sql);
                      
                      
                      
                      
                      
                      
                      
                      //  sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/003361','SU/003362','SU/003363','SU/003364','SU/003365','SU/003366','SU/003367','SU/003368','SU/003369','SU/003370','SU/003371','SU/003372','SU/003373','SU/003374','SU/003375','SU/003376','SU/003377','SU/003378','SU/003379','SU/003380','SU/003381','SU/003382','SU/003383','SU/003384','SU/003385','SU/003386','SU/003387','SU/003388','SU/003389','SU/003390','SU/003391','SU/003392','SU/003401','SU/003506','SU/003510','SU/003511') AND SCD_PARTY_CODE IN (300153) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'S18' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU' AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 300153) AND SCD_DISC_PERCENT = 3.50";
                      //  stmt.execute(sql);
                      
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE SCD_PIECE_NO IN ('29225/13','12099/09','27252/07','11190/09','10229/08','26464/04','26495/05','29155/05','11931/10','11849/09','26535/07','26639/09','26680/01','26680/02','26680/07','26697/01','26697/02','26697/08','29747/09','29350/05','27211/05','25725/02','25246/04','25497/05','25497/06','25497/07','25497/09','25498/09') AND SCD_PARTY_CODE = 311912 AND SCD_DISC_PERCENT = 3.50";
            stmt.execute(sql);
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =3.50 WHERE SCD_PIECE_NO IN ('26200/04','26199/06','26200/07','10013/04','26199/01','26202/07','26868/10','29493/11','29494/03','26872/05','12574/01','28224/05','27302/07') AND SCD_PARTY_CODE = 300153 AND SCD_DISC_PERCENT = 1.75";
            stmt.execute(sql);
             
             */
                      
                      
                      sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
                      stmt.execute(sql);
                      
                      
                      
                      
                      stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_BRAND = BRAND_ID WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID= 80 AND SEASON_ID = SCD_SEASON_ID  AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5))") ;
                      
                      
                      stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER1= SCD_DISC_PERCENT,SCD_DISC_AMT1 =SCD_DISC_AMOUNT WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID=80");
                      
                      
                      stmt.execute("UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,DINESHMILLS.D_SAL_QUALITY_MASTER SET SCD_DISC_PER2 = 3 ,SCD_DISC_PER1 = SCD_DISC_PERCENT,SCD_DISC_AMT2=3*SCD_NET_AMOUNT/100 WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID= 80 AND SEASON_ID = SCD_SEASON_ID AND QUALITY_ID = CONCAT(3,SUBSTRING(SCD_QUALITY_ID,2,5)) AND SCD_PARTY_CODE = 510901 AND BRAND_ID IN ('CON','FAV','GG','TUZ','PR') AND SCD_DISC_PERCENT >0");
                      
                      stmt.execute("UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PER_CAPTION = CASE WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 = 0 THEN CONCAT(SCD_DISC_PER1,' + 0')  WHEN SCD_DISC_PER1 > 0 AND SCD_DISC_PER2 > 0 THEN CONCAT(SCD_DISC_PER1,' + 3')  WHEN SCD_DISC_PER1 = 0 AND SCD_DISC_PER2 = 0 THEN '0 + 0' END, SCD_DISC_AMOUNT = (SCD_DISC_AMT1+ SCD_DISC_AMT2), SCD_DISC_PERCENT = (SCD_DISC_PER1+ SCD_DISC_PER2)  WHERE SCD_SEASON_ID='S18' AND SCD_SCHEME_ID=80");
                      
                      
                      
                      
                      
                      
                      
                      
                      
                      
                      
                  }catch(SQLException sqe){
                      sqe.printStackTrace();
                  }
                  jLabelStatus.setText("Generate Scheme Detail Table Complete");
                  System.out.println("Scheme Detail Done");
                  // ABD Discount % end
                  
                  //  System.exit(0);
                  ((JFrame)getParent().getParent().getParent().getParent()).dispose();
              }
          }.start();
          //((JFrame)getParent().getParent().getParent().getParent()).dispose();
      }
    
      
      
      
      
      
      
      
      
      
      
      private void runBreakPieceProcessw17(){
        jLabelStatus.setText("Processing ...");
        cmbPartyCode.setEnabled(false);
        cmbQualityId.setEnabled(false);
        cmbShade.setEnabled(false);
        formatGridABDInvoiceTable1();
        new Thread(){
            public void run(){
                generatePartyCombo();
                int rowinvoice=0;
                int Partyitems = cmbPartyCodeModel.getSize();
                System.out.println(Partyitems);
                for(int i=1;i<Partyitems;i++){
                    Object element = cmbPartyCodeModel.getElementAt(i);
                    generateQualityCombo(element);
                    int Qualityitems = cmbQualityIDModel.getSize();
                    System.out.println(element+"-"+Qualityitems);
                    for(int j=1;j<Qualityitems;j++){
                        Object element1=cmbQualityIDModel.getElementAt(j);
                        generateShadeCombo(element,element1);
                        int Shadeitems= cmbShadeModel.getSize();
                        System.out.println(element+"-"+element1+"-"+Shadeitems);
                        for(int k=1;k<Shadeitems;k++){
                            Object element2=cmbShadeModel.getElementAt(k);
                            generateABDDetailTable(element,element1,element2);
                            generateABDInvoiceTable(element,element1,element2);
                            rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                            generateBreakPiece();
                            txtInvoicerows.setText(Long.toString(rowinvoice));
                            txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                        }
                    }
                }
                
                deletetable();
                InsertIntoTable();
                jLabelStatus.setText(" All Data Inserted into Table");
                cmbPartyCode.setEnabled(true);
                cmbQualityId.setEnabled(true);
                cmbShade.setEnabled(true);
                
                //ABD Discount % Start
                DeleteTmpSaleSchemeTable();
                jLabelStatus.setText("Generate Scheme Detail Table Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            
          //  String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
        //    stmt.execute(sql11); 
            
          //  sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2016-03-01' AND  ABDI_INVOICE_DATE <= '2016-03-31'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'S16') ";
          //  stmt.execute(sql11); 
         
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W17'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W17'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            //-----------3.5%
            String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
        //    sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
          //  stmt.execute(sql);            
            //----------1.25%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";          
            stmt.execute(sql);  
          //  sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0125),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";           
           // stmt.execute(sql);
            //----------2.5%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
           // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.025),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE  WHERE ABDI_PARTY_CODE = 559901";
            //stmt.execute(sql);
            //----------0.75%
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);  
           // sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 71,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,0.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0075),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE WHERE ABDI_PARTY_CODE = 559901";
           // stmt.execute(sql);
            //--------delete 0 value net qty
            
            
            sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
            stmt.execute(sql);
            
            //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
           sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
           stmt.execute(sql);
            
           sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
           stmt.execute(sql);
            

           sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 3.50 WHERE SCD_INVOICE_DATE >='2017-09-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =4.5 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='W17')";
           stmt.execute(sql);
          
        
           sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 1.75 WHERE SCD_INVOICE_DATE >='2017-09-01' AND SCD_INVOICE_DATE <='2017-09-30' AND SCD_DISC_PERCENT =2.25 AND SCD_QUALITY_ID NOT IN (SELECT NW_QLTY_ID FROM SALES.D_SAL_NEW_QLTY_DESPATCH WHERE NW_QLTY_SEASONID ='W17')";
           stmt.execute(sql);
        
           
           /* 
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
             
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_ALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
             */
             /*
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
            
                        
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
            
   
             */
             
             
             
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);
       
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            
            
            */
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
            stmt.execute(sql);   
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
            stmt.execute(sql);   
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 5.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =4.50";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 4.50 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =3.50";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.75 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =2.25";
            stmt.execute(sql); 
            
            sql = "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT = 2.25 WHERE SCD_PARTY_CODE IN (279965)  AND SCD_DISC_PERCENT =1.75";
            stmt.execute(sql); 
            
            sql = "DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_PARTY_CODE IN (159991,509912) ";
            stmt.execute(sql); 
            
           sql= "UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =2.25  WHERE  SCD_INVOICE_NO IN ('SU/003450','SU/003459','SU/003529','SU/003449') AND SCD_PARTY_CODE IN (279965) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0)  IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W17' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'AND SN_CANCELLED =0 AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 279965)  AND SCD_DISC_PERCENT = 4.50";
            stmt.execute(sql);

           sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/003213','SU/003214','SU/003215','SU/003216','SU/003217','SU/003218','SU/003219','SU/003220','SU/003221') AND SCD_PARTY_CODE IN (311912) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W17' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU'  AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 311912) AND SCD_DISC_PERCENT = 3.50";
           stmt.execute(sql);  
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE  SCD_INVOICE_NO IN ('SU/003361','SU/003362','SU/003363','SU/003364','SU/003365','SU/003366','SU/003367','SU/003368','SU/003369','SU/003370','SU/003371','SU/003372','SU/003373','SU/003374','SU/003375','SU/003376','SU/003377','SU/003378','SU/003379','SU/003380','SU/003381','SU/003382','SU/003383','SU/003384','SU/003385','SU/003386','SU/003387','SU/003388','SU/003389','SU/003390','SU/003391','SU/003392','SU/003401','SU/003506','SU/003510','SU/003511') AND SCD_PARTY_CODE IN (300153) AND CONCAT(SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE+0) IN  (SELECT DISTINCT CONCAT(SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE+0) FROM SALES.D_SAL_SALENOTE_DATA WHERE SN_SEASON_ID = 'W17' AND SUBSTRING(SN_ORDER_NO,1,2) ='AU' AND SN_ORDER_DATE <='2017-12-01' AND SN_PARTY_CODE = 300153) AND SCD_DISC_PERCENT = 3.50";
            stmt.execute(sql);  
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =1.75 WHERE SCD_PIECE_NO IN ('29225/13','12099/09','27252/07','11190/09','10229/08','26464/04','26495/05','29155/05','11931/10','11849/09','26535/07','26639/09','26680/01','26680/02','26680/07','26697/01','26697/02','26697/08','29747/09','29350/05','27211/05','25725/02','25246/04','25497/05','25497/06','25497/07','25497/09','25498/09') AND SCD_PARTY_CODE = 311912 AND SCD_DISC_PERCENT = 3.50";
            stmt.execute(sql); 
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_PERCENT =3.50 WHERE SCD_PIECE_NO IN ('26200/04','26199/06','26200/07','10013/04','26199/01','26202/07','26868/10','29493/11','29494/03','26872/05','12574/01','28224/05','27302/07') AND SCD_PARTY_CODE = 300153 AND SCD_DISC_PERCENT = 1.75";
            stmt.execute(sql); 
            
            
            
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
            stmt.execute(sql); 
            
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }     
        jLabelStatus.setText("Generate Scheme Detail Table Complete");
        System.out.println("Scheme Detail Done");
        // ABD Discount % end
                
            //  System.exit(0);                                
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
            }
        }.start();
        //((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }
      
      private void generateABD(){
          //new Thread(){
          //    public void run(){
          jLabelStatus.setText("Generate ABD Start");
          try{
              Connection conn=data.getConn();
              Statement stmt=conn.createStatement();
              String sql = "DELETE FROM SALES.TMP_ABD_DETAIL";
              stmt.execute(sql);
              
              //----------------- INSERT ABD TABLE -----------------
              sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='101' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='W18' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
              stmt.execute(sql);
              
              
              
              
              
              
              //----------------- UPDATE CATEGORY ABD TABLE -----------------
              String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
              String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
              sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='101' AND SLAB_SEASON_ID ='W18'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
              sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              //
              
              sql = "DELETE FROM SALES.TMP_SAL_INVOICE_HEADER";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18'";
              stmt.execute(sql);
              
              sql = "DELETE FROM  SALES.TMP_SAL_INVOICE_DETAIL";
              stmt.execute(sql);
              
              //    sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
              //  stmt.execute(sql);
              sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND UNIT_CODE NOT IN (3,4,5,6,7,0)";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
              stmt.execute(sql);
              
              
              
              
              //--------------------LIFTING DATA ---------------------------------------
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='101' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
              sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='101' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
              sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='101' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
              sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='101' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
              sql2 = " AND UNIT_CODE NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              //----------------------------------------------------------------------
              
              sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'W18' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
              stmt.execute(sql);
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
              sql2 = "   AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
              sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
              sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              
              sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID ='101') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
              sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
              sql = sql1 + sql2;
              stmt.execute(sql);
              
              
              
      
              
              
              
              
              
              //  ----------------------------------------------------------------------------
              
              //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
              // stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
              stmt.execute(sql);
              
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
              stmt.execute(sql);
              
              sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='W18' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='101'";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
              stmt.execute(sql);
              
              //'Unload Me
              
              sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
              stmt.execute(sql);
              
              sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'101',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='W18' AND SLAB_SCHEME_ID = '101') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
              stmt.execute(sql);
              
              sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
              stmt.execute(sql);
              
              
          }catch(SQLException sqe){
              sqe.printStackTrace();
          }
          //}
          //}.start();
          jLabelStatus.setText("Generate ABD Complete");
          System.out.println("abddone");
      }
      
      
    
 
    private void generateABDOLD(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='S18' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            
            
            
            
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
             //
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18'";
            stmt.execute(sql);            
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
            stmt.execute(sql);            
            
        //    sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
          //  stmt.execute(sql);            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND UNIT_CODE NOT IN (3,4,5,6,7,0)";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
           
            
           
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
           //---------------------------------------------------------------------- 
            
            sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'S18' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "   AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);


            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);




            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);



            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);

            
            
            
            
            
            
            
            
          //  ----------------------------------------------------------------------------
            
            //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
           // stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='S18' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
          
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'80',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID = '80') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);   
            
              sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
            stmt.execute(sql);            
          
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD Complete");
        System.out.println("abddone");
    }
    
    
    
    
    
    
    private void generateABD_S18(){
        //new Thread(){
        //    public void run(){
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='S18' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            
            
            
            
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80' AND SLAB_SEASON_ID ='S18'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            //
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18'";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
            stmt.execute(sql);
            
            //    sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
            //  stmt.execute(sql);
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND UNIT_CODE NOT IN (3,4,5,6,7,0)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);
            
            
            
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='80' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            //----------------------------------------------------------------------
            
            sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN4_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0,ABDD_LIFT_MIN4_MTRODD = 0,ABDD_LIFT_MIN4_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'S18' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "   AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID ='80') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            
            
            
            
            
            
            
            //  ----------------------------------------------------------------------------
            
            //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
            // stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='S18' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='80'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'80',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S18' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='S18' AND SLAB_SCHEME_ID = '80') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
            stmt.execute(sql);
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();
        }
        //}
        //}.start();
        jLabelStatus.setText("Generate ABD Complete");
        System.out.println("abddone");
    }
 
    
    
    
    
    
    
    
    
    
    
    private void generateABDw17(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='71' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='W17' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            
            
            
            
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='71' AND SLAB_SEASON_ID ='W17'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
             //
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17'";
            stmt.execute(sql);            
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
            stmt.execute(sql);            
            
        //    sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
          //  stmt.execute(sql);            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND UNIT_CODE NOT IN (3,4,5,6,7,0)";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
           
            
           
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='71' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='71' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='71' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='71' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
           //---------------------------------------------------------------------- 
            
            sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN4_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0,ABDD_LIFT_MIN4_MTRODD = 0,ABDD_LIFT_MIN4_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'W17' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "   AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);


            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);




            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);



            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID ='71') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);

            
            
            
            
            
            
            
            
          //  ----------------------------------------------------------------------------
            
            //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
           // stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='W17' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='71'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
          
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'71',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W17' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='W17' AND SLAB_SCHEME_ID = '71') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);   
            
              sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
            stmt.execute(sql);            
          
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD Complete");
        System.out.println("abddone");
    }
    
    
    
    private void generateABDW16(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='53' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='W16' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            
            
            
            
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='53' AND SLAB_SEASON_ID ='W16'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
             //
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16'";
            stmt.execute(sql);            
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
           
            
           
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='53' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='53' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='53' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='53' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
           //---------------------------------------------------------------------- 
            
            sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN4_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0,ABDD_LIFT_MIN4_MTRODD = 0,ABDD_LIFT_MIN4_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'W16' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "   AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);


            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);




            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);



            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID ='53') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);

            
            
            
            
            
            
            
            
          //  ----------------------------------------------------------------------------
            
            //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
           // stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='W16' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='53'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
          
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'53',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='W16' AND SLAB_SCHEME_ID = '53') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);   
            
              sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
            stmt.execute(sql);            
          
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD Complete");
        System.out.println("abddone");
    }
    
    
    
 private void generateABDS16(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='44' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='S16' AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND  SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            
            
            
            
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='44' AND SLAB_SEASON_ID ='S16'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_UNIT_ID NOT IN (21) AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
             //
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16'";
            stmt.execute(sql);            
            
            sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
            stmt.execute(sql);            
            
            sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND UNIT_CODE NOT IN (2,3,4,5,6,7,0)";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
            
            sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
            stmt.execute(sql);            
           
            
           
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='44' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='44' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='44' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='44' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND UNIT_CODE NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
           //---------------------------------------------------------------------- 
            
            sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN4_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0,ABDD_LIFT_MIN4_MTRODD = 0,ABDD_LIFT_MIN4_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'S16' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "   AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  UNIT_CODE NOT IN (5,6) AND  QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);


            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);




            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);



            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID ='44') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
            sql2 = "  AND UNIT_CODE+0 NOT IN (2,3,5,6,7) AND  QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);

            
            
            
            
            
            
            
            
          //  ----------------------------------------------------------------------------
            
            //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
           // stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='S16' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='44'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
          
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'44',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S16' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='S16' AND SLAB_SCHEME_ID = '44') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);   
            
              sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
            stmt.execute(sql);            
          
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD Complete");
        System.out.println("abddone");
    }
 
 private void generateABDS15(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='22' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='S15' AND SN_CANCELLED =0 AND SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
            stmt.execute(sql);
            
            //----------------- UPDATE CATEGORY ABD TABLE -----------------
            String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
            String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='22' AND SLAB_SEASON_ID ='S15'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
            sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            
            //--------------------LIFTING DATA ---------------------------------------
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='22' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S15' AND SLAB_SCHEME_ID ='22') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='22' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S15' AND SLAB_SCHEME_ID ='22') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
            sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='22' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S15' AND SLAB_SCHEME_ID ='22') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='22' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='S15' AND SLAB_SCHEME_ID ='22') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
            sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
            sql = sql1 + sql2;
            stmt.execute(sql);
            
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
            stmt.execute(sql);
            
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
            stmt.execute(sql);
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='S15' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='22'";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
          
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            //'Unload Me
            
            sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'22',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='S15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='S15' AND SLAB_SCHEME_ID = '22') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
            stmt.execute(sql);            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD S15 Complete");
        System.out.println("abds15done");
    }


 
  private void generateABDODD(){
       //new Thread(){
        //    public void run(){  
        jLabelStatus.setText("Generate ABDODD Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            String sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAYODD";
            stmt.execute(sql);            
            
            //----------------- INSERT ABD TABLE -----------------
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAYODD SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','', '','','','','','','','','','','' ,'','','','' ,'','','','','','','','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='W15' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='25'";
            stmt.execute(sql);
         
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAYODD SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT_MIN1_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH) ,CASE WHEN SUM(ABDD_LIFT0_LIFT1_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN  ((SUM(ABDD_LIFT_MIN1_MTRFRESH) -SUM(ABDD_LIFT0_LIFT1_MTR_L))/SUM(ABDD_CATG1_MTR))*100 WHEN SUM(ABDD_LIFT0_LIFT1_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH) /SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT_MIN1_MTRODD+ ABDD_LIFT_MIN2_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) ,CASE WHEN SUM(ABDD_LIFT1_LIFT2_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN  ((SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) -SUM(ABDD_LIFT1_LIFT2_MTR_L))/SUM(ABDD_CATG1_MTR))*100 WHEN SUM(ABDD_LIFT1_LIFT2_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) /SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT_MIN3_MTRODD),SUM(ABDD_LIFT_MIN3_MTRFRESH) ,CASE WHEN SUM(ABDD_LIFT2_LIFT3_MTR) <= SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN  ((SUM(ABDD_LIFT_MIN3_MTRFRESH) -SUM(ABDD_LIFT2_LIFT3_MTR_L))/SUM(ABDD_CATG1_MTR))*100 WHEN SUM(ABDD_LIFT2_LIFT3_MTR) > SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN3_MTRFRESH) /SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_MIN4_MTRODD),SUM(ABDD_LIFT_MIN4_MTRFRESH) ,CASE WHEN SUM(ABDD_LIFT_CATG1_SN_MTR) <= SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN  ((SUM(ABDD_LIFT_MIN4_MTRFRESH) -SUM(ABDD_LIFT_CATG1_SN_MTR_L))/SUM(ABDD_CATG1_MTR))*100 WHEN SUM(ABDD_LIFT_CATG1_SN_MTR) > SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN4_MTRFRESH) /SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100   FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
            stmt.execute(sql);
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAYODD SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR, ABDD_LIFT_MIN1_MTRODD,ABDD_LIFT_MIN1_MTRFRESH ,CASE WHEN ABDD_CATG1_MTR <= ABDD_LIFT_MIN1_MTRFRESH  THEN 100 WHEN ABDD_CATG1_MTR > ABDD_LIFT_MIN1_MTRFRESH  THEN (ABDD_LIFT_MIN1_MTRFRESH/ ABDD_CATG1_MTR)*100 END ,  ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT_MIN2_MTRODD+ABDD_LIFT_MIN1_MTRODD ,ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH ,CASE WHEN ABDD_CATG1_MTR <= ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH  THEN 100 WHEN ABDD_CATG1_MTR > ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH  THEN ((ABDD_LIFT_MIN1_MTRFRESH+ ABDD_LIFT_MIN2_MTRFRESH) / ABDD_CATG1_MTR)*100 END,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT_MIN3_MTRODD,ABDD_LIFT_MIN3_MTRFRESH ,CASE WHEN ABDD_CATG1_MTR <= ABDD_LIFT_MIN3_MTRFRESH  THEN 100 WHEN ABDD_CATG1_MTR > ABDD_LIFT_MIN3_MTRFRESH  THEN (ABDD_LIFT_MIN3_MTRFRESH / ABDD_CATG1_MTR)*100 END,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L, ABDD_LIFT_MIN4_MTRODD,ABDD_LIFT_MIN4_MTRFRESH ,CASE WHEN ABDD_CATG1_MTR <= ABDD_LIFT_MIN4_MTRFRESH  THEN 100 WHEN ABDD_CATG1_MTR > ABDD_LIFT_MIN4_MTRFRESH  THEN (ABDD_LIFT_MIN4_MTRFRESH / ABDD_CATG1_MTR)*100 END,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON  FROM SALES.TMP_ABD_DETAIL";
            stmt.execute(sql);
          
            
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAYODD SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT_MIN1_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT_MIN1_MTRODD+ ABDD_LIFT_MIN2_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT_MIN3_MTRODD),SUM(ABDD_LIFT_MIN3_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN3_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_MIN4_MTRODD),SUM(ABDD_LIFT_MIN4_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN4_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100  FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
            stmt.execute(sql);
       
            sql = "INSERT INTO SALES.TMP_ABD_DISPLAYODD SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT_MIN1_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT_MIN1_MTRODD+ ABDD_LIFT_MIN2_MTRODD),SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN1_MTRFRESH+ABDD_LIFT_MIN2_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT_MIN3_MTRODD),SUM(ABDD_LIFT_MIN3_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN3_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN3_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_MIN4_MTRODD),SUM(ABDD_LIFT_MIN4_MTRFRESH) ,CASE WHEN SUM(ABDD_CATG1_MTR) <= SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN 100 WHEN SUM(ABDD_CATG1_MTR) > SUM(ABDD_LIFT_MIN4_MTRFRESH)  THEN (SUM(ABDD_LIFT_MIN4_MTRFRESH) / SUM(ABDD_CATG1_MTR))*100 END,SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
            stmt.execute(sql);
            
            
           
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }
         //}
        //}.start();
        jLabelStatus.setText("Generate ABD ODD Complete");
        System.out.println("abd OOD done");
  }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
       
     private void runBreakPieceProcessS15(){
        jLabelStatus.setText("Processing ...");
        cmbPartyCode.setEnabled(false);
        cmbQualityId.setEnabled(false);
        cmbShade.setEnabled(false);
        formatGridABDInvoiceTable1();
        new Thread(){
            public void run(){
                generatePartyCombo();
                int rowinvoice=0;
                int Partyitems = cmbPartyCodeModel.getSize();
                System.out.println(Partyitems);
                for(int i=1;i<Partyitems;i++){
                    Object element = cmbPartyCodeModel.getElementAt(i);
                    generateQualityCombo(element);
                    int Qualityitems = cmbQualityIDModel.getSize();
                    System.out.println(element+"-"+Qualityitems);
                    for(int j=1;j<Qualityitems;j++){
                        Object element1=cmbQualityIDModel.getElementAt(j);
                        generateShadeCombo(element,element1);
                        int Shadeitems= cmbShadeModel.getSize();
                        System.out.println(element+"-"+element1+"-"+Shadeitems);
                        for(int k=1;k<Shadeitems;k++){
                            Object element2=cmbShadeModel.getElementAt(k);
                            generateABDDetailTable(element,element1,element2);
                            generateABDInvoiceTable(element,element1,element2);
                            rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                            generateBreakPiece();
                            txtInvoicerows.setText(Long.toString(rowinvoice));
                            txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                        }
                    }
                }
                
                deletetable();
                InsertIntoTable();
                jLabelStatus.setText(" All Data Inserted into Table");
                cmbPartyCode.setEnabled(true);
                cmbQualityId.setEnabled(true);
                cmbShade.setEnabled(true);
                
                //ABD Discount % Start
                DeleteTmpSaleSchemeTable();
                jLabelStatus.setText("Generate Scheme Detail Table Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();            
            //-----------4.5%
            //String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT '22',ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);            
            //----------2.25%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";           
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT '22',ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";           
            stmt.execute(sql);
            //----------3.5%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT '22',ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);
            //----------1.75%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE";
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT '22',ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE";
            stmt.execute(sql);
            //--------delete 0 value net qty
            
            
            sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
            stmt.execute(sql);
            
            //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
            sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
            stmt.execute(sql);
            
            sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
            stmt.execute(sql);
            

            
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'S15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-06-21'";
            stmt.execute(sql);
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'S15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'S15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);
       
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'S15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            
            
            
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
            stmt.execute(sql);   
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
            stmt.execute(sql);   
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
            stmt.execute(sql); 
            
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }     
        jLabelStatus.setText("Generate Scheme Detail Table Complete");
        System.out.println("Scheme Detail Done");
        // ABD Discount % end
                
            //  System.exit(0);                                
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
            }
        }.start();
        //((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }    
       
     private void generateABDW15(){
         //new Thread(){
         //    public void run(){
         jLabelStatus.setText("Generate ABD Start");
         try{
             Connection conn=data.getConn();
             Statement stmt=conn.createStatement();
             String sql = "TRUNCATE TABLE SALES.TMP_ABD_DETAIL";
             stmt.execute(sql);
             
             //----------------- INSERT ABD TABLE -----------------
             sql = "INSERT INTO SALES.TMP_ABD_DETAIL SELECT SN_SEASON_ID,SN_PARTY_CODE,0,SN_QUALITY_ID,CATEGORY_LIST,QLT_NEW_OLD,1,SN_SHADE,SUM(SN_UNITS),SUM(SN_ABD_TOTAL_MTR),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 FROM SALES.D_SAL_SALENOTE_DATA A,DINESHMILLS.D_SAL_QUALITY_MASTER C ,(SELECT DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID , SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='25' ) AS M WHERE QUALITY_ID = SN_QUALITY_ID AND SEASON_ID = SN_SEASON_ID AND SN_PARTY_CODE = SLAB_PARTY_CODE AND SN_SEASON_ID ='W15' AND SN_CANCELLED =0 AND SLAB_SEASON_ID = SN_SEASON_ID AND CATEGORY_LIST IN ('REGULAR','S-QL') GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD ORDER BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,CATEGORY_LIST,QLT_NEW_OLD LIMIT 10000000";
             stmt.execute(sql);
             
             
             
             
             
             
             //----------------- UPDATE CATEGORY ABD TABLE -----------------
             String sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15'  AND SLAB_ORDER_SR_NO =1 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG1_UNIT =UN, ABDD_CATG1_MTR =MTR , ABDD_CATG1_CAPTION =PERIOD ";
             String sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15'  AND SLAB_ORDER_SR_NO =2 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG2_UNIT =UN, ABDD_CATG2_MTR =MTR , ABDD_CATG2_CAPTION =PERIOD ";
             sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL ,(SELECT SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE, CONCAT(SLAB_ORDER_FROMDATE, '  TO  ',SLAB_ORDER_TO_DATE) AS PERIOD, SUM(SN_UNITS) AS UN , SUM(SN_ABD_TOTAL_MTR) AS MTR FROM SALES.D_SAL_SALENOTE_DATA,(SELECT  DISTINCT SLAB_SEASON_ID,SLAB_SCHEME_ID ,SLAB_ORDER_SR_NO, SLAB_PARTY_CODE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15' ) AS SLAB WHERE SN_PARTY_CODE = SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='25' AND SLAB_SEASON_ID ='W15'  AND SLAB_ORDER_SR_NO =3 AND SN_CANCELLED =0 AND SN_ORDER_DATE >= SLAB_ORDER_FROMDATE AND SN_ORDER_DATE <=  SLAB_ORDER_TO_DATE GROUP BY SN_SEASON_ID,SN_PARTY_CODE,SN_QUALITY_ID,SN_SHADE,SLAB_ORDER_FROMDATE,SLAB_ORDER_TO_DATE ) AS A SET ABDD_CATG3_UNIT =UN, ABDD_CATG3_MTR =MTR , ABDD_CATG3_CAPTION =PERIOD ";
             sql2 = " Where ABDD_PARTY_CODE = SN_PARTY_CODE And SN_QUALITY_ID = ABDD_QUALITY_ID AND SN_SHADE =  ABDD_SHADE AND SN_SEASON_ID = ABDD_SEASON_ID ";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             //
             
             sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_HEADER";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_SAL_INVOICE_HEADER SELECT A.*  FROM DINESHMILLS.D_SAL_INVOICE_HEADER A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15'";
             stmt.execute(sql);
             
             sql = "TRUNCATE TABLE SALES.TMP_SAL_INVOICE_DETAIL";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_SAL_INVOICE_DETAIL SELECT A.*   FROM DINESHMILLS.D_SAL_INVOICE_DETAIL A,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15'";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_SAL_INVOICE_HEADER SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_SAL_INVOICE_DETAIL SET PARTY_CODE = 159991 WHERE PARTY_CODE = 739901";
             stmt.execute(sql);
             
             
             
             
             //--------------------LIFTING DATA ---------------------------------------
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =1 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='25' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =2 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='25' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1  ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =3 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='25' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH,SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,ABDD_LIFT_PER_SLAB FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.TMP_ABD_LIFT_SLAB WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND ABDD_LIFT_SR_NO =4 AND ABDD_LIFT_SEASON_ID =D.SEASON_ID AND ABDD_LIFT_SCHEME_ID ='25' AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= ABDD_LIFT_FROM_DATE AND A.INVOICE_DATE <= ABDD_LIFT_TO_DATE AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,ABDD_LIFT_PER_SLAB ) AS INV SET  ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             //----------------------------------------------------------------------
             
             sql = "UPDATE  SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET ABDD_LIFT_MIN1_MTR =0,ABDD_LIFT_MIN2_MTR =0,ABDD_LIFT_MIN3_MTR =0,ABDD_LIFT_MIN4_MTR =0,ABDD_LIFT_MIN1_MTRODD = 0,ABDD_LIFT_MIN1_MTRFRESH = 0,ABDD_LIFT_MIN2_MTRODD = 0,ABDD_LIFT_MIN2_MTRFRESH = 0,ABDD_LIFT_MIN3_MTRODD = 0,ABDD_LIFT_MIN3_MTRFRESH = 0,ABDD_LIFT_MIN4_MTRODD = 0,ABDD_LIFT_MIN4_MTRFRESH = 0 WHERE  ABDD_QUALITY_ID =  NW_QLTY_ID  AND NW_QLTY_SEASONID = 'W15' AND   NW_QLTY_SEASONID = ABDD_SEASON_ID ";
             stmt.execute(sql);
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB1 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= NW_ABD_SEASON_START  AND A.INVOICE_DATE <= NW_ABD_DESP_DATE1 AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3)GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB1 ) AS INV SET ABDD_LIFT_MIN1_MTR = MTR,ABDD_LIFT_MIN1_MTRODD = ODD,ABDD_LIFT_MIN1_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB2 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE3   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE4 AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB2 ) AS INV SET ABDD_LIFT_MIN2_MTR = MTR,ABDD_LIFT_MIN2_MTRODD = ODD,ABDD_LIFT_MIN2_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB3 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= NW_ABD_DESP_DATE5   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB3 ) AS INV SET ABDD_LIFT_MIN3_MTR = MTR,ABDD_LIFT_MIN3_MTRODD = ODD,ABDD_LIFT_MIN3_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             
             sql1 = "UPDATE SALES.TMP_ABD_DETAIL,(SELECT A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AS QUALITY,B.PATTERN_CODE AS SHADE,SUM(GROSS_QTY) AS MTR,SUM(COALESCE(CASE WHEN UNIT_CODE IN (5,6) THEN GROSS_QTY END,0)) AS ODD,SUM(COALESCE(CASE WHEN UNIT_CODE NOT IN (5,6) THEN GROSS_QTY END,0)) AS FRESH, SUM(B.NET_AMOUNT) AS NET_AMOUNT,AVG(RATE) AS RATE,NW_ABD_LIFT_PER_SLAB4 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D,SALES.D_SAL_NEW_QLTY_DESPATCH WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND NW_QLTY_SEASONID =D.SEASON_ID  AND NW_QLTY_ID = CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)) AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE  FROM SALES.D_SAL_SCHEME_SLAB WHERE SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID ='25') AND A.INVOICE_DATE >= NW_ABD_SEASON_START   AND A.INVOICE_DATE <= NW_ABD_DESP_DATE6 AND A.WAREHOUSE_CODE =1 ";
             sql2 = " AND QUALITY_INDICATOR IN (0,3) GROUP BY  A.PARTY_CODE,CONCAT(3,SUBSTRING(B.QUALITY_NO,2,5)),B.PATTERN_CODE,NW_ABD_LIFT_PER_SLAB4 ) AS INV SET ABDD_LIFT_MIN4_MTR = MTR,ABDD_LIFT_MIN4_MTRODD = ODD,ABDD_LIFT_MIN4_MTRFRESH = FRESH WHERE ABDD_PARTY_CODE = PARTY_CODE AND QUALITY+0 = ABDD_QUALITY_ID+0 AND SHADE+0 = ABDD_SHADE+0";
             sql = sql1 + sql2;
             stmt.execute(sql);
             
             
             
             
             
             
             
             
             
             //  ----------------------------------------------------------------------------
             
             //sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_PARTY_CODE = 159991 WHERE ABDD_PARTY_CODE = 739901 ";
             // stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_PERCENT_SEASON = (ABDD_LIFT_MIN4_MTR / ABDD_MTRS) *100";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR =  (ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR_L =  CASE WHEN ABDD_LIFT1_LIFT2_MTR > ABDD_CATG1_MTR THEN ABDD_LIFT1_LIFT2_MTR - ABDD_CATG1_MTR END";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_MTR = ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT1_LIFT2_MTR_L";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT1_LIFT2_PER = (ABDD_LIFT1_LIFT2_MTR/ABDD_CATG1_MTR)*100";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL =  ABDD_CATG1_MTR -(ABDD_LIFT_MIN1_MTR + ABDD_LIFT_MIN2_MTR)";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_CATG1_SHORTFALL = 0 WHERE  ABDD_CATG1_SHORTFALL <= 0";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  0";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_CATG1_SHORTFALL Where ABDD_CATG1_SHORTFALL <= ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR =  ABDD_LIFT_MIN3_MTR Where ABDD_CATG1_SHORTFALL > ABDD_LIFT_MIN3_MTR AND ABDD_LIFT_MIN3_MTR != 0";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_MTR_L =  ABDD_LIFT_MIN3_MTR - ABDD_LIFT2_LIFT3_MTR Where ABDD_LIFT_MIN3_MTR > ABDD_CATG1_SHORTFALL";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT2_LIFT3_PER = (ABDD_LIFT2_LIFT3_MTR/ABDD_CATG1_MTR)*100";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR =  (ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR)";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_MTR_L =  (ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L)";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT_CATG1_SN_PER =  (ABDD_LIFT_CATG1_SN_MTR/ABDD_CATG1_MTR)*100";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_EXTRA =  ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR+ ABDD_LIFT1_LIFT2_MTR_L + ABDD_LIFT2_LIFT3_MTR_L";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR = 0";
             stmt.execute(sql);
             
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR =  CASE WHEN  ABDD_LIFT_MIN1_MTR =  ABDD_CATG1_MTR  THEN ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR <  ABDD_CATG1_MTR   THEN  ABDD_LIFT_MIN1_MTR WHEN  ABDD_LIFT_MIN1_MTR >  ABDD_CATG1_MTR   THEN  ABDD_CATG1_MTR END";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_MTR_L  =  CASE WHEN  ABDD_LIFT_MIN1_MTR != ABDD_LIFT0_LIFT1_MTR THEN ABDD_LIFT_MIN1_MTR - ABDD_LIFT0_LIFT1_MTR END";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR =  ABDD_LIFT1_LIFT2_MTR - ABDD_LIFT0_LIFT1_MTR";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_MTR_L =  ABDD_LIFT1_LIFT2_MTR_L - ABDD_LIFT0_LIFT1_MTR_L";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT1_PER = (ABDD_LIFT0_LIFT1_MTR/ABDD_CATG1_MTR)*100";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_DETAIL SET ABDD_LIFT0_LIFT2_PER = (ABDD_LIFT0_LIFT2_MTR/ABDD_CATG1_MTR)*100";
             stmt.execute(sql);
             
             sql = "TRUNCATE TABLE SALES.TMP_ABD_DISPLAY";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT DISTINCT 0,SLAB_PARTY_CODE,CONCAT_WS(' ,',PARTY_NAME,CITY_ID) AS PARTY,'','','','','','','','','','','','','','','','' ,'','','','' ,'','','','' FROM SALES.D_SAL_SCHEME_SLAB,DINESHMILLS.D_SAL_PARTY_MASTER WHERE SLAB_SEASON_ID ='W15' AND PARTY_CODE =SLAB_PARTY_CODE AND SLAB_SCHEME_ID ='25'";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '3',ABDD_PARTY_CODE,'PARTY TOTAL', '******','******','******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L),SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,' ', ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID,ABDD_SHADE,ABDD_BOOKED_UNIT,ABDD_MTRS,ABDD_CATG1_UNIT,ABDD_CATG1_MTR,ABDD_CATG2_UNIT,ABDD_CATG2_MTR,ABDD_LIFT0_LIFT1_MTR,ABDD_LIFT0_LIFT1_PER,ABDD_LIFT0_LIFT1_MTR_L,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_PER,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_PER,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT_CATG1_SN_MTR,ABDD_LIFT_CATG1_SN_PER,ABDD_LIFT_CATG1_SN_MTR_L,ABDD_LIFT_MIN4_MTR,ABDD_LIFT_PERCENT_SEASON FROM SALES.TMP_ABD_DETAIL ";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE,'', ABDD_CATG_OLD,ABDD_CATG,CONCAT(ABDD_QUALITY_ID, ' TOTAL' ),'***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG,ABDD_QUALITY_ID";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_DISPLAY SELECT '1',ABDD_PARTY_CODE, '',CONCAT(ABDD_CATG_OLD,' TOTAL' ),ABDD_CATG,'******','***',SUM(ABDD_BOOKED_UNIT),SUM(ABDD_MTRS),SUM(ABDD_CATG1_UNIT) , SUM(ABDD_CATG1_MTR),SUM(ABDD_CATG2_UNIT) , SUM(ABDD_CATG2_MTR),SUM(ABDD_LIFT0_LIFT1_MTR),(SUM(ABDD_LIFT0_LIFT1_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT0_LIFT1_MTR_L),SUM(ABDD_LIFT1_LIFT2_MTR),(SUM(ABDD_LIFT1_LIFT2_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT1_LIFT2_MTR_L),SUM(ABDD_LIFT2_LIFT3_MTR),(SUM(ABDD_LIFT2_LIFT3_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT2_LIFT3_MTR_L),SUM(ABDD_LIFT_CATG1_SN_MTR),(SUM(ABDD_LIFT_CATG1_SN_MTR) / SUM(ABDD_CATG1_MTR))*100,SUM(ABDD_LIFT_CATG1_SN_MTR_L), SUM(ABDD_LIFT_MIN4_MTR),(SUM(ABDD_LIFT_MIN4_MTR)/SUM(ABDD_MTRS))*100 FROM SALES.TMP_ABD_DETAIL GROUP BY ABDD_PARTY_CODE, ABDD_CATG_OLD,ABDD_CATG";
             stmt.execute(sql);
             
             //'Unload Me
             
             sql = "TRUNCATE TABLE SALES.TMP_ABD_INVOICE";
             stmt.execute(sql);
             
             sql = "INSERT INTO SALES.TMP_ABD_INVOICE SELECT D.SEASON_ID,'25',A.PARTY_CODE, A.INVOICE_NO,A.INVOICE_DATE,B.QUALITY_NO/10,CONCAT(3,SUBSTRING(B.QUALITY_NO/10,2,5)),B.PATTERN_CODE,B.PIECE_NO,GROSS_QTY,B.FLAG_DEF_CODE,NET_QTY,B.RATE ,GROSS_AMOUNT,B.NET_AMOUNT,A.BALE_NO,A.GATEPASS_NO,'',DEF_DISC_PER,ADDL_DISC_PER,DEF_DISCOUNT,ADDL_DISCOUNT,0,0,0,0 FROM SALES.TMP_SAL_INVOICE_HEADER A,SALES.TMP_SAL_INVOICE_DETAIL  B,DINESHMILLS.D_SAL_SEASON_MASTER D WHERE A.INVOICE_TYPE =1 AND A.INVOICE_DATE >= DATE_FROM AND A.INVOICE_DATE <= DATE_TO AND D.SEASON_ID ='W15' AND A.INVOICE_DATE = B.INVOICE_DATE AND A.INVOICE_NO = B.INVOICE_NO AND A.CANCELLED =0 AND A.APPROVED =1 AND A.BALE_NO = B.BALE_NO AND A.PARTY_CODE IN (SELECT DISTINCT SLAB_PARTY_CODE FROM SALES.D_SAL_SCHEME_SLAB WHERE  SLAB_SEASON_ID ='W15' AND SLAB_SCHEME_ID = '25') AND A.WAREHOUSE_CODE =1   AND QUALITY_INDICATOR IN (0,3) LIMIT 1000000000000";
             stmt.execute(sql);
             
             sql = "UPDATE SALES.TMP_ABD_INVOICE SET ABDI_PARTY_CODE = 159991 WHERE ABDI_PARTY_CODE = 739901 ";
             stmt.execute(sql);
             
             
         }catch(SQLException sqe){
             sqe.printStackTrace();
         }
         //}
         //}.start();
         jLabelStatus.setText("Generate ABD Complete");
         System.out.println("abddone");
    }      
       
   
   private void runBreakPieceProcessW15(){
        jLabelStatus.setText("Processing ...");
        cmbPartyCode.setEnabled(false);
        cmbQualityId.setEnabled(false);
        cmbShade.setEnabled(false);
        formatGridABDInvoiceTable1();
        new Thread(){
            public void run(){
                generatePartyCombo();
                int rowinvoice=0;
                int Partyitems = cmbPartyCodeModel.getSize();
                System.out.println(Partyitems);
                for(int i=1;i<Partyitems;i++){
                    Object element = cmbPartyCodeModel.getElementAt(i);
                    generateQualityCombo(element);
                    int Qualityitems = cmbQualityIDModel.getSize();
                    System.out.println(element+"-"+Qualityitems);
                    for(int j=1;j<Qualityitems;j++){
                        Object element1=cmbQualityIDModel.getElementAt(j);
                        generateShadeCombo(element,element1);
                        int Shadeitems= cmbShadeModel.getSize();
                        System.out.println(element+"-"+element1+"-"+Shadeitems);
                        for(int k=1;k<Shadeitems;k++){
                            Object element2=cmbShadeModel.getElementAt(k);
                            generateABDDetailTable(element,element1,element2);
                            generateABDInvoiceTable(element,element1,element2);
                            rowinvoice = rowinvoice + jTableABDInvoice.getRowCount();
                            generateBreakPiece();
                            txtInvoicerows.setText(Long.toString(rowinvoice));
                            txtbreakpiecerows.setText(Integer.toString(jTable1.getRowCount()));
                        }
                    }
                }
                
                deletetable();
                InsertIntoTable();
                jLabelStatus.setText(" All Data Inserted into Table");
                cmbPartyCode.setEnabled(true);
                cmbQualityId.setEnabled(true);
                cmbShade.setEnabled(true);
                
                //ABD Discount % Start
                DeleteTmpSaleSchemeTable();
                jLabelStatus.setText("Generate Scheme Detail Table Start");
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();
            
            String sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_INVOICE_DATE >= '2015-09-01' AND  ABDI_INVOICE_DATE <= '2015-09-30'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'W15') ";
            stmt.execute(sql11); 
            
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE  SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_INVOICE_DATE >= '2015-09-01' AND  ABDI_INVOICE_DATE <= '2015-09-30'  AND ABDI_QLTY_ID NOT IN ( SELECT NW_QLTY_ID FROM  SALES.D_SAL_NEW_QLTY_DESPATCH WHERE  NW_QLTY_SEASONID = 'W15') ";
            stmt.execute(sql11); 
         
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH  SET ABDD_LIFT2_LIFT3_MTR = ABDD_LIFT1_LIFT2_MTR , ABDD_LIFT2_LIFT3_MTR_L = ABDD_LIFT1_LIFT2_MTR_L, ABDD_LIFT2_LIFT3_MTR_L_AMOUNT = ABDD_LIFT1_LIFT2_MTR_L_AMOUNT ,ABDD_LIFT2_LIFT3_MTR_AMOUNT = ABDD_LIFT1_LIFT2_MTR_AMOUNT ,ABDD_NET_QTY_AFTER_LIFT23  = ABDD_NET_QTY_AFTER_LIFT12 ,ABDD_NET_QTY_AFTER_LIFT23L  = ABDD_NET_QTY_AFTER_LIFT12L WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W15'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            sql11 = "UPDATE SALES.TMP_ABD_BREAKPIECE,SALES.D_SAL_NEW_QLTY_DESPATCH   SET ABDD_LIFT1_LIFT2_MTR=0 ,  ABDD_LIFT1_LIFT2_MTR_L=0,  ABDD_LIFT1_LIFT2_MTR_L_AMOUNT=0 , ABDD_LIFT1_LIFT2_MTR_AMOUNT=0 , ABDD_NET_QTY_AFTER_LIFT12=0 , ABDD_NET_QTY_AFTER_LIFT12L=0 WHERE ABDI_QLTY_ID = NW_QLTY_ID+0  AND NW_QLTY_SEASONID = 'W15'  AND ABDI_INVOICE_DATE >= NW_ABD_DESP_DATE3  AND ABDI_INVOICE_DATE <= NW_ABD_DESP_DATE4 ";
            stmt.execute(sql11); 
         
            //-----------4.5%
            //String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT1_LIFT2_MTR,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            String sql = "INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 25,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12,ABDD_LIFT1_LIFT2_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,4.5,ROUND(ABDD_LIFT1_LIFT2_MTR_AMOUNT*.045),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);            
            //----------2.25%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT1_LIFT2_MTR_L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";           
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 25,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT12L,ABDD_LIFT1_LIFT2_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,2.25,ROUND(ABDD_LIFT1_LIFT2_MTR_L_AMOUNT*.0225),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";           
            stmt.execute(sql);
            //----------3.5%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT2_LIFT3_MTR,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 25,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23,ABDD_LIFT2_LIFT3_MTR_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,3.5,ROUND(ABDD_LIFT2_LIFT3_MTR_AMOUNT*.035),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE ";
            stmt.execute(sql);
            //----------1.75%
            //sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 5,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_LIFT2_LIFT3_MTR_L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE";
            sql="INSERT INTO SALES.TMP_SAL_SCHEME_DETAIL (SCD_SCHEME_ID,SCD_SEASON_ID,SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG,SCD_UNIT_CODE,SCD_GROSS_QTY,SCD_GROSS_AMOUNT,SCD_NET_QTY,SCD_NET_AMOUNT,SCD_AGENT_LAST_INVOICE,SCD_AGENT_SR_NO,SCD_INVOICE_NO,SCD_INVOICE_DATE,SCD_INV_RATE,SCD_PL_RATE,SCD_QUALITY_ELIGIBLE,SCD_DISC_PER_MTR_RATE,SCD_DISC_PERCENT,SCD_DISC_AMOUNT,SCD_GATE_PASS_NO,SCD_GATEPASS_DATE,SCD_BALE_NO) SELECT 25,ABDI_SEASON_ID,ABDI_PARTY_CODE,ABDI_QLTY_ID,ABDI_SHADE,ABDI_PIECE_NO,ABDI_DA_CODE,0,ABDI_GROSS_QTY,0,ABDD_NET_QTY_AFTER_LIFT23L,ABDD_LIFT2_LIFT3_MTR_L_AMOUNT,0,0,ABDI_INVOICE_NO,ABDI_INVOICE_DATE,ABDI_RATE,0,'',0,1.75,ROUND(ABDD_LIFT2_LIFT3_MTR_L_AMOUNT*.0175),'','0000-00-00',0 FROM SALES.TMP_ABD_BREAKPIECE";
            stmt.execute(sql);
            //--------delete 0 value net qty
            
            
            sql="DELETE FROM SALES.TMP_SAL_SCHEME_DETAIL WHERE SCD_NET_QTY=0.00";
            stmt.execute(sql);
            
            //update flag value to null where duplicate value inserted in same piece of qlty,shade while making breakpiece
            sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL,(SELECT SCD_PARTY_CODE PP,SCD_QUALITY_ID QQ,SCD_SHADE SS,SCD_PIECE_NO AS PC ,SCD_FLAG AS FLG ,COUNT(*)  AS CNT,MIN(SCD_DISC_PERCENT) AS PER FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE, SCD_QUALITY_ID ,SCD_SHADE,SCD_PIECE_NO,SCD_FLAG HAVING COUNT(*) > 1) AS AA SET SCD_FLAG ='' WHERE SCD_PIECE_NO  = PC AND SCD_FLAG = FLG AND SCD_PARTY_CODE = PP AND SCD_QUALITY_ID = QQ AND SCD_SHADE =SS AND SCD_DISC_PERCENT = PER";
            stmt.execute(sql);
            
            sql="UPDATE  SALES.TMP_SAL_SCHEME_DETAIL SET SCD_GROSS_QTY = SCD_NET_QTY+ SCD_FLAG*.10";
            stmt.execute(sql);
            

            
            
          
           /* 
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL  SET SCD_GATE_PASS_NO  = 1,SCD_BALE_NO =1";
             stmt.execute(sql);
            
             
             sql="TRUNCATE TABLE  TEMP_DATABASE.TM81";
             stmt.execute(sql);
            
             sql="INSERT INTO TEMP_DATABASE.TM81 SELECT SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE,COUNT(*),'','' FROM SALES.TMP_SAL_SCHEME_DETAIL GROUP BY SCD_PARTY_CODE,SCD_QUALITY_ID,SCD_PIECE_NO,SCD_SHADE HAVING COUNT(*) > 1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_GATE_PASS_NO  = 2 WHERE SCD_PARTY_CODE = COL01 AND SCD_QUALITY_ID = COL02 AND SCD_PIECE_NO = COL03 AND SCD_SHADE = COL04";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 1 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (4.50,3.50)";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL ,TEMP_DATABASE.TM81 SET SCD_BALE_NO   = 2 WHERE  SCD_GATE_PASS_NO  = 2 AND SCD_DISC_PERCENT IN (2.25,1.75)";
             stmt.execute(sql);
             */
             /*
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =1";
             stmt.execute(sql);
            
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1 AND SCD_GATE_PASS_NO =2 AND SCD_BALE_NO =2";
             stmt.execute(sql);
            
                        
             sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL A ,SALES.D_SAL_NEW_QLTY_DESPATCH B,SALES.TMP_ABD_DETAIL SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0)  WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15'  AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1    AND SCD_GATE_PASS_NO =1 AND SCD_BALE_NO =1 AND ABDD_PARTY_CODE =SCD_PARTY_CODE AND ABDD_QUALITY_ID = SCD_QUALITY_ID AND ABDD_SHADE =SCD_SHADE AND ABDD_LIFT1_LIFT2_MTR + ABDD_LIFT2_LIFT3_MTR +0 =0";
             stmt.execute(sql);
            
   
             */
             
             
             
            /*
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND NW_ABD_DESP_DATE1 >='2015-12-21'";
            stmt.execute(sql);
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 4.50,SCD_DISC_AMOUNT = ROUND(((4.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT NOT IN (4.50,1.75,2.25) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 2.25,SCD_DISC_AMOUNT = ROUND(((2.25 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE <= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);
       
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL,SALES.D_SAL_NEW_QLTY_DESPATCH SET SCD_DISC_PERCENT  = 3.50,SCD_DISC_AMOUNT = ROUND(((3.50 *SCD_NET_AMOUNT)/100),0) WHERE SCD_QUALITY_ID = NW_QLTY_ID+0 AND NW_QLTY_SEASONID = 'W15' AND SCD_DISC_PERCENT IN (1.75) AND SCD_INVOICE_DATE >= NW_ABD_DESP_DATE1"; 
            stmt.execute(sql);

            
            
            */
            
            
            
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ABDI_NET_AMOUNT WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT < 0 AND ABDI_NET_AMOUNT - SCD_NET_AMOUNT > -1";
            stmt.execute(sql);   
            
            sql="UPDATE SALES.TMP_SAL_SCHEME_DETAIL , SALES.TMP_ABD_INVOICE SET SCD_NET_AMOUNT = ROUND(((SCD_NET_QTY*SCD_INV_RATE)  - ((SCD_NET_QTY*SCD_INV_RATE)*(ABDI_DEF_DISC / 100))),2)  WHERE ABDI_INVOICE_NO  = SCD_INVOICE_NO AND ABDI_INVOICE_DATE  = SCD_INVOICE_DATE AND ABDI_PARTY_CODE  = SCD_PARTY_CODE AND SCD_PIECE_NO = ABDI_PIECE_NO AND SCD_QUALITY_ID = ABDI_QLTY_ID AND SCD_SHADE = ABDI_SHADE AND ABDI_NET_AMOUNT != SCD_NET_AMOUNT";
            stmt.execute(sql);   
            
            sql ="UPDATE SALES.TMP_SAL_SCHEME_DETAIL SET SCD_DISC_AMOUNT = ROUND((SCD_NET_AMOUNT*SCD_DISC_PERCENT/100),2)";
            stmt.execute(sql); 
            
            
            
        }catch(SQLException sqe){
            sqe.printStackTrace();            
        }     
        jLabelStatus.setText("Generate Scheme Detail Table Complete");
        System.out.println("Scheme Detail Done");
        // ABD Discount % end
                
            //  System.exit(0);                                
            ((JFrame)getParent().getParent().getParent().getParent()).dispose();
            }
        }.start();
        //((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }  
     
     
     
     
     
       
}


