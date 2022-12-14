/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.MachineDataCapture;

import EITLERP.ComboData;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.data;
import SDMLATTPAY.AdvanceSearch.ATTPAYLOV;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Dharmendra
 */
public class frmMachineDataCaptureZKTeco extends javax.swing.JApplet {

    /**
     * Initializes the frmF6InvAnalysis
     */
    private int EditMode = 0;
    private boolean DoNotEvaluate = false;
    private EITLTableModel DataModel = new EITLTableModel();
    private EITLComboModel modelDept = new EITLComboModel();
    private EITLComboModel modelDevice = new EITLComboModel();

    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    @Override
    public void init() {
        /* Create and display the applet */
        initComponents();
        file1.show(false);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        GenerateDeptCombo();
        GenerateDeviceCombo();
        GenerateReport();

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cmdRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdWhStkExporttoExcel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        file1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        lblEmpNo = new javax.swing.JLabel();
        txtEmpNo = new javax.swing.JTextField();
        lblDeptCmb1 = new javax.swing.JLabel();
        cmbDept = new javax.swing.JComboBox();
        lblDeviceCmb = new javax.swing.JLabel();
        cmbDevice = new javax.swing.JComboBox();

        cmdRefresh.setText("Refresh");
        cmdRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefreshActionPerformed(evt);
            }
        });

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

        cmdWhStkExporttoExcel.setText("Export to Excel");
        cmdWhStkExporttoExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdWhStkExporttoExcelActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 29));
        jPanel1.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Punching Machine Data Capture");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(2, 4, 460, 20);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Date :");

        txtDate = new EITLERP.FeltSales.common.DatePicker.DateTextFieldAdvanceSearch();

        lblEmpNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmpNo.setText("Employee No :");

        txtEmpNo.setToolTipText("Press F1 key for search Employee No");
        txtEmpNo = new EITLERP.JTextFieldHint(new JTextField(),"Search by F1");
        txtEmpNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmpNoFocusLost(evt);
            }
        });
        txtEmpNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmpNoKeyPressed(evt);
            }
        });

        lblDeptCmb1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDeptCmb1.setText("Department :");

        cmbDept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Dept", "WEAVING", "NEEDLING", "FINISHING", "WAREHOUSE", "ENGINEERING" }));
        cmbDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDeptItemStateChanged(evt);
            }
        });

        lblDeviceCmb.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDeviceCmb.setText("Device :");

        cmbDevice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Dept", "WEAVING", "NEEDLING", "FINISHING", "WAREHOUSE", "ENGINEERING" }));
        cmbDevice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDeviceItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDeviceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(cmbDevice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(4, 4, 4)
                                .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEmpNo)
                                .addGap(3, 3, 3)
                                .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDeptCmb1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(cmbDept, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(file1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmdWhStkExporttoExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(cmdRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmpNo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbDept, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDeptCmb1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDeviceCmb, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(file1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(cmdWhStkExporttoExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefreshActionPerformed
        // TODO add your handling code here:
        if (!Validate()) {
            return;
        } else {
            GenerateReport();
        }
    }//GEN-LAST:event_cmdRefreshActionPerformed

    private void cmdWhStkExporttoExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdWhStkExporttoExcelActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table, new File(file1.getSelectedFile().toString() + ".xls"), "Punching Machine Data Capture");

            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmdWhStkExporttoExcelActionPerformed

    private void txtEmpNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmpNoFocusLost
        // TODO add your handling code here:
        if (!txtEmpNo.getText().trim().equals("") && data.IsRecordExist("SELECT * FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText().trim() + "' AND APPROVED=1 AND CANCELED=0")) {
//            txtEmpName.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText() + "'"));
        } else {
            if (!txtEmpNo.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "Employee No doesn't exist/under approval.");
            }
            txtEmpNo.setText("");
//            txtEmpName.setText("");
        }
    }//GEN-LAST:event_txtEmpNoFocusLost

    private void txtEmpNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmpNoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            ATTPAYLOV aList = new ATTPAYLOV();
            aList.SQL = "SELECT PAY_EMP_NO,EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE APPROVED=1 AND CANCELED=0";
            aList.ReturnCol = 1;
            aList.ShowReturnCol = true;
            aList.DefaultSearchOn = 2;
            //aList.DefaultSearchOn=1;

            if (aList.ShowLOV()) {
                txtEmpNo.setText(aList.ReturnVal);
//                txtEmpName.setText(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST WHERE PAY_EMP_NO='" + txtEmpNo.getText() + "'"));
            }
        }
    }//GEN-LAST:event_txtEmpNoKeyPressed

    private void cmbDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDeptItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDeptItemStateChanged

    private void cmbDeviceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDeviceItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDeviceItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JComboBox cmbDevice;
    private javax.swing.JButton cmdRefresh;
    private javax.swing.JButton cmdWhStkExporttoExcel;
    private javax.swing.JFileChooser file1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDeptCmb1;
    private javax.swing.JLabel lblDeviceCmb;
    private javax.swing.JLabel lblEmpNo;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtEmpNo;
    // End of variables declaration//GEN-END:variables

    private void GenerateReport() {
        Connection conn = null;
        ResultSet rsTmp;
        Statement st = null;
        String driver = "com.mysql.jdbc.Driver";
        String strSQL = "";
        String pDate = EITLERPGLOBAL.formatDateDB(txtDate.getText());
        String pEmpNo = txtEmpNo.getText().trim();
        DataModel = new EITLTableModel();
        Table.removeAll();
        Table.setModel(DataModel);
        Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

        try {
            Class.forName(driver).newInstance();
            conn = data.getConn();
            st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

//            strSQL = "SELECT serialno,DEVICENAME,machinesn,CHECKTIME,USERID,EMP_NAME,DESIGNATION,DEPARTMENT,MAIN_CATEGORY,CATEGORY FROM "
            strSQL = "SELECT DEVICENAME AS 'Device ID',DEVICE_DESC AS 'Device Desc',DATE_FORMAT(CHECKTIME, '%d/%m/%Y') AS 'Date',"
                    + "TIME_FORMAT(CHECKTIME, '%T') AS 'Time',USERID AS 'Employee No',EMP_NAME AS 'Employee Name',DESIGNATION AS 'Designation',"
                    + "DEPARTMENT AS 'Department',MAIN_CATEGORY AS 'Category',CATEGORY AS 'Sub Category' FROM "
                    + "(SELECT DEVICENAME,machinesn,CHECKTIME,USERID,EMP_NAME,DESIGNATION,DEPARTMENT,MAIN_CATEGORY,CATEGORY"
                    + ",EMP_DEPARTMENT,DEVICE_DESC FROM "
                    + "(SELECT A.serialno,DEVICENAME,machinesn,USERID,CHECKTIME FROM Ingress.auditdata A,Ingress.device D "
                    //                    + "WHERE month(CHECKTIME) =3 #and machinesn =11\n"
                    + "WHERE DATE(CHECKTIME) = '" + pDate + "' "
                    + "AND A.serialno = D.serialno AND userid !=0 ";

            if (!txtEmpNo.getText().trim().equals("")) {
                strSQL += "AND userid = " + txtEmpNo.getText().trim().substring(3) + " ";
            }

            strSQL += "UNION ALL "
                    + "SELECT id ,device_ip ,device_id ,emp_code ,checktime  FROM "
                    + "(SELECT id,emp_code,DATE_FORMAT(punch_time, '%Y-%m-%d %H:%i:%s') as checktime,terminal_sn,terminal_alias,area_alias "
                    + "FROM ZKTECO.iclock_transaction "
                    + "where DATE(punch_time) = '" + pDate + "' "
                    + "AND emp_code!=0 ";
            
            if (!txtEmpNo.getText().trim().equals("")) {
                strSQL += "AND emp_code = " + txtEmpNo.getText().trim().substring(3) + " ";
            }

            strSQL += ") A "
                    + "LEFT JOIN "
                    + "(SELECT DEVICE_IP,DEVICE_ID,DEVICE_SN FROM SDMLATTPAY.ATT_PUNCHING_MACHINE_MASTER_ZKTECO) B "
                    + "ON terminal_sn=DEVICE_SN ";

//            if (!txtEmpNo.getText().trim().equals("")) {
//                strSQL += "AND emp_code = " + txtEmpNo.getText().trim().substring(3) + " ";
//            }

            strSQL += " ) AS AD "
                    //                    + "AND A.serialno = D.serialno AND userid !=0 AND userid = " + pUserID + "  ) AS AD "
                    + "LEFT JOIN "
                    + "(SELECT PAY_EMP_NO,EMPID,EMP_NAME,DESIGNATION,DEPARTMENT,CATEGORY,MAIN_CATEGORY,EMP_DEPARTMENT FROM "
                    + "(SELECT PAY_EMP_NO,EMPID,EMP_NAME,EMP_DESIGNATION,DESIGNATION,EMP_DEPARTMENT,DEPARTMENT,EMP_CATEGORY,CATEGORY,EMP_MAIN_CATEGORY,MCG.NAME AS MAIN_CATEGORY FROM "
                    + "(SELECT PAY_EMP_NO,EMPID,EMP_NAME,EMP_DESIGNATION,DESIGNATION,EMP_DEPARTMENT,DEPARTMENT,EMP_CATEGORY,CG.NAME AS CATEGORY,EMP_MAIN_CATEGORY FROM "
                    + "(SELECT PAY_EMP_NO,EMPID,EMP_NAME,EMP_DESIGNATION,DS.NAME AS DESIGNATION,EMP_DEPARTMENT,DEPARTMENT,EMP_CATEGORY,EMP_MAIN_CATEGORY FROM "
                    + "(SELECT PAY_EMP_NO,EMPID,EMP_NAME,EMP_DESIGNATION,EMP_DEPARTMENT,D.NAME AS DEPARTMENT,EMP_CATEGORY,EMP_MAIN_CATEGORY FROM "
                    + "(SELECT PAY_EMP_NO,SUBSTRING(PAY_EMP_NO,4,7) AS EMPID,EMP_NAME,EMP_DESIGNATION,EMP_DEPARTMENT,EMP_CATEGORY,EMP_MAIN_CATEGORY FROM SDMLATTPAY.ATTPAY_EMPMST "
                    + "WHERE SUBSTRING(PAY_EMP_NO,1,3) = 'BRD') AS E "
                    + "LEFT JOIN "
                    + "(SELECT * FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER) AS D ON DPTID = EMP_DEPARTMENT) AS ED "
                    + "LEFT JOIN "
                    + "(SELECT * FROM SDMLATTPAY.ATT_DESIGNATION_MASTER) AS DS ON EMP_DESIGNATION = DSGID) AS ED "
                    + "LEFT JOIN "
                    + "(SELECT * FROM SDMLATTPAY.ATT_CATEGORY_MASTER) AS CG ON CTGID = EMP_CATEGORY) AS MM "
                    + "LEFT JOIN "
                    + "(SELECT * FROM SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER) AS MCG ON SECID = EMP_MAIN_CATEGORY) AS EMPMST "
                    + ") AS SPP "
                    + "ON EMPID+0 = USERID "
                    + "LEFT JOIN "
                    + "(SELECT DEVICE_IP,DEVICE_DESC FROM SDMLATTPAY.ATT_PUNCHING_MACHINE_MASTER "
                    + "WHERE DEVICE_IP!='200.0.0.175' "
                    + "UNION ALL "
                    + "SELECT DEVICE_IP,DEVICE_DESC FROM SDMLATTPAY.ATT_PUNCHING_MACHINE_MASTER_ZKTECO) AS PMD ON DEVICE_IP = DEVICENAME "
                    + "WHERE 1=1 ";

            if (!cmbDept.getSelectedItem().toString().trim().equals("Select Dept")) {
                strSQL += "AND EMP_DEPARTMENT IN (SELECT CONVERT(DPTID,CHAR (8)) AS DPTID FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER "
                        + "WHERE TRIM(ERP_DEPT)='" + cmbDept.getSelectedItem() + "' GROUP BY ERP_DEPT) ";
            }

            if (!cmbDevice.getSelectedItem().toString().trim().equals("Select Device")) {
                strSQL += "AND DEVICE_DESC='" + cmbDevice.getSelectedItem() + "' ";
            }

            strSQL += ") AS MAIN "
                    + "ORDER BY CHECKTIME DESC ";

            System.out.println("Query..." + strSQL);
            rsTmp = st.executeQuery(strSQL);

            ResultSetMetaData rsInfo = rsTmp.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel.addColumn(rsInfo.getColumnName(i));
            }
            rsTmp.first();
            System.out.println("Row no." + rsTmp.getRow());
            if (rsTmp.getRow() > 0) {
                while (!rsTmp.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    for (int m = 1; m < i; m++) {
                        rowData[m - 1] = rsTmp.getString(m);
                    }
                    DataModel.addRow(rowData);
                    rsTmp.next();
                }
            }
            DataModel.TableReadOnly(true);
            final TableColumnModel columnModel = Table.getColumnModel();
            for (int column = 0; column < Table.getColumnCount(); column++) {
                int width = 60; // Min width
                for (int row = 0; row < Table.getRowCount(); row++) {
                    TableCellRenderer renderer = Table.getCellRenderer(row, column);
                    Component comp = Table.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 10, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel.getColumn(column).setPreferredWidth(width);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            try {
                st.close();
                conn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

    private boolean Validate() {
        //Form level validations
        if (txtDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter from date");
            return false;
        } else if (!EITLERPGLOBAL.isDate(txtDate.getText())) {
            JOptionPane.showMessageDialog(null, "Invalid Date in DD/MM/YYYY format.");
            return false;
        }
        return true;
    }

    private void GenerateDeptCombo() {
        modelDept = new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(modelDept);

        ComboData aData = new ComboData();
        aData.Text = "Select Dept";
        aData.Code = 0;
        modelDept.addElement(aData);

        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            int counter = 1;
            ResultSet rsTmp = stTmp.executeQuery("SELECT DISTINCT TRIM(ERP_DEPT) AS ERP_DEPT FROM SDMLATTPAY.ATT_DEPARTMENT_MASTER ORDER BY ERP_DEPT ");
            while (rsTmp.next()) {
                aData.Code = counter;
                aData.Text = rsTmp.getString("ERP_DEPT");
                modelDept.addElement(aData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GenerateDeviceCombo() {
        modelDevice = new EITLComboModel();
        cmbDevice.removeAllItems();
        cmbDevice.setModel(modelDevice);

        ComboData aData = new ComboData();
        aData.Text = "Select Device";
        aData.Code = 0;
        modelDevice.addElement(aData);

        try {
            Connection Conn = data.getConn();
            Statement stTmp = Conn.createStatement();
            int counter = 1;
            ResultSet rsTmp = stTmp.executeQuery("SELECT DISTINCT(DEVICE_DESC) AS DEVICE_DESC FROM "
                    + "(SELECT DEVICE_IP,DEVICE_DESC FROM SDMLATTPAY.ATT_PUNCHING_MACHINE_MASTER "
                    + "WHERE DEVICE_IP!='200.0.0.175' "
                    + "UNION ALL "
                    + "SELECT DEVICE_IP,DEVICE_DESC FROM SDMLATTPAY.ATT_PUNCHING_MACHINE_MASTER_ZKTECO) AS A "
                    + "ORDER BY DEVICE_DESC ");
            while (rsTmp.next()) {
                aData.Code = counter;
                aData.Text = rsTmp.getString("DEVICE_DESC");
                modelDevice.addElement(aData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
