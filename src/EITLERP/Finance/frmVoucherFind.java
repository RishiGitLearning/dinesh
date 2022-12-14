/*
 * frmGRNFind.java
 *
 * Created on May 15, 2004, 1:35 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  nrpithva
 */  
 
/*<APPLET CODE=frmGRNFind.class HEIGHT=405 WIDTH=565></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
 

public class frmVoucherFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;

    public boolean Cancelled=false;
    public String strQuery;
    public int VoucherType=1;

    private EITLComboModel cmbForDeptModel;
    private EITLComboModel cmbBuyerModel;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(565,420);
        initComponents();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtVoucherNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        txtPartyName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtBookCode = new javax.swing.JTextField();
        txtBookName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtMainCode = new javax.swing.JTextField();
        txtMainName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtGRNNo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPONo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        lblLegacyNo = new javax.swing.JLabel();
        txtLegacyNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Records");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 12, 170, 15);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(482, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(482, 70, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtVoucherNo);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(482, 110, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setLabelFor(txtVoucherNo);
        jLabel2.setText("Voucher No. :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(5, 16, 90, 15);

        txtVoucherNo.setNextFocusableComponent(txtFromDate);
        jPanel1.add(txtVoucherNo);
        txtVoucherNo.setBounds(100, 14, 132, 19);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date  From :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(5, 54, 90, 15);

        txtFromDate.setNextFocusableComponent(txtToDate);
        jPanel1.add(txtFromDate);
        txtFromDate.setBounds(100, 50, 130, 19);

        jLabel4.setText(" To :");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(236, 54, 34, 15);

        jPanel1.add(txtToDate);
        txtToDate.setBounds(282, 50, 130, 19);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Party Code :");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(5, 167, 90, 15);

        txtPartyCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPartyCodeFocusLost(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(100, 166, 121, 19);

        txtPartyName.setEditable(false);
        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(100, 187, 309, 19);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Book Code :");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(5, 89, 90, 15);

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

        jPanel1.add(txtBookCode);
        txtBookCode.setBounds(100, 88, 121, 19);

        txtBookName.setEditable(false);
        jPanel1.add(txtBookName);
        txtBookName.setBounds(100, 109, 309, 19);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Main Code :");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(5, 216, 90, 15);

        txtMainCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMainCodeFocusLost(evt);
            }
        });
        txtMainCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMainCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtMainCode);
        txtMainCode.setBounds(100, 215, 121, 19);

        txtMainName.setEditable(false);
        jPanel1.add(txtMainName);
        txtMainName.setBounds(100, 236, 309, 19);

        jLabel5.setDisplayedMnemonic('G');
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setLabelFor(txtVoucherNo);
        jLabel5.setText("GRN No. :");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(5, 272, 90, 15);

        txtGRNNo.setNextFocusableComponent(txtFromDate);
        jPanel1.add(txtGRNNo);
        txtGRNNo.setBounds(100, 269, 132, 19);

        jLabel6.setDisplayedMnemonic('G');
        jLabel6.setLabelFor(txtVoucherNo);
        jLabel6.setText("PO No. :");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(243, 272, 60, 15);

        txtPONo.setNextFocusableComponent(txtFromDate);
        jPanel1.add(txtPONo);
        txtPONo.setBounds(311, 269, 132, 19);

        jLabel7.setDisplayedMnemonic('G');
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setLabelFor(txtVoucherNo);
        jLabel7.setText("Invoice No. :");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(5, 301, 90, 15);

        txtInvoiceNo.setNextFocusableComponent(txtFromDate);
        jPanel1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(100, 298, 132, 19);

        lblLegacyNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLegacyNo.setText("Legacy No. :");
        jPanel1.add(lblLegacyNo);
        lblLegacyNo.setBounds(5, 140, 90, 15);

        jPanel1.add(txtLegacyNo);
        txtLegacyNo.setBounds(100, 140, 80, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 458, 340);

    }//GEN-END:initComponents

    private void txtMainCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainCodeFocusLost
        // TODO add your handling code here:
        txtMainName.setText(clsAccount.getAccountName(txtMainCode.getText(),""));
    }//GEN-LAST:event_txtMainCodeFocusLost

    private void txtMainCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMainCodeKeyPressed
        // TODO add your handling code here:
        try
        {
            if(evt.getKeyCode()==112)
            {
            
                         LOV aList=new LOV();
                        
                        aList.SQL="SELECT MAIN_ACCOUNT_CODE,ACCOUNT_NAME FROM D_FIN_GL WHERE COMPANY_ID=2 AND APPROVED=1 ORDER BY ACCOUNT_NAME";
                        aList.ReturnCol=1;
                        aList.ShowReturnCol=true;
                        aList.DefaultSearchOn=2;
                        aList.UseSpecifiedConn=true;
                        aList.dbURL=FinanceGlobal.FinURL;
                        
                        if(aList.ShowLOV()) {
                            txtMainCode.setText(aList.ReturnVal);
                            txtMainName.setText(clsAccount.getAccountName(txtMainCode.getText(),""));
                        }
            }
        }
        catch(Exception e)
        {
            
        }
    }//GEN-LAST:event_txtMainCodeKeyPressed

    private void txtBookCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBookCodeFocusLost
        // TODO add your handling code here:
        try {
            txtBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_txtBookCodeFocusLost

    private void txtBookCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBookCodeKeyPressed
        // TODO add your handling code here:
        try {
            
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT BOOK_CODE,BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE COMPANY_ID=2 ORDER BY BOOK_NAME";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtBookCode.setText(aList.ReturnVal);
                    txtBookName.setText(clsBook.getBookName(EITLERPGLOBAL.gCompanyID, txtBookCode.getText()));
                }
                
            }
            
        }
        catch(Exception e) {
            
        }
        
    }//GEN-LAST:event_txtBookCodeKeyPressed

    private void txtPartyCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPartyCodeFocusLost
        // TODO add your handling code here:
    txtPartyName.setText(clsPartyMaster.getAccountName(txtMainCode.getText(),txtPartyCode.getText()));        
    }//GEN-LAST:event_txtPartyCodeFocusLost

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
try
{
    if(evt.getKeyCode()==112)
    {
    
                            LOV aList=new LOV();
                        
                        aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE COMPANY_ID=2 AND APPROVED=1 ORDER BY PARTY_NAME";
                        aList.ReturnCol=1;
                        aList.ShowReturnCol=true;
                        aList.DefaultSearchOn=2;
                        aList.UseSpecifiedConn=true;
                        aList.dbURL=FinanceGlobal.FinURL;
                        
                        if(aList.ShowLOV()) {
                            txtPartyCode.setText(aList.ReturnVal);
                            txtPartyName.setText(clsPartyMaster.getAccountName(txtMainCode.getText(),txtPartyCode.getText()));
                        }
    }
}
catch(Exception e)
{
    
}
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
        Cancelled=false;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdClearActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        strQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        // TODO add your handling code here:
       Cancelled=false;
       String subQuery="";
       
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+"  ";
       
       if(!txtVoucherNo.getText().equals(""))
       {
         strQuery=strQuery+" AND VOUCHER_NO LIKE '"+txtVoucherNo.getText()+"' ";
       }
       
       if(!txtFromDate.getText().equals(""))
       {
         strQuery=strQuery+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
       }

       if(!txtToDate.getText().equals(""))
       {
         strQuery=strQuery+" AND VOUCHER_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
       }
       
       if(!txtBookCode.getText().equals(""))
       {
         strQuery=strQuery+" AND BOOK_CODE='"+txtBookCode.getText()+"' ";
       }

       if(!txtLegacyNo.getText().equals(""))
       {
         strQuery=strQuery+" AND LEGACY_NO='"+txtLegacyNo.getText().trim()+"' ";
       }
       
       //====== Sub Query =======//
       if(!txtMainCode.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND MAIN_ACCOUNT_CODE='"+txtMainCode.getText()+"' ";
       }
       
       if(!txtPartyCode.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND SUB_ACCOUNT_CODE='"+txtPartyCode.getText()+"' ";
       }
       
       
       if(!txtGRNNo.getText().equals(""))
       {
         subQuery=subQuery+" AND GRN_NO='"+txtGRNNo.getText()+"' ";
       }

       if(!txtPONo.getText().equals(""))
       {
         subQuery=subQuery+" AND PO_NO='"+txtPONo.getText()+"' ";
       }
       
       if(!txtInvoiceNo.getText().equals(""))
       {
         subQuery=subQuery+" AND INVOICE_NO='"+txtInvoiceNo.getText()+"' ";
       }

       
       if(!subQuery.trim().equals(""))
       {
        subQuery=" AND VOUCHER_NO IN (SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+subQuery+")";
       }
       
       strQuery=strQuery+subQuery;
       //========== End Sub Query ============//
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLegacyNo;
    private javax.swing.JTextField txtBookCode;
    private javax.swing.JTextField txtBookName;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtGRNNo;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtLegacyNo;
    private javax.swing.JTextField txtMainCode;
    private javax.swing.JTextField txtMainName;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtVoucherNo;
    // End of variables declaration//GEN-END:variables
    


}
