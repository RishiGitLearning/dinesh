/*
 * frmLegacyVouchers.java
 *
 * Created on August 23, 2008, 11:18 AM
 */
package EITLERP.EWB;

/**
 *
 * @author root
 */
import EITLERP.Sales.*;
import EITLERP.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;

public class frmEwayDetailEntry extends javax.swing.JApplet {

    private EITLTableModel DataModel = new EITLTableModel();
    private EITLComboModel cmbInvoiceTypeModel;

    /**
     * Initializes the applet frmLegacyVouchers
     */
    public void init() {
        setSize(571, 474);
        initComponents();
        GenerateCombo();
        FormatGrid();
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        lblInvoiceNo = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        cmdShowList = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbInvoiceType = new javax.swing.JComboBox();

        getContentPane().setLayout(null);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setText("Eway Detail Entry");
        lblTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 2, 666, 25);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 90, 85, 16);

        jLabel2.setText("Display Invoice of period :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(6, 39, 180, 16);
        getContentPane().add(txtFromDate);
        txtFromDate.setBounds(100, 90, 100, 28);
        getContentPane().add(txtToDate);
        txtToDate.setBounds(270, 90, 100, 28);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("To Date :");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(200, 90, 60, 16);

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
        jScrollPane1.setViewportView(Table);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(3, 174, 690, 240);

        lblInvoiceNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvoiceNo.setText("Invoice No. :");
        getContentPane().add(lblInvoiceNo);
        lblInvoiceNo.setBounds(10, 120, 85, 16);
        getContentPane().add(txtInvoiceNo);
        txtInvoiceNo.setBounds(100, 120, 160, 28);

        cmdShowList.setText("Show List");
        cmdShowList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowListActionPerformed(evt);
            }
        });
        getContentPane().add(cmdShowList);
        cmdShowList.setBounds(420, 144, 110, 28);

        cmdSave.setText("Save");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        getContentPane().add(cmdSave);
        cmdSave.setBounds(440, 420, 90, 28);

        lblStatus.setForeground(new java.awt.Color(51, 153, 255));
        lblStatus.setText("Status");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(6, 423, 280, 16);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Invoice Type :");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 60, 85, 16);
        getContentPane().add(cmbInvoiceType);
        cmbInvoiceType.setBounds(100, 60, 180, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < Table.getRowCount(); i++) {
            if (DataModel.getValueByVariable("LR_NO", i).length() > 20) {
                JOptionPane.showMessageDialog(this, "LR No. should not more then twenty digits at Sr No. " + (i + 1));
                return;
            }
        }
        new Thread() {
            public void run() {
                try {
                    String sql;
                    for (int i = 0; i < Table.getRowCount(); i++) {
                        String InvoiceNo = DataModel.getValueByVariable("INVOICE_NO", i);
                        lblStatus.setText("Updating LR No. in Invoice No. " + InvoiceNo);
                        String InvoiceDate = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("INVOICE_DATE", i));
                        String PartyCode = DataModel.getValueByVariable("PARTY_CODE", i);
                        String BaleNo = DataModel.getValueByVariable("BALE_NO", i);
                        String LRNo = DataModel.getValueByVariable("LR_NO", i);
                        String LRDt = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("LR_DATE", i));
                        String mcarrier = DataModel.getValueByVariable("CARRIER", i);
                        double mfreight = 0;
                        try {
                            mfreight = Double.parseDouble(DataModel.getValueByVariable("FREIGHT", i).toString());
                        } catch (Exception a) {
                            mfreight = 0;
                        }
                        String mewayrmk = DataModel.getValueByVariable("EWAY_REMARKS", i);
                        String mewayind = DataModel.getValueByVariable("EWAY_ID", i);
                        String ewayDt = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("EWAY_DATE", i));
                        String mvehicle = DataModel.getValueByVariable("VEHICLE_NO", i);
                        String mtrandoc = DataModel.getValueByVariable("TRANS_DOC_NO", i);                        
                        String mtrandocDt = EITLERPGLOBAL.formatDateDB(DataModel.getValueByVariable("TRANS_DOC_DATE", i));
                        String mdrivernm = DataModel.getValueByVariable("DRIVER_NAME", i);

                        sql = "UPDATE D_SAL_INVOICE_HEADER SET LR_NO='" + LRNo + "',"
                                + "LR_DATE='" + LRDt + "',CARRIER='" + mcarrier + "',FREIGHT=" + mfreight + ","
                                + "EWAY_REMARKS='" + mewayrmk + "',EWAY_ID='" + mewayind + "',EWAY_DATE='" + ewayDt + "',"
                                + "VEHICLE_NO='" + mvehicle + "',TRANSPORTER_DOC_NO='" + mtrandoc + "',"
                                + "TRANSPORTER_DOC_DATE='" + mtrandocDt + "',DRIVER_NAME='" + mdrivernm + "',"
                                + "CHANGED=1,CHANGED_DATE=CURDATE(),MODIFIED_BY='" + EITLERPGLOBAL.gLoginID + "' WHERE PARTY_CODE='" + PartyCode + "' AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";
                        data.Execute(sql);
                    }
                    lblStatus.setText("Ready");
                } catch (Exception e) {
                }
            }
        ;
    }.start();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdShowListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowListActionPerformed
        // TODO add your handling code here:

        String FromDate = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
        String ToDate = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
        int InvoiceType = cmbInvoiceType.getSelectedIndex();
        new Thread() {

            public void run() {
                try {
                    String strSQL = "SELECT INVOICE_NO,INVOICE_DATE,PARTY_CODE,BALE_NO,LR_NO,LR_DATE,CARRIER,FREIGHT,EWAY_REMARKS,EWAY_ID,EWAY_DATE,VEHICLE_NO,TRANSPORTER_DOC_NO,TRANSPORTER_DOC_DATE,DRIVER_NAME FROM D_SAL_INVOICE_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_TYPE=" + cmbInvoiceType.getSelectedIndex() + " ";
                    String strCondition = "";

                    if (!txtFromDate.getText().trim().equals("")) {
                        strCondition += "AND INVOICE_DATE>='" + EITLERPGLOBAL.formatDateDB(txtFromDate.getText()) + "' ";
                    }

                    if (!txtToDate.getText().trim().equals("")) {
                        strCondition += "AND INVOICE_DATE<='" + EITLERPGLOBAL.formatDateDB(txtToDate.getText()) + "' ";
                    }

                    if (!txtInvoiceNo.getText().trim().equals("")) {
                        strCondition += "AND INVOICE_NO='" + txtInvoiceNo.getText() + "' ";
                    }

                    strSQL += strCondition;
                    strSQL += " ORDER BY INVOICE_DATE, INVOICE_NO ";

                    lblStatus.setText("Fetching Records ... ");

                    ResultSet rsTmp = data.getResult(strSQL);
                    rsTmp.first();

                    FormatGrid();

                    if (rsTmp.getRow() > 0) {
                        while (!rsTmp.isAfterLast()) {

                            Object[] rowData = new Object[1];
                            DataModel.addRow(rowData);

                            int NewRow = Table.getRowCount() - 1;
                            lblStatus.setText("Generating Table " + NewRow);
                            DataModel.setValueByVariable("SR_NO", Integer.toString(NewRow + 1), NewRow);
                            DataModel.setValueByVariable("INVOICE_NO", UtilFunctions.getString(rsTmp, "INVOICE_NO", ""), NewRow);
                            DataModel.setValueByVariable("INVOICE_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "INVOICE_DATE", "0000-00-00")), NewRow);
                            DataModel.setValueByVariable("PARTY_CODE", UtilFunctions.getString(rsTmp, "PARTY_CODE", ""), NewRow);
                            DataModel.setValueByVariable("BALE_NO", UtilFunctions.getString(rsTmp, "BALE_NO", ""), NewRow);
                            DataModel.setValueByVariable("LR_NO", UtilFunctions.getString(rsTmp, "LR_NO", ""), NewRow);
                            DataModel.setValueByVariable("LR_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "LR_DATE", "0000-00-00")), NewRow);
                            DataModel.setValueByVariable("CARRIER", UtilFunctions.getString(rsTmp, "CARRIER", ""), NewRow);
                            DataModel.setValueByVariable("FREIGHT", UtilFunctions.getString(rsTmp, "FREIGHT", ""), NewRow);
                            DataModel.setValueByVariable("EWAY_REMARKS", UtilFunctions.getString(rsTmp, "EWAY_REMARKS", ""), NewRow);
                            DataModel.setValueByVariable("EWAY_ID", UtilFunctions.getString(rsTmp, "EWAY_ID", ""), NewRow);
                            DataModel.setValueByVariable("EWAY_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "EWAY_DATE", "0000-00-00")), NewRow);
                            DataModel.setValueByVariable("VEHICLE_NO", UtilFunctions.getString(rsTmp, "VEHICLE_NO", ""), NewRow);
                            DataModel.setValueByVariable("TRANS_DOC_NO", UtilFunctions.getString(rsTmp, "TRANSPORTER_DOC_NO", ""), NewRow);                            
                            DataModel.setValueByVariable("TRANS_DOC_DATE", EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp, "TRANSPORTER_DOC_DATE", "0000-00-00")), NewRow);
                            DataModel.setValueByVariable("DRIVER_NAME", UtilFunctions.getString(rsTmp, "DRIVER_NAME", ""), NewRow);

                            rsTmp.next();
                        }
                    }
                    lblStatus.setText("Ready ");
                } catch (Exception e) {
                    e.printStackTrace();
                    lblStatus.setText("Ready ");
                }
            }
        ;
    }.start();
    }//GEN-LAST:event_cmdShowListActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JComboBox cmbInvoiceType;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInvoiceNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtToDate;
    // End of variables declaration//GEN-END:variables

    private void FormatGrid() {
        try {
            DataModel = new EITLTableModel();
            Table.removeAll();

            Table.setModel(DataModel);
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            DataModel.addColumn("Sr."); //0 - Read Only
            DataModel.addColumn("Invoice No."); //1
            DataModel.addColumn("Invoice Date"); //2
            DataModel.addColumn("Party Code"); //3
            DataModel.addColumn("Bale No."); //4
            DataModel.addColumn("LR No."); //5
            DataModel.addColumn("LR Date"); //6
            DataModel.addColumn("Carrier"); //7
            DataModel.addColumn("Freight"); //8            
            DataModel.addColumn("Eway Remarks "); //9
            DataModel.addColumn("Eway No."); //10
            DataModel.addColumn("Eway Date"); //11
            DataModel.addColumn("Vehicle No."); //12
            DataModel.addColumn("Transporter Doc No."); //13
            DataModel.addColumn("Transporter Doc Date"); //14
            DataModel.addColumn("Driver Name"); //15

            DataModel.SetVariable(0, "SR_NO"); //0 - Read Only
            DataModel.SetVariable(1, "INVOICE_NO"); //0 - Read Only
            DataModel.SetVariable(2, "INVOICE_DATE"); //0 - Read Only
            DataModel.SetVariable(3, "PARTY_CODE"); //0 - Read Only
            DataModel.SetVariable(4, "BALE_NO"); //0 - Read Only
            DataModel.SetVariable(5, "LR_NO"); //0 - Read Only
            DataModel.SetVariable(6, "LR_DATE"); //
            DataModel.SetVariable(7, "CARRIER"); //
            DataModel.SetVariable(8, "FREIGHT"); //
            DataModel.SetVariable(9, "EWAY_REMARKS"); //
            DataModel.SetVariable(10, "EWAY_ID"); //
            DataModel.SetVariable(11, "EWAY_DATE"); //
            DataModel.SetVariable(12, "VEHICLE_NO"); //
            DataModel.SetVariable(13, "TRANS_DOC_NO"); //
            DataModel.SetVariable(14, "TRANS_DOC_DATE"); //
            DataModel.SetVariable(15, "DRIVER_NAME"); //

            DataModel.TableReadOnly(false);
            DataModel.SetReadOnly(0);
            DataModel.SetReadOnly(1);
            DataModel.SetReadOnly(2);
            DataModel.SetReadOnly(3);
            DataModel.SetReadOnly(4);

            Table.getColumnModel().getColumn(0).setMaxWidth(50);
            Table.getColumnModel().getColumn(1).setMaxWidth(100);
            Table.getColumnModel().getColumn(2).setMaxWidth(100);
            Table.getColumnModel().getColumn(2).setMinWidth(80);
            Table.getColumnModel().getColumn(3).setMaxWidth(90);
            Table.getColumnModel().getColumn(4).setMaxWidth(90);
            Table.getColumnModel().getColumn(9).setMinWidth(100);
            Table.getColumnModel().getColumn(13).setMinWidth(150);
            Table.getColumnModel().getColumn(14).setMinWidth(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Table formatting completed
    }

    private void GenerateCombo() {
        //--- Generate Type Combo ------//
        cmbInvoiceTypeModel = new EITLComboModel();

        cmbInvoiceType.removeAllItems();
        cmbInvoiceType.setModel(cmbInvoiceTypeModel);

        ComboData aData = new ComboData();
        aData.Code = 0;
        aData.Text = "Select Invoice Type";
        cmbInvoiceTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 1;
        aData.Text = "Suiting Sales";
        cmbInvoiceTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 2;
        aData.Text = "Felt Sales";
        cmbInvoiceTypeModel.addElement(aData);

        aData = new ComboData();
        aData.Code = 3;
        aData.Text = "Filter Sales";
        cmbInvoiceTypeModel.addElement(aData);

        //===============================//
    }
}
