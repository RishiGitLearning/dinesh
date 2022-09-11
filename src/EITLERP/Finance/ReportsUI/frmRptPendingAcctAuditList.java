/*
 * frmRptPendingPJVList.java
 *
 * Created on April 16, 2008, 12:01 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import TReportWriter.*;
import java.util.*;
import java.lang.*;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import java.awt.event.*;
import java.math.*;
import java.text.DecimalFormat;
import java.lang.Double;
import java.io.File;
import EITLERP.Production.ReportUI.*;
import java.awt.Cursor;

public class frmRptPendingAcctAuditList extends javax.swing.JApplet {
    
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    private clsExcelExporter exp = new clsExcelExporter();  
       
    /** Initializes the applet frmRptPendingPJVList */
    public void init() {
        setSize(830,570);
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
        javax.swing.JButton cmdExporttoExcel;

        cmdPreview = new javax.swing.JButton();
        lblSubCode9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdSelectAll = new javax.swing.JButton();
        cmdClearAll = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        cmdShowVoucher = new javax.swing.JButton();
        cmdExporttoExcel = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        cmdPreview.setText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(700, 490, 100, 25);

        lblSubCode9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSubCode9.setText("Pending Accounts & Account Voucher List");
        lblSubCode9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        getContentPane().add(lblSubCode9);
        lblSubCode9.setBounds(20, 50, 320, 15);

        jPanel7.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jPanel7);
        jPanel7.setBounds(10, 30, 670, 14);

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
        jScrollPane1.setBounds(10, 70, 670, 410);

        cmdSelectAll.setMnemonic('A');
        cmdSelectAll.setText("Select All");
        cmdSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAllActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSelectAll);
        cmdSelectAll.setBounds(700, 110, 100, 25);

        cmdClearAll.setMnemonic('L');
        cmdClearAll.setText("Clear All");
        cmdClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearAllActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClearAll);
        cmdClearAll.setBounds(700, 150, 100, 25);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("Pending Account & Account Voucher List");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 0, 840, 25);

        cmdShowVoucher.setText("Show Voucher");
        cmdShowVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowVoucherActionPerformed(evt);
            }
        });

        getContentPane().add(cmdShowVoucher);
        cmdShowVoucher.setBounds(540, 490, 140, 25);

        cmdExporttoExcel.setText("Export to Excel");
        cmdExporttoExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExporttoExcelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdExporttoExcel);
        cmdExporttoExcel.setBounds(308, 490, 210, 25);

    }//GEN-END:initComponents

    private void cmdExporttoExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExporttoExcelActionPerformed
      try{
             
                    exp.fillData(Table,new File("/root/Desktop/Pendinglist.xls"));
                    //exp.fillData(TableI,new File("C://ProductionProcess.xls"));
                    exp.fillData(Table,new File("D://Pendinglist.xls"));
                    JOptionPane.showMessageDialog(null, "Data saved at " +
                            //"'C: \\ result.xls' successfully", "Message",
                            "'/root/Desktop/Pendinglist.xls' successfully in Linux PC or 'D://Pendinglist.xls' successfully in Windows PC    ", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
         
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }     
    
    }//GEN-LAST:event_cmdExporttoExcelActionPerformed

    private void cmdShowVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowVoucherActionPerformed
        // TODO add your handling code here:
        try {
            if(Table.getSelectedRow()>=0) {
                String DocNo=Table.getValueAt(Table.getSelectedRow(),2).toString();
                if(!DocNo.trim().equals("")) {
                    int CompanyID=EITLERPGLOBAL.gCompanyID;
                    AppletFrame aFrame=new AppletFrame("Voucher");
                    aFrame.startAppletEx("EITLERP.Finance.frmVoucher","Voucher");
                    frmVoucher ObjDoc=(frmVoucher) aFrame.ObjApplet;
                    ObjDoc.FindEx(CompanyID,DocNo);
                }
            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_cmdShowVoucherActionPerformed

    private void cmdClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount();i++) {
            DataModel.setValueAt(Boolean.valueOf(false), i, 0);
        }
    }//GEN-LAST:event_cmdClearAllActionPerformed

    private void cmdSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount();i++) {
            DataModel.setValueAt(Boolean.valueOf(true), i, 0);
        }
    }//GEN-LAST:event_cmdSelectAllActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdClearAll;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdSelectAll;
    private javax.swing.JButton cmdShowVoucher;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSubCode9;
    private javax.swing.JLabel lblTitle;
    // End of variables declaration//GEN-END:variables
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=1;i<=8;i++) {
            DataModel.SetReadOnly(i);
        }
        
        //Add Columns to it
        DataModel.addColumn(""); //0 Selection
        DataModel.addColumn("Sr.");//1
        DataModel.addColumn("Voucher No");//2
        DataModel.addColumn("Voucher Date");//3
        DataModel.addColumn("Main Account Code");//4
        DataModel.addColumn("Sub Account Code");//5
        DataModel.addColumn("Amount");//6
        DataModel.addColumn("User Name");//7
        
        DataModel.SetVariable(0,"");
        DataModel.SetVariable(1,"SR_NO");
        DataModel.SetVariable(2,"VOUCHER_NO");
        DataModel.SetVariable(3,"VOUCHER_DATE");
        DataModel.SetVariable(4,"MAIN_ACCOUNT_CODE");
        DataModel.SetVariable(5,"SUB_ACCOUNT_CODE");
        DataModel.SetVariable(6,"AMOUNT");
        DataModel.SetVariable(7,"USER_NAME");
        
        Rend.setCustomComponent(0,"CheckBox");
        Table.getColumnModel().getColumn(0).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        Table.getColumnModel().getColumn(2).setPreferredWidth(100);
        Table.getColumnModel().getColumn(3).setPreferredWidth(100);
        Table.getColumnModel().getColumn(7).setPreferredWidth(80);
    }
    
    private void GenerateGrid() {
        try {
            String str = "SELECT B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE,SUM(D.AMOUNT) AS AMOUNT, 'BARODA' AS COMPANY FROM DINESHMILLS.D_COM_DOC_DATA A, FINANCE.D_FIN_VOUCHER_HEADER B, DINESHMILLS.D_COM_USER_MASTER C, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE A.COMPANY_ID= 2  AND  A.STATUS='W' AND A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.VOUCHER_NO AND B.APPROVED=0 AND B.CANCELLED=0 AND B.VOUCHER_DATE >= '2008-04-01' AND A.COMPANY_ID=C.COMPANY_ID AND A.USER_ID=C.USER_ID AND C.DEPT_ID IN (10,16) AND A.COMPANY_ID=D.COMPANY_ID AND B.VOUCHER_NO=D.VOUCHER_NO AND D.EFFECT ='C' AND D.MAIN_ACCOUNT_CODE<>'' AND D.SUB_ACCOUNT_CODE<>'' GROUP BY B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE UNION SELECT B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE,SUM(D.AMOUNT), 'BARODA' AS COMPANY FROM DINESHMILLS.D_COM_DOC_DATA A, FINANCE.D_FIN_VOUCHER_HEADER B, DINESHMILLS.D_COM_USER_MASTER C, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE A.COMPANY_ID= 2  AND  A.STATUS='W' AND A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.VOUCHER_NO AND B.APPROVED=0 AND B.CANCELLED=0 AND B.VOUCHER_DATE >= '2008-04-01' AND A.COMPANY_ID=C.COMPANY_ID AND A.USER_ID=C.USER_ID AND C.DEPT_ID IN (10,16) AND A.COMPANY_ID=D.COMPANY_ID AND B.VOUCHER_NO=D.VOUCHER_NO AND D.EFFECT ='D' AND D.MAIN_ACCOUNT_CODE<>'' AND D.SUB_ACCOUNT_CODE<>'' GROUP BY B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE UNION  SELECT B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE,SUM(D.AMOUNT), 'ANKLESHWAR' AS COMPANY  FROM DINESHMILLSA.D_COM_DOC_DATA A, FINANCE.D_FIN_VOUCHER_HEADER B, DINESHMILLSA.D_COM_USER_MASTER C, FINANCE.D_FIN_VOUCHER_DETAIL D WHERE A.COMPANY_ID= 3  AND  A.STATUS='W' AND A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.VOUCHER_NO AND B.APPROVED=0 AND B.CANCELLED=0 AND B.VOUCHER_DATE >= '2008-04-01' AND A.COMPANY_ID=C.COMPANY_ID AND A.USER_ID=C.USER_ID AND C.DEPT_ID IN (10,16) AND A.COMPANY_ID=D.COMPANY_ID AND B.VOUCHER_NO=D.VOUCHER_NO AND D.MAIN_ACCOUNT_CODE<>'' AND D.SUB_ACCOUNT_CODE<>'' GROUP BY B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE  ORDER BY USER_NAME,VOUCHER_DATE,VOUCHER_NO";
 
            /*
                        String str = "SELECT B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME, "+
                " D.EFFECT,D.MAIN_ACCOUNT_CODE,D.SUB_ACCOUNT_CODE,D.AMOUNT "+
                " FROM DINESHMILLS.D_COM_DOC_DATA A, FINANCE.D_FIN_VOUCHER_HEADER B, "+
                " DINESHMILLS.D_COM_USER_MASTER C, FINANCE.D_FIN_VOUCHER_DETAIL D "+
                " WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MODULE_ID=59 AND A.STATUS='W' "+
                " AND A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.VOUCHER_NO "+
                " AND B.APPROVED=0 AND B.CANCELLED=0 AND B.VOUCHER_TYPE=1 AND B.VOUCHER_DATE >= '2008-04-01' "+
                " AND A.COMPANY_ID=C.COMPANY_ID AND A.USER_ID=C.USER_ID "+
                " AND C.DEPT_ID=10 AND A.COMPANY_ID=D.COMPANY_ID AND B.VOUCHER_NO=D.VOUCHER_NO "+
                " AND D.EFFECT='C' AND D.MAIN_ACCOUNT_CODE<>'' AND D.SUB_ACCOUNT_CODE<>'' "+
                " ORDER BY B.VOUCHER_DATE,B.VOUCHER_NO";
 
             
             
             */
            
            
            
            
//            "SELECT B.VOUCHER_NO,A.USER_ID,B.VOUCHER_DATE,C.DEPT_ID,C.USER_NAME "+
//                " FROM DINESHMILLS.D_COM_DOC_DATA A, FINANCE.D_FIN_VOUCHER_HEADER B, DINESHMILLS.D_COM_USER_MASTER C "+
//                " WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MODULE_ID=59 AND A.STATUS='W' "+
//                " AND A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.VOUCHER_NO "+
//                " AND B.APPROVED=0 AND B.CANCELLED=0 AND B.VOUCHER_TYPE=1 AND B.VOUCHER_DATE >= '2008-04-01' "+
//                " AND A.COMPANY_ID=C.COMPANY_ID AND A.USER_ID=C.USER_ID "+
//                " AND C.DEPT_ID=10 "+
//                " ORDER BY B.VOUCHER_DATE,B.VOUCHER_NO";

            ResultSet rsTmp = data.getResult(str);
            rsTmp.first();
            int i =0;
            if (rsTmp.getRow()>0) {
                while (!rsTmp.isAfterLast()) {
                    i++;
                    Object[] rowData=new Object[8];
                    rowData[0]=Boolean.valueOf(false); //By default not selected
                    rowData[1]=Integer.toString(i);
                    rowData[2]=rsTmp.getString("VOUCHER_NO");
                    rowData[3]=EITLERPGLOBAL.formatDate(rsTmp.getString("VOUCHER_DATE"));
                    rowData[4]=rsTmp.getString("MAIN_ACCOUNT_CODE");
                    rowData[5]=rsTmp.getString("SUB_ACCOUNT_CODE");
                    rowData[6]=Double.toString(rsTmp.getDouble("AMOUNT"));
                    rowData[7]=rsTmp.getString("USER_NAME");
                    
                    DataModel.addRow(rowData);

                    //Set the Collection
                    DataModel.SetUserObject(Table.getRowCount()-1, rowData);
                    rsTmp.next();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void GenerateReport() {        
        try {
            String VoucherNoList="";
            for(int i=0;i<=Table.getRowCount()-1;i++) {
                if(Table.getValueAt(i,0).toString().equals("true")) {
                    //Selected Item
                    VoucherNoList = VoucherNoList + "'" + DataModel.getValueByVariable("VOUCHER_NO",i) + "',";
                    //colSelItems.put(Integer.toString(colSelItems.size()+1),ObjItem);
                }
            }
            String strQuery = " AND A.VOUCHER_NO IN ("+VoucherNoList.substring(0,VoucherNoList.length()-1)+") ";
            
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptPendingAcctAuditList.jsp?dbURL="+FinanceGlobal.FinURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&Query="+strQuery);
            System.out.println("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptPendingAcctAuditList.jsp?dbURL="+FinanceGlobal.FinURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&Query="+strQuery);
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}