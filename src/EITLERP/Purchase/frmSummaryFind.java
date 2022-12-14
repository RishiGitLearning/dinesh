/*
 * frmQuoteFind.java
 *
 * Created on June 04, 2004, 9:37 AM
 */

package EITLERP.Purchase;

/**  
 * 
 * @author  jadave
 */
import javax.swing.*; 
import java.awt.*;
import java.util.*;
import EITLERP.*; 

/*<APPLET CODE=frmQuoteFind.class HEIGHT=382 WIDTH=560></APPLET>*/

public class frmSummaryFind extends javax.swing.JApplet {
    
     private EITLComboModel cmbStatusModel;
     public boolean Cancelled=false;
     public String strQuery;
    
    /** Creates new form frmTemplate */
    public frmSummaryFind() 
    {
         setSize(600,240);
         System.gc();
         initComponents();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDateFrom = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDateTo = new javax.swing.JTextField();
        txtItemID = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtInquiryNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        
        getContentPane().setLayout(null);
        
        jPanel1.setLayout(null);
        
        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setText("Quote No.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 16, 64, 15);
        
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(98, 14, 132, 19);
        
        jLabel3.setText("Date  From");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 50, 70, 15);
        
        jPanel1.add(txtDateFrom);
        txtDateFrom.setBounds(98, 50, 130, 19);
        
        jLabel4.setText(" To");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(248, 50, 20, 15);
        
        jPanel1.add(txtDateTo);
        txtDateTo.setBounds(282, 50, 130, 19);
        
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
        txtItemID.setBounds(100, 112, 127, 19);
        
        jLabel11.setText("Item");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(35, 112, 52, 15);
        
        txtItemName.setEditable(false);
        jPanel1.add(txtItemName);
        txtItemName.setBounds(101, 133, 309, 19);
        
        jLabel5.setText("Inquiry No.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(19, 83, 76, 15);
        
        jPanel1.add(txtInquiryNo);
        txtInquiryNo.setBounds(99, 81, 132, 19);
        
        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 38, 458, 165);
        
        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Records");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(6, 12, 170, 15);
        
        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdFind);
        cmdFind.setBounds(482, 40, 70, 25);
        
        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(482, 70, 70, 25);
        
        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdClear);
        cmdClear.setBounds(482, 110, 70, 25);
        
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

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
         strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND APPROVAL_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'";
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
      
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);  //+" AND QUOT_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND QUOT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ";
       
       if(!txtDocNo.getText().equals(""))
       {
         strQuery=strQuery+" AND APPROVAL_NO='"+txtDocNo.getText()+"' ";
       }
       
       if(!txtDateFrom.getText().equals(""))
       {
         strQuery=strQuery+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.formatDateDB(txtDateFrom.getText())+"' ";
       }

       if(!txtDateTo.getText().equals(""))
       {
         strQuery=strQuery+" AND APPROVAL_DATE<='"+EITLERPGLOBAL.formatDateDB(txtDateTo.getText())+"' ";
       }
       
       if(!txtItemID.getText().trim().equals(""))
       {
         strQuery=strQuery+" AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_CODE='"+txtItemID.getText().trim()+"') ";
       }
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDateFrom;
    private javax.swing.JTextField txtDateTo;
    private javax.swing.JTextField txtDocNo;
    private javax.swing.JTextField txtInquiryNo;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    // End of variables declaration//GEN-END:variables
    
    
    
}
