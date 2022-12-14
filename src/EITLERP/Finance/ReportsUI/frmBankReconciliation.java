/*
 * frmLegacyVouchers.java
 *
 * Created on August 23, 2008, 11:18 AM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */

import EITLERP.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.*;

public class frmBankReconciliation extends javax.swing.JApplet {
    
    private EITLTableModel DataModel=new EITLTableModel();
    
    /** Initializes the applet frmLegacyVouchers */
    public void init() {
        setSize(675,500);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        bgVoucher = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtVoucherNo = new javax.swing.JTextField();
        cmdShowList = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        lblBookName = new javax.swing.JLabel();
        OpgPayment = new javax.swing.JRadioButton();
        OpgReceipt = new javax.swing.JRadioButton();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" ASSIGN REALIZATION DATE TO VOUCHER...");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 2, 666, 25);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(5, 67, 85, 15);

        jLabel2.setText("Display voucher of period :->");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(6, 39, 189, 15);

        txtFromDate.setNextFocusableComponent(txtToDate);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(95, 66, 100, 19);

        txtToDate.setNextFocusableComponent(OpgPayment);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(263, 67, 100, 19);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(200, 68, 60, 15);

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
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseDBLClicked(evt);
            }
        });

        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(7, 146, 648, 289);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Voucher No. :");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(5, 93, 85, 15);

        txtVoucherNo.setNextFocusableComponent(txtBookCode);
        getContentPane().add(txtVoucherNo);
        txtVoucherNo.setBounds(95, 92, 160, 19);

        cmdShowList.setText("Show List");
        cmdShowList.setNextFocusableComponent(cmdSave);
        cmdShowList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowListActionPerformed(evt);
            }
        });

        getContentPane().add(cmdShowList);
        cmdShowList.setBounds(503, 113, 100, 25);

        cmdSave.setText("Save");
        cmdSave.setNextFocusableComponent(txtFromDate);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSave);
        cmdSave.setBounds(553, 440, 90, 25);

        lblStatus.setForeground(new java.awt.Color(51, 153, 255));
        lblStatus.setText("Status");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(4, 439, 280, 15);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Book Code :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(5, 118, 85, 15);

        txtBookCode.setNextFocusableComponent(cmdShowList);
        txtBookCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBookCodeFocusLost(evt);
            }
        });
        txtBookCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBookCodeKeyPressed(evt);
            }
        });
        txtBookCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseDBLClicked(evt);
            }
        });

        getContentPane().add(txtBookCode);
        txtBookCode.setBounds(95, 117, 100, 19);

        lblBookName.setText(".");
        getContentPane().add(lblBookName);
        lblBookName.setBounds(204, 119, 280, 15);

        OpgPayment.setSelected(true);
        OpgPayment.setText(" Payment");
        bgVoucher.add(OpgPayment);
        OpgPayment.setNextFocusableComponent(OpgReceipt);
        getContentPane().add(OpgPayment);
        OpgPayment.setBounds(400, 70, 82, 20);

        OpgReceipt.setText(" Receipt");
        bgVoucher.add(OpgReceipt);
        OpgReceipt.setNextFocusableComponent(txtVoucherNo);
        getContentPane().add(OpgReceipt);
        OpgReceipt.setBounds(500, 70, 80, 20);

    }//GEN-END:initComponents
    
    private void mouseDBLClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseDBLClicked
        // TODO add your handling code here:
        try {
            if(evt.getClickCount()==2) {
                String VoucherNo = DataModel.getValueAt(Table.getSelectedRow(),1).toString();
                clsVoucher.OpenVoucher(VoucherNo, null);
            }
        }catch(Exception e) {}
        
    }//GEN-LAST:event_mouseDBLClicked
    
    private void txtBookCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusLost
        // TODO add your handling code here:
        lblBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
    }//GEN-LAST:event_txtBookCodeFocusLost
    
    private void txtBookCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBookCodeKeyPressed
        // TODO add your handling code here:
        try {
            
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT BOOK_CODE,BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_TYPE=1 AND BOOK_CODE NOT IN (30,70,26) ORDER BY BOOK_CODE";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtBookCode.setText(aList.ReturnVal);
                    lblBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
                }
            }
        }
        catch(Exception e) {
        }
        
    }//GEN-LAST:event_txtBookCodeKeyPressed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        new Thread() {
            public void run() {
                try {
                    for(int i=0;i<Table.getRowCount();i++) {
                        String VoucherNo=DataModel.getValueByVariable("VOUCHER_NO", i);
                        
                        lblStatus.setText("Updating Voucher "+VoucherNo);
                        
                        String LegacyNo=DataModel.getValueByVariable("LEGACY_NO", i).toString();
                        String LegacyDate=EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("LEGACY_DATE", i).toString());
                        String RealizationDate=EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("REALIZATION_DATE", i).toString());
                        String ChequeDate = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("CHEQUE_DATE", i).toString());
                        String VoucherDate = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("VOUCHER_DATE", i).toString());
                        if(!RealizationDate.equals("")) {
                            if(java.sql.Date.valueOf(RealizationDate).before(java.sql.Date.valueOf(VoucherDate))) {
                                JOptionPane.showMessageDialog(null,"Please enter valid realization date against Voucher No. "+ VoucherNo +" and Legacy no. "+LegacyNo);
                                continue;
                            }
                        }
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET REALIZATION_DATE='"+RealizationDate+"', CHANGED=1, CHANGED_DATE=CURDATE() WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER_H SET REALIZATION_DATE='"+RealizationDate+"', CHANGED=1, CHANGED_DATE=CURDATE() WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                    }
                    lblStatus.setText("Ready");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdShowListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowListActionPerformed
        // TODO add your handling code here:
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter from date...");
            return;
        } else if(!(EITLERPGLOBAL.isDate(txtFromDate.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Please Enter The From Date in DD/MM/YYYY Format.");
            return;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter to date...");
            return;
        } else if(!(EITLERPGLOBAL.isDate(txtToDate.getText().trim()))) {
            JOptionPane.showMessageDialog(null,"Please Enter The To Date in DD/MM/YYYY Format.");
            return;
        }
        
        if(txtBookCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter book code...");
            return;
        }
        
        String SQL = "SELECT * FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+txtBookCode.getText().trim()+"' AND BOOK_TYPE=1 AND BOOK_CODE NOT IN (30,70,26)";
        if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
            JOptionPane.showMessageDialog(this,"Please specify proper Bank Book Code.");
            return;
        }
        
        new Thread() {
            
            public void run() {
                try {
                    String strSQL="SELECT VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,LEGACY_DATE,CHEQUE_NO, CHEQUE_DATE, REALIZATION_DATE, VOUCHER_TYPE FROM D_FIN_VOUCHER_HEADER  WHERE APPROVED=1 AND CANCELLED=0 ";
                    String strCondition="";
                    
                    if(!txtFromDate.getText().trim().equals("")) {
                        strCondition+="AND VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
                    }
                    
                    if(!txtToDate.getText().trim().equals("")) {
                        strCondition+="AND VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
                    }
                    
                    if(!txtVoucherNo.getText().trim().equals("")) {
                        strCondition+="AND VOUCHER_NO='"+txtVoucherNo.getText()+"' ";
                    }
                    
                    if(!txtBookCode.getText().trim().equals("")) {
                        strCondition+="AND BOOK_CODE='"+txtBookCode.getText()+"' ";
                    }
                    
                    if(OpgPayment.isSelected()) {
                        strCondition+="AND VOUCHER_TYPE IN ("+FinanceGlobal.TYPE_PAYMENT+","+FinanceGlobal.TYPE_PAYMENT_2+") ";
                    } else if(OpgReceipt.isSelected()) {
                        strCondition+="AND VOUCHER_TYPE IN ("+FinanceGlobal.TYPE_RECEIPT+") ";
                    }
                    
                    
                    strSQL+=strCondition;
                    strSQL+="GROUP BY VOUCHER_NO " +
                    "ORDER BY CONVERT(LEGACY_NO,SIGNED) ";
                    
                    lblStatus.setText("Fetching Records ... ");
                    
                    
                    
                    ResultSet rsParty;
                    
                    ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    FormatGrid();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            
                            Object[] rowData=new Object[1];
                            DataModel.addRow(rowData);
                            
                            int NewRow=Table.getRowCount()-1;
                            lblStatus.setText("Generating Table "+NewRow);
                            
                            String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                            String VoucherDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00"));
                            String LegacyNo=UtilFunctions.getString(rsTmp,"LEGACY_NO","");
                            String LegacyDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"LEGACY_DATE","0000-00-00"));
                            String ChequeNo=UtilFunctions.getString(rsTmp,"CHEQUE_NO","");
                            String ChequeDate=EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00"));
                            String RealizationDate = EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"REALIZATION_DATE","0000-00-00"));
                            String AccountName = "";
                            int VoucherType = UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE",0);
                            
                            String Effect = "";
                            if(OpgPayment.isSelected()) {
                                Effect="D";
                            }
                            if(OpgReceipt.isSelected()) {
                                Effect="C";
                            }
                            
                            double Amount = 0;
                            String strAmount = "";
                            rsParty = data.getResult("SELECT MAIN_ACCOUNT_CODE, SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO ='"+VoucherNo+"' AND EFFECT='"+Effect+"' AND SUB_ACCOUNT_CODE <> '' AND IS_DEDUCTION<>1 " , FinanceGlobal.FinURL);
                            rsParty.first();
                            if(rsParty.getRow()>0) {
                                AccountName = clsAccount.getAccountName(rsParty.getString("MAIN_ACCOUNT_CODE"), rsParty.getString("SUB_ACCOUNT_CODE"));
                                DataModel.setValueByVariable("PARTY_NAME",AccountName,NewRow);
                                strAmount = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" " +
                                "AND VOUCHER_NO ='"+VoucherNo+"' AND EFFECT='"+Effect+"' AND IS_DEDUCTION<>1 ";
                                Amount = data.getDoubleValueFromDB(strAmount,FinanceGlobal.FinURL);
                            } else {
                                rsParty = data.getResult("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO ='"+VoucherNo+"' AND EFFECT='"+Effect+"' AND IS_DEDUCTION<>1 " , FinanceGlobal.FinURL);
                                if(rsParty.getRow()>0) {
                                    AccountName = clsAccount.getAccountName(rsParty.getString("MAIN_ACCOUNT_CODE"), "");
                                    DataModel.setValueByVariable("PARTY_NAME",AccountName,NewRow);
                                    strAmount = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" " +
                                    "AND VOUCHER_NO ='"+VoucherNo+"' AND EFFECT='"+Effect+"' AND IS_DEDUCTION<>1 ";
                                    Amount = data.getDoubleValueFromDB(strAmount,FinanceGlobal.FinURL);
                                }
                            }
                            
                            DataModel.setValueByVariable("AMOUNT",Double.toString(Amount),NewRow);
                            
                            DataModel.setValueByVariable("SR_NO",Integer.toString(NewRow+1), NewRow);
                            DataModel.setValueByVariable("VOUCHER_NO",VoucherNo,NewRow);
                            DataModel.setValueByVariable("VOUCHER_DATE",VoucherDate,NewRow);
                            DataModel.setValueByVariable("LEGACY_NO",LegacyNo,NewRow);
                            DataModel.setValueByVariable("LEGACY_DATE",LegacyDate,NewRow);
                            DataModel.setValueByVariable("CHEQUE_NO",ChequeNo,NewRow);
                            DataModel.setValueByVariable("CHEQUE_DATE",ChequeDate,NewRow);
                            DataModel.setValueByVariable("REALIZATION_DATE",RealizationDate,NewRow);
                            
                            rsTmp.next();
                        }
                    }
                    lblStatus.setText("Ready ");
                }
                catch(Exception e) {
                    lblStatus.setText("Ready ");
                }
            };
        }.start();
    }//GEN-LAST:event_cmdShowListActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgPayment;
    private javax.swing.JRadioButton OpgReceipt;
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup bgVoucher;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBookName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtVoucherNo;
    // End of variables declaration//GEN-END:variables
    
    
    private void FormatGrid() {
        
        
        try {
            
            
            DataModel=new EITLTableModel();
            Table.removeAll();
            
            Table.setModel(DataModel);
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            DataModel.addColumn("Sr."); //0 - Read Only
            DataModel.addColumn("Voucher No."); //1
            DataModel.addColumn("Voucher Date"); //2
            DataModel.addColumn("Legacy No."); //3
            DataModel.addColumn("Legacy Date"); //4
            DataModel.addColumn("Party Name"); //5
            DataModel.addColumn("Cheque No."); //6
            DataModel.addColumn("Cheque Date"); //7
            DataModel.addColumn("Amount"); //8
            DataModel.addColumn("Realization Date"); //9
            
            DataModel.SetVariable(0,"SR_NO"); //0 - Read Only
            DataModel.SetVariable(1,"VOUCHER_NO"); //1 - Read Only
            DataModel.SetVariable(2,"VOUCHER_DATE"); //2 - Read Only
            DataModel.SetVariable(3,"LEGACY_NO"); //3 - Read Only
            DataModel.SetVariable(4,"LEGACY_DATE"); //4 - Read Only
            DataModel.SetVariable(5,"PARTY_NAME"); //5 - Read Only
            DataModel.SetVariable(6,"CHEQUE_NO"); //6 - Read Only
            DataModel.SetVariable(7,"CHEQUE_DATE"); //7 - Read Only
            DataModel.SetVariable(8,"AMOUNT"); //8 - Read Only
            
            DataModel.SetVariable(9,"REALIZATION_DATE");
            
            DataModel.TableReadOnly(false);
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);
            DataModel.SetReadOnly(5);
            DataModel.SetReadOnly(6);
            DataModel.SetReadOnly(7);
            DataModel.SetReadOnly(8);
            
            DataModel.SetNumeric(0,true);
            DataModel.SetNumeric(8,true);
            
            Table.getColumnModel().getColumn(0).setPreferredWidth(40);
            Table.getColumnModel().getColumn(1).setPreferredWidth(100);
            Table.getColumnModel().getColumn(2).setPreferredWidth(90);
            Table.getColumnModel().getColumn(3).setPreferredWidth(90);
            Table.getColumnModel().getColumn(4).setPreferredWidth(90);
            Table.getColumnModel().getColumn(5).setPreferredWidth(125);
            Table.getColumnModel().getColumn(6).setPreferredWidth(80);
            Table.getColumnModel().getColumn(7).setPreferredWidth(90);
            Table.getColumnModel().getColumn(8).setPreferredWidth(105);
            Table.getColumnModel().getColumn(9).setPreferredWidth(100);
        }
        catch(Exception e) {
        }
        //Table formatting completed
    }
    
}
