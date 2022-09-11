/*
 * frmRptLotWiseItemRM.java
 *
 * Created on January 23, 2010, 12:50 AM
 */

package EITLERP.Stores;
import java.sql.*;
import EITLERP.*;
import TReportWriter.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import javax.swing.*;
/**
 *
 * @author  PrathmeshShah
 */
public class frmRptLotWiseItemRM_old extends javax.swing.JApplet {
    int counter=0;
    private TReportEngine objEngine=new TReportEngine();
    /** Initializes the applet frmRptLotWiseItemRM */
    public void init() {
        initComponents();
        setSize(300, 200);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        txtAsOnDate = new javax.swing.JTextField();
        cmbPrint = new javax.swing.JButton();
        cmbExit = new javax.swing.JButton();

        getContentPane().setLayout(null);

        jLabel1.setText("As on Date :");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(30, 20, 90, 15);

        txtAsOnDate.setText("31/12/2009");
        getContentPane().add(txtAsOnDate);
        txtAsOnDate.setBounds(130, 20, 110, 19);

        cmbPrint.setText("Print");
        cmbPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPrintActionPerformed(evt);
            }
        });

        getContentPane().add(cmbPrint);
        cmbPrint.setBounds(90, 80, 63, 25);

        cmbExit.setText("Exit");
        cmbExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbExitActionPerformed(evt);
            }
        });

        getContentPane().add(cmbExit);
        cmbExit.setBounds(230, 80, 57, 25);

    }//GEN-END:initComponents
    
    private void cmbPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPrintActionPerformed
        // TODO add your handling code here:
        TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
        
        objData.AddColumn("SR_NO");
        objData.AddColumn("ITEM_CODE");
        objData.AddColumn("ITEM_DESC");
        objData.AddColumn("UNIT_DESC");
        objData.AddColumn("ITEM_LOT_NO");
        objData.AddColumn("AUTO_LOT_NO");
        objData.AddColumn("STOCK_QTY");
        objData.AddColumn("STOCK_VALUE");
        
        try{
            if(txtAsOnDate.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null,"Please Enter Date");
                return;
            }
            if(!EITLERPGLOBAL.isDate(txtAsOnDate.getText()))
            {
                JOptionPane.showMessageDialog(null,"Invalid Date Please Enter valid date");
                return;
            }
            try {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date fromDate = df.parse(txtAsOnDate.getText());
                Date RestrictDate = df.parse("01/10/2009");
                
                
                if(fromDate.before(RestrictDate)) {
                    JOptionPane.showMessageDialog(null,"From Date must be equals or after 01/10/2009");
                    return;
                }
            }
            catch(Exception e) {
                
            }
            
            TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();
            int StockEntryNo=0;
            String ItemId="",StockEntryDate="";
            String Qry = "SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText()) +"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY ENTRY_DATE DESC";
            ResultSet rsTmp= data.getResult(Qry, clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
            rsTmp.first();
            
            StockEntryNo=rsTmp.getInt("ENTRY_NO");
            StockEntryDate=rsTmp.getString("ENTRY_DATE");
            rsTmp.next();
            
            
            
            ResultSet rsMain;
            Qry="(SELECT A.ITEM_ID,A.LOT_NO  AS AUTO_LOT_NO, A.ITEM_LOT_NO, A.OPENING_QTY AS QTY,A.OPENING_VALUE AS VALUE, A.OPENING_RATE AS RATE "+
            "FROM D_COM_OPENING_STOCK_DETAIL A, D_INV_ITEM_MASTER B WHERE "+
            "A.ITEM_ID = B.ITEM_ID AND A.COMPANY_ID = B.COMPANY_ID AND B.GROUP_CODE=3 AND A.OPENING_QTY >0 AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND "+
            "A.ENTRY_NO ="+StockEntryNo+")"+" UNION "+
            "(SELECT DISTINCT B.ITEM_ID, AUTO_LOT_NO AS AUTO_LOT_NO, C.ITEM_LOT_NO,C.LOT_ACCEPTED_QTY AS QTY,C.LOT_ACCEPTED_QTY*B.LANDED_RATE AS VALUE, B.LANDED_RATE AS RATE "+
            "FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B, D_INV_GRN_LOT C "+
            "WHERE A.GRN_NO = B.GRN_NO AND A.GRN_NO=C.GRN_NO AND B.GRN_NO=C.GRN_NO "+
            "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID AND A.COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" "+
            "AND B.COMPANY_ID =C.COMPANY_ID AND B.SR_NO = C.GRN_SR_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND "+
            "A.GRN_DATE >='"+StockEntryDate +"' AND A.GRN_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText())+ "' ) ORDER BY ITEM_ID, AUTO_LOT_NO";
            
            rsMain=data.getResult(Qry,clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
            
            rsMain.first();
            
            while (!rsMain.isAfterLast()){
                counter++;
                double ItemLotStockQty=0, ItemLotStockValue=0;
                ItemId= rsMain.getString("ITEM_ID");
                String AutoLotNo= rsMain.getString("AUTO_LOT_NO");
                String LotNo= rsMain.getString("ITEM_LOT_NO");
                ItemLotStockQty=EITLERPGLOBAL.round(rsMain.getDouble("QTY"),3);
                ItemLotStockValue=EITLERPGLOBAL.round(rsMain.getDouble("VALUE"),3);
                double ItemRate = EITLERPGLOBAL.round(rsMain.getDouble("RATE"),3);
                System.out.println("Item ID : "+ItemId + " Auto Lot No:"+ AutoLotNo+" Qty.: "+ItemLotStockQty+ " Value:"+ItemLotStockValue);
                
                Qry="SELECT C.ISSUED_LOT_QTY,C.ISSUED_LOT_QTY*B.RATE AS RECEIPT_VALUE FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B, D_INV_ISSUE_LOT C "+
                "WHERE A.ISSUE_NO = B.ISSUE_NO AND A.ISSUE_NO=C.ISSUE_NO AND B.ISSUE_NO=C.ISSUE_NO "+
                "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID AND B.COMPANY_ID =C.COMPANY_ID "+
                "AND B.ITEM_CODE = C.ITEM_ID AND B.ITEM_CODE = '"+ItemId+"'  AND C.AUTO_LOT_NO='"+AutoLotNo+"' "+
                "AND B.SR_NO = C.ISSUE_SR_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+
                "AND A.ISSUE_DATE >='"+StockEntryDate+"' AND A.ISSUE_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText())+ "' ";
                
                
                double IssueQty=0;
                double IssueValue=0;
                double tempQty = 0;
                rsTmp = data.getResult(Qry,clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
                rsTmp.first();
                if (rsTmp.getRow()>0){
                    while (!rsTmp.isAfterLast()) {
                        tempQty = EITLERPGLOBAL.round(rsTmp.getDouble("C.ISSUED_LOT_QTY"),3);
                        IssueQty+=tempQty;
                        IssueValue+=EITLERPGLOBAL.round(ItemRate*tempQty,3);
                        rsTmp.next();
                    }
                }
                ItemLotStockQty-= EITLERPGLOBAL.round(IssueQty,3);
                ItemLotStockValue-=EITLERPGLOBAL.round(IssueValue,3);
                
                System.out.println("Issue Qty : "+ IssueQty+ " IssueValue :"+ IssueValue);
                System.out.println("Closing Qty : "+ ItemLotStockQty+ " ClosingValue :"+ ItemLotStockValue);
                
                //                if(ItemLotStockQty>0  ) {
                
                objRow=objData.newRow();
                String ItemDescription = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, ItemId);
                int UnitID = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, ItemId);
                
                
                String UnitDesc =clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT", UnitID);
                objRow.setValue("SR_NO",String.valueOf(counter));
                objRow.setValue("ITEM_CODE",ItemId);
                objRow.setValue("ITEM_DESC",ItemDescription);
                objRow.setValue("UNIT_DESC",UnitDesc);
                objRow.setValue("ITEM_LOT_NO",LotNo);
                objRow.setValue("AUTO_LOT_NO",AutoLotNo);
                objRow.setValue("STOCK_QTY",String.valueOf(EITLERPGLOBAL.round(ItemLotStockQty,3)));
                objRow.setValue("STOCK_VALUE",String.valueOf(EITLERPGLOBAL.round(ItemLotStockValue,3)));
                objData.AddRow(objRow);
                //                }
                
                
                rsMain.next();
            }
            String ItemDesc="";
            if(EITLERPGLOBAL.gCompanyID ==2 ) {
                ItemDesc="OF RAW MATERIAL FOR BARODA";
            }
            if(EITLERPGLOBAL.gCompanyID ==3 ) {
                ItemDesc="OF RAW MATERIAL FOR ANKLESHWAR.";
            }
            
            HashMap Parameters=new HashMap();
            Parameters.put("RUN_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("ITEM_TYPE",ItemDesc);
            Parameters.put("AS_ON_DATE",txtAsOnDate.getText());
            
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/ItemLotWiseStockRM.rpt",Parameters,objData);
            
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_cmbPrintActionPerformed
    
    private void cmbExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbExitActionPerformed
        // TODO add your handling code here:
        //(JFrame).getParent().dispose();
        // ((JFrame)getParent().getParent().getParent().getParent()).dispose();\
    }//GEN-LAST:event_cmbExitActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmbExit;
    private javax.swing.JButton cmbPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtAsOnDate;
    // End of variables declaration//GEN-END:variables
    
}