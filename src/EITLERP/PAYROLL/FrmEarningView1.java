/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.PAYROLL;

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.data;
import java.io.File;
import java.sql.ResultSet;

/**
 *
 * @author root
 */
public class FrmEarningView1 extends javax.swing.JApplet {

    private EITLComboModel mcmbtype, mcmbcategory, mcmbdept, mcmbdivision;
    /**
     * Initializes the applet FrmEarningView
     */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>

        /* Create and display the applet */
        initComponents();
        setSize(900,600);
        GenerateTypeCombo();
        
    }

    
    
    public void GenerateTypeCombo()
    {
        String sql;
        
        mcmbtype = new EITLComboModel();        
        
        cmbtype.removeAllItems();
        cmbtype.setModel(mcmbtype);
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
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        frmyyyymm = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        toyyyymm = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ExportData = new javax.swing.JButton();
        cmbcompany = new javax.swing.JComboBox();
        cmbdept = new javax.swing.JComboBox();
        cmbtype = new javax.swing.JComboBox();
        cmbdivision = new javax.swing.JComboBox();
        cmbcategory = new javax.swing.JComboBox();

        MainPanel.setLayout(null);

        jLabel1.setText("Company");
        MainPanel.add(jLabel1);
        jLabel1.setBounds(10, 30, 90, 20);

        jLabel8.setText("Department");
        MainPanel.add(jLabel8);
        jLabel8.setBounds(10, 64, 100, 20);

        jLabel4.setText("From");
        MainPanel.add(jLabel4);
        jLabel4.setBounds(10, 124, 40, 20);

        jLabel7.setText("YYYYMM");
        MainPanel.add(jLabel7);
        jLabel7.setBounds(80, 100, 50, 17);
        MainPanel.add(frmyyyymm);
        frmyyyymm.setBounds(70, 120, 70, 30);

        jLabel6.setText("To");
        MainPanel.add(jLabel6);
        jLabel6.setBounds(180, 120, 20, 17);
        MainPanel.add(toyyyymm);
        toyyyymm.setBounds(210, 120, 70, 30);

        jLabel5.setText("YYYYMM");
        MainPanel.add(jLabel5);
        jLabel5.setBounds(220, 100, 50, 17);

        jLabel9.setText("Division");
        MainPanel.add(jLabel9);
        jLabel9.setBounds(230, 60, 50, 17);

        jLabel3.setText("Type");
        MainPanel.add(jLabel3);
        jLabel3.setBounds(250, 30, 40, 20);

        jLabel2.setText("Category");
        MainPanel.add(jLabel2);
        jLabel2.setBounds(440, 30, 70, 20);

        ExportData.setText("EXPORT TO EXCEL");
        ExportData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportDataActionPerformed(evt);
            }
        });
        MainPanel.add(ExportData);
        ExportData.setBounds(10, 420, 160, 40);

        cmbcompany.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MainPanel.add(cmbcompany);
        cmbcompany.setBounds(100, 30, 110, 27);

        cmbdept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MainPanel.add(cmbdept);
        cmbdept.setBounds(100, 60, 110, 27);

        cmbtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MainPanel.add(cmbtype);
        cmbtype.setBounds(300, 30, 130, 27);

        cmbdivision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MainPanel.add(cmbdivision);
        cmbdivision.setBounds(300, 60, 130, 27);

        cmbcategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        MainPanel.add(cmbcategory);
        cmbcategory.setBounds(510, 30, 130, 27);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ExportDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportDataActionPerformed
        // TODO add your handling code here:
        File file = null;
//        file1.setVisible(true);
//        try {
//            int returnVal = file1.showOpenDialog(this);
//            if (returnVal == file1.APPROVE_OPTION) {
//                file = file1.getSelectedFile();
//            }
//
//            //            exp.fillData(TableView, file, "EARNING_DATA");
//            //            JOptionPane.showMessageDialog(null, "Data saved at "
//                //                    + file1.getSelectedFile().toString() + " successfully... ", "Message",
//                //                    JOptionPane.INFORMATION_MESSAGE);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }//GEN-LAST:event_ExportDataActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ExportData;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JComboBox cmbcategory;
    private javax.swing.JComboBox cmbcompany;
    private javax.swing.JComboBox cmbdept;
    private javax.swing.JComboBox cmbdivision;
    private javax.swing.JComboBox cmbtype;
    private javax.swing.JTextField frmyyyymm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField toyyyymm;
    // End of variables declaration//GEN-END:variables
}
