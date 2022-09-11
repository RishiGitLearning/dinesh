

package EITLERP.Finance.ReportsUI;


import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.Utils.*;
import EITLERP.Utils.SimpleDataProvider.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import java.text.*;
import TReportWriter.*;

public class frmRptInterestProvision extends javax.swing.JApplet {
    
    private EITLComboModel cmbReceiptTypeModel;
    private EITLComboModel cmbReportTypeModel;
    private EITLComboModel cmbMonthModel;
    private TReportEngine objEngine=new TReportEngine();
    private TReportWriter.SimpleDataProvider.TTable objData=new TReportWriter.SimpleDataProvider.TTable();
    
    
    /** Initializes the applet frmRptGRNInfo */
    public void init() {
        setSize(424,264);
        initComponents();
        Bar.setVisible(false);
        lblBar.setVisible(false);
    }
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblToYear = new javax.swing.JLabel();
        txtToYear = new javax.swing.JTextField();
        cmdPreview = new javax.swing.JButton();
        lblFromYear = new javax.swing.JLabel();
        txtFromYear = new javax.swing.JTextField();
        lblBar = new javax.swing.JLabel();
        Bar = new javax.swing.JProgressBar();

        getContentPane().setLayout(null);

        jPanel3.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(0, 153, 204));
        jPanel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel6.setText("INTEREST PROVISION FOR CUMULATIVE DEPOSIT");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(9, 8, 350, 15);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 2, 800, 30);

        lblToYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToYear.setText("To Year :");
        getContentPane().add(lblToYear);
        lblToYear.setBounds(220, 73, 60, 15);

        txtToYear.setColumns(10);
        getContentPane().add(txtToYear);
        txtToYear.setBounds(285, 70, 90, 20);

        cmdPreview.setText("Preview Report");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });

        getContentPane().add(cmdPreview);
        cmdPreview.setBounds(160, 100, 130, 25);

        lblFromYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromYear.setText("From Year :");
        getContentPane().add(lblFromYear);
        lblFromYear.setBounds(35, 73, 70, 15);

        txtFromYear.setColumns(10);
        getContentPane().add(txtFromYear);
        txtFromYear.setBounds(110, 70, 90, 20);

        lblBar.setText("...");
        getContentPane().add(lblBar);
        lblBar.setBounds(10, 130, 200, 15);

        getContentPane().add(Bar);
        Bar.setBounds(10, 150, 200, 14);

    }//GEN-END:initComponents
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        if ( ! Validate()) {
            return;
        }
        GenerateReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar Bar;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBar;
    private javax.swing.JLabel lblFromYear;
    private javax.swing.JLabel lblToYear;
    private javax.swing.JTextField txtFromYear;
    private javax.swing.JTextField txtToYear;
    // End of variables declaration//GEN-END:variables
    
    private void GenerateReport() {
        new Thread(){
            
            public void run(){
                
                try {
                    String strSQL = "";
                    String FromDate = txtFromYear.getText().trim()+"-04-01";
                    String ToDate = txtToYear.getText().trim()+"-03-31";
                    String MDate = EITLERPGLOBAL.addDaysToDate(ToDate, 1, "yyyy-MM-dd");
                    
                    TReportWriter.SimpleDataProvider.TRow objRow;
                    TReportWriter.SimpleDataProvider.TTable objReportData=new TReportWriter.SimpleDataProvider.TTable();
                    
                    objReportData.AddColumn("SR_NO");
                    objReportData.AddColumn("RECEIPT_NO");
                    objReportData.AddColumn("LEGACY_NO");
                    objReportData.AddColumn("PARTY_CODE");
                    objReportData.AddColumn("APPLICANT_NAME");
                    objReportData.AddColumn("AMOUNT");
                    objReportData.AddColumn("CUM_AMOUNT");
                    objReportData.AddColumn("PERIOD");
                    objReportData.AddColumn("INT_DATE");
                    objReportData.AddColumn("INT_AMOUNT");
                    objReportData.AddColumn("INT_PROV");
                    objReportData.AddColumn("INT_CUM");
                    objReportData.AddColumn("TAX");
                    objReportData.AddColumn("PTAX");
                    objReportData.AddColumn("UP_TAX");
                    objReportData.AddColumn("RECEIPT_DATE");
                    objReportData.AddColumn("RATE");
                    
                    
                    TReportWriter.SimpleDataProvider.TRow objOpeningRow=objReportData.newRow();
                    objOpeningRow.setValue("SR_NO","");
                    objOpeningRow.setValue("RECEIPT_NO","");
                    objOpeningRow.setValue("LEGACY_NO","");
                    objOpeningRow.setValue("PARTY_CODE","");
                    objOpeningRow.setValue("APPLICANT_NAME","");
                    objOpeningRow.setValue("AMOUNT","");
                    objOpeningRow.setValue("CUM_AMOUNT","");
                    objOpeningRow.setValue("PERIOD","");
                    objOpeningRow.setValue("INT_DATE","0000-00-00");
                    objOpeningRow.setValue("INT_AMOUNT","");
                    objOpeningRow.setValue("INT_PROV","");
                    objOpeningRow.setValue("INT_CUM","");
                    objOpeningRow.setValue("TAX","");
                    objOpeningRow.setValue("PTAX","");
                    objOpeningRow.setValue("UP_TAX","");
                    objOpeningRow.setValue("RECEIPT_DATE","0000-00-00");
                    objOpeningRow.setValue("RATE","");
                    
                    strSQL = "SELECT COUNT(*) FROM D_FD_DEPOSIT_MASTER WHERE MATURITY_DATE>='"+MDate+"' AND EFFECTIVE_DATE<='"+ToDate+"' " +
                    "AND DEPOSIT_TYPE_ID=2 AND APPROVED=1 AND CANCELLED=0 AND (PM_DATE>'"+ToDate+"' OR PM_DATE='0000-00-00' OR PM_DATE='') " +
                    //"AND RECEIPT_NO IN ('000221')  " +
                    "ORDER BY RECEIPT_DATE";
                    int BarCounter = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL); //
                    Bar.setVisible(true);
                    lblBar.setVisible(true);
                    Bar.setMaximum(BarCounter);
                    Bar.setMinimum(0);
                    Bar.setValue(0);
                    
                    strSQL = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE MATURITY_DATE>='"+MDate+"' AND EFFECTIVE_DATE<='"+ToDate+"' " +
                    "AND DEPOSIT_TYPE_ID=2 AND APPROVED=1 AND CANCELLED=0 AND (PM_DATE>'"+ToDate+"' OR PM_DATE='0000-00-00' OR PM_DATE='') " +
                    //"AND RECEIPT_NO IN ('000221') " +
                    "ORDER BY RECEIPT_DATE";
                    
                    ResultSet rsReceipt=data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsReceipt.first();
                    
                    int Counter = 0;
                    int SrNo = 0;
                    int RecordCounter = 0;
                    if(rsReceipt.getRow()>0) {
                        while(!rsReceipt.isAfterLast()) {
                            RecordCounter++;
                            Bar.setValue(RecordCounter);
                            lblBar.setText("Processing Record "+RecordCounter);
                            
                            SrNo++;
                            objRow=objReportData.newRow();
                            String PartyCode = UtilFunctions.getString(rsReceipt,"PARTY_CODE", "");
                            String ReceiptNo = UtilFunctions.getString(rsReceipt,"RECEIPT_NO","");
                            
                            objRow.setValue("SR_NO",Integer.toString(SrNo));
                            objRow.setValue("RECEIPT_NO",UtilFunctions.getString(rsReceipt,"RECEIPT_NO",""));
                            objRow.setValue("LEGACY_NO",UtilFunctions.getString(rsReceipt,"LEGACY_NO",""));
                            objRow.setValue("APPLICANT_NAME",UtilFunctions.getString(rsReceipt,"APPLICANT_NAME",""));
                            objRow.setValue("PARTY_CODE",UtilFunctions.getString(rsReceipt,"PARTY_CODE",""));
                            
                            objRow.setValue("AMOUNT",Double.toString(UtilFunctions.getDouble(rsReceipt,"AMOUNT",0)));
                            double Amount = UtilFunctions.getDouble(rsReceipt,"AMOUNT",0);
                            
                            String SQL = "SELECT SUM(A.INTEREST_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0 AND A.WARRANT_DATE<'"+FromDate+"' ";
                            double cumAmount = EITLERPGLOBAL.round(data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL) + Amount,0);
                            objRow.setValue("CUM_AMOUNT",Double.toString(cumAmount));
                            
                            int period = data.getIntValueFromDB("SELECT DEPOSIT_PERIOD FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            objRow.setValue("PERIOD",Integer.toString(period));
                            
                            objRow.setValue("INT_DATE",EITLERPGLOBAL.formatDate(ToDate));
                            
                            SQL = "SELECT SUM(A.INTEREST_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0 AND A.WARRANT_DATE>='"+FromDate+"' AND A.WARRANT_DATE<='"+ToDate+"' ";
                            double interestAmount = EITLERPGLOBAL.round(data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL),0);
                            if(interestAmount > 0.0) {
                                objRow.setValue("INT_AMOUNT",Double.toString(interestAmount));
                            } else {
                                objRow.setValue("INT_AMOUNT","");
                            }
                            
                            String WarrantSQL = "SELECT B.WARRANT_DATE FROM D_FD_INT_CALC_HEADER A, D_FD_INT_CALC_DETAIL B " +
                            "WHERE A.DOC_NO=B.DOC_NO AND B.RECEIPT_NO='"+ReceiptNo+"' AND B.PARTY_CODE='"+PartyCode+"' AND B.WARRANT_DATE>='"+FromDate+"' " +
                            "AND B.WARRANT_DATE<='"+ToDate+"' AND A.TDS_ONLY=0 ORDER BY B.WARRANT_DATE DESC ";
                            
                            String LastCalcDate = "";
                            if(data.IsRecordExist(WarrantSQL,FinanceGlobal.FinURL)) {
                                LastCalcDate = data.getStringValueFromDB(WarrantSQL,FinanceGlobal.FinURL);
                            } else {
                                LastCalcDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            }
                            double interestProv = EITLERPGLOBAL.round(interestAmount+cumAmount,0);
                            interestProv = EITLERPGLOBAL.round(getCurrentInterest(LastCalcDate, ReceiptNo, PartyCode,interestProv),0);
                            if(interestProv > 0) {
                                objRow.setValue("INT_PROV",Double.toString(interestProv));
                            } else {
                                objRow.setValue("INT_PROV","");
                            }
                            
                            double interestCum = EITLERPGLOBAL.round(interestAmount + interestProv,0);
                            if(interestCum > 0) {
                                objRow.setValue("INT_CUM",Double.toString(interestCum));
                            } else {
                                objRow.setValue("INT_CUM","");
                            }
                            
                            SQL = "SELECT SUM(A.TDS_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0 AND A.WARRANT_DATE>='"+FromDate+"' AND A.WARRANT_DATE<='"+ToDate+"' ";
                            double Tax = EITLERPGLOBAL.round(data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL),0);
                            if(Tax > 0) {
                                objRow.setValue("TAX",Double.toString(Tax));
                            } else {
                                objRow.setValue("TAX","");
                            }
                            
                            double PartyInterest = 0;
                            double pTax = 0;

                            if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FD_TAX_FORM_RECEIVED WHERE PARTY_CODE='"+PartyCode+"' AND RECEIVED_DATE>='"+FromDate+"' AND RECEIVED_DATE<='"+ToDate+"' ",FinanceGlobal.FinURL)) {
                                //PartyInterest = clsDepositMaster.checkTDSAmount(PartyCode);
                                PartyInterest = checkTDSAmount(PartyCode,ReceiptNo);
                                if(PartyInterest > 5000) {
                                    if(ReceiptNo.equals("M025090")||ReceiptNo.equals("M010043") || ReceiptNo.equals("000221")) {
                                        pTax = EITLERPGLOBAL.round(((interestProv * 20)/100),0);
                                    } else {
                                        pTax = EITLERPGLOBAL.round(clsDepositMaster.calculateTDSAmount(interestProv),0);
                                    }
                                }
                            }
                            
                            if(pTax > 0) {
                                Counter++;
                                System.out.println("Counter = "+Counter+" ReceiptNo = " + ReceiptNo +" PartyCode = "+PartyCode+" Amount = "+Amount +" Party Amount = "+PartyInterest+" Interest Prov = "+interestProv+" PTAX = "+pTax);
                                objRow.setValue("PTAX",Double.toString(pTax));
                            } else {
                                objRow.setValue("PTAX","");
                            }
                            
                            double upTax = data.getDoubleValueFromDB("SELECT SUM(TDS_AMOUNT) FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' AND WARRANT_DATE<'" + FromDate + "' ",FinanceGlobal.FinURL);
                            upTax = EITLERPGLOBAL.round((Tax+pTax+upTax),0);
                            
                            if(upTax > 0 ) {
                                objRow.setValue("UP_TAX",Double.toString(upTax));
                            } else {
                                objRow.setValue("UP_TAX","");
                            }
                            
                            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            objRow.setValue("RECEIPT_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                            
                            double Rate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                            objRow.setValue("RATE",Double.toString(Rate));
                            objReportData.AddRow(objRow);
                            
                            rsReceipt.next();
                        }
                    }
                    
                    Bar.setVisible(false);
                    lblBar.setText("Completed...");
                    int Comp_ID = EITLERPGLOBAL.gCompanyID;
                    String From_Year = "April " + txtFromYear.getText().trim();
                    String To_Year = "March " + txtToYear.getText().trim();
                    
                    HashMap Parameters=new HashMap();
                    Parameters.put("FROM_YEAR",From_Year);
                    Parameters.put("TO_YEAR",To_Year);
                    Parameters.put("SYS_DATE",EITLERPGLOBAL.getCurrentDate());
                    
                    objEngine.PreviewReport("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/finance/rptInterestProv.rpt",Parameters,objReportData);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
    
    private boolean Validate() {
        //Form level validations
        
        if(txtFromYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter From Year");
            return false;
        }
        if(txtToYear.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Enter To Year");
            return false;
        }
        return true;
    }
    
    public double getCurrentInterest(String EffectiveDate, String ReceiptNo, String PartyCode, double Amount) {
        double currentInterest = 0.0;
        int Days = 0,Months=0;
        String LastDate = "";
        double iPercentage = 0.0;
        double interestAmount = 0;
        try {
            LastDate = txtToYear.getText().trim()+"-03-31";
            iPercentage = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(LastDate))+1;
            
            GregorianCalendar cal = new GregorianCalendar();
            //if(cal.isLeapYear(Integer.parseInt(EffectiveDate.substring(0,4)))) {
            if(cal.isLeapYear(Integer.parseInt(LastDate.substring(0,4)))) {
                currentInterest = EITLERPGLOBAL.round(((Amount * iPercentage * Days)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            } else {
                currentInterest = EITLERPGLOBAL.round(((Amount * iPercentage * Days)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            }
        } catch (Exception e) {
            return currentInterest;
        }
        return currentInterest;
    }
    
    public double checkTDSAmount(String PartyCode, String ReceiptNo) {
        double interestAmount = 0.0;
        String StartFinYear = txtFromYear.getText().trim()+"-04-01";
        String EndFinYear = txtToYear.getText().trim()+"-03-31";
        //String StartFinYear = EITLERPGLOBAL.formatDateDB(txtFromDate.getText().trim());
        //String EndFinYear = EITLERPGLOBAL.formatDateDB(txtToDate.getText().trim());
        String EffectiveDate = "";
        String MaturityDate = "";
        String PMDate = "";
        int DiffofDays = 1;
        double Rate = 0.0;
        double Amount = 0.0;
        int DepositType = 0;
        GregorianCalendar cal = new GregorianCalendar();
        String StartDate="";
        try {
            // Matured and Closed within financial year.
            ResultSet rsTDSClose = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=1 AND PARTY_CODE='"+PartyCode+"' AND MATURITY_DATE>='"+StartFinYear+"' AND MATURITY_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL);
            rsTDSClose.first();
            if(rsTDSClose.getRow() > 0 ) {
                while(!rsTDSClose.isAfterLast()) {
                    DepositType = rsTDSClose.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSClose.getDouble("AMOUNT");
                    Rate = rsTDSClose.getDouble("INTEREST_RATE");
                    MaturityDate = rsTDSClose.getString("MATURITY_DATE");
                    if(DepositType==2)  {
                        ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+rsTDSClose.getString("RECEIPT_NO")+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    }
                    DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate))+1;
                    StartDate=StartFinYear;
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()) ) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSClose.next();
                }
            }
            rsTDSClose.close();
            StartDate="";
            // Open within financial year.
            //ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL); //AND RECEIPT_NO<>'"+ReceiptNo+"'
            ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE MATURITY_DATE>'"+EndFinYear+"' AND (PM_DATE>'"+EndFinYear+"' OR PM_DATE='0000-00-00' OR PM_DATE='') AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL); //AND RECEIPT_NO<>'"+ReceiptNo+"'
             
            rsTDSOpen.first();
            if(rsTDSOpen.getRow() > 0 ) {
                while(!rsTDSOpen.isAfterLast()) {
                    DepositType = rsTDSOpen.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSOpen.getDouble("AMOUNT");
                    Rate = rsTDSOpen.getDouble("INTEREST_RATE");
                    EffectiveDate = rsTDSOpen.getString("EFFECTIVE_DATE");
                    MaturityDate = rsTDSOpen.getString("MATURITY_DATE");
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                        if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf(EndFinYear))){
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate))+1;
                            StartDate=StartFinYear;
                        } else {
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear))+1;
                            StartDate=StartFinYear;
                        }
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear))+1;
                        StartDate=EffectiveDate;
                    }
                    
                    if(DepositType==2)  {
                        String SQL = "SELECT A.INTEREST_AMOUNT  FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B " +
                        "WHERE A.RECEIPT_NO='"+rsTDSOpen.getString("RECEIPT_NO")+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0 " +
                        //"AND A.WARRANT_DATE<='"+EndFinYear+"' " +
                        "AND A.WARRANT_DATE<'"+StartFinYear+"' " +
                        "ORDER BY A.WARRANT_DATE ";
                        ResultSet rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                        //}
                    }
                    
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSOpen.next();
                }
            }
            rsTDSOpen.close();
            
            // Premature within financial year.
            ResultSet rsTDSPM = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=2 AND PARTY_CODE='"+PartyCode+"' AND PM_DATE>='"+StartFinYear+"' AND PM_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL);
            rsTDSPM.first();
            double PMAmount = 0.0;
            String tmpDate1 = "";
            if(rsTDSPM.getRow() > 0) {
                while(!rsTDSPM.isAfterLast()) {
                    DepositType = rsTDSPM.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSPM.getDouble("AMOUNT");
                    Rate = rsTDSPM.getDouble("INTEREST_RATE")-1;
                    EffectiveDate = rsTDSPM.getString("EFFECTIVE_DATE");
                    PMDate = rsTDSPM.getString("PM_DATE");
                    int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+rsTDSPM.getString("SCHEME_ID")+"' ",FinanceGlobal.FinURL);
                    double nyear = Months/12.0;
                    double nTimes = 12.0/Months;
                    String tmpDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
                    if(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                        if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                            if(DepositType == 2 ) {
                                while(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = tmpDate;
                                    tmpDate = clsCalcInterest.addMonthToDate(tmpDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(tmpDate1), java.sql.Date.valueOf(PMDate))+1;
                                StartDate=tmpDate1;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            } else {
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate))+1;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        } else {
                            if(DepositType == 2) {
                                while(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(PMDate)) && java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = EffectiveDate;
                                    EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(PMDate))+1;
                                StartDate=StartFinYear;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(StartFinYear)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        }
                    }
                    rsTDSPM.next();
                }
            }
            rsTDSPM.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestAmount;
    }
}