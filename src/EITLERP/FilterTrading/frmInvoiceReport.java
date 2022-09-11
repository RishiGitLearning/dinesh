/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */
package EITLERP.FilterTrading;

/**
 *
 * @author
 */
/*<APPLET CODE=frmInvoiceReport.class HEIGHT=574 WIDTH=758></APPLET>*/
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import TReportWriter.SimpleDataProvider.TRow;
import TReportWriter.SimpleDataProvider.TTable;
import TReportWriter.TReportEngine;
import java.lang.*;
import java.sql.*;
import java.lang.String;

//import EITLERP.Purchase.frmSendMail;
public class frmInvoiceReport extends javax.swing.JApplet {

    private int EditMode = 0;

    //private clsPackingentry ObjPackingEntry;
    private TReportEngine objEngine = new TReportEngine();
    private EITLComboModel cmbLotModel = new EITLComboModel();

    private int SelHierarchyID = 0; //Selected Hierarchy
    private int lnFromID = 0;
    private String SelPrefix = ""; //Selected Prefix
    private String SelSuffix = ""; //Selected Prefix
    private int FFNo = 0;

    private EITLTableCellRenderer Renderer = new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint = new EITLTableCellRenderer();

    private HashMap colVariables = new HashMap();
    private HashMap colVariables_H = new HashMap();
    //clsColumn ObjColumn=new clsColumn();

    private boolean Updating = false;
    private boolean Updating_H = false;
    private boolean DoNotEvaluate = false;

    private boolean HistoryView = false;
    private String theDocNo = "";
    public frmPendingApprovals frmPA;
    private int charge09index = 0;
    private EITLTableCellRenderer Rend = new EITLTableCellRenderer();
    String cellLastValue = "";

