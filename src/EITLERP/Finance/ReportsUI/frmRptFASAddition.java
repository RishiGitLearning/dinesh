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
public class frmRptFASAddition extends javax.swing.JApplet {
    
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
        jLabel1.setText("Fixed Asset Addition Report");
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
            objReportData.AddColumn("MAIN_ACCOUNT_CODE");
            objReportData.AddColumn("ACCOUNT_NAME");
            objReportData.AddColumn("ASSET_NO");
            objReportData.AddColumn("ASSET_DATE");
            objReportData.AddColumn("ITEM_ID");
            objReportData.AddColumn("ITEM_DESC");
            objReportData.AddColumn("VOUCHER_NO");
            objReportData.AddColumn("VOUCHER_DATE");
            objReportData.AddColumn("GRN_NO");
            objReportData.AddColumn("GRN_DATE");
            objReportData.AddColumn("AMOUNT");
            objReportData.AddColumn("FROM_DATE");
            objReportData.AddColumn("TO_DATE");
            objReportData.AddColumn("MONTHS");
            objReportData.AddColumn("DEPRN_PER");
            objReportData.AddColumn("DEPRN_AMOUNT");
            objReportData.AddColumn("REMARKS");
            
            
            TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
            
            
            objOpeningRow.setValue("COMPANY_ID","");
            objOpeningRow.setValue("SR_NO","");
            objOpeningRow.setValue("MAIN_ACCOUNT_CODE","");
            objOpeningRow.setValue("ACCOUNT_NAME","");
            objOpeningRow.setValue("ASSET_NO","");
            objOpeningRow.setValue("ASSET_DATE","0000-00-00");
            objOpeningRow.setValue("ITEM_ID","");
            objOpeningRow.setValue("ITEM_DESC","");
            objOpeningRow.setValue("VOUCHER_NO","");
            objOpeningRow.setValue("VOUCHER_DATE","0000-00-00");
            objOpeningRow.setValue("GRN_NO","");
            objOpeningRow.setValue("GRN_DATE","0000-00-00");
            objOpeningRow.setValue("AMOUNT","");
            objOpeningRow.setValue("FROM_DATE","0000-00-00");
            objOpeningRow.setValue("TO_DATE","0000-00-00");
            objOpeningRow.setValue("MONTHS","");
            objOpeningRow.setValue("DEPRN_PER","");
            objOpeningRow.setValue("DEPRN_AMOUNT","");
            objOpeningRow.setValue("REMARKS","");
            //            objOpeningRow.setValue("TOTAL","");
            
            String Year = String.valueOf(EITLERPGLOBAL.getComboCode(cmbFirstYear));
            
            //2052006
            String FromDate=Year.substring(0,4)+"-04-01";
            String ToDate=Year.substring(4)+"-03-31";
            //String FromDate="2004-04-01";
            //String ToDate="2005-03-31";
            
            String strQry="SELECT  A.ASSET_NO,A.ASSET_DATE,A.ITEM_ID,A.ITEM_DESC,A.QTY,B.AMOUNT,A.PJ_VOUCHER_NO,A.PJ_VOUCHER_DATE,A.GRN_NO, "+
            "DATE_FORMAT(C.DEPRECIATION_FROM_DATE,'%b,%y') AS FROM_DATE,DATE_FORMAT(C.DEPRECIATION_TO_DATE,'%b,%y') AS TO_DATE, "+
            "PERIOD_DIFF(DATE_FORMAT(C.DEPRECIATION_TO_DATE,'%Y%m'),DATE_FORMAT(C.DEPRECIATION_FROM_DATE,'%Y%m'))+1 AS MONTHS, "+
            "C.DEPRECIATION_PERCENTAGE,C.DEPRECIATION_FOR_THE_YEAR,A.REMARKS,A.MAIN_ACCOUNT_CODE "+
            "FROM D_FAS_MASTER_HEADER A, D_FAS_MASTER_DETAIL B,D_FAS_MASTER_DETAIL_EX C "+
            "WHERE A.ASSET_NO = B.ASSET_NO AND A.ASSET_NO = C.ASSET_NO AND B.SR_NO = C.DETAIL_SR_NO "+
            "AND A.APPROVED = 1 AND A.CANCELLED = 0 AND C.TYPE=1 "+
            "AND C.DEPRECIATION_FROM_DATE>='" + FromDate + "' AND C.DEPRECIATION_TO_DATE<='" + ToDate + "' "+
            "AND A.COMPANY_ID ='" + EITLERPGLOBAL.gCompanyID + "' "+
            "AND A.ASSET_DATE>='" + FromDate + "' AND A.ASSET_DATE<='" + ToDate + "' "+
            "ORDER BY A.ASSET_DATE";
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
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsFAS.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE =  '" + rsFAS.getString("MAIN_ACCOUNT_CODE")  + "'",FinanceGlobal.FinURL));
                    objRow.setValue("ASSET_NO",rsFAS.getString("ASSET_NO"));
                    objRow.setValue("ASSET_DATE",EITLERPGLOBAL.formatDate(rsFAS.getString("ASSET_DATE")));
                    objRow.setValue("ITEM_ID",rsFAS.getString("ITEM_ID"));
                    
