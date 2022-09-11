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
import EITLERP.Finance.FinanceGlobal;
/**
 *
 * @author  PrathmeshShah
 */
public class frmRptLotWiseItemRM extends javax.swing.JApplet {
    int counter=0;
    private TReportEngine objEngine=new TReportEngine();
    private EITLComboModel cmbItemTypeModel= new EITLComboModel();
    /** Initializes the applet frmRptLotWiseItemRM */
    public void init() {
        initComponents();
        GenerateCombo();
        setSize(370, 200);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        cmbExit = new javax.swing.JButton();
        txtAsOnDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbPrint = new javax.swing.JButton();
        cmbItemType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        chkZeroInclude = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        cmbExit.setText("Exit");
        cmbExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbExitActionPerformed(evt);
            }
        });

        jPanel1.add(cmbExit);
        cmbExit.setBounds(160, 110, 57, 25);

        jPanel1.add(txtAsOnDate);
        txtAsOnDate.setBounds(100, 80, 110, 19);

        jLabel1.setText("As on Date :");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 80, 80, 15);

        cmbPrint.setText("Print");
        cmbPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPrintActionPerformed(evt);
            }
        });

        jPanel1.add(cmbPrint);
        cmbPrint.setBounds(80, 110, 63, 25);

        jPanel1.add(cmbItemType);
        cmbItemType.setBounds(100, 20, 190, 24);

        jLabel2.setText("Item Type :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 20, 70, 20);

        chkZeroInclude.setSelected(true);
        chkZeroInclude.setText("Inclue Zero Qty Lot");
        jPanel1.add(chkZeroInclude);
        chkZeroInclude.setBounds(100, 50, 150, 20);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(30, 40, 320, 150);

        jPanel2.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));
        jPanel2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("LOT WISE ITEM STOCK INFORMATION");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(9, 8, 240, 15);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(1, 2, 800, 30);

        jLabel7.setText("ITEM STOCK INFORMATION");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(9, 8, 230, 15);

    }//GEN-END:initComponents
    
    private void cmbPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPrintActionPerformed
        // TODO add your handling code here:
        if(EITLERPGLOBAL.getComboCode(cmbItemType) == 1) {
            
        }
        else {
            GenerateReportRaw();
        }
    }//GEN-LAST:event_cmbPrintActionPerformed
    
    private void cmbExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbExitActionPerformed
        // TODO add your handling code here:
        //(JFrame).getParent().dispose();
        // ((JFrame)getParent().getParent().getParent().getParent()).dispose();\
    }//GEN-LAST:event_cmbExitActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkZeroInclude;
    private javax.swing.JButton cmbExit;
    private javax.swing.JComboBox cmbItemType;
    private javax.swing.JButton cmbPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtAsOnDate;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateCombo() {
        
        //----- Generate cmbItem ------- //
        try {
            
            cmbItemTypeModel=new EITLComboModel();
            cmbItemType.removeAll();
            cmbItemType.setModel(cmbItemTypeModel);
            
            ComboData objData=new ComboData();
            objData.Code=1;
            objData.Text="General";
            cmbItemTypeModel.addElement(objData);
            
            objData=new ComboData();
            objData.Code=2;
            objData.Text="Raw Material";
            cmbItemTypeModel.addElement(objData);
            
            objData=new ComboData();
            objData.Code=3;
            objData.Text="Raw Material STM";
            cmbItemTypeModel.addElement(objData);
            
        }
        catch(Exception e) {
            
        }
        //------------------------------ //
        
    }
    private void GenerateReportRaw() {
        TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
        
        objData.AddColumn("SR_NO");
        objData.AddColumn("ITEM_CODE");
        objData.AddColumn("ITEM_DESC");
        objData.AddColumn("UNIT_DESC");
        objData.AddColumn("GRN_NO");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("ITEM_LOT_NO");
        objData.AddColumn("AUTO_LOT_NO");
        objData.AddColumn("STOCK_QTY");
        objData.AddColumn("STOCK_VALUE");
        
        try{
            if(txtAsOnDate.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Please Enter Date");
                return;
            }
            if(!EITLERPGLOBAL.isDate(txtAsOnDate.getText())) {
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
            
            String Heading="";
            String Condition="";
            String Condition1="";
            if(EITLERPGLOBAL.getComboCode(cmbItemType) == 3) {
                Heading = "OF RAW MATERIAL STM FOR ";
                Condition=" CATEGORY_ID = 13 ";
                Condition1=" AND A.GRN_TYPE = 3 ";
            }
            else {
                Heading = "OF RAW MATERIAL FOR ";
                Condition=" CATEGORY_ID <> 13 ";
                Condition1=" AND A.GRN_TYPE <> 3 ";
            }
            
            if(EITLERPGLOBAL.gCompanyID ==2 ) {
                Heading +="BARODA";
            }
            if(EITLERPGLOBAL.gCompanyID ==3 ) {
                Heading +="ANKLESHWAR.";
            }
            
            ResultSet rsMain;
            
            
            Qry="SELECT A.ITEM_ID,A.LOT_NO  AS AUTO_LOT_NO, A.ITEM_LOT_NO, A.OPENING_QTY AS QTY, "+
            "A.OPENING_VALUE AS VALUE, A.OPENING_RATE AS RATE  "+
            "FROM D_COM_OPENING_STOCK_DETAIL A, D_INV_ITEM_MASTER B  "+
            "WHERE A.ITEM_ID = B.ITEM_ID AND A.COMPANY_ID = B.COMPANY_ID  "+
            "AND B.CATEGORY_ID IN (SELECT CATEGORY_ID FROM D_INV_ITEM_CATEGORY WHERE CATEGORY_TYPE =2 AND "+Condition+" )  "+
            "AND A.OPENING_QTY >0 AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.ENTRY_NO ="+StockEntryNo+" "+
            "UNION  "+
            "SELECT B.ITEM_ID, AUTO_LOT_NO AS AUTO_LOT_NO, C.ITEM_LOT_NO, "+
            "C.LOT_ACCEPTED_QTY AS QTY,C.LOT_ACCEPTED_QTY*B.LANDED_RATE AS VALUE,  "+
            "B.LANDED_RATE AS RATE  "+
            "FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B, D_INV_GRN_LOT C,D_INV_ITEM_MASTER D "+
            "WHERE A.GRN_NO = B.GRN_NO AND A.GRN_NO=C.GRN_NO AND B.GRN_NO=C.GRN_NO  "+
            "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID  "+
            "AND A.COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" AND B.COMPANY_ID =C.COMPANY_ID AND B.SR_NO = C.GRN_SR_NO  "+
            "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND B.ITEM_ID = D.ITEM_ID AND D.APPROVED = 1 AND D.CANCELLED = 0  "+
            "AND D.CATEGORY_ID IN  "+
            "(SELECT CATEGORY_ID FROM D_INV_ITEM_CATEGORY WHERE CATEGORY_TYPE =2 AND "+Condition+" )  "+
            Condition1 +
            "AND A.GRN_DATE >='"+StockEntryDate +"' AND A.GRN_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText())+ "'  "+
            "ORDER BY ITEM_ID, AUTO_LOT_NO";
            
            System.out.println(Qry);
            rsMain=data.getResult(Qry,clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
            
            rsMain.first();
            if(rsMain.getRow()>0) {
                while (!rsMain.isAfterLast()){
                    counter++;
                    double ItemLotStockQty=0, ItemLotStockValue=0;
                    ItemId= rsMain.getString("ITEM_ID");
                    String AutoLotNo= rsMain.getString("AUTO_LOT_NO");
                    if(ItemId.equals("RM22235002")) {
                        boolean t= true;
                    }
                    if(AutoLotNo.equals("100168")) {
                        boolean t1= true;
                    }
                    
                    String LotNo= rsMain.getString("ITEM_LOT_NO");
                    ItemLotStockQty=EITLERPGLOBAL.round(rsMain.getDouble("QTY"),3);
                    ItemLotStockValue=EITLERPGLOBAL.round(rsMain.getDouble("VALUE"),3);
                    double ItemRate = EITLERPGLOBAL.round(rsMain.getDouble("RATE"),3);
                    //System.out.println("Item ID : "+ItemId + " Auto Lot No:"+ AutoLotNo+" Qty.: "+ItemLotStockQty+ " Value:"+ItemLotStockValue);
                    
                    Qry="SELECT A.ISSUE_NO,B.SR_NO,C.ISSUED_LOT_QTY,C.ISSUED_LOT_QTY*B.RATE AS RECEIPT_VALUE FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B, D_INV_ISSUE_LOT C "+
                    "WHERE A.ISSUE_NO = B.ISSUE_NO AND A.ISSUE_NO=C.ISSUE_NO AND B.ISSUE_NO=C.ISSUE_NO "+
                    "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID AND B.COMPANY_ID =C.COMPANY_ID "+
                    "AND B.ITEM_CODE = C.ITEM_ID AND B.ITEM_CODE = '"+ItemId+"'  AND C.AUTO_LOT_NO='"+AutoLotNo+"' "+
                    "AND B.SR_NO = C.ISSUE_SR_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+
                    "AND A.ISSUE_DATE >='"+StockEntryDate+"' AND A.ISSUE_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText())+ "' "+
                    " UNION " +
                    "SELECT A.STM_NO AS ISSUE_NO,B.SR_NO,C.ISSUED_LOT_QTY ,(C.ISSUED_LOT_QTY * B.RATE) AS RECEIPT_VALUE "+
                    "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B,D_INV_STM_LOT C "+
                    "WHERE A.COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND A.STM_NO = B.STM_NO AND A.STM_NO = C.STM_NO AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO "+
                    "AND A.STM_DATE >='"+StockEntryDate+"' AND A.STM_DATE <='"+ EITLERPGLOBAL.formatDateDB(txtAsOnDate.getText())+ "' AND A.STM_TYPE=B.STM_TYPE " +
                    "AND B.ITEM_ID = '"+ItemId+"' AND C.AUTO_LOT_NO='"+AutoLotNo+"' AND A.APPROVED=1 AND A.CANCELLED=0";
                    
                    
                    
                    double IssueQty=0;
                    double IssueValue=0;
                    double tempQty = 0;
                    rsTmp = data.getResult(Qry,clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
                    rsTmp.first();
                    if (rsTmp.getRow()>0){
                        while (!rsTmp.isAfterLast()) {
                            tempQty = EITLERPGLOBAL.round(rsTmp.getDouble("ISSUED_LOT_QTY"),3);
                            IssueQty+=tempQty;
                            IssueValue+=EITLERPGLOBAL.round(ItemRate*tempQty,3);
                            rsTmp.next();
                        }
                    }
                    ItemLotStockQty-= EITLERPGLOBAL.round(IssueQty,3);
                    ItemLotStockValue-=EITLERPGLOBAL.round(IssueValue,3);
                    
                    
                    if(ItemLotStockQty ==0) {
                        ItemLotStockValue=0.0;
                    }
                    
                    if(ItemLotStockQty <0) {
                        JOptionPane.showMessageDialog(null,"Item Stock Quantity in Negative for Item ID:" + ItemId + " Please Contact Administrator");
                    }
                    
                    //System.out.println("Issue Qty : "+ IssueQty+ " IssueValue :"+ IssueValue);
                    //System.out.println("Closing Qty : "+ ItemLotStockQty+ " ClosingValue :"+ ItemLotStockValue);
                    
                    
                    
                    objRow=objData.newRow();
                    String ItemDescription = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, ItemId);
                    int UnitID = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, ItemId);
                    
                    if(!chkZeroInclude.isSelected() && EITLERPGLOBAL.round(ItemLotStockQty,3) > 0) {
                        String UnitDesc =clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT", UnitID);
                        objRow.setValue("SR_NO",String.valueOf(counter));
                        objRow.setValue("ITEM_CODE",ItemId);
                        objRow.setValue("ITEM_DESC",ItemDescription);
                        objRow.setValue("UNIT_DESC",UnitDesc);
                        String GRNNo = data.getStringValueFromDB("SELECT GRN_NO FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+AutoLotNo+"' ");
                        objRow.setValue("GRN_NO",GRNNo);
                        String VoucherNo = "";
                        //VoucherNo = data.getStringValueFromDB("SELECT DISTINCT(VOUCHER_NO) FROM D_FIN_VOUCHER_DETAIL WHERE SUB_ACCOUNT_CODE<>'' AND EFFECT='C' AND GRN_NO='"+GRNNo+"' ",FinanceGlobal.FinURL);
                        objRow.setValue("VOUCHER_NO",VoucherNo);
                        objRow.setValue("ITEM_LOT_NO",LotNo);
                        objRow.setValue("AUTO_LOT_NO",AutoLotNo);
                        objRow.setValue("STOCK_QTY",String.valueOf(EITLERPGLOBAL.round(ItemLotStockQty,3)));
                        objRow.setValue("STOCK_VALUE",String.valueOf(EITLERPGLOBAL.round(ItemLotStockValue,3)));
                        objData.AddRow(objRow);
                    }
                    if(chkZeroInclude.isSelected()) {
                        String UnitDesc =clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT", UnitID);
                        objRow.setValue("SR_NO",String.valueOf(counter));
                        objRow.setValue("ITEM_CODE",ItemId);
                        objRow.setValue("ITEM_DESC",ItemDescription);
                        objRow.setValue("UNIT_DESC",UnitDesc);
                        String GRNNo = data.getStringValueFromDB("SELECT GRN_NO FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+AutoLotNo+"' ");
                        objRow.setValue("GRN_NO",GRNNo);
                        String VoucherNo = "";
                        //VoucherNo = data.getStringValueFromDB("SELECT DISTINCT(VOUCHER_NO) FROM D_FIN_VOUCHER_DETAIL WHERE SUB_ACCOUNT_CODE<>'' AND EFFECT='C' AND GRN_NO='"+GRNNo+"' ",FinanceGlobal.FinURL);
                        objRow.setValue("VOUCHER_NO",VoucherNo);
                        objRow.setValue("ITEM_LOT_NO",LotNo);
                        objRow.setValue("AUTO_LOT_NO",AutoLotNo);
                        objRow.setValue("STOCK_QTY",String.valueOf(EITLERPGLOBAL.round(ItemLotStockQty,3)));
                        objRow.setValue("STOCK_VALUE",String.valueOf(EITLERPGLOBAL.round(ItemLotStockValue,3)));
                        objData.AddRow(objRow);
                    }
                    rsMain.next();
                }
            }
            HashMap Parameters=new HashMap();
            Parameters.put("RUN_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("ITEM_TYPE",Heading);
            Parameters.put("AS_ON_DATE",txtAsOnDate.getText());
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/ItemLotWiseStockRM.rpt",Parameters,objData);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void GenerateReportGen() {
        
    }
    
}
