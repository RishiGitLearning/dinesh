/*
 * LOV.java
 *
 * Created on April 7, 2004, 5:11 PM
 */


/*
 Sample Code
        LOV aList=new LOV();
 
        aList.SQL="SELECT COMPANY_ID,COMPANY_NAME FROM D_COM_COMPANY_MASTER ORDER BY COMPANY_ID";
        aList.ReturnCol=1;
        aList.ShowReturnCol=false;
        aList.DefaultSearchOn=2;
 
        if(aList.ShowLOV())
        {
         JOptionPane.showMessageDialog(null,"Selected "+aList.ReturnVal);
        }
        else
        {
         JOptionPane.showMessageDialog(null,"Cancelled");
        }
 */


package EITLERP;

/*<APPLET CODE=LOV.Class HEIGHT=300 WIDTH=200></APPLT>
 */

import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.net.*;
import java.awt.Frame;

/**
 *
 * @author  nrpithva
 */

public class LOV_Old extends javax.swing.JApplet {
    
    public String SQL;
    public int ReturnCol;
    public int SecondCol=-1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    
    public boolean Cancelled=true;
    public boolean UseSpecifiedConn=false;
    public String dbURL="";
    
    public String ReturnVal="";
    public String SecondVal="";
    
    private JDialog aDialog;
    
    private EITLTableModel DataModel;
    
    public boolean UseCreatedConn=false;
    
    /** Creates new form LOV */
    public LOV_Old() {
        System.gc();
        initComponents();
        DataModel=new EITLTableModel();
        SQL="";
        ReturnCol=0;
        ShowReturnCol=false;
        DefaultSearchOn=0;
        lblSearch.setDisplayedMnemonic('S');
        lblSearch.setLabelFor((Component) txtSearch);
    }
    
    public LOV_Old(String pSQL,int pReturnCol,boolean pShowReturnCol,int pDefaultSearchOn) {
        System.gc();
        initComponents();
        DataModel=new EITLTableModel();
        SQL=pSQL;
        ReturnCol=pReturnCol;
        ShowReturnCol=pShowReturnCol;
        DefaultSearchOn=pDefaultSearchOn;
        lblSearch.setDisplayedMnemonic('S');
        lblSearch.setLabelFor((Component) txtSearch);
        
    }
    
