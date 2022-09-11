/* frmFeltSalesInfo.java 
 * 
 * 
 * Created on July 13, 2005, 10:47 AM
 */
package EITLERP.Finance.ReportsUI;

import EITLERP.Stores.QueryReport.New.*;
import EITLERP.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
public class frmRptCreditPartyAnalysis extends javax.swing.JApplet {
    public boolean forceToChange=false;
    public boolean cancelled=false;
    private EITLTableModel[] DataModel;
    private EITLTableModel DataModelB2B = new EITLTableModel();  
    private EITLTableModel DataModelB2BMIR = new EITLTableModel();  
   //private EITLTableModel DataModelB2BHSN = new EITLTableModel();  
    
    String strProductCode = "";
    private JDialog aDialog;
    DateFormat df =  new SimpleDateFormat("dd-MMM-yy");
    HashMap hmPieceList=new HashMap();
    String ORDER_BY="";
    //private clsExcelExporter exp = new clsExcelExporter();
    //private EITLERP.Production.FeltCreditNote.clsExcelExporter exp = new EITLERP.Production.FeltCreditNote.clsExcelExporter();
    //private clsExcelExporterB2BHSN expHSN = new clsExcelExporterB2BHSN();
    private clsExcel_Exporter exp = new clsExcel_Exporter();
    
