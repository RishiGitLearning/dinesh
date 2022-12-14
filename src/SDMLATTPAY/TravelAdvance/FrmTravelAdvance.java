
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.TravelAdvance;

import EITLERP.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Dharmendra PRAJAPATI
 *
 */
public class FrmTravelAdvance extends javax.swing.JApplet {

    private int EditMode = 0;
    private EITLTableModel DataModel;
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign1 = new EITLTableCellRenderer();
    private String DOC_NO = "";

    String seleval = "", seltyp = "", selqlt = "", selshd = "", selpiece = "", selext = "", selinv = "", selsz = "";
    private int mlstrc;
    private String menusele = "";
    public EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    @Override
    public void init() {

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        initComponents();

        try {
            DefaultSettings();
        } catch (SQLException ex) {

        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    public void DefaultSettings() throws SQLException {

        //String data = toString();
        clearFields();
        lblTitle1.setBackground(new Color(0, 102, 153));
        lblTitle1.setForeground(Color.WHITE);
    }

    private void clearFields() {
        FormatGrid();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        file1 = new javax.swing.JFileChooser();
        Tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lblStatus1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnEmpMstETE = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblTitle1 = new javax.swing.JLabel();

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

        lblStatus1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus1.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(lblStatus1);
        lblStatus1.setBounds(10, 420, 920, 30);

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
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 70, 900, 290);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setToolTipText("Add Row");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel1.add(cmdAdd);
        cmdAdd.setBounds(620, 40, 90, 23);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setToolTipText("Remove Selected Row");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jPanel1.add(cmdRemove);
        cmdRemove.setBounds(730, 40, 90, 23);

        jLabel1.setText("Press F1 for EmpCode");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 44, 590, 20);

        btnEmpMstETE.setLabel("Export to Excel");
        btnEmpMstETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpMstETEActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmpMstETE);
        btnEmpMstETE.setBounds(10, 370, 150, 30);

        jButton1.setText("HDFC File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(180, 370, 130, 30);

        Tab.addTab("Travel Advance", jPanel1);

        getContentPane().add(Tab);
        Tab.setBounds(0, 30, 930, 490);

        lblTitle1.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle1.setText("Travel Advance");
        lblTitle1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle1.setOpaque(true);
        getContentPane().add(lblTitle1);
        lblTitle1.setBounds(0, 0, 930, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void TabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabMouseClicked

    }//GEN-LAST:event_TabMouseClicked

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 112) //F1 Key pressed
        {
            if (Table.getSelectedColumn() == 1) {
                LOV aList = new LOV();
                aList.SQL = "SELECT PAY_EMPID,EMP_NAME,BANK_ACCOUNT_NO FROM SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP A "
                        + "WHERE DATE_OF_LEAVING='0000-00-00' ";
                aList.ReturnCol = 1;
                aList.ShowReturnCol = true;
                aList.DefaultSearchOn = 1;

                if (aList.ShowLOV()) {
                    Table.setValueAt(aList.ReturnVal, Table.getSelectedRow(), 1);
                    Table.setValueAt(data.getStringValueFromDB("SELECT EMP_NAME FROM SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP WHERE PAY_EMPID='" + aList.ReturnVal + "'"), Table.getSelectedRow(), 2);
                    Table.setValueAt(data.getStringValueFromDB("SELECT BANK_ACCOUNT_NO FROM SDMLATTPAY.ATTPAY_EMPMST_FROM_SPP WHERE PAY_EMPID='" + aList.ReturnVal + "'"), Table.getSelectedRow(), 3);
                    Table.changeSelection(Table.getSelectedRow(), 4, false, false);
                }
            }

            final TableColumnModel columnModel = Table.getColumnModel();
            for (int column = 0; column < Table.getColumnCount(); column++) {
                int width = 100; // Min width
                for (int row = 0; row < Table.getRowCount(); row++) {
                    TableCellRenderer renderer = Table.getCellRenderer(row, column);
                    Component comp = Table.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width + 1, width);
                }
                if (width > 300) {
                    width = 300;
                }
                columnModel.getColumn(column).setPreferredWidth(width);
            }
        }
    }//GEN-LAST:event_TableKeyPressed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        Object[] rowData = new Object[12];
        rowData[0] = Integer.toString(Table.getRowCount() + 1);
        rowData[1] = "";
        rowData[5] = "TRAVELLING ADVANCE";
        DataModel.addRow(rowData);
        Table.changeSelection(Table.getRowCount() - 1, 1, false, false);
        Table.requestFocus();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        if (Table.getRowCount() > 0) {
            DataModel.removeRow(Table.getSelectedRow());
            RearrangeSrNo();
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void TableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_TableKeyReleased

    private void btnEmpMstETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpMstETEActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);

            exprt.fillData(Table, new File(file1.getSelectedFile().toString() + ".xls"), "Sheet1");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString().trim() + ".xls successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpMstETEActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showSaveDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);
            PrintWriter pw = new PrintWriter(new File(file1.getSelectedFile().toString()));
            StringBuilder sb = new StringBuilder();
            sb.append("Account");
            sb.append(",");
            sb.append("Credit");
            sb.append(",");
            sb.append("Amount");
            sb.append(",");
            sb.append("Narration");
            sb.append("\r\n");
            for (int i = 0; i < Table.getRowCount(); i++) {
                sb.append(Table.getValueAt(i, 3));
                sb.append(",");
                sb.append("C");
                sb.append(",");
                sb.append(Table.getValueAt(i, 4));
                sb.append(",");
                sb.append(Table.getValueAt(i, 5));
                if ((i + 1) < Table.getRowCount()) {
                    sb.append("\r\n");
                }
            }
            pw.write(sb.toString());
            pw.close();
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void FormatGrid() {
        DataModel = new EITLTableModel();
        Table.removeAll();

        Table.setModel(DataModel);
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Add Columns to it
        DataModel.addColumn("Sr.No.");   //0
        DataModel.addColumn("Emp Code");   //1
        DataModel.addColumn("Name");   //2
        DataModel.addColumn("Account No.");   //3
        DataModel.addColumn("Amount");   //4        
        DataModel.addColumn("Narration");   //5
        DataModel.SetReadOnly(0);
        DataModel.SetReadOnly(1);
        DataModel.SetReadOnly(2);
        DataModel.SetReadOnly(3);
        //DataModel.SetReadOnly(4);

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JTable Table;
    private javax.swing.JButton btnEmpMstETE;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JFileChooser file1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblTitle1;
    // End of variables declaration//GEN-END:variables
private void RearrangeSrNo() {
        String mno;
        for (int m = 0; m < Table.getRowCount(); m++) {
            mno = String.valueOf(m + 1);
            Table.setValueAt(mno, m, 0);
        }
    }
}
