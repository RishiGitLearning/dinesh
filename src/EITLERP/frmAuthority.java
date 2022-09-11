/*
 * frmItemCriteria.java
 *
 * Created on May 28, 2005, 3:04 PM
 */

package EITLERP;

/**
 *
 * @author  root
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;


public class frmAuthority extends javax.swing.JApplet {
    
    
    private EITLComboModel cmbUserModel=new EITLComboModel();
    private EITLComboModel cmbAuthorityModel=new EITLComboModel();
    private EITLComboModel cmbModuleModel=new EITLComboModel();
    private EITLComboModel cmbDeptModel=new EITLComboModel();
    
    
    private EITLTableModel DataModel=new EITLTableModel();
    
    private clsAuthority ObjAuthority=new clsAuthority();
    
    
    /** Initializes the applet frmItemCriteria */
    public void init() {
        initComponents();
        GenerateCombo();
        GenerateGrid();
        setSize(675, 560);
    }
    
    
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbUser = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtEntryNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbDept = new javax.swing.JComboBox();
        lblStartsWith = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        lblTo = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbAuthority = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(null);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DOCUMENT AUTHORITY DELEGATION SETUP");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(7, 10, 322, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 660, 35);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(null);

        jLabel2.setText("User");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(92, 55, 38, 15);
        jPanel2.add(cmbUser);
        cmbUser.setBounds(141, 50, 251, 24);

        jLabel3.setText("Entry No.");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(69, 15, 64, 15);

        txtEntryNo.setEnabled(false);
        jPanel2.add(txtEntryNo);
        txtEntryNo.setBounds(141, 12, 118, 21);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Give Authority Of");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(1, 93, 130, 15);

        cmbDept.setNextFocusableComponent(cmdAdd);
        cmbDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDeptItemStateChanged(evt);
            }
        });
        jPanel2.add(cmbDept);
        cmbDept.setBounds(142, 189, 251, 24);

        lblStartsWith.setText("From Date");
        jPanel2.add(lblStartsWith);
        lblStartsWith.setBounds(59, 159, 74, 15);
        jPanel2.add(txtFromDate);
        txtFromDate.setBounds(141, 157, 118, 21);

        lblTo.setText("To");
        jPanel2.add(lblTo);
        lblTo.setBounds(268, 160, 24, 15);

        txtToDate.setNextFocusableComponent(cmbDept);
        jPanel2.add(txtToDate);
        txtToDate.setBounds(307, 157, 118, 21);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Module ");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(61, 126, 70, 15);
        jPanel2.add(cmbAuthority);
        cmbAuthority.setBounds(141, 87, 251, 24);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Department");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(27, 193, 100, 15);
        jPanel2.add(cmbModule);
        cmbModule.setBounds(141, 121, 251, 24);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(7, 41, 644, 230);

        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(8, 320, 643, 210);

        cmdAdd.setText("Add ");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        getContentPane().add(cmdAdd);
        cmdAdd.setBounds(352, 287, 138, 25);

        cmdRemove.setText("Remove ");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        getContentPane().add(cmdRemove);
        cmdRemove.setBounds(497, 287, 151, 25);
    }// </editor-fold>//GEN-END:initComponents
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if(Table.getSelectedRow()>=0 && Table.getRowCount()>0) {
            if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete selected condition ?","Confirmation",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                int EntryNo=Integer.parseInt((String)Table.getValueAt(Table.getSelectedRow(), 0));
                ObjAuthority.Remove(EITLERPGLOBAL.gCompanyID, EntryNo);
                GenerateGrid();
            }
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:

        
        if(!txtFromDate.getText().trim().equals(""))
        {
          if(!EITLERPGLOBAL.isDate(txtFromDate.getText()))  
          {
            JOptionPane.showMessageDialog(null,"Please enter valid From date");
            return;
          }
          
          if(txtToDate.getText().trim().equals(""))
          {
            JOptionPane.showMessageDialog(null,"Please enter to date");
            return;
          }
        }
        

        if(!txtToDate.getText().trim().equals(""))
        {
          if(!EITLERPGLOBAL.isDate(txtToDate.getText()))  
          {
            JOptionPane.showMessageDialog(null,"Please enter valid to date");
            return;
          }
          
          if(txtFromDate.getText().trim().equals(""))
          {
            JOptionPane.showMessageDialog(null,"Please enter from date");
            return;
          }
        }
               
        int UserID=EITLERPGLOBAL.getComboCode(cmbUser);
        int AuthorityUserID=EITLERPGLOBAL.getComboCode(cmbAuthority);
        int ModuleID=EITLERPGLOBAL.getComboCode(cmbModule);
        int DeptID=EITLERPGLOBAL.getComboCode(cmbDept);
        String FromDate=EITLERPGLOBAL.formatDateDB(txtFromDate.getText());
        String ToDate=EITLERPGLOBAL.formatDateDB(txtToDate.getText());
        
        //Set the Data
        ObjAuthority.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjAuthority.setAttribute("SR_NO",0);
        ObjAuthority.setAttribute("AUTHORITY_USER_ID",AuthorityUserID);
        ObjAuthority.setAttribute("FROM_DATE",FromDate);
        ObjAuthority.setAttribute("TO_DATE",ToDate);
        ObjAuthority.setAttribute("USER_ID",UserID);
        ObjAuthority.setAttribute("MODULE_ID",ModuleID);
        ObjAuthority.setAttribute("DEPT_ID",DeptID);
        ObjAuthority.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
        ObjAuthority.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        ObjAuthority.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
        ObjAuthority.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        
        if(ObjAuthority.Insert()) {
            GenerateGrid();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while saving");
        }
    }//GEN-LAST:event_cmdAddActionPerformed
    
    private void cmbDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDeptItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbDeptItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbAuthority;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JComboBox cmbUser;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStartsWith;
    private javax.swing.JLabel lblTo;
    private javax.swing.JTextField txtEntryNo;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables
    
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.addColumn("Sr No.");
        DataModel.addColumn("User");
        DataModel.addColumn("From Date");
        DataModel.addColumn("To Date");
        DataModel.addColumn("Module");
        DataModel.addColumn("Authority");
        DataModel.addColumn("Department");
        
        DataModel.SetNumeric(1,true);
                
        DataModel.TableReadOnly(true);
    }
    
    
    private void GenerateGrid() {
        HashMap List=new HashMap();
        
        List=ObjAuthority.getList();
        
        FormatGrid();
        
        for(int i=1;i<=List.size();i++) {
            Object[] rowData=new Object[7];
            clsAuthority ObjItem=(clsAuthority)List.get(Integer.toString(i));
            
            rowData[0]=Integer.toString((int)ObjItem.getAttribute("SR_NO").getVal());
            rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("USER_ID").getVal());
            rowData[2]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("FROM_DATE").getObj());
            rowData[3]=EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("TO_DATE").getObj());
            rowData[4]=clsModules.getModuleName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("MODULE_ID").getVal());
            rowData[5]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("AUTHORITY_USER_ID").getVal());
            rowData[6]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,(int)ObjItem.getAttribute("DEPT_ID").getVal());
           
            DataModel.addRow(rowData);
        }
        
    }
    
    
    
    private void GenerateCombo() {

        //--- Module Combo ------//
        cmbModuleModel=new EITLComboModel();
        cmbModule.removeAllItems();
        cmbModule.setModel(cmbModuleModel);
        
        String strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY MODULE_ID";
        
        HashMap List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            //Check that Module Access Rights are given
            int ModuleID=(int)ObjModules.getAttribute("MODULE_ID").getVal();
            int MenuID=clsMenu.getMenuIDFromModule(EITLERPGLOBAL.gCompanyID, ModuleID);
            
            //if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,MenuID)) {
            ComboData aData=new ComboData();
            aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbModuleModel.addElement(aData);
            //}
        }
        //===============================//
        
        
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
        

        
        //-------- Generating Buyer Combo --------//
        cmbAuthorityModel=new EITLComboModel();
        cmbAuthority.removeAllItems();
        cmbAuthority.setModel(cmbAuthorityModel);
        ObjUser=new clsUser();
        List=ObjUser.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID));
        for(int i=1;i<=List.size();i++) {
            ObjUser=(clsUser) List.get(Integer.toString(i));
            
            ComboData aData=new ComboData();
            
            aData.Text=(String) ObjUser.getAttribute("USER_NAME").getObj();
            aData.Code=(long)ObjUser.getAttribute("USER_ID").getVal();
            
            cmbAuthorityModel.addElement(aData);
        }
        //----------------------------------------//
        

        //----- Generate Department Combo ------- //
        cmbDeptModel=new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);
        
        
        List=clsDepartment.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        for(int i=1;i<=List.size();i++) {
            clsDepartment ObjDept=(clsDepartment) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text=(String) ObjDept.getAttribute("DEPT_DESC").getObj();
            cmbDeptModel.addElement(aData);
        }
        //------------------------------ //
        
        
    }
}
