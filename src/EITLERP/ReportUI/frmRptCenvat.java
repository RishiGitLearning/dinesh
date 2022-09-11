/*
 * frmRptStockStatement.java
 *
 * Created on August 13, 2005, 3:21 PM
 */

package EITLERP.ReportUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import java.sql.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Utils.*;
import java.text.*;
import java.util.HashMap;
import java.util.Date;
import TReportWriter.*;

public class frmRptCenvat extends javax.swing.JApplet {
    
    private EITLComboModel cmbYearModel=new EITLComboModel();
    private EITLComboModel cmbItemTypeModel=new EITLComboModel();
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptStockStatement */
    public void init() {
        setSize(520, 220);
        initComponents();
        // Bar.setVisible(false);
        GenerateCombo();
        //chkReprocess.setEnabled(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cmbItemType = new javax.swing.JComboBox();
        cmbYear = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmdPrint = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 153, 204));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CENVAT REPORT");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(8, 8, 187, 15);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(1, 0, 520, 30);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        cmbItemType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbItemTypeItemStateChanged(evt);
            }
        });
        cmbItemType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbItemTypeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbItemTypeFocusLost(evt);
            }
        });

        jPanel2.add(cmbItemType);
        cmbItemType.setBounds(120, 20, 190, 23);

        cmbYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbYearFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbYearFocusLost(evt);
            }
        });

        jPanel2.add(cmbYear);
        cmbYear.setBounds(120, 70, 120, 23);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Item Type :");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 20, 100, 23);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Financial Year :");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 70, 100, 23);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        jPanel2.add(cmdPrint);
        cmdPrint.setBounds(340, 70, 119, 30);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(10, 50, 490, 130);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setText("....");
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 190, 500, 22);

    }//GEN-END:initComponents
    
    private void cmbYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbYearFocusLost
        // TODO add your handling code here:
        lblStatus.setText("...");
    }//GEN-LAST:event_cmbYearFocusLost
    
    private void cmbYearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbYearFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Select Financial Year");
    }//GEN-LAST:event_cmbYearFocusGained
    
    private void cmbItemTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbItemTypeFocusLost
        // TODO add your handling code here:
        lblStatus.setText("...");
    }//GEN-LAST:event_cmbItemTypeFocusLost
    
    private void cmbItemTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbItemTypeFocusGained
        // TODO add your handling code here:
        lblStatus.setText("Select Item Type");
    }//GEN-LAST:event_cmbItemTypeFocusGained
    
    private void cmbItemTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbItemTypeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbItemTypeItemStateChanged
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
        //if(EITLERPGLOBAL.getCombostrCode(cmbItemTypeModel).equals("2"))
        GenerateReportRaw();
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        try {
            
            cmbItemTypeModel=new EITLComboModel();
            cmbItemType.removeAll();
            cmbItemType.setModel(cmbItemTypeModel);
            
            ComboData objData=new ComboData();
            objData.Code=2;
            objData.Text="Raw Material";
            
            cmbItemTypeModel.addElement(objData);
            
            //ComboData objData=new ComboData();
            //objData.Code=2;
            //objData.Text="Raw Material";
            
            //cmbItemTypeModel.addElement(objData);
            
            
            cmbYearModel=new EITLComboModel();
            cmbYear.removeAll();
            cmbYear.setModel(cmbYearModel);
            
            
            String Qry = "SELECT YEAR_FROM,YEAR_TO FROM D_COM_FIN_YEAR WHERE COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " ORDER BY YEAR_TO DESC";
            
            ResultSet rs = data.getResult(Qry);
            //ComboDatobjData;
            while(!rs.isAfterLast()) {
                objData=new ComboData();
                objData.Code=Long.parseLong(rs.getString("YEAR_FROM")+rs.getString("YEAR_TO"));
                objData.Text=rs.getString("YEAR_FROM")+"-"+rs.getString("YEAR_TO");
                cmbYearModel.addElement(objData);
                rs.next();
            }
            
            
            
        }
        catch(Exception e) {
            
        }
        
    }
    private void GenerateReportGen() {
        
    }
    private void GenerateReportRaw() {
        
        String SQL="";
        ResultSet rsItem,rsLot,rsIssue,rsSTM;
        
        try {
            
            
            
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESC");
            objReportData.AddColumn("AUTO_LOT_NO");
            objReportData.AddColumn("OPENING_QTY");
            objReportData.AddColumn("OPENING_VALUE");
            objReportData.AddColumn("OPENING_RATE");
            objReportData.AddColumn("RECEIPT_QTY");
            objReportData.AddColumn("RECEIPT_VALUE");
            objReportData.AddColumn("RECEIPT_RATE");
            objReportData.AddColumn("ISSUE_QTY");
            objReportData.AddColumn("ISSUE_VALUE");
            objReportData.AddColumn("ISSUE_RATE");
            objReportData.AddColumn("CLOSING_QTY");
            objReportData.AddColumn("CLOSING_VALUE");
            objReportData.AddColumn("CLOSING_RATE");
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_DESC","");
            objOpeningRow.setValue("AUTO_LOT_NO","");
            objOpeningRow.setValue("OPENING_QTY","");
            objOpeningRow.setValue("OPENING_VALUE","");
            objOpeningRow.setValue("OPENING_RATE","");
            objOpeningRow.setValue("RECEIPT_QTY","");
            objOpeningRow.setValue("RECEIPT_VALUE","");
            objOpeningRow.setValue("RECEIPT_RATE","");
            objOpeningRow.setValue("ISSUE_QTY","");
            objOpeningRow.setValue("ISSUE_VALUE","");
            objOpeningRow.setValue("ISSUE_RATE","");
            objOpeningRow.setValue("CLOSING_QTY","");
            objOpeningRow.setValue("CLOSING_VALUE","");
            objOpeningRow.setValue("CLOSING_RATE","");
            
            
            String FromDate = String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(0,4)+"-04-01";
            String ToDate = String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(4,8)+"-03-31";
            
            //======= Find the last cut-off date stock entry =================//
            ResultSet rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+FromDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            String StockEntryNo="";
            String StockEntryDate="";
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getString("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            
            
            
            
            //================================================================//
            String t1 = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB());
            String t2 = EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.getCurrentDateDB());
            //System.out.println("  = " + StockEntryNo + "===" + StockEntryDate);
            SQL = "SELECT  DISTINCT ITEM_ID "+
            "FROM D_COM_OPENING_STOCK_DETAIL   "+
            "WHERE  ITEM_ID LIKE 'RM%' AND ENTRY_NO = 9 AND(CENVAT_AMOUNT <> '' OR CENVAT_AMOUNT IS NOT NULL)  "+
            "UNION "+
            "SELECT DISTINCT B.ITEM_ID "+
            "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B,D_INV_GRN_LOT C "+
            "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_NO = C.GRN_NO AND B.ITEM_ID = C.ITEM_ID "+
            "AND B.SR_NO = C.GRN_SR_NO "+
            "AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 "+
            "AND A.GRN_DATE>='" + StockEntryDate + "' AND A.GRN_DATE<='" + ToDate + "'  "+
            "AND B.ITEM_ID LIKE 'RM%'  "+
            "AND(B.COLUMN_8_AMT>0  OR A.COLUMN_8_AMT > 0 ) "+
            "ORDER BY 1";
            
            rsItem = data.getResult(SQL);
            String ItemID="";
            double OpeningQty=0,OpeningAmount=0,OpeningRate=0;
            double ReceiptQty=0,ReceiptAmount=0,ReceiptRate=0;
            double IssueQty=0,IssueAmount=0,IssueRate=0;
            
            
            String AutoLotNo="";
            int counter=0;
            double LotRate=0,LotQty=0;
            if(rsItem.getRow()>0) {
                
                while(!rsItem.isAfterLast()) {
                    counter++;
                    OpeningQty=0;
                    OpeningAmount=0;
                    OpeningRate=0;
                    ItemID = rsItem.getString("ITEM_ID").trim();
                    if(ItemID.equals("RM12146001"))
                    {
                        boolean t = true;
                    }
                    
                    SQL = "SELECT 'O'AS OPENING ,LOT_NO AS AUTO_LOT_NO , OPENING_QTY AS QTY, CENVAT_AMOUNT " +
                    "FROM D_COM_OPENING_STOCK_DETAIL " +
                    "WHERE ENTRY_NO = '9' AND ITEM_ID = '" + ItemID + "' AND (CENVAT_AMOUNT <> '' OR CENVAT_AMOUNT IS NOT NULL) "+
                    "UNION "+
                    "SELECT 'O'AS OPENING,C.AUTO_LOT_NO AS AUTO_LOT_NO,C.LOT_RECEIVED_QTY AS QTY,IF(B.COLUMN_8_AMT > 0,B.COLUMN_8_AMT,A.COLUMN_8_AMT) AS CENVAT_AMOUNT " +
                    "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B,D_INV_GRN_LOT C " +
                    "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_NO = C.GRN_NO AND B.ITEM_ID = C.ITEM_ID " +
                    "AND B.SR_NO = C.GRN_SR_NO AND A.COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 " +
                    "AND A.GRN_DATE>='" + StockEntryDate + "' AND A.GRN_DATE<'" + FromDate + "' AND B.ITEM_ID ='" + ItemID + "' " +
                    "AND(B.COLUMN_8_AMT>0  OR A.COLUMN_8_AMT) " +
                    "UNION "+
                    "SELECT 'R'AS OPENING,C.AUTO_LOT_NO AS AUTO_LOT_NO,C.LOT_RECEIVED_QTY AS QTY,IF(B.COLUMN_8_AMT > 0,B.COLUMN_8_AMT,A.COLUMN_8_AMT) AS CENVAT_AMOUNT "+
                    "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B,D_INV_GRN_LOT C "+
                    "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_NO = C.GRN_NO AND B.ITEM_ID = C.ITEM_ID "+
                    "AND B.SR_NO = C.GRN_SR_NO AND A.COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 "+
                    "AND A.GRN_DATE>='" + FromDate + "' AND A.GRN_DATE<='" + ToDate + "' AND B.ITEM_ID ='" + ItemID + "' "+
                    "AND(B.COLUMN_8_AMT>0  OR A.COLUMN_8_AMT) "+
                    "ORDER BY AUTO_LOT_NO";
                    
                    rsLot = data.getResult(SQL);
                    if(rsLot.getRow()>0) {
                        while(!rsLot.isAfterLast()) {
                            OpeningQty=0;
                            OpeningAmount=0;
                            OpeningRate=0;
                            String Flag = rsLot.getString("OPENING");
                            AutoLotNo = rsLot.getString("AUTO_LOT_NO");
                            double Qty = rsLot.getDouble("QTY");
                            double CenvatAmount = rsLot.getDouble("CENVAT_AMOUNT");
                            double CenvatRate = EITLERPGLOBAL.round(rsLot.getDouble("CENVAT_AMOUNT") / rsLot.getDouble("QTY"),3);
                            
                            if(Flag.equals("O")) {
                                
                                SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY " +
                                "FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B ,D_INV_ISSUE_LOT C "+
                                "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.ISSUE_NO = B.ISSUE_NO " +
                                "AND A.APPROVED=1 AND A.CANCELED=0 AND B.ISSUE_NO = C.ISSUE_NO  "+
                                "AND B.ITEM_CODE = C.ITEM_ID AND B.SR_NO = C.ISSUE_SR_NO " +
                                "AND A.ISSUE_DATE >= '" + StockEntryDate + "' AND A.ISSUE_DATE < '" + FromDate + "' "+
                                "AND B.ITEM_CODE = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                
                                rsIssue = data.getResult(SQL);
                                IssueQty=EITLERPGLOBAL.round(rsIssue.getDouble("QTY"),3);
                                if(IssueQty>0) {
                                    boolean t = true;
                                }
                                
                                SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY "+
                                "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B ,D_INV_STM_LOT C "+
                                "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.STM_NO = B.STM_NO AND B.STM_NO = C.STM_NO  "+
                                "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO  "+
                                "AND A.STM_DATE >= '" + StockEntryDate + "' AND A.STM_DATE < '" + FromDate + "' "+
                                "AND B.ITEM_ID = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                
                                rsSTM = data.getResult(SQL);
                                
                                IssueQty +=EITLERPGLOBAL.round(rsSTM.getDouble("QTY"),3);
                                
                                objRow=objReportData.newRow();
                                objRow.setValue("SR_NO",String.valueOf(counter));
                                objRow.setValue("ITEM_ID",ItemID);
                                objRow.setValue("ITEM_DESC",clsItem.getItemName(EITLERPGLOBAL.gCompanyID,ItemID));
                                objRow.setValue("AUTO_LOT_NO",String.valueOf(AutoLotNo));
                                
                                OpeningQty=EITLERPGLOBAL.round(Qty-IssueQty,3);
                                OpeningAmount=EITLERPGLOBAL.round((OpeningQty*CenvatRate),3);
                                OpeningRate=CenvatRate;
                                
                                if((Qty-IssueQty) > 0 ) {
                                    objRow.setValue("OPENING_QTY",String.valueOf(OpeningQty));
                                    objRow.setValue("OPENING_VALUE",String.valueOf(OpeningAmount));
                                    objRow.setValue("OPENING_RATE",String.valueOf(OpeningRate));
                                    
                                    objRow.setValue("RECEIPT_QTY","0.0");
                                    objRow.setValue("RECEIPT_VALUE","0.0");
                                    objRow.setValue("RECEIPT_RATE","0.0");
                                    
                                    
                                    
                                    SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY " +
                                    "FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B ,D_INV_ISSUE_LOT C "+
                                    "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.ISSUE_NO = B.ISSUE_NO " +
                                    "AND A.APPROVED=1 AND A.CANCELED=0 AND B.ISSUE_NO = C.ISSUE_NO  "+
                                    "AND B.ITEM_CODE = C.ITEM_ID AND B.SR_NO = C.ISSUE_SR_NO " +
                                    "AND A.ISSUE_DATE >= '" + FromDate + "' AND A.ISSUE_DATE <= '" + ToDate + "' "+
                                    "AND B.ITEM_CODE = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                    
                                    rsIssue = data.getResult(SQL);
                                    IssueQty=EITLERPGLOBAL.round(rsIssue.getDouble("QTY"),3);
                                    if(IssueQty>0) {
                                        boolean t = true;
                                    }
                                    
                                    SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY "+
                                    "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B ,D_INV_STM_LOT C "+
                                    "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.STM_NO = B.STM_NO AND B.STM_NO = C.STM_NO  "+
                                    "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO  "+
                                    "AND A.STM_DATE >= '" + FromDate + "' AND A.STM_DATE <= '" + ToDate + "' "+
                                    "AND B.ITEM_ID = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                    
                                    rsSTM = data.getResult(SQL);
                                    
                                    IssueQty +=EITLERPGLOBAL.round(rsSTM.getDouble("QTY"),3);
                                    
                                    //IssueQty=OpeningQty-IssueQty;
                                    IssueAmount=EITLERPGLOBAL.round((IssueQty*CenvatRate),3);
                                    IssueRate=CenvatRate;
                                    
                                    objRow.setValue("ISSUE_QTY",String.valueOf(EITLERPGLOBAL.round(IssueQty,3)));
                                    objRow.setValue("ISSUE_VALUE",String.valueOf(IssueAmount));
                                    objRow.setValue("ISSUE_RATE",String.valueOf(IssueRate));
                                    
                                    objRow.setValue("CLOSING_QTY",String.valueOf(EITLERPGLOBAL.round(OpeningQty - IssueQty,3)));
                                    objRow.setValue("CLOSING_VALUE",String.valueOf(EITLERPGLOBAL.round((OpeningQty - IssueQty)*CenvatRate,3)));
                                    objRow.setValue("CLOSING_RATE","0.0");
                                    objReportData.AddRow(objRow);
                                }
                                
                            }
                            else if(Flag.equals("R")) {
                                
                                objRow=objReportData.newRow();
                                objRow.setValue("SR_NO",String.valueOf(counter));
                                objRow.setValue("ITEM_ID",ItemID);
                                objRow.setValue("ITEM_DESC",clsItem.getItemName(EITLERPGLOBAL.gCompanyID,ItemID));
                                objRow.setValue("AUTO_LOT_NO",String.valueOf(AutoLotNo));
                                
                                objRow.setValue("OPENING_QTY","0.0");
                                objRow.setValue("OPENING_VALUE","0.0");
                                objRow.setValue("OPENING_RATE","0.0");
                                
                                //double Qty = rsLot.getDouble("QTY");
                                //double CenvatAmount = rsLot.getDouble("CENVAT_AMOUNT");
                                //double CenvatRate = EITLERPGLOBAL.round(rsLot.getDouble("CENVAT_AMOUNT") / rsLot.getDouble("QTY"),3);
                                
                                
                                objRow.setValue("RECEIPT_QTY",String.valueOf(Qty));
                                objRow.setValue("RECEIPT_VALUE",String.valueOf(CenvatAmount));
                                objRow.setValue("RECEIPT_RATE",String.valueOf(CenvatRate));
                                
                                
                                
                                SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY " +
                                "FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B ,D_INV_ISSUE_LOT C "+
                                "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.ISSUE_NO = B.ISSUE_NO " +
                                "AND A.APPROVED=1 AND A.CANCELED=0 AND B.ISSUE_NO = C.ISSUE_NO  "+
                                "AND B.ITEM_CODE = C.ITEM_ID AND B.SR_NO = C.ISSUE_SR_NO " +
                                "AND A.ISSUE_DATE >= '" + FromDate + "' AND A.ISSUE_DATE <= '" + ToDate + "' "+
                                "AND B.ITEM_CODE = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                
                                rsIssue = data.getResult(SQL);
                                IssueQty=EITLERPGLOBAL.round(rsIssue.getDouble("QTY"),3);
                                if(IssueQty>0) {
                                    boolean t = true;
                                }
                                
                                SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY "+
                                "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B ,D_INV_STM_LOT C "+
                                "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.STM_NO = B.STM_NO AND B.STM_NO = C.STM_NO  "+
                                "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO  "+
                                "AND A.STM_DATE >= '" + FromDate + "' AND A.STM_DATE < '" + ToDate + "' "+
                                "AND B.ITEM_ID = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                                
                                rsSTM = data.getResult(SQL);
                                
                                IssueQty +=EITLERPGLOBAL.round(rsSTM.getDouble("QTY"),3);
                                IssueAmount=EITLERPGLOBAL.round((IssueQty*CenvatRate),3);
                                IssueRate=CenvatRate;
                                objRow.setValue("ISSUE_QTY",String.valueOf(EITLERPGLOBAL.round(IssueQty,3)));
                                objRow.setValue("ISSUE_VALUE",String.valueOf(IssueAmount));
                                objRow.setValue("ISSUE_RATE",String.valueOf(IssueRate));
                                
                                
                                objRow.setValue("CLOSING_QTY",String.valueOf(EITLERPGLOBAL.round(Qty - IssueQty,3)));
                                objRow.setValue("CLOSING_VALUE",String.valueOf(EITLERPGLOBAL.round((Qty - IssueQty)*CenvatRate,3)));
                                objRow.setValue("CLOSING_RATE",String.valueOf(CenvatRate));
                                objReportData.AddRow(objRow);
                            }
                            rsLot.next();
                        }
                    }
                    rsItem.next();
                }
            }
            HashMap Parameters=new HashMap();
            Parameters.put("COMPANY_NAME",clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID).toUpperCase());
            Parameters.put("FROM_MONTH","APRIL'" + String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(0,4));
            Parameters.put("TO_MONTH","MARCH'" + String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(4,8));
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptCenvat.rpt",Parameters,objReportData);
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}