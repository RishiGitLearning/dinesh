/*
 * frmItemFind.java
 *
 * Created on April 28, 2004, 11:04 AM
 */

package EITLERP;

/*<APPLET CODE=frmItemFind.class HEIGHT=280 WIDTH=460></APPLET>*/
/**
 *
 * @author  nrpithva
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class frmEmpFind extends javax.swing.JApplet {
    
    private EITLComboModel cmbTypeModel;
    private EITLComboModel cmbCategoryModel;
    private EITLComboModel cmbWareHouseModel;
    private EITLComboModel cmbLocationModel;
    private EITLComboModel cmbUnitModel;
    
    public boolean Cancelled=false;
    public String strQuery;
    
    private String SelWareHouseID="";
    private EITLComboModel cmbDesignationModel;
    
    /** Initializes the applet frmItemFind */
    public void init() {
        System.gc();
        setSize(512,200);
        initComponents();
        GenerateCombo();
        cmbDesignation.setSelectedIndex(-1);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEmpID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cmbDesignation = new javax.swing.JComboBox();
        cmdCancel = new javax.swing.JButton();
        cmdFind = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtDesignationName = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Employee");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(12, 9, 112, 15);

        jLabel2.setText("Employee ID");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(33, 40, 89, 15);

        txtEmpID.setNextFocusableComponent(txtName);
        getContentPane().add(txtEmpID);
        txtEmpID.setBounds(126, 37, 110, 22);

        jLabel3.setText("Name");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(77, 70, 35, 15);

        txtName.setNextFocusableComponent(cmbDesignation);
        getContentPane().add(txtName);
        txtName.setBounds(125, 66, 262, 22);

        jLabel9.setText("Designation Code");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(5, 107, 120, 15);

        cmbDesignation.setEditable(true);
        cmbDesignation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDesignationItemStateChanged(evt);
            }
        });

        getContentPane().add(cmbDesignation);
        cmbDesignation.setBounds(124, 101, 261, 22);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(404, 50, 70, 25);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(404, 20, 70, 25);

        cmdClear.setText("Clear ");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.setNextFocusableComponent(txtEmpID);
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(406, 94, 70, 25);

        jLabel4.setText("Designation Name");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(7, 140, 120, 15);

        txtDesignationName.setNextFocusableComponent(cmbDesignation);
        getContentPane().add(txtDesignationName);
        txtDesignationName.setBounds(126, 136, 262, 22);

    }//GEN-END:initComponents

    private void cmbDesignationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDesignationItemStateChanged
        // TODO add your handling code here:
      
    }//GEN-LAST:event_cmbDesignationItemStateChanged

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        Cancelled=false;
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID;
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
        
        strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
        
        if(!txtEmpID.getText().equals("")) {
            strQuery=strQuery+" AND EMPLOYEE_ID LIKE '"+txtEmpID.getText()+"%' ";
        }
        
        if(!txtName.getText().equals("")) {
            strQuery=strQuery+" AND EMPLOYEE_NAME LIKE '%"+txtName.getText()+"%' ";
        }
        
        if(cmbDesignation.getSelectedIndex()!=-1) {
            strQuery=strQuery+" AND DESIGNATION_ID='"+EITLERPGLOBAL.getCombostrCode(cmbDesignation) +"' ";
        }
        
        if(!txtDesignationName.getText().trim().equals(""))
        {
           strQuery=strQuery+" AND DESIGNATION_ID IN (SELECT DESIGNATION_ID FROM D_COM_DESIGNATION_MASTER WHERE DESIGNATION_NAME LIKE '"+txtDesignationName.getText().trim()+"%') "; 
        }
        
        
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDesignation;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtDesignationName;
    private javax.swing.JTextField txtEmpID;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbType ------- //
        cmbDesignationModel=new EITLComboModel();
        cmbDesignation.removeAllItems();
        cmbDesignation.setModel(cmbDesignationModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        
        List=clsDesignation.getList(strCondition);
        
        for(int i=1;i<=List.size();i++) {
            clsDesignation ObjDes=(clsDesignation) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=0;
            aData.Text=(String)ObjDes.getAttribute("DESIGNATION_NAME").getObj();
            aData.strCode=(String) ObjDes.getAttribute("DESIGNATION_ID").getObj();
            cmbDesignationModel.addElement(aData);
        }
        //------------------------------ //
        
        txtDesignationName.setText("");
    }
    
}
