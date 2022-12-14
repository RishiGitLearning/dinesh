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


public class frmPartyGroupingFind extends javax.swing.JApplet {
    
    public boolean Cancelled=false;
    public String strQuery;
    public String subQuery;
    private EITLComboModel cmbTypeModel;
    
    /** Initializes the applet frmGRNFind */
    public void init() {
        System.gc();
        setSize(730,300);
        initComponents();
        GenerateCombos();
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
        lblGroupMainParty = new javax.swing.JLabel();
        txtGroupMainParty = new javax.swing.JTextField();
        lblGroupSubParty = new javax.swing.JLabel();
        txtGroupMainPartyName = new javax.swing.JTextField();
        txtGroupSubParty = new javax.swing.JTextField();
        txtGroupSubPartyName = new javax.swing.JTextField();
        lblSelectType = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox();

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
        cmdFind.setBounds(570, 40, 70, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(570, 70, 70, 25);

        cmdClear.setText("Clear");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtGroupMainParty);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(570, 110, 70, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        lblGroupMainParty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGroupMainParty.setLabelFor(txtGroupMainParty);
        lblGroupMainParty.setText("Group Main Party :");
        jPanel1.add(lblGroupMainParty);
        lblGroupMainParty.setBounds(5, 40, 125, 15);

        txtGroupMainParty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupMainPartyFocusLost(evt);
            }
        });
        txtGroupMainParty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupMainPartyKeyPressed(evt);
            }
        });

        jPanel1.add(txtGroupMainParty);
        txtGroupMainParty.setBounds(135, 40, 110, 19);

        lblGroupSubParty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGroupSubParty.setText("Group Sub Party :");
        jPanel1.add(lblGroupSubParty);
        lblGroupSubParty.setBounds(5, 70, 125, 15);

        txtGroupMainPartyName.setEditable(false);
        txtGroupMainPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupMainPartyNameFocusLost(evt);
            }
        });
        txtGroupMainPartyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupMainPartyNameKeyPressed(evt);
            }
        });

        jPanel1.add(txtGroupMainPartyName);
        txtGroupMainPartyName.setBounds(255, 40, 290, 19);

        txtGroupSubParty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupSubPartyFocusLost(evt);
            }
        });
        txtGroupSubParty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupSubPartyKeyPressed(evt);
            }
        });

        jPanel1.add(txtGroupSubParty);
        txtGroupSubParty.setBounds(135, 70, 110, 19);

        txtGroupSubPartyName.setEditable(false);
        jPanel1.add(txtGroupSubPartyName);
        txtGroupSubPartyName.setBounds(255, 70, 290, 19);

        lblSelectType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectType.setText("Select Type :");
        jPanel1.add(lblSelectType);
        lblSelectType.setBounds(5, 10, 120, 15);

        jPanel1.add(cmbType);
        cmbType.setBounds(130, 10, 180, 24);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(6, 39, 550, 100);

    }//GEN-END:initComponents
    
    private void txtGroupSubPartyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupSubPartyKeyPressed
        // TODO add your handling code here:
        try {
            
            //            if(evt.getKeyCode()==112) {
            //                LOV aList=new LOV();
            //
            //                aList.SQL="SELECT PARTY_CODE,PARTY_NAME "+
            //                " FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_CODE ";
            //                aList.ReturnCol=1;
            //                aList.ShowReturnCol=true;
            //                aList.DefaultSearchOn=2;
            //                aList.UseSpecifiedConn=true;
            //                aList.dbURL=FinanceGlobal.FinURL;
            //
            //                if(aList.ShowLOV()) {
            //                    txtChildPartyID.setText(aList.ReturnVal);
            //                    txtChildPartyName.setText(clsPolicyMaster.getPartyName(EITLERPGLOBAL.gCompanyID, txtChildPartyID.getText()));
            //                }
            //
            //            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtGroupSubPartyKeyPressed
    
    private void txtGroupSubPartyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupSubPartyFocusLost
        // TODO add your handling code here:
        if (! txtGroupSubParty.getText().trim().equals("")) {
            txtGroupSubPartyName.setText(clsAccount.getAccountName(EITLERPGLOBAL.getCombostrCode(cmbType), txtGroupSubParty.getText().trim()));
        }
    }//GEN-LAST:event_txtGroupSubPartyFocusLost
    
    private void txtGroupMainPartyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupMainPartyFocusLost
        // TODO add your handling code here:
        if (! txtGroupMainParty.getText().trim().equals("")) {
            txtGroupMainPartyName.setText(clsAccount.getAccountName(EITLERPGLOBAL.getCombostrCode(cmbType),txtGroupMainParty.getText().trim()));
        }
    }//GEN-LAST:event_txtGroupMainPartyFocusLost
    
    private void txtGroupMainPartyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupMainPartyKeyPressed
        // TODO add your handling code here:
        try {
            
            //            if(evt.getKeyCode()==112) {
            //                LOV aList=new LOV();
            //
            //                aList.SQL="SELECT PARTY_CODE,PARTY_NAME "+
            //                " FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_CODE ";
            //                aList.ReturnCol=1;
            //                aList.ShowReturnCol=true;
            //                aList.DefaultSearchOn=2;
            //                aList.UseSpecifiedConn=true;
            //                aList.dbURL=FinanceGlobal.FinURL;
            //
            //                if(aList.ShowLOV()) {
            //                    txtParentPartyID.setText(aList.ReturnVal);
            //                    txtParentPartyName.setText(clsPolicyMaster.getPolicyName(EITLERPGLOBAL.gCompanyID, txtParentPartyID.getText()));
            //                }
            //
            //            }
            
        }
        catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtGroupMainPartyKeyPressed
    
    private void txtGroupMainPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupMainPartyNameFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtGroupMainPartyNameFocusLost
    
    private void txtGroupMainPartyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupMainPartyNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupMainPartyNameKeyPressed
    
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
        
        strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        subQuery="";
        if(!txtGroupMainParty.getText().equals("")) {
            strQuery=strQuery+" AND GROUP_MAIN_PARTY='"+txtGroupMainParty.getText()+"' ";
        }
        
        //        if(!txtFromDate.getText().equals("")) {
        //            strQuery=strQuery+" AND FROM_DATE >='"+EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' ";
        //        }
        //
        //        if(!txtToDate.getText().equals("")) {
        //            strQuery=strQuery+" AND TO_DATE <='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
        //        }
        
        //FOR THE SUB QUERY TO FIND IN DETAIL
        if(!txtGroupSubParty.getText().equals("")) {
            subQuery +=" AND GROUP_SUB_PARTY ='" + txtGroupSubParty.getText().trim() + " '";
        }
        if(!subQuery.equals("")) {
            subQuery=" AND GROUP_MAIN_PARTY IN ( SELECT GROUP_MAIN_PARTY FROM D_FIN_PARTY_GROUPING_DETAIL WHERE COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " " + subQuery + " ) ";
        }
        //subQuery=" AND DOC_NO ( SELECT DOC_NO FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL WHERE ) ";// <='"+EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"' ";
        
        
        strQuery=strQuery + subQuery;
        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbType;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblGroupMainParty;
    private javax.swing.JLabel lblGroupSubParty;
    private javax.swing.JLabel lblSelectType;
    private javax.swing.JTextField txtGroupMainParty;
    private javax.swing.JTextField txtGroupMainPartyName;
    private javax.swing.JTextField txtGroupSubParty;
    private javax.swing.JTextField txtGroupSubPartyName;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        
        cmbTypeModel=new EITLComboModel();
        cmbType.removeAllItems();
        cmbType.setModel(cmbTypeModel);
        
        ComboData aData = new ComboData();
        aData.Code=0;
        aData.strCode="";
        aData.Text="Select Type";
        cmbTypeModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code=1;
        aData.strCode="210027";
        aData.Text="Suiting";
        cmbTypeModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code=2;
        aData.strCode="210010";
        aData.Text="Felt";
        cmbTypeModel.addElement(aData);
        
        aData = new ComboData();
        aData.Code=3;
        aData.strCode="210072";
        aData.Text="Filter";
        cmbTypeModel.addElement(aData);
    }
    
}
