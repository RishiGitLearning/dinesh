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

public class frmRptCenvatOldNew extends javax.swing.JApplet {
    
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
        ResultSet rsItem,rsLot,rsIssue,rsSTM,rsTmpLot,rsTmpIssue,rsTmpSTM;
        
        try {
            
            
            String FromDate = String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(0,4)+"-04-01";
            String ToDate = String.valueOf(EITLERPGLOBAL.getComboCode(cmbYear)).substring(4,8)+"-03-31";
            
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
            System.out.println("  = " + StockEntryNo + "===" + StockEntryDate);
            SQL = "SELECT  DISTINCT ITEM_ID "+
            "FROM D_COM_OPENING_STOCK_DETAIL   "+
            "WHERE  ENTRY_NO = '" + StockEntryNo + "' AND(CENVAT_AMOUNT <> '' OR CENVAT_AMOUNT IS NOT NULL)  "+
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
                    if(ItemID.equals("RM11121001")) {
                        boolean falt = true;
                    }
                    System.out.println(ItemID);
                    
                    
                    
                    
                    SQL ="SELECT 'O' AS TYPE,ITEM_ID,LOT_NO,OPENING_QTY AS QTY,OPENING_RATE,CENVAT_AMOUNT "+
                    "FROM D_COM_OPENING_STOCK_DETAIL   "+
                    "WHERE COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "' AND ENTRY_NO = '" + StockEntryNo + "' AND ITEM_ID='" + ItemID + "' " +
                    "AND (CENVAT_AMOUNT <> '' OR CENVAT_AMOUNT IS NOT NULL) "+
                    "UNION "+
                    "SELECT 'R' AS TYPE,B.ITEM_ID,C.AUTO_LOT_NO AS LOT_NO,C.LOT_RECEIVED_QTY AS QTY,B.LANDED_RATE, "+
                    "ROUND(IF(B.COLUMN_8_AMT > 0,B.COLUMN_8_AMT,A.COLUMN_8_AMT),3) AS CENVAT_AMOUNT "+
                    "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B,D_INV_GRN_LOT C "+
                    "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_NO = C.GRN_NO AND B.ITEM_ID = C.ITEM_ID "+
                    "AND B.SR_NO = C.GRN_SR_NO AND A.COMPANY_ID='" + EITLERPGLOBAL.gCompanyID + "'"+
                    "AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 "+
                    "AND A.GRN_DATE>='" + FromDate + "' AND A.GRN_DATE<='" + ToDate + "'  "+
                    "AND B.ITEM_ID ='" + ItemID + "'  "+
                    "AND(B.COLUMN_8_AMT>0  OR A.COLUMN_8_AMT) "+
                    "ORDER BY ITEM_ID,LOT_NO";
                    
                    rsLot = data.getResult(SQL);
                    if(rsLot.getRow()>0) {
                        while(!rsLot.isAfterLast()) {
                            String Type = rsLot.getString("TYPE");
                            AutoLotNo = rsLot.getString("LOT_NO");
                            if(AutoLotNo.equals("100172")) {
                                boolean falt2 = true;
                            }
                            
                            double CenvatRate = 0.0;
                            objRow=objReportData.newRow();
                            objRow.setValue("SR_NO",String.valueOf(counter));
                            objRow.setValue("ITEM_ID",ItemID);
                            objRow.setValue("ITEM_DESC",clsItem.getItemName(EITLERPGLOBAL.gCompanyID,ItemID));
                            objRow.setValue("AUTO_LOT_NO",String.valueOf(AutoLotNo));
                            
                            if(Type.equals("O")) {
                                objRow.setValue("OPENING_QTY",rsLot.getString("QTY"));
                                objRow.setValue("OPENING_VALUE",rsLot.getString("CENVAT_AMOUNT"));
                                CenvatRate = EITLERPGLOBAL.round(rsLot.getDouble("CENVAT_AMOUNT") / rsLot.getDouble("QTY"),3);
                                objRow.setValue("OPENING_RATE","0.0");
                            }
                            else {
                                objRow.setValue("OPENING_QTY","0.0");
                                objRow.setValue("OPENING_VALUE","0.0");
                                objRow.setValue("OPENING_RATE","0.0");
                            }
                            if(Type.equals("R")) {
                                objRow.setValue("RECEIPT_QTY",rsLot.getString("QTY"));
                                CenvatRate = EITLERPGLOBAL.round(rsLot.getDouble("CENVAT_AMOUNT") / rsLot.getDouble("QTY"),3);
                                //objRow.setValue("RECEIPT_VALUE",String.valueOf(EITLERPGLOBAL.round(CenvatRate * rsLot.getDouble("QTY"),3)));
                                objRow.setValue("RECEIPT_VALUE",String.valueOf(EITLERPGLOBAL.round(rsLot.getDouble("CENVAT_AMOUNT"),3)));
                                objRow.setValue("RECEIPT_RATE","0.0");
                            }
                            else {
                                objRow.setValue("RECEIPT_QTY","0.0");
                                objRow.setValue("RECEIPT_VALUE","0.0");
                                objRow.setValue("RECEIPT_RATE","0.0");
                            }
                            
                            //----CHECK ISSUE QTY
                            SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B ,D_INV_ISSUE_LOT C "+
                            "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.ISSUE_NO = B.ISSUE_NO AND B.ISSUE_NO = C.ISSUE_NO  "+
                            "AND B.ITEM_CODE = C.ITEM_ID AND B.SR_NO = C.ISSUE_SR_NO "+
                            "AND B.ITEM_CODE = '" + ItemID + "' AND A.ISSUE_DATE>='" + FromDate + "'  AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                            
                            rsIssue = data.getResult(SQL);
                            
                            if(rsIssue.getRow()>0) {
                                IssueQty=rsIssue.getDouble("QTY");
                            }
                            
                            //----CHECK ISSUE STM QTY
                            SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B ,D_INV_STM_LOT C "+
                            "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.STM_NO = B.STM_NO AND B.STM_NO = C.STM_NO "+
                            "AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO "+
                            "AND B.ITEM_ID = '" + ItemID + "' AND A.STM_DATE>='" + FromDate + "'  AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                            
                            rsSTM = data.getResult(SQL);
                            
                            if(rsSTM.getRow()>0) {
                                IssueQty -=rsSTM.getDouble("QTY");
                            }
                            
                            /*SQL = "SELECT SUM(C.ISSUED_LOT_QTY) AS QTY "+
                            "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B ,D_INV_STM_LOT C "+
                            "WHERE A.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND A.STM_NO = B.STM_NO AND B.STM_NO = C.STM_NO  "+
                            "AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO  "+
                            "AND B.ITEM_ID = '" + ItemID + "' AND C.AUTO_LOT_NO ='" + AutoLotNo + "' ";
                             
                            rsSTM = data.getResult(SQL);
                             
                            if(rsSTM.getRow()>0) {
                                IssueQty +=rsSTM.getDouble("QTY");
                            }
                             */
                            objRow.setValue("ISSUE_QTY",String.valueOf(IssueQty));
                            objRow.setValue("ISSUE_VALUE",String.valueOf(EITLERPGLOBAL.round(IssueQty*CenvatRate,3)));
                            objRow.setValue("ISSUE_RATE","");
                            objRow.setValue("CLOSING_QTY",String.valueOf(EITLERPGLOBAL.round((rsLot.getDouble("QTY") - IssueQty),3)));
                            objRow.setValue("CLOSING_VALUE",String.valueOf(EITLERPGLOBAL.round((rsLot.getDouble("QTY") - IssueQty) * CenvatRate ,3)));
                            objRow.setValue("CLOSING_RATE","");
                            objReportData.AddRow(objRow);
                            IssueQty=0.0;
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
