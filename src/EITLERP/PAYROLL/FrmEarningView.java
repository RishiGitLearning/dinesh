/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.PAYROLL;

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.data;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

/**
 *
 * @author Dharmendra
 */
public class FrmEarningView extends javax.swing.JApplet {

    /**
     * Initializes the applet FrmEarningView
     */
    private EITLComboModel mcmbtype, mcmbcategory, mcmbdept, mcmbdivision;
    private EITLTableModel DataModel;
    //  private clsExcelExporter exp = new clsExcelExporter();
    String Query = "";
    String DISPLAY_FIELDS;
    String WHERE_CONDITION;
    int Fields_length = 0;
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>

        initComponents();
        setSize(800, 500);
        GenerateTypeCombo();
        mcmbcategory = new EITLComboModel();
        GenerateCategoryCombo();
        mcmbdept = new EITLComboModel();
        GenerateDeptCombo();
        mcmbdivision = new EITLComboModel();
        GenerateDivisionCombo();
        
        
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        file1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        cmbcompany = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        frmyyyymm = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        toyyyymm = new javax.swing.JTextField();
        cmdview = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableView = new javax.swing.JTable();
        ExportData = new javax.swing.JButton();
        cmbtype = new javax.swing.JComboBox();
        cmbcategory = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        cmbdept = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cmbdivision = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        txtEmpCode = new javax.swing.JTextField();

        getContentPane().setLayout(null);

