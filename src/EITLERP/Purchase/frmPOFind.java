/*
 * frmPOFind.java
 *
 * Created on May 20, 2004, 2:30 PM
 */

package EITLERP.Purchase;

/** 
 *
 * @author  nrpithva
 */   

/*<APPLET CODE=frmPOFind HEIGHT=425 WIDTH=570></APPLET>*/
 
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;

public class frmPOFind extends javax.swing.JApplet {

    public boolean Cancelled=false;
    public String strQuery;

    private EITLComboModel cmbStatusModel;
    private EITLComboModel cmbBuyerModel;
    private EITLComboModel cmbShipModel;
    
    /** Initializes the applet frmPOFind */
    public void init() {
        System.gc();
        setSize(570,493);
        initComponents();
        GenerateCombos();
        cmbBuyer.setSelectedIndex(-1);
        cmbShip.setSelectedIndex(-1);
        cmbStatus.setSelectedIndex(-1);
        cmbStatus.setVisible(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtSuppCode = new javax.swing.JTextField();
        txtSuppName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDateFrom = new javax.swing.JTextField();
        txtDeliveryDate = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtQuotationNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtQuotationDate = new javax.swing.JTextField();
        txtInquiryDate = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtInquiryNo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbBuyer = new javax.swing.JComboBox();
        cmbShip = new javax.swing.JComboBox();
        cmbStatus = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtDateTo = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtQuotID = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtIndentNo = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

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

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Records");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(8, 8, 170, 15);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel4.setText("Supplier");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(54, 88, 52, 15);

        txtSuppCode.setNextFocusableComponent(txtDeliveryDate);
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
        txtSuppCode.setBounds(114, 84, 62, 19);

        txtSuppName.setEditable(false);
        jPanel1.add(txtSuppName);
        txtSuppName.setBounds(178, 84, 212, 19);

        jLabel2.setText("P.O. No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(52, 18, 56, 15);

        txtDocNo.setBackground(new java.awt.Color(204, 204, 255));
        txtDocNo.setNextFocusableComponent(txtDateFrom);
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(114, 14, 114, 19);

        jLabel3.setText("Date From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(38, 54, 68, 15);

        txtDateFrom.setNextFocusableComponent(txtDateTo);
        jPanel1.add(txtDateFrom);
        txtDateFrom.setBounds(114, 50, 100, 19);

        txtDeliveryDate.setNextFocusableComponent(txtQuotationNo);
        jPanel1.add(txtDeliveryDate);
        txtDeliveryDate.setBounds(114, 120, 114, 19);

        jLabel16.setText("Delivery Date");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(15, 122, 103, 15);

        jLabel9.setText("Quotation No.");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(14, 162, 93, 15);

        txtQuotationNo.setNextFocusableComponent(txtQuotationDate);
        jPanel1.add(txtQuotationNo);
        txtQuotationNo.setBounds(114, 158, 114, 19);

        jLabel10.setText("Date");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(254, 162, 29, 15);

        txtQuotationDate.setNextFocusableComponent(txtInquiryNo);
        jPanel1.add(txtQuotationDate);
        txtQuotationDate.setBounds(290, 160, 100, 19);

        txtInquiryDate.setNextFocusableComponent(cmbBuyer);
        jPanel1.add(txtInquiryDate);
        txtInquiryDate.setBounds(290, 192, 100, 19);

        jLabel15.setText("Date");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(254, 194, 29, 15);

        txtInquiryNo.setNextFocusableComponent(txtInquiryDate);
        jPanel1.add(txtInquiryNo);
        txtInquiryNo.setBounds(114, 190, 114, 19);

        jLabel12.setText("Inquiry No.");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(31, 192, 80, 15);

        jLabel11.setText("Buyer");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(64, 230, 42, 16);

        cmbBuyer.setEditable(true);
        cmbBuyer.setNextFocusableComponent(cmbStatus);
        jPanel1.add(cmbBuyer);
        cmbBuyer.setBounds(114, 226, 180, 24);

        cmbShip.setEditable(true);
        cmbShip.setNextFocusableComponent(chkCancelled);
        jPanel1.add(cmbShip);
        cmbShip.setBounds(114, 292, 254, 24);

        cmbStatus.setEditable(true);
        cmbStatus.setNextFocusableComponent(cmbShip);
        jPanel1.add(cmbStatus);
        cmbStatus.setBounds(114, 258, 94, 24);

        jLabel17.setText("Status");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(62, 262, 46, 15);

        chkCancelled.setText("Cancelled");
        chkCancelled.setNextFocusableComponent(cmdFind);
        jPanel1.add(chkCancelled);
        chkCancelled.setBounds(371, 5, 85, 23);

        jLabel5.setText("To");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(226, 54, 34, 15);

        txtDateTo.setNextFocusableComponent(txtSuppCode);
        jPanel1.add(txtDateTo);
        txtDateTo.setBounds(258, 50, 100, 19);

        jLabel18.setText("Shipping");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(48, 298, 56, 15);

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
        txtItemID.setBounds(115, 327, 121, 19);

        jLabel13.setText("Item");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(50, 327, 52, 15);

        txtItemName.setEditable(false);
        jPanel1.add(txtItemName);
        txtItemName.setBounds(116, 348, 309, 19);

        jLabel14.setText("Our Quot. No.");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(18, 388, 93, 15);

        txtQuotID.setNextFocusableComponent(txtQuotationDate);
        jPanel1.add(txtQuotID);
        txtQuotID.setBounds(118, 384, 114, 19);

        jLabel19.setText("Indent No.");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(249, 385, 80, 15);

        txtIndentNo.setNextFocusableComponent(txtInquiryDate);
        jPanel1.add(txtIndentNo);
        txtIndentNo.setBounds(332, 383, 114, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(4, 32, 462, 419);

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
            
            aList.SQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
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
        strQuery="";
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
       
       //strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
       
       if(!txtDocNo.getText().trim().equals(""))
       {
         strQuery=strQuery+" AND PO_NO='"+txtDocNo.getText()+"' ";
       }
       
       if(!txtDateFrom.getText().equals(""))
       {
         strQuery=strQuery+" AND PO_DATE>='"+EITLERPGLOBAL.formatDateDB(txtDateFrom.getText())+"' ";
       }

       if(!txtDateTo.getText().equals(""))
       {
         strQuery=strQuery+" AND PO_DATE<='"+EITLERPGLOBAL.formatDateDB(txtDateTo.getText())+"' ";
       }
       
       if(!txtSuppCode.getText().trim().equals(""))
       {
         strQuery=strQuery+" AND SUPP_ID='"+txtSuppCode.getText().trim()+"' ";  
       }
       
       if(!txtDeliveryDate.getText().equals(""))
       {
         strQuery=strQuery+" AND DELIVERY_DATE='"+EITLERPGLOBAL.formatDateDB(txtDeliveryDate.getText())+"' ";
       }
       
       if(!txtQuotationNo.getText().trim().equals(""))
       {
         strQuery=strQuery+" AND QUOTATION_NO='"+txtQuotationNo.getText()+"' ";       
       }
       
       if(!txtQuotationDate.getText().equals(""))
       {
         strQuery=strQuery+" AND QUOTATION_DATE='"+EITLERPGLOBAL.formatDateDB(txtQuotationDate.getText())+"' ";
       }
       
       if(!txtInquiryNo.getText().trim().equals(""))
       {
         strQuery=strQuery+" AND INQUIRY_NO='"+txtInquiryNo.getText()+"' ";       
       }
       
       if(!txtInquiryDate.getText().equals(""))
       {
         strQuery=strQuery+" AND INQUIRY_DATE='"+EITLERPGLOBAL.formatDateDB(txtInquiryDate.getText())+"' ";
       }
       
       if(cmbBuyer.getSelectedIndex()!=-1) 
       {
         strQuery=strQuery+" AND BUYER="+EITLERPGLOBAL.getComboCode(cmbBuyer)+" ";         
       }
       
       if(cmbStatus.getSelectedIndex()!=-1)
       {
         strQuery=strQuery+" AND STATUS='"+EITLERPGLOBAL.getCombostrCode(cmbStatus)+"' ";           
       }
       
       if(cmbShip.getSelectedIndex()!=-1)
       {
        strQuery=strQuery+" AND SHIP_ID="+EITLERPGLOBAL.getComboCode(cmbShip)+" ";              
       }
       
       if(chkCancelled.isSelected())
       {
        strQuery=strQuery+" AND CANCELLED=1 ";
       }

       //====== Sub Query =======//
       if(!txtItemID.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND ITEM_ID='"+txtItemID.getText()+"' ";
       }
       
       if(!txtQuotID.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND QUOT_ID='"+txtQuotID.getText()+"' ";   
       }
       
       if(!txtIndentNo.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND INDENT_NO='"+txtIndentNo.getText()+"' ";   
       }
       
       if(!subQuery.trim().equals(""))
       {
        subQuery=" AND PO_NO IN (SELECT PO_NO FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+subQuery+")";
       }
       
       
       strQuery=strQuery+subQuery;
       //========== End Sub Query ============//
       
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JComboBox cmbBuyer;
    private javax.swing.JComboBox cmbShip;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDateFrom;
    private javax.swing.JTextField txtDateTo;
    private javax.swing.JTextField txtDeliveryDate;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtIndentNo;
    private javax.swing.JTextField txtInquiryDate;
    private javax.swing.JTextField txtInquiryNo;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtQuotID;
    private javax.swing.JTextField txtQuotationDate;
    private javax.swing.JTextField txtQuotationNo;
    private javax.swing.JTextField txtSuppCode;
    private javax.swing.JTextField txtSuppName;
    // End of variables declaration//GEN-END:variables

private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        clsUser ObjUser=new clsUser();

       //-------- Generating Buyer Combo --------// 
        cmbBuyerModel=new EITLComboModel();
        cmbBuyer.removeAllItems();
        cmbBuyer.setModel(cmbBuyerModel);
        
       List=ObjUser.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID));
       for(int i=1;i<=List.size();i++)
        {
           ObjUser=(clsUser) List.get(Integer.toString(i));

           ComboData aData=new ComboData();

           aData.Text=(String) ObjUser.getAttribute("USER_NAME").getObj();
           aData.Code=(long)ObjUser.getAttribute("USER_ID").getVal();
           
           cmbBuyerModel.addElement(aData);
        }
       cmbBuyer.setSelectedIndex(-1);
       //----------------------------------------//
        

       //-------- Generating Buyer Combo --------// 
        cmbShipModel=new EITLComboModel();
        cmbShip.removeAllItems();
        cmbShip.setModel(cmbShipModel);
        
       List=clsShippingAddress.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID));
       for(int i=1;i<=List.size();i++)
        {
           clsShippingAddress ObjShip=(clsShippingAddress) List.get(Integer.toString(i));

           ComboData aData=new ComboData();

           aData.Text=Integer.toString((int)ObjShip.getAttribute("SHIP_ID").getVal())+" "+(String)ObjShip.getAttribute("CITY").getObj();
           aData.Code=(long)ObjShip.getAttribute("SHIP_ID").getVal();
           
           cmbShipModel.addElement(aData);
        }
       cmbShip.setSelectedIndex(-1);
       //----------------------------------------//
       
       
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
        cmbStatus.setSelectedIndex(-1);
        //===============================//
    }    
}
