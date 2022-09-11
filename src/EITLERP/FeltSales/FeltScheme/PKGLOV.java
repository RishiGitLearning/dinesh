/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltScheme;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.net.*;
import java.awt.Frame;
import EITLERP.*;

/**
 *
 * @author  nrpithva
 */

public class PKGLOV extends javax.swing.JApplet {
    
    public String SQL;
    public int ReturnCol;
    public int SecondCol=-1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    public boolean DefaltOnCode = true;
    public boolean Cancelled=true;
    public boolean UseSpecifiedConn=false;
    public String dbURL="";
    public String pcd="";
    
    public String ReturnVal="";
    public String SecondVal="";
    
    public boolean UseCreatedConn=false;
    private EITLTableModel  DataModelPieceNo;
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer ColumnColor = new EITLTableCellRenderer();
    private EITLTableCellRenderer POColor = new EITLTableCellRenderer();
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    private JDialog aDialog;
    
    private EITLTableModel DataModel;
    
    
    
    /** Creates new form LOV */
    public PKGLOV() {
        System.gc();
        initComponents();
        DataModel=new EITLTableModel();
        SQL="";
        ReturnCol=0;
        ShowReturnCol=false;
        DefaultSearchOn=1;
        lblSearch.setDisplayedMnemonic('S');
        lblSearch.setLabelFor((Component) txtSearch);
    }
    
