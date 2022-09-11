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
 
public class frmHierarchyFind extends javax.swing.JApplet {
  
    
    private EITLComboModel cmbModuleModel;
    private EITLComboModel cmbUserModel;
    
    public boolean Cancelled=false;
    public String strQuery;

    private String SelWareHouseID="";    
    
    /** Initializes the applet frmItemFind */
    public void init() {
        System.gc();
        setSize(540 ,280);
        initComponents();
        GenerateCombo();
        cmbModule.setSelectedIndex(-1);
        cmbUser.setSelectedIndex(-1);
        
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox();
        cmbUser = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmdCancel = new javax.swing.JButton();
        cmdFind = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Find Hierarchy");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(2, 4, 120, 15);

        jLabel3.setText("Name");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(16, 55, 35, 15);

        txtName.setNextFocusableComponent(cmbModule);
        getContentPane().add(txtName);
        txtName.setBounds(62, 50, 265, 23);

        jLabel9.setText("Module");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(16, 108, 72, 15);

        cmbModule.setNextFocusableComponent(cmbUser);
        getContentPane().add(cmbModule);
        cmbModule.setBounds(16, 130, 310, 24);

        getContentPane().add(cmbUser);
        cmbUser.setBounds(16, 180, 310, 24);

        jLabel4.setText("Starts with User");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(16, 160, 120, 15);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.setNextFocusableComponent(cmdClear);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(440, 40, 70, 25);

        cmdFind.setText("Find");
        cmdFind.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdFind.setNextFocusableComponent(cmdCancel);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });

        getContentPane().add(cmdFind);
        cmdFind.setBounds(440, 10, 70, 25);

        cmdClear.setText("Clear ");
        cmdClear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });

        getContentPane().add(cmdClear);
        cmdClear.setBounds(440, 90, 70, 25);

    }//GEN-END:initComponents

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
       
       if(!txtName.getText().equals(""))
       {
           strQuery=strQuery+" AND HIERARCHY_NAME LIKE '"+txtName.getText()+"%' ";
       }
       
       
       if(cmbModule.getSelectedIndex()!=-1)
       {
          strQuery=strQuery+" AND MODULE_ID="+EITLERPGLOBAL.getComboCode(cmbModule) +" "; 
       }
       
       if(cmbUser.getSelectedIndex()!=-1)
       {
         strQuery=strQuery+" AND HIERARCHY_ID IN (SELECT HIERARCHY_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND USER_ID="+EITLERPGLOBAL.getComboCode(cmbUser)+" AND APPROVAL_SEQUENCE=1)";
       }
       
       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JComboBox cmbUser;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    private void GenerateCombo()
    {
   //Generates Combo Boxes
   HashMap List=new HashMap();
   String strCondition="";
   

   
        //----- Generate cmbType ------- //
        cmbModuleModel=new EITLComboModel();
        cmbModule.removeAllItems();
        cmbModule.setModel(cmbModuleModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) + " ORDER BY MODULE_DESC";
        
        List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbModuleModel.addElement(aData);
        }

        
        //-------- Generating Buyer Combo --------//
        cmbUserModel=new EITLComboModel();
        cmbUser.removeAllItems();
        cmbUser.setModel(cmbUserModel);
        
        clsUser ObjUser=new clsUser();
        
        List=ObjUser.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID));
        for(int i=1;i<=List.size();i++) {
            ObjUser=(clsUser) List.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            
            aData.Text=(String) ObjUser.getAttribute("USER_NAME").getObj();
            aData.Code=(long)ObjUser.getAttribute("USER_ID").getVal();
            
            cmbUserModel.addElement(aData);
        }
        //----------------------------------------//
        
        
        
   //------------------------------ //
    }

}
