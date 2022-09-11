/*
 * frmLedger.java
 *
 * Created on August 24, 2007, 10:52 AM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Finance.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;
import java.sql.*;
import EITLERP.Finance.Config.*;
import java.text.*;

public class frmTaxExFormUpdation extends javax.swing.JApplet {
    
    /** Initializes the applet frmLedger */
    public void init() {
        setSize(525, 330);
        initComponents();
        clearFields();
        SetMenuForRights();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        MainPanel = new javax.swing.JTabbedPane();
        TaxFormUpdation = new javax.swing.JPanel();
        txtPartyCode = new javax.swing.JTextField();
        lblPartyCode = new javax.swing.JLabel();
        lblMessage2 = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();
        cmdSingleStatus = new javax.swing.JButton();
        lblDateTo = new javax.swing.JLabel();
        txtDateTo = new javax.swing.JTextField();
        lblDateFrom = new javax.swing.JLabel();
        txtDateFrom = new javax.swing.JTextField();
        txtMainAccountCode = new javax.swing.JTextField();
        lblMainAccountCode = new javax.swing.JLabel();
        lblReceivedDate = new javax.swing.JLabel();
        txtReceivedDate = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" POSTING STATUS FOR TAX EXEMPTION FORM");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(1, 1, 520, 25);

        TaxFormUpdation.setLayout(null);

        TaxFormUpdation.setBorder(new javax.swing.border.EtchedBorder());
        txtPartyCode.setNextFocusableComponent(txtMainAccountCode);
        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusGained(evt);
            }
        });

        TaxFormUpdation.add(txtPartyCode);
        txtPartyCode.setBounds(90, 48, 120, 19);

        lblPartyCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPartyCode.setText("Party Code :");
        TaxFormUpdation.add(lblPartyCode);
        lblPartyCode.setBounds(5, 50, 80, 15);

        lblMessage2.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage2.setText(" Insert Party Code for Tax Exemption Form  Received.");
        lblMessage2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        TaxFormUpdation.add(lblMessage2);
        lblMessage2.setBounds(5, 10, 490, 20);

        lblPartyName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPartyName.setText("Party Name :");
        TaxFormUpdation.add(lblPartyName);
        lblPartyName.setBounds(5, 82, 80, 15);

        txtPartyName.setEnabled(false);
        TaxFormUpdation.add(txtPartyName);
        txtPartyName.setBounds(90, 80, 390, 19);

        cmdSingleStatus.setText("Update Tax Ex. Form Status");
        cmdSingleStatus.setNextFocusableComponent(txtPartyCode);
        cmdSingleStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSingleStatusActionPerformed(evt);
            }
        });

        TaxFormUpdation.add(cmdSingleStatus);
        cmdSingleStatus.setBounds(91, 178, 204, 25);

        lblDateTo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDateTo.setText("To Date :");
        TaxFormUpdation.add(lblDateTo);
        lblDateTo.setBounds(284, 110, 80, 15);

        txtDateTo.setNextFocusableComponent(txtReceivedDate);
        txtDateTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateToFocusGained(evt);
            }
        });

        TaxFormUpdation.add(txtDateTo);
        txtDateTo.setBounds(372, 109, 120, 19);

        lblDateFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDateFrom.setText("From Date :");
        TaxFormUpdation.add(lblDateFrom);
        lblDateFrom.setBounds(5, 113, 80, 15);

        txtDateFrom.setNextFocusableComponent(txtDateTo);
        txtDateFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateFromFocusGained(evt);
            }
        });

        TaxFormUpdation.add(txtDateFrom);
        txtDateFrom.setBounds(90, 110, 120, 19);

        txtMainAccountCode.setNextFocusableComponent(txtDateFrom);
        txtMainAccountCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMainAccountCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMainAccountCodeFocusLost(evt);
            }
        });

        TaxFormUpdation.add(txtMainAccountCode);
        txtMainAccountCode.setBounds(370, 47, 120, 19);

        lblMainAccountCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMainAccountCode.setText("Main Account Code :");
        TaxFormUpdation.add(lblMainAccountCode);
        lblMainAccountCode.setBounds(225, 49, 139, 15);

        lblReceivedDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReceivedDate.setText("Received Date :");
        TaxFormUpdation.add(lblReceivedDate);
        lblReceivedDate.setBounds(8, 147, 95, 15);

        txtReceivedDate.setNextFocusableComponent(cmdSingleStatus);
        txtReceivedDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReceivedDateFocusGained(evt);
            }
        });

        TaxFormUpdation.add(txtReceivedDate);
        txtReceivedDate.setBounds(107, 144, 120, 19);

        MainPanel.addTab("Tax Form Updation", TaxFormUpdation);

        getContentPane().add(MainPanel);
        MainPanel.setBounds(2, 27, 510, 240);

        lblStatus.setText("...");
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(5, 270, 505, 20);

    }//GEN-END:initComponents
    
    private void txtReceivedDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceivedDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert Form Received date.");
    }//GEN-LAST:event_txtReceivedDateFocusGained
    
    private void txtDateToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateToFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert End date of Financial Year.");
    }//GEN-LAST:event_txtDateToFocusGained
    
    private void txtDateFromFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFromFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert Start date of Financial Year.");
    }//GEN-LAST:event_txtDateFromFocusGained
    
    private void txtMainAccountCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainAccountCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert Main Code.");
    }//GEN-LAST:event_txtMainAccountCodeFocusGained
    
    private void txtMainAccountCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainAccountCodeFocusLost
        // TODO add your handling code here:
        if(!txtPartyCode.getText().trim().equals("")) {
            generateData();
        } else {
            txtPartyName.setText("");
        }
    }//GEN-LAST:event_txtMainAccountCodeFocusLost
    
    private void cmdSingleStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSingleStatusActionPerformed
        // TODO add your handling code here:
        ShowMessage("Processing...");
        if(!Validate()) {
            ShowMessage("Not Processed...");
            return;
        }
        generateData();
        updateData();
        clearFields();
        ShowMessage("Done...");
    }//GEN-LAST:event_cmdSingleStatusActionPerformed
    
    private void txtPartyCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert Party Code.");
    }//GEN-LAST:event_txtPartyCodeFocusGained
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JPanel TaxFormUpdation;
    private javax.swing.JButton cmdSingleStatus;
    private javax.swing.JLabel lblDateFrom;
    private javax.swing.JLabel lblDateTo;
    private javax.swing.JLabel lblMainAccountCode;
    private javax.swing.JLabel lblMessage2;
    private javax.swing.JLabel lblPartyCode;
    private javax.swing.JLabel lblPartyName;
    private javax.swing.JLabel lblReceivedDate;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtDateFrom;
    private javax.swing.JTextField txtDateTo;
    private javax.swing.JTextField txtMainAccountCode;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtReceivedDate;
    // End of variables declaration//GEN-END:variables
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
    private boolean Validate() {
        /*if(MainPanel.getSelectedIndex() == 0) {
            if(txtFinancialYear.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null,"Please Enter Financial year like 20??-20??.");
                return false;
            }
        } else if(MainPanel.getSelectedIndex() == 1) {*/
        if(!data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' AND MAIN_ACCOUNT_CODE='"+txtMainAccountCode.getText().trim()+"' ",FinanceGlobal.FinURL)) {
            JOptionPane.showMessageDialog(null,"Please Enter valid Party Code and Main Account Code.");
            return false;
        }
        
        if(txtDateFrom.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Date From.");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtDateFrom.getText())) {
            JOptionPane.showMessageDialog(null,"Please enter valid Date From in DD/MM/YY Format.");
            return false;
        }
        
        if(txtDateTo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Date To.");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtDateTo.getText())) {
            JOptionPane.showMessageDialog(null,"Please enter valid Date To in DD/MM/YY Format.");
            return false;
        }
        
        if(txtReceivedDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter Date To.");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtReceivedDate.getText())) {
            JOptionPane.showMessageDialog(null,"Please enter valid Date To in DD/MM/YY Format.");
            return false;
        }
        
        
        java.sql.Date FDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtDateFrom.getText().trim()));
        java.sql.Date TDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtDateTo.getText().trim()));
        java.sql.Date RDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(txtReceivedDate.getText().trim()));
        
        if((RDate.after(FDate)||RDate.compareTo(FDate)==0)&&(RDate.before(TDate)||RDate.compareTo(TDate)==0)) {
            //Within the year
        }
        else {
            JOptionPane.showMessageDialog(null,"Received Date does not belong between From Date & To Date.");
            return false;
        }
        //}
        return true;
    }
    
    private void generateData() {
        //if(MainPanel.getSelectedIndex()==1) {
            txtPartyName.setText(data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' AND MAIN_ACCOUNT_CODE='"+txtMainAccountCode.getText().trim()+"' ",FinanceGlobal.FinURL));
        //}
    }
    
    private void updateData() {
        try {
            /*if(MainPanel.getSelectedIndex() == 0) {
                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET TAX_EX_FORM_RECEIVED=0 WHERE DEPOSIT_STATUS=0",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET TAX_EX_FORM_RECEIVED=0 WHERE DEPOSIT_STATUS=0",FinanceGlobal.FinURL);
            } else if(MainPanel.getSelectedIndex() == 1) {*/
            String PartyCode = txtPartyCode.getText().trim();
            String PartyName = txtPartyName.getText().trim();
            String MainAccountCode = txtMainAccountCode.getText().trim();
            data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET TAX_EX_FORM_RECEIVED=1 WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL);
            data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET TAX_EX_FORM_RECEIVED=1 WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL);
            String SQL = "SELECT A.RECEIPT_NO FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND A.SCHEME_ID=B.SCHEME_ID AND B.SCHEME_TYPE=3";
            ResultSet rsReceiptCD = data.getResult(SQL,FinanceGlobal.FinURL);
            rsReceiptCD.first();
            if(rsReceiptCD.getRow() > 0) {
                while(!rsReceiptCD.isAfterLast()) {
                    String ReceiptNo = rsReceiptCD.getString("RECEIPT_NO");
                    String DateFrom = EITLERPGLOBAL.formatDateDB(txtDateFrom.getText());
                    String DateTo = EITLERPGLOBAL.formatDateDB(txtDateTo.getText());
                    SQL = "SELECT DOC_NO,SR_NO,COMPANY_ID,INTEREST_AMOUNT,TDS_AMOUNT,NET_INTEREST FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+DateFrom+"' AND WARRANT_DATE<='"+DateTo+"' ";
                    ResultSet rsInterestCD = data.getResult(SQL,FinanceGlobal.FinURL);
                    rsInterestCD.first();
                    if(rsInterestCD.getRow() > 0) {
                        while(!rsInterestCD.isAfterLast()){
                            int CompanyID = rsInterestCD.getInt("COMPANY_ID");
                            String DocNo = rsInterestCD.getString("DOC_NO");
                            int srNo = rsInterestCD.getInt("SR_NO");
                            double interestAmount = rsInterestCD.getDouble("INTEREST_AMOUNT");
                            double TDSAmount = rsInterestCD.getDouble("TDS_AMOUNT");
                            double netInterest = rsInterestCD.getDouble("NET_INTEREST");
                            data.Execute("UPDATE D_FD_INT_CALC_DETAIL SET NET_INTEREST="+interestAmount+",TDS_AMOUNT=0 WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND DOC_NO='"+DocNo+"' AND SR_NO="+srNo, FinanceGlobal.FinURL);
                            data.Execute("UPDATE D_FD_INT_CALC_DETAIL_H SET NET_INTEREST="+interestAmount+",TDS_AMOUNT=0 WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND DOC_NO='"+DocNo+"' AND SR_NO="+srNo, FinanceGlobal.FinURL);
                            rsInterestCD.next();
                        }
                    }
                    rsReceiptCD.next();
                }
            }
            int srNo = UtilFunctions.CInt(Integer.toString(data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FD_TAX_FORM_RECEIVED",FinanceGlobal.FinURL)))+1;
            Connection Conn=data.getConn(FinanceGlobal.FinURL);
            Statement Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSql = "SELECT * FROM D_FD_TAX_FORM_RECEIVED LIMIT 1";
            ResultSet rsTaxForm=Stmt.executeQuery(strSql);
            
            rsTaxForm.moveToInsertRow();
            rsTaxForm.updateLong("SR_NO", srNo);
            rsTaxForm.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsTaxForm.updateString("PARTY_CODE", PartyCode);
            rsTaxForm.updateString("PARTY_NAME", PartyName);
            rsTaxForm.updateString("MAIN_ACCOUNT_CODE", MainAccountCode);
            rsTaxForm.updateBoolean("TAX_EX_FORM_RECEIVED", true);
            rsTaxForm.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(txtDateFrom.getText().trim()));
            rsTaxForm.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(txtDateTo.getText().trim()));
            rsTaxForm.updateString("RECEIVED_DATE", EITLERPGLOBAL.formatDateDB(txtReceivedDate.getText().trim()));
            rsTaxForm.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsTaxForm.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsTaxForm.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTaxForm.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsTaxForm.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTaxForm.updateBoolean("CHANGED",true);
            rsTaxForm.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTaxForm.updateBoolean("CANCELLED",false);
            rsTaxForm.insertRow();
            rsTaxForm.close();
            //}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clearFields() {
        /*if(MainPanel.getSelectedIndex() == 0) {
            txtFinancialYear.setText("");
        } else if(MainPanel.getSelectedIndex() == 1) {*/
        txtPartyCode.setText("");
        txtPartyName.setText("");
        txtMainAccountCode.setText("");
        txtDateFrom.setText("");
        txtDateTo.setText("");
        txtPartyCode.requestFocus();
        txtReceivedDate.setText("");
        //}
    }
    
    private void SetMenuForRights() {
        //int Counter = 0;
        // --- All Tax Exemption Form Cleared --
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11111)) {
            MainPanel.setSelectedIndex(0);
            MainPanel.setEnabledAt(0, true);
            //Counter++;
        }
        else {
            MainPanel.setEnabledAt(0, false);
        }*/
        
        // --- Party Tax Exemption Form Received--
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11112)) {
            MainPanel.setSelectedIndex(0);
            MainPanel.setEnabledAt(1, true);
            //Counter++;
        }
        else {
            MainPanel.setEnabledAt(1, false);
        }*/
        /*if(Counter == 2) {
            MainPanel.setSelectedIndex(0);
        }*/
    }
}
