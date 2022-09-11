/*
 * frmFindFeltRate.java
 * This form is used for searching  the details of Felt Rate Master and
 * Felt Rate Updation Modules 
 * Created on July 19, 2013, 5:17 PM
 */

package EITLERP.Production.WarpingPreparationOfWeavingLooms_MNE;
/**
 *
 * @author  Vivek Kumar
 */


import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.LOV;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Date;
import javax.swing.text.MaskFormatter;


public class frmFindWarpingPreparationOfWeavingLoom_MNE extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(390,210);
        initComponents();
         
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
        DocNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Find :: Felt Loom Wise Beam Fall Planning");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 310, 15);

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

        DocNo.setToolTipText("Press F1 key for search Party Code");
        DocNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                DocNoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                DocNoFocusLost(evt);
            }
        });
        DocNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                DocNoKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DocNoKeyPressed(evt);
            }
        });
        jPanel1.add(DocNo);
        DocNo.setBounds(120, 30, 160, 30);

        jLabel10.setText("Doc No");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(20, 30, 100, 30);

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
        
        stringFindQuery=" LPP_DOC_NO !='' ";
        
        if(!DocNo.getText().trim().equals(""))
        {
            String DOC_NO_LIST = "''";
            ResultSet rsData = data.getResult("SELECT LPP_DOC_NO FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER  WHERE LPP_DOC_NO='"+DocNo.getText()+"' ORDER BY LPP_DOC_NO DESC");    
            try{
                rsData.first();
                while(!rsData.isAfterLast())
                {
                   DOC_NO_LIST = DOC_NO_LIST + ",'"+rsData.getString("LPP_DOC_NO")+"'";                   
                   rsData.next();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            stringFindQuery = stringFindQuery + " AND LPP_DOC_NO IN ("+DOC_NO_LIST+")";
        }
        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed

    private void DocNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DocNoFocusGained
        // TODO add your handling code here:
      
    }//GEN-LAST:event_DocNoFocusGained

    private void DocNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_DocNoFocusLost
        
    }//GEN-LAST:event_DocNoFocusLost

    private void DocNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DocNoKeyPressed


        
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
                    DocNo.setText(aList.ReturnVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error = " + e.getMessage());
            }
        }
    }//GEN-LAST:event_DocNoKeyPressed

    private void DocNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DocNoKeyTyped

    }//GEN-LAST:event_DocNoKeyTyped
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DocNo;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