    public boolean ShowLOV() {
        try {
            GenerateLOV();
            
            setSize(400,350);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"List",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        }
        catch(Exception e) {
        }
        return !Cancelled;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();

        getContentPane().setLayout(null);

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        lblSearch.setText("Search");
        getContentPane().add(lblSearch);
        lblSearch.setBounds(10, 0, 50, 20);

        txtSearch.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                OnKeyPressed(evt);
            }
        });

        getContentPane().add(txtSearch);
        txtSearch.setBounds(10, 20, 380, 19);

        cmdOK.setText("OK");
        cmdOK.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(230, 290, 70, 30);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(310, 290, 70, 30);

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
        Table.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                OnChange(evt);
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OnDblClicked(evt);
            }
        });

        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 50, 380, 230);

    }//GEN-END:initComponents
    
    private void OnDblClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OnDblClicked
        // Add your handling code here:
        
        DefaultSearchOn=Table.getSelectedColumn()+1;
        
        if(evt.getClickCount()==2) {
            if(Table.getRowCount()<=0) {
                Cancelled=true;
            }
            else {
                Cancelled=false;
                ReturnVal=(String) DataModel.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                
                try {
                    if(SecondCol>=0) {
                        SecondVal=(String) DataModel.getValueAt(Table.getSelectedRow(),SecondCol-1);
                    }
                }
                catch(Exception e) {
                }
            }
            aDialog.dispose();
            return;
        }
        
    }//GEN-LAST:event_OnDblClicked
    
    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // Add your handling code here:
        try {
            if(evt.getKeyCode()==10) //Enter key pressed
            {
                if(Table.getRowCount()<=0) {
                    Cancelled=true;
                }
                else {
                    Cancelled=false;
                    ReturnVal=(String) DataModel.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                    
                    try {
                        if(SecondCol>=0) {
                            SecondVal=(String) DataModel.getValueAt(Table.getSelectedRow(),SecondCol-1);
                        }
                    }
                    catch(Exception e) {
                        
                    }
                    
                }
                aDialog.dispose();
                return;
            }
            
            if(evt.getKeyCode()==27) //Escape key pressed
            {
                Cancelled=true;
                ReturnVal="";
                aDialog.dispose();
                return;
            }
            
            if(evt.getKeyCode()==40) //Down Arrow key pressed
            {
                if(Table.getSelectedRow()<Table.getRowCount()) {
                    Table.changeSelection(Table.getSelectedRow()+1,DefaultSearchOn-1,false,false);
                }
                return;
            }
            
            
            if(evt.getKeyCode()==38) //Up Arrow key pressed
            {
                if(Table.getSelectedRow()>=0) {
                    Table.changeSelection(Table.getSelectedRow()-1,DefaultSearchOn-1,false,false);
                }
                
                return;
            }
            
        }
        catch(Exception e)
        {}
    }//GEN-LAST:event_txtSearchKeyPressed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // Add your handling code here:
        Cancelled=true;
        ReturnVal="";
        System.gc();
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void OnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OnKeyPressed
        // Add your handling code here:
        SearchRow(evt.getKeyChar());
    }//GEN-LAST:event_OnKeyPressed
    
    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // Add your handling code here:
        if(Table.getRowCount()<=0) {
            Cancelled=true;
        }
        else {
            Cancelled=false;
            ReturnVal=(String) DataModel.getValueAt(Table.getSelectedRow(),ReturnCol-1);
            
            try {
                if(SecondCol>=0) {
                    SecondVal=(String) DataModel.getValueAt(Table.getSelectedRow(),SecondCol-1);
                }
            }
            catch(Exception e) {
                
            }
            
        }
        System.gc();
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed
    
    private void OnChange(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_OnChange
        // Add your handling code here:
    }//GEN-LAST:event_OnChange
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    
    //Generates Table from the Data
    private void GenerateLOV() {
        try {
            Connection Conn;
            
            if(UseSpecifiedConn) {
                Conn=data.getConn(dbURL);
            }
            else {
                if(UseCreatedConn) {
                    //Conn=data.getCreatedConn();
                    Conn=data.getConn();
                }
                else {
                    Conn=data.getConn();
                }
                
            }
            
            Statement stmt=Conn.createStatement();
            ResultSet rsData=stmt.executeQuery(SQL);
            ResultSetMetaData rsInfo=rsData.getMetaData();
            
            //Format the table from the resultset meta data
            for(int i=1;i<=rsInfo.getColumnCount();i++) {
                DataModel.addColumn(rsInfo.getColumnName(i));
            }
            
            Table.setModel(DataModel);
            Table.setColumnSelectionAllowed(true);
            Table.setRowSelectionAllowed(true);
            DataModel.TableReadOnly(true);
            
            //Now Populate the table
            rsData.first();
            
            while(!rsData.isAfterLast()) {
                Object[] rowData=new Object[rsInfo.getColumnCount()];
                
                //Fillup the array
                for(int i=1;i<=rsInfo.getColumnCount();i++) {
                    switch(rsInfo.getColumnType(i)) {
                        case -5: //Long
                            rowData[i-1]= Long.toString(rsData.getLong(i));
                            break;
                        case 4: //Integer,Small int
                            rowData[i-1]=Integer.toString(rsData.getInt(i));
                            break;
                        case 5: //Integer,Small int
                            rowData[i-1]=Integer.toString(rsData.getInt(i));
                            break;
                        case -6: //Integer,Small int
                            rowData[i-1]=Integer.toString(rsData.getInt(i));
                            break;
                        case 16: //Boolean
                            if(rsData.getBoolean(i)==true) {
                                rowData[i-1]="Yes";
                            }
                            else {
                                rowData[i-1]="No";
                            }
                            break;
                        case 91: //Date
                            rowData[i-1]= EITLERPGLOBAL.formatDate(rsData.getDate(i));
                            break;
                        case 8: //Double
                            rowData[i-1]=Double.toString(rsData.getDouble(i));
                            break;
                        case 6: //Float
                            rowData[i-1]=Float.toString(rsData.getFloat(i));
                            break;
                        case 12 ://Varchar
                            rowData[i-1]=rsData.getString(i);
                            break;
                        default : //Varchar
                            rowData[i-1]=rsData.getString(i);
                            break;
                    } //Switch
                }// for
                
                //Add a row to the table
                DataModel.addRow(rowData);
                
                //Move to the next row
                rsData.next();
            }
            
            TableColumnModel ColModel=Table.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            
            
            Table.changeSelection(0,0,false,false);
            txtSearch.requestFocus();
            
            if(ShowReturnCol==false) {
                ColModel.getColumn(ReturnCol-1).setMinWidth(0);
                ColModel.getColumn(ReturnCol-1).setPreferredWidth(0);
            }
            
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            //e.printStackTrace();
        }
    }
    
    
    private void SearchRow(char RecentKey) {
        //All Columns will have String Data
        String txtData=txtSearch.getText()+RecentKey;
        String txtTableData="";
        
        int Rows=Table.getModel().getRowCount();
        
        if(txtData.equals("")) {
            Table.changeSelection(0,0, false, false);
            return;
        }
        
        //Loop through Table
        for(int i=0;i<Rows;i++) {
            //Read the table data
            txtTableData=(String) Table.getModel().getValueAt(i,DefaultSearchOn-1);
            
            //Compare with partial search
            if(txtData.length()>txtTableData.length()) {
            }
            else {
                if(txtTableData.substring(0,txtData.length()).toLowerCase().equals(txtData.toLowerCase())) {
                    //Move the row pointer to selected row
                    int row = i;
                    int col = DefaultSearchOn-1;
                    boolean toggle = false;
                    boolean extend = false;
                    Table.changeSelection(row, col, toggle, extend);
                    
                    //Exit the loop
                    i=Table.getModel().getRowCount();
                }
            }
            
            
        }
    }
    
    //Recurses through the hierarchy of classes
    //until it finds Frame
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
}
