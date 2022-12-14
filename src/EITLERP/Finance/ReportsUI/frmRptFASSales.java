/*
 * frmRptDeptWiseComparison.java
 *
 * Created on March 20, 2010, 2:36 PM
 */

package EITLERP.Finance.ReportsUI;

import EITLERP.*;
import java.sql.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import TReportWriter.*;
import java.util.*;
import javax.swing.*;
import EITLERP.Finance.*;
/**
 *
 * @author  root
 */
public class frmRptFASSales extends javax.swing.JApplet {
    
    private EITLComboModel cmbFirstYearModel=new EITLComboModel();
    
    
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    /** Initializes the applet frmRptDeptWiseComparison */
    public void init() {
        setSize(400,200);
        initComponents();
        GenerateCombo();
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cmbFirstYear = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmdpreview = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        getContentPane().add(cmbFirstYear);
        cmbFirstYear.setBounds(110, 50, 160, 25);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Select Year :");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 50, 90, 25);

        cmdpreview.setText("Preview");
        cmdpreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdpreview);
        cmdpreview.setBounds(170, 110, 82, 25);

        jLabel1.setBackground(new java.awt.Color(0, 51, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Fixed Asset Sales Report");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 400, 27);

    }//GEN-END:initComponents
    
    private void cmdpreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpreviewActionPerformed
        // TODO add your handling code here:
        GenerateReport();
    }//GEN-LAST:event_cmdpreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbFirstYear;
    private javax.swing.JButton cmdpreview;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
    private void GenerateReport() {
        
        try {
            TReportWriter.SimpleDataProvider.TRow objRow;
            TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
            String Condition = "";
            
            if(EITLERPGLOBAL.getComboCode(cmbFirstYear)==0) {
                JOptionPane.showMessageDialog(null,"Please Select The Year");
                
                return;
            }
            objReportData.AddColumn("COMPANY_ID");
            objReportData.AddColumn("SR_NO");
            objReportData.AddColumn("SALE_NO");
            objReportData.AddColumn("SALE_DATE");
            objReportData.AddColumn("ASSET_NO");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_NAME");
            objReportData.AddColumn("MAIN_ACCOUNT_CODE");
            objReportData.AddColumn("ACCOUNT_NAME");
            objReportData.AddColumn("YEAR_OF_PURCHASE");
            objReportData.AddColumn("ORIGINAL_COST");
            objReportData.AddColumn("OPENING_DEPRN");
            objReportData.AddColumn("CURRENT_YEAR_DEPRN");
            objReportData.AddColumn("TOTAL_DEPRN");
            objReportData.AddColumn("WDV_COST");
            objReportData.AddColumn("SALE_AMOUNT");
            objReportData.AddColumn("PROFIT");
            objReportData.AddColumn("LOSS");
            
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            
            objOpeningRow.setValue("COMPANY_ID","");
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("SALE_NO","");
            objOpeningRow.setValue("SALE_DATE","0000-00-00");
            objOpeningRow.setValue("ASSET_NO","");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_NAME","");
            objOpeningRow.setValue("MAIN_ACCOUNT_CODE","");
            objOpeningRow.setValue("ACCOUNT_NAME","");
            objOpeningRow.setValue("YEAR_OF_PURCHASE","");
            objOpeningRow.setValue("ORIGINAL_COST","");
            objOpeningRow.setValue("OPENING_DEPRN","");
            objOpeningRow.setValue("CURRENT_YEAR_DEPRN","");
            objOpeningRow.setValue("TOTAL_DEPRN","");
            objOpeningRow.setValue("WDV_COST","");
            objOpeningRow.setValue("SALE_AMOUNT","");
            objOpeningRow.setValue("PROFIT","");
            objOpeningRow.setValue("LOSS","");
            
            
            
            //            objOpeningRow.setValue("TOTAL","");
            
            
            
            
            
            String Year = String.valueOf(EITLERPGLOBAL.getComboCode(cmbFirstYear));
            
            //2052006
            String FromDate=Year.substring(0,4)+"-04-01";
            String ToDate=Year.substring(4)+"-03-31";
            //String FromDate="2004-04-01";
            //String ToDate="2005-03-31";
            
            //            String strQry="SELECT  A.ASSET_NO,A.ASSET_DATE,A.ITEM_ID,A.ITEM_DESC,A.QTY,B.AMOUNT,A.PJ_VOUCHER_NO,A.PJ_VOUCHER_DATE,A.GRN_NO, "+
            //            "DATE_FORMAT(C.DEPRECIATION_FROM_DATE,'%b,%y') AS FROM_DATE,DATE_FORMAT(C.DEPRECIATION_TO_DATE,'%b,%y') AS TO_DATE, "+
            //            "PERIOD_DIFF(DATE_FORMAT(C.DEPRECIATION_TO_DATE,'%Y%m'),DATE_FORMAT(C.DEPRECIATION_FROM_DATE,'%Y%m'))+1 AS MONTHS, "+
            //            "C.DEPRECIATION_PERCENTAGE,C.DEPRECIATION_FOR_THE_YEAR,A.REMARKS "+
            //            "FROM D_FAS_MASTER_HEADER A, D_FAS_MASTER_DETAIL B,D_FAS_MASTER_DETAIL_EX C "+
            //            "WHERE A.ASSET_NO = B.ASSET_NO AND A.ASSET_NO = C.ASSET_NO AND B.SR_NO = C.DETAIL_SR_NO "+
            //            "AND A.APPROVED = 1 AND A.CANCELLED = 0 AND C.TYPE=1 "+
            //            "AND C.DEPRECIATION_FROM_DATE>='" + FromDate + "' AND C.DEPRECIATION_TO_DATE<='" + ToDate + "' "+
            //            "AND A.COMPANY_ID ='" + EITLERPGLOBAL.gCompanyID + "' "+
            //            "AND A.ASSET_DATE>='" + FromDate + "' AND A.ASSET_DATE<='" + ToDate + "' "+
            //            "ORDER BY A.ASSET_DATE";
            
            
            String strQry="SELECT A.SALE_NO,A.DOC_DATE,C.MAIN_ACCOUNT_CODE,B.AMOUNT,B.BOOK_OPENING_VALUE,B.BOOK_CLOSING_VALUE,C.ITEM_DESC,B.ITEM_ID "+
            ",B.BOOK_CUMULATIVE_DEPRECIATION,B.BOOK_CURRENT_YEAR_DEPRECIATION,B.SALE_VALUE,B.BOOK_PROFIT_LOSS,B.YEAR,B.ASSET_NO "+
            "FROM D_FAS_SALE_HEADER A,D_FAS_SALE_DETAIL B,D_FAS_MASTER_HEADER C "+
            "WHERE A.SALE_NO = B.SALE_NO AND A.DOC_DATE >='" + FromDate + "' AND A.DOC_DATE <='" + ToDate + "' "+
            "AND B.ASSET_NO = C.ASSET_NO " +
            "ORDER BY C.MAIN_ACCOUNT_CODE,B.ITEM_ID";
            
            
            System.out.println(strQry);
            
            
            
            double amount=0;
            //strQry="SELECT  DISTINCT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_COST_CENTER_MAPPING "+
            //"WHERE COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " AND APPROVED= 1 AND CANCELLED = 0 ORDER BY DEPT_ID";
            ResultSet rsFAS = data.getResult(strQry,FinanceGlobal.FinURL);
            rsFAS.first();
            int cnt=0;
            if(rsFAS.getRow()>0) {
                while(!rsFAS.isAfterLast()) {
                    
                    objRow=objReportData.newRow();
                    cnt++;
                    objRow.setValue("COMPANY_ID",String.valueOf(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("SR_NO",String.valueOf(cnt));
                    objRow.setValue("SALE_NO",rsFAS.getString("SALE_NO"));
                    objRow.setValue("SALE_DATE",EITLERPGLOBAL.formatDate(rsFAS.getString("DOC_DATE")));
                    objRow.setValue("ASSET_NO",rsFAS.getString("ASSET_NO"));
                    objRow.setValue("ITEM_ID",rsFAS.getString("ITEM_ID"));
                    
                    if(rsFAS.getString("ITEM_ID").substring(0,2).equals("DI")) {
                                            objRow.setValue("ITEM_NAME",rsFAS.getString("ITEM_DESC"));
                                       }
                                     else {
                                       objRow.setValue("ITEM_NAME", EITLERP.clsItem.getItemName(EITLERPGLOBAL.gCompanyID,rsFAS.getString("ITEM_ID")));
                                 }
                    
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsFAS.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME", EITLERP.Finance.clsGL.getAccountName(EITLERPGLOBAL.gCompanyID,rsFAS.getString("MAIN_ACCOUNT_CODE")));
                    objRow.setValue("YEAR_OF_PURCHASE",rsFAS.getString("YEAR"));
                    objRow.setValue("ORIGINAL_COST",rsFAS.getString("AMOUNT"));
                    objRow.setValue("OPENING_DEPRN",String.valueOf(rsFAS.getDouble("BOOK_CUMULATIVE_DEPRECIATION") - rsFAS.getDouble("BOOK_CURRENT_YEAR_DEPRECIATION")));
                    objRow.setValue("CURRENT_YEAR_DEPRN",rsFAS.getString("BOOK_CURRENT_YEAR_DEPRECIATION"));
                    objRow.setValue("TOTAL_DEPRN",rsFAS.getString("BOOK_CUMULATIVE_DEPRECIATION"));
                    objRow.setValue("WDV_COST",rsFAS.getString("BOOK_CLOSING_VALUE"));
                    objRow.setValue("SALE_AMOUNT",rsFAS.getString("SALE_VALUE"));
                    
                    if(rsFAS.getDouble("BOOK_PROFIT_LOSS") <=0) {
                        objRow.setValue("LOSS",String.valueOf(Math.abs(rsFAS.getDouble("BOOK_PROFIT_LOSS"))));
                    }
                    else {
                        objRow.setValue("PROFIT",String.valueOf(rsFAS.getDouble("BOOK_PROFIT_LOSS")));
                    }
                    
                    objReportData.AddRow(objRow);
                    
                    rsFAS.next();
                    
                    
                    
                    
                }
            }
            HashMap Parameters=new HashMap();
            if(EITLERPGLOBAL.gCompanyID==2) {
                Parameters.put("CITY","BARODA");
            }
            else {
                Parameters.put("CITY","ANKLESHWAR");
            }
            
            Parameters.put("COMPANY_ID",Integer.toString(EITLERPGLOBAL.gCompanyID));
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            Parameters.put("YEAR",Year.substring(0,4)+"-"+Year.substring(4));
            Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
            
            //Parameters.put("FIRSTYEAR1",FirstYear1.substring(2));
            //Parameters.put("FIRSTYEAR2",FirstYear2.substring(2));
            //Parameters.put("SECONDYEAR1",SecondYear1.substring(2));
            //            Parameters.put("SECONDYEAR2",SecondYear2.substring(2));
            
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptFASSales.rpt",Parameters,objReportData);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        //System.out.println("");
    }
    
    private void GenerateCombo() {
        
        cmbFirstYearModel=new EITLComboModel();
        cmbFirstYear.removeAll();
        cmbFirstYear.setModel(cmbFirstYearModel);
        
        
        
        
        
        
        String Qry = "SELECT YEAR_FROM,YEAR_TO FROM D_COM_FIN_YEAR WHERE COMPANY_ID = " + EITLERPGLOBAL.gCompanyID + " ";
        try{
            ComboData objData;
            objData=new ComboData();
            objData.Code=0;
            objData.Text="-Please Select Year-";
            cmbFirstYearModel.addElement(objData);
            
            ResultSet rs = data.getResult(Qry);
            
            while(rs.next()) {
                objData=new ComboData();
                objData.Code=Long.parseLong(rs.getString("YEAR_FROM")+rs.getString("YEAR_TO"));
                objData.Text=rs.getString("YEAR_FROM")+"-"+rs.getString("YEAR_TO");
                cmbFirstYearModel.addElement(objData);
                
                
            }
        }
        catch(Exception e) {
            
        }
    }
}