    public void init() {
        initComponents();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        //setSize(780,660);
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
        file1 = new javax.swing.JFileChooser();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtSupplierCode = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnShowB2BMIR = new javax.swing.JButton();
        Export_B2BMIR = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        tableCrDetail = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtTotalAmt = new javax.swing.JTextField();
        CLR_BTN = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();

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
        jLabel2.setBounds(0, 70, 930, 10);

        txtFromDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(170, 30, 110, 30);

        txtSupplierCode = new JTextFieldHint(new JTextField(),"Press F1");
        txtSupplierCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSupplierCodeFocusGained(evt);
            }
        });
        txtSupplierCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierCodeKeyPressed(evt);
            }
        });
        getContentPane().add(txtSupplierCode);
        txtSupplierCode.setBounds(560, 30, 100, 30);

        jPanel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jPanel1.setLayout(null);

        btnShowB2BMIR.setText("Show List");
        btnShowB2BMIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowB2BMIRActionPerformed(evt);
            }
        });
        jPanel1.add(btnShowB2BMIR);
        btnShowB2BMIR.setBounds(10, 10, 130, 30);

        Export_B2BMIR.setText("EXPORT TO EXCEL");
        Export_B2BMIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Export_B2BMIRActionPerformed(evt);
            }
        });
        jPanel1.add(Export_B2BMIR);
        Export_B2BMIR.setBounds(540, 10, 170, 27);

        tableCrDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        tableCrDetail.setSelectionBackground(new java.awt.Color(208, 220, 234));
        tableCrDetail.setSelectionForeground(new java.awt.Color(231, 16, 16));
        tableCrDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableCrDetailKeyPressed(evt);
            }
        });
        jScrollPane11.setViewportView(tableCrDetail);

        jPanel1.add(jScrollPane11);
        jScrollPane11.setBounds(10, 50, 910, 400);

        jLabel7.setText("Total Amount");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(290, 460, 110, 30);

        txtTotalAmt.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jPanel1.add(txtTotalAmt);
        txtTotalAmt.setBounds(410, 460, 130, 27);

        jTabbedPane2.addTab("DETAIL", jPanel1);

        getContentPane().add(jTabbedPane2);
        jTabbedPane2.setBounds(0, 80, 930, 560);

        CLR_BTN.setText("Clear");
        CLR_BTN.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        CLR_BTN.setMargin(new java.awt.Insets(2, 7, 2, 7));
        CLR_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLR_BTNActionPerformed(evt);
            }
        });
        getContentPane().add(CLR_BTN);
        CLR_BTN.setBounds(800, 30, 80, 30);

        jLabel3.setText("Period : ");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 30, 60, 30);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("From Date :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(60, 30, 100, 30);

        jLabel5.setText("Supp Code :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(470, 30, 90, 30);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("CREDIT PARTY ANALYSIS");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 930, 25);

        jLabel6.setText("To Date :");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(290, 30, 70, 30);

        txtToDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToDateFocusGained(evt);
            }
        });
        getContentPane().add(txtToDate);
        txtToDate.setBounds(360, 30, 100, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSupplierCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSupplierCodeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupplierCodeFocusGained
                                 
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        
    }//GEN-LAST:event_formMouseClicked

    private void CLR_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLR_BTNActionPerformed
        txtFromDate.setText("");
        txtSupplierCode.setText("");        
        txtToDate.setText("");
        
    }//GEN-LAST:event_CLR_BTNActionPerformed

    private void tableCrDetailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCrDetailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCrDetailKeyPressed

    private void Export_B2BMIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_B2BMIRActionPerformed
        // TODO add your handling code here:
        /*try {
            exp.fillData(Table_b2bMIR, new File("/root/Desktop/MIR_B2B.xls"));
            exp.fillData(Table_b2bMIR, new File("D://MIR_B2B.xls"));
            File file = new File("/root/Desktop/MIR_B2B.xls");
            File file1 = new File("D://MIR_B2B.xls");
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop Not Supported");
                return;
            } else {
                Desktop desk = Desktop.getDesktop();
                if (file.exists()) {
                    desk.open(file);
                } else if (file1.exists()) {
                    desk.open(file1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        File file = null;
        file1.setVisible(true);
        try {
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);
            
            exp.fillData(tableCrDetail, new File(file1.getSelectedFile().toString() + ".xls"), "GSTR2");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }//GEN-LAST:event_Export_B2BMIRActionPerformed

    private void btnShowB2BMIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowB2BMIRActionPerformed
        
        if ( ! Validate()) {
            return;
        }
        FormatGridMIR();
        GenerateCrPartyDetail(ORDER_BY);
    }//GEN-LAST:event_btnShowB2BMIRActionPerformed

    private void txtToDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToDateFocusGained

    private void txtSupplierCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierCodeKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM DINESHMILLS.D_COM_SUPP_MASTER WHERE APPROVED=1 AND CANCELLED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtSupplierCode.setText(aList.ReturnVal);                
            }
        }
    }//GEN-LAST:event_txtSupplierCodeKeyPressed
                
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CLR_BTN;
    private javax.swing.JButton Export_B2BMIR;
    private javax.swing.JButton btnShowB2BMIR;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tableCrDetail;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtSupplierCode;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtTotalAmt;
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
    
    
    private void FormatGridMIR() {
        try {
            DataModelB2BMIR = new EITLTableModel();
            tableCrDetail.removeAll();

            tableCrDetail.setModel(DataModelB2BMIR);
            tableCrDetail.setAutoResizeMode(0);

            DataModelB2BMIR.addColumn("SrNo"); //0 - Read Only
            DataModelB2BMIR.addColumn("SUPP CODE"); //1
            DataModelB2BMIR.addColumn("SUPP NAME"); //2            
            DataModelB2BMIR.addColumn("PAYMENT DAYS"); //4
            DataModelB2BMIR.addColumn("AMOUNT"); //5           
            
            
           
            /*
            DataModelB2BMIR.SetVariable(0, "SR_N0"); 
            DataModelB2BMIR.SetVariable(1, "GSTIN_UIN"); 
            DataModelB2BMIR.SetVariable(2, "TRADE_NAME"); 
            DataModelB2BMIR.SetVariable(3, "INVOICE_NO"); 
            DataModelB2BMIR.SetVariable(4, "INVOICE_DATE"); 
            DataModelB2BMIR.SetVariable(5, "INVOICE_AMT");
            DataModelB2BMIR.SetVariable(6, "TAXABLE_VALUE");
            DataModelB2BMIR.SetVariable(7, "HSN_CODE");
            DataModelB2BMIR.SetVariable(8, "GST_RATE");
            DataModelB2BMIR.SetVariable(9, "IGST_AMT");
            DataModelB2BMIR.SetVariable(10, "CGST_AMT");
            DataModelB2BMIR.SetVariable(11, "SGST_AMT");
            DataModelB2BMIR.SetVariable(12, "CESS");
            DataModelB2BMIR.SetVariable(13, "POS");
            DataModelB2BMIR.SetVariable(14, "ITEM_ID");
            DataModelB2BMIR.SetVariable(15, "IGST_AMT_ITC");
            DataModelB2BMIR.SetVariable(16, "CGST_AMT_ITC");
            DataModelB2BMIR.SetVariable(17, "SGST_AMT_ITC");
            DataModelB2BMIR.SetVariable(18, "CESS_ITC");
            DataModelB2BMIR.SetVariable(19, "INVOICE_NO_AMEND");
            DataModelB2BMIR.SetVariable(20, "INVOICE_DATE_AMEND");
            */
            
            
            for(int i=0;i<=17;i++) {
                DataModelB2BMIR.SetReadOnly(i);
            }
                        
            tableCrDetail.getColumnModel().getColumn(0).setMinWidth(30);
            tableCrDetail.getColumnModel().getColumn(0).setMaxWidth(40);
            tableCrDetail.getColumnModel().getColumn(1).setMinWidth(100);
            //Table_b2bMIR.getColumnModel().getColumn(1).setMaxWidth(70);
            tableCrDetail.getColumnModel().getColumn(2).setMinWidth(200);
            //Table_b2bMIR.getColumnModel().getColumn(2).setMaxWidth(80);
            //Table_b2bMIR.getColumnModel().getColumn(3).setMinWidth(100);
            tableCrDetail.getColumnModel().getColumn(3).setMinWidth(100);
            //Table_b2bMIR.getColumnModel().getColumn(4).setMaxWidth(80);
            tableCrDetail.getColumnModel().getColumn(4).setMinWidth(100);
            //Table_b2bMIR.getColumnModel().getColumn(5).setMaxWidth(80);
            
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : "+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void GenerateGRNB2BData(String ORDER_BY) {
        
        try {
            
            
//            data.Execute("");
            /*
            data.Execute("TRUNCATE TABLE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT");
            
            
            data.Execute("INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT (SUPP_ID,INVOICE_NO, INVOICE_DATE, INVOICE_AMT,  ITEM_ID, GRN_NO, GRN_DATE, QTY, UNIT, RATE, TOTAL_AMOUNT, NET_AMOUNT,HSN_CODE,IGST_RATE,CGST_RATE,SGST_RATE)  "
                    + " ( SELECT H.SUPP_ID,H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMOUNT, D.ITEM_ID,H.GRN_NO,H.GRN_DATE,D.QTY,D.UNIT,D.RATE,D.TOTAL_AMOUNT,D.NET_AMOUNT,D.HSN_SAC_CODE,D.COLUMN_3_PER,D.COLUMN_4_PER,D.COLUMN_5_PER FROM DINESHMILLS.D_INV_GRN_HEADER H,DINESHMILLS.D_INV_GRN_DETAIL D " +
                        "WHERE H.COMPANY_ID=2 AND " +
                        "H.GRN_NO IN ( SELECT distinct B.GRN_NO FROM FINANCE.D_FIN_VOUCHER_HEADER A , FINANCE.D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.GRN_NO!='' AND A.VOUCHER_DATE >= '"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND A.VOUCHER_DATE <= '"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' AND SUBSTRING(A.VOUCHER_NO,1,2) = 'PJ' AND A.APPROVED =1 AND A.CANCELLED =0) " +
                        "AND H.GRN_NO=D.GRN_NO AND H.APPROVED =1 AND H.CANCELLED =0 ORDER BY H.GRN_NO)");
            
            
            data.Execute("UPDATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT A,DINESHMILLS.D_COM_SUPP_MASTER B " +
                        " SET A.TYPE=2,A.TRADE_NAME=B.SUPP_NAME,A.GSTIN_UIN=B.GSTIN_NO,A.POS=B.STATE " +
                        " WHERE A.SUPP_ID = B.SUPPLIER_CODE"); 
            
            data.Execute("UPDATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT A,DINESHMILLS.D_INV_GRN_HSN B " +
                        " SET A.TAXABLE_VALUE=B.INVOICE_AMOUNT,A.IGST_AMT=B.INVOICE_IGST,A.CGST_AMT=B.INVOICE_CGST" +
                        ",A.SGST_AMT=B.INVOICE_SGST,A.CESS=B.INVOICE_GST_COMP_CESS,A.CGST_AMT_ITC=B.CGST_INPUT_CR_AMT,"
                        + "A.SGST_AMT_ITC=B.SGST_INPUT_CR_AMT,A.IGST_AMT_ITC=B.IGST_INPUT_CR_AMT " +
                        " WHERE A.GRN_NO=B.GRN_NO AND A.GRN_DATE=B.GRN_DATE AND A.HSN_CODE=B.HSN_CODE");
            */
            
            data.Execute("TRUNCATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT");
            /*
            data.Execute(" INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT (" +
                        " GRN_NO,GRN_DATE,TAXABLE_VALUE,HSN_CODE,SGST_AMT,CGST_AMT,IGST_AMT,SGST_AMT_ITC,CGST_AMT_ITC,IGST_AMT_ITC,SGST_RATE,CGST_RATE,IGST_RATE,SUPP_ID,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,CESS)\n" +
                        " " +
                        " SELECT H.GRN_NO,H.GRN_DATE,G.INVOICE_AMOUNT,HSN_CODE,INVOICE_SGST,INVOICE_CGST,INVOICE_IGST,SGST_INPUT_CR_AMT,CGST_INPUT_CR_AMT,IGST_INPUT_CR_AMT,SGST_INPUT_CR_PER,CGST_INPUT_CR_PER,IGST_INPUT_CR_PER,SUPP_ID,INVOICE_NO,INVOICE_DATE,H.INVOICE_AMOUNT,G.INVOICE_GST_COMP_CESS FROM DINESHMILLS.D_INV_GRN_HSN G,DINESHMILLS.D_INV_GRN_HEADER H " +
                        " " +
                        " WHERE H.GRN_DATE >='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND H.GRN_DATE <='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"'  AND G.GRN_NO=H.GRN_NO AND H.APPROVED =1 AND H.CANCELLED =0 ");
            */
            data.Execute("INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT ( " +
                        " GRN_NO,GRN_DATE,TAXABLE_VALUE,HSN_CODE,SGST_AMT,CGST_AMT,IGST_AMT,SGST_AMT_ITC,CGST_AMT_ITC,IGST_AMT_ITC,SGST_RATE,CGST_RATE,IGST_RATE,SUPP_ID,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,CESS) " +
                        " SELECT H.GRN_NO,H.GRN_DATE,G.INVOICE_AMOUNT,HSN_CODE,INVOICE_SGST,INVOICE_CGST,INVOICE_IGST,SGST_INPUT_CR_AMT,CGST_INPUT_CR_AMT,IGST_INPUT_CR_AMT,SGST_INPUT_CR_PER,CGST_INPUT_CR_PER,IGST_INPUT_CR_PER,SUPP_ID,INVOICE_NO,INVOICE_DATE,H.INVOICE_AMOUNT,G.INVOICE_GST_COMP_CESS FROM DINESHMILLS.D_INV_GRN_HSN G,DINESHMILLS.D_INV_GRN_HEADER H " +
                        " WHERE G.GRN_NO= H.GRN_NO AND H.APPROVED =1 AND H.CANCELLED =0 " +
                        " AND H.GRN_NO IN ( SELECT distinct B.GRN_NO FROM FINANCE.D_FIN_VOUCHER_HEADER A , FINANCE.D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.GRN_NO!='' AND A.VOUCHER_DATE >= '"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND A.VOUCHER_DATE <= '"+EITLERPGLOBAL.formatDateDB(txtSupplierCode.getText())+"' AND SUBSTRING(A.VOUCHER_NO,1,2) = 'PJ' AND A.APPROVED =1 AND A.CANCELLED =0)");
            
            System.out.println("INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT ( " +
                        " GRN_NO,GRN_DATE,TAXABLE_VALUE,HSN_CODE,SGST_AMT,CGST_AMT,IGST_AMT,SGST_AMT_ITC,CGST_AMT_ITC,IGST_AMT_ITC,SGST_RATE,CGST_RATE,IGST_RATE,SUPP_ID,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,CESS) " +
                        " SELECT H.GRN_NO,H.GRN_DATE,G.INVOICE_AMOUNT,HSN_CODE,INVOICE_SGST,INVOICE_CGST,INVOICE_IGST,SGST_INPUT_CR_AMT,CGST_INPUT_CR_AMT,IGST_INPUT_CR_AMT,SGST_INPUT_CR_PER,CGST_INPUT_CR_PER,IGST_INPUT_CR_PER,SUPP_ID,INVOICE_NO,INVOICE_DATE,H.INVOICE_AMOUNT,G.INVOICE_GST_COMP_CESS FROM DINESHMILLS.D_INV_GRN_HSN G,DINESHMILLS.D_INV_GRN_HEADER H " +
                        " WHERE G.GRN_NO= H.GRN_NO AND H.APPROVED =1 AND H.CANCELLED =0 " +
                        " AND H.GRN_NO IN ( SELECT distinct B.GRN_NO FROM FINANCE.D_FIN_VOUCHER_HEADER A , FINANCE.D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.GRN_NO!='' AND A.VOUCHER_DATE >= '"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND A.VOUCHER_DATE <= '"+EITLERPGLOBAL.formatDateDB(txtSupplierCode.getText())+"' AND SUBSTRING(A.VOUCHER_NO,1,2) = 'PJ' AND A.APPROVED =1 AND A.CANCELLED =0)");
            
            data.Execute("INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT (  GRN_NO,GRN_DATE,TAXABLE_VALUE,HSN_CODE,SGST_AMT,CGST_AMT,IGST_AMT,SGST_AMT_ITC,CGST_AMT_ITC,IGST_AMT_ITC,SGST_RATE,CGST_RATE,IGST_RATE,SUPP_ID,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,CESS)  \n"
                    + " SELECT H.JOB_NO,H.JOB_DATE,G.INVOICE_AMOUNT,HSN_CODE,INVOICE_SGST,INVOICE_CGST,INVOICE_IGST,SGST_INPUT_CR_AMT,CGST_INPUT_CR_AMT,IGST_INPUT_CR_AMT,SGST_INPUT_CR_PER,CGST_INPUT_CR_PER,IGST_INPUT_CR_PER,SUPP_ID,INVOICE_NO,INVOICE_DATE,H.INVOICE_AMOUNT,G.INVOICE_GST_COMP_CESS FROM DINESHMILLS.D_INV_JOB_HSN G,DINESHMILLS.D_INV_JOB_HEADER H \n"
                    + " WHERE G.JOB_NO=H.JOB_NO AND H.APPROVED=1 AND H.CANCELLED=0 AND H.JOB_NO IN ( SELECT distinct B.GRN_NO FROM FINANCE.D_FIN_VOUCHER_HEADER A , FINANCE.D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.GRN_NO!='' AND A.VOUCHER_DATE >= '"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND A.VOUCHER_DATE <= '"+EITLERPGLOBAL.formatDateDB(txtSupplierCode.getText())+"' AND SUBSTRING(A.VOUCHER_NO,1,2) = 'PJ' AND A.APPROVED =1 AND A.CANCELLED =0)");
            
           data.Execute("INSERT INTO DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT (  GRN_NO,GRN_DATE,TAXABLE_VALUE,HSN_CODE,SGST_AMT,CGST_AMT,IGST_AMT,SGST_AMT_ITC,CGST_AMT_ITC,IGST_AMT_ITC,SGST_RATE,CGST_RATE,IGST_RATE,SUPP_ID,INVOICE_NO,INVOICE_DATE,INVOICE_AMT,CESS)  "
                   + " SELECT H.JOB_NO,H.JOB_DATE,G.INVOICE_AMOUNT,HSN_CODE,INVOICE_SGST,INVOICE_CGST,INVOICE_IGST,SGST_INPUT_CR_AMT,CGST_INPUT_CR_AMT,IGST_INPUT_CR_AMT,SGST_INPUT_CR_PER,CGST_INPUT_CR_PER,IGST_INPUT_CR_PER,SUPP_ID,INVOICE_NO,INVOICE_DATE,H.INVOICE_AMOUNT,G.INVOICE_GST_COMP_CESS FROM DINESHMILLS.D_INV_JOB_HSN G,DINESHMILLS.D_INV_JOB_HEADER H "
                   + " WHERE G.JOB_NO=H.JOB_NO AND H.APPROVED=1 AND H.CANCELLED=0 AND H.JOB_NO IN ( SELECT distinct B.GRN_NO FROM FINANCE.D_FIN_VOUCHER_HEADER A , FINANCE.D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.GRN_NO!='' AND A.VOUCHER_DATE >= '2020-06-01' AND A.VOUCHER_DATE <= '2020-06-30' AND SUBSTRING(A.VOUCHER_NO,1,2) = 'PJ' AND A.APPROVED =1 AND A.CANCELLED =0)");
            
            //data.Execute("UPDATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT A,DINESHMILLS.D_COM_SUPP_MASTER B  SET A.TYPE=2,A.TRADE_NAME=B.SUPP_NAME,A.GSTIN_UIN=B.GSTIN_NO,A.POS=B.STATE WHERE A.SUPP_ID = B.SUPPLIER_CODE");
            
           data.Execute("UPDATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT A,FINANCE.D_FIN_PARTY_MASTER B  SET A.TYPE=2,A.TRADE_NAME=B.PARTY_NAME,A.GSTIN_UIN=B.GSTIN_NO,A.POS=B.STATE WHERE A.SUPP_ID = B.PARTY_CODE AND MAIN_ACCOUNT_CODE NOT IN (210027,210010,210072)");
           
            data.Execute("UPDATE DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT SET ITEM_ID = CASE WHEN (SGST_AMT_ITC+CGST_AMT_ITC+IGST_AMT_ITC) = 0 THEN 'Ineligible' ELSE 'Inputs' END,GST_RATE = SGST_RATE+CGST_RATE+IGST_RATE");
            
            
            String strSQL = "SELECT * FROM DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT";
            
            //String strSQL2 = "SELECT *,(IGST_RATE+CGST_RATE+SGST_RATE) AS GST_RATE_DISPLAY FROM DINESHMILLS.TMP_STORES_MIR_SERVICE_QUERY_REPORT";
            
            
            ResultSet rs = data.getResult(strSQL);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {
//SR_N0, GSTIN_UIN, SUPP_ID, TRADE_NAME, TYPE, INVOICE_NO, INVOICE_DATE, INVOICE_AMT, HSN_CODE, ITEM_ID, TAXABLE_VALUE, GST_RATE, IGST_RATE, IGST_AMT, CGST_RATE, CGST_AMT, SGST_RATE, SGST_AMT, CESS, POS, DIFF_PER_TAX_RATE, SUPPLY_COVER, COMMON_CREDIT, GRN_NO, GRN_DATE, QTY, UNIT, RATE, TOTAL_AMOUNT, NET_AMOUNT
                Object[] rowData = new Object[25];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;
                rowData[1] = rs.getString("GSTIN_UIN");//GSTIN_UIN
                rowData[2] = rs.getString("TRADE_NAME");//TRADE_NAME
                rowData[3] = rs.getString("INVOICE_NO");
                rowData[4] = EITLERPGLOBAL.formatDate(rs.getString("INVOICE_DATE"));
                rowData[5] = rs.getString("INVOICE_AMT");
                rowData[6] = rs.getString("TAXABLE_VALUE");//TAXABLE_VALUE
                rowData[7] = rs.getString("HSN_CODE");//HSN_CODE
                rowData[8] = rs.getString("GST_RATE");//GST_RATE
                rowData[9] = rs.getString("IGST_AMT");//IGST_AMT
                rowData[10] = rs.getString("CGST_AMT");//CGST_AMT
                rowData[11] = rs.getString("SGST_AMT");//SGST_AMT
                rowData[12] = rs.getString("CESS");//CESS
                rowData[13] = rs.getString("POS");//POS
                
//                String Eligibility = "";
//                if(rs.getString("ITEM_ID").equals("99002001"))
//                {
//                    Eligibility = "Capital Goods";
//                }
//                else if(rs.getString("ITEM_ID").equals("99004001"))
//                {
//                    Eligibility = "Input Service";
//                }
//                else
//                {
//                    Eligibility = "Inputs";
//                }
                rowData[14] = rs.getString("ITEM_ID");
                
                rowData[15] = rs.getString("IGST_AMT_ITC");//IGST_AMT
                rowData[16] = rs.getString("CGST_AMT_ITC");//CGST_AMT
                rowData[17] = rs.getString("SGST_AMT_ITC");//SGST_AMT
                rowData[18] = rs.getString("CESS_ITC");//CESS
                rowData[19] = rs.getString("INVOICE_NO_AMEND");//CESS
                //rowData[20] = rs.getString("INVOICE_DATE_AMEND");//CESS
                
                
                DataModelB2B.addRow(rowData);
                rs.next();
            }
            rs.close();
            //Object[] rowData2 = new Object[25];
            //DataModelB2BMIR.addRow(rowData2);
            
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        
        String sql;
        sql="SELECT ROUND(SUM(INVOICE_AMT),2) FROM DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT ";
        String invAmt = data.getStringValueFromDB(sql);
        
        
        sql="SELECT ROUND(SUM(TAXABLE_VALUE),2) FROM DINESHMILLS.TMP_STORES_GRN_QUERY_REPORT ";
        String taxAmt = data.getStringValueFromDB(sql);
        
    }
    
    public void GenerateCrPartyDetail(String ORDER_BY) {
        String cndtn = "";
        String cndtn1 = "";
        try {
            
            if (txtSupplierCode.getText().trim().equals("")) {
                cndtn += " AND SUB_ACCOUNT_CODE IN (SELECT SUPPLIER_CODE FROM DINESHMILLS.D_COM_SUPP_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PAYMENT_DAYS>0) ";
                cndtn1 += " AND SUB_ACCOUNT_CODE IN (SELECT SUPPLIER_CODE FROM DINESHMILLS.D_COM_SUPP_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND SUPPLIER_CODE LIKE '7%') ";
            }else{
                cndtn += " AND SUB_ACCOUNT_CODE = '"+ txtSupplierCode.getText().trim() +"' ";
            }
            
            /*String strSQL2 = "SELECT SUB_ACCOUNT_CODE,SUPP_NAME,PAYMENT_DAYS,AMOUNT FROM\n"
                    //+ "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='2019-04-01' AND H.VOUCHER_DATE<='2020-03-31' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' AND H.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "LEFT JOIN\n"
                    + "(SELECT SUPPLIER_CODE,SUPP_NAME,PAYMENT_DAYS FROM DINESHMILLS.D_COM_SUPP_MASTER) AS B\n"
                    + "ON A.SUB_ACCOUNT_CODE=B.SUPPLIER_CODE";
            */
            String strSQL2="SELECT SUB_ACCOUNT_CODE,SUPP_NAME,PAYMENT_DAYS,AMOUNT FROM\n"
                    //+ "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='2019-04-01' AND H.VOUCHER_DATE<='2020-03-31' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' AND H.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "LEFT JOIN\n"
                    + "(SELECT SUPPLIER_CODE,SUPP_NAME,PAYMENT_DAYS FROM DINESHMILLS.D_COM_SUPP_MASTER) AS B\n"
                    + "ON A.SUB_ACCOUNT_CODE=B.SUPPLIER_CODE"
                    + " UNION ALL "
                    +"SELECT SUB_ACCOUNT_CODE,SUPP_NAME,PAYMENT_DAYS,AMOUNT FROM\n"
                    //+ "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='2019-04-01' AND H.VOUCHER_DATE<='2020-03-31' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' AND H.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn1 + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "LEFT JOIN \n"
                    + "(SELECT SUPPLIER_CODE,SUPP_NAME,PAYMENT_DAYS FROM DINESHMILLS.D_COM_SUPP_MASTER) AS B\n"
                    + "ON A.SUB_ACCOUNT_CODE=B.SUPPLIER_CODE";
            
            
            System.out.println("SQL "+strSQL2);
            
            ResultSet rs = data.getResult(strSQL2);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {
//SR_N0, GSTIN_UIN, SUPP_ID, TRADE_NAME, TYPE, INVOICE_NO, INVOICE_DATE, INVOICE_AMT, HSN_CODE, ITEM_ID, TAXABLE_VALUE, GST_RATE, IGST_RATE, IGST_AMT, CGST_RATE, CGST_AMT, SGST_RATE, SGST_AMT, CESS, POS, DIFF_PER_TAX_RATE, SUPPLY_COVER, COMMON_CREDIT, GRN_NO, GRN_DATE, QTY, UNIT, RATE, TOTAL_AMOUNT, NET_AMOUNT
                Object[] rowData = new Object[10];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;                
                rowData[1] = rs.getString("SUB_ACCOUNT_CODE");
                rowData[2] = rs.getString("SUPP_NAME");                
                rowData[3] = rs.getString("PAYMENT_DAYS");
                rowData[4] = rs.getString("AMOUNT");//TAXABLE_VALUE
                
                DataModelB2BMIR.addRow(rowData);
                rs.next();
            }
            rs.close();
            //Object[] rowData2 = new Object[25];
            //DataModelB2BMIR.addRow(rowData2);
            
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
                
        String sql1,sql2;
        sql1="SELECT SUM(AMOUNT) FROM (SELECT SUB_ACCOUNT_CODE,SUPP_NAME,PAYMENT_DAYS,AMOUNT FROM\n"
                    + "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' AND H.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "LEFT JOIN\n"
                    + "(SELECT SUPPLIER_CODE,SUPP_NAME,PAYMENT_DAYS FROM DINESHMILLS.D_COM_SUPP_MASTER) AS B\n"
                    + "ON A.SUB_ACCOUNT_CODE=B.SUPPLIER_CODE ) AS X";
        sql2="SELECT SUM(AMOUNT) FROM (SELECT SUB_ACCOUNT_CODE,SUPP_NAME,PAYMENT_DAYS,AMOUNT FROM\n"
                    + "(SELECT D.SUB_ACCOUNT_CODE,SUM(AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE H.VOUCHER_NO=D.VOUCHER_NO AND H.VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim())+"' AND H.VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim())+"' AND H.APPROVED=1 AND H.CANCELLED=0 AND BOOK_CODE IN (40,41,42,43) AND EFFECT='C' AND MAIN_ACCOUNT_CODE IN ('125019','125033') " + cndtn1 + " GROUP BY SUB_ACCOUNT_CODE) AS A\n"
                    + "LEFT JOIN\n"
                    + "(SELECT SUPPLIER_CODE,SUPP_NAME,PAYMENT_DAYS FROM DINESHMILLS.D_COM_SUPP_MASTER) AS B\n"
                    + "ON A.SUB_ACCOUNT_CODE=B.SUPPLIER_CODE ) AS X";
        long invAmt1 = data.getLongValueFromDB(sql1);
        long invAmt2 = data.getLongValueFromDB(sql2);
        long invAmt=invAmt1+invAmt2;
        txtTotalAmt.setText(Long.toString(invAmt));
        System.out.println(invAmt1);
        System.out.println(invAmt2);
        System.out.println(invAmt);
        
        
        
        
    }
    
    
    
    
}