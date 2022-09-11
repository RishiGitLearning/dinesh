/*
 * SelectFirstFree.java
 *
 * Created on May 7, 2004, 4:11 PM
 */
/*<APPLET CODE=LOV.Class HEIGHT=300 WIDTH=300></APPLT>
 */
/**
 *
 * @author 
 */
package EITLERP.FeltSales.common;

import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.table.*;

public class SelectFirstFree extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();
    public boolean Cancelled = true;
    public String Prefix = "";
    public String Suffix = "";
    public String DocNo = "";
    private JDialog aDialog;
    public int ModuleID = 0;
    public int FirstFreeNo = 0;

    public SelectFirstFree() {
        System.gc();
        initComponents();
    }

    /**
     * Initializes the applet SelectFirstFree
     */
    public void init() {
        System.gc();
        initComponents();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        getContentPane().setLayout(null);

        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("Select the Prefix for document no.");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(12, 12, 246, 16);

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
        Table.setNextFocusableComponent(cmdOK);
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 50, 298, 174);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });
        getContentPane().add(cmdOK);
        cmdOK.setBounds(316, 52, 78, 28);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(316, 88, 78, 28);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jPanel1);
        jPanel1.setBounds(8, 36, 392, 4);
    }// </editor-fold>//GEN-END:initComponents

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (Table.getRowCount() <= 0) {
                Cancelled = true;
            } else {
                Cancelled = false;
                Prefix = (String) DataModel.getValueAt(Table.getSelectedRow(), 0);
                Suffix = (String) DataModel.getValueAt(Table.getSelectedRow(), 2);
                DocNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, Prefix, Suffix, false);
            }
            aDialog.dispose();
        }
    }//GEN-LAST:event_TableMouseClicked

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancelled = true;
        DocNo = "";
        Prefix = "";
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:

        if (Table.getRowCount() <= 0) {
            Cancelled = true;
        } else {
            Cancelled = false;
            Prefix = (String) DataModel.getValueAt(Table.getSelectedRow(), 0);
            Suffix = (String) DataModel.getValueAt(Table.getSelectedRow(), 2);
            FirstFreeNo = Integer.parseInt((String) DataModel.getValueAt(Table.getSelectedRow(), 3));

            DocNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, FirstFreeNo, false);
        }
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
        try {
            if (evt.getKeyCode() == 10) //Enter key pressed
            {
                if (Table.getRowCount() <= 0) {
                    Cancelled = true;
                } else {
                    Cancelled = false;
                    Prefix = (String) DataModel.getValueAt(Table.getSelectedRow(), 0);
                    Suffix = (String) DataModel.getValueAt(Table.getSelectedRow(), 2);
                    FirstFreeNo = Integer.parseInt((String) DataModel.getValueAt(Table.getSelectedRow(), 3));
                    DocNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, FirstFreeNo, false);
                }
                aDialog.dispose();
                return;
            }

            if (evt.getKeyCode() == 27) //Escape key pressed
            {
                Cancelled = true;
                DocNo = "";
                Prefix = "";
                aDialog.dispose();
                return;
            }

            if (evt.getKeyCode() == 40) //Down Arrow key pressed
            {
                if (Table.getSelectedRow() < Table.getRowCount()) {
                    Table.changeSelection(Table.getSelectedRow() + 1, 0, false, false);
                }
                return;
            }

            if (evt.getKeyCode() == 38) //Up Arrow key pressed
            {
                if (Table.getSelectedRow() >= 0) {
                    Table.changeSelection(Table.getSelectedRow() - 1, 0, false, false);
                }

                return;
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_TableKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void GenerateTable() {
        Connection Conn = null;
        Statement stmt = null;
        ResultSet rsTmp;

        //Format the Table
        Table.removeAll();

        Table.setModel(DataModel);
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //Add Columns to it
        DataModel.addColumn("Prefix");
        DataModel.addColumn("Last Used No.");
        DataModel.addColumn("Suffix");
        DataModel.addColumn("FF ID.");

        DataModel.TableReadOnly(true);

        try {
            Conn = data.getConn();
            stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            rsTmp = stmt.executeQuery("SELECT PREFIX_CHARS,LAST_USED_NO,SUFFIX_CHARS,FIRSTFREE_NO FROM D_COM_FIRSTFREE WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + ModuleID + " AND BLOCKED='N'");
            rsTmp.first();

            while (!rsTmp.isAfterLast()) {
                Object[] rowData = new Object[4];

                String Temp = rsTmp.getString("PREFIX_CHARS");
                rowData[0] = rsTmp.getString("PREFIX_CHARS");
                rowData[1] = rsTmp.getString("LAST_USED_NO");
                rowData[2] = rsTmp.getString("SUFFIX_CHARS");
                rowData[3] = Integer.toString(rsTmp.getInt("FIRSTFREE_NO"));

                DataModel.addRow(rowData);
                rsTmp.next();
            }

            Table.changeSelection(0, 0, false, false);
            Table.requestFocus();
            Table.getColumnModel().getColumn(1).setPreferredWidth(210);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());  
        } finally {
            try {
                stmt.close();
                Conn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while (c != null) {
            if (c instanceof Frame) {
                return (Frame) c;
            }

            c = c.getParent();
        }
        return (Frame) null;
    }

    public boolean ShowList() {
        try {
            GenerateTable();

            if (Table.getRowCount() <= 0) {
                Cancelled = true;
                aDialog.dispose();
                return Cancelled;
            } else {
                if (Table.getRowCount() == 1) {
                    Cancelled = false;
                    Prefix = (String) DataModel.getValueAt(Table.getSelectedRow(), 0);
                    Suffix = (String) DataModel.getValueAt(Table.getSelectedRow(), 2);
                    FirstFreeNo = Integer.parseInt((String) DataModel.getValueAt(Table.getSelectedRow(), 3));
                    DocNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, ModuleID, FirstFreeNo, false);
                    aDialog.dispose();
                    return Cancelled;
                }
            }

            setSize(400, 270);

            Frame f = findParentFrame(this);

            aDialog = new JDialog(f, "First Free No.", true);

            aDialog.getContentPane().add("Center", this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);

            //Place it to center of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int) (screenSize.width - appletSize.getWidth()) / 2, (int) (screenSize.height - appletSize.getHeight()) / 2);

            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        } catch (Exception e) {
        }
        return !Cancelled;
    }

}
