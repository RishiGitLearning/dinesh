/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.IncrementProposal;

import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableCellRenderer;
import EITLERP.EITLTableModel;
import EITLERP.clsAuthority;
import EITLERP.data;
import EITLERP.frmPendingApprovals;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FrmIncrementManagementProposal_old extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbModuleModel;
    private EITLTableModel DataModelApprovalStatus;
    private EITLTableModel DataModelUpdateHistory;

    private EITLTableModel DataModel_EmpDetail;
    private EITLTableModel DataModel_LastIncrementDetail;
    private EITLTableModel DataModel_ProposedIncrement;
    private EITLTableModel DataModel_ProposedIncrementDetail;
    private EITLTableModel DataModel_SalaryDetail;
    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private int FFNo = 363;
    private int ModuleId = 849;
    private String DOC_NO = "";
    private clsIncrementProposal objIncrement;
    private EITLComboModel cmbSendToModel;
    private static int esiclimit = 21001;

    public boolean PENDING_DOCUMENT = false; //for refresh pending document module
    public frmPendingApprovals frmPA;

    String selectedEmpNo = "";
    String selectedEmpName = "";
    String selecteddept = "";
    String selectedcategory = "";
    int datalist_index = 1, selerow = -1;

    /**
     * Initializes the applet FrmFeltOrder
     */
    @Override
    public void init() {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        initComponents();
        lblTitle.setForeground(Color.WHITE);

        FormatGrid();

        DefaultSettings();

        objIncrement = new clsIncrementProposal();
        boolean load = objIncrement.LoadData(" WHERE IED_DOC_NO LIKE '%DG52%'");

        if (load) {
            DisplayData();
        } else {
            JOptionPane.showMessageDialog(this, "Error occured while Loading Data. Error is " + objIncrement.LastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() {

        //String data = toString();
        Object[] rowData = new Object[15];
        rowData[0] = "1";

        clearFields();
    }

    private void clearFields() {
        txtDesignation.setText("");
        FormatGrid();
    }

    private void DisplayData() {
    }

    private void FormatGrid() {
        try {
            DataModel_ProposedIncrementDetail = new EITLTableModel();
            TableIncrementStatus.removeAll();

            TableIncrementStatus.setModel(DataModel_ProposedIncrementDetail);
            TableIncrementStatus.setAutoResizeMode(0);

            DataModel_ProposedIncrementDetail.addColumn("Department"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Emp No"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Emp Name"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Increse in CTC%"); //0 - Read Only
//            DataModel_ProposedIncrementDetail.addColumn("Increse in Montly CTC");
            DataModel_ProposedIncrementDetail.addColumn("Current CTC Amount (Yearly)"); //            
            DataModel_ProposedIncrementDetail.addColumn("Proposal CTC Amount (Yearly)"); // 

            DataModel_ProposedIncrementDetail.addColumn("Current Basic"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Increment Basic"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Proposal Basic"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("Current Personal Pay "); //1
            DataModel_ProposedIncrementDetail.addColumn("Increment Personal Pay "); //1
            DataModel_ProposedIncrementDetail.addColumn("Proposal Personal Pay "); //1
            DataModel_ProposedIncrementDetail.addColumn("DA Rate"); //
            DataModel_ProposedIncrementDetail.addColumn("Current HRA "); //
            DataModel_ProposedIncrementDetail.addColumn("Increment HRA "); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal HRA "); //
            DataModel_ProposedIncrementDetail.addColumn("Award HRA"); //                        
            DataModel_ProposedIncrementDetail.addColumn("Current Magazine Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Increment Magazine Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Magazine Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Current Electricity Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Increment Electricity Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Electricity Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Current Performance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Increment Performance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Performance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Current Conveyance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Increment Conveyance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Conveyance Allowance "); //
            DataModel_ProposedIncrementDetail.addColumn("Current Medical / Education / Hotel %"); //
            DataModel_ProposedIncrementDetail.addColumn("Increment Medical / Education / Hotel %"); //            
            DataModel_ProposedIncrementDetail.addColumn("Current Medical /Education / Hotel Amount "); //                                 
            DataModel_ProposedIncrementDetail.addColumn("Proposal Medical /Education / Hotel Amount "); //            
            DataModel_ProposedIncrementDetail.addColumn("Current Inflation / Bonus"); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Inflation / Bonus"); //
            DataModel_ProposedIncrementDetail.addColumn("Current PF13%"); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal PF13%"); //
            DataModel_ProposedIncrementDetail.addColumn("Current ESIC"); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal ESIC"); //
            DataModel_ProposedIncrementDetail.addColumn("Current Gratuity 4.81%"); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Gratuity 4.81%"); //
            DataModel_ProposedIncrementDetail.addColumn("Petrol Amount"); //            
            DataModel_ProposedIncrementDetail.addColumn("Current Super Annuation Amount"); //
            DataModel_ProposedIncrementDetail.addColumn("Proposal Super Annuation Amount"); //
            DataModel_ProposedIncrementDetail.addColumn("CTC Monthly Current"); //0 - Read Only
            DataModel_ProposedIncrementDetail.addColumn("CTC Monthly Proposal"); //0 - Read Only          
            DataModel_ProposedIncrementDetail.addColumn("Proposal Promotion"); //  

            DataModel_ProposedIncrementDetail.SetVariable(0, "IED_DEPARTMENT"); //0 - Read Only
            DataModel_ProposedIncrementDetail.SetVariable(1, "IED_PAY_EMP_NO"); //0 - Read Only
            DataModel_ProposedIncrementDetail.SetVariable(2, "IED_EMP_NAME"); //0 - Read Only
            DataModel_ProposedIncrementDetail.SetVariable(3, "IED_REVISED_PER");
//            DataModel_ProposedIncrementDetail.SetVariable(4, "IED_DIFF_CTC");
            DataModel_ProposedIncrementDetail.SetVariable(4, "IED_CURRENT_YEARLY_CTC"); //3
            DataModel_ProposedIncrementDetail.SetVariable(5, "IED_REVISED_YEARLY_CTC");

            DataModel_ProposedIncrementDetail.SetVariable(6, "IED_CURRENT_BASIC");
            DataModel_ProposedIncrementDetail.SetVariable(7, "IED_CURRENT_INC_BASIC"); //0 - Read Only
            DataModel_ProposedIncrementDetail.SetVariable(8, "IED_REVISED_BASIC"); //0 - Read Only
            DataModel_ProposedIncrementDetail.SetVariable(9, "IED_CURRENT_PERSONAL_PAY");
            DataModel_ProposedIncrementDetail.SetVariable(10, "IED_CURRENT_INC_PERSONAL_PAY"); //1
            DataModel_ProposedIncrementDetail.SetVariable(11, "IED_REVISED_PERSONAL_PAY"); //1
            DataModel_ProposedIncrementDetail.SetVariable(12, "IED_CURRENT_DA_INDEX");
            DataModel_ProposedIncrementDetail.SetVariable(13, "IED_CURRENT_HRA");
            DataModel_ProposedIncrementDetail.SetVariable(14, "IED_CURRENT_INC_HRA"); //2
            DataModel_ProposedIncrementDetail.SetVariable(15, "IED_REVISED_HRA"); //2
            DataModel_ProposedIncrementDetail.SetVariable(16, "IED_CURRENT_AWARD_HRA"); //3
            DataModel_ProposedIncrementDetail.SetVariable(17, "IED_CURRENT_MAGAZINE");
            DataModel_ProposedIncrementDetail.SetVariable(18, "IED_CURRENT_INC_MAGAZINE");
            DataModel_ProposedIncrementDetail.SetVariable(19, "IED_REVISED_MAGAZINE"); //3
            DataModel_ProposedIncrementDetail.SetVariable(20, "IED_CURRENT_ELECTRICITY");
            DataModel_ProposedIncrementDetail.SetVariable(21, "IED_CURRENT_INC_ELECTRICITY");
            DataModel_ProposedIncrementDetail.SetVariable(22, "IED_REVISED_ELECTRICITY"); //4
            DataModel_ProposedIncrementDetail.SetVariable(23, "IED_CURRENT_PERFORMANCE_ALLOWANCE");
            DataModel_ProposedIncrementDetail.SetVariable(24, "IED_CURRENT_INC_PERFORMANCE_ALLOWANCE");
            DataModel_ProposedIncrementDetail.SetVariable(25, "IED_REVISED_PERFORMANCE_ALLOWANCE"); //3
            DataModel_ProposedIncrementDetail.SetVariable(26, "IED_CURRENT_CONVEY_ALLOWANCE");
            DataModel_ProposedIncrementDetail.SetVariable(27, "IED_CURRENT_INC_CONVEY_ALLOWANCE");
            DataModel_ProposedIncrementDetail.SetVariable(28, "IED_REVISED_CONVEY_ALLOWANCE"); //1
            DataModel_ProposedIncrementDetail.SetVariable(29, "IED_CURRENT_MEDICAL_PER");
            DataModel_ProposedIncrementDetail.SetVariable(30, "IED_REVISED_MEDICAL_PER"); //2
            DataModel_ProposedIncrementDetail.SetVariable(31, "IED_CURRENT_MEDICAL_AMOUNT");
            DataModel_ProposedIncrementDetail.SetVariable(32, "IED_REVISED_MEDICAL_AMOUNT"); //2
            DataModel_ProposedIncrementDetail.SetVariable(33, "IED_CURRENT_INFLATION_BONUS_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(34, "IED_REVISED_INFLATION_BONUS_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(35, "IED_CURRENT_PF_AMOUNT");
            DataModel_ProposedIncrementDetail.SetVariable(36, "IED_REVISED_PF_AMOUNT"); //4
            DataModel_ProposedIncrementDetail.SetVariable(37, "IED_CURRENT_ESIC_AMOUNT");
            DataModel_ProposedIncrementDetail.SetVariable(38, "IED_REVISED_ESIC_AMOUNT");
            DataModel_ProposedIncrementDetail.SetVariable(39, "IED_CURRENT_GRATUITY_AMOUNT");
            DataModel_ProposedIncrementDetail.SetVariable(40, "IED_REVISED_GRATUITY_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(41, "IED_CURRENT_PETROL_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(42, "IED_CURRENT_SUPER_ANNUATION_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(43, "IED_REVISED_SUPER_ANNUATION_AMOUNT"); //3
            DataModel_ProposedIncrementDetail.SetVariable(44, "IED_CURRENT_MONTHLY_CTC");
            DataModel_ProposedIncrementDetail.SetVariable(45, "IED_REVISED_MONTHLY_CTC");//3
            DataModel_ProposedIncrementDetail.SetVariable(46, "IED_PROPOSE_PROMOTION");

            for (int i = 0; i <= 46; i++) {
                DataModel_ProposedIncrementDetail.SetReadOnly(i);
            }
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            int ImportCol = DataModel_ProposedIncrementDetail.getColFromVariable("IED_PROPOSE_PROMOTION");
            Renderer.setCustomComponent(ImportCol, "CheckBox");
            JCheckBox aCheckBox = new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            aCheckBox.setVisible(true);
            aCheckBox.setEnabled(true);
            aCheckBox.setSelected(false);
            TableIncrementStatus.getColumnModel().getColumn(ImportCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableIncrementStatus.getColumnModel().getColumn(ImportCol).setCellRenderer(Renderer);
//            Desktop desktop = Desktop.getDesktop();  
//	          
//	            File f = new File( DIR + txtEmpNo.getText()  +  ".doc");
//	             desktop.open(f);  // opens application (MSWord) associated with .doc file

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDesignation = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableIncrementStatus = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtDept = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        orderby = new javax.swing.JComboBox();
        cmdview = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        lblTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatus1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        ltbPink = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        Tab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        Tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabMouseClicked(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        jLabel2.setText("Designation");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(200, 10, 90, 30);

        txtDesignation.setEditable(false);
        jPanel1.add(txtDesignation);
        txtDesignation.setBounds(290, 10, 100, 30);

        jLabel1.setText("Employees Increment Details");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 50, 230, 14);

        TableIncrementStatus.setModel(new javax.swing.table.DefaultTableModel(
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
        TableIncrementStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableIncrementStatusMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableIncrementStatus);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 70, 1240, 480);

        jLabel3.setText("Departmet");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 10, 90, 30);

        txtDept.setEditable(false);
        jPanel1.add(txtDept);
        txtDept.setBounds(80, 10, 100, 30);

        jLabel4.setText("Order By");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(410, 10, 90, 30);

        orderby.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Employee", "Department", "Designation" }));
        jPanel1.add(orderby);
        orderby.setBounds(490, 10, 150, 30);

        cmdview.setText("View");
        cmdview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdviewActionPerformed(evt);
            }
        });
        jPanel1.add(cmdview);
        cmdview.setBounds(670, 10, 130, 30);

        Tab.addTab("Employee Increment by Management", jPanel1);

        jPanel8.setLayout(null);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("*Calculate basis on [Current Basic+Current Personal Pay+Proposal Basic+Proposal Personal Pay]\n*Not appilcable for BRD STAFF WITH DA\n\nHRA\t\t\t\tELECTRICITY\t\t\tMAGAZINE\n\n>18500\t4500\t\t\t>8100\t500\t\t\t>6000\t300\n>16500\t4000\t\t\t>7100\t400\t\t\t>5000\t200\n>14500\t3700\t\t\t>6100\t300\t\t\t>4000\t100\n>12500\t3400\t\t\t>5100\t200\t\t\t>2999\t 50\n>10500\t3100\t\t\t>4100\t150\n>9500\t2800\t\t\t>3299\t100\n>8500\t2500\n>7500\t2300\n>6500\t2000\n>5500\t1600\n>4500\t1200\n>4000\t 800\n>3500\t 600\n>3000\t 500\n>2500\t 400\n>2000\t 300\n>1599\t 200\n");
        jScrollPane8.setViewportView(jTextArea1);

        jPanel8.add(jScrollPane8);
        jScrollPane8.setBounds(10, 0, 920, 570);

        Tab.addTab("Rules", jPanel8);

        getContentPane().add(Tab);
        Tab.setBounds(0, 30, 1270, 640);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("SDML Employee Increment by Management");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 0, 1180, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 580, 930, 22);

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus1);
        lblStatus1.setBounds(20, 590, 740, 30);
        getContentPane().add(jPanel5);
        jPanel5.setBounds(210, 50, 10, 10);
        getContentPane().add(ltbPink);
        ltbPink.setBounds(10, 70, 0, 0);
    }// </editor-fold>//GEN-END:initComponents


    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked

    }//GEN-LAST:event_TabMouseClicked


    private void TableIncrementStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableIncrementStatusMouseClicked
        // TODO add your handling code here:     
        selerow = TableIncrementStatus.getSelectedRow();
    }//GEN-LAST:event_TableIncrementStatusMouseClicked

    private void cmdviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdviewActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "";
            ResultSet v;
            sql = "SELECT USER_NAME,D.*,MC.NAME AS MAIN_CATEGORY,SC.NAME AS SUB_CATEGORY "
                    + "FROM SDMLATTPAY.INCREMENT_MANAGEMENT_ENTRY_DETAIL D "
                    + "LEFT JOIN (SELECT USER_ID,USER_NAME FROM DINESHMILLS.D_COM_USER_MASTER) AS UM "
                    + "ON D.IED_UPDATE_BY=USER_ID  "
                    + "LEFT JOIN SDMLATTPAY.ATT_MAIN_CATEGORY_MASTER MC ON SECID=IED_MAIN_CATEGORY "
                    + "LEFT JOIN SDMLATTPAY.ATT_CATEGORY_MASTER SC ON CTGID=IED_CATEGORY  "
                    + "GROUP BY IED_PAY_EMP_NO "
                    + "ORDER BY IED_PAY_EMP_NO,IED_SR_NO";
            v = data.getResult(sql);
            v.first();
            int srno = 0;
            String empno = "";
            if (v.getRow() > 0) {
                while (!v.isAfterLast()) {
                    int r = 0;
                    if (!empno.equalsIgnoreCase(v.getString("IED_PAY_EMP_NO"))) {
                        empno = v.getString("IED_PAY_EMP_NO");
                        srno++;
                        r = 0;
                        Object[] rowData = new Object[100];
                        rowData[r] = srno;
                        r++;
                        rowData[r] = v.getString("IED_DEPARTMENT");
                        r++;
                        rowData[r] = v.getString("IED_PAY_EMP_NO");
                        r++;
                        rowData[r] = v.getString("IED_EMP_NAME");
                        r++;
                        rowData[r] = EITLERPGLOBAL.formatDate(v.getString("IED_EMP_DATE_OF_JOING"));
                        r++;
                        rowData[r] = EITLERPGLOBAL.formatDate(v.getString("IED_LAST_INC_DUE_DATE"));
                        r++;
                        rowData[r] = v.getString("IED_DESIGNATION");
                        r++;
                        rowData[r] = v.getString("IED_QUALIFICATION");
                        r++;
                        rowData[r] = v.getString("MAIN_CATEGORY");
                        r++;
                        rowData[r] = v.getString("SUB_CATEGORY");
                        r++;
                        DataModel_ProposedIncrementDetail.addRow(rowData);
                    }

                    r = 0;
                    Object[] rowData = new Object[100];
                    rowData[r] = v.getString("USER_NAME");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_BASIC");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_PERSONAL_PAY");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_HRA");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_MAGAZINE");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_ELECTRICITY");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_PERFORMANCE_ALLOWANCE");
                    r++;
                    rowData[r] = v.getDouble("IED_CURRENT_INC_CONVEY_ALLOWANCE");
                    r++;
                    rowData[r] = v.getBoolean("IED_PROPOSE_PROMOTION");
                    r++;
                    DataModel_ProposedIncrementDetail.addRow(rowData);
                    v.next();

                }
                final TableColumnModel columnModel2 = TableIncrementStatus.getColumnModel();
                for (int column = 0; column < TableIncrementStatus.getColumnCount(); column++) {
                    int width = 100; // Min width
                    for (int row = 0; row < TableIncrementStatus.getRowCount(); row++) {
                        TableCellRenderer renderer = TableIncrementStatus.getCellRenderer(row, column);
                        Component comp = TableIncrementStatus.prepareRenderer(renderer, row, column);
                        width = Math.max(comp.getPreferredSize().width + 10, width);
                    }
                    if (width > 300) {
                        width = 300;
                    }
                    columnModel2.getColumn(column).setPreferredWidth(width);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdviewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable TableIncrementStatus;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel ltbPink;
    private javax.swing.JComboBox orderby;
    private javax.swing.JFormattedTextField txtDept;
    private javax.swing.JFormattedTextField txtDesignation;
    // End of variables declaration//GEN-END:variables

}
