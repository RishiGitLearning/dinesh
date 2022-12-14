/*
 * 
 * Created on December 20, 2013, 12:14 PM
 */

package EITLERP.Sales.PostSJ;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import java.sql.*;
import EITLERP.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  Ashutosh Pathak
 */
public class frmRptCategorywiseReports extends javax.swing.JApplet {
    
    private EITLComboModel cmbInvoiceTypeModel;
    
    /** Initializes the applet frmABDReport */
    public void init() {
        initComponents();        
        setSize(420, 200);
        GenerateCombo();
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        RptFromDate = new javax.swing.JTextField();
        RptToDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbInvoiceType = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("POST SJ REPORTS -CATEGORY WISE SALES");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 2, 500, 25);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14));
        lblStatus.setForeground(new java.awt.Color(0, 51, 255));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 390, 450, 20);

        getContentPane().add(jLabel1);
        jLabel1.setBounds(270, 130, 0, 0);

        jButton2.setText("Agent Code Wise Sales Performance");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(30, 120, 240, 25);

        getContentPane().add(RptFromDate);
        RptFromDate.setBounds(120, 40, 80, 19);

        getContentPane().add(RptToDate);
        RptToDate.setBounds(240, 40, 80, 19);

        jLabel3.setText("From Date");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 40, 70, 20);

        jLabel4.setText("To");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(210, 40, 20, 20);

        getContentPane().add(cmbInvoiceType);
        cmbInvoiceType.setBounds(30, 80, 120, 24);

    }//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String FromDate=EITLERPGLOBAL.formatDateDB(RptFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(RptToDate.getText());    
        //String InvoiceType=cmbInvoiceType.getSelectedItem().toString()+" Sales";
        String InvoiceType=cmbInvoiceType.getSelectedItem().toString();
        String Type=Integer.toString(cmbInvoiceType.getSelectedIndex());
        System.out.println(InvoiceType);
        System.out.println(Type);
        if(cmbInvoiceType.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null,"Please Select any type");
            cmbInvoiceType.requestFocus();
        }else if(cmbInvoiceType.getSelectedIndex()==2){
            JOptionPane.showMessageDialog(null,"Please Select Suitings type");
            cmbInvoiceType.requestFocus();
        }else if(cmbInvoiceType.getSelectedIndex()==3){
            JOptionPane.showMessageDialog(null,"Please Select Suitings type");
            cmbInvoiceType.requestFocus();
        }        
        else{
            GenerateCategorywiseReport(FromDate,ToDate,Type,InvoiceType);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed
                        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField RptFromDate;
    private javax.swing.JTextField RptToDate;
    private javax.swing.JComboBox cmbInvoiceType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() { 
        cmbInvoiceTypeModel=new EITLComboModel();
        cmbInvoiceType.removeAllItems();
        cmbInvoiceType.setModel(cmbInvoiceTypeModel);
        
        ComboData aData=new ComboData();        
        aData.Text="-Select Type-";
        aData.Code=0;
        cmbInvoiceTypeModel.addElement(aData);
        
        aData.Text="Suitings";
        aData.Code=1;
        cmbInvoiceTypeModel.addElement(aData);
        
        aData.Text="Felt";
        aData.Code=2;
        cmbInvoiceTypeModel.addElement(aData);
        
        aData.Text="Filter Fab.";
        aData.Code=3;
        cmbInvoiceTypeModel.addElement(aData);
        
    }
    
    private void GenerateCategorywiseReport(String FromDate,String ToDate,String Type,String InvoiceType){
        try{
            String Condition="";
            int l=0;
            TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
            objData.AddColumn("SR_NO");
            objData.AddColumn("INVOICE_NO");
            objData.AddColumn("TOTAL_GROSS_QTY");
            objData.AddColumn("TOTAL_NET_AMOUNT");
            objData.AddColumn("COLUMN_1_AMT");
            objData.AddColumn("TOTAL_SALES");
            //objData.AddColumn("EXCISE_HLC_AMT");
            //objData.AddColumn("INV_AMT");
            
            
            String str="SELECT X.INVOICE_TYPE, INVOICE_NO ,TOTAL_GROSS_QTY,TOTAL_NET_QTY,TOTAL_GROSS_AMOUNT,COLUMN_9_AMT,COLUMN_16_AMT,TOTAL_NET_AMOUNT,COLUMN_1_AMT,TOTAL_VALUE, NET_AMOUNT,COLUMN_25_AMT,COLUMN_24_AMT,COLUMN_8_AMT,VAT4,VAT1,CST5,CST2,COLUMN_12_AMT,TOTAL_SQ_MTR,TRD_DISCOUNT,COLUMN_13_AMT,TOTAL_NET_AMOUNT+COLUMN_1_AMT AS TOTAL_SALES FROM ";
                str+="(SELECT INVOICE_TYPE,'COMBO' AS INVOICE_NO ,SUM(A.TOTAL_GROSS_QTY) AS TOTAL_GROSS_QTY,SUM(A.TOTAL_NET_QTY) AS TOTAL_NET_QTY,SUM(A.TOTAL_GROSS_AMOUNT) AS TOTAL_GROSS_AMOUNT,SUM(A.COLUMN_9_AMT) AS COLUMN_9_AMT,SUM(A.COLUMN_16_AMT) AS COLUMN_16_AMT,SUM(A.TOTAL_NET_AMOUNT) AS TOTAL_NET_AMOUNT,SUM(A.COLUMN_1_AMT) AS COLUMN_1_AMT,SUM(A.TOTAL_VALUE) AS TOTAL_VALUE,SUM(A.NET_AMOUNT) AS NET_AMOUNT,SUM(A.COLUMN_25_AMT) AS COLUMN_25_AMT,SUM(A.COLUMN_24_AMT) AS COLUMN_24_AMT,SUM(A.COLUMN_8_AMT) AS COLUMN_8_AMT,SUM(A.VAT4) AS VAT4,SUM(A.VAT1) AS VAT1,SUM(A.CST5) AS CST5,SUM(A.CST2) AS CST2,SUM(A.COLUMN_12_AMT) AS COLUMN_12_AMT,SUM(A.TOTAL_SQ_MTR) AS TOTAL_SQ_MTR,SUM(A.COLUMN_13_AMT) AS COLUMN_13_AMT  FROM D_SAL_INVOICE_HEADER A  WHERE A.COMPANY_ID=2 AND A.INVOICE_TYPE='"+Type+"' AND A.INVOICE_DATE>='"+FromDate+"' AND A.INVOICE_DATE<='"+ToDate+"' AND A.WAREHOUSE_CODE=0 AND A.QUALITY_INDICATOR=0 AND A.CANCELLED=0 AND A.APPROVED=1 GROUP BY INVOICE_TYPE) AS X ";
                str+="LEFT JOIN (SELECT INVOICE_TYPE,SUM(TRD_DISCOUNT) AS TRD_DISCOUNT FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND WAREHOUSE_CODE=0 AND CANCELLED=0 GROUP BY INVOICE_TYPE) AS Y ON X.INVOICE_TYPE=Y.INVOICE_TYPE ";
                str+="UNION ALL ";
                str+="SELECT X.INVOICE_TYPE, INVOICE_NO ,TOTAL_GROSS_QTY,TOTAL_NET_QTY,TOTAL_GROSS_AMOUNT,COLUMN_9_AMT,COLUMN_16_AMT,TOTAL_NET_AMOUNT,COLUMN_1_AMT,TOTAL_VALUE, NET_AMOUNT,COLUMN_25_AMT,COLUMN_24_AMT,COLUMN_8_AMT,VAT4,VAT1,CST5,CST2,COLUMN_12_AMT,TOTAL_SQ_MTR,TRD_DISCOUNT,COLUMN_13_AMT,TOTAL_NET_AMOUNT+COLUMN_1_AMT AS TOTAL_SALES FROM ";
                str+="(SELECT INVOICE_TYPE,'REGULAR' AS INVOICE_NO ,SUM(TOTAL_GROSS_QTY) AS TOTAL_GROSS_QTY,SUM(TOTAL_NET_QTY) AS TOTAL_NET_QTY,SUM(TOTAL_GROSS_AMOUNT) AS TOTAL_GROSS_AMOUNT,SUM(COLUMN_9_AMT) AS COLUMN_9_AMT,SUM(COLUMN_16_AMT) AS COLUMN_16_AMT,SUM(TOTAL_NET_AMOUNT) AS TOTAL_NET_AMOUNT,SUM(COLUMN_1_AMT) AS COLUMN_1_AMT,SUM(TOTAL_VALUE) AS TOTAL_VALUE,SUM(NET_AMOUNT) AS NET_AMOUNT,SUM(COLUMN_25_AMT) AS COLUMN_25_AMT,SUM(COLUMN_24_AMT) AS COLUMN_24_AMT,SUM(COLUMN_8_AMT) AS COLUMN_8_AMT,SUM(VAT4) AS VAT4,SUM(VAT1) AS VAT1,SUM(CST5) AS CST5,SUM(CST2) AS CST2,SUM(COLUMN_12_AMT) AS COLUMN_12_AMT,SUM(TOTAL_SQ_MTR) AS TOTAL_SQ_MTR,SUM(A.COLUMN_13_AMT) AS COLUMN_13_AMT FROM D_SAL_INVOICE_HEADER A WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND WAREHOUSE_CODE IN (1,9)  AND QUALITY_INDICATOR IN (0,2,3,4) AND CANCELLED=0 AND APPROVED=1 AND (TOTAL_NET_AMOUNT/TOTAL_GROSS_QTY)>80 GROUP BY INVOICE_TYPE) AS X ";
                str+="LEFT JOIN (SELECT INVOICE_TYPE,SUM(TRD_DISCOUNT) AS TRD_DISCOUNT FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND WAREHOUSE_CODE IN (1,9) AND CANCELLED=0 GROUP BY INVOICE_TYPE) AS Y ON X.INVOICE_TYPE=Y.INVOICE_TYPE ";
                str+="UNION ALL ";
                str+="SELECT X.INVOICE_TYPE, INVOICE_NO ,TOTAL_GROSS_QTY,TOTAL_NET_QTY,TOTAL_GROSS_AMOUNT,COLUMN_9_AMT,COLUMN_16_AMT,TOTAL_NET_AMOUNT,COLUMN_1_AMT,TOTAL_VALUE, NET_AMOUNT,COLUMN_25_AMT,COLUMN_24_AMT,COLUMN_8_AMT,VAT4,VAT1,CST5,CST2,COLUMN_12_AMT,TOTAL_SQ_MTR,TRD_DISCOUNT,COLUMN_13_AMT,TOTAL_NET_AMOUNT+COLUMN_1_AMT AS TOTAL_SALES FROM ";
                str+="(SELECT INVOICE_TYPE,'BLANKET' AS INVOICE_NO ,SUM(TOTAL_GROSS_QTY) AS TOTAL_GROSS_QTY,SUM(TOTAL_NET_QTY) AS TOTAL_NET_QTY,SUM(TOTAL_GROSS_AMOUNT) AS TOTAL_GROSS_AMOUNT,SUM(COLUMN_9_AMT) AS COLUMN_9_AMT,SUM(COLUMN_16_AMT) AS COLUMN_16_AMT,SUM(TOTAL_NET_AMOUNT) AS TOTAL_NET_AMOUNT,SUM(COLUMN_1_AMT) AS COLUMN_1_AMT,SUM(TOTAL_VALUE) AS TOTAL_VALUE,SUM(NET_AMOUNT) AS NET_AMOUNT,SUM(COLUMN_25_AMT) AS COLUMN_25_AMT,SUM(COLUMN_24_AMT) AS COLUMN_24_AMT,SUM(COLUMN_8_AMT) AS COLUMN_8_AMT,SUM(VAT4) AS VAT4,SUM(VAT1) AS VAT1,SUM(CST5) AS CST5,SUM(CST2) AS CST2,SUM(COLUMN_12_AMT) AS COLUMN_12_AMT,SUM(TOTAL_SQ_MTR) AS TOTAL_SQ_MTR,SUM(A.COLUMN_13_AMT) AS COLUMN_13_AMT FROM D_SAL_INVOICE_HEADER A WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND WAREHOUSE_CODE IN (4)  AND CANCELLED=0 AND APPROVED=1 AND (TOTAL_NET_AMOUNT/TOTAL_GROSS_QTY)>80 GROUP BY INVOICE_TYPE) AS X ";
                str+="LEFT JOIN (SELECT INVOICE_TYPE,SUM(TRD_DISCOUNT) AS TRD_DISCOUNT FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND WAREHOUSE_CODE IN (4) AND CANCELLED=0 GROUP BY INVOICE_TYPE) AS Y ON X.INVOICE_TYPE=Y.INVOICE_TYPE ";
                str+="UNION ALL ";
                str+="SELECT X.INVOICE_TYPE, INVOICE_NO ,TOTAL_GROSS_QTY,TOTAL_NET_QTY,TOTAL_GROSS_AMOUNT,COLUMN_9_AMT,COLUMN_16_AMT,TOTAL_NET_AMOUNT,COLUMN_1_AMT,TOTAL_VALUE, NET_AMOUNT,COLUMN_25_AMT,COLUMN_24_AMT,COLUMN_8_AMT,VAT4,VAT1,CST5,CST2,COLUMN_12_AMT,TOTAL_SQ_MTR, COALESCE(TRD_DISCOUNT,0),COLUMN_13_AMT,TOTAL_NET_AMOUNT+COLUMN_1_AMT AS TOTAL_SALES FROM ";
                str+="(SELECT INVOICE_TYPE,'FRC' AS INVOICE_NO ,SUM(TOTAL_GROSS_QTY) AS TOTAL_GROSS_QTY,SUM(TOTAL_NET_QTY) AS TOTAL_NET_QTY,SUM(TOTAL_GROSS_AMOUNT) AS TOTAL_GROSS_AMOUNT,SUM(COLUMN_9_AMT) AS COLUMN_9_AMT,SUM(COLUMN_16_AMT) AS COLUMN_16_AMT,SUM(TOTAL_NET_AMOUNT) AS TOTAL_NET_AMOUNT,SUM(COLUMN_1_AMT) AS COLUMN_1_AMT,SUM(TOTAL_VALUE) AS TOTAL_VALUE,SUM(NET_AMOUNT) AS NET_AMOUNT,SUM(COLUMN_25_AMT) AS COLUMN_25_AMT,SUM(COLUMN_24_AMT) AS COLUMN_24_AMT,SUM(COLUMN_8_AMT) AS COLUMN_8_AMT,SUM(VAT4) AS VAT4,SUM(VAT1) AS VAT1,SUM(CST5) AS CST5,SUM(CST2) AS CST2,SUM(COLUMN_12_AMT) AS COLUMN_12_AMT,SUM(TOTAL_SQ_MTR) AS TOTAL_SQ_MTR,SUM(A.COLUMN_13_AMT) AS COLUMN_13_AMT FROM D_SAL_INVOICE_HEADER A WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND (TOTAL_NET_AMOUNT/TOTAL_GROSS_QTY)<80 AND CANCELLED=0 AND APPROVED=1 GROUP BY INVOICE_TYPE) AS X ";
                str+="LEFT JOIN (SELECT INVOICE_TYPE,SUM(TRD_DISCOUNT) AS TRD_DISCOUNT FROM D_SAL_INVOICE_DETAIL WHERE COMPANY_ID=2 AND INVOICE_TYPE='"+Type+"' AND INVOICE_DATE>='"+FromDate+"' AND INVOICE_DATE<='"+ToDate+"' AND CANCELLED=0 AND (NET_AMOUNT/GROSS_QTY)< 80 GROUP BY INVOICE_TYPE) AS Y ON X.INVOICE_TYPE=Y.INVOICE_TYPE ";                         
            
            ResultSet  rsTemp =data.getResult(str);
            if(rsTemp.getRow()>0) {
            while(!rsTemp.isAfterLast()){
                l++;
                TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();                
                objRow.setValue("SR_NO",Integer.toString(l));
                objRow.setValue("INVOICE_NO",UtilFunctions.getString(rsTemp,"INVOICE_NO",""));                
                objRow.setValue("TOTAL_GROSS_QTY",UtilFunctions.getString(rsTemp,"TOTAL_GROSS_QTY",""));
                objRow.setValue("TOTAL_NET_AMOUNT",UtilFunctions.getString(rsTemp,"TOTAL_NET_AMOUNT",""));
                objRow.setValue("COLUMN_1_AMT",UtilFunctions.getString(rsTemp,"COLUMN_1_AMT",""));
                objRow.setValue("TOTAL_SALES",UtilFunctions.getString(rsTemp,"TOTAL_SALES",""));
                //objRow.setValue("EXCISE_HLC_AMT",UtilFunctions.getString(rsTemp,"EXCISE_HLC_AMT",""));
                objData.AddRow(objRow);                
                rsTemp.next();
            }
            }
            HashMap parameter=new HashMap();
            parameter.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            parameter.put("FROM_DATE",EITLERPGLOBAL.formatDate(FromDate));
            parameter.put("TO_DATE",EITLERPGLOBAL.formatDate(ToDate));
            parameter.put("INV_TYPE",InvoiceType);
            //parameter.put("BOOK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,BookCode));
            //parameter.put("TOTAL_INVOICE_NO",Integer.toString(l));
            
            //parameter.put("NAME","ORDER");
            //parameter.put("LAST_DATE","15/02/2014"
            
           // EITLERPGLOBAL.PAGE_BREAK=true;
            new TReportWriter.TReportEngine().PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/PostSJ/rptCategorywiseSalesPerformance.rpt",parameter,objData);
            rsTemp.close();            
           
        }catch(SQLException e) {
            e.printStackTrace();
            
        }
    }
    
    
        
    
    
}