    /**
     * Creates new form frmSalesParty
     */
    public frmInvoiceReport() {
        System.gc();
        setSize(800, 700);
        initComponents();

        btn.setVisible(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        lblStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtinvdt = new javax.swing.JTextField();
        cmbLot = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        btn = new javax.swing.JPanel();
        cmdinvslp = new javax.swing.JButton();
        cmbinvsmy = new javax.swing.JButton();
        cmdinvsummary = new javax.swing.JButton();
        cmdinvslp1 = new javax.swing.JButton();
        cmdexcise = new javax.swing.JButton();
        cmdvat = new javax.swing.JButton();

        getContentPane().setLayout(null);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(0, 0, 255));
        lblStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(20, 640, 610, 20);

        jLabel1.setText("Invoice Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 80, 100, 17);

        jLabel2.setText("DD/MM/YYYY");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(130, 50, 90, 17);

        txtinvdt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtinvdtFocusLost(evt);
            }
        });
        getContentPane().add(txtinvdt);
        txtinvdt.setBounds(120, 70, 120, 40);

        cmbLot.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        getContentPane().add(cmbLot);
        cmbLot.setBounds(250, 70, 130, 40);

        jLabel3.setText("Lot No.");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(250, 50, 70, 17);

        btn.setLayout(null);

        cmdinvslp.setText("Invoice Slip");
        cmdinvslp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdinvslpActionPerformed(evt);
            }
        });
        btn.add(cmdinvslp);
        cmdinvslp.setBounds(400, 30, 130, 29);

        cmbinvsmy.setText("Invoice Summary");
        cmbinvsmy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbinvsmyActionPerformed(evt);
            }
        });
        btn.add(cmbinvsmy);
        cmbinvsmy.setBounds(210, 30, 180, 29);

        cmdinvsummary.setText("Invoice No Summary");
        cmdinvsummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdinvsummaryActionPerformed(evt);
            }
        });
        btn.add(cmdinvsummary);
        cmdinvsummary.setBounds(10, 30, 190, 29);

        cmdinvslp1.setText("Transport Letter");
        cmdinvslp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdinvslp1ActionPerformed(evt);
            }
        });
        btn.add(cmdinvslp1);
        cmdinvslp1.setBounds(210, 90, 180, 29);

        cmdexcise.setText("Excise Sheet");
        cmdexcise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexciseActionPerformed(evt);
            }
        });
        btn.add(cmdexcise);
        cmdexcise.setBounds(10, 90, 190, 29);

        cmdvat.setText("Vat");
        cmdvat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdvatActionPerformed(evt);
            }
        });
        btn.add(cmdvat);
        cmdvat.setBounds(400, 90, 130, 29);

        getContentPane().add(btn);
        btn.setBounds(30, 140, 680, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void txtinvdtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtinvdtFocusLost
        // TODO add your handling code here:
        if (GenerateCombo()) {
            btn.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invoice not Generated...");
            txtinvdt.setText("");
            btn.setVisible(false);
            txtinvdt.requestFocus();
        }
    }//GEN-LAST:event_txtinvdtFocusLost

    private void cmdinvslpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdinvslpActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();

            //Populate Columns
            objData.AddColumn("TAX_INVOICE");
            objData.AddColumn("TAX_LABEL");
            objData.AddColumn("INVOICE_NO");
            objData.AddColumn("PARTY_NAME");
            objData.AddColumn("ADDRESS1");
            objData.AddColumn("ADDRESS2");
            objData.AddColumn("PINCODE");
            objData.AddColumn("TIN_NO");
            objData.AddColumn("TIN_DATE");
            objData.AddColumn("GATEPASS_NO");
            objData.AddColumn("DOCUMENT_THROUGH");
            objData.AddColumn("INV_NO");
            objData.AddColumn("INVOICE_DATE");
            objData.AddColumn("PACKING_DATE");
            objData.AddColumn("PARTY_CODE");
            objData.AddColumn("AGENT_NAME");
            objData.AddColumn("SALE_NOTE_NO");
            objData.AddColumn("CHARGE_CD");
            objData.AddColumn("BALE_NO");
            objData.AddColumn("STATION_CODE");
            objData.AddColumn("TRANSPORTER_NAME");
            objData.AddColumn("DESCRIPTION");
            objData.AddColumn("QUALITY_CD");
            objData.AddColumn("WIDTH");
            objData.AddColumn("GROSS_SQ_MTR");
            objData.AddColumn("PIECE_NO");
            objData.AddColumn("GROSS_QTY");
            objData.AddColumn("NET_QTY");
            objData.AddColumn("FLAG_DEF_CODE");
            objData.AddColumn("NET_QTY");
            objData.AddColumn("GROSS_AMOUNT");
            objData.AddColumn("RATE");
            objData.AddColumn("NET_AMOUNT");
            objData.AddColumn("EXCISABLE_VALUE");
            objData.AddColumn("TOTAL_SQ_MTR");
            objData.AddColumn("TOTAL_KG");
            objData.AddColumn("TOTAL_GROSS_QTY");
            objData.AddColumn("TOTAL_NET_QTY");
            objData.AddColumn("TOTAL_NET_AMOUNT");
            objData.AddColumn("EXCISABLE_VALUE");
            objData.AddColumn("TAX_LABEL1");
            objData.AddColumn("TAX_LABEL2");
            objData.AddColumn("TAX_VAL1");
            objData.AddColumn("TAX_VAL2");
            objData.AddColumn("INSURANCE_CHARGES");
            objData.AddColumn("TOTAL_VALUE");
            objData.AddColumn("PAY_AMT");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT RIGHT(100000+H.INVOICE_SR_NO,5) AS TAX_INVOICE,"
                        + "CASE WHEN H.CST2>0 THEN 'RETAIL INVOICE' ELSE 'TAX INVOICE' END AS TAX_LABEL,H.INVOICE_NO,"
                        + "P.PARTY_NAME,P.ADDRESS1,P.ADDRESS2,P.PINCODE,COALESCE(P.TIN_NO,'') AS TIN_NO,COALESCE(P.TIN_DATE,'') AS TIN_DATE,H.GATEPASS_NO,P.DOCUMENT_THROUGH,"
                        + "H.INV_NO,H.INVOICE_DATE,H.PACKING_DATE,H.PARTY_CODE,COALESCE(M.PARTY_NAME,'') AS AGENT_NAME,H.SALE_NOTE_NO,"
                        + "CONCAT((H.BANK_CHARGES*1),(H.TRANSPORT_MODE*1)) AS CHARGE_CD,H.BALE_NO,H.STATION_CODE,H.TRANSPORTER_NAME,"
                        + "D.DESCRIPTION,D.QUALITY_CD,D.WIDTH,D.GROSS_SQ_MTR,D.PIECE_NO,D.GROSS_QTY,D.NET_QTY,COALESCE(D.FLAG_DEF_CODE,'') AS FLAG_DEF_CODE,"
                        + "D.NET_QTY,D.GROSS_AMOUNT,D.RATE,D.NET_AMOUNT,D.EXCISABLE_VALUE,"
                        + "H.TOTAL_SQ_MTR,H.TOTAL_KG,H.TOTAL_GROSS_QTY,H.TOTAL_NET_QTY,H.TOTAL_NET_AMOUNT,H.EXCISABLE_VALUE,"
                        + "CASE WHEN H.VAT4>0 THEN 'VAT 4%' WHEN H.CST2>0 THEN 'CST 2%' ELSE 'CST 5%' END AS TAX_LABEL1,"
                        + "CASE WHEN H.VAT1>0 THEN 'VAT 1%' ELSE '' END AS TAX_LABEL2,"
                        + "CASE WHEN H.VAT4>0 THEN H.VAT4 WHEN H.CST2>0 THEN H.CST2 ELSE H.CST5 END AS TAX_VAL1,"
                        + "CASE WHEN H.VAT1>0 THEN H.VAT1 ELSE 0 END AS TAX_VAL2,H.INSURANCE_CHARGES,H.TOTAL_VALUE,"
                        + "H.NET_AMOUNT AS PAY_AMT "
                        + "FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER H,FILTERFABRIC.FF_TRD_INVOICE_DETAIL D,DINESHMILLS.D_SAL_PARTY_MASTER P "
                        + "LEFT JOIN (SELECT PARTY_CODE,PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210072) M "
                        + "ON M.PARTY_CODE=CONCAT(LEFT(P.PARTY_CODE,2),'0000')  "
                        + "WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=D.INVOICE_DATE AND H.LOT_NO=D.LOT_NO AND "
                        + "H.PARTY_CODE=P.PARTY_CODE AND P.MAIN_ACCOUNT_CODE=210072 AND "
                        + "H.INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND H.LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot);

                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("TAX_INVOICE", rsReport.getString("TAX_INVOICE"));
                        objRow.setValue("TAX_LABEL", rsReport.getString("TAX_LABEL"));
                        objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                        objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                        objRow.setValue("ADDRESS1", rsReport.getString("ADDRESS1"));
                        objRow.setValue("ADDRESS2", rsReport.getString("ADDRESS2"));
                        objRow.setValue("PINCODE", rsReport.getString("PINCODE"));
                        objRow.setValue("TIN_NO", rsReport.getString("TIN_NO"));
                        objRow.setValue("TIN_DATE", rsReport.getString("TIN_DATE"));
                        objRow.setValue("GATEPASS_NO", rsReport.getString("GATEPASS_NO"));
                        objRow.setValue("DOCUMENT_THROUGH", rsReport.getString("DOCUMENT_THROUGH"));
                        objRow.setValue("INV_NO", rsReport.getString("INV_NO"));
                        objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                        objRow.setValue("PACKING_DATE", rsReport.getString("PACKING_DATE"));
                        objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                        objRow.setValue("AGENT_NAME", rsReport.getString("AGENT_NAME"));
                        objRow.setValue("SALE_NOTE_NO", rsReport.getString("SALE_NOTE_NO"));
                        objRow.setValue("CHARGE_CD", rsReport.getString("CHARGE_CD"));
                        objRow.setValue("BALE_NO", rsReport.getString("BALE_NO"));
                        objRow.setValue("STATION_CODE", rsReport.getString("STATION_CODE"));
                        objRow.setValue("TRANSPORTER_NAME", rsReport.getString("TRANSPORTER_NAME"));
                        objRow.setValue("DESCRIPTION", rsReport.getString("DESCRIPTION"));
                        objRow.setValue("QUALITY_CD", rsReport.getString("QUALITY_CD"));
                        objRow.setValue("WIDTH", Double.toString(rsReport.getDouble("WIDTH")));
                        objRow.setValue("GROSS_SQ_MTR", Double.toString(rsReport.getDouble("GROSS_SQ_MTR")));
                        objRow.setValue("PIECE_NO", rsReport.getString("PIECE_NO"));
                        objRow.setValue("GROSS_QTY", Double.toString(rsReport.getDouble("GROSS_QTY")));
                        objRow.setValue("NET_QTY", Double.toString(rsReport.getDouble("NET_QTY")));
                        objRow.setValue("FLAG_DEF_CODE", rsReport.getString("FLAG_DEF_CODE"));
                        objRow.setValue("NET_QTY", Double.toString(rsReport.getDouble("NET_QTY")));
                        objRow.setValue("GROSS_AMOUNT", Double.toString(rsReport.getDouble("GROSS_AMOUNT")));
                        objRow.setValue("RATE", Double.toString(rsReport.getDouble("RATE")));
                        objRow.setValue("NET_AMOUNT", Double.toString(rsReport.getDouble("NET_AMOUNT")));
                        objRow.setValue("EXCISABLE_VALUE", Double.toString(rsReport.getDouble("EXCISABLE_VALUE")));
                        objRow.setValue("TOTAL_SQ_MTR", Double.toString(rsReport.getDouble("TOTAL_SQ_MTR")));
                        objRow.setValue("TOTAL_KG", Double.toString(rsReport.getDouble("TOTAL_KG")));
                        objRow.setValue("TOTAL_GROSS_QTY", Double.toString(rsReport.getDouble("TOTAL_GROSS_QTY")));
                        objRow.setValue("TOTAL_NET_QTY", Double.toString(rsReport.getDouble("TOTAL_NET_QTY")));
                        objRow.setValue("TOTAL_NET_AMOUNT", Double.toString(rsReport.getDouble("TOTAL_NET_AMOUNT")));
                        objRow.setValue("EXCISABLE_VALUE", Double.toString(rsReport.getDouble("EXCISABLE_VALUE")));
                        objRow.setValue("TAX_LABEL1", rsReport.getString("TAX_LABEL1"));
                        objRow.setValue("TAX_LABEL2", rsReport.getString("TAX_LABEL2"));
                        objRow.setValue("TAX_VAL1", Double.toString(rsReport.getDouble("TAX_VAL1")));
                        objRow.setValue("TAX_VAL2", Double.toString(rsReport.getDouble("TAX_VAL2")));
                        objRow.setValue("INSURANCE_CHARGES", Double.toString(rsReport.getDouble("INSURANCE_CHARGES")));
                        objRow.setValue("TOTAL_VALUE", Double.toString(rsReport.getDouble("TOTAL_VALUE")));
                        objRow.setValue("PAY_AMT", Double.toString(rsReport.getDouble("PAY_AMT")));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptInvoiceSlip.rpt", Parameters, objData);

        }
    }//GEN-LAST:event_cmdinvslpActionPerformed

    private void cmbinvsmyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinvsmyActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();
            objData.AddColumn("INVOICE_NO");
            objData.AddColumn("INVOICE_DATE");
            objData.AddColumn("LOT_NO");
            objData.AddColumn("AGENT_SR_NO");
            objData.AddColumn("PARTY_CODE");
            objData.AddColumn("PARTY_NAME");
            objData.AddColumn("STATION_CODE");
            objData.AddColumn("CHRG_CD");
            objData.AddColumn("NO_OF_PIECES");
            objData.AddColumn("TOTAL_SQ_MTR");
            objData.AddColumn("TOTAL_GROSS_QTY");
            objData.AddColumn("TOTAL_GROSS_AMOUNT");
            objData.AddColumn("BALE_NO");
            objData.AddColumn("VAT4");
            objData.AddColumn("VAT1");
            objData.AddColumn("CST2");
            objData.AddColumn("CST5");
            objData.AddColumn("INSURANCE_CHARGES");
            objData.AddColumn("TOTAL_VALUE");
            objData.AddColumn("NET_AMOUNT");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT INVOICE_NO,INVOICE_DATE,LOT_NO,AGENT_SR_NO,H.PARTY_CODE,PARTY_NAME,STATION_CODE,"
                        + "CONCAT(BANK_CHARGES*1,TRANSPORT_MODE*1) AS CHRG_CD,NO_OF_PIECES,TOTAL_SQ_MTR,TOTAL_GROSS_QTY,TOTAL_GROSS_AMOUNT,"
                        + "BALE_NO,VAT4,VAT1,CST2,CST5,INSURANCE_CHARGES,TOTAL_VALUE,NET_AMOUNT "
                        + " FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER H,DINESHMILLS.D_SAL_PARTY_MASTER M "
                        + " WHERE H.PARTY_CODE=M.PARTY_CODE AND M.MAIN_ACCOUNT_CODE=210072 AND "
                        + "H.INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND "
                        + "H.LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot);

                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                        objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                        objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                        objRow.setValue("LOT_NO", Integer.toString(rsReport.getInt("LOT_NO")));
                        objRow.setValue("AGENT_SR_NO", Integer.toString(rsReport.getInt("AGENT_SR_NO")));
                        objRow.setValue("NO_OF_PIECES", Integer.toString(rsReport.getInt("NO_OF_PIECES")));
                        objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                        objRow.setValue("CHRG_CD", rsReport.getString("CHRG_CD"));
                        objRow.setValue("BALE_NO", rsReport.getString("BALE_NO"));
                        objRow.setValue("STATION_CODE", rsReport.getString("STATION_CODE"));
                        objRow.setValue("TOTAL_SQ_MTR", Double.toString(rsReport.getDouble("TOTAL_SQ_MTR")));
                        objRow.setValue("TOTAL_GROSS_QTY", Double.toString(rsReport.getDouble("TOTAL_GROSS_QTY")));
                        objRow.setValue("TOTAL_GROSS_AMOUNT", Double.toString(rsReport.getDouble("TOTAL_GROSS_AMOUNT")));
                        objRow.setValue("VAT4", Double.toString(rsReport.getDouble("VAT4")));
                        objRow.setValue("VAT1", Double.toString(rsReport.getDouble("VAT1")));
                        objRow.setValue("CST2", Double.toString(rsReport.getDouble("CST2")));
                        objRow.setValue("CST5", Double.toString(rsReport.getDouble("CST5")));
                        objRow.setValue("INSURANCE_CHARGES", Double.toString(rsReport.getDouble("INSURANCE_CHARGES")));
                        objRow.setValue("TOTAL_VALUE", Double.toString(rsReport.getDouble("TOTAL_VALUE")));
                        objRow.setValue("NET_AMOUNT", Double.toString(rsReport.getDouble("NET_AMOUNT")));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptInvoiceSummary.rpt", Parameters, objData);

        }

    }//GEN-LAST:event_cmbinvsmyActionPerformed

    private void cmdinvsummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdinvsummaryActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();
            objData.AddColumn("INVMIN");
            objData.AddColumn("INVMAX");
            objData.AddColumn("INVTOT");
            objData.AddColumn("AGENT");
            objData.AddColumn("AGNTMIN");
            objData.AddColumn("AGNTMAX");
            objData.AddColumn("AGNTTOT");
            objData.AddColumn("GPMIN");
            objData.AddColumn("GPMAX");
            objData.AddColumn("GPTOT");
            objData.AddColumn("PARTY_NAME");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT A.*,B.*,C.*,PARTY_NAME FROM "
                        + "((SELECT MIN(INVOICE_NO) AS INVMIN,MAX(INVOICE_NO) AS INVMAX,(MAX(INVOICE_NO)-MIN(INVOICE_NO))+1 AS INVTOT "
                        + " FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER WHERE INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' "
                        + " AND LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot) + ") AS A,"
                        + "(SELECT LEFT(PARTY_CODE,2) AS AGENT,MIN(AGENT_SR_NO) AS AGNTMIN,MAX(AGENT_SR_NO) AS AGNTMAX,"
                        + "(MAX(AGENT_SR_NO)-MIN(AGENT_SR_NO))+1 AS AGNTTOT FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER "
                        + " WHERE INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND "
                        + " LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot) + ""
                        + " GROUP BY AGENT_ALPHA) AS B,"
                        + "(SELECT MIN(GATEPASS_NO) AS GPMIN,MAX(GATEPASS_NO) AS GPMAX,(MAX(GATEPASS_NO)-MIN(GATEPASS_NO))+1 AS GPTOT "
                        + " FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER WHERE INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' "
                        + "AND LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot) + ") AS C"
                        + ")"
                        + " LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER D"
                        + " ON D.PARTY_CODE=CONCAT(AGENT,'0000')"
                        + " WHERE MAIN_ACCOUNT_CODE=210072 ";
                
                
                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("INVMIN", Integer.toString(rsReport.getInt("INVMIN")));
                        objRow.setValue("INVMAX", Integer.toString(rsReport.getInt("INVMAX")));
                        objRow.setValue("INVTOT", Integer.toString(rsReport.getInt("INVTOT")));
                        objRow.setValue("AGENT", Integer.toString(rsReport.getInt("AGENT")));
                        objRow.setValue("AGNTMIN", Integer.toString(rsReport.getInt("AGNTMIN")));
                        objRow.setValue("AGNTMAX", Integer.toString(rsReport.getInt("AGNTMAX")));
                        objRow.setValue("AGNTTOT", Integer.toString(rsReport.getInt("AGNTTOT")));
                        objRow.setValue("GPMIN", Integer.toString(rsReport.getInt("GPMIN")));
                        objRow.setValue("GPMAX", Integer.toString(rsReport.getInt("GPMAX")));
                        objRow.setValue("GPTOT", Integer.toString(rsReport.getInt("GPTOT")));
                        objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptInvoiceAgent.rpt", Parameters, objData);

        }

    }//GEN-LAST:event_cmdinvsummaryActionPerformed

    private void cmdinvslp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdinvslp1ActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();
            objData.AddColumn("INVOICE_DATE");
            objData.AddColumn("TRANSPORTER_NAME");
            objData.AddColumn("TRANSPORTER_CODE");
            objData.AddColumn("PARTY_NAME");
            objData.AddColumn("STATION_CODE");
            objData.AddColumn("NET_AMOUNT");
            objData.AddColumn("BALE_NO");
            objData.AddColumn("GROSS_WEIGHT");
            objData.AddColumn("TRANSPORTER_CODE");
            objData.AddColumn("SR_NO");
            objData.AddColumn("LAST");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT INVOICE_DATE,TRANSPORTER_NAME,TRANSPORTER_CODE,PARTY_NAME,STATION_CODE,NET_AMOUNT,BALE_NO,GROSS_WEIGHT,TRANSPORTER_CODE, @i:= IF(TRANSPORTER_CODE = @last_outlet, @i + 1, 1) as SR_NO,"
                        + " @last_outlet := TRANSPORTER_CODE AS LAST"
                        + " from (SELECT  @i := 0, @last_outlet := NULL) h "
                        + " JOIN (SELECT H.INVOICE_DATE,H.TRANSPORTER_NAME,TRANSPORTER_CODE,PARTY_NAME,STATION_CODE,NET_AMOUNT,BALE_NO,GROSS_WEIGHT "
                        + " FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER H,DINESHMILLS.D_SAL_PARTY_MASTER M,(SELECT @a:= 0) AS a  "
                        + " WHERE H.PARTY_CODE=M.PARTY_CODE AND MAIN_ACCOUNT_CODE=210072 AND TRANSPORT_MODE=2 AND "
                        + " H.INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND "
                        + " H.LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot)
                        + " ORDER BY TRANSPORTER_CODE) I ";

                System.out.println(strSQL);
                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                        objRow.setValue("TRANSPORTER_NAME", rsReport.getString("TRANSPORTER_NAME"));
                        objRow.setValue("STATION_CODE", rsReport.getString("STATION_CODE"));
                        objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                        objRow.setValue("BALE_NO", rsReport.getString("BALE_NO"));
                        objRow.setValue("TRANSPORTER_CODE", Integer.toString(rsReport.getInt("TRANSPORTER_CODE")));
                        objRow.setValue("LAST", Integer.toString(rsReport.getInt("LAST")));
                        objRow.setValue("SR_NO", Integer.toString(rsReport.getInt("SR_NO")));
                        objRow.setValue("NET_AMOUNT", Double.toString(rsReport.getDouble("NET_AMOUNT")));
                        objRow.setValue("GROSS_WEIGHT", Double.toString(rsReport.getDouble("GROSS_WEIGHT")));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptTransport.rpt", Parameters, objData);

        }
    }//GEN-LAST:event_cmdinvslp1ActionPerformed

    private void cmdexciseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexciseActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();
            objData.AddColumn("INV");
            objData.AddColumn("INVOICE_DATE");
            objData.AddColumn("AGENT_ALPHA");
            objData.AddColumn("AGENT_SR_NO");
            objData.AddColumn("INVOICE_NO");
            objData.AddColumn("GATEPASS_NO");
            objData.AddColumn("BALE_NO");
            objData.AddColumn("TOTAL_SQ_MTR");
            objData.AddColumn("TOTAL_GROSS_QTY");
            objData.AddColumn("FLAG");
            objData.AddColumn("TOTAL_NET_QTY");
            objData.AddColumn("TOTAL_VALUE");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT 1 AS INV,H.INVOICE_DATE,H.AGENT_ALPHA,H.AGENT_SR_NO,H.INVOICE_NO,H.GATEPASS_NO,H.BALE_NO,H.TOTAL_SQ_MTR,H.TOTAL_GROSS_QTY,"
                        + "SUM(D.FLAG_DEF_CODE) AS FLAG,H.TOTAL_NET_QTY,H.TOTAL_VALUE "
                        + " FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER H, FILTERFABRIC.FF_TRD_INVOICE_DETAIL D"
                        + " WHERE H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=D.INVOICE_DATE AND "
                        + " H.INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND "
                        + " H.LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot)
                        + " GROUP BY D.INVOICE_DATE,D.INVOICE_NO  ";

                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                        objRow.setValue("AGENT_ALPHA", rsReport.getString("AGENT_ALPHA"));
                        objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                        objRow.setValue("GATEPASS_NO", rsReport.getString("GATEPASS_NO"));
                        objRow.setValue("BALE_NO", rsReport.getString("BALE_NO"));
                        objRow.setValue("FLAG", Integer.toString(rsReport.getInt("FLAG")));
                        objRow.setValue("INV", Integer.toString(rsReport.getInt("INV")));
                        objRow.setValue("AGENT_SR_NO", Integer.toString(rsReport.getInt("AGENT_SR_NO")));
                        objRow.setValue("TOTAL_SQ_MTR", Double.toString(rsReport.getDouble("TOTAL_SQ_MTR")));
                        objRow.setValue("TOTAL_GROSS_QTY", Double.toString(rsReport.getDouble("TOTAL_GROSS_QTY")));
                        objRow.setValue("TOTAL_NET_QTY", Double.toString(rsReport.getDouble("TOTAL_NET_QTY")));
                        objRow.setValue("TOTAL_VALUE", Double.toString(rsReport.getDouble("TOTAL_VALUE")));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptExcise.rpt", Parameters, objData);

        }
    }//GEN-LAST:event_cmdexciseActionPerformed

    private void cmdvatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdvatActionPerformed
        // TODO add your handling code here:
        String sql;
        if (cmbLot.isVisible()) {
            HashMap Parameters = new HashMap();
            Parameters.put("CURDATE", EITLERPGLOBAL.getCurrentDate());

            TTable objData = new TTable();
            objData.AddColumn("CHECK_POST");
            objData.AddColumn("STATION_CODE");
            objData.AddColumn("PARTY_NAME");
            objData.AddColumn("ADDRESS1");
            objData.AddColumn("ADDRESS2");
            objData.AddColumn("CITY_ID");
            objData.AddColumn("INVOICE_NO");
            objData.AddColumn("INVOICE_DATE");
            objData.AddColumn("TOTAL_GROSS_QTY");
            objData.AddColumn("PER");
            objData.AddColumn("NET_AMOUNT");
            objData.AddColumn("TIN");
            objData.AddColumn("PARTY_CODE");
            objData.AddColumn("TRANSPORTER_NAME");

            try {

                String strSQL = "";
                ResultSet rsReport;

                //Retrieve data
                strSQL = "SELECT H.CHECK_POST,H.STATION_CODE,P.PARTY_NAME,P.ADDRESS1,P.ADDRESS2,P.CITY_ID,H.INVOICE_NO,H.INVOICE_DATE,"
                        + "H.TOTAL_GROSS_QTY, CASE WHEN CST2>0 THEN 2 ELSE 5 END AS PER,H.NET_AMOUNT,"
                        + "CASE WHEN LENGTH(P.TIN_NO)>5 THEN P.TIN_NO ELSE '99999999999' END AS TIN,H.PARTY_CODE,H.TRANSPORTER_NAME "
                        + " FROM DINESHMILLS.D_SAL_PARTY_MASTER P,FILTERFABRIC.FF_TRD_INVOICE_HEADER H "
                        + " WHERE H.PARTY_CODE=P.PARTY_CODE AND MAIN_ACCOUNT_CODE=210072 AND VAT1=0  AND "
                        + " H.INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "' AND "
                        + " H.LOT_NO=" + EITLERPGLOBAL.getComboCode(cmbLot)
                        + " ORDER BY H.PARTY_CODE";

                System.out.println(strSQL);
                rsReport = data.getResult(strSQL);
                rsReport.first();

                if (rsReport.getRow() > 0) {

                    while (!rsReport.isAfterLast()) {
                        TRow objRow = new TRow();

                        objRow.setValue("INVOICE_DATE", rsReport.getString("INVOICE_DATE"));
                        objRow.setValue("CHECK_POST", rsReport.getString("CHECK_POST"));
                        objRow.setValue("INVOICE_NO", rsReport.getString("INVOICE_NO"));
                        objRow.setValue("STATION_CODE", rsReport.getString("STATION_CODE"));
                        objRow.setValue("PARTY_NAME", rsReport.getString("PARTY_NAME"));
                        objRow.setValue("ADDRESS1", rsReport.getString("ADDRESS1"));
                        objRow.setValue("ADDRESS2", rsReport.getString("ADDRESS2"));
                        objRow.setValue("CITY_ID", rsReport.getString("CITY_ID"));
                        objRow.setValue("TIN", rsReport.getString("TIN"));
                        objRow.setValue("TRANSPORTER_NAME", rsReport.getString("TRANSPORTER_NAME"));
                        objRow.setValue("PARTY_CODE", rsReport.getString("PARTY_CODE"));
                        objRow.setValue("PER", Double.toString(rsReport.getDouble("PER")));
                        objRow.setValue("NET_AMOUNT", Double.toString(rsReport.getDouble("NET_AMOUNT")));
                        objRow.setValue("TOTAL_GROSS_QTY", Double.toString(rsReport.getDouble("TOTAL_GROSS_QTY")));

                        objData.AddRow(objRow);
                        rsReport.next();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            objEngine.PreviewReport("http://" + EITLERPGLOBAL.HostIP + "/EITLERP/FilterTrading/Reports/rptVat.rpt", Parameters, objData);

        }
    }//GEN-LAST:event_cmdvatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbLot;
    private javax.swing.JButton cmbinvsmy;
    private javax.swing.JButton cmdexcise;
    private javax.swing.JButton cmdinvslp;
    private javax.swing.JButton cmdinvslp1;
    private javax.swing.JButton cmdinvsummary;
    private javax.swing.JButton cmdvat;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtinvdt;
    // End of variables declaration//GEN-END:variables

    private boolean GenerateCombo() {
        int i = 0;
        try {

            cmbLotModel = new EITLComboModel();
            cmbLot.removeAllItems();
            cmbLot.setModel(cmbLotModel);

            ResultSet rs = data.getResult("SELECT DISTINCT LOT_NO FROM FILTERFABRIC.FF_TRD_INVOICE_HEADER WHERE INVOICE_DATE='" + EITLERPGLOBAL.formatDateDB(txtinvdt.getText().trim()) + "'");
            rs.first();

            while (!rs.isAfterLast()) {
                i++;
                ComboData objData = new ComboData();
                objData.Text = rs.getString("LOT_NO");
                objData.Code = rs.getInt("LOT_NO");
                cmbLotModel.addElement(objData);
                rs.next();
            }
            if (i > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

        }
        return false;
    }
}