/*
 * 
 *
 * 
 */

package EITLERP.Stores;

import EITLERP.GSTR.*;
import EITLERP.*;
import EITLERP.Finance.FinanceGlobal;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.table.JTableHeader;
//import EITLERP.Production.ReportUI.*;
import java.io.File;



/*<APPLET CODE=frmChangePassword HEIGHT=200 WIDTH=430></APPLET>*/
/**
 *
 * @author  
 */
public class frmMIRDataForVenderRating extends javax.swing.JApplet {
    
    public boolean forceToChange=false;
    public boolean cancelled=false;
    private EITLTableModel[] DataModel;
    private EITLTableModel DataModelB2B = new EITLTableModel();  
    private EITLTableModel DataModelDebitnoteDetail= new EITLTableModel();
    
    String strProductCode = "";
    private JDialog aDialog;
    DateFormat df =  new SimpleDateFormat("dd-MMM-yy");
    HashMap hmPieceList=new HashMap();
    String ORDER_BY="";
    private clsExcelExporter exp = new clsExcelExporter();
    /** Initializes the applet frmChangePassword */
    public void init() {
        initComponents();
        //ExporttoExcelFileChooser.setVisible(false);
        FormatGrid();//780, 560
        setSize(780,560);
        jLabel1.setForeground(Color.WHITE);
        btnSortB2B.setVisible(false);
        
    }   
    
