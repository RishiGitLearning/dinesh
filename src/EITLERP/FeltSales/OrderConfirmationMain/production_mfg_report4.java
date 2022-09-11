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
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.Frame;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class production_mfg_report4 extends javax.swing.JApplet {

    
    /**
     * Initializes the applet searchkey
     */
    public String SQL, MSQL;
    public int ReturnCol;
    public int SecondCol = -1;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    public String Party_Code;
    public String QueryCode;
    public boolean Cancelled = true;
    public boolean UseSpecifiedConn = false;
    public String dbURL = "";
    
    public String Order_Group = "";
    public String ReturnVal = "";
    public String SecondVal = "";

    private JFrame aDialog;

    private EITLTableModel DataModel;

    public boolean UseCreatedConn = false;
    public String report_include_pieces="";
    private int mfnd = 0;
    private int mtotcol = 0;
    Connection Conn = null;
    Statement stmt = null;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    HashMap hmPieceList=new HashMap();
    public String OC_MONTH="";
    public production_mfg_report4() {
        System.gc();
        initComponents();
        DataModel = new EITLTableModel();
        SQL = "";
        MSQL = "";
        ReturnCol = 0;
        ShowReturnCol = false;
        DefaultSearchOn = 0;
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

    public production_mfg_report4(String pSQL, int pReturnCol, boolean pShowReturnCol, int pDefaultSearchOn) {
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
           
        } catch (InstantiationException ex) {
            
        } catch (IllegalAccessException ex) {
            
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            
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
    private void generateReport(String OC_MONTH)
    {
        included_pieces.setText(report_include_pieces);
        String sql = "";
        String cndtn = "";
        String SUB_QRY = "";
        if(!"".equals(report_include_pieces))
        {
            SUB_QRY = " (PR_OC_MONTHYEAR = '" + OC_MONTH + "' OR PR_PIECE_NO IN ("+report_include_pieces+")) ";
        }
        else
        {
            SUB_QRY = " PR_OC_MONTHYEAR = '" + OC_MONTH + "' ";
        }
        data.Execute("DELETE  FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID = " + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'");
        
        String sql1 = "INSERT INTO PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH "
            + "(LOGIN_ID,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION,ACNE_CAPACITY,EXPORT_CAPACITY,NORTH_CAPACITY,EAST_CAPACITY,SOUTH_CAPACITY,KEY_CAPACITY,TOTAL_CAPACITY,OC_MONTH)"
            + " SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)) AS ACNE,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)) AS EXPORT,"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)) AS NORTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)) AS WEST, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)) AS SOUTH, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)) AS KEYCLIENT, "
            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
            + "GROUP BY PRODUCT_CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION"
            + " UNION ALL "
            + "SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,'5'AS PRODUCT_CATEGORY,'5. GRAND TOTAL' AS  PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL ' AS MTR_CAPTION, "
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =5 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =6 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =2 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =3 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =1 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(CASE WHEN INCHARGE_CODE =7 THEN PRODUCT_CAPACITY END,0)),"
            + "SUM(COALESCE(PRODUCT_CAPACITY,0)) AS TOTAL,'" + OC_MONTH + "' AS OC_MONTH "
            + "FROM PRODUCTION.FELT_PP_PRODUCTION_CAPACITY "
            + "WHERE MTR_CAPTION_CODE !=10 " ;
        
        System.out.println("SQL Query:" + sql1);
       
        data.Execute(sql1);
        
        String sql2 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD =5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION, "
+ "MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE !=10 AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID,PR_OC_MONTHYEAR,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7 "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID "
+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";

      System.out.println("SQL Query2:" + sql2);
       
        data.Execute(sql2);
String sql3 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, CATEGORY,PRODUCT_CAPTION,MTR_CAPTION_CODE,MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ "AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE =10 AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID,PR_OC_MONTHYEAR,CATEGORY,PRODUCT_CAPTION,MTR_CAPTION ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7 "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID "
+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH ";
      System.out.println("SQL Query:" + sql3);
       
        data.Execute(sql3);


