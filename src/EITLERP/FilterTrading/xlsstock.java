package EITLERP.FilterTrading;

import javax.swing.*;
import java.util.*;
import EITLERP.*;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.awt.Color;
import java.io.File;
import java.lang.*;
import java.sql.*;
import java.lang.String;
import javax.swing.table.TableColumnModel;

public class xlsstock extends javax.swing.JApplet {

    private TReportEngine objEngine = new TReportEngine();
    private clsExcelExporter exp = new clsExcelExporter();
    private EITLTableModel DataModelReport;
    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();
    private EITLTableCellRenderer CellAlign = new EITLTableCellRenderer();
    private EITLTableCellRenderer RowFormat = new EITLTableCellRenderer();
    private String minvdt;

    String cellLastValue = "", mdocnm = "";
    String mfilenm;
    private boolean chk = true;

    public void init() {
        
    }

    public xlsstock() {
        System.gc();
        setSize(600, 800);
        initComponents();
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM/yyyy");

        FormatGrid();
        file1.setVisible(false);
        lblTitle.setForeground(Color.WHITE);

    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_grp_hierarchy = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        btn_grp_type = new javax.swing.ButtonGroup();
        btngrp = new javax.swing.ButtonGroup();
        grpreport = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        FilterlPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        txtfrmdt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtstk = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        file1 = new javax.swing.JFileChooser();
        summary = new javax.swing.JRadioButton();
        detail = new javax.swing.JRadioButton();
        ReportPanel = new javax.swing.JPanel();
        lbltot = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableReport = new javax.swing.JTable();
        btn_exp_xls = new javax.swing.JButton();
        cmbrpt = new javax.swing.JButton();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("As on Date Stock For Filter Fabric Trading");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

        FilterlPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        FilterlPanel.setToolTipText("");
        FilterlPanel.setLayout(null);

        jButton3.setText(" GET STOCK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        FilterlPanel.add(jButton3);
        jButton3.setBounds(110, 230, 120, 30);

        txtfrmdt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        FilterlPanel.add(txtfrmdt);
        txtfrmdt.setBounds(180, 40, 130, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("AS ON DATE:");
        FilterlPanel.add(jLabel4);
        jLabel4.setBounds(70, 40, 100, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("DD/MM/YYYY");
        FilterlPanel.add(jLabel6);
        jLabel6.setBounds(190, 70, 110, 30);

        txtstk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        FilterlPanel.add(txtstk);
        txtstk.setBounds(180, 130, 130, 30);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("More Than And Equal");
        FilterlPanel.add(jLabel1);
        jLabel1.setBounds(10, 130, 160, 40);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Stock");
        FilterlPanel.add(jLabel2);
        jLabel2.setBounds(120, 120, 60, 30);
        FilterlPanel.add(file1);
        file1.setBounds(360, -40, 435, 413);

        grpreport.add(summary);
        summary.setFont(new java.awt.Font("DejaVu LGC Sans", 1, 13)); // NOI18N
        summary.setSelected(true);
        summary.setText("Summary");
        FilterlPanel.add(summary);
        summary.setBounds(60, 190, 94, 22);

        grpreport.add(detail);
        detail.setFont(new java.awt.Font("DejaVu LGC Sans", 1, 13)); // NOI18N
        detail.setText("Detail");
        FilterlPanel.add(detail);
        detail.setBounds(210, 190, 68, 22);

        jTabbedPane1.addTab("Filter", null, FilterlPanel, "");

        ReportPanel.setLayout(null);

        lbltot.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lbltot.setEnabled(false);
        ReportPanel.add(lbltot);
        lbltot.setBounds(630, 290, 80, 30);

        TableReport.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        TableReport.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TableReport);

        ReportPanel.add(jScrollPane1);
        jScrollPane1.setBounds(0, 0, 990, 290);

        btn_exp_xls.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        btn_exp_xls.setText("Export To Excel");
        btn_exp_xls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exp_xlsActionPerformed(evt);
            }
        });
        ReportPanel.add(btn_exp_xls);
        btn_exp_xls.setBounds(10, 300, 160, 40);

