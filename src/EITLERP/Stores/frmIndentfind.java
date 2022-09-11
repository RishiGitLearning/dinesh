/*
 * frmIndentfind.java
 *
 * Created on May 25, 2004, 1:51 PM
 */

package EITLERP.Stores;

/**
 *  
 * @author  nhpatel
 */
/*<APPLET CODE=frmItemHierarchy.class HEIGHT=245 WIDTH=550*/
import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
 
public class frmIndentfind extends javax.swing.JApplet {

    private EITLComboModel cmbDeptModel;
    private EITLComboModel cmbIndentModel;
    public boolean Cancelled=false;
    public String strQuery;
    
    /** Creates new form frmIndentfind */
    public frmIndentfind() {
        System.gc();
        setSize(580,290);
        initComponents();
        GenerateCombos();
        cmbDept.setSelectedIndex(-1);
        cmbIndentType.setSelectedIndex(-1);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIndentNo = new javax.swing.JTextField();
        txtFromIndentDate = new javax.swing.JTextField();
        cmbIndentType = new javax.swing.JComboBox();
        cmbDept = new javax.swing.JComboBox();
        txtToIndentDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPRNo = new javax.swing.JTextField();
        cmdFind = new javax.swing.JButton();
        cmdCancel2 = new javax.swing.JButton();
        cmdClear = new javax.swing.JButton();
        
        getContentPane().setLayout(null);
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        
        jPanel1.setLayout(null);
        
        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel1.setText("Indent No.");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(62, 31, 72, 20);
        
        jLabel3.setText("Indent Type");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(54, 92, 80, 20);
        
        jLabel4.setText("Department");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(54, 122, 80, 20);
        
        jLabel2.setText("From Indent Date.");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 60, 114, 20);
        
        txtIndentNo.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.add(txtIndentNo);
        txtIndentNo.setBounds(140, 30, 99, 22);
        
        jPanel1.add(txtFromIndentDate);
        txtFromIndentDate.setBounds(140, 60, 90, 22);
        
        cmbIndentType.setEditable(true);
        jPanel1.add(cmbIndentType);
        cmbIndentType.setBounds(140, 90, 184, 24);
        
        cmbDept.setEditable(true);
        jPanel1.add(cmbDept);
        cmbDept.setBounds(140, 120, 184, 24);
        
        jPanel1.add(txtToIndentDate);
        txtToIndentDate.setBounds(320, 60, 90, 22);
        
        jLabel5.setText("To Date.");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(250, 60, 60, 20);
        
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
        txtItemID.setBounds(141, 156, 121, 19);
        
        jLabel13.setText("Item");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(91, 159, 39, 15);
        
        txtItemName.setEditable(false);
        jPanel1.add(txtItemName);
        txtItemName.setBounds(142, 177, 309, 19);
        
        jLabel14.setText("PR NO.");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(87, 217, 45, 15);
        
        jPanel1.add(txtPRNo);
        txtPRNo.setBounds(144, 213, 114, 19);
        
        getContentPane().add(jPanel1);
        jPanel1.setBounds(7, 10, 458, 250);
        
        cmdFind.setText("Find");
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdFind);
        cmdFind.setBounds(478, 19, 80, 25);
        
        cmdCancel2.setText("Cancel");
        cmdCancel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancel2ActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdCancel2);
        cmdCancel2.setBounds(478, 59, 80, 25);
        
        cmdClear.setText("Clear");
        cmdClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearActionPerformed(evt);
            }
        });
        
        getContentPane().add(cmdClear);
        cmdClear.setBounds(478, 99, 80, 25);
        
    }//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

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

    private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
        // TODO add your handling code here:
       Cancelled=false;
       String subQuery="";
       
       strQuery=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
       
       if(!txtIndentNo.getText().equals(""))
       {
         strQuery=strQuery+" AND INDENT_NO='"+txtIndentNo.getText()+"' ";
       }
       
       if(!txtFromIndentDate.getText().equals(""))
       {
         strQuery=strQuery+" AND INDENT_DATE>='"+EITLERPGLOBAL.formatDateDB(txtFromIndentDate.getText())+"' ";
       }
       if(!txtToIndentDate.getText().equals(""))
       {
         strQuery=strQuery+" AND INDENT_DATE<='"+EITLERPGLOBAL.formatDateDB(txtToIndentDate.getText())+"' ";
       }
       if(cmbIndentType.getSelectedIndex() !=-1)
       {
         String IndentCode = (String) EITLERPGLOBAL.getCombostrCode(cmbIndentType);  
         strQuery=strQuery+" AND INDENT_TYPE ='"+IndentCode+"' ";
       }
       if(cmbDept.getSelectedIndex() !=-1)
       {
         int DeptCode = EITLERPGLOBAL.getComboCode(cmbDept);  
         strQuery=strQuery+" AND FOR_DEPT_ID = "+DeptCode+" ";
       }
       
       
       //====== Sub Query =======//
       if(!txtItemID.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND ITEM_CODE='"+txtItemID.getText()+"' ";
       }
       
       if(!txtPRNo.getText().trim().equals(""))
       {
        subQuery=subQuery+" AND MR_NO='"+txtPRNo.getText()+"' ";   
       }
       
       if(!subQuery.trim().equals(""))
       {
        subQuery=" AND INDENT_NO IN (SELECT INDENT_NO FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+subQuery+")";
       }
       
       strQuery=strQuery+subQuery;
       //========== End Sub Query ============//
       

       getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdFindActionPerformed

    private void cmdCancel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancel2ActionPerformed
        // TODO add your handling code here:
        strQuery="";
        Cancelled=true;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdCancel2ActionPerformed

    private void cmdClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        // TODO add your handling code here:
        strQuery=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID;
        Cancelled=false;
        getParent().getParent().getParent().getParent().show(false);
    }//GEN-LAST:event_cmdClearActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JComboBox cmbIndentType;
    private javax.swing.JButton cmdCancel2;
    private javax.swing.JButton cmdClear;
    private javax.swing.JButton cmdFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtFromIndentDate;
    private javax.swing.JTextField txtIndentNo;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPRNo;
    private javax.swing.JTextField txtToIndentDate;
    // End of variables declaration//GEN-END:variables

    private void GenerateCombos()
    {
        HashMap List=new HashMap();
        String strCondition="";
        //--- Generate Type Combo ------//
        cmbIndentModel=new EITLComboModel();
        cmbIndentType.removeAllItems();
        cmbIndentType.setModel(cmbIndentModel);
        
        ComboData aData=new ComboData();
        aData.strCode="R";
        aData.Text="MATERIAL REQUISITION";
        cmbIndentModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="M";
        aData.Text="M.O.M";
        cmbIndentModel.addElement(aData);

        aData=new ComboData();
        aData.strCode="O";
        aData.Text="OTHERS";
        cmbIndentModel.addElement(aData);
        //===============================//
        cmbDeptModel=new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);
        
        List=clsDepartment.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        for(int i=1;i<=List.size();i++) {
            clsDepartment ObjDept=(clsDepartment) List.get(Integer.toString(i));
            aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text=(String) ObjDept.getAttribute("DEPT_DESC").getObj();
            cmbDeptModel.addElement(aData);
        }
        //------------------------------ //
    }
   
}