String sql4 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH AS CAP, "
//+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5' collate utf8_unicode_ci AS CATEGORY,'5. GRAND TOTAL' collate utf8_unicode_ci AS PRODUCT_CAPTION,'11' collate utf8_unicode_ci AS MTR_CAPTION_CODE,'GRAND TOTAL' collate utf8_unicode_ci AS MTR_CAPTION, "
+ "(SELECT LOGIN_ID, PR_OC_MONTHYEAR, '5'  AS CATEGORY,'5. GRAND TOTAL'  AS PRODUCT_CAPTION,'11' AS MTR_CAPTION_CODE,'GRAND TOTAL' AS MTR_CAPTION, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 5 THEN CNT END,0)) AS SC5, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 6 THEN CNT END,0)) AS SC6, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 2 THEN CNT END,0)) AS SC2, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 3 THEN CNT END,0)) AS SC3, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 1 THEN CNT END,0)) AS SC1, "
+ "SUM(COALESCE(CASE WHEN INCHARGE_CD= 7 THEN CNT END,0)) AS SC7  "
//+ "FROM (SELECT '" + EITLERPGLOBAL.gUserID + "' collate utf8_unicode_ci AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "FROM (SELECT " + EITLERPGLOBAL.gUserID + " AS LOGIN_ID,PR_OC_MONTHYEAR,  "
+ "QM.CATEGORY,PRODUCT_CAPTION,MTR_CAPTION,PR_PIECE_NO,PR_LENGTH,1 AS CNT,INCHARGE_CD,MTR_FROM,MTR_TO,PRODUCT_CAPACITY,MTR_CAPTION_CODE "
+ " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR ,DINESHMILLS.D_SAL_PARTY_MASTER PM,PRODUCTION.FELT_PP_PRODUCTION_CAPACITY PC, "
+ "(SELECT CATEGORY,PRODUCT_CODE FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE EFFECTIVE_TO ='0000-00-00' AND APPROVED =1) AS QM  "
+ "WHERE PM.PARTY_CODE = PR.PR_PARTY_CODE AND PR.PR_PRODUCT_CODE = PRODUCT_CODE AND "+SUB_QRY+" "
+ " AND QM.CATEGORY = PRODUCT_CATEGORY AND MTR_FROM <= PR_LENGTH AND MTR_TO >= PR_LENGTH AND MTR_CAPTION_CODE =10 AND INCHARGE_CODE = INCHARGE_CD  "
+ ") AS NG GROUP BY LOGIN_ID,PR_OC_MONTHYEAR ) AS ACT "
+ "SET CAP.ACNE_ACTUAL = ACT.SC5, "
+ "CAP.EXPORT_ACTUAL = ACT.SC6, "
+ "CAP.SOUTH_ACTUAL = ACT.SC1, "
+ "CAP.NORTH_ACTUAL = ACT.SC2, "
+ "CAP.EAST_ACTUAL = ACT.SC3, "
+ "CAP.KEY_ACTUAL = ACT.SC7  "
+ "WHERE ACT.CATEGORY = CAP.CATEGORY "
+ "AND ACT.MTR_CAPTION_CODE = CAP.MTR_CAPTION_CODE "
+ "AND ACT.LOGIN_ID = CAP.LOGIN_ID "
+ "AND ACT.PR_OC_MONTHYEAR = CAP.OC_MONTH" ;

      System.out.println("SQL Query:" + sql4);
      data.Execute(sql4);

      
      String sql5 = "UPDATE PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH SET TOTAL_ACTUAL = ACNE_ACTUAL + EXPORT_ACTUAL + NORTH_ACTUAL + SOUTH_ACTUAL + EAST_ACTUAL + KEY_ACTUAL WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'";

      System.out.println("SQL Query:" + sql5);
       
        data.Execute(sql5);


 //sql = "SELECT * FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH WHERE LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
   sql = "SELECT PRODUCT_CAPTION AS 'PRODUCT', MTR_CAPTION AS 'LENGTH CATEGORY',ACNE_CAPACITY ,ACNE_ACTUAL, EXPORT_CAPACITY ,EXPORT_ACTUAL ,NORTH_CAPACITY ,NORTH_ACTUAL ,EAST_CAPACITY AS 'EAST/WEST_CAPACITY',EAST_ACTUAL AS 'EAST/WEST_ACTUAL' ,SOUTH_CAPACITY,SOUTH_ACTUAL,KEY_CAPACITY AS 'KEYCLIENT_CAPACITY',KEY_ACTUAL AS 'KEYCLIENT_ACTUAL' ,TOTAL_CAPACITY ,TOTAL_ACTUAL ,OC_MONTH FROM PRODUCTION.FELT_PP_TMP_PRODUCTION_CAPACITY_OC_MONTH  WHERE TOTAL_CAPACITY + TOTAL_ACTUAL > 0 AND LOGIN_ID =" + EITLERPGLOBAL.gUserID + " AND OC_MONTH ='" + OC_MONTH + "'ORDER BY PRODUCT_CAPTION,MTR_CAPTION_CODE +0";
      
        SQL = sql;
        System.out.println("SQL Query:" + sql);
        ResultSet rs;
        TableCapacityPlanning.removeAll();
        DataModel = new EITLTableModel();
        TableCapacityPlanning.setModel(DataModel);
        TableCapacityPlanning.setAutoResizeMode(TableCapacityPlanning.AUTO_RESIZE_OFF);
        try {
            rs = EITLERP.data.getResult(sql);
            DataModel.addColumn("Sr.No.");
            ResultSetMetaData rsInfo = rs.getMetaData();

            //Format the table from the resultset meta data
            int i = 1;
            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                DataModel.addColumn(rsInfo.getColumnName(i));
            }

            for (i = 1; i <= rsInfo.getColumnCount(); i++) {
                TableCapacityPlanning.getColumnModel().getColumn(i).setMinWidth(120);
            }
            
            TableCapacityPlanning.getColumnModel().getColumn(0).setMinWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(0).setMaxWidth(50);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMinWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMaxWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMinWidth(200);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMaxWidth(200);
            
            
            rs.first();
            int k = 1;
           
            if (rs.getRow() > 0) {
                while (!rs.isAfterLast()) {
                    Object[] rowData = new Object[100];
                    rowData[0] = k;
                    //GreyTotal = GreyTotal + rs.getDouble("Grey Weight Needling");
                    for (int m = 1; m < i; m++) {
                        rowData[m] = rs.getString(m);
                    }
                    DataModel.addRow(rowData);
                    rs.next();
                    k++;
                }
            }
            DataModel.TableReadOnly(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void AddPiecetoQuery(String PIECE)
    {
        
        if("".equals(report_include_pieces))
        {
            report_include_pieces = PIECE;
        }
        else
        {
            report_include_pieces = report_include_pieces + ",'" + PIECE + "'";
        }
        
        generateReport(OC_MONTH);
    }
    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TableCapacityPlanning = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtOCMONTH = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        included_pieces = new javax.swing.JLabel();

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setEnabled(false);

        TableCapacityPlanning.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        TableCapacityPlanning.setModel(new javax.swing.table.DefaultTableModel(
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
        TableCapacityPlanning.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TableCapacityPlanning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableCapacityPlanningMouseClicked(evt);
            }
        });
        TableCapacityPlanning.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableCapacityPlanningKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TableCapacityPlanning);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 90, 1010, 280);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText("Mfg Plan ProductWise/ AreaWise Quantity  Allocation Monthwise");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 1030, 25);

        txtOCMONTH.setEnabled(false);
        getContentPane().add(txtOCMONTH);
        txtOCMONTH.setBounds(100, 30, 120, 37);

        jLabel2.setText("OC MONTH");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 37, 90, 20);

        jLabel3.setText("Pieces included :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 70, 120, 20);
        getContentPane().add(included_pieces);
        included_pieces.setBounds(130, 70, 890, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void TableCapacityPlanningKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableCapacityPlanningKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_TableCapacityPlanningKeyPressed

    private void TableCapacityPlanningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableCapacityPlanningMouseClicked
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
    }//GEN-LAST:event_TableCapacityPlanningMouseClicked

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TableCapacityPlanning;
    private javax.swing.JLabel included_pieces;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField txtOCMONTH;
    // End of variables declaration//GEN-END:variables

    public void setsearchText(String pstxt) {
       
       
    }
    
    public boolean ShowRSLOV() {
        try {
            //GenerateLOV();
            included_pieces.setText(report_include_pieces);
            generateReport(OC_MONTH);
            //setSize(930, 600);
            txtOCMONTH.setText(OC_MONTH);
            
            Frame f = findParentFrame(this);

            aDialog = new JFrame("Mfg Plan ProductWise/ AreaWise Quantity  Allocation Monthwise");
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

            TableCapacityPlanning.setModel(DataModel);
            TableCapacityPlanning.setColumnSelectionAllowed(true);
            TableCapacityPlanning.setRowSelectionAllowed(true);
            DataModel.TableReadOnly(true);

            //Now Populate the table
            rsData.first();
            mtotcol = rsInfo.getColumnCount();
            while (!rsData.isAfterLast()) {
                Object[] rowData = new Object[rsInfo.getColumnCount()];

                //Fillup the array
                for (int i = 1; i <= rsInfo.getColumnCount(); i++) {
                            
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

            TableColumnModel ColModel = TableCapacityPlanning.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            
            TableCapacityPlanning.changeSelection(0, 0, false, false);
            
            TableCapacityPlanning.getColumnModel().getColumn(0).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(1).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(2).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(3).setMinWidth(130);
            TableCapacityPlanning.getColumnModel().getColumn(4).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(5).setMinWidth(150);
            TableCapacityPlanning.getColumnModel().getColumn(6).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(7).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(8).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(9).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(10).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(11).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(12).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(13).setMinWidth(100);
            TableCapacityPlanning.getColumnModel().getColumn(14).setMinWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(15).setMinWidth(120);
            TableCapacityPlanning.getColumnModel().getColumn(16).setMinWidth(120);
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
            TableCapacityPlanning.setModel(DataModel);
            TableCapacityPlanning.setColumnSelectionAllowed(true);
            TableCapacityPlanning.setRowSelectionAllowed(true);
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

            TableColumnModel ColModel = TableCapacityPlanning.getColumnModel();
            //Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableCapacityPlanning.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

            TableCapacityPlanning.changeSelection(0, 0, false, false);
        

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