                    if(rsFAS.getString("ITEM_ID").substring(0,2).equals("DI")) {
                        objRow.setValue("ITEM_DESC",rsFAS.getString("ITEM_DESC"));
                    }
                    else {
                        objRow.setValue("ITEM_DESC", EITLERP.clsItem.getItemName(EITLERPGLOBAL.gCompanyID,rsFAS.getString("ITEM_ID")));
                    }
                    
                    
                    //objRow.setValue("ITEM_DESC","");
                    objRow.setValue("VOUCHER_NO",rsFAS.getString("PJ_VOUCHER_NO"));
                    objRow.setValue("VOUCHER_DATE",rsFAS.getString("PJ_VOUCHER_DATE"));
                    objRow.setValue("GRN_NO",rsFAS.getString("GRN_NO"));
                    objRow.setValue("GRN_DATE","0000-00-00");
                    
                    //                String SQL="SELECT PJ_VOUCHER_AMOUNT+CUSTOM_DUTY_VOUCHER_AMOUNT+LANDING_VOUCHER_AMOUNT+ "+
                    //                "FREIGHT_OCTROI_VOUCHER_AMOUNT+INSTALLATION_VOUCHER_AMOUNT "+
                    //                "FROM D_FAS_MASTER_HEADER WHERE ASSET_NO = '" + rsFAS.getString("ASSET_NO") + "' ";
                    //                amount  = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    //
                    //                SQL="SELECT SUM(OTHERS_VOUCHER_AMOUNT) FROM D_FAS_OTHER_VOUCHER WHERE ASSET_NO = '" + rsFAS.getString("ASSET_NO") + "' ";
                    //                amount+=data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    
                    objRow.setValue("AMOUNT",rsFAS.getString("AMOUNT"));
                    objRow.setValue("FROM_DATE",rsFAS.getString("FROM_DATE"));
                    objRow.setValue("TO_DATE",rsFAS.getString("TO_DATE"));
                    objRow.setValue("MONTHS",rsFAS.getString("MONTHS"));
                    objRow.setValue("DEPRN_PER",rsFAS.getString("DEPRECIATION_PERCENTAGE"));
                    objRow.setValue("DEPRN_AMOUNT",rsFAS.getString("DEPRECIATION_FOR_THE_YEAR"));
                    objRow.setValue("REMARKS",rsFAS.getString("REMARKS"));
                    
                    //rsFAS.getString("")
                    //                objRow.setValue("JAN1",String.valueOf(EITLERPGLOBAL.round(JanFirst,0)));
                    //                objRow.setValue("JAN2",String.valueOf(EITLERPGLOBAL.round(JanSecond,0)));
                    //                objRow.setValue("FEB1",String.valueOf(EITLERPGLOBAL.round(FebFirst,0)));
                    //                objRow.setValue("FEB2",String.valueOf(EITLERPGLOBAL.round(FebSecond,0)));
                    //                objRow.setValue("MAR1",String.valueOf(EITLERPGLOBAL.round(MarFirst,0)));
                    //                objRow.setValue("MAR2",String.valueOf(EITLERPGLOBAL.round(MarSecond,0)));
                    //                objRow.setValue("QTR41",String.valueOf(EITLERPGLOBAL.round(FourthQtr1,0)));
                    //                objRow.setValue("QTR42",String.valueOf(EITLERPGLOBAL.round(FourthQtr2,0)));
                    
                    rsFAS.next();
                    
                    
                    
                    objReportData.AddRow(objRow);
                    
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
            
            
            
            objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptFASAddition.rpt",Parameters,objReportData);
            
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
