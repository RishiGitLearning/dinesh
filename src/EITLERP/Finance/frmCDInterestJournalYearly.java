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

public class frmCDInterestJournalYearly extends javax.swing.JApplet {
    public String VoucherDate="";
    
    /** Initializes the applet frmLedger */
    public void init() {
        setSize(525, 295);
        initComponents();
        clearFields();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblTitle = new javax.swing.JLabel();
        MainPanel = new javax.swing.JTabbedPane();
        CDInterestTransfer = new javax.swing.JPanel();
        lblFromDate = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        cmdInterestTransfer = new javax.swing.JButton();
        lblToDate = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        lblMessage = new javax.swing.JLabel();
        lblMessage1 = new javax.swing.JLabel();
        cmdClear = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" CD Interest Transfer - Journal TR-24");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(1, 1, 520, 25);

        CDInterestTransfer.setLayout(null);

        CDInterestTransfer.setBorder(new javax.swing.border.EtchedBorder());
        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date :");
        CDInterestTransfer.add(lblFromDate);
        lblFromDate.setBounds(29, 60, 77, 15);

        txtFromDate.setNextFocusableComponent(txtToDate);
        txtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFromDateFocusGained(evt);
            }
        });

        CDInterestTransfer.add(txtFromDate);
        txtFromDate.setBounds(110, 60, 130, 19);

        cmdInterestTransfer.setText("CD Interest Transfer Yearly");
        cmdInterestTransfer.setNextFocusableComponent(txtFromDate);
        cmdInterestTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInterestTransferActionPerformed(evt);
            }
        });

        CDInterestTransfer.add(cmdInterestTransfer);
        cmdInterestTransfer.setBounds(121, 111, 207, 25);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("To Date :");
        CDInterestTransfer.add(lblToDate);
        lblToDate.setBounds(260, 62, 63, 15);

        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToDateFocusGained(evt);
            }
        });

        CDInterestTransfer.add(txtToDate);
        txtToDate.setBounds(330, 60, 130, 19);

        lblMessage.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage.setText(" Insert From Date and To Date to transfer CD Interest in TR- 24 at the end of ");
        lblMessage.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        CDInterestTransfer.add(lblMessage);
        lblMessage.setBounds(5, 10, 500, 20);

        lblMessage1.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblMessage1.setText("  Financial Year.");
        lblMessage1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        CDInterestTransfer.add(lblMessage1);
        lblMessage1.setBounds(5, 30, 500, 20);

        cmdClear.setText("Clear");
        cmdClear.setNextFocusableComponent(txtFromDate);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        CDInterestTransfer.add(cmdClear);
        cmdClear.setBounds(340, 110, 80, 25);

        MainPanel.addTab("CD Interest Journal Transfer", CDInterestTransfer);

        getContentPane().add(MainPanel);
        MainPanel.setBounds(2, 27, 520, 200);

        lblStatus.setText("...");
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(5, 230, 505, 20);

    }//GEN-END:initComponents
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        txtFromDate.setText("");
        txtToDate.setText("");
    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdInterestTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInterestTransferActionPerformed
        // TODO add your handling code here:
        ShowMessage("Processing...");
        if(!Validate()) {
            ShowMessage("Not Processed Properly...");
            return;
        }
        GetData();
        //clearFields();
        //ShowMessage("Done...");
    }//GEN-LAST:event_cmdInterestTransferActionPerformed
    
    private void txtToDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert To Date in DD/MM/YYYY format..");
    }//GEN-LAST:event_txtToDateFocusGained
    
    private void txtFromDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Insert From Date in DD/MM/YYYY format.");
    }//GEN-LAST:event_txtFromDateFocusGained
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CDInterestTransfer;
    private javax.swing.JTabbedPane MainPanel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdInterestTransfer;
    private javax.swing.JLabel lblFromDate;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblMessage1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblToDate;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
    private boolean Validate() {
        if(txtFromDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter From Date.");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtFromDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please Enter From Date in DD/MM/YYYY format.");
            return false;
        }
        
        if(txtToDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date.");
            return false;
        } else if(!EITLERPGLOBAL.isDate(txtToDate.getText().trim())) {
            JOptionPane.showMessageDialog(null,"Please Enter To Date in DD/MM/YYYY format.");
            return false;
        }
        
        return true;
    }
    
    private void GetData() {
        try {
            String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
            String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
            VoucherDate = ToDate;
            int Counter = 0;
            String strSql = "SELECT DISTINCT A.RECEIPT_NO,A.PARTY_CODE,A.RECEIPT_DATE,A.INT_CALC_DATE,A.MATURITY_DATE,A.COMPANY_ID FROM D_FD_DEPOSIT_MASTER A, D_FD_INT_CALC_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.RECEIPT_NO=B.RECEIPT_NO AND A.DEPOSIT_TYPE_ID=2 AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND B.WARRANT_DATE>='"+FromDate+"' AND B.WARRANT_DATE<='"+ToDate+"' AND (A.PM_DATE>'"+ToDate+"' OR A.PM_DATE='0000-00-00' OR A.PM_DATE='') " +
            "AND A.MATURITY_DATE>'"+ToDate+"' GROUP BY A.RECEIPT_NO ORDER BY A.MATURITY_DATE ";
            
            /*String strSql = "SELECT A.RECEIPT_NO, A.COMPANY_ID, A.INT_CALC_DATE, A.MATURITY_DATE FROM D_FD_DEPOSIT_MASTER A, D_FD_INT_CALC_DETAIL B " +
            " WHERE A.COMPANY_ID=B.COMPANY_ID AND A.RECEIPT_NO=B.RECEIPT_NO AND A.DEPOSIT_TYPE_ID=2 " +
            " AND B.WARRANT_DATE>='"+FromDate+"' AND B.WARRANT_DATE<='"+ToDate+"' " +
            " GROUP BY RECEIPT_NO";*/
            //" AND A.RECEIPT_NO='M003330'" +
            Connection conn = data.getConn(FinanceGlobal.FinURL);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInterest = stmt.executeQuery(strSql);
            rsInterest.first();
            
            while(!rsInterest.isAfterLast()) {
                /*String MaturityDate = rsInterest.getString("MATURITY_DATE");
                String InterestDate = rsInterest.getString("INT_CALC_DATE");
                if(InterestDate==null) {
                    rsInterest.next();
                    continue;
                }
                String tempDate = EITLERPGLOBAL.addDaysToDate(InterestDate, 1, "yyyy-MM-dd");
                int CompanyID = rsInterest.getInt("COMPANY_ID");
                String ReceiptNo = rsInterest.getString("RECEIPT_NO");
                //double InterestAmount = rsInterest.getDouble("TOTAL_AMOUNT");
                if(java.sql.Date.valueOf(tempDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0) {
                    rsInterest.next();
                    System.out.println(ReceiptNo);
                    continue;
                } else {*/
                
                String ReceiptNo = rsInterest.getString("RECEIPT_NO");
                int CompanyID = rsInterest.getInt("COMPANY_ID");
                String PartyCode = rsInterest.getString("PARTY_CODE");
                double InterestAmount = data.getDoubleValueFromDB("SELECT SUM(A.INTEREST_AMOUNT) FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.WARRANT_DATE>='"+FromDate+"' AND A.WARRANT_DATE<='"+ToDate+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                double TDSAmount = data.getDoubleValueFromDB("SELECT SUM(A.TDS_AMOUNT) FROM D_FD_INT_CALC_DETAIL A,  D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.WARRANT_DATE>='"+FromDate+"' AND A.WARRANT_DATE<='"+ToDate+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                if(InterestAmount!=0) {
                    if(!PostVoucher24(CompanyID, ReceiptNo, InterestAmount,TDSAmount)) {
                        System.out.println(" Voucher for Receipt No.= "+ReceiptNo+" with total interest Amount = "+InterestAmount+" not posted properly.");
                    } else {
                        Counter++;
                    }
                }
                //}
                rsInterest.next();
            }
            ShowMessage(Counter + " Voucher(s) has been posted. Done...");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean PostVoucher24(int CompanyID, String ReceiptNo, double InterestAmount,double TDSAmount) {
        
        try {
            String MaturityDate ="";
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo); 
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            int Fin_Hierarchy_ID = 0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                Fin_Hierarchy_ID = HierarchyID;
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            
            //====== Gethering Data ======//
            int VoucherSrNo=0;
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            //String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = "";
            if(DepositerCategory == 2) {
                List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "SHAREHOLDER_ACCOUNT_CODE", "");
            } else {
                List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "PUBLIC_ACCOUNT_CODE", "");
            }
            if(List.size()>0) {
                //Get cumulative interest payable account.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                MainAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String IntMainAccountCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            //====== End of Gethering Data ======//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL); // Payment
            objVoucher.setAttribute("CHEQUE_NO",""); // Blank
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00"); // Blank
            objVoucher.setAttribute("BANK_NAME",BankName); // Book Name
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(VoucherDate));// End Date of Financial year
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","CD INTEREST YEARLY FROM " + txtFromDate.getText().trim() + " - " + txtToDate.getText().trim());
            
            objVoucher.setAttribute("HIERARCHY_ID",Fin_Hierarchy_ID);
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+Fin_Hierarchy_ID+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            //=======End of  Voucher Header ============//
            
            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();
            
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",IntMainAccountCode); //437158
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","CD INTEREST YEARLY FROM " + txtFromDate.getText().trim() + " - " + txtToDate.getText().trim());
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); //CD INTEREST LEDGER
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode); //PARTY CODE
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("REMARKS","CD INTEREST YEARLY FROM " + txtFromDate.getText().trim() + " - " + txtToDate.getText().trim());
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo); // Receipt No.
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate)); // Receipt Date.
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(TDSAmount > 0.0 ) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); //Int Main Account Code
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","TDS FROM CD INTEREST YEARLY FOR " + txtFromDate.getText().trim() + " - " + txtToDate.getText().trim());
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("REMARKS","TDS FROM CD INTEREST YEARLY FOR " + txtFromDate.getText().trim() + " - " + txtToDate.getText().trim());
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void clearFields() {
        txtFromDate.setText("");
        txtToDate.setText("");
    }
}