    /*public frmChangePassword() {
        setSize(430,250);
        initComponents();
        lblUser.setText(clsUser.getUserName(SDMLERPGLOBAL.gCompanyID,SDMLERPGLOBAL.gUserID));
    }*/
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ExporttoExcelFileChooser = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        Table_b2b = new javax.swing.JTable();
        btnShowB2B = new javax.swing.JButton();
        Export_B2B = new javax.swing.JButton();
        btnSortB2B = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSuppID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblSuppName = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("MIR Data For Vender Rating");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 2, 780, 25);

        jLabel2.setBackground(new java.awt.Color(0, 102, 153));
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 100, 780, 10);

        txtFromDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(150, 30, 130, 30);

        txtToDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        getContentPane().add(txtToDate);
        txtToDate.setBounds(390, 30, 120, 30);

        jPanel10.setLayout(null);

        Table_b2b.setModel(new javax.swing.table.DefaultTableModel(
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
        Table_b2b.setSelectionBackground(new java.awt.Color(208, 220, 234));
        Table_b2b.setSelectionForeground(new java.awt.Color(231, 16, 16));
        Table_b2b.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Table_b2bKeyPressed(evt);
            }
        });
        jScrollPane10.setViewportView(Table_b2b);

        jPanel10.add(jScrollPane10);
        jScrollPane10.setBounds(10, 50, 740, 240);

        btnShowB2B.setText("Show List");
        btnShowB2B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowB2BActionPerformed(evt);
            }
        });
        jPanel10.add(btnShowB2B);
        btnShowB2B.setBounds(10, 10, 130, 30);

        Export_B2B.setText("EXPORT TO EXCEL");
        Export_B2B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Export_B2BActionPerformed(evt);
            }
        });
        jPanel10.add(Export_B2B);
        Export_B2B.setBounds(10, 320, 170, 30);

        btnSortB2B.setText("SORT DATA");
        btnSortB2B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortB2BActionPerformed(evt);
            }
        });
        jPanel10.add(btnSortB2B);
        btnSortB2B.setBounds(600, 10, 120, 30);

        jTabbedPane2.addTab("Detail", jPanel10);

        getContentPane().add(jTabbedPane2);
        jTabbedPane2.setBounds(10, 120, 760, 400);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("MIR From Date :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 30, 130, 20);

        jLabel5.setText("To Date :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(310, 30, 70, 20);

        txtSuppID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppIDFocusLost(evt);
            }
        });
        txtSuppID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppIDKeyPressed(evt);
            }
        });
        getContentPane().add(txtSuppID);
        txtSuppID.setBounds(150, 70, 110, 20);

        jLabel3.setText("Supplier Code");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(40, 70, 100, 20);
        getContentPane().add(lblSuppName);
        lblSuppName.setBounds(280, 70, 310, 20);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear);
        btnClear.setBounds(530, 30, 80, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void Export_B2BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Export_B2BActionPerformed
        try{
        File file=null;        
        ExporttoExcelFileChooser.setDialogTitle("Enter Excel File Name");
        ExporttoExcelFileChooser.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
        int returnVal = ExporttoExcelFileChooser.showSaveDialog(frmMIRDataForVenderRating.this);
        if ( returnVal == JFileChooser.APPROVE_OPTION) {
            file = ExporttoExcelFileChooser.getSelectedFile();
            exp.fillData(Table_b2b,new File(file+".xls"));
            JOptionPane.showMessageDialog(null," Excel File Saved at : "+ file+".xls","Message",JOptionPane.INFORMATION_MESSAGE);
        }           
        
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }     
        /*
        File file = null;
        file1.setVisible(true);
        try {
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }

            exp.fillData(Table_b2b, file, "B2B_GSTR1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        */
    }//GEN-LAST:event_Export_B2BActionPerformed

    private void btnShowB2BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowB2BActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        FormatGrid();
        GenerateB2BData(ORDER_BY);
    }//GEN-LAST:event_btnShowB2BActionPerformed

    private void Table_b2bKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Table_b2bKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Table_b2bKeyPressed

    private void btnSortB2BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortB2BActionPerformed
        // TODO add your handling code here:
        sort_B2B_query_creator();
        btnShowB2BActionPerformed(null);
    }//GEN-LAST:event_btnSortB2BActionPerformed

    private void txtSuppIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppIDKeyPressed
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            LOV aList = new LOV();

            aList.SQL = "SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND BLOCKED='N' AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;

            if (aList.ShowLOV()) {
                txtSuppID.setText(aList.ReturnVal);
                lblSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, aList.ReturnVal));
            }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuppIDKeyPressed
    }
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
     txtFromDate.setText("");
     txtToDate.setText("");
     txtSuppID.setText("");
     lblSuppName.setText(""); 
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtSuppIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppIDFocusLost
        if (!txtSuppID.getText().trim().equals("")) {
            lblSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppID.getText()));
        }else{
            lblSuppName.setText(""); 
        }
        

    }//GEN-LAST:event_txtSuppIDFocusLost
          
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Export_B2B;
    private javax.swing.JFileChooser ExporttoExcelFileChooser;
    private javax.swing.JTable Table_b2b;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnShowB2B;
    private javax.swing.JButton btnSortB2B;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lblSuppName;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtSuppID;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private JTableHeader header;
    private Object selectedColumn = null;
    
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
            DataModelB2B = new EITLTableModel();
            Table_b2b.removeAll();

            Table_b2b.setModel(DataModelB2B);
            Table_b2b.setAutoResizeMode(0);

            DataModelB2B.addColumn("SR No"); //0 - Read Only
            DataModelB2B.addColumn("MIR No"); //1
            DataModelB2B.addColumn("MIR Date"); //2
            DataModelB2B.addColumn("MIR SUPP ID"); //3
            DataModelB2B.addColumn("Supplier Name"); //4
            DataModelB2B.addColumn("City"); //5
            
            DataModelB2B.addColumn("Dept Desc"); //6
            DataModelB2B.addColumn("Buyer Name"); //7
            DataModelB2B.addColumn("PO"); //8
            DataModelB2B.addColumn("PO Date"); //9
            DataModelB2B.addColumn("Purpose"); //10
            DataModelB2B.addColumn("Delivery Date"); //11
            DataModelB2B.addColumn("MIR PO Diff"); //12
            
            DataModelB2B.addColumn("RJN No"); //13
            DataModelB2B.addColumn("RJN Date"); //14
            DataModelB2B.addColumn("RH Remark"); //15
            DataModelB2B.addColumn("RD Remark"); //16
                     
            
            /*            
            DataModelB2B.SetVariable(0, "SR_N0"); //0 - Read Only
            DataModelB2B.SetVariable(1, "GSTIN_NO"); //1
            DataModelB2B.SetVariable(2, "INVOICE_NO");
            DataModelB2B.SetVariable(3, "INVOICE_DATE");
            DataModelB2B.SetVariable(4, "INVOICE_VALUE"); //2
            DataModelB2B.SetVariable(5, "PLACE_OF_SUPPLY");
            DataModelB2B.SetVariable(6, "REV_CHRG"); //2
            DataModelB2B.SetVariable(7, "INVOICE_TYPE"); //3
            DataModelB2B.SetVariable(8, "E_COMM_GSTIN_NO");
            DataModelB2B.SetVariable(9, "RATE"); //4
            DataModelB2B.SetVariable(10, "TAXABLE_VALUE"); //5
            DataModelB2B.SetVariable(11, "CESS_AMOUNT");
            
            DataModelB2B.SetVariable(12, "PARTY_CODE");
            DataModelB2B.SetVariable(13, "PARTY_NAME");
            DataModelB2B.SetVariable(14, "SGST_PER"); //2
            DataModelB2B.SetVariable(15, "SGST_AMT");
            DataModelB2B.SetVariable(16, "CGST_PER"); //2
            DataModelB2B.SetVariable(17, "CGST_AMT"); //3
            DataModelB2B.SetVariable(18, "IGST_PER");
            DataModelB2B.SetVariable(19, "IGST_AMT"); //4
            DataModelB2B.SetVariable(20, "HSN_CODE");
            
            DataModelB2B.SetNumeric(4, true);
            DataModelB2B.SetNumeric(10, true);
            DataModelB2B.SetNumeric(11, true);
                        
            DataModelB2B.SetReadOnly(0);
            DataModelB2B.SetReadOnly(1);
            DataModelB2B.SetReadOnly(2);
            DataModelB2B.SetReadOnly(3);
            DataModelB2B.SetReadOnly(4);
            DataModelB2B.SetReadOnly(5);
            DataModelB2B.SetReadOnly(6);
            DataModelB2B.SetReadOnly(7);
            DataModelB2B.SetReadOnly(8);
            DataModelB2B.SetReadOnly(9);
            DataModelB2B.SetReadOnly(10);
            DataModelB2B.SetReadOnly(11);
            DataModelB2B.SetReadOnly(12);
            DataModelB2B.SetReadOnly(13);
            DataModelB2B.SetReadOnly(14);
            DataModelB2B.SetReadOnly(15);
            DataModelB2B.SetReadOnly(16);
            DataModelB2B.SetReadOnly(17);
            DataModelB2B.SetReadOnly(18);
            DataModelB2B.SetReadOnly(19);
            DataModelB2B.SetReadOnly(20);
            */            
            Table_b2b.getColumnModel().getColumn(0).setMinWidth(30);
            Table_b2b.getColumnModel().getColumn(0).setMaxWidth(40);
            Table_b2b.getColumnModel().getColumn(1).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(1).setMaxWidth(70);
            Table_b2b.getColumnModel().getColumn(2).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(2).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(3).setMinWidth(100);
            Table_b2b.getColumnModel().getColumn(4).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(4).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(5).setMinWidth(120);
            //Table_b2b.getColumnModel().getColumn(5).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(6).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(6).setMaxWidth(100);
            Table_b2b.getColumnModel().getColumn(7).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(7).setMaxWidth(100);
            Table_b2b.getColumnModel().getColumn(8).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(8).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(9).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(9).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(10).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(10).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(11).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(11).setMaxWidth(80);
            Table_b2b.getColumnModel().getColumn(12).setMinWidth(80);
            //Table_b2b.getColumnModel().getColumn(13).setMinWidth(120);
            //Table_b2b.getColumnModel().getColumn(14).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(15).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(16).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(17).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(18).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(19).setMinWidth(80);
