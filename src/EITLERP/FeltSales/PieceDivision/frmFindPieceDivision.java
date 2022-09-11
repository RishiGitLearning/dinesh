/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.FeltSales.PieceDivision;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.LOV;
import EITLERP.clsSales_Party;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.MaskFormatter;


public class frmFindPieceDivision extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(390,210);
        initComponents();
         try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(S_O_DATE);
        } catch (ParseException ex) {
            System.out.println("Error on Mask : "+ex.getLocalizedMessage());
        }
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmdFind = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtDocNo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        S_O_DATE = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        PARTY_CODE = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find :: Felt Piece Division");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 190, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setMargin(new java.awt.Insets(0, 14, 0, 14));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        getContentPane().add(cmdFind);
        cmdFind.setBounds(310, 80, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(310, 110, 70, 25);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setLayout(null);
        jPanel1.add(txtDocNo);
        txtDocNo.setBounds(110, 10, 160, 27);

        jLabel3.setDisplayedMnemonic('G');
        jLabel3.setText("Document No");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 130, 17);
        jPanel1.add(S_O_DATE);
        S_O_DATE.setBounds(110, 40, 160, 30);

        jLabel2.setText("S.O.Date");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 47, 80, 20);

        PARTY_CODE.setToolTipText("Press F1 key for search Party Code");
        PARTY_CODE.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PARTY_CODEFocusLost(evt);
            }
        });
        PARTY_CODE.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                PARTY_CODEKeyTyped(evt);
            }
        });
        jPanel1.add(PARTY_CODE);
        PARTY_CODE.setBounds(110, 77, 160, 30);

        jLabel9.setText("Party Code");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 80, 100, 30);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 290, 120);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
        
        String docNo = txtDocNo.getText().trim();
        boolean flag=false;
        if(!docNo.equals("")) {
            stringFindQuery=" S_ORDER_NO ='"+ docNo +"'";
            flag=true;
        }
        boolean flag2=false;
        if(EITLERPGLOBAL.formatDateDB(S_O_DATE.getText()) != null)
        {
            if(flag)
            {
                stringFindQuery = stringFindQuery+" AND S_ORDER_DATE = "+EITLERPGLOBAL.formatDateDB(S_O_DATE.getText());
            }
            else
            {
                stringFindQuery = " S_ORDER_DATE ='"+EITLERPGLOBAL.formatDateDB(S_O_DATE.getText())+"'";
            }
            flag2 = true;
        }
        if(!PARTY_CODE.getText().trim().equals(""))
        {
            if(flag || flag2)
            {
                stringFindQuery = stringFindQuery+" AND PARTY_CODE = "+PARTY_CODE.getText();
            }
            else
            {
                stringFindQuery = " PARTY_CODE ='"+PARTY_CODE.getText()+"'";
            }
        }
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed

    private void PARTY_CODEFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusGained
        // TODO add your handling code here:
      
    }//GEN-LAST:event_PARTY_CODEFocusGained

    private void PARTY_CODEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PARTY_CODEFocusLost
        
    }//GEN-LAST:event_PARTY_CODEFocusLost

    private void PARTY_CODEKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyPressed


        
        if (evt.getKeyCode() == 112) {
            try {
                LOV aList = new LOV();

                aList.SQL = "SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER  WHERE MAIN_ACCOUNT_CODE='210010'";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 2;
                aList.UseSpecifiedConn = true;
                aList.dbURL = EITLERPGLOBAL.DatabaseURL;

                if (aList.ShowLOV()) {
                    PARTY_CODE.setText(aList.ReturnVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_PARTY_CODEKeyPressed

    private void PARTY_CODEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PARTY_CODEKeyTyped

    }//GEN-LAST:event_PARTY_CODEKeyTyped
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField PARTY_CODE;
    private javax.swing.JFormattedTextField S_O_DATE;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDocNo;
    // End of variables declaration//GEN-END:variables
    
}