        jLabel1.setText("Company");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 30, 80, 20);

        cmbcompany.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALL", "VADODARA", "ANKLESHWAR" }));
        getContentPane().add(cmbcompany);
        cmbcompany.setBounds(100, 30, 200, 30);

        jLabel2.setText("Category");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(530, 30, 70, 30);

        jLabel3.setText("Type");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(340, 30, 40, 30);

        jLabel4.setText("From");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(60, 130, 40, 20);
        getContentPane().add(frmyyyymm);
        frmyyyymm.setBounds(100, 120, 70, 30);

        jLabel5.setText("YYYYMM");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(220, 100, 50, 20);

        jLabel6.setText("To");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(190, 130, 20, 17);

        jLabel7.setText("YYYYMM");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(110, 100, 60, 20);
        getContentPane().add(toyyyymm);
        toyyyymm.setBounds(210, 120, 70, 30);

        cmdview.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cmdview.setText("VIEW");
        cmdview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdviewActionPerformed(evt);
            }
        });
        getContentPane().add(cmdview);
        cmdview.setBounds(650, 120, 120, 30);

        TableView.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableView);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 160, 760, 250);

        ExportData.setText("EXPORT TO EXCEL");
        ExportData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportDataActionPerformed(evt);
            }
        });
        getContentPane().add(ExportData);
        ExportData.setBounds(10, 420, 160, 40);

        cmbtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbtype);
        cmbtype.setBounds(380, 30, 140, 30);

        cmbcategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbcategory);
        cmbcategory.setBounds(600, 30, 170, 30);

        jLabel8.setText("Department");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(10, 70, 100, 30);

        cmbdept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbdept);
        cmbdept.setBounds(100, 70, 200, 27);

        jLabel9.setText("Division");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(320, 70, 50, 30);

        cmbdivision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbdivision);
        cmbdivision.setBounds(380, 70, 140, 30);

        jLabel10.setText("Emp Code ");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(300, 127, 90, 20);
        getContentPane().add(txtEmpCode);
        txtEmpCode.setBounds(380, 120, 140, 27);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdviewActionPerformed
        // TODO add your handling code here:
        FillTableData();
        
        Query = "SELECT "+DISPLAY_FIELDS+" FROM PAYROLL.EARNING "+WHERE_CONDITION;
        System.out.println("QUERY : "+Query);
        ResultSet trs;
        trs = data.getResult(Query);
        
        try {
            trs.last();
            int No_of_rows  = trs.getRow();
            if(No_of_rows>0)
            {
                trs.first();
                int i=0;
                while (!trs.isAfterLast()) {
                    //cmbData.Text = trs.getString("EMPLOYEE_TYPE");
                        String[] rowData = {};
                        DataModel.addRow(rowData);
                        for(int j=0;j<Fields_length;j++)
                        {
                            DataModel.setValueAt(trs.getString(j+1), i, j);
                        }

                    i++;    
                    trs.next();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No Record Found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdviewActionPerformed
    private void FillTableData()
    {
        DISPLAY_FIELDS = "COMPANY_ID,DIVISION_CODE,DEPT_CODE,EMPLOYEE_TYPE,EMPLOYEE_CATEGORY,EMPLOYEE_CODE,NAME";
        WHERE_CONDITION = " WHERE EMPLOYEE_CODE!='' ";
        
        if(cmbcompany.getSelectedItem().equals("ALL") && cmbtype.getSelectedItem().equals("ALL"))
        { 
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID IN (1,2) AND EMPLOYEE_TYPE IN ('STAFF','WORKER','RETAINER','FIXED_TERM')";       
        }
        else if(cmbcompany.getSelectedItem().equals("ALL") && cmbtype.getSelectedItem().equals("STAFF"))
        { 
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID IN (1,2) AND EMPLOYEE_TYPE='STAFF'";   
        }
        else if(cmbcompany.getSelectedItem().equals("ALL") && cmbtype.getSelectedItem().equals("WORKER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,ADHOC_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID IN (1,2) AND EMPLOYEE_TYPE='WORKER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("ALL") && cmbtype.getSelectedItem().equals("RETAINER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",RETAINER_FEE,OTHER_ALLOWANCE,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID IN (1,2) AND EMPLOYEE_TYPE='RETAINER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("ALL") && cmbtype.getSelectedItem().equals("FIXED_TERM"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,HRA,ATTENDANCE_ALLOWANCE,PAID_HOLIDAY,OTHER_PFABLE_AMOUNT,OTHER_NONPFABLE_AMOUNT,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID IN (1,2) AND EMPLOYEE_TYPE='FIXED_TERM'";     
        }
        else if(cmbcompany.getSelectedItem().equals("VADODARA") && cmbtype.getSelectedItem().equals("STAFF"))
        { 
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=1 AND EMPLOYEE_TYPE='STAFF'";       
        }
        else if(cmbcompany.getSelectedItem().equals("VADODARA") && cmbtype.getSelectedItem().equals("WORKER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,ADHOC_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=1 AND EMPLOYEE_TYPE='WORKER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("VADODARA") && cmbtype.getSelectedItem().equals("RETAINER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",RETAINER_FEE,OTHER_ALLOWANCE,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=1 AND EMPLOYEE_TYPE='RETAINER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("VADODARA") && cmbtype.getSelectedItem().equals("RETAILNER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",RETAINER_FEE,OTHER_ALLOWANCE,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=1 AND EMPLOYEE_TYPE='RETAILNER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("VADODARA") && cmbtype.getSelectedItem().equals("FIXED_TERM"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,HRA,ATTENDANCE_ALLOWANCE,PAID_HOLIDAY,OTHER_PFABLE_AMOUNT,OTHER_NONPFABLE_AMOUNT,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=1 AND EMPLOYEE_TYPE='FIXED_TERM'";     
        }
        else if(cmbcompany.getSelectedItem().equals("ANKLESHWAR") && cmbtype.getSelectedItem().equals("STAFF"))
        { 
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=2 AND EMPLOYEE_TYPE='STAFF'";    
        }
        else if(cmbcompany.getSelectedItem().equals("ANKLESHWAR") && cmbtype.getSelectedItem().equals("WORKER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,DA,CONVEY_ALLOWANCE,OTHER_ALLOWANCE,PERSONAL_PAY,ADHOC_PAY,PAID_HOLIDAY,WASHING_ALLOWANCE,ATTENDANCE_ALLOWANCE,DA_DIFFERANCE,ELECTRICITY,MAGAZINE,PERFORMANCE_ALLOWANCE,TOTAL_EARNING,HRA";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=2 AND EMPLOYEE_TYPE='WORKER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("ANKLESHWAR") && cmbtype.getSelectedItem().equals("RETAINER"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",RETAINER_FEE,OTHER_ALLOWANCE,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=2 AND EMPLOYEE_TYPE='RETAINER'";     
        }
        else if(cmbcompany.getSelectedItem().equals("ANKLESHWAR") && cmbtype.getSelectedItem().equals("FIXED_TERM"))
        {
            DISPLAY_FIELDS = DISPLAY_FIELDS + ",BASIC,HRA,ATTENDANCE_ALLOWANCE,PAID_HOLIDAY,OTHER_PFABLE_AMOUNT,OTHER_NONPFABLE_AMOUNT,TOTAL_EARNING";
            WHERE_CONDITION = WHERE_CONDITION + " AND COMPANY_ID=2 AND EMPLOYEE_TYPE='FIXED_TERM'";     
        }
        
        if(!cmbcategory.getSelectedItem().equals("ALL"))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND EMPLOYEE_CATEGORY='"+EITLERPGLOBAL.getCombostrCode(cmbcategory)+"'";
        }
        
        if(!cmbdept.getSelectedItem().equals("ALL"))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND DEPT_CODE='"+EITLERPGLOBAL.getCombostrCode(cmbdept)+"'";
        }
        
        if(!cmbdivision.getSelectedItem().equals("ALL"))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND DIVISION_CODE='"+EITLERPGLOBAL.getCombostrCode(cmbdivision)+"'";
        }
        
        if(!frmyyyymm.getText().equals(""))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND YYYYMM >= '"+frmyyyymm.getText()+"'";
        }
        if(!toyyyymm.getText().equals(""))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND YYYYMM <= '"+toyyyymm.getText()+"'";
        }
        
        if(!txtEmpCode.getText().equals(""))
        {
            WHERE_CONDITION = WHERE_CONDITION + " AND EMPLOYEE_CODE = '"+txtEmpCode.getText()+"'";
        }
        
        /*
        COMPANY_ID	 
        EMPLOYEE_TYPE	 
        EMPLOYEE_CATEGORY	 
        EMPLOYEE_CODE	
        NAME	 
        DEPT_CODE	 
        DIVISION_CODE	 
        SUB_CODE	 
        BASIC	 
        DA	 
        HRA	 
        CONVEY_ALLOWANCE	 
        PAID_HOLIDAY	 
        ELECTRICITY	 
        OTHER_ALLOWANCE	
        PERFORMANCE_ALLOWANCE	 
        PERSONAL_PAY	 
        MAGAZINE	 
        TDS	 
        SALARY_ADJUST	 
        ADHOC_PAY	
        DA_DIFFERANCE	 
        ATTENDANCE_ALLOWANCE	 
        SHIFT_ALLOWANCE	 
        WASHING_ALLOWANCE	 
        OTHER_PFABLE_AMOUNT	 
        OTHER_NONPFABLE_AMOUNT	 
        RETAINER_FEE	 
        TOTAL_EARNING	 
        MEDICAL_AMOUNT	 
        HOTEL_AMOUNT	 
        TELEPHONE_AMOUNT	 
        KNOWLEDGE_AMOUNT	 
        EDUCATION_AMOUNT	 
        FURNISHING_AMOUNT	 
        TRAVELLING_AMOUNT	 
        ENTERTAINMENT_AMOUNT	 
        REIMBURSEMENT_AMOUNT	 
        YYYYMM
        */
        FormatGrid();
        
    }
    
    private void ExportDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportDataActionPerformed
        // TODO add your handling code here:
        File file = null;
        file1.setVisible(true);
        try {
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
//            exp.fillData(TableView, file, "EARNING_DATA");
//            JOptionPane.showMessageDialog(null, "Data saved at "
//                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
//                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_ExportDataActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ExportData;
    private javax.swing.JTable TableView;
    private javax.swing.JComboBox cmbcategory;
    private javax.swing.JComboBox cmbcompany;
    private javax.swing.JComboBox cmbdept;
    private javax.swing.JComboBox cmbdivision;
    private javax.swing.JComboBox cmbtype;
    private javax.swing.JButton cmdview;
    private javax.swing.JFileChooser file1;
    private javax.swing.JTextField frmyyyymm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField toyyyymm;
    private javax.swing.JTextField txtEmpCode;
    // End of variables declaration//GEN-END:variables
 
    
    private void FormatGrid()
    {
       try {
                DataModel = new EITLTableModel();
                TableView.removeAll();

                TableView.setModel(DataModel);
                TableView.setAutoResizeMode(0);

                int i=0;
                   
                    String[] str=DISPLAY_FIELDS.split(",");
                    Fields_length = str.length;
                    for (String str1 : str) {
                        
                        if(str1.contains(" AS "))
                        {
                            String[] str_column = str1.split(" AS ");
                            
                            DataModel.addColumn(str_column[1]);
                            DataModel.SetVariable(i, str_column[0]);
                            TableView.getColumnModel().getColumn(i).setMinWidth(100);
                        }
                        else
                        {
                            DataModel.addColumn(str1);
                            DataModel.SetVariable(i, str1);
                            TableView.getColumnModel().getColumn(i).setMinWidth(100);
                        }
                        
                        i++;
                    }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    private void GenerateTypeCombo() {
        String sql;

        mcmbtype = new EITLComboModel();
        cmbtype.setModel(mcmbtype);
        cmbtype.removeAllItems();
        ResultSet trs = null;

        sql = "SELECT DISTINCT EMPLOYEE_TYPE FROM PAYROLL.EARNING";
        trs = data.getResult(sql);

        ComboData cmbData1 = new ComboData();
        cmbData1.Text = "ALL";
        cmbData1.strCode = "ALL";
        mcmbtype.addElement(cmbData1);

        try {
            trs.first();
            while (!trs.isAfterLast()) {
                ComboData cmbData = new ComboData();
                cmbData.Text = trs.getString("EMPLOYEE_TYPE");
                cmbData.strCode = trs.getString("EMPLOYEE_TYPE");
                mcmbtype.addElement(cmbData);
                trs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateCategoryCombo() {

        cmbcategory.setModel(mcmbcategory);
        cmbcategory.removeAllItems();

        ComboData cmbData1 = new ComboData();
        cmbData1.Text = "ALL";
        cmbData1.strCode = "ALL";
        mcmbcategory.addElement(cmbData1);

        ComboData cmbData = new ComboData();
        cmbData.Text = "PERMENANT";
        cmbData.strCode = "C";
        mcmbcategory.addElement(cmbData);

        ComboData cmbData2 = new ComboData();
        cmbData2.Text = "BADLI";
        cmbData2.strCode = "D";
        mcmbcategory.addElement(cmbData2);

    }

    private void GenerateDeptCombo() {
        String sql;

        cmbdept.setModel(mcmbdept);
        cmbdept.removeAllItems();
        ResultSet trs = null;

        ComboData cmbData1 = new ComboData();
        cmbData1.Text = "ALL";
        cmbData1.strCode = "ALL";
        mcmbdept.addElement(cmbData1);

        sql = "SELECT DISTINCT E.DEPT_CODE,COALESCE(M.DESCRIPTION,CONCAT(E.DEPT_CODE,'-GENERAL')) AS DESCRIPTION  "
                + "FROM PAYROLL.EARNING E LEFT JOIN PAYROLL.DEPT_MASTER M ON E.DEPT_CODE=M.DEPT_CODE";
        trs = data.getResult(sql);

        try {
            trs.first();
            while (!trs.isAfterLast()) {
                ComboData cmbData = new ComboData();
                cmbData.Text = trs.getString("DESCRIPTION");
                cmbData.strCode = trs.getString("DEPT_CODE");
                mcmbdept.addElement(cmbData);
                trs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateDivisionCombo() {
        String sql;

        cmbdivision.setModel(mcmbdivision);
        cmbdivision.removeAllItems();
        ResultSet trs = null;

        ComboData cmbData1 = new ComboData();
        cmbData1.Text = "ALL";
        cmbData1.strCode = "ALL";
        mcmbdivision.addElement(cmbData1);

        sql = "SELECT DISTINCT COALESCE(M.DIVISION,'GENERAL') AS DIVISION  "
                + "FROM PAYROLL.EARNING E LEFT JOIN PAYROLL.DEPT_MASTER M ON E.DEPT_CODE=M.DEPT_CODE";
        trs = data.getResult(sql);

        try {
            trs.first();
            while (!trs.isAfterLast()) {
                ComboData cmbData = new ComboData();
                cmbData.Text = trs.getString("DIVISION");
                cmbData.strCode = trs.getString("DIVISION");
                mcmbdivision.addElement(cmbData);
                trs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