//            Table_b2b.getColumnModel().getColumn(20).setMinWidth(80);
//            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Enter Correct Details in Table. Error is : "+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
     
    public void GenerateB2BData(String ORDER_BY) {
        String strCondition="";
        if(!txtSuppID.getText().equals("")){
                        strCondition= " AND H.SUPP_ID='"+txtSuppID.getText()+"' ";
                    }
        try {            
            String strSQL = "SELECT MIR_NO,MIR_DATE,MIRSUPPID,SUPP_NAME,CITY,DEPT_DESC,USER_NAME AS BUYER_NAME, MIRPO AS PO, PO_DATE,PURPOSE,DELIVERY_DATE, "
                    + "DATEDIFF(MIR_DATE,DELIVERY_DATE) AS MIR_PO_DIFF,COALESCE(RJN_NO,'') AS RJN_NO,COALESCE(RJN_DATE,'') AS RJN_DATE,COALESCE(RHREMARK,'') AS RHREMARK,COALESCE(RDREMARK,'') AS RDREMARK "
                    + "FROM "                    
                    + "(SELECT DISTINCT H.MIR_NO,H.MIR_DATE,H.SUPP_ID AS MIRSUPPID,SUPP_NAME,CITY,D.DEPT_ID,DEPT_DESC,PO_NO AS MIRPO  "
                    + "FROM DINESHMILLS.D_INV_MIR_DETAIL D ,DINESHMILLS.D_INV_MIR_HEADER H,D_COM_DEPT_MASTER DP, DINESHMILLS.D_COM_SUPP_MASTER S "
                    + "WHERE D.MIR_NO = H.MIR_NO AND H.MIR_DATE >='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND H.MIR_DATE <= '"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' AND H.CANCELLED =0 "
                    + "AND H.APPROVED =1 AND D.DEPT_ID = DP.DEPT_ID  AND S.SUPPLIER_CODE = H.SUPP_ID "+strCondition+" "                    
                    + " AND SUBSTRING(H.MIR_NO,1,1) !='S' )  AS MIR "
                    + "LEFT JOIN "
                    + "(SELECT DISTINCT H.PO_NO,H.PO_DATE,SUPP_ID,PURPOSE,DELIVERY_DATE,H.REMARKS AS HREMARKS ,BUYER,USER_NAME "
                    + "FROM DINESHMILLS.D_PUR_PO_HEADER H, DINESHMILLS.D_PUR_PO_DETAIL D,DINESHMILLS.D_COM_USER_MASTER U "
                    + "WHERE H.PO_NO = D.PO_NO AND H.CANCELLED =0 AND H.APPROVED =1 AND U.USER_ID = BUYER) AS PO "
                    + "ON PO_NO = MIRPO "
                    + "LEFT JOIN "
                    + "(SELECT DISTINCT H.RJN_NO,H.RJN_DATE,PO_NO AS RJNPO,H.REMARKS AS RHREMARK ,D.REMARKS AS RDREMARK "
                    + "FROM DINESHMILLS.D_INV_RJN_DETAIL D,DINESHMILLS.D_INV_RJN_HEADER H "
                    + "WHERE H.RJN_NO = D.RJN_NO AND H.CANCELLED =0 AND H.APPROVED =1 AND PO_NO !='') AS RJN "
                    + "ON MIRPO = RJNPO ";
            System.out.println(" SQL : "+strSQL);
            ResultSet rs = data.getResult(strSQL);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {

                Object[] rowData = new Object[25];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;
                rowData[1] = rs.getString("MIR_NO");                
                rowData[2] = EITLERPGLOBAL.formatDate(rs.getDate("MIR_DATE"));                
                rowData[3] = rs.getString("MIRSUPPID");
                rowData[4] = rs.getString("SUPP_NAME");
                rowData[5] = rs.getString("CITY");
                rowData[6] = rs.getString("DEPT_DESC");                
                rowData[7] = rs.getString("BUYER_NAME");                
                rowData[8] = rs.getString("PO");
                rowData[9] = EITLERPGLOBAL.formatDate(rs.getString("PO_DATE"));
                rowData[10] = rs.getString("PURPOSE");                               
                rowData[11] = EITLERPGLOBAL.formatDate(rs.getString("DELIVERY_DATE"));                
                rowData[12] = rs.getString("MIR_PO_DIFF");
                rowData[13] = rs.getString("RJN_NO");                
                rowData[14] = EITLERPGLOBAL.formatDate(rs.getString("RJN_DATE"));
                rowData[15] = rs.getString("RHREMARK");
                rowData[16] = rs.getString("RDREMARK");
                
                

                DataModelB2B.addRow(rowData);
                rs.next();
            }
            rs.close();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }
    
    public void GenerateDebitnoteDetail(String ORDER_BY) {
        
        try {
            String strSQL = "SELECT *,ROUND((GST_AMT_C*100)/PUR_AMT_C,0) PER FROM "
                    + "(SELECT EXTRACT(YEAR FROM VOUCHER_DATE) AS YYYY,EXTRACT(MONTH FROM VOUCHER_DATE) AS MM, "
                    + "H.VOUCHER_NO, "
                    + "MAX(COALESCE(CASE WHEN  EFFECT ='D' THEN MAIN_ACCOUNT_CODE END,0)) AS MAC, "
                    + "MAX(COALESCE(CASE WHEN SUB_ACCOUNT_CODE !='' THEN SUB_ACCOUNT_CODE END,0)) AS SAB, "
                    + "SUM(COALESCE(CASE WHEN  EFFECT ='D'THEN AMOUNT END,0)) AS AMT, "
                    //+ "#SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (231757,231756,231758,231759,231761) AND EFFECT ='D'THEN AMOUNT END,0)) AS GST_AMT, "
                    + "MAX(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN (231757,231756,231758,231759,231761) AND EFFECT ='C'THEN MAIN_ACCOUNT_CODE END,0)) AS MAC_PURCHASE, "
                    + "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE NOT IN "
                    + "(231767,231764,231763,231761,231760,231759,231758,231757,2317561, "
                    + "231756,231719,214836,127588,127587,127584,127583,127582,127581,127580,127579,127577,127576,127575,127574,127573,127572,127571,127570,127569,127568,127567,127566,127565)"
                    + "AND EFFECT ='C'THEN AMOUNT END,0)) AS PUR_AMT_C, "
                    + "MAX(COALESCE(CASE WHEN  INVOICE_NO !='' THEN INVOICE_NO END,0)) AS INVOICE_NO, "
                    + "MAX(COALESCE(CASE WHEN  INVOICE_NO !='' THEN INVOICE_DATE END,0)) AS INVOICE_DATE, "
                    + "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN "
                    + "(231767,231764,231763,231761,231760,231759,231758,231757,2317561, "
                    + "231756,231719,214836,127588,127587,127584,127583,127582,127581,127580,127579,127577,127576,127575,127574,127573,127572,127571,127570,127569,127568,127567,127566,127565) "
                    + "AND EFFECT ='C'THEN AMOUNT END,0)) AS GST_AMT_C, "
                    + "SUM(COALESCE(CASE WHEN MAIN_ACCOUNT_CODE IN "
                    + "(231767,231764,231763,231761,231760,231759,231758,231757,2317561, "
                    + "231756,231719,214836,127588,127587,127584,127583,127582,127581,127580,127579,127577,127576,127575,127574,127573,127572,127571,127570,127569,127568,127567,127566,127565) "
                    + "AND EFFECT ='D'THEN AMOUNT END,0)) AS GST_AMT_D "
                    + "FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D "
                    + "WHERE H.VOUCHER_NO = D.VOUCHER_NO "
                    + "AND H.VOUCHER_NO IN ( SELECT DISTINCT H.VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER H, FINANCE.D_FIN_VOUCHER_DETAIL D "
                    + "WHERE H.VOUCHER_NO = D.VOUCHER_NO "
                    //+ "#AND EXTRACT(MONTH FROM VOUCHER_DATE) = 2 "
                    //+ "AND EXTRACT(YEAR FROM VOUCHER_DATE) >= 2018 "
                    + " AND VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' "                    
                    + "AND H.APPROVED =1 AND H.CANCELLED =0 "
                    + "AND SUBSTRING(H.VOUCHER_NO,1,2) IN ('DN')  AND BOOK_CODE = 45 "
                    + "AND MAIN_ACCOUNT_CODE IN (231767,231764,231763,231761,231760,231759,231758,231757,2317561, "
                    + "231756,231719,214836,127588,127587,127584,127583,127582,127581,127580,127579,127577,127576,127575,127574,127573,127572,127571,127570,127569,127568,127567,127566,127565) "
                    + ") "
                    + "GROUP BY EXTRACT(YEAR FROM VOUCHER_DATE),EXTRACT(MONTH FROM VOUCHER_DATE),H.VOUCHER_NO) AS M";
            System.out.println(" SQL : "+strSQL);
            ResultSet rs = data.getResult(strSQL);
            int cnt = 1;
            String pName = "";
            while (!rs.isAfterLast()) {

                Object[] rowData = new Object[25];
                // rowData[0]=rs.getString("");
                rowData[0] = cnt++;
                rowData[1] = rs.getString("YYYY");
                rowData[2] = rs.getString("MM");
                //rowData[3] = df.format(rs.getDate("INVOICE_DATE"));
                rowData[3] = rs.getString("VOUCHER_NO");                
                rowData[4] = rs.getString("MAC");
                rowData[5] = rs.getString("SAB");
                rowData[6] = rs.getString("AMT");                
                rowData[7] = rs.getString("MAC_PURCHASE");
                rowData[8] = rs.getString("PUR_AMT_C");
                rowData[9] = rs.getString("INVOICE_NO");
                rowData[10] = EITLERPGLOBAL.formatDate(rs.getString("INVOICE_DATE"));
                rowData[11] = rs.getString("GST_AMT_C");
                rowData[12] = rs.getString("GST_AMT_D");                
                rowData[13] = rs.getString("PER");
                
                

                DataModelDebitnoteDetail.addRow(rowData);
                rs.next();
            }
            rs.close();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }
    
    public void sort_B2B_query_creator() {   
        
        SelectSortFields sort=new SelectSortFields();
        
        sort.setField("INVOICE_NO", "Invoice Number");
        sort.setField("INVOICE_DATE", "Invoice Date");
        sort.setField("INVOICE_VALUE", "Invoice Value");
        sort.setField("PLACE_OF_SUPPLY", "Place Of Supply");
        sort.setField("INVOICE_TYPE", "Invoice Type");
        sort.setField("RATE", "Rate");
        sort.setField("PARTY_CODE", "Party Code");
        
        ORDER_BY = sort.getQuery(SelectSortFields.DEFAULT_ORDER.ASCENDING);
    }
}
