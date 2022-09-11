package EITLERP;

import EITLERP.*;
import EITLERP.Finance.*;
import TReportWriter.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import java.lang.*;
import javax.swing.text.*;
import java.sql.*;
import java.lang.String;
import java.net.*;
import java.io.*;
import java.math.*;
import java.math.BigDecimal;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

public class frmStockReport extends javax.swing.JApplet {

    private int EditMode = 0;

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbItemTypeModel;

    private EITLTableModel DataModelRate;
    private EITLTableModel DataModelDesc;
    private EITLTableModel DataModelInvoice;
    private EITLTableModel DataModelDiscount;
    private EITLTableModel DataModelA;
    private EITLTableModel DataModelHS;
    private EITLTableModel DataModelOtherpartyDiscount;
    private TReportEngine objEngine = new TReportEngine();

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();

    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private EITLComboModel cmbPriorityModel;

    private boolean HistoryView = false;
    private String theDocNo = "", minvdt;
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "", mdocnm = "";
    String mfilenm;
    private boolean chk = true;

    public frmStockReport() {

        System.gc();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int scrwidth = gd.getDisplayMode().getWidth();
        int scrheight = gd.getDisplayMode().getHeight();
        setSize(scrwidth, scrheight);
        initComponents();
        file1.setVisible(false);
        FormatGrid();
    }


    private void initComponents() {//GEN-BEGIN:initComponents
        btn_grp_hierarchy = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        btn_grp_type = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lbltot = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableRate = new javax.swing.JTable();
        import_data = new javax.swing.JButton();
        file1 = new javax.swing.JFileChooser();
        lbltitle = new javax.swing.JLabel();
        txt_title = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("Stock Import For Suiting Dept");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

        Tab1.setLayout(null);

        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Tab1.add(jButton1);
        jButton1.setBounds(650, 370, 140, 40);

        lbltot.setFont(new java.awt.Font("Verdana", 1, 14));
        lbltot.setEnabled(false);
        Tab1.add(lbltot);
        lbltot.setBounds(630, 290, 80, 30);

        TableRate.setFont(new java.awt.Font("Verdana", 1, 14));
        TableRate.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableRate);

