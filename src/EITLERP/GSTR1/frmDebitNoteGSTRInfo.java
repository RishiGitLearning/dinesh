/* frmFeltSalesInfo.java 
 * 
 * 
 * Created on July 13, 2005, 10:47 AM
 */
package EITLERP.GSTR1;

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
public class frmDebitNoteGSTRInfo extends javax.swing.JApplet {
    public boolean forceToChange=false;
    public boolean cancelled=false;
    private EITLTableModel[] DataModel;
    private EITLTableModel DataModelCDNR = new EITLTableModel();  
    
    String strProductCode = "";
    private JDialog aDialog;
    DateFormat df =  new SimpleDateFormat("dd-MMM-yy");
    HashMap hmPieceList=new HashMap();
    String ORDER_BY="";
    private clsExcelExporterCDNR exp = new clsExcelExporterCDNR();
    
    public void init() {
        DNDataProcess();
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
        Table_cdnr = new javax.swing.JTable();
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

        Table_cdnr.setModel(new javax.swing.table.DefaultTableModel(
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
        Table_cdnr.setSelectionBackground(new java.awt.Color(208, 220, 234));
        Table_cdnr.setSelectionForeground(new java.awt.Color(231, 16, 16));
        Table_cdnr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table_cdnrKeyPressed(evt);
            }
        });
        jScrollPane10.setViewportView(Table_cdnr);

        jPanel10.add(jScrollPane10);
        jScrollPane10.setBounds(10, 50, 720, 340);

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
        Export_CDNR.setBounds(540, 10, 170, 28);
        jPanel10.add(file1);
        file1.setBounds(150, 0, 435, 380);

        jTabbedPane2.addTab("CDNR", jPanel10);

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
        jLabel1.setText("Debit Note (GSTR) Information System");
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

    private void Table_cdnrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_cdnrKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_cdnrKeyPressed

