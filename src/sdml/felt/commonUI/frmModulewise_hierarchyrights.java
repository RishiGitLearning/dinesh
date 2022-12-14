/*
 * frmLogin.java
 *
 * Created on April 6, 2004, 11:10 AM
 */
package sdml.felt.commonUI;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.net.*;

/**
 *
 * @author 
 *
 */
public class frmModulewise_hierarchyrights extends javax.swing.JApplet {

    /**
     * Initialize the <i>Login</i> Form
     *
     * @see This is sample
     *
     */
    private SDMLComboModel cmbDeptModel;
    private Vector ListVector;

    public void init() {
        //System.setProperty("javaplugin.maxHeapSize", "400m");
        System.gc();
        setSize(400, 500);
        ListVector = new Vector(1000);
        //GenerateList();
        GenerateCombo();
        //GenerateList();
    }

    /**
     * Creates new form frmLogin
     */
    public frmModulewise_hierarchyrights() {
        System.gc();
        initComponents();

        //Chaning Look and Feel
        if (GUIManager.isAvailableLookAndFeel(GUIManager.Windows)) {
            GUIManager.SetLookFeel(GUIManager.Windows);
            GUIManager.UpdateComponents(this);
        }
        jLabel2.setVisible(false);
        cmbDepartment.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListModule = new javax.swing.JList();
        cmdClearSelection = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbDepartment = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(270, 131));
        jScrollPane1.setViewportView(ListModule);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 50, 200, 310);

        cmdClearSelection.setText("Clear Selection");
        cmdClearSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearSelectionActionPerformed(evt);
            }
        });
        jPanel1.add(cmdClearSelection);
        cmdClearSelection.setBounds(125, 371, 150, 23);

        jLabel1.setText("For mulitiple selection Hold CTRL key.....");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 400, 300, 14);

        jLabel2.setText("Department ");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 20, 100, 14);

        cmbDepartment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartmentItemStateChanged(evt);
            }
        });
        cmbDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDepartmentActionPerformed(evt);
            }
        });
        jPanel1.add(cmbDepartment);
        cmbDepartment.setBounds(110, 20, 210, 20);

        jButton1.setText("Select All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(21, 371, 96, 23);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(20, 10, 330, 420);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        getContentPane().add(cmdPrint);
        cmdPrint.setBounds(50, 440, 88, 23);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        SelectAll();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDepartmentActionPerformed
        GenerateList();
    }//GEN-LAST:event_cmbDepartmentActionPerformed

    private void cmbDepartmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartmentItemStateChanged
        GenerateList();
    }//GEN-LAST:event_cmbDepartmentItemStateChanged

    private void cmdClearSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearSelectionActionPerformed
        // TODO add your handling code here:
        ListModule.clearSelection();

    }//GEN-LAST:event_cmdClearSelectionActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_cmdPrintActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ListModule;
    private javax.swing.JComboBox cmbDepartment;
    private javax.swing.JButton cmdClearSelection;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void GenerateList() {
        HashMap Listmap = new HashMap();
        ListVector.removeAllElements();
        ListModule.setListData(ListVector);

        //String Condition = " WHERE COMPANY_ID="+SDMLERPGLOBAL.gCompanyID+"   AND MODULE_ID IN (SELECT MODULE_ID FROM D_COM_DEPT_MODULES WHERE DEPT_ID = "+ SDMLERPGLOBAL.getComboCode(cmbDepartment) +"  )";
        String Condition = " WHERE COMPANY_ID=" + SDMLERPGLOBAL.gCompanyID + " ORDER BY MODULE_DESC";
        Listmap = clsModules.getList(Condition);
        if (!Listmap.isEmpty()) {
            String desc = "";
            int id = 0;
            //ListVector.addElement(new String("All"));
            for (int i = 1; i <= Listmap.size(); i++) {
                clsModules ObjModules = (clsModules) Listmap.get(Integer.toString(i));

                id = ObjModules.getAttribute("MODULE_ID").getInt();
                desc = ObjModules.getAttribute("MODULE_DESC").getString() + "---" + id;
                ListVector.addElement(desc);

            }
            ListModule.setListData(ListVector);
        }
    }

    private void PreviewReport() {

        int[] ModuleId = ListModule.getSelectedIndices();

        if (ModuleId.length == 0) {
            JOptionPane.showMessageDialog(null, "Please Select any Module");
            return;
        }

        String qry = "AND A.MODULE_ID IN (";

        for (int i = 0; i < ModuleId.length; i++) {

            String str = ListVector.get(ModuleId[i]).toString();
            String[] array = str.split("---");

            //  System.out.println("Module Name" + array[0] +"Module ID:-" + array[1]);
            qry += array[1];
            if (i != ModuleId.length - 1) {
                qry += ",";
            }

        }
        qry += ")";

        //System.out.println("STRING QRY" + qry);
        HashMap Params = new HashMap();
        //(1) company_id - Integer
        //(2) grn_no     - String

        Params.put("company_id", new Integer(SDMLERPGLOBAL.gCompanyID));
        Params.put("qry", qry);

        try {
            String DeptName = clsDepartment.getDeptName(SDMLERPGLOBAL.gCompanyID, SDMLERPGLOBAL.getComboCode(cmbDepartment));
            URL ReportFile = new URL("http://" + SDMLERPGLOBAL.HostIP + "/RS/Reports/rptmodulewise_hierarchyrights.jsp?dbURL=" + SDMLERPGLOBAL.DatabaseURL + "&CompanyID=" + SDMLERPGLOBAL.gCompanyID + "&DocNo=" + qry + "&DeptName=" + DeptName);
            System.out.println("http://" + SDMLERPGLOBAL.HostIP + "/RS/Reports/rptmodulewise_hierarchyrights.jsp?dbURL=" + SDMLERPGLOBAL.DatabaseURL + "&CompanyID=" + SDMLERPGLOBAL.gCompanyID + "&DocNo=" + qry + "&DeptName=" + DeptName);
            SDMLERPGLOBAL.loginContext.showDocument(ReportFile, "_blank");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File error " + e.getMessage());
        }
    }

    private void GenerateCombo() {
        HashMap List = new HashMap();
        String strCondition = "";

        //----- Generate Department Combo ------- //
        cmbDeptModel = new SDMLComboModel();
        cmbDepartment.removeAllItems();
        cmbDepartment.setModel(cmbDeptModel);

        //List=clsDepartment.getList(" WHERE COMPANY_ID="+SDMLERPGLOBAL.gCompanyID);
        //List=clsDepartment.getList(" WHERE COMPANY_ID="+SDMLERPGLOBAL.gCompanyID+" AND DEPT_ID IN (SELECT DEPT_ID FROM D_COM_USER_MASTER WHERE USER_ID = "+9+" )");
        System.out.println("User ID=" + SDMLERPGLOBAL.gUserID);
        if (SDMLERPGLOBAL.gUserID == 1 || SDMLERPGLOBAL.gUserID == 11) {
            List = clsDepartment.getList(" WHERE COMPANY_ID=" + SDMLERPGLOBAL.gCompanyID + " ");
        } else {
            List = clsDepartment.getList(" WHERE COMPANY_ID=" + SDMLERPGLOBAL.gCompanyID + " AND DEPT_ID IN (SELECT DEPT_ID FROM D_COM_USER_MASTER WHERE USER_ID = " + SDMLERPGLOBAL.gUserID + " )");
        }

        ComboData aData1 = new ComboData();
        aData1.Code = -1;
        aData1.Text = "";
        cmbDeptModel.addElement(aData1);

        for (int i = 1; i <= List.size(); i++) {
            clsDepartment ObjDept = (clsDepartment) List.get(Integer.toString(i));
            ComboData aData = new ComboData();
            aData.Code = (int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text = (String) ObjDept.getAttribute("DEPT_DESC").getObj();
            cmbDeptModel.addElement(aData);
        }
        //------------------------------ //

    }

    private void SelectAll() {

        ListModule.setSelectionInterval(0, ListModule.getModel().getSize() - 1);
        //.setSelectionInterval(start, end);
        /*
        
         for(int i=0;i<=ListModule.getModel().getSize()-1;i++)
         {
         ListModule.setSelectedIndex(i);
         }*/

    }

}
