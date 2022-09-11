/*
 * frmFindFeltProductionWeaving.java
 * This form is used for searching  the details of Felt Production Weaving
 * 
 * Created on March 14, 2013, 1:09 PM
 */

package EITLERP.Production.FeltMachineSurvey;
/**
 *
 * @author  Vivek Kumar
 */
/*<APPLET CODE=frmGRNFind.class HEIGHT=405 WIDTH=565></APPLET>*/

import EITLERP.EITLERPGLOBAL;
import EITLERP.*;


public class frmmachinesurveyFindamend extends javax.swing.JApplet {
    
    public boolean Cancelled=true;
    public String stringFindQuery="";   
   
    public void init() {
        System.gc();
        setSize(355,169);
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
        txtPartyCode = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Felt Machine Survey Amendment");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 7, 250, 15);

        cmdFind.setMnemonic('F');
        cmdFind.setText("Find");
        cmdFind.setToolTipText("Find Proforma Details");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(20, 100, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(100, 100, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtPartyCode);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(180, 100, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setDisplayedMnemonic('G');
        jLabel2.setLabelFor(txtPartyCode);
        jLabel2.setText("Party Code");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 20, 80, 15);

        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        jPanel1.add(txtPartyCode);
        txtPartyCode.setBounds(110, 20, 130, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(10, 30, 247, 60);

    }//GEN-END:initComponents

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE IN(SELECT DISTINCT(MP_AMD_PARTY_CODE) FROM PRODUCTION.FELT_MACHINE_POSITION_AMEND )ORDER BY PARTY_NAME";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);                
                
            }
        }
    }//GEN-LAST:event_txtPartyCodeKeyPressed
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        stringFindQuery="";        
        txtPartyCode.setText("");

    }//GEN-LAST:event_cmdClearActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        stringFindQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        Cancelled=false;
        
      
        String partycode = txtPartyCode.getText();        
        
        
        if(!partycode.equals("")) {
            stringFindQuery+=" AA.MP_AMD_PARTY_CODE ='"+ partycode +"'";
        }  
        
        //System.out.println(stringFindQuery);
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtPartyCode;
    // End of variables declaration//GEN-END:variables
    
}