    private void btnShowCDNRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCDNRActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        FormatGrid();
        GenerateCDNRData();
    }//GEN-LAST:event_btnShowCDNRActionPerformed

    private void Export_CDNRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_CDNRActionPerformed
        //        // TODO add your handling code here:
        //        try{
            //            exp.fillData(Table_b2b,new File("/root/Desktop/gstr1_B2B.xls"));
            //            exp.fillData(Table_b2b,new File("D://gstr1_B2B.xls"));
            //            JOptionPane.showMessageDialog(null, "Data saved at " +
                //                "'/root/Desktop/gstr1_B2B.xls' successfully in Linux PC or 'D://gstr1_B2B.xls' successfully in Windows PC    ", "Message",
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

            exp.fillData(Table_cdnr, file, "CDNR_GSTR");
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
    private javax.swing.JTable Table_cdnr;
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
            Table_cdnr.removeAll();

            Table_cdnr.setModel(DataModelCDNR);
            Table_cdnr.setAutoResizeMode(0);
								

            DataModelCDNR.addColumn("SrNo"); //0 - Read Only
            DataModelCDNR.addColumn("GSTIN/UIN of Recipient"); //1
            DataModelCDNR.addColumn("Receiver Name"); //2
            DataModelCDNR.addColumn("Invoice/Advance Receipt Number"); //3
            DataModelCDNR.addColumn("Invoice/Advance Receipt date"); //4
            DataModelCDNR.addColumn("Note/Refund Voucher Number"); //5
            DataModelCDNR.addColumn("Note/Refund Voucher date"); //6
            DataModelCDNR.addColumn("Document Type"); //7
            DataModelCDNR.addColumn("Place Of Supply"); //8
            DataModelCDNR.addColumn("Note/Refund Voucher Value"); //9
            DataModelCDNR.addColumn("Applicable % of Tax Rate"); //10
            DataModelCDNR.addColumn("Rate"); //11
            DataModelCDNR.addColumn("Taxable Value"); //12
            DataModelCDNR.addColumn("Cess Amount"); //13
            DataModelCDNR.addColumn("Pre GST"); //14
            DataModelCDNR.addColumn("IGST Amount"); //15
            DataModelCDNR.addColumn("SGST Amount"); //16
            DataModelCDNR.addColumn("CGST Amount"); //17
            DataModelCDNR.addColumn("IGST Input Credit Amount"); //18
            DataModelCDNR.addColumn("SGST Input Credit Amount"); //19
            DataModelCDNR.addColumn("CGST Input Credit Amount"); //20
            
            DataModelCDNR.SetNumeric(9, true);
            DataModelCDNR.SetNumeric(10, true);
            DataModelCDNR.SetNumeric(11, true);
            DataModelCDNR.SetNumeric(12, true);
            DataModelCDNR.SetNumeric(15, true);
            DataModelCDNR.SetNumeric(16, true);
            DataModelCDNR.SetNumeric(17, true);
            DataModelCDNR.SetNumeric(18, true);
            DataModelCDNR.SetNumeric(19, true);
            DataModelCDNR.SetNumeric(20, true);
                        
            for(int i=0;i<=14;i++) {
                DataModelCDNR.SetReadOnly(i);
            }
                        
            Table_cdnr.getColumnModel().getColumn(0).setMinWidth(50);
            //Table_cdnr.getColumnModel().getColumn(0).setMaxWidth(40);
            Table_cdnr.getColumnModel().getColumn(1).setMinWidth(80);
            //Table_cdnr.getColumnModel().getColumn(1).setMaxWidth(70);
            Table_cdnr.getColumnModel().getColumn(2).setMinWidth(80);
            //Table_cdnr.getColumnModel().getColumn(2).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(3).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(4).setMinWidth(80);
            //Table_cdnr.getColumnModel().getColumn(4).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(5).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(5).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(6).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(6).setMaxWidth(100);
            Table_cdnr.getColumnModel().getColumn(7).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(7).setMaxWidth(100);
            Table_cdnr.getColumnModel().getColumn(8).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(8).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(9).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(9).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(10).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(10).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(11).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(11).setMaxWidth(80);
            Table_cdnr.getColumnModel().getColumn(12).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(13).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(14).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(15).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(16).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(17).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(18).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(19).setMinWidth(80);
            Table_cdnr.getColumnModel().getColumn(20).setMinWidth(80);
            
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : "+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
     
    public void GenerateCDNRData() {
        
        try {
            String strSQL = "SELECT *,(IGST_PER+SGST_PER+CGST_PER) AS RATE FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR ";
            strSQL += "WHERE DEBITNOTE_VOUCHER_DATE >= '"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) +"' AND DEBITNOTE_VOUCHER_DATE <= '"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText()) +"'  "; 
            strSQL += "ORDER BY DEBITNOTE_VOUCHER_DATE,DEBITNOTE_VOUCHER_NO "; 

            ResultSet rs = data.getResult(strSQL);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {

                Object[] rowData = new Object[25];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;
                rowData[1] = rs.getString("PARTY_GSTIN_ID");
                rowData[2] = rs.getString("PARTY_SUPP_NAME");
                rowData[3] = rs.getString("INVOICE_NO");
                if (rs.getString("INVOICE_DATE").equals("") || rs.getString("INVOICE_DATE").equals("0000-00-00")) {
                    rowData[4] = "";
                } else {
                    rowData[4] = df.format(rs.getDate("INVOICE_DATE"));
                }
                rowData[5] = rs.getString("DEBITNOTE_VOUCHER_NO");
                if (rs.getString("DEBITNOTE_VOUCHER_DATE").equals("") || rs.getString("DEBITNOTE_VOUCHER_DATE").equals("0000-00-00")) {
                    rowData[6] = "";
                } else {
                    rowData[6] = df.format(rs.getDate("DEBITNOTE_VOUCHER_DATE"));
                }
                rowData[7] = rs.getString("DOCUMENT_TYPE");
                rowData[8] = rs.getString("PARTY_PLACE_OF_SUPPLY");
                rowData[9] = rs.getString("DEBIT_NOTE_AMOUNT");
                rowData[10] = rs.getString("INTEREST_PER");
                rowData[11] = rs.getInt("RATE");
//                rowData[12] = rs.getString("INTEREST_AMT");
                rowData[12] = rs.getString("INTEREST_VOUCHER_AMOUNT");
                rowData[13] = "";                
                rowData[14] = "N";
                rowData[15] = rs.getString("IGST_AMT");
                rowData[16] = rs.getString("CGST_AMT");
                rowData[17] = rs.getString("SGST_AMT");
                rowData[18] = rs.getString("IGST_CREDIT_AMOUNT");
                rowData[19] = rs.getString("CGST_CREDIT_AMOUNT");
                rowData[20] = rs.getString("SGST_CREDIT_AMOUNT");
                
                DataModelCDNR.addRow(rowData);
                rs.next();
            }
            rs.close();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }
    
    public void DNDataProcess() {
        
        String Q1 = "TRUNCATE TABLE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP";
        System.out.println("Q1 : "+Q1);
        data.Execute(Q1);
        
        
        String Q2 = "INSERT INTO  FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP ";
        Q2 += "(BOOK_CODE,DEBITNOTE_VOUCHER_NO,DEBITNOTE_VOUCHER_DATE,INVOICE_NO,INVOICE_DATE,HSN_SAC_CODE,MAIN_ACCOUNT_CODE,DB_PARTY_CODE, ";
        Q2 += "DEBIT_NOTE_AMOUNT,INTEREST_ACCOUNT_CODE,INTEREST_VOUCHER_AMOUNT, ";
        Q2 += "IGST_ACCOUNT_CODE,IGST_AMT,CGST_ACCOUNT_CODE,CGST_AMT,SGST_ACCOUNT_CODE,SGST_AMT, ";
        Q2 += "IGST_CREDIT_CODE,IGST_CREDIT_AMOUNT, ";
        Q2 += "CGST_CREDIT_CODE,CGST_CREDIT_AMOUNT, ";
        Q2 += "SGST_CREDIT_CODE,SGST_CREDIT_AMOUNT, ";
        Q2 += "CST_CODE,CST_VALUE) ";
        Q2 += "SELECT BOOK_CODE,H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE ,MAX(HSN_SAC_CODE), ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT ='D' THEN MAIN_ACCOUNT_CODE END, 0)) AS MAIN_CODE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT = 'D' THEN SUB_ACCOUNT_CODE END, 0)) AS SUB_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (210010,210027,210072,125019,125033) AND SUB_ACCOUNT_CODE !='' AND EFFECT = 'D' THEN AMOUNT END, 0)) AS TOTVALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,125019,125033,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573,437277,127547,231758,231756,231757) AND SUB_ACCOUNT_CODE ='' AND EFFECT ='C' THEN MAIN_ACCOUNT_CODE END, 0)) AS INT_CODE, ";
//        #MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573) AND SUB_ACCOUNT_CODE ='' AND EFFECT = 'C' THEN SUB_ACCOUNT_CODE END, 0)) AS SUB_CODE,
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (210010,210027,210072,125019,127570,127575,127576,127569,127572,127565,127566,127571,127567,127574,127568,127573,437277,127547,231758,231756,231757) AND SUB_ACCOUNT_CODE ='' AND EFFECT = 'C' THEN AMOUNT END, 0)) AS TOTVALUE, ";
//        #SUM(COALESCE(CASE WHEN SUB_ACCOUNT_CODE!='' THEN AMOUNT END, 0)),
//        #SUM(COALESCE(CASE WHEN SUB_ACCOUNT_CODE='' THEN AMOUNT END, 0)),
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127570,127575,127576,127569) THEN MAIN_ACCOUNT_CODE END, 0)) AS IGST_CODE , ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127570,127575,127576,127569) THEN AMOUNT END, 0)) AS IGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127572,127565,127566,127571) THEN MAIN_ACCOUNT_CODE END, 0)) AS CGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127572,127565,127566,127571) THEN AMOUNT END, 0)) AS CGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127567,127574,127568,127573) THEN MAIN_ACCOUNT_CODE END, 0))AS SGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (127567,127574,127568,127573) THEN AMOUNT END, 0)) AS SGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231758) THEN MAIN_ACCOUNT_CODE END, 0)) AS IGST_CODE , ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231758) THEN AMOUNT END, 0)) AS IGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231756) THEN MAIN_ACCOUNT_CODE END, 0)) AS CGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231756) THEN AMOUNT END, 0)) AS CGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231757) THEN MAIN_ACCOUNT_CODE END, 0))AS SGST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (231757) THEN AMOUNT END, 0)) AS SGST_VALUE, ";
        Q2 += "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (437277,127547) THEN MAIN_ACCOUNT_CODE END, 0))AS CST_CODE, ";
        Q2 += "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN (437277,127547) THEN AMOUNT END, 0)) AS CST_VALUE ";
        Q2 += "FROM  FINANCE.D_FIN_VOUCHER_DETAIL D,FINANCE.D_FIN_VOUCHER_HEADER H ";
        Q2 += "WHERE SUBSTRING(D.VOUCHER_NO,1,2)  ='DN' ";
        Q2 += "AND H.VOUCHER_NO = D.VOUCHER_NO ";
        Q2 += "AND H.CANCELLED =0 AND H.APPROVED IN (1) ";
        Q2 += "AND H.VOUCHER_DATE >='2017-07-01' AND BOOK_CODE IN (12,16,18,45) AND H.VOUCHER_NO NOT IN (SELECT DISTINCT DEBITNOTE_VOUCHER_NO FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR) ";
        Q2 += "GROUP BY  BOOK_CODE,H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE "; 

        System.out.println("Q2 : "+Q2);
        data.Execute(Q2);
        
        
        String Q3 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M, FINANCE.D_SAL_GSTR_INVOICE_ERP E SET M.HSN_SAC_CODE = E.HSN_CODE WHERE M.INVOICE_NO = E.INVOICE_NO AND M.INVOICE_DATE = E.INVOICE_DATE AND HSN_SAC_CODE ='' ";
        System.out.println("Q3 : "+Q3);
        data.Execute(Q3);
        
        
        String Q4 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 5515 WHERE  SUBSTRING(INVOICE_NO,1,2) ='00' AND HSN_SAC_CODE ='' ";
        System.out.println("Q4 : "+Q4);
        data.Execute(Q4);
        
        
        String Q5 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 59113290 WHERE  SUBSTRING(INVOICE_NO,1,2) ='F0' AND HSN_SAC_CODE ='' ";
        System.out.println("Q5 : "+Q5);
        data.Execute(Q5);
        
        
        
        String Q6 = "UPDATE FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET HSN_SAC_CODE = 59113290 WHERE  SUBSTRING(INVOICE_NO,1,2) ='B0' AND HSN_SAC_CODE ='' ";
        System.out.println("Q6 : "+Q6);
        data.Execute(Q6);
        
        
        
        String Q7 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP  T, FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M SET T.DEBITMEMO_NO =M.DEBITMEMO_NO,T.DEBITMEMO_DATE = M.DEBITMEMO_DATE ,T.RECEIPT_VOUCHER_NO = M.RECEIPT_VOUCHER_NO, T.INVOICE_DUE_DATE=M.INVOICE_DUE_DATE ,T.VALUE_DATE=M.VALUE_DATE,T.DAYS = M.DAYS,T.INTEREST_PER = M.INTEREST_PER, T.INTEREST_AMT = SUM(M.INTEREST_AMT),T.HSN_SAC_CODE = M.HSN_SAC_CODE WHERE  T.INVOICE_NO = M.INVOICE_NO AND T.INVOICE_DATE = M.INVOICE_DATE AND T.MAIN_ACCOUNT_CODE = M.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (12,16,18) AND T.DB_PARTY_CODE = M.DB_PARTY_CODE GROUP BY M.INVOICE_NO,M.INVOICE_DATE,M.MAIN_ACCOUNT_CODE,M.DB_PARTY_CODE "; 
        System.out.println("Q7 : "+Q7);
        data.Execute(Q7);
        
        
