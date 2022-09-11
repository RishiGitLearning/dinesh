/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltScheme;

/**
 *
 * @author Dharmendra
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.net.*;
import java.awt.Frame;
import EITLERP.*;

public class SchemeLOV extends javax.swing.JApplet {

    /**
     * Initializes the applet SchemeLOV
     */
    public String SQL, SQL1, COND, ORDERBY;
    public int ReturnCol;
    public int SecondCol = -1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    public boolean DefaltOnCode = true;
    public boolean Cancelled = true;
    public boolean UseSpecifiedConn = false;
    public String dbURL = "";
    public String pcd = "";

    public String ReturnVal = "";
    public String SecondVal = "";

    public boolean UseCreatedConn = false;
    private EITLTableModel DataModelPieceNo;
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer ColumnColor = new EITLTableCellRenderer();
    private EITLTableCellRenderer POColor = new EITLTableCellRenderer();
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();

    private JDialog aDialog;

    private EITLTableModel DataModel;

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

    }

    public SchemeLOV() {
        System.gc();
        initComponents();
        DataModel = new EITLTableModel();
        SQL = "";
        ReturnCol = 0;
        ShowReturnCol = false;
        DefaultSearchOn = 1;
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        Table1 = new javax.swing.JTable();
        cmdok = new javax.swing.JButton();
        cmdcancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();

        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 200));

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
        jScrollPane3.setViewportView(Table);

        jTabbedPane1.addTab("SELECT PIECE[S]", jScrollPane3);

        Table1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(Table1);

        jTabbedPane2.addTab("SCHEME NOT APPLICABLE PIECE[S]", jScrollPane4);

        cmdok.setText("OK");
        cmdok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdokActionPerformed(evt);
            }
        });

        cmdcancel.setText("CANCEL");
        cmdcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdcancelActionPerformed(evt);
            }
        });

        jLabel1.setText("Piece");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(193, 193, 193)
                                .addComponent(cmdok, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmdcancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(txtSearch))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdok, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdcancel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
        try {
            if (evt.getKeyCode() == 10) //Enter key pressed
            {
                if (Table.getRowCount() <= 0) {
                    Cancelled = true;
                } else {
                    Cancelled = false;
                    if ((boolean) Table.getValueAt(Table.getSelectedRow(), 0)) {
                        ReturnVal = ReturnVal + ",'" + (String) Table.getValueAt(Table.getSelectedRow(), 1)+"'";

                    }

                    //ReturnVal=(String) Table.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                    try {
                        if (SecondCol >= 0) {
                            SecondVal = (String) Table.getValueAt(Table.getSelectedRow(), SecondCol - 1);
                        }
                    } catch (Exception e) {

                    }

                }
                aDialog.dispose();
                return;
            }

            if (evt.getKeyCode() == 27) //Escape key pressed
            {
                Cancelled = true;
                ReturnVal = "";
                aDialog.dispose();
                return;
            }

            if (evt.getKeyCode() == 40) //Down Arrow key pressed
            {
                if (Table.getSelectedRow() < Table.getRowCount()) {
                    Table.changeSelection(Table.getSelectedRow() + 1, DefaultSearchOn - 1, false, false);
                }
                return;
            }

            if (evt.getKeyCode() == 38) //Up Arrow key pressed
            {
                if (Table.getSelectedRow() >= 0) {
                    Table.changeSelection(Table.getSelectedRow() - 1, DefaultSearchOn - 1, false, false);
                }

                return;
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtSearchKeyPressed

    private void cmdcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdcancelActionPerformed
        // TODO add your handling code here:
        Cancelled = true;
        ReturnVal = "";
        System.gc();
        aDialog.dispose();
    }//GEN-LAST:event_cmdcancelActionPerformed

    private void txtSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyTyped
        // TODO add your handling code here:
        SearchRow(evt.getKeyChar());
    }//GEN-LAST:event_txtSearchKeyTyped

    private void cmdokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdokActionPerformed
        // TODO add your handling code here:
        if (Table.getRowCount() <= 0) {
            Cancelled = true;
        } else {
            Cancelled = false;

            //int Rows = Table.getModel().getRowCount();

            //Loop through Table
            //for (int i = 0; i < Rows; i++) {
            for (int i = 0; i < Table.getRowCount(); i++) {
                //Read the table data
                //       txtTableData=(String) Table.getModel().getValueAt(i,DefaultSearchOn-1);
                if ((boolean) Table.getValueAt(i, 0)) {
                    ReturnVal = ReturnVal + ",'" + (String) Table.getValueAt(i, 1)+"'";

                }
            }

            try {
                if (SecondCol >= 0) {
                    SecondVal = (String) Table.getValueAt(Table.getSelectedRow(), SecondCol - 1);
                }
            } catch (Exception e) {

            }

        }

        System.gc();
        aDialog.dispose();
    }//GEN-LAST:event_cmdokActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JTable Table1;
    private javax.swing.JButton cmdcancel;
    private javax.swing.JButton cmdok;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
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

    public boolean ShowLOV() {

        try {
            //FormatGrid();
            GenerateLOV();

            setSize(900, 650);

            Frame f = findParentFrame(this);

            aDialog = new JDialog(f, "List", true);

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

    private void GenerateLOV() {
        try {
            Connection Conn;

            if (UseSpecifiedConn) {
                Conn = data.getConn(dbURL);
            } else {
                if (UseCreatedConn) {
                    //Conn=data.getCreatedConn();
                    Conn = data.getConn();
                } else {
                    Conn = data.getConn();
                }

            }

            Statement stmt = Conn.createStatement();
            System.out.println("*** LOV Query = " + SQL);
            ResultSet rsData = stmt.executeQuery(SQL1);

           ResultSetMetaData rsInfo = rsData.getMetaData();

            FormatGrid();
            //Now Populate the table
            try{
            rsData.first();            
            while (!rsData.isAfterLast()) {
                Object[] rowData = new Object[rsInfo.getColumnCount()];

                //Fillup the array
                for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
                    switch (rsInfo.getColumnType(i)) {
                        case 0: //Long
                            rowData[i - 1] = false;
                            break;
                        case -5: //Long
                            rowData[i - 1] = Long.toString(rsData.getLong(i));
                            break;
                        case 4: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case 5: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case -6: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case 16: //Boolean
                            if (rsData.getBoolean(i) == true) {
                                rowData[i - 1] = "Yes";
                            } else {
                                rowData[i - 1] = "No";
                            }
                            break;
                        case 91: //Date
                            rowData[i - 1] = EITLERPGLOBAL.formatDate(rsData.getDate(i));
                            break;
                        case 8: //Double
                            rowData[i - 1] = Double.toString(rsData.getDouble(i));
                            break;
                        case 6: //Float
                            rowData[i - 1] = Float.toString(rsData.getFloat(i));
                            break;
                        case 12://Varchar
                            rowData[i - 1] = rsData.getString(i);
                            break;
                        default: //Varchar
                            rowData[i - 1] = rsData.getString(i);
                            break;
                    } //Switch
                }// for

                //Add a row to the table
                DataModelPieceNo.addRow(rowData);

                //Move to the next row
                rsData.next();
            }
            
            //GenerateGrid();
            //TableColumnModel ColModel=Table.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            Table1.setAutoResizeMode(Table.AUTO_RESIZE_OFF);
            }catch(Exception e){
                
            }
            try{
             rsData = stmt.executeQuery(SQL);

             rsInfo = rsData.getMetaData();

            //Format the table from the resultset meta data
//            for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
//                DataModel.addColumn(rsInfo.getColumnName(i));
//
//            }
//
//            Table.setModel(DataModel);
//            Table.setColumnSelectionAllowed(true);
//            Table.setRowSelectionAllowed(true);
            //DataModel.TableReadOnly(true);
            //Now Populate the table
            rsData.first();
            
            while (!rsData.isAfterLast()) {
                Object[] rowData = new Object[rsInfo.getColumnCount()];

                //Fillup the array
                for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
                    switch (rsInfo.getColumnType(i)) {
                        case 0: //Long
                            rowData[i - 1] = false;
                            break;
                        case -5: //Long
                            rowData[i - 1] = Long.toString(rsData.getLong(i));
                            break;
                        case 4: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case 5: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case -6: //Integer,Small int
                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
                            break;
                        case 16: //Boolean
                            if (rsData.getBoolean(i) == true) {
                                rowData[i - 1] = "Yes";
                            } else {
                                rowData[i - 1] = "No";
                            }
                            break;
                        case 91: //Date
                            rowData[i - 1] = EITLERPGLOBAL.formatDate(rsData.getDate(i));
                            break;
                        case 8: //Double
                            rowData[i - 1] = Double.toString(rsData.getDouble(i));
                            break;
                        case 6: //Float
                            rowData[i - 1] = Float.toString(rsData.getFloat(i));
                            break;
                        case 12://Varchar
                            rowData[i - 1] = rsData.getString(i);
                            break;
                        default: //Varchar
                            rowData[i - 1] = rsData.getString(i);
                            break;
                    } //Switch
                }// for

                //Add a row to the table
                DataModel.addRow(rowData);

                //Move to the next row
                rsData.next();
            }
            //GenerateGrid();
            //TableColumnModel ColModel=Table.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            Table.setAutoResizeMode(Table.AUTO_RESIZE_OFF);

           

            Table.changeSelection(0, 0, false, false);

            txtSearch.requestFocus();
            }catch(Exception e){
                
            }
            if (ShowReturnCol == false) {
                //ColModel.getColumn(ReturnCol-1).setMinWidth(0);
                //ColModel.getColumn(ReturnCol-1).setPreferredWidth(0);
            }

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            //e.printStackTrace();
        }
    }

    private void SearchRow(char RecentKey) {
        //All Columns will have String Data
        String txtData = txtSearch.getText() + RecentKey;
        String txtTableData = "";
        int Rows = Table.getModel().getRowCount();

        if (txtData.equals("")) {
            Table.changeSelection(0, 0, false, false);
            return;
        }

        //Loop through Table
        for (int i = 0; i < Rows; i++) {
            //Read the table data
            txtTableData = (String) Table.getModel().getValueAt(i, DefaultSearchOn - 1);

            //Compare with partial search
            if (txtData.length() > txtTableData.length()) {
            } else {
                if (txtTableData.substring(0, txtData.length()).toLowerCase().equals(txtData.toLowerCase())) {
                    //Move the row pointer to selected row
                    int row = i;
                    int col = DefaultSearchOn - 1;
                    boolean toggle = false;
                    boolean extend = false;
                    Table.changeSelection(row, col, toggle, extend);

                    //Exit the loop
                    i = Table.getModel().getRowCount();
                }
            }
        }
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>((DefaultTableModel) Table.getModel());

        if (DefaltOnCode) {
            System.out.println(" Key  = " + RecentKey);
            int temp = RecentKey;
            if (temp < 48 || temp > 57) {
                System.out.println("Recent Key = " + RecentKey + " Come to FALSE");
                DefaltOnCode = false;
            }

        }

        if (DefaltOnCode) {
            //sorter.setRowFilter(RowFilter.regexFilter(txtData, 0));
            sorter.setRowFilter(RowFilter.regexFilter(txtData, 1));
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtData, 1));
            } catch (Exception e) {
                System.out.println("Pattern Matching Error");
            }
        }
        Table.setRowSorter(sorter);

        DataModel = (EITLTableModel) sorter.getModel();

        Table.setModel(DataModel);
        Table.changeSelection(0, 0, false, false);
        //Table.setRowSelectionInterval(0, 0);
        //JOptionPane.showMessageDialog(null, "Called");
    }

    private void FormatGrid() {
        DataModel = new EITLTableModel();
        Table.removeAll();

        Table.setModel(DataModel);
        TableColumnModel ColModel = Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModel.addColumn("Select");  //1
        DataModel.addColumn("Piece No."); //2
        DataModel.addColumn("Party Code"); //4
        DataModel.addColumn("Party Name"); //5
        DataModel.addColumn("Product Code"); //5
        DataModel.addColumn("Order No"); //8
        DataModel.addColumn("Order Date"); //3

//        DataModel.addColumn("Charge Code"); //64
        Rend.setCustomComponent(0, "CheckBox");
        Table.getColumnModel().getColumn(0).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        DataModel.SetVariable(0, "");
        DataModel.SetVariable(1, "PIECE_NO");
        DataModel.SetVariable(5, "S_ORDER_NO");
        DataModel.SetVariable(6, "S_ORDER_DATE");
        DataModel.SetVariable(2, "PARTY_CODE");
        DataModel.SetVariable(3, "PARTY_NAME");
        DataModel.SetVariable(4, "PRODUCT_CODE");
//        DataModel.SetVariable(6,"CHARGE_CODE"); 

        DataModel.TableReadOnly(false);

        for (int i = 1; i <= 7; i++) {
            DataModel.SetReadOnly(i);
        }

        DataModelPieceNo = new EITLTableModel();
        Table1.removeAll();

        Table1.setModel(DataModelPieceNo);
        Table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DataModelPieceNo.addColumn("Piece No."); //0
        DataModelPieceNo.addColumn("Party Code"); //1
        DataModelPieceNo.addColumn("Party Name"); //2
        DataModelPieceNo.addColumn("Product Code"); //3
        DataModelPieceNo.addColumn("Order No"); //4
        DataModelPieceNo.addColumn("Order Date"); //5
        DataModelPieceNo.addColumn("Charge Code"); //6
        DataModelPieceNo.addColumn("Previous Disc%"); //7
        DataModelPieceNo.addColumn("Reason"); //8
        
        Table1.getColumnModel().getColumn(8).setMinWidth(800);
    }
}
