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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class searchkey_UPNwise extends javax.swing.JApplet {

    /**
     * Initializes the applet searchkey
     */
    public String SQL, MSQL;
    public int ReturnCol;
    public int SecondCol = -1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    public String UPN;
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
    String USER_TYPE="OTHER";
    public boolean UseCreatedConn = false;

    private int mfnd = 0;
    private int mtotcol = 0;
    Connection Conn = null;
    Statement stmt = null;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    HashMap hmPieceList=new HashMap();
    
    public searchkey_UPNwise() {
        System.gc();
        initComponents();
        DataModel = new EITLTableModel();
        SQL = "";
        MSQL = "";
        ReturnCol = 0;
        ShowReturnCol = false;
        DefaultSearchOn = 0;
        txtUPN.setText(UPN);
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

    public searchkey_UPNwise(String pSQL, int pReturnCol, boolean pShowReturnCol, int pDefaultSearchOn) {
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
            java.util.logging.Logger.getLogger(searchkey_UPNwise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(searchkey_UPNwise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(searchkey_UPNwise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(searchkey_UPNwise.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        txtUPN = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

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
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

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
        Table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                TableMouseMoved(evt);
            }
        });
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
        jLabel1.setText("Existing Piece Available for same UPN");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1030, 25);

        txtUPN.setEnabled(false);
        getContentPane().add(txtUPN);
        txtUPN.setBounds(60, 30, 160, 37);

        jLabel2.setText("UPN ");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 37, 100, 20);

        jButton1.setText("RESCHEDULE MONTH");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(810, 340, 200, 30);
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
        //JOptionPane.showMessageDialog(null, ""+Table.getSelectedColumn());
        if(Table.getSelectedColumn()==7)
        {
            String OC_MONTH = DataModel.getValueAt( Table.getSelectedRow(), 8).toString();
            if("".equals(OC_MONTH))
            {
                DataModel.ResetReadOnly(7);
            }
            
        }
        if(Table.getSelectedColumn() == DataModel.getColFromVariable("RESCHEDULE_REQ_MONTH"))
        {
            
            
            String OC_MONTH = DataModel.getValueByVariable("OC_MONTH", Table.getSelectedRow());
            
            if(!OC_MONTH.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Reschedule not possible, because OC Month is is not blank.");
                Table.setValueAt("", Table.getSelectedRow(), 10);
                
            }
        }
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //Obj.clear();
        boolean update_flag = false;
        for(int i=0;i<=DataModel.getColumnCount();i++)
        {
           
            if(!DataModel.getValueAt(i, 0).equals(""))
            {
                //JOptionPane.showMessageDialog(this, "Selected = "+DataModel.getValueAt(i, 0)+" for Piece = "+DataModel.getValueAt(i, 1));
                
                String Piece_No = DataModel.getValueAt(i, 0).toString();
                String PartyCode = DataModel.getValueAt(i, 1).toString();
                String UPN = DataModel.getValueAt(i, 5).toString();
                String PR_REQUESTED_MONTH = DataModel.getValueAt(i, 6).toString();
                String RESCHEDULE_MONTH = DataModel.getValueAt(i, 7).toString();
                String OC_MONTH = DataModel.getValueAt(i, 8).toString();
                
                if("".equals(OC_MONTH)  && !"".equals(RESCHEDULE_MONTH))
                {
                
                    clsPlannerActivities planner = new clsPlannerActivities();
                    planner.setPLANNER_TYPE("RESCHEDULE_MONTH");
                    planner.setPLANNER_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setPIECE_NO(Piece_No);
                    planner.setPARTY_CODE(PartyCode);//
                    planner.setUPN(UPN);//
                    planner.setREQ_MONTH(PR_REQUESTED_MONTH);//
                    planner.setRESCHEDULE_REQ_MONTH(RESCHEDULE_MONTH);//
                    planner.setOC_MONTH("");//
                    planner.setUPDATED_OC_MONTH("");
                    planner.setCURRENT_SCH_MONTH("");//
                    planner.setUPDATED_CURRENT_SCH_MONTH("");
                    planner.setSPECIAL_REQ_MONTH("");//
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setSPECIAL_REQ_MONTH_DATE("");
                    planner.setUPDATED_SPECIAL_REQ_MONTH("");
                    planner.setEXP_WIP_DELIVERY_DATE("");//
                    planner.setUPDATED_EXP_WIP_DELIVERY_DATE("0000-00-00");//
                    planner.setEXP_PI_DATE("0000-00-00");//
                    planner.setUPDATED_EXP_PI_DATE("");
                    planner.setUSER_ID(EITLERPGLOBAL.gUserID+"");
                    planner.setUPDATE_STATUS("PENDING");
                    planner.setSTATUS_UPDATE_DATE(EITLERPGLOBAL.getCurrentDateDB());
                    planner.setEXP_PAY_CHQ_RCV_DATE("0000-00-00");
                    planner.saveTransaction();
                    update_flag = true;
                }
            }
        }
        if(update_flag)
        {
            updateActivitiesTransactions();  
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void TableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMouseMoved
        // TODO add your handling code here:
        
    }//GEN-LAST:event_TableMouseMoved
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
                                    try{
                                                String EXP_WIP_DELIVERY_DATE = rsData.getString("EXP_WIP_DELIVERY_DATE");
                                                String EXP_PI_DATE = rsData.getString("EXP_PI_DATE");
                                                String PARTY_CODE = data.getStringValueFromDB("SELECT PR_PARTY_CODE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                String CHARGE_CODE = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PARTY_CODE+"'");

                                                if(CHARGE_CODE.equals("9") || CHARGE_CODE.equals("09") || CHARGE_CODE.equals("8") || CHARGE_CODE.equals("08"))
                                                {
                                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='"+EXP_WIP_DELIVERY_DATE+"',PR_EXP_PI_DATE='"+EXP_PI_DATE+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                }
                                                else
                                                {
                                                    data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='"+EXP_WIP_DELIVERY_DATE+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
                                                }
                                                
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE PLANNER_ID='"+PLANNER_ID+"' AND PIECE_NO='"+PIECE_NO+"'");
                                    }catch(Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                
                                
//                                                String EXP_WIP_DELIVERY_DATE = rsData.getString("EXP_WIP_DELIVERY_DATE");
//                                                String EXP_PI_DATE = rsData.getString("EXP_PI_DATE");
//                                                
//                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_WIP_DELIVERY_DATE='"+EXP_WIP_DELIVERY_DATE+"',PR_EXP_PI_DATE='"+EXP_PI_DATE+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
//                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES SET UPDATE_STATUS='UPDATED',STATUS_UPDATE_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE PLANNER_ID='"+PLANNER_ID+"' AND PIECE_NO='"+PIECE_NO+"'");
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
                            case "EXP_PAY_CHQ_RCV_DATE":
                                
                                                String EXP_PAY_CHQ_RCV_DATE = rsData.getString("EXP_PAY_CHQ_RCV_DATE");
                                                
                                                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_EXP_PAY_CHQRC_DATE='"+EXP_PAY_CHQ_RCV_DATE+"' WHERE PR_PIECE_NO='"+PIECE_NO+"'");
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
        System.out.println("Date : " + sdf.format(date));
        return sdf.format(date);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField txtUPN;
    // End of variables declaration//GEN-END:variables

    public void setsearchText(String pstxt) {
       
       
    }
    
    public boolean ShowRSLOV() {
        try {
            txtUPN.setText(UPN);
            GenerateLOV();

            int USER_ID = EITLERPGLOBAL.gUserID;
            int DEPT_ID = EITLERPGLOBAL.gUserDeptID;
            System.out.println("USER = "+USER_ID+" DEPT ID = "+DEPT_ID);
            
            if(USER_ID==352 || USER_ID==136 || USER_ID==329 || USER_ID==331 || USER_ID==28)
            {
                USER_TYPE = "SALES";
                jButton1.setEnabled(true);
            }
            
            for(int i=0;i<=DataModel.getColumnCount();i++)
            {
//                if(i!=7)
//                {
                    DataModel.SetReadOnly(i);
//                }
                
            }
            //setSize(930, 600);
            Frame f = findParentFrame(this);

            aDialog = new JDialog(f, "Piece Register List with same UPN", true);
            aDialog.getContentPane().add("Center", this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(1030, 410);
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
            while (!rsData.isAfterLast()) {
                
                Object[] rowData = new Object[rsInfo.getColumnCount()];

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
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            
            Table.changeSelection(0, 0, false, false);
            
            TableColumn dateColumn_OC_MONTH = Table.getColumnModel().getColumn(7);
            JComboBox monthbox = new JComboBox();
            
            String month_name = "";
            java.util.Date date = new java.util.Date();
            int month;
            int year = date.getYear() + 1900;
            
//            if(date.getDate() <= 15)
//            {
//                month = date.getMonth()+2;
//            }
//            else
//            {
//                month = date.getMonth()+3;
//            }
            month = date.getMonth()+3;
            monthbox.addItem("");
            for (int i = 0; i < 40; i++) {
                month = month + 1;

                if (month >= 13) {
                    month = 1;
                    year = year + 1;
                }

                if (month == 1) {
                    month_name = "Jan";
                } else if (month == 2) {
                    month_name = "Feb";
                } else if (month == 3) {
                    month_name = "Mar";
                } else if (month == 4) {
                    month_name = "Apr";
                } else if (month == 5) {
                    month_name = "May";
                } else if (month == 6) {
                    month_name = "Jun";
                } else if (month == 7) {
                    month_name = "Jul";
                } else if (month == 8) {
                    month_name = "Aug";
                } else if (month == 9) {
                    month_name = "Sep";
                } else if (month == 10) {
                    month_name = "Oct";
                } else if (month == 11) {
                    month_name = "Nov";
                } else if (month == 12) {
                    month_name = "Dec";
                }
                monthbox.addItem(month_name + " - " + year);
            }

             //ActionListener cbActionListener =
            
            monthbox.addActionListener( new ActionListener() {//add actionlistner to listen for change
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        //String s = (String) date1.getSelectedItem();//get the selected item
                        //JOptionPane.showMessageDialog(null, "Event on row "+Table.getSelectedRow()+" col "+Table.getSelectedRow());
                        
                        String OC_MONTH = Table.getValueAt(Table.getSelectedRow(), 8).toString();
                        if("".equals(OC_MONTH) && "SALES".equals(USER_TYPE))
                        {
                            
                        }
                        else
                        {
                            Table.setValueAt("", Table.getSelectedRow(), Table.getSelectedColumn());
                        }
                    }
                });
            dateColumn_OC_MONTH.setCellEditor(new DefaultCellEditor(monthbox));
            
            Table.getColumnModel().getColumn(0).setMinWidth(150);
            Table.getColumnModel().getColumn(1).setMinWidth(100);
            Table.getColumnModel().getColumn(2).setMinWidth(100);
            Table.getColumnModel().getColumn(3).setMinWidth(150);
            Table.getColumnModel().getColumn(4).setMinWidth(80);
            Table.getColumnModel().getColumn(5).setMinWidth(80);
            Table.getColumnModel().getColumn(6).setMinWidth(120);
            Table.getColumnModel().getColumn(7).setMinWidth(150);
            Table.getColumnModel().getColumn(8).setMinWidth(130);
            Table.getColumnModel().getColumn(9).setMinWidth(130);
            Table.getColumnModel().getColumn(10).setMinWidth(100);
            Table.getColumnModel().getColumn(11).setMinWidth(100);
            
        } catch (Exception e) {
           // e.printStackTrace();
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
//                        case -5: //Long
//                            rowData[i - 1] = Long.toString(rsData.getLong(i));
//                            break;
//                        case 4: //Integer,Small int
//                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
//                            break;
//                        case 5: //Integer,Small int
//                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
//                            break;
//                        case -6: //Integer,Small int
//                            rowData[i - 1] = Integer.toString(rsData.getInt(i));
//                            break;
//                        case 16: //Boolean
//                            if (rsData.getBoolean(i) == true) {
//                                rowData[i - 1] = "Yes";
//                            } else {
//                                rowData[i - 1] = "No";
//                            }
//                            break;
//                        case 91: //Date
//                            rowData[i - 1] = EITLERPGLOBAL.formatDate(rsData.getDate(i));
//                            break;
//                        case 8: //Double
//                            rowData[i - 1] = Double.toString(rsData.getDouble(i));
//                            break;
//                        case 6: //Float
//                            rowData[i - 1] = Float.toString(rsData.getFloat(i));
//                            break;
//                        case 12://Varchar
//                            rowData[i - 1] = rsData.getString(i);
//                            break;
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