        cmbrpt.setText("Report");
        cmbrpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbrptActionPerformed(evt);
            }
        });
        ReportPanel.add(cmbrpt);
        cmbrpt.setBounds(736, 320, 190, 40);

        jTabbedPane1.addTab("Report", ReportPanel);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(5, 70, 1000, 460);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String condition = "", mfrmdt = "";

        if (txtfrmdt.getText().trim().length() > 0) {
            condition += "PRODUCTION_DATE<='" + EITLERPGLOBAL.formatDateDB(txtfrmdt.getText().trim()) + " 23:59:59'";
            mfrmdt = EITLERPGLOBAL.formatDateDB(txtfrmdt.getText().trim());
        } else {
            txtfrmdt.setText(EITLERPGLOBAL.getCurrentDate());
            condition += "PRODUCTION<='" + EITLERPGLOBAL.getCurrentDate() + " 23:59:59'";
            mfrmdt = EITLERPGLOBAL.getCurrentDate();
        }

        FormatGrid();
        Connection Conn = null;
        Statement st = null, st1 = null, st2 = null;
        ResultSet rsData = null, rsData1 = null;
        String itm = "", msql = "";
        Integer tpc = 0;

        try {
            Conn = data.getConn();
            st = Conn.createStatement();
            st1 = Conn.createStatement();
            st2 = Conn.createStatement();
            st.execute("TRUNCATE TABLE FILTERFABRIC.FF_TRD_STOCK");
            if (condition.trim().length() > 0) {
                condition = " WHERE " + condition;
            }
            msql = "INSERT INTO FILTERFABRIC.FF_TRD_STOCK "
                    + " SELECT PRODUCTION_NO,PRODUCTION_DATE,QUALITY_CD,PIECE_NO,WIDTH,GROSS_METER,SQ_METER,NET_METER,KGS,FLAG_CD,PIECE_STATUS,CREATED_DATE "
                    + " FROM FILTERFABRIC.FF_TRD_PIECE_REGISTER " + condition;
            st.execute(msql);

            if (txtfrmdt.getText().trim().length() > 0) {
                msql = "UPDATE FF_TRD_STOCK S,FF_TRD_PACKING_DETAIL D"
                        + " SET PIECE_STATUS='F'"
                        + " WHERE S.QUALITY_CD=D.QUALITY_CD AND S.PIECE_NO=D.PIECE_NO "
                        + " AND D.PACKING_DATE>'" + EITLERPGLOBAL.formatDateDB(txtfrmdt.getText().trim()) + "'";
            }

            //st.execute("update TMP_ITEM_STOCK_MASTER i,RETURN_ITEM_DETAIL a,D_COM_DOC_DATA b set i.return_qty=i.return_qty-a.return_qty,i.stock_qty=i.stock_qty+a.return_qty where a.created_date>'" + EITLERPGLOBAL.formatDateDB(txtfrmdt.getText().trim()) + " 23:59:59' and concat(trim(a.quality_cd),trim(a.shade_cd),trim(a.piece_no),trim(a.ext),trim(a.invoice_no),trim(a.size))=i.key_val and a.whsr_no=b.doc_no and b.status='F'");
            if (txtstk.getText().trim().length() > 0) {
                st.execute("DELETE FROM FILTERFABRIC.FF_TRD_STOCK WHERE NET_METER<" + txtstk.getText().trim());
            }

            if (detail.isSelected()) {
                msql = "SELECT S.*,M.DESCRIPTION,M.GROUP AS GRP,M.SYENTIC_PER,M.STYLE "
                        + " FROM FILTERFABRIC.FF_TRD_STOCK S,FILTERFABRIC.FF_TRD_QUALITY_MASTER M"
                        + " WHERE S.QUALITY_CD=M.QUALITY_CD AND S.PIECE_STATUS='F'"
                        + " ORDER BY S.QUALITY_CD,S.PIECE_NO";
            } else {
                msql = "SELECT QUALITY_CD,COUNT(*) AS NOP,SUM(GROSS_METER) AS GRSMTR,SUM(KGS) AS KG"
                        + " FROM FILTERFABRIC.FF_TRD_STOCK WHERE PIECE_STATUS='F'"
                        + " GROUP BY QUALITY_CD";
            }
            System.out.println(msql);
            rsData = st.executeQuery(msql);
            rsData.first();

            double tg, tn, tkg, qg, qn, qkg;
            tg = tn = tkg = qg = qn = qkg = 0.0;
            String qlt;
            while (!rsData.isAfterLast()) {
                qlt = rsData.getString("QUALITY_CD");
                Object[] rowDataRate = new Object[30];
                rowDataRate[0] = rsData.getString("QUALITY_CD");
                if (detail.isSelected()) {
                    rowDataRate[1] = rsData.getString("PIECE_NO");
                    rowDataRate[2] = EITLERPGLOBAL.round(rsData.getDouble("WIDTH"), 2);
                    rowDataRate[3] = EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    rowDataRate[4] = EITLERPGLOBAL.round(rsData.getDouble("SQ_METER"), 2);
                    rowDataRate[5] = EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    rowDataRate[6] = EITLERPGLOBAL.round(rsData.getDouble("KGS"), 2);
                    rowDataRate[7] = rsData.getString("FLAG_CD");
                    rowDataRate[8] = rsData.getString("DESCRIPTION");
                    rowDataRate[9] = rsData.getString("GRP");
                    rowDataRate[10] = rsData.getString("SYENTIC_PER");
                    rowDataRate[11] = rsData.getString("STYLE");
                } else {
                    rowDataRate[1] = rsData.getInt("NOP");
                    rowDataRate[2] = EITLERPGLOBAL.round(rsData.getDouble("GRSMTR"), 2);
                    rowDataRate[3] = EITLERPGLOBAL.round(rsData.getDouble("KG"), 2);
                    rowDataRate[4] = EITLERPGLOBAL.round(rsData.getDouble("GRSMTR"), 2);
                }

                if (detail.isSelected()) {
                    tg = tg + EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    qg = qg + EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    tn = tn + EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    qn = qn + EITLERPGLOBAL.round(rsData.getDouble("GROSS_METER"), 2);
                    tkg = tkg + EITLERPGLOBAL.round(rsData.getDouble("KGS"), 2);
                    qkg = qkg + EITLERPGLOBAL.round(rsData.getDouble("KGS"), 2);
                } else {
                    tpc = tpc + rsData.getInt("NOP");
                    tn = tn + EITLERPGLOBAL.round(rsData.getDouble("GRSMTR"), 2);
                    tkg = tkg + EITLERPGLOBAL.round(rsData.getDouble("KG"), 2);

                }

                DataModelReport.addRow(rowDataRate);
                TableReport.changeSelection(TableReport.getRowCount() - 1, 1, false, false);
                TableReport.requestFocus();
                rsData.next();
                if (detail.isSelected()) {
                    if (rsData.isAfterLast() || !rsData.getString("QUALITY_CD").equalsIgnoreCase(qlt)) {
                        add_blank();
                        Object[] rowData = new Object[10];
                        rowData[2] = "TOTAL";
                        rowData[3] = EITLERPGLOBAL.round(qg, 2);
                        rowData[5] = EITLERPGLOBAL.round(qn, 2);
                        rowData[6] = EITLERPGLOBAL.round(qkg, 2);
                        DataModelReport.addRow(rowData);
                        qg = qn = qkg = 0;
                        add_blank();

                    }
                }
            }
            add_blank();
            add_blank();
            Object[] rowData = new Object[10];
            
            if (detail.isSelected()) {
                rowData[2] = "GRAND TOTAL";
                rowData[3] = EITLERPGLOBAL.round(tg, 2);
                rowData[5] = EITLERPGLOBAL.round(tn, 2);
                rowData[6] = EITLERPGLOBAL.round(tkg, 2);
            } else {
                rowData[0] = "GRAND TOTAL";
                rowData[1] = tpc;
                rowData[2] = EITLERPGLOBAL.round(tn, 2);
                rowData[3] = EITLERPGLOBAL.round(tkg, 2);
                rowData[4] = EITLERPGLOBAL.round(tn, 2);
            }
            DataModelReport.addRow(rowData);
            qg = qn = qkg = 0;
            add_blank();

            TableReport.changeSelection(0, 1, false, false);
            TableReport.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                Conn.close();
            } catch (Exception s) {
                s.printStackTrace();
            }
        }
        jTabbedPane1.setSelectedIndex(1);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_exp_xlsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exp_xlsActionPerformed
        // TODO add your handling code here:
        File file = null;
        file1.setVisible(true);
        try {
            int returnVal = file1.showOpenDialog(this);
            if (returnVal == file1.APPROVE_OPTION) {
                file = file1.getSelectedFile();
            }
            exp.fillData(TableReport, file, "STOCKDT");
            JOptionPane.showMessageDialog(null, "Data saved at "
                    + file.toString() + " successfully ...", "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }//GEN-LAST:event_btn_exp_xlsActionPerformed

    private void cmbrptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbrptActionPerformed
        // TODO add your handling code here:
        String sql;

        HashMap Parameters = new HashMap();
        Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
        if (txtfrmdt.getText().trim().length() > 0) {
            Parameters.put("ASONDATE", EITLERPGLOBAL.formatDateDB(txtfrmdt.getText().trim()));
        } else {
            Parameters.put("ASONDATE", EITLERPGLOBAL.getCurrentDateDB());
        }

        TTable objData = new TTable();
        if (detail.isSelected()) {
            objData.AddColumn("PRODUCTION_NO");
            objData.AddColumn("PRODUCTION_DATE");
            objData.AddColumn("QUALITY_CD");
            objData.AddColumn("PIECE_NO");
            objData.AddColumn("WIDTH");
            objData.AddColumn("GROSS_METER");
            objData.AddColumn("SQ_METER");
            objData.AddColumn("NET_METER");
            objData.AddColumn("KGS");
            objData.AddColumn("FLAG_CD");
            objData.AddColumn("PIECE_STATUS");
            objData.AddColumn("CREATED_DATE");
            objData.AddColumn("DESCRIPTION");
            objData.AddColumn("GRP");
            objData.AddColumn("SYENTIC_PER");
            objData.AddColumn("STYLE");
        } else {
            objData.AddColumn("QUALITY_CD");
            objData.AddColumn("NOP");
            objData.AddColumn("GRSMTR");
            objData.AddColumn("KG");
        }
        try {

            String strSQL = "";
            ResultSet rsReport;

            //Retrieve data
            if (detail.isSelected()) {
                strSQL = "SELECT S.*,COALESCE(M.DESCRIPTION,'') AS DESCRIPTION,COALESCE(M.GROUP,'') AS GRP,COALESCE(M.SYENTIC_PER,'') AS SYENTIC_PER ,COALESCE(M.STYLE,'') AS STYLE "
                        + " FROM FILTERFABRIC.FF_TRD_STOCK S,FILTERFABRIC.FF_TRD_QUALITY_MASTER M"
                        + " WHERE S.QUALITY_CD=M.QUALITY_CD AND S.PIECE_STATUS='F'"
                        + " ORDER BY S.QUALITY_CD,S.PIECE_NO ";
            } else {
                strSQL = "SELECT QUALITY_CD,COUNT(*) AS NOP,SUM(GROSS_METER) AS GRSMTR,SUM(KGS) AS KG"
                        + " FROM FILTERFABRIC.FF_TRD_STOCK"
                        + " GROUP BY QUALITY_CD";
            }
            rsReport = data.getResult(strSQL);
            rsReport.first();

            if (rsReport.getRow() > 0) {

                while (!rsReport.isAfterLast()) {
                    TRow objRow = new TRow();

                    if (detail.isSelected()) {
                        objRow.setValue("PRODUCTION_NO", rsReport.getString("PRODUCTION_NO"));
                        objRow.setValue("PRODUCTION_DATE", rsReport.getString("PRODUCTION_DATE"));
                        objRow.setValue("QUALITY_CD", rsReport.getString("QUALITY_CD"));
                        objRow.setValue("PIECE_NO", rsReport.getString("PIECE_NO"));
                        objRow.setValue("WIDTH", Double.toString(rsReport.getDouble("WIDTH")));
                        objRow.setValue("GROSS_METER", Double.toString(rsReport.getDouble("GROSS_METER")));
                        objRow.setValue("SQ_METER", Double.toString(rsReport.getDouble("SQ_METER")));
                        objRow.setValue("NET_METER", Double.toString(rsReport.getDouble("NET_METER")));
                        objRow.setValue("KGS", Double.toString(rsReport.getDouble("KGS")));
                        objRow.setValue("FLAG_CD", rsReport.getString("FLAG_CD"));
                        objRow.setValue("PIECE_STATUS", rsReport.getString("PIECE_STATUS"));
                        objRow.setValue("CREATED_DATE", rsReport.getString("CREATED_DATE"));
                        objRow.setValue("DESCRIPTION", rsReport.getString("DESCRIPTION"));
                        objRow.setValue("GRP", rsReport.getString("GRP"));
                        objRow.setValue("SYENTIC_PER", Double.toString(rsReport.getDouble("SYENTIC_PER")));
                        objRow.setValue("STYLE", rsReport.getString("STYLE"));
                    } else {
                        objRow.setValue("QUALITY_CD", rsReport.getString("QUALITY_CD"));
                        objRow.setValue("NOP", Integer.toString(rsReport.getInt("NOP")));
                        objRow.setValue("GRSMTR", Double.toString(rsReport.getDouble("GRSMTR")));
                        objRow.setValue("KG", Double.toString(rsReport.getDouble("KG")));
                    }
                    objData.AddRow(objRow);
                    rsReport.next();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (detail.isSelected()) {
            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptStkD.rpt", Parameters, objData);            
        } else {
            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptStkS.rpt", Parameters, objData);            
        }

    }//GEN-LAST:event_cmbrptActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FilterlPanel;
    private javax.swing.JPanel ReportPanel;
    private javax.swing.JTable TableReport;
    private javax.swing.JButton btn_exp_xls;
    private javax.swing.ButtonGroup btn_grp_hierarchy;
    private javax.swing.ButtonGroup btn_grp_type;
    private javax.swing.ButtonGroup btngrp;
    private javax.swing.JButton cmbrpt;
    private javax.swing.JRadioButton detail;
    private javax.swing.JFileChooser file1;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.ButtonGroup grpreport;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbltot;
    private javax.swing.JRadioButton summary;
    private javax.swing.JTextField txtfrmdt;
    private javax.swing.JTextField txtstk;
    // End of variables declaration//GEN-END:variables

    private void FormatGrid() {
        try {
            DataModelReport = new EITLTableModel();
            TableReport.removeAll();
            TableReport.setModel(DataModelReport);
            TableColumnModel ColModel = TableReport.getColumnModel();

            EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
            Renderer.setColor(0, 0, Color.BLUE);

            if (detail.isSelected()) {
                DataModelReport.addColumn("QUALITY_CD");
                DataModelReport.addColumn("PIECE_NO");
                DataModelReport.addColumn("WIDTH");
                DataModelReport.addColumn("GROSS_METER");
                DataModelReport.addColumn("SQ_METER");
                DataModelReport.addColumn("NET_METER");
                DataModelReport.addColumn("KGS");
                DataModelReport.addColumn("FLAG_CD");
                DataModelReport.addColumn("DESCRIPTION");
                DataModelReport.addColumn("GROUP");
                DataModelReport.addColumn("SYENTIC_PER");
                DataModelReport.addColumn("STYLE");
            } else {
                DataModelReport.addColumn("Quality Code");
                DataModelReport.addColumn("No of Pieces");
                DataModelReport.addColumn("Receipt Meters");
                DataModelReport.addColumn("Receipt KGS");
                DataModelReport.addColumn("Net Meters");
            }
            DataModelReport.TableReadOnly(true);

        } catch (Exception e) {
        }

        //Table formatting completed
    }

    private void add_blank() {
        Object[] rowData = new Object[10];
        rowData[0] = "";
        DataModelReport.addRow(rowData);
    }
}