//        #SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP;
        
//        #SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER;
        
        String Q8 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_SAL_PARTY_MASTER M,DINESHMILLS.D_SAL_STATE_MASTER S SET T.PARTY_GSTIN_ID = M.GSTIN_NO,T.PARTY_PLACE_OF_SUPPLY= S.GST_PLACE_OF_SUPPLY,DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =PARTY_NAME, PARTY_ADDRESS1 = ADDRESS1, PARTY_ADDRESS2 = ADDRESS2, PARTY_DISPATCH_STATION = DISPATCH_STATION ,COMPANY_NAME = 'SHRI DINESH MILLS LIMITED' ,COMPANY_ADDRESS ='P.O. BOX NO.-2501, PADRA ROAD,VADODARA-390005, GUJARAT' ,COMPANY_GSTIN ='24AADCDCS3115Q1Z8' ,COMPANY_FAX_ADDRESS ='FAX: 2336195, PHONE: 2330060-65, URL: www.dineshmills.com' WHERE M.PARTY_CODE = T.DB_PARTY_CODE AND S.STATE_GST_CODE = M.STATE_GST_CODE AND BOOK_CODE IN (12,16,18) ";
        System.out.println("Q8 : "+Q8);
        data.Execute(Q8);
        
        
//#SELECT * FROM DINESHMILLS.D_COM_SUPP_MASTER;
        
        String Q9 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M SET DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (12,16,18) "; 
        System.out.println("Q9 : "+Q9);
        data.Execute(Q9);        
        
        
        String Q10 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M SET DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY ,COMPANY_NAME = 'SHRI DINESH MILLS LIMITED' ,COMPANY_ADDRESS ='P.O. BOX NO.-2501, PADRA ROAD,VADODARA-390005, GUJARAT' ,COMPANY_GSTIN ='24AADCDCS3115Q1Z8' ,COMPANY_FAX_ADDRESS ='FAX: 2336195, PHONE: 2330060-65, URL: www.dineshmills.com' WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND BOOK_CODE IN (45) ";
        System.out.println("Q10 : "+Q10);
        data.Execute(Q10);
        
        
        
        String Q11 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T,DINESHMILLS.D_COM_SUPP_MASTER M,DINESHMILLS.D_SAL_STATE_MASTER S SET T.PARTY_GSTIN_ID = M.GSTIN_NO,T.PARTY_PLACE_OF_SUPPLY= S.GST_PLACE_OF_SUPPLY,DOCUMENT_TYPE ='D', PARTY_SUPP_NAME =SUPP_NAME, PARTY_ADDRESS1 = ADD1, PARTY_ADDRESS2 = ADD2, PARTY_ADDRESS3 = ADD3, PARTY_DISPATCH_STATION = CITY WHERE M.SUPPLIER_CODE = T.DB_PARTY_CODE AND M.MAIN_ACCOUNT_CODE = T.MAIN_ACCOUNT_CODE AND S.STATE_GST_CODE = M.STATE_GST_CODE AND BOOK_CODE IN (45) ";
        System.out.println("Q11 : "+Q11);
        data.Execute(Q11);
        
        
        String Q12 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.MAIN_ACCOUNT_CODE_NAME = G.ACCOUNT_NAME WHERE T.MAIN_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q12 : "+Q12);
        data.Execute(Q12);
        
        
        String Q13 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.INTEREST_CODE_NAME = G.ACCOUNT_NAME WHERE T.INTEREST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q13 : "+Q13);
        data.Execute(Q13);
        
        
        String Q14 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.IGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.IGST_PER = G.GST_PERCENT WHERE T.IGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE "; 
        System.out.println("Q14 : "+Q14);
        data.Execute(Q14);
        
        
        String Q15 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.CGST_PER = G.GST_PERCENT WHERE  T.CGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q15 : "+Q15);
        data.Execute(Q15);
        
        
        String Q16 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.SGST_ACCOUNT_NAME = G.ACCOUNT_NAME,T.SGST_PER = G.GST_PERCENT WHERE T.SGST_ACCOUNT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q16 : "+Q16);
        data.Execute(Q16);
        
        
        String Q17 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CST_CODE_NAME = G.ACCOUNT_NAME WHERE T.CST_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q17 : "+Q17);
        data.Execute(Q17);
        
        
        String Q18 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.IGST_CREDIT_NAME = G.ACCOUNT_NAME,T.IGST_PER = G.GST_PERCENT WHERE T.IGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q18 : "+Q18);
        data.Execute(Q18);
        
        
        String Q19 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.CGST_CREDIT_NAME = G.ACCOUNT_NAME,T.CGST_PER = G.GST_PERCENT WHERE T.CGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q19 : "+Q19);
        data.Execute(Q19);
        
        
        String Q20 = "UPDATE FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP T, FINANCE.D_FIN_GL G SET T.SGST_CREDIT_NAME = G.ACCOUNT_NAME,T.SGST_PER = G.GST_PERCENT WHERE T.SGST_CREDIT_CODE = G.MAIN_ACCOUNT_CODE ";
        System.out.println("Q20 : "+Q20);
        data.Execute(Q20);
        
        
//        SELECT * FROM FINANCE.D_FIN_GL G;

//#SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP WHERE HSN_SAC_CODE = '' AND BOOK_CODE =12 SUBSTRING(INVOICE_NO,1,2) ='SU'

//#SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M WHERE  SUBSTRING(INVOICE_NO,1,2) ='00' AND HSN_SAC_CODE =''
        
//        #SELECT * FROM FINANCE.D_FIN_DEBITMEMO_RECEIPT_MAPPING M WHERE  SUBSTRING(INVOICE_NO,1,2) ='00'
        
        String Q21 = "INSERT INTO FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP ";
        System.out.println("Q21 : "+Q21);
        data.Execute(Q21);

 
//SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR_TEMP; 
        
//        SELECT * FROM FINANCE.D_FIN_DEBITNOTE_VOUCHER_GSTR;

//#WHERE BOOK_CODE =45;
        
    }
}
