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
 

public class frmExpenseFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;

    public boolean Cancelled=false;
    public String strQuery;
    public int GRNType=2;

    private EITLComboModel cmbForDeptModel;
    private EITLComboModel cmbBuyerModel;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(565,300);
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
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDocDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtExpenseID = new javax.swing.JTextField();
        txtExpenseName = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtInvoiceDate = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPartyName = new javax.swing.JTextField();

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
        cmdFind.setBounds(453, 39, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(453, 69, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(453, 109, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setText("Doc No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(45, 22, 60, 15);

        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(101, 20, 110, 19);

        jLabel3.setText("Date");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(229, 22, 60, 15);

        jPanel1.add(txtDocDate);
        txtDocDate.setBounds(285, 20, 110, 19);

        jLabel4.setText("Expense Code");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(11, 51, 90, 15);

        txtExpenseID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpenseIDFocusLost(evt);
            }
        });
        txtExpenseID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpenseIDKeyPressed(evt);
            }
        });

        jPanel1.add(txtExpenseID);
        txtExpenseID.setBounds(101, 49, 110, 19);

        txtExpenseName.setEditable(false);
        jPanel1.add(txtExpenseName);
        txtExpenseName.setBounds(102, 70, 310, 19);

        jLabel12.setText("Invoice No.");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(19, 97, 80, 15);

        jPanel1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(100, 95, 110, 19);

        jLabel13.setText("Date");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(240, 97, 40, 15);

        jPanel1.add(txtInvoiceDate);
        txtInvoiceDate.setBounds(285, 95, 110, 19);

        jLabel14.setText("Party Name");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(19, 127, 80, 15);

        jPanel1.add(txtPartyName);
        txtPartyName.setBounds(101, 125, 310, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 440, 160);

    }//GEN-END:initComponents

    private void txtExpenseIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpenseIDFocusLost
        // TODO add your handling code here:
        txtExpenseName.setText(clsExpense.getExpenseName(txtExpenseID.getText()));
    }//GEN-LAST:event_txtExpenseIDFocusLost

    private void txtExpenseIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpenseIDKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) {
            
            LOV aList=new LOV();
            
            aList.SQL="SELECT EXPENSE_ID,EXPENSE_DESCRIPTION FROM D_FIN_EXPENSE_MASTER WHERE APPROVED=1 ORDER BY EXPENSE_DESCRIPTION";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            aList.UseSpecifiedConn=true;
            aList.dbURL=FinanceGlobal.FinURL;
            
            if(aList.ShowLOV()) {
                txtExpenseID.setText(aList.ReturnVal);
                txtExpenseName.setText(clsExpense.getExpenseName(txtExpenseID.getText()));
            }
        }
        
    }//GEN-LAST:event_txtExpenseIDKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ";
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
       
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
       
       if(!txtDocNo.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_NO='"+txtDocNo.getText()+"' ";
       }
       
       if(!txtDocDate.getText().equals(""))
       {
         strQuery=strQuery+" AND DOC_DATE='"+EITLERPGLOBAL.formatDateDB(txtDocDate.getText())+"' ";
       }

       if(!txtExpenseID.getText().equals(""))
       {
         strQuery=strQuery+" AND EXPENSE_ID ='"+txtExpenseID.getText()+"' ";
       }
       
       if(!txtInvoiceNo.getText().equals(""))
       {
         strQuery=strQuery+" AND INVOICE_NO ='"+txtInvoiceNo.getText()+"' ";
       }
       
       if(!txtInvoiceDate.getText().equals(""))
       {
         strQuery=strQuery+" AND INVOICE_DATE ='"+EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText())+"' ";
       }
       
       if(!txtPartyName.getText().equals(""))
       {
         strQuery=strQuery+" AND PARTY_NAME LIKE '%"+txtPartyName.getText()+"%' ";
       }
      
       strQuery=strQuery;
       //========== End Sub Query ============//
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtExpenseID;
    private javax.swing.JTextField txtExpenseName;
    private javax.swing.JTextField txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtPartyName;
    // End of variables declaration//GEN-END:variables
    


}