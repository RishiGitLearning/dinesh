/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
package EITLERP.FeltSales.OrderConfirmationMain;

import EITLERP.EITLERPGLOBAL;
import EITLERP.EITLTableModel;
import EITLERP.*;
import EITLERP.FeltSales.OrderConfirmation.FrmPieceOC;
import EITLERP.FeltSales.OrderConfirmation.clsPieceOCDetails;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.Frame;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class searchkey_invoiced_partywise extends javax.swing.JApplet {

    /**
     * Initializes the applet searchkey
     */
    public String SQL, MSQL;
    public int ReturnCol;
    public int SecondCol = -1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    public String PARTY_CODE;
    public String QueryCode;
    public boolean Cancelled = true;
    public boolean UseSpecifiedConn = false;
    public String dbURL = "";
    
    public String Order_Group = "";
    public String ReturnVal = "";
    public String SecondVal = "";
    public String SELECTED_MONTH = "";
    private JDialog aDialog;
    HashMap Obj = new HashMap();
    private EITLTableModel DataModel;

    public boolean UseCreatedConn = false;

    private int mfnd = 0;
    private int mtotcol = 0;
    Connection Conn = null;
    Statement stmt = null;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    HashMap hmPieceList=new HashMap();
    private EITLTableCellRenderer Renderer_invoiced = new EITLTableCellRenderer();
    public searchkey_invoiced_partywise() {
        System.gc();
        initComponents();
        DataModel = new EITLTableModel();
        SQL = "";
        MSQL = "";
        ReturnCol = 0;
        ShowReturnCol = false;
        DefaultSearchOn = 0;
        txtPartyCode.setText(PARTY_CODE);
        try {
            if (UseSpecifiedConn) {
                Conn = data.getConn(EITLERPGLOBAL.DatabaseURL_Production);
            } else {
                if (UseCreatedConn) {
                    //Conn=data.getCreatedConn();
                    Conn = data.getConn(EITLERPGLOBAL.DatabaseURL_Production);
                } else {
                    Conn = data.getConn(EITLERPGLOBAL.DatabaseURL_Production);
                }
            }
            stmt = Conn.createStatement();
        } catch (Exception e) {
              System.out.println("Error on connectrion = "+e.getMessage());  
        }
        jLabel1.setForeground(Color.WHITE);
        
    }

    public searchkey_invoiced_partywise(String pSQL, int pReturnCol, boolean pShowReturnCol, int pDefaultSearchOn) {
        System.gc();
        initComponents();
        DataModel = new EITLTableModel();
        SQL = pSQL;
        MSQL = pSQL;
        ReturnCol = pReturnCol;
        ShowReturnCol = pShowReturnCol;
        DefaultSearchOn = pDefaultSearchOn;
        
    }

    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(searchkey_invoiced_partywise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(searchkey_invoiced_partywise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(searchkey_invoiced_partywise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(searchkey_invoiced_partywise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
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

        cmdOK = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lblPartyName = new javax.swing.JLabel();

        cmdOK.setText("CLOSE UPN WISE PIECE LIST");
        cmdOK.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setEnabled(false);

        Table.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
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
        Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMouseClicked(evt);
            }
        });
        Table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(0, 70, 1010, 260);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Invoiced Piece for PARTY");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1030, 25);

        txtPartyCode.setEnabled(false);
        getContentPane().add(txtPartyCode);
        txtPartyCode.setBounds(110, 30, 160, 37);

        jLabel2.setText("PARTY CODE");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 37, 100, 20);
        getContentPane().add(lblPartyName);
        lblPartyName.setBounds(270, 30, 490, 30);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // Add your handling code here:
        
        System.gc();
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed

    private void TableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_TableKeyPressed

    private void TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseClicked
        // TODO add your handling code here:
//        if(evt.getClickCount() == 2)
//        {
//            if (Table.getRowCount() <= 0) {
//                    Cancelled = true;
//                } else {
//                    Cancelled = false;
//                    ReturnVal = (String) DataModel.getValueAt(Table.getSelectedRow(), ReturnCol - 1);
//                     //  SecondVal = (String) DataModel.getValueAt(Table.getSelectedRow(), SecondCol - 1); 
//                            
//                    try {
//                        if (SecondCol >= 0) {
//                            SecondVal = (String) DataModel.getValueAt(Table.getSelectedRow(), SecondCol - 1);
//                        }
//                    } catch (Exception e) {
//                           System.out.println("Error on second value = "+e.getMessage()); 
//                    }
//
//                }
//                aDialog.dispose();
//                return;
//        }
    }//GEN-LAST:event_TableMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 27) //Escape key pressed
        {
            Cancelled = true;
            ReturnVal = "";
            aDialog.dispose();
            return;
        }
    }//GEN-LAST:event_formKeyPressed
    private void updateActivitiesTransactions()
    {   try{
                ResultSet rsData = data.getResult("SELECT * FROM PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES where UPDATE_STATUS='PENDING'");
                if (rsData.getRow() > 0) {
                    rsData.first();
                    while (!rsData.isAfterLast()) {
                        String PLANNER_ID = rsData.getString("PLANNER_ID");
                        String PIECE_NO = rsData.getString("PIECE_NO");
                        
                        switch (rsData.getString("PLANNER_TYPE")) {
                            case "EXP_WIP_DEL_DATE":
                                                
                                                String EXP_WIP_DELIVERY_DATE = rsData.getString("EXP_WIP_DELIVERY_DATE");
                                                String EXP_PI_DATE = rsData.getString("EXP_PI_DATE");
                                                
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='"+EXP_WIP_DELIVERY_DATE+"',PR_EXP_PI_DATE='"+EXP_PI_DATE+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE PLANNER_ID='"+PLANNER_ID+"' AND PIECE_NO='"+PIECE_NO+"'");
                                break;
                            case "RESCHEDULE_MONTH":
                                
                                                String RESCHEDULE_REQ_MONTH = rsData.getString("RESCHEDULE_REQ_MONTH");
                                                
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_REQUESTED_MONTH='"+RESCHEDULE_REQ_MONTH+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE PLANNER_ID='"+PLANNER_ID+"' AND  PIECE_NO='"+PIECE_NO+"'");
                                
                                break;
                            case "SPL_REQUEST_MONTHYEAR":
                                                String SPECIAL_REQ_MONTH = rsData.getString("SPECIAL_REQ_MONTH");
                                                String SPECIAL_REQ_MONTH_DATE = rsData.getString("SPECIAL_REQ_MONTH_DATE");
                                                
                                                if("".equals(SPECIAL_REQ_MONTH))
                                                {
                                                    try{
                                                        java.util.Date date=new SimpleDateFormat("yyyy-MM-dd").parse(SPECIAL_REQ_MONTH_DATE); 
                                                        System.out.println("output month : "+date.getMonth());
                                                        System.out.println("output year : "+date.getYear());
                                                        int month = date.getMonth()+1;
                                                        int year = date.getYear()+1900;
                                                        
                                                        SPECIAL_REQ_MONTH = getMonthName(month) + " - " + year;
                                                    }catch(Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if("".equals(SPECIAL_REQ_MONTH_DATE))
                                                {
                                                    SPECIAL_REQ_MONTH_DATE = LastDayOfReqMonth(SPECIAL_REQ_MONTH_DATE);
                                                }
                                                
                                                
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_SPL_REQUEST_MONTHYEAR='"+SPECIAL_REQ_MONTH+"',PR_SPL_REQUEST_DATE='"+SPECIAL_REQ_MONTH_DATE+"',PR_CURRENT_SCH_MONTH='"+SPECIAL_REQ_MONTH+"',PR_CURRENT_SCH_LAST_DDMMYY='' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE PLANNER_ID='"+PLANNER_ID+"' AND  PIECE_NO='"+PIECE_NO+"'");
                                
                                break;
                        }
                                
                        
                        rsData.next();
                    }
                }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private String LastDayOfReqMonth(String Req_Month)
    {
        int Year = Integer.parseInt(Req_Month.substring(6));
        int Month = 0;
        if(Req_Month.contains("Jan"))
        {    Month = 1; }
        else if(Req_Month.contains("Feb"))
        {    Month = 2; }
        else if(Req_Month.contains("Mar"))
        {    Month = 3; }
        else if(Req_Month.contains("Apr"))
        {    Month = 4; }
        else if(Req_Month.contains("May"))
        {    Month = 5; }
        else if(Req_Month.contains("Jun"))
        {    Month = 6; }
        else if(Req_Month.contains("Jul"))
        {    Month = 7; }
        else if(Req_Month.contains("Aug"))
        {    Month = 8; }
        else if(Req_Month.contains("Sep"))
        {    Month = 9; }
        else if(Req_Month.contains("Oct"))
        {    Month = 10; }
        else if(Req_Month.contains("Nov"))
        {    Month = 11; }
        else if(Req_Month.contains("Dec"))
        {    Month = 12; }
        
        Calendar cal = new GregorianCalendar(Year, Month, 0);
        java.util.Date date = cal.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.println("Date : " + sdf.format(date));
        return sdf.format(date);
    }
    private java.util.Date LastDayOfReqMonthDate(String Req_Month)
    {
        int Year = Integer.parseInt(Req_Month.substring(6));
        int Month = 0;
        if(Req_Month.contains("Jan"))
        {    Month = 1; }
        else if(Req_Month.contains("Feb"))
        {    Month = 2; }
        else if(Req_Month.contains("Mar"))
        {    Month = 3; }
        else if(Req_Month.contains("Apr"))
        {    Month = 4; }
        else if(Req_Month.contains("May"))
        {    Month = 5; }
        else if(Req_Month.contains("Jun"))
        {    Month = 6; }
        else if(Req_Month.contains("Jul"))
        {    Month = 7; }
        else if(Req_Month.contains("Aug"))
        {    Month = 8; }
        else if(Req_Month.contains("Sep"))
        {    Month = 9; }
        else if(Req_Month.contains("Oct"))
        {    Month = 10; }
        else if(Req_Month.contains("Nov"))
        {    Month = 11; }
        else if(Req_Month.contains("Dec"))
        {    Month = 12; }
        
        Calendar cal = new GregorianCalendar(Year, Month, 0);
        return cal.getTime();
        
    }
    private String getMonthName(int month)
    {
                if (month == 1) {
                    return "Jan";
                } else if (month == 2) {
                    return "Feb";
                } else if (month == 3) {
                    return "Mar";
                } else if (month == 4) {
                    return "Apr";
                } else if (month == 5) {
                    return "May";
                } else if (month == 6) {
                    return "Jun";
                } else if (month == 7) {
                    return "Jul";
                } else if (month == 8) {
                    return "Aug";
                } else if (month == 9) {
                    return "Sep";
                } else if (month == 10) {
                    return "Oct";
                } else if (month == 11) {
                    return "Nov";
                } else if (month == 12) {
                    return "Dec";
                }
                else
                {
                    return "";
                }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JButton cmdOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblPartyName;
    public javax.swing.JTextField txtPartyCode;
    // End of variables declaration//GEN-END:variables

    public void setsearchText(String pstxt) {
       
       
    }
    
    public boolean ShowRSLOV() {
        
        try {
            txtPartyCode.setText(PARTY_CODE);
            GenerateLOV();
                    
            for(int i=0;i<=DataModel.getColumnCount();i++)
            {
                    DataModel.SetReadOnly(i);
                    
            }
            
            //setSize(930, 600);
            Frame f = findParentFrame(this);

            aDialog = new JDialog(f, "Piece Register List with same UPN", true);
            aDialog.getContentPane().add("Center", this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(1020, 380);
            aDialog.setResizable(true);
            aDialog.addWindowListener(null);
           
            //Place it to center of the screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation(50, (int) (screenSize.height - appletSize.getHeight()) / 6);

            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
            
        } catch (Exception e) {
        }
        return !Cancelled;
    }

    private void GenerateLOV() {
        try {
            System.out.println("ShowLOV1 SQL = "+SQL); 
            ResultSet rsData = stmt.executeQuery(SQL);
            //System.out.println("Data on rsData = "+rsData.getString(1));
            ResultSetMetaData rsInfo = rsData.getMetaData();

            //Format the table from the resultset meta data
            for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel.addColumn(rsInfo.getColumnName(i));
            }

            Table.setModel(DataModel);
            Table.setColumnSelectionAllowed(true);
            Table.setRowSelectionAllowed(true);
            //DataModel.TableReadOnly(true);

            //Now Populate the table
            rsData.first();
            mtotcol = rsInfo.getColumnCount();
            int row = 0;
            while (!rsData.isAfterLast()) {
                
                Object[] rowData = new Object[rsInfo.getColumnCount()];
                String OC_MONTH_DDMMYY = "";
                String FNSG_DATE = "";
                String DISPATCHED_DATE = "";
                //Fillup the array
                for (int i = 1 ; i <= rsInfo.getColumnCount(); i++) {
                     
                        switch (rsInfo.getColumnType(i)) {
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
                            case 91: //Date
                                rowData[i - 1] = EITLERPGLOBAL.formatDate(rsData.getDate(i));
                                break;
                            case 8: //Double
                                rowData[i - 1] = Double.toString(rsData.getDouble(i));
                                break;
                            case 6: //Float
                                rowData[i - 1] = Float.toString(rsData.getFloat(i));
                                break;

                            default: //Varchar
                                OC_MONTH_DDMMYY = rsData.getString(7);
                                FNSG_DATE = rsData.getString(5);
                                DISPATCHED_DATE = rsData.getString(6);
                                rowData[i - 1] = rsData.getString(i);
                                break;
                        } //Switch
                        
                }// for

                //Add a row to the table
                DataModel.addRow(rowData);
//                System.out.println("OCMONTHDDMMYY = "+OC_MONTH_DDMMYY+" FNSG_DATE = "+FNSG_DATE);
//                java.util.Date date_oc_month=new SimpleDateFormat("yyyy-MM-dd").parse(OC_MONTH_DDMMYY);  
//                java.util.Date date_fnsg =new SimpleDateFormat("yyyy-MM-dd").parse(FNSG_DATE);  
//                //date_fnsg.setMonth(date_fnsg.getMonth()-1);
//                System.out.println("ocmonth date "+date_oc_month+", fnsg date : "+date_fnsg);
//                if(!"0000-00-00".equals(OC_MONTH_DDMMYY))
//                {
//                    if(date_oc_month.before(date_fnsg))
//                    {
//                        Renderer_invoiced.setBackColor(row, 0, Color.PINK);
//                        Renderer_invoiced.setBackColor(row, 1, Color.PINK);
//                        Renderer_invoiced.setBackColor(row, 2, Color.PINK);
//                        Renderer_invoiced.setBackColor(row, 3, Color.PINK);
//                        Renderer_invoiced.setBackColor(row, 4, Color.PINK);
//                        Renderer_invoiced.setBackColor(row, 5, Color.PINK);
//                    }
//                }
                
                            java.util.Date date_oc_month=new SimpleDateFormat("yyyy-MM-dd").parse(OC_MONTH_DDMMYY);  
                            java.util.Date date_fnsg =new SimpleDateFormat("yyyy-MM-dd").parse(FNSG_DATE);  
                            java.util.Date date_dispatch =new SimpleDateFormat("yyyy-MM-dd").parse(DISPATCHED_DATE);
                
                            java.util.Date lastdate_oc_month = null;  
                            java.util.Date lastdate_fnsg = null;  
                            java.util.Date lastdate_dispatch = null;  
                            
                            try{
                                int month = date_oc_month.getMonth()+1;
                                int year = date_oc_month.getYear()+1900;

                                String OC_MTH = getMonthName(month) + " - " + year;
                                lastdate_oc_month = LastDayOfReqMonthDate(OC_MTH);
                                
                                month = date_fnsg.getMonth()+1;
                                year = date_fnsg.getYear()+1900;

                                String OC_FNSG = getMonthName(month) + " - " + year;
                                lastdate_fnsg = LastDayOfReqMonthDate(OC_FNSG);
                                
                                month = date_dispatch.getMonth()+1;
                                year = date_dispatch.getYear()+1900;

                                String DISPATCH_MTH = getMonthName(month) + " - " + year;
                                lastdate_dispatch = LastDayOfReqMonthDate(DISPATCH_MTH);
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                            
                            if(!"0000-00-00".equals(OC_MONTH_DDMMYY))
                            {
                                
                                if(lastdate_oc_month.before(lastdate_fnsg))
                                {
                                    //PINK
                                    Renderer_invoiced.setBackColor(row, 0, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 1, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 2, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 3, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 4, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 5, new Color(237, 165, 181));
                                    Renderer_invoiced.setBackColor(row, 6, new Color(237, 165, 181));
                                }
                                else if(lastdate_oc_month.before(lastdate_dispatch))
                                {
                                    //Blue
                                    Renderer_invoiced.setBackColor(row, 0, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 1, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 2, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 3, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 4, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 5, new Color(114, 144, 223));
                                    Renderer_invoiced.setBackColor(row, 6, new Color(114, 144, 223));
                                }    
                                else
                                {
                                    //GREEN
                                    Renderer_invoiced.setBackColor(row, 0, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 1, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 2, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 3, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 4, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 5, new Color(112, 192, 127));
                                    Renderer_invoiced.setBackColor(row, 6, new Color(112, 192, 127));
                                }
                            }
                            else
                            {
                                    //WHITE
                                    Renderer_invoiced.setBackColor(row, 0, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 1, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 2, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 3, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 4, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 5, new Color(213, 210, 211));
                                    Renderer_invoiced.setBackColor(row, 6, new Color(213, 210, 211));
                            }
                
                row = row + 1;
                //Move to the next row
                rsData.next();
            }

            Table.getColumnModel().getColumn(0).setMinWidth(100);
            Table.getColumnModel().getColumn(1).setMinWidth(150);
            Table.getColumnModel().getColumn(2).setMinWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(150);
            Table.getColumnModel().getColumn(4).setMinWidth(150);
            Table.getColumnModel().getColumn(5).setMinWidth(150);
            Table.getColumnModel().getColumn(6).setMinWidth(0);
            Table.getColumnModel().getColumn(6).setMaxWidth(0);
            
            Table.getColumnModel().getColumn(0).setCellRenderer(this.Renderer_invoiced);
            Table.getColumnModel().getColumn(1).setCellRenderer(this.Renderer_invoiced);
            Table.getColumnModel().getColumn(2).setCellRenderer(this.Renderer_invoiced);
            Table.getColumnModel().getColumn(3).setCellRenderer(this.Renderer_invoiced);
            Table.getColumnModel().getColumn(4).setCellRenderer(this.Renderer_invoiced);
            Table.getColumnModel().getColumn(5).setCellRenderer(this.Renderer_invoiced);
            
            
//            Table.getColumnModel().getColumn(12).setMinWidth(100);
            
//            Table.getColumnModel().getColumn(17).setMinWidth(120);
            
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            e.printStackTrace();
        }
    }

    private void FilterLOV() {
        try {

            ResultSet rsData = stmt.executeQuery(SQL);
            
            ResultSetMetaData rsInfo = rsData.getMetaData();
            //JOptionPane.showMessageDialog(null, "Data Loaded");
            //Format the table from the resultset meta data
            Table.setModel(DataModel);
            Table.setColumnSelectionAllowed(true);
            Table.setRowSelectionAllowed(true);
            DataModel.TableReadOnly(true);

            int rowCount = DataModel.getRowCount();
//Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
                DataModel.removeRow(i);
            }

            //Now Populate the table
            rsData.first();
            
            while (!rsData.isAfterLast()) {
                Object[] rowData = new Object[rsInfo.getColumnCount()];
                 //Fillup the array
                for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
                    switch (rsInfo.getColumnType(i)) {

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

            TableColumnModel ColModel = Table.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

            Table.changeSelection(0, 0, false, false);
        
            if (ShowReturnCol == false) {
                ColModel.getColumn(ReturnCol - 1).setMinWidth(0);
                ColModel.getColumn(ReturnCol - 1).setPreferredWidth(0);
            }

        } catch (Exception e) {
           // JOptionPane.showMessageDialog(null,"Error on filter"+e.getMessage());
            JOptionPane.showMessageDialog(null,"PIECE NOT FOUND! ");
           
            //e.printStackTrace();
        }
    }

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

    public void destroy() {
        try {
            stmt.close();
            Conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