        Tab1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 790, 290);

        import_data.setFont(new java.awt.Font("Verdana", 1, 14));
        import_data.setText("Import Excel Data");
        import_data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                import_dataActionPerformed(evt);
            }
        });

        Tab1.add(import_data);
        import_data.setBounds(10, 370, 190, 40);

        Tab1.add(file1);
        file1.setBounds(120, 0, 506, 326);

        lbltitle.setText("TITLE :");
        Tab1.add(lbltitle);
        lbltitle.setBounds(45, 320, 60, 30);

        txt_title.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        Tab1.add(txt_title);
        txt_title.setBounds(110, 310, 640, 50);

        jTabbedPane1.addTab("Suiting Stock ", Tab1);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 70, 810, 470);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14));
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 550, 610, 20);

    }//GEN-END:initComponents

    private void import_dataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_import_dataActionPerformed
        // TODO add your handling code here:
        try {
            import_data();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_import_dataActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (txt_title.getText().length() > 0) {
            try {
                TReportWriter.SimpleDataProvider.TRow objRow;
                TReportWriter.SimpleDataProvider.TTable objReportData = new TReportWriter.SimpleDataProvider.TTable();

                objReportData.AddColumn("ITEM_CODE");
                objReportData.AddColumn("REFERENCE");
                objReportData.AddColumn("UNIT");
                objReportData.AddColumn("METERS");
                objReportData.AddColumn("KGS");
                objReportData.AddColumn("NOS");
                objReportData.AddColumn("COST");
                objReportData.AddColumn("NRV");
                objReportData.AddColumn("RATE");
                objReportData.AddColumn("VALUE");
            //objReportData.AddColumn("TTL_EXC");

                TReportWriter.SimpleDataProvider.TRow objOpeningRow = objReportData.newRow();

                objOpeningRow.setValue("ITEM_CODE", "");
                objOpeningRow.setValue("REFERENCE", "");
                objOpeningRow.setValue("UNIT", "");
                objOpeningRow.setValue("METERS", "");
                objOpeningRow.setValue("KGS", "");
                objOpeningRow.setValue("NOS", "");
                objOpeningRow.setValue("COST", "");
                objOpeningRow.setValue("NRV", "");
                objOpeningRow.setValue("RATE", "");
                objOpeningRow.setValue("VALUE", "");
            //objOpeningRow.setValue("TTL_EXC","");

                // String strSQL = "SELECT * FROM SALES.D_CREDIT_NOTE_DETAIL1 WHERE CND_TYPE='YLYINC' AND CND_INVOICE_DATE>='"+ EITLERPGLOBAL.formatDateDB(txtFromDate.getText())+"' AND CND_INVOICE_DATE<='"+ EITLERPGLOBAL.formatDateDB(txtToDate.getText())+"'";
                String strSQL = "SELECT * FROM Test.TMP_IMPORT_MIS_STG_STOCK";
                System.out.println(strSQL);

                ResultSet rsTmp = data.getResult(strSQL);
                rsTmp.first();

                int Counter = 0;

                if (rsTmp.getRow() > 0) {
                    while (!rsTmp.isAfterLast()) {
                        Counter++;
                        objRow = objReportData.newRow();

                        objRow.setValue("ITEM_CODE", UtilFunctions.getString(rsTmp, "ITEM_CODE", ""));
                        objRow.setValue("REFERENCE", UtilFunctions.getString(rsTmp, "REFERENCE", ""));
                        objRow.setValue("UNIT", UtilFunctions.getString(rsTmp, "UNIT", ""));
                        objRow.setValue("METERS", UtilFunctions.getString(rsTmp, "METERS", ""));
                        objRow.setValue("KGS", UtilFunctions.getString(rsTmp, "KGS", ""));
                        objRow.setValue("NOS", UtilFunctions.getString(rsTmp, "NOS", ""));
                        objRow.setValue("COST", UtilFunctions.getString(rsTmp, "COST", ""));
                        objRow.setValue("NRV", UtilFunctions.getString(rsTmp, "NRV", ""));
                        objRow.setValue("RATE", UtilFunctions.getString(rsTmp, "RATE", ""));
                        objRow.setValue("VALUE", UtilFunctions.getString(rsTmp, "VALUE", ""));
                    //objRow.setValue("TTL_EXC",UtilFunctions.getString(rsTmp,"TTL_EXC",""));

                        objReportData.AddRow(objRow);

                        rsTmp.next();
                    }
                }

                int Comp_ID = EITLERPGLOBAL.gCompanyID;
                HashMap Parameters = new HashMap();

            // Parameters.put("CND_SUB_ACCOUNT_CODE",txtPartyCode.getText().toString());
                // Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                //Parameters.put("FROM_DATE",txtFromDate.getText().trim());
                // Parameters.put("TO_DATE",txtToDate.getText().trim());
                Parameters.put("SYS_DATE", EITLERPGLOBAL.getCurrentDate());
                Parameters.put("TITLE", txt_title.getText().trim());

                objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/Reports/rptmis.rpt", Parameters, objReportData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please Enter Title to be Print.");
            txt_title.requestFocus();
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableRate;
    private javax.swing.ButtonGroup btn_grp_hierarchy;
    private javax.swing.ButtonGroup btn_grp_type;
    private javax.swing.JFileChooser file1;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton import_data;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbltitle;
    private javax.swing.JLabel lbltot;
    private javax.swing.JTextField txt_title;
    // End of variables declaration//GEN-END:variables

    private void disp_import_data() {

        Connection Conn = null;
        Statement stmt = null;
        try {
            Conn = data.getConn();
            stmt = Conn.createStatement();
            ResultSet rsData = stmt.executeQuery("SELECT * FROM Test.TMP_IMPORT_MIS_STG_STOCK ");

            rsData.first();
            int i = 0;
            String mdata;
            while (!rsData.isAfterLast()) {

                Object[] rowDataRate = new Object[30];
                rowDataRate[0] = String.valueOf(i + 1);
                rowDataRate[1] = rsData.getString("ITEM_CODE");
                rowDataRate[2] = rsData.getString("REFERENCE");
                rowDataRate[3] = rsData.getString("UNIT");
                rowDataRate[4] = rsData.getString("METERS");
                rowDataRate[5] = rsData.getString("KGS");
                rowDataRate[6] = rsData.getString("NOS");
                rowDataRate[7] = rsData.getString("COST");
                rowDataRate[8] = rsData.getString("NRV");
                rowDataRate[9] = rsData.getString("RATE");
                rowDataRate[10] = rsData.getString("VALUE");
                rowDataRate[11] = rsData.getString("TTL_EXC");

                DataModelRate.addRow(rowDataRate);
                i = i + 1;
                rsData.next();
            }
            TableRate.changeSelection(0, 0, false, false);
            TableRate.requestFocus();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in Load Suiting Stock");
        } finally {
            try {
                stmt.close();
                Conn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

    private void FormatGrid() {
        try {
            DataModelRate = new EITLTableModel();
            TableRate.removeAll();
            TableRate.setModel(DataModelRate);
            TableColumnModel ColModel = TableRate.getColumnModel();
            TableRate.setAutoResizeMode(TableRate.AUTO_RESIZE_OFF);
            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.BLUE);

            DataModelRate.addColumn("SR No."); //0
            DataModelRate.addColumn("ITEM_CODE"); //0
            DataModelRate.addColumn("REFERENCE"); //1
            DataModelRate.addColumn("UNIT");//2            
            DataModelRate.addColumn("METERS"); //4
            DataModelRate.addColumn("KGS"); //5
            DataModelRate.addColumn("NOS"); //6
            DataModelRate.addColumn("COST"); //7
            DataModelRate.addColumn("NRV");
            DataModelRate.addColumn("RATE");
            DataModelRate.addColumn("VALUE");
            DataModelRate.addColumn("TTL_EXC");

            DataModelRate.TableReadOnly(true);

            //DataModelRate.TableReadOnly(false);
            DataModelRate.SetReadOnly(0);
            DataModelRate.SetReadOnly(1);
            DataModelRate.SetReadOnly(2);
            DataModelRate.SetReadOnly(3);
            DataModelRate.SetReadOnly(4);
            DataModelRate.SetReadOnly(5);
            DataModelRate.SetReadOnly(6);
            DataModelRate.SetReadOnly(7);
            DataModelRate.SetReadOnly(8);
            DataModelRate.SetReadOnly(9);
            DataModelRate.SetReadOnly(10);
            DataModelRate.SetReadOnly(11);

            RowFormat = new EITLTableCellRenderer();

            for (int j = 0; j < TableRate.getColumnCount(); j++) {
                TableRate.getColumnModel().getColumn(j).setCellRenderer(RowFormat);
            }

            CellAlign.setHorizontalAlignment(JLabel.LEFT);
            TableRate.getColumnModel().getColumn(0).setCellRenderer(Renderer);

            //TableEntry.getColumnModel().getColumn(16).setPreferredWidth(100);
        } catch (Exception e) {
        }
        Updating = false;
        //Table formatting completed

    }

    private void import_data() {
        Connection Conn = null, con = null;
        Statement stmt = null;

        try {
            String strSQL = "";
            Conn = data.getConn();
            Conn.setAutoCommit(false);
            stmt = Conn.createStatement();

            stmt.execute("TRUNCATE TABLE Test.TMP_IMPORT_MIS_STG_STOCK");

            con = data.getConn();
            PreparedStatement pstm = null;
            File file = null;
            file1.setVisible(true);
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            file1.setVisible(false);
            FileInputStream input = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            String mval, sql;
            sql = "INSERT INTO Test.TMP_IMPORT_MIS_STG_STOCK (ITEM_CODE,REFERENCE,UNIT,METERS,KGS,NOS,COST,NRV,RATE,VALUE,TTL_EXC) ";
            sql = sql + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            pstm = Conn.prepareStatement(sql);
            double dval;
            int i;
            for (i = 1; i <= sheet.getLastRowNum(); i++) {
                row = (Row) sheet.getRow(i);
                mval = row.getCell(0).getStringCellValue();
                pstm.setString(1, mval);
                mval = row.getCell(1).getStringCellValue();
                pstm.setString(2, mval);
                try {
                    mval = row.getCell(2).getStringCellValue();
                } catch (Exception e) {
                    mval = "";
                }
                pstm.setString(3, mval);

                for (int a = 4; a < 12; a++) {
                    try {
                        mval = row.getCell(a - 1).getStringCellValue();
                    } catch (Exception aa) {
                        mval = "";
                    }
                    try {
                        dval = Double.parseDouble(mval);
                    } catch (Exception e) {
                        dval = 0;
                    }
                    pstm.setDouble(a, dval);
                }
                pstm.addBatch();
                if ((i + 1) % 1000 == 0) {
                    pstm.executeBatch();
                    Conn.commit();
                }
                lblStatus.setText("Import rows " + i);
            }
            //con.commit();
            pstm.executeBatch();
            Conn.commit();
            pstm.close();
            //con.close();
            input.close();
            Conn.setAutoCommit(true);
            lblStatus.setText("Success Import Excel ...Imported Row[s] :" + (i - 1));
            disp_import_data();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                Conn.close();
                con.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
    }

}
