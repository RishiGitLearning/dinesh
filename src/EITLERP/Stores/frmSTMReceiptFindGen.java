/*
 * frmGRNFind.java
 *
 * Created on May 15, 2004, 1:35 PM
 */

package EITLERP.Stores;
 
/**
 *
 * @author  nrpithva
 */
 
/*<APPLET CODE=frmGRNFind.class HEIGHT=405 WIDTH=565></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
 

public class frmSTMReceiptFindGen extends javax.swing.JApplet {
    
    private EITLComboModel cmbStatusModel;

    public boolean Cancelled=false;
    public String strQuery;
    public int GRNType=3;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(565,405);
        initComponents();
        GenerateCombos();
        cmbStatus.setSelectedIndex(-1);
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
        txtDateFrom = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtChalanNo = new javax.swing.JTextField();
        txtInvoiceNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtGatepassNo = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        chkGRNPending = new javax.swing.JCheckBox();
        chkImportConcess = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        txtDateTo = new javax.swing.JTextField();
        txtChalanDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtInvoiceDate = new javax.swing.JTextField();
        chkCenvated = new javax.swing.JCheckBox();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        txtItemID = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPONo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtMIRNo = new javax.swing.JTextField();

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
        cmdClear.setNextFocusableComponent(txtDocNo);
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
        jLabel2.setLabelFor(txtDocNo);
        jLabel2.setText("STM Receipt No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(4, 16, 90, 15);

        txtDocNo.setNextFocusableComponent(txtDateFrom);
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(98, 14, 132, 19);

        jLabel3.setText("Date  From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 54, 70, 15);

        txtDateFrom.setNextFocusableComponent(txtDateTo);
        jPanel1.add(txtDateFrom);
        txtDateFrom.setBounds(98, 50, 130, 19);

        jLabel5.setText("Chalan No.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(24, 86, 70, 15);

        txtChalanNo.setNextFocusableComponent(txtChalanDate);
        jPanel1.add(txtChalanNo);
        txtChalanNo.setBounds(98, 82, 132, 19);

        txtInvoiceNo.setNextFocusableComponent(txtInvoiceDate);
        jPanel1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(98, 116, 132, 19);

        jLabel7.setText("Invoice No.");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(24, 120, 70, 15);

        jLabel9.setText("Gatepass No.");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 158, 84, 15);

        txtGatepassNo.setNextFocusableComponent(cmbStatus);
        jPanel1.add(txtGatepassNo);
        txtGatepassNo.setBounds(98, 154, 132, 19);

        cmbStatus.setEditable(true);
        cmbStatus.setNextFocusableComponent(txtSuppCode);
        jPanel1.add(cmbStatus);
        cmbStatus.setBounds(98, 186, 94, 24);

        jLabel17.setText("Status");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(52, 190, 46, 15);

        chkGRNPending.setText("GRN Pending");
        chkGRNPending.setNextFocusableComponent(chkImportConcess);
        jPanel1.add(chkGRNPending);
        chkGRNPending.setBounds(98, 260, 172, 23);

        chkImportConcess.setText("Import Concessional");
        chkImportConcess.setNextFocusableComponent(chkCenvated);
        jPanel1.add(chkImportConcess);
        chkImportConcess.setBounds(98, 292, 166, 23);

        jLabel4.setText(" To");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(236, 54, 34, 15);

        txtDateTo.setNextFocusableComponent(txtChalanNo);
        jPanel1.add(txtDateTo);
        txtDateTo.setBounds(282, 50, 130, 19);

        txtChalanDate.setNextFocusableComponent(txtInvoiceNo);
        jPanel1.add(txtChalanDate);
        txtChalanDate.setBounds(282, 82, 132, 19);

        jLabel6.setText("Date ");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(240, 86, 36, 15);

        jLabel8.setText("Date ");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(240, 120, 32, 15);

        txtInvoiceDate.setNextFocusableComponent(txtGatepassNo);
        jPanel1.add(txtInvoiceDate);
        txtInvoiceDate.setBounds(282, 116, 132, 19);

        chkCenvated.setText("Cenvated Items");
        chkCenvated.setNextFocusableComponent(chkCancelled);
        jPanel1.add(chkCenvated);
        chkCenvated.setBounds(282, 260, 148, 23);

        chkCancelled.setText("Cancelled");
        chkCancelled.setNextFocusableComponent(cmdFind);
        jPanel1.add(chkCancelled);
        chkCancelled.setBounds(282, 290, 85, 23);

        jLabel10.setText("Supplier");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(38, 226, 52, 15);

        txtSuppCode.setNextFocusableComponent(chkGRNPending);
        txtSuppCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuppCodeFocusLost(evt);
            }
        });
        txtSuppCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSuppCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtSuppCode);
        txtSuppCode.setBounds(98, 222, 62, 19);

        txtSuppName.setEditable(false);
        jPanel1.add(txtSuppName);
        txtSuppName.setBounds(162, 222, 242, 19);

        txtItemID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtItemIDFocusLost(evt);
            }
        });
        txtItemID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemIDKeyPressed(evt);
            }
        });

        jPanel1.add(txtItemID);
        txtItemID.setBounds(102, 329, 121, 19);

        jLabel13.setText("Item");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(37, 329, 52, 15);

        txtItemName.setEditable(false);
        jPanel1.add(txtItemName);
        txtItemName.setBounds(103, 350, 309, 19);

        jLabel14.setText("PO No.");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(46, 388, 53, 15);

        jPanel1.add(txtPONo);
        txtPONo.setBounds(105, 386, 114, 19);

        jLabel19.setText("MIR No.");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(260, 387, 53, 15);

        jPanel1.add(txtMIRNo);
        txtMIRNo.setBounds(319, 385, 114, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 38, 458, 425);

    }//GEN-END:initComponents

    private void txtItemIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtItemIDFocusLost
        // TODO add your handling code here:
        if(!txtItemID.getText().trim().equals(""))
        {
          txtItemName.setText(clsItem.getItemName(EITLERPGLOBAL.gCompanyID,txtItemID.getText()));  
        }
        else
        {
          txtItemName.setText("");  
        }
        
    }//GEN-LAST:event_txtItemIDFocusLost

    private void txtItemIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemIDKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 ORDER BY ITEM_ID";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtItemID.setText(aList.ReturnVal);
                txtItemName.setText(clsItem.getItemName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        
    }//GEN-LAST:event_txtItemIDKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void txtSuppCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuppCodeFocusLost
        // TODO add your handling code here:
        if(!txtSuppCode.getText().trim().equals("")) {
            txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, txtSuppCode.getText()));
        }
    }//GEN-LAST:event_txtSuppCodeFocusLost

    private void txtSuppCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSuppCodeKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT SUPPLIER_CODE,SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ST35_REGISTERED=1 AND BLOCKED='N' AND APPROVED=1 ORDER BY SUPP_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtSuppCode.setText(aList.ReturnVal);
                txtSuppName.setText(clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
    }//GEN-LAST:event_txtSuppCodeKeyPressed

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_TYPE="+GRNType+" AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
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
       
       //strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GRN_TYPE=4 ";
       
       if(!txtDocNo.getText().equals(""))
       {
         strQuery=strQuery+" AND GRN_NO='"+txtDocNo.getText()+"' ";
       }
       
       if(!txtDateFrom.getText().equals(""))
       {
         strQuery=strQuery+" AND GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(txtDateFrom.getText())+"' ";
       }

       if(!txtDateTo.getText().equals(""))
       {
         strQuery=strQuery+" AND GRN_DATE<='"+EITLERPGLOBAL.formatDateDB(txtDateTo.getText())+"' ";
       }
       
       if(!txtChalanNo.getText().equals(""))
       {
         strQuery=strQuery+" AND CHALAN_NO='"+txtChalanNo.getText()+"' ";  
       }
       
       if(!txtChalanDate.getText().equals(""))
       {
         strQuery=strQuery+" AND CHALAN_DATE='"+EITLERPGLOBAL.formatDateDB(txtChalanDate.getText())+"' ";   
       }
       
       if(!txtInvoiceNo.getText().equals(""))
       {
         strQuery=strQuery+" AND INVOICE_NO='"+txtInvoiceNo.getText()+"' ";  
       }
       
       if(!txtInvoiceDate.getText().equals(""))
       {
         strQuery=strQuery+" AND INVOICE_DATE='"+EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText())+"' ";   
       }
       
       if(!txtGatepassNo.getText().equals(""))
       {
         strQuery=strQuery+" AND GATEPASS_NO='"+txtGatepassNo.getText()+"' ";  
       }
       
       if(cmbStatus.getSelectedIndex()!=-1)
       {
         strQuery=strQuery+" AND OPEN_STATUS='"+EITLERPGLOBAL.getCombostrCode(cmbStatus)+"' ";
       }
       
       if(chkGRNPending.isSelected())
       {
        strQuery=strQuery+" AND GRN_PENDING=1 ";
       }
       
       if(chkImportConcess.isSelected())
       {
        strQuery=strQuery+" AND IMPORT_CONCESS=1";
       }
       
       if(chkCenvated.isSelected())
       {
        strQuery=strQuery+" AND CENVATED_ITEMS=1";
       }
       
       if(chkCancelled.isSelected())
       {
        strQuery=strQuery+" AND CANCELLED=1";
       }
       
       if(!txtSuppCode.getText().trim().equals(""))
       {
        strQuery=strQuery+" AND SUPP_ID='"+txtSuppCode.getText()+"' ";
       }
       
       
       //====== Sub Query =======//
       if(!txtItemID.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND ITEM_ID='"+txtItemID.getText()+"' ";
       }
       
       if(!txtPONo.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND PO_NO='"+txtPONo.getText()+"' ";   
       }
       
       if(!txtMIRNo.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND MIR_NO='"+txtMIRNo.getText()+"' ";   
       }
       
       if(!subQuery.trim().equals(""))
       {
        subQuery=" AND GRN_NO IN (SELECT GRN_NO FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+subQuery+")";
       }
       
       
       strQuery=strQuery+subQuery;
       //========== End Sub Query ============//
       
       
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JCheckBox chkCenvated;
    private javax.swing.JCheckBox chkGRNPending;
    private javax.swing.JCheckBox chkImportConcess;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtChalanDate;
    private javax.swing.JTextField txtChalanNo;
    private javax.swing.JTextField txtDateFrom;
    private javax.swing.JTextField txtDateTo;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtGatepassNo;
    private javax.swing.JTextField txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtMIRNo;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    // End of variables declaration//GEN-END:variables
    

    private void GenerateCombos()
    {
        //--- Generate Type Combo ------//
        cmbStatusModel=new EITLComboModel();
        cmbStatus.removeAllItems();
        cmbStatus.setModel(cmbStatusModel);
        
        ComboData aData=new ComboData();
        aData.strCode="O";
        aData.Text="Open";
        cmbStatusModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="C";
        aData.Text="Close";
        cmbStatusModel.addElement(aData);
        //===============================//
    }
}