    public PKGLOV(String pSQL,int pReturnCol,boolean pShowReturnCol,int pDefaultSearchOn) {
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
            //FormatGrid();
            GenerateLOV();
            
            setSize(500,450);
            
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
    private void initComponents() {
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
                if(txtSearch.getText().equals(""))
                {
                    //System.out.println("Default True 1");
                    DefaltOnCode =true;
                }
                txtSearchKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if(txtSearch.getText().equals(""))
                {
                    //System.out.println("Default True 1");
                    DefaltOnCode =true;
                }
                OnKeyPressed(evt);
            }
        });

        getContentPane().add(txtSearch);
        txtSearch.setBounds(10, 20, 480, 19);

        cmdOK.setText("OK");
        cmdOK.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(400, 390, 70, 30);

        cmdCancel.setText("Cancel");
        cmdCancel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(310, 390, 70, 30);

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
        jScrollPane1.setBounds(10, 50, 480, 330);

    }
    
    private void OnDblClicked(java.awt.event.MouseEvent evt) {
        // Add your handling code here:
        
        DefaultSearchOn=Table.getSelectedColumn()+1;
        
        if(evt.getClickCount()==2) {
            if(Table.getRowCount()<=0) {
                Cancelled=true;
            }
            else {
                 Cancelled=false;
               // ReturnVal=(String) Table.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                if((boolean)Table.getValueAt(Table.getSelectedRow(),0))
                    {
                            ReturnVal=ReturnVal+","+(String) Table.getValueAt(Table.getSelectedRow(),1);

                    }            
                try {
                    if(SecondCol>=0) {
                        SecondVal=(String) Table.getValueAt(Table.getSelectedRow(),SecondCol-1);
                    }
                }
                catch(Exception e) {
                }
            }
            aDialog.dispose();
            return;
        }
//        if(Table.getRowCount()<=0) {
//            Cancelled=true;
//        }
//        else {
//            GenerateGrid();
//            Cancelled=false;
//        }
//        aDialog.dispose();
        
    }
    
    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        try {
            if(evt.getKeyCode()==10) //Enter key pressed
            {
                if(Table.getRowCount()<=0) {
                    Cancelled=true;
                }
                else {
                    Cancelled=false;
                    if((boolean)Table.getValueAt(Table.getSelectedRow(),0))
                    {
                            ReturnVal=ReturnVal+","+(String) Table.getValueAt(Table.getSelectedRow(),1);

                    }
                    
                    //ReturnVal=(String) Table.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                    
                    try {
                        if(SecondCol>=0) {
                            SecondVal=(String) Table.getValueAt(Table.getSelectedRow(),SecondCol-1);
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
    }
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        Cancelled=true;
        ReturnVal="";
        System.gc();
        aDialog.dispose();
    }
    
    private void OnKeyPressed(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        
        SearchRow(evt.getKeyChar());
       
    }
    
    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        //GenerateGrid();
       if(Table.getRowCount()<=0) {
            Cancelled=true;
        }
        else {
            Cancelled=false;
            
            int Rows=Table.getModel().getRowCount();
        
            //Loop through Table
            for(int i=0;i<Rows;i++) {
                //Read the table data
             //       txtTableData=(String) Table.getModel().getValueAt(i,DefaultSearchOn-1);
                if((boolean)Table.getValueAt(i,0))
                {
                        ReturnVal=ReturnVal+","+(String) Table.getValueAt(i,1);

                }
            } 
            
            
            try {
                if(SecondCol>=0) {
                    SecondVal=(String) Table.getValueAt(Table.getSelectedRow(),SecondCol-1);
                }
            }
            catch(Exception e) {
                
            }
            
        }

       
        System.gc();
        aDialog.dispose();

    }
    
    private void OnChange(java.awt.event.InputMethodEvent evt) {
        // Add your handling code here:
    }
    
    
    // Variables declaration - do not modify
    private javax.swing.JTable Table;
    private javax.swing.JTable TablePieceNo;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration
    
    
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
            System.out.println("*** LOV Query = "+SQL);
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
            FormatGrid();
            while(!rsData.isAfterLast()) {
                Object[] rowData=new Object[rsInfo.getColumnCount()];
               
                //Fillup the array
                for(int i=1;i<=rsInfo.getColumnCount();i++) {
                    switch(rsInfo.getColumnType(i)) {
                        case 0: //Long
                            rowData[i-1]= false;
                            break;
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
            //GenerateGrid();
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
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>((DefaultTableModel) Table.getModel());
        
        
        if(DefaltOnCode)
        {
            System.out.println(" Key  = "+RecentKey);
            int temp = RecentKey;
            if(temp < 48 || temp > 57)
            {
                System.out.println("Recent Key = "+RecentKey+" Come to FALSE");
                DefaltOnCode = false;
            }
            
        }
        
        if(DefaltOnCode)
        {
            sorter.setRowFilter(RowFilter.regexFilter(txtData,0));
        }
        else
        {   try{
            sorter.setRowFilter(RowFilter.regexFilter("(?i)"+txtData,1));    
            }catch(Exception e)
            {
                System.out.println("Pattern Matching Error");
            }
        }        
        Table.setRowSorter(sorter);
        
        DataModel =(EITLTableModel)sorter.getModel();
        
        Table.setModel(DataModel);
        Table.changeSelection(0,0,false,false);
        //Table.setRowSelectionInterval(0, 0);
        //JOptionPane.showMessageDialog(null, "Called");
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
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        Table.removeAll();
        
        Table.setModel(DataModel);
        TableColumnModel ColModel=Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.addColumn("Select");  //1
        DataModel.addColumn("Piece No."); //2
        DataModel.addColumn("Party Code"); //4
        DataModel.addColumn("Party Name"); //5
        DataModel.addColumn("Order No"); //8
        DataModel.addColumn("Order Date"); //3
        
//        DataModel.addColumn("Charge Code"); //64
              
      
        Rend.setCustomComponent(0,"CheckBox");
        Table.getColumnModel().getColumn(0).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        
        DataModel.SetVariable(0,"");
        DataModel.SetVariable(1,"PIECE_NO"); 
        DataModel.SetVariable(4,"S_ORDER_NO");
        DataModel.SetVariable(5,"S_ORDER_DATE"); 
        DataModel.SetVariable(2,"PARTY_CODE"); 
        DataModel.SetVariable(3,"PARTY_NAME"); 
//        DataModel.SetVariable(6,"CHARGE_CODE"); 
        
        DataModel.TableReadOnly(false);
        
        for(int i=1;i<=7;i++) {
            DataModel.SetReadOnly(i);
        }
}
//     private void GenerateGrid() {
//             // TODO add your handling code here:
//                
//     
//        //if(txtPartyCode.getText().equals(""))
//        //{
//        //} else {
//            //JOptionPane.showMessageDialog(null, "Called 1");
//            ArrayList<FeltPieceRegisterNew> data = new ArrayList<FeltPieceRegisterNew>();
//
//            FeltPieceRegisterServiceImpl service =new FeltPieceRegisterServiceImpl();
//
//            data = service.getByPartyCode(pcd);
//
//            int i=0;
//      
//            for(FeltPieceRegisterNew piece:data)
//            {
//                i++;
//                Object[] rowData =new Object[15];
//                rowData[0] = i;
//                rowData[1] = piece.getPieceNo();
//                rowData[2] = piece.getRcvdMtr();
//                rowData[3] = piece.getRcvdWdth();
//                rowData[4] = "0";
//                rowData[5] = "0";
//                rowData[6] = piece.getStyle();
//                rowData[7] = piece.getProductCode();
//                rowData[8] = piece.getPositionNo();
//                rowData[9] = "";
//                rowData[10] = "";
//                rowData[11] = "";
//                rowData[12] = "";
//                rowData[13] = "";
//
//                DataModelPieceNo.addRow(rowData);
//            }
//        //}
//    }

    
    
}
