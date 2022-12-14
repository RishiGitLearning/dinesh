/* frmFeltSalesInfo.java 
 * 
 * 
 * Created on July 13, 2005, 10:47 AM
 */
package EITLERP.Suitings.SuitingGR;

import EITLERP.*;
import java.awt.Color;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.table.JTableHeader;


/**
 *
 * @author ashutosh/RISHI
 */
public class frmGSTRInfoSuitingGR extends javax.swing.JApplet {
    public boolean forceToChange=false;
    public boolean cancelled=false;
    private EITLTableModel[] DataModel;
    private EITLTableModel DataModelCDNR = new EITLTableModel();  
    
    String strProductCode = "";
    private JDialog aDialog;
    DateFormat df =  new SimpleDateFormat("dd-MMM-yy");
    HashMap hmPieceList=new HashMap();
    String ORDER_BY="";
    private clsExcelExporterSTGGR exp = new clsExcelExporterSTGGR();
    
    public void init() {
        initComponents();
        file1.setVisible(false);
        FormatGrid();//780, 560
        setSize(780,560);
        jLabel1.setForeground(Color.WHITE);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblStatus = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        Table_CDNR = new javax.swing.JTable();
        btnShowCDNR = new javax.swing.JButton();
        Export_CDNR = new javax.swing.JButton();
        file1 = new javax.swing.JFileChooser();
        CLR_BTN = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel2.setBackground(new java.awt.Color(0, 102, 153));
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 70, 780, 10);

        txtFromDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(170, 30, 110, 30);

        txtToDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToDateFocusGained(evt);
            }
        });
        getContentPane().add(txtToDate);
        txtToDate.setBounds(380, 30, 100, 30);

        jPanel10.setLayout(null);

        Table_CDNR.setModel(new javax.swing.table.DefaultTableModel(
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
        Table_CDNR.setSelectionBackground(new java.awt.Color(208, 220, 234));
        Table_CDNR.setSelectionForeground(new java.awt.Color(231, 16, 16));
        Table_CDNR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table_CDNRKeyPressed(evt);
            }
        });
        jScrollPane10.setViewportView(Table_CDNR);

        jPanel10.add(jScrollPane10);
        jScrollPane10.setBounds(10, 50, 720, 370);

        btnShowCDNR.setText("Show List");
        btnShowCDNR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCDNRActionPerformed(evt);
            }
        });
        jPanel10.add(btnShowCDNR);
        btnShowCDNR.setBounds(10, 10, 130, 30);

        Export_CDNR.setText("EXPORT TO EXCEL");
        Export_CDNR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Export_CDNRActionPerformed(evt);
            }
        });
        jPanel10.add(Export_CDNR);
        Export_CDNR.setBounds(540, 10, 170, 33);
        jPanel10.add(file1);
        file1.setBounds(150, 0, 435, 380);

        jTabbedPane2.addTab("CDNR(9B)", jPanel10);

        getContentPane().add(jTabbedPane2);
        jTabbedPane2.setBounds(10, 80, 760, 470);

        CLR_BTN.setText("Clear");
        CLR_BTN.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CLR_BTN.setMargin(new java.awt.Insets(2, 7, 2, 7));
        CLR_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLR_BTNActionPerformed(evt);
            }
        });
        getContentPane().add(CLR_BTN);
        CLR_BTN.setBounds(540, 30, 80, 30);

        jLabel3.setText("Period : ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 30, 60, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("From Date :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(60, 30, 100, 20);

        jLabel5.setText("To Date :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(310, 30, 70, 20);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("GSTR Information of Suiting GR");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 780, 25);
    }// </editor-fold>//GEN-END:initComponents

    private void txtToDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToDateFocusGained
                                 
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        
    }//GEN-LAST:event_formMouseClicked

    private void Table_CDNRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_CDNRKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_CDNRKeyPressed

    private void btnShowCDNRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCDNRActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        FormatGrid();
        GenerateCDNRData(ORDER_BY);
    }//GEN-LAST:event_btnShowCDNRActionPerformed

    private void Export_CDNRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_CDNRActionPerformed
        //        // TODO add your handling code here:
        //        try{
            //            exp.fillData(Table_CDNR,new File("/root/Desktop/gstr1_CDNR.xls"));
            //            exp.fillData(Table_CDNR,new File("D://gstr1_CDNR.xls"));
            //            JOptionPane.showMessageDialog(null, "Data saved at " +
                //                "'/root/Desktop/gstr1_CDNR.xls' successfully in Linux PC or 'D://gstr1_CDNR.xls' successfully in Windows PC    ", "Message",
                //                JOptionPane.INFORMATION_MESSAGE);
            //        }
        //        catch(Exception ex) {
            //            ex.printStackTrace();
            //        }
        // TODO add your handling code here:
        File file = null;
        file1.setVisible(true);
        try {
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }

            exp.fillData(Table_CDNR, file, "CDNR_GSTR1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                + file1.getSelectedFile().toString() + " successfully... ", "Message",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }//GEN-LAST:event_Export_CDNRActionPerformed

    private void CLR_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLR_BTNActionPerformed
        txtFromDate.setText("");
        txtToDate.setText("");
        FormatGrid();
    }//GEN-LAST:event_CLR_BTNActionPerformed
                
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CLR_BTN;
    private javax.swing.JButton Export_CDNR;
    private javax.swing.JTable Table_CDNR;
    private javax.swing.JButton btnShowCDNR;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private boolean Validate() {
        //Form level validations
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter from Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
    private void FormatGrid() {
        try {
            DataModelCDNR = new EITLTableModel();
            Table_CDNR.removeAll();

            Table_CDNR.setModel(DataModelCDNR);
            Table_CDNR.setAutoResizeMode(0);

            DataModelCDNR.addColumn("SrNo"); //0 - Read Only
            DataModelCDNR.addColumn("GSTIN/UIN of Recipient"); //1
            DataModelCDNR.addColumn("Invoice Number"); //2
            DataModelCDNR.addColumn("Invoice date"); //3
            DataModelCDNR.addColumn("Note/Refund Voucher No"); //4
            DataModelCDNR.addColumn("Note/Refund Voucher Date"); //5
            DataModelCDNR.addColumn("Document Type"); //6
            DataModelCDNR.addColumn("Reason for Issuing document"); //7
            DataModelCDNR.addColumn("Place Of Supply"); //8
            DataModelCDNR.addColumn("Note/Refund Voucher Value"); //9
            DataModelCDNR.addColumn("Rate"); //10
            DataModelCDNR.addColumn("Taxable Value"); //11
            DataModelCDNR.addColumn("Cess Amount"); //12
            DataModelCDNR.addColumn("Pre GST"); //13
            
            
            DataModelCDNR.SetNumeric(9, true);
            DataModelCDNR.SetNumeric(10, true);
            DataModelCDNR.SetNumeric(11, true);
            DataModelCDNR.SetNumeric(12, true);
                        
            for(int i=0;i<=13;i++) {
                DataModelCDNR.SetReadOnly(i);
            }
                        
            Table_CDNR.getColumnModel().getColumn(0).setMinWidth(30);
            Table_CDNR.getColumnModel().getColumn(0).setMaxWidth(40);
            Table_CDNR.getColumnModel().getColumn(1).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(1).setMaxWidth(70);
            Table_CDNR.getColumnModel().getColumn(2).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(2).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(3).setMinWidth(80);
            Table_CDNR.getColumnModel().getColumn(4).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(4).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(5).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(5).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(6).setMinWidth(50);
            //Table_CDNR.getColumnModel().getColumn(6).setMaxWidth(100);
            Table_CDNR.getColumnModel().getColumn(7).setMinWidth(120);
            //Table_CDNR.getColumnModel().getColumn(7).setMaxWidth(100);
            Table_CDNR.getColumnModel().getColumn(8).setMinWidth(120);
            //Table_CDNR.getColumnModel().getColumn(8).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(9).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(9).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(10).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(10).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(11).setMinWidth(80);
            //Table_CDNR.getColumnModel().getColumn(11).setMaxWidth(80);
            Table_CDNR.getColumnModel().getColumn(12).setMinWidth(80);
            Table_CDNR.getColumnModel().getColumn(13).setMinWidth(50);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : "+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void GenerateCDNRData(String ORDER_BY) {
        
        try {
            String GSTIN_NO="",StateCd="",POS="";
            //String strSQL = "SELECT *,RIGHT(HSN_CODE,4) AS HSN,CASE WHEN CGST_AMT=0 THEN '0.00' ELSE CGST_PER END AS PER_CGST,CASE WHEN SGST_AMT=0 THEN '0.00' ELSE SGST_PER END AS PER_SGST,CASE WHEN IGST_AMT=0 THEN '0.00' ELSE IGST_PER END AS PER_IGST FROM FINANCE.D_SAL_GSTR_INVOICE WHERE INVOICE_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND INVOICE_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"' AND APPROVED=1 AND CANCELLED=0 ORDER BY INVOICE_DATE,INVOICE_NO "+ORDER_BY;
//            String strSQL = "SELECT HSN.*,GRH.PARTY_CODE,H.VOUCHER_NO,H.VOUCHER_DATE,D.AMOUNT ";
//            strSQL += "FROM STGSALES.D_SAL_HSN_INVOICE_POSTING HSN,STGSALES.D_STG_GOODS_RETURNS_HEADER GRH, ";
//            strSQL += "FINANCE.D_FIN_VOUCHER_HEADER H,FINANCE.D_FIN_VOUCHER_DETAIL D ";
//            strSQL += "WHERE GRH.GR_ID=HSN.GR_ID AND GRH.GR_DATE=HSN.GR_DATE ";
//            strSQL += "AND GRH.APPROVED=1 AND GRH.CANCELED=0 ";
//            strSQL += "AND GRH.GR_ID=D.GR_NO AND GRH.GR_DATE=D.GR_DATE ";
//            strSQL += "AND D.VOUCHER_NO=H.VOUCHER_NO ";
//            strSQL += "AND H.VOUCHER_NO LIKE 'CN__13%' ";
//            strSQL += "AND H.VOUCHER_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND H.VOUCHER_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"'  "; 
//            strSQL += "AND H.APPROVED=1 AND H.CANCELLED=0 AND H.BOOK_CODE=13 AND H.REASON_CODE=28 ";
            
            String strSQL = "SELECT HSN.*,GRH.PARTY_CODE,GRH.GRCN_NO,GRH.GRCN_DATE,SUM(D.AMOUNT) AS AMOUNT  ";
            strSQL += "FROM STGSALES.D_SAL_HSN_INVOICE_POSTING HSN,STGSALES.D_STG_GOODS_RETURNS_HEADER GRH, ";
            strSQL += "FINANCE.D_FIN_VOUCHER_HEADER H,FINANCE.D_FIN_VOUCHER_DETAIL D ";
            strSQL += "WHERE GRH.GR_ID=HSN.GR_ID AND GRH.GR_DATE=HSN.GR_DATE ";
            strSQL += "AND GRH.APPROVED=1 AND GRH.CANCELED=0 ";
            strSQL += "AND GRH.GR_ID=D.GR_NO AND GRH.GR_DATE=D.GR_DATE ";
            strSQL += "AND HSN.INVOICE_NO=D.GR_INVOICE_NO ";
            strSQL += "AND HSN.INVOICE_DATE=D.GR_INVOICE_DATE ";
            strSQL += "AND GRH.GRCN_NO=H.VOUCHER_NO AND GRH.GRCN_DATE=H.VOUCHER_DATE ";
            strSQL += "AND H.VOUCHER_NO LIKE 'CN__13%' ";
            strSQL += "AND H.VOUCHER_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND H.VOUCHER_DATE<= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"'  "; 
            strSQL += "AND H.APPROVED=1 AND H.CANCELLED=0 AND H.BOOK_CODE=13 AND H.REASON_CODE=28 ";
            strSQL += "AND D.EFFECT='C' ";
            strSQL += "GROUP BY HSN.INVOICE_NO,HSN.INVOICE_DATE ";
            
            ResultSet rs = data.getResult(strSQL);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {

                Object[] rowData = new Object[25];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;
                GSTIN_NO = data.getStringValueFromDB("SELECT GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+rs.getString("PARTY_CODE")+"' AND MAIN_ACCOUNT_CODE=210027 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0 ");
                POS = clsPlaceOfSupply.PlaceOfSupply(GSTIN_NO.substring(0,2));
                rowData[1] = GSTIN_NO;
                rowData[2] = rs.getString("INVOICE_NO");
                rowData[3] = df.format(rs.getDate("INVOICE_DATE"));
                rowData[4] = rs.getString("GRCN_NO");
                rowData[5] = df.format(rs.getDate("GRCN_DATE"));
                rowData[6] = "C";
                rowData[7] = "01-Sales Return";
                rowData[8] = POS;
                rowData[9] = rs.getString("AMOUNT");
                rowData[10] = Double.parseDouble("5");
                rowData[11] = rs.getString("NET_GR_AMOUNT");
                rowData[12] = Double.parseDouble("0");
                rowData[13] = "N";
                
                DataModelCDNR.addRow(rowData);
                rs.next();
            }
            rs.close();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }
    
}