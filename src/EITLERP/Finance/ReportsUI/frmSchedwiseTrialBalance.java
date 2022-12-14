/*
 * frmSchedwiseTrialBalance.java
 *
 * Created on December 20, 2007, 6:27 AM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author
 */

import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import javax.sql.*;
import java.util.*;
import java.sql.* ;
import EITLERP.Finance.*;
import javax.swing.table.*;
import javax.swing.text.*;
import TReportWriter.*;
import java.text.*;

public class frmSchedwiseTrialBalance extends javax.swing.JApplet {
    
    clsAccount objAccount;
    EITLTableModel TableModelT;
    HashMap props;
    EITLComboModel cmbModel;
    private EITLTableCellRenderer RowFormat=new EITLTableCellRenderer();
    private boolean ProcessCompleted=false;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    private EITLTableCellRenderer CellAlign=new EITLTableCellRenderer();
    DecimalFormat dFormat=new DecimalFormat("###0.00");
    
    public frmSchedwiseTrialBalance() {
    }
    
    /** Initializes the applet frmSchedwiseTrialBalance */
    public void init() {
        initComponents();
        setSize(770,573);
        txtAsOnDate.requestFocus();
        cmbModel = new EITLComboModel();
        GenerateCombo();
        FormatGridN();
        Bar.setVisible(false);
        lblStatus.setVisible(false);
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        MainPanel = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAsOnDate = new javax.swing.JTextField();
        btnEnter = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox();
        Bar = new javax.swing.JProgressBar();
        lblStatus = new javax.swing.JLabel();
        chkApproved = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TResult = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmdSearch = new javax.swing.JButton();
        lblDebitTotal = new javax.swing.JLabel();
        lblCreditTotal = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        lblL_DebitTotal = new javax.swing.JLabel();
        lblL_CreditTotal = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel1.setText("As On Date :");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(26, 32, 100, 20);

        txtAsOnDate.setNextFocusableComponent(btnEnter);
        txtAsOnDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAsOnDateActionPerformed(evt);
            }
        });

        jPanel1.add(txtAsOnDate);
        txtAsOnDate.setBounds(116, 32, 120, 20);

        btnEnter.setText("Generate");
        btnEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterActionPerformed(evt);
            }
        });

        jPanel1.add(btnEnter);
        btnEnter.setBounds(303, 70, 100, 25);

        jLabel2.setText("Report for");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(26, 72, 80, 20);

        jPanel1.add(cmbType);
        cmbType.setBounds(116, 72, 180, 20);

        jPanel1.add(Bar);
        Bar.setBounds(30, 140, 180, 20);

        lblStatus.setText(".");
        jPanel1.add(lblStatus);
        lblStatus.setBounds(30, 120, 180, 15);

        chkApproved.setSelected(true);
        chkApproved.setText("Only Approved Vouchers");
        jPanel1.add(chkApproved);
        chkApproved.setBounds(279, 32, 209, 23);

        MainPanel.addTab("As On Date", jPanel1);

        jPanel2.setLayout(null);

        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        TResult.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TResult);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(2, 36, 750, 380);

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);
        jButton1.setBounds(653, 422, 100, 25);

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 9));
        jLabel11.setText("Click on the column,  type keywords in search textbox and click on Go button to search.");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(12, 14, 410, 11);

        jLabel12.setText("Search");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(445, 13, 50, 15);

        jPanel2.add(txtSearch);
        txtSearch.setBounds(500, 10, 130, 19);

        cmdSearch.setFont(new java.awt.Font("Dialog", 0, 10));
        cmdSearch.setText("Go");
        cmdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSearchActionPerformed(evt);
            }
        });

        jPanel2.add(cmdSearch);
        cmdSearch.setBounds(640, 11, 100, 20);

        lblDebitTotal.setFont(new java.awt.Font("Dialog", 1, 14));
        lblDebitTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDebitTotal.setText("...");
        jPanel2.add(lblDebitTotal);
        lblDebitTotal.setBounds(340, 430, 160, 17);

        lblCreditTotal.setFont(new java.awt.Font("Dialog", 1, 14));
        lblCreditTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreditTotal.setText("...");
        jPanel2.add(lblCreditTotal);
        lblCreditTotal.setBounds(510, 430, 140, 17);

        jButton2.setText("Generate Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton2);
        jButton2.setBounds(600, 460, 150, 25);

        lblL_DebitTotal.setFont(new java.awt.Font("Dialog", 1, 14));
        lblL_DebitTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblL_DebitTotal.setText("...");
        jPanel2.add(lblL_DebitTotal);
        lblL_DebitTotal.setBounds(0, 430, 160, 17);

        lblL_CreditTotal.setFont(new java.awt.Font("Dialog", 1, 14));
        lblL_CreditTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblL_CreditTotal.setText("...");
        jPanel2.add(lblL_CreditTotal);
        lblL_CreditTotal.setBounds(170, 430, 150, 17);

        MainPanel.addTab("View Result", jPanel2);

        getContentPane().add(MainPanel);
        MainPanel.setBounds(1, 35, 770, 520);

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("SCHEDULE WISE TRIAL BALANCE");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 230, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(1, 2, 800, 30);

    }//GEN-END:initComponents
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(cmbType.getSelectedIndex()==0) {
            GenerateNominal();
        }
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void cmdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSearchActionPerformed
        // TODO add your handling code here:
        searchWithin(' ');
    }//GEN-LAST:event_cmdSearchActionPerformed
    
    private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterActionPerformed
        // TODO add your handling code here:
        if(!txtAsOnDate.getText().trim().equals("")) {
            if(EITLERPGLOBAL.isDate(txtAsOnDate.getText().trim())) {
                if(cmbType.getSelectedIndex()==0) {
                    getAccountBalanceN();
                }
            } else {
                JOptionPane.showMessageDialog(null,"Please Enter the Date in dd/mm/yyyy format");
                txtAsOnDate.requestFocus();
            }
        }
        
        else {
            JOptionPane.showMessageDialog(null,"Please Enter the Date");
            txtAsOnDate.requestFocus();
        }
    }//GEN-LAST:event_btnEnterActionPerformed
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jPanel1MouseClicked
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MainPanel.setSelectedIndex(0);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void txtAsOnDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAsOnDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAsOnDateActionPerformed
    
    private void FormatGridN() {
        TResult.removeAll();
        TableModelT = new EITLTableModel();
        TResult.setModel(TableModelT);
        TableModelT.addColumn("Last Year Debit");
        TableModelT.addColumn("Last Year Credit");
        TableModelT.addColumn("SH. Code");
        TableModelT.addColumn("Main Code");
        TableModelT.addColumn("Account Name");
        TableModelT.addColumn("Debit");
        TableModelT.addColumn("Credit");
        
        TableModelT.SetVariable(0,"LASTYEAR_DEBIT_AMOUNT");
        TableModelT.SetVariable(1,"LASTYEAR_CREDIT_AMOUNT");
        TableModelT.SetVariable(2,"SH_CODE");
        TableModelT.SetVariable(3,"MAIN_CODE");
        TableModelT.SetVariable(4,"ACCOUNT_NAME");
        TableModelT.SetVariable(5,"DEBIT_AMOUNT");
        TableModelT.SetVariable(6,"CREDIT_AMOUNT");
        
        RowFormat =new EITLTableCellRenderer();
        
        for(int j=0;j<TResult.getColumnCount();j++) {
            TResult.getColumnModel().getColumn(j).setCellRenderer(RowFormat);
        }
        
        CellAlign=new EITLTableCellRenderer();
        CellAlign.setHorizontalAlignment(JLabel.RIGHT);
        
        TResult.getColumnModel().getColumn(0).setCellRenderer(CellAlign);
        TResult.getColumnModel().getColumn(1).setCellRenderer(CellAlign);
        TResult.getColumnModel().getColumn(5).setCellRenderer(CellAlign);
        TResult.getColumnModel().getColumn(6).setCellRenderer(CellAlign);
        
    }
    
    private void GenerateCombo() {
        cmbType.setModel(cmbModel);
        ComboData aData = new ComboData();
        aData.Text="Nominal Ledger";
        aData.Code=1;
        cmbModel.addElement(aData);
        cmbType.setSelectedIndex(0);
    }
    
    private void getAccountBalanceN() {
        
        new Thread(){
            
            public void run(){
                try {
                    double Amount,LastYear_Amount ;
                    int DrCount = 0;
                    int CrCount = 0;
                    int Counter=0;
                    double DrTotal=0;
                    double CrTotal=0;
                    double L_DrTotal=0;
                    double L_CrTotal=0;
                    int L_DrCount = 0;
                    
                    HashMap Credit= new HashMap();
                    HashMap Debit = new HashMap();
                    
                    int Count=data.getIntValueFromDB("SELECT COUNT(*) AS ACCOUNT_COUNT  FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE LIKE '%000' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL);
                    Bar.setVisible(true);
                    lblStatus.setVisible(true);
                    Bar.setMaximum(Count);
                    Bar.setMinimum(0);
                    Bar.setValue(0);
                    
                    String LastYear = "";
                    LastYear = txtAsOnDate.getText().trim();
                    int year = Integer.parseInt(LastYear.substring(LastYear.lastIndexOf("/")+1)) - 1;
                    LastYear = "31/03/"+ year ;
                    
                    ResultSet rsAccount =data.getResult("SELECT MAIN_ACCOUNT_CODE,ACCOUNT_NAME FROM D_FIN_GL ORDER BY SH_CODE,MAIN_ACCOUNT_CODE",FinanceGlobal.FinURL);
                    rsAccount.first();
                    while(!rsAccount.isAfterLast()) {
                        Counter++;
                        Bar.setValue(Counter);
                        lblStatus.setText("Processing Record "+Counter);
                        
                        
                        if(rsAccount.getString("MAIN_ACCOUNT_CODE").equals("133203")) {
                            boolean halt=true;
                        }
                        
                        
                        if(chkApproved.isSelected()) {
                            Amount = clsAccount.getClosingBalance(rsAccount.getString("MAIN_ACCOUNT_CODE"),"",EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText().trim()),true);                            
                            LastYear_Amount = clsAccount.getClosingBalance(rsAccount.getString("MAIN_ACCOUNT_CODE"),"",EITLERPGLOBAL.formatDateDB(LastYear),true);
                        }
                        else {
                            Amount = clsAccount.getAvailableClosingBalance(rsAccount.getString("MAIN_ACCOUNT_CODE"),"",EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText().trim()));
                            LastYear_Amount = clsAccount.getAvailableClosingBalance(rsAccount.getString("MAIN_ACCOUNT_CODE"),"",EITLERPGLOBAL.formatDateDB(LastYear));
                        }
                        
                        
                        clsSchedwiseTrialBalance ObjTrial = new clsSchedwiseTrialBalance();
                        ObjTrial.setAttribute("MAIN_ACCOUNT_CODE",rsAccount.getString("MAIN_ACCOUNT_CODE")) ;
                        ObjTrial.setAttribute("ACCOUNT_NAME",rsAccount.getString("ACCOUNT_NAME")) ;
                        
                        DrCount++;
                        
                        if(Amount > 0) {
                            DrTotal+=Amount;
                            //ObjTrial.setAttribute("CREDIT",dFormat.format(0)) ;
                            ObjTrial.setAttribute("DEBIT", dFormat.format(Amount)) ;
                            //Debit.put(Integer.toString(DrCount), ObjTrial);
                            //DrCount++;
                        }
                        else if(Amount < 0){
                            
                            CrTotal+=Math.abs(Amount);
                            //ObjTrial.setAttribute("DEBIT", dFormat.format(0)) ;
                            ObjTrial.setAttribute("CREDIT",dFormat.format(Math.abs(Amount))) ;
                            //Debit.put(Integer.toString(DrCount), ObjTrial);
                            //DrCount++;
                        }
                        else {
                            //ObjTrial.setAttribute("DEBIT", dFormat.format(0)) ;
                            //ObjTrial.setAttribute("CREDIT",dFormat.format(0)) ;
                            //Debit.put(Integer.toString(DrCount), ObjTrial);
                            //DrCount++;
                        }
                        
                        if(LastYear_Amount > 0) {
                            L_DrTotal+=LastYear_Amount;
                            //ObjTrial.setAttribute("LASTYEAR_CREDIT",dFormat.format(0)) ;
                            ObjTrial.setAttribute("LASTYEAR_DEBIT", dFormat.format(LastYear_Amount)) ;
                            //Debit.put(Integer.toString(L_DrCount), ObjTrial);
                            //L_DrCount++;
                        }
                        else if(LastYear_Amount < 0){
                            
                            L_CrTotal+=Math.abs(LastYear_Amount);
                            //ObjTrial.setAttribute("LASTYEAR_DEBIT", dFormat.format(0)) ;
                            ObjTrial.setAttribute("LASTYEAR_CREDIT",dFormat.format(Math.abs(LastYear_Amount))) ;
                            //Debit.put(Integer.toString(L_DrCount), ObjTrial);
                            //L_DrCount++;
                        }
                        else {
                            //ObjTrial.setAttribute("LASTYEAR_DEBIT", dFormat.format(0)) ;
                            //ObjTrial.setAttribute("LASTYEAR_CREDIT",dFormat.format(0)) ;
                            //Debit.put(Integer.toString(L_DrCount), ObjTrial);
                            //L_DrCount++;
                        }
                        
                        Debit.put(Integer.toString(DrCount), ObjTrial);
                        rsAccount.next();
                        
                    }
                    
                    lblDebitTotal.setText(dFormat.format(DrTotal));
                    lblCreditTotal.setText(dFormat.format(CrTotal));
                    
                    lblL_DebitTotal.setText(dFormat.format(L_DrTotal));
                    lblL_CreditTotal.setText(dFormat.format(L_CrTotal));
                    
                    FormatGridN();
                    GenerateGridN(Debit);
                    ProcessCompleted=true;
                    lblStatus.setText("Completed...");
                    
                }
                catch(Exception e) {
                    ProcessCompleted=true;
                    e.printStackTrace();
                }
                
                MainPanel.setSelectedIndex(1);
                Bar.setVisible(false);
                lblStatus.setVisible(false);
            };
        }.start();
        
    }
    
    private void GenerateGridN(HashMap List) {
        
        objData = new TReportWriter.SimpleDataProvider.TTable();
        
        for(int i=1;i<=List.size();i++) {
            clsSchedwiseTrialBalance ObjBalance =(clsSchedwiseTrialBalance)List.get(Integer.toString(i));
            //Object[] rowData = new Object[List.size()];
            Object[] rowData = new Object[7];

            String SHCode=data.getStringValueFromDB("SELECT SH_CODE FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+ObjBalance.getAttribute("MAIN_ACCOUNT_CODE").getString()+"'",FinanceGlobal.FinURL);
            
            rowData[0] = ObjBalance.getAttribute("LASTYEAR_DEBIT").getString();
            rowData[1] = ObjBalance.getAttribute("LASTYEAR_CREDIT").getString();
            rowData[2] = SHCode;
            rowData[3] = ObjBalance.getAttribute("MAIN_ACCOUNT_CODE").getString();
            rowData[4] = ObjBalance.getAttribute("ACCOUNT_NAME").getString();
            rowData[5] = ObjBalance.getAttribute("DEBIT").getString();
            rowData[6] = ObjBalance.getAttribute("CREDIT").getString();           
            
            TableModelT.addRow(rowData);
//            TResult.setValueAt(ObjBalance.getAttribute("LASTYEAR_DEBIT").getString(), i, 0);
//            TResult.setValueAt(ObjBalance.getAttribute("LASTYEAR_CREDIT").getString(), i,1);
//            TResult.setValueAt(SHCode, i,2);
//            TResult.setValueAt((String)ObjBalance.getAttribute("MAIN_ACCOUNT_CODE").getObj(),i,3);
//            TResult.setValueAt((String)ObjBalance.getAttribute("ACCOUNT_NAME").getObj(),i,4);
//            TResult.setValueAt(ObjBalance.getAttribute("DEBIT").getString(), i, 5);
//            TResult.setValueAt(ObjBalance.getAttribute("CREDIT").getString(), i,6);                        
            
            TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();            
            objRow.setValue("MAIN_CODE",(String)ObjBalance.getAttribute("MAIN_ACCOUNT_CODE").getObj());
            objRow.setValue("AC_NAME",(String)ObjBalance.getAttribute("ACCOUNT_NAME").getObj());
            objRow.setValue("DEBIT",ObjBalance.getAttribute("DEBIT").getString());
            objRow.setValue("CREDIT",ObjBalance.getAttribute("CREDIT").getString());
            objRow.setValue("SH_CODE",SHCode);
            objRow.setValue("LASTYEAR_DEBIT",ObjBalance.getAttribute("LASTYEAR_DEBIT").getString());
            objRow.setValue("LASTYEAR_CREDIT",ObjBalance.getAttribute("LASTYEAR_CREDIT").getString());            
            objData.AddRow(objRow);
        }
    }
    
    private void searchWithin(char recentKey) {
        
        try {
            
            RowFormat.removeBackColors();
            
            TResult.repaint();
            
            String searchString=txtSearch.getText();
            
            if(!searchString.trim().equals("")) {
                
                
                if(recentKey!=' ') {
                    searchString=searchString+recentKey;
                }
                
                searchString=searchString.toLowerCase();
                
                int currentCol=TResult.getSelectedColumn();
                
                for(int i=0;i<=TableModelT.getRowCount()-1;i++) {
                    
                    if(TableModelT.getValueAt(i,currentCol).toString().toLowerCase().indexOf(searchString)!=-1) {
                        
                        TableModelT.changeSelection(i, currentCol, false,false);
                        
                        for(int j=0;j<TableModelT.getColumnCount();j++) {
                            RowFormat.setBackColor(i, j, Color.YELLOW);
                        }
                        
                    }
                }
                
            }
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void GenerateNominal() {
        try {
            
            HashMap Parameters=new HashMap();
            Parameters.put("ON_DATE",txtAsOnDate.getText());
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptSchedNominalLedger.rpt",Parameters,objData);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JTable TResult;
    private javax.swing.JButton btnEnter;
    private javax.swing.JCheckBox chkApproved;
    private javax.swing.JComboBox cmbType;
    private javax.swing.JButton cmdSearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCreditTotal;
    private javax.swing.JLabel lblDebitTotal;
    private javax.swing.JLabel lblL_CreditTotal;
    private javax.swing.JLabel lblL_DebitTotal;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtAsOnDate;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
}
