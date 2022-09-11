package EITLERP.Finance.Util;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Finance.*;
import EITLERP.data;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

public class clsImportWarrantCD {
    
    public clsImportWarrantCD() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportWarrantCD objImport = new clsImportWarrantCD();
            objImport.ImportToWarrant();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("The End.");
    }
    
    private void ImportToWarrant() {
        long Counter = 1;
        long SubRecord = 0;
        Connection objConn = null;
        Statement stDeposit = null;
        Statement stWarrantD = null;
        ResultSet rsDeposit = null;
        ResultSet rsWarrantD = null;
        try {
            objConn = data.getConn(FinanceGlobal.FinURL);
            stDeposit = objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stWarrantD = objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDeposit = stDeposit.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND (SCHEME_ID='000004' OR SCHEME_ID='000005') AND EFFECTIVE_DATE<'2008-11-01' ORDER BY EFFECTIVE_DATE");
            rsDeposit.first();
            
            String ReceiptNo = "";
            String eDate = "";
            String mDate = "";
            String interestCalcDate = "";
            double interestAmount = 0.0;
            double Amount = 0.0;
            double Rate = 0.0;
            int Months = 6;
            double nyear = Months/12.0;
            double nTimes = 12 / Months;
            String MainAccountCode = "";
            String PartyCode = "";
            String IntMainCode = "";
            double partyInterest = 0.0;
            int totalDays = 0;
            double currentInterest = 0.0;
            double TDSAmount = 0.0;
            Counter=1;
            while(!rsDeposit.isAfterLast()) {
                ReceiptNo = rsDeposit.getString("RECEIPT_NO");
                eDate = rsDeposit.getString("EFFECTIVE_DATE");
                mDate = rsDeposit.getString("MATURITY_DATE");
                interestCalcDate = getInterestDate(2, eDate, ReceiptNo);
                interestAmount = 0.0;
                Amount = rsDeposit.getDouble("AMOUNT");
                Rate = rsDeposit.getDouble("INTEREST_RATE");
                Months = 6;
                //nyear = Months/12;
                nTimes = 12/Months;
                MainAccountCode = rsDeposit.getString("MAIN_ACCOUNT_CODE");
                PartyCode = rsDeposit.getString("PARTY_CODE");
                IntMainCode = rsDeposit.getString("INTEREST_MAIN_CODE");
                TDSAmount = 0.0;
                totalDays = 0;
                String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"", FinanceGlobal.FinURL);
                SubRecord = 0;
                while(java.sql.Date.valueOf(interestCalcDate).before(java.sql.Date.valueOf("2008-11-30"))) {
                    TDSAmount = 0.0;
                    String SQLQuery = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' ";
                    eDate = rsDeposit.getString("EFFECTIVE_DATE");
                    Amount = rsDeposit.getDouble("AMOUNT");
                    if(data.IsRecordExist(SQLQuery, FinanceGlobal.FinURL)) {
                        SQLQuery = "SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' ";
                        ResultSet rsResult = data.getResult(SQLQuery, FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()) {
                            Amount += rsResult.getDouble("INTEREST_AMOUNT");
                            eDate = clsCalcInterest.addMonthToDate(eDate, Months);
                            rsResult.next();
                        }
                        
                        rsResult.close();
                        eDate = EITLERPGLOBAL.addDaysToDate(eDate, 1, "yyyy-MM-dd");
                    }
                    
                    totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(eDate), java.sql.Date.valueOf(interestCalcDate));
                    /*if(totalDays<0) {
                        System.out.println();
                    }*/
                    if(java.sql.Date.valueOf(interestCalcDate).before(java.sql.Date.valueOf(mDate)) && java.sql.Date.valueOf(eDate).compareTo(java.sql.Date.valueOf(mDate)) != 0) {
                        interestAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,2);
                    }
                    String StartFinYear = "";
                    String EndFinYear = "";
                    if(Integer.parseInt(interestCalcDate.substring(5,7)) < 4 ) {
                        int FinYear = Integer.parseInt(interestCalcDate.substring(0,4))-1;
                        StartFinYear = Integer.toString(FinYear)+"-04-01";
                        EndFinYear = interestCalcDate.substring(0,4)+"-03-31";
                    } else {
                        int FinYear = Integer.parseInt(interestCalcDate.substring(0,4))+1;
                        StartFinYear = interestCalcDate.substring(0,4)+"-04-01";
                        EndFinYear = Integer.toString(FinYear)+"-03-31";
                    }
                    /*if(data.getIntValueFromDB("SELECT TAX_FORM FROM D_FD_TAX_FORM WHERE PARTY_CODE='"+PartyCode+"' AND START_FIN_YEAR='"+StartFinYear+"' AND END_FIN_YEAR='"+EndFinYear+"'", FinanceGlobal.FinURL) != 2) {
                        partyInterest = checkTDSAmount(PartyCode,ReceiptNo,StartFinYear,EndFinYear);
                        partyInterest += interestAmount;
                        if(partyInterest > 5000) {
                            currentInterest = getCurrentInterest(ReceiptNo, interestCalcDate, StartFinYear, EndFinYear);
                            TDSAmount = clsDepositMaster.calculateTDSAmount(currentInterest);
                        }
                    }*/
                    
                    rsWarrantD = stWarrantD.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL LIMIT 1");
                    rsWarrantD.first();
                    String FirstDate = interestCalcDate.substring(0,8)+"01";
                    String LastDate = data.getStringValueFromDB("SELECT LAST_DAY('"+interestCalcDate+"') FROM DUAL");
                    int SrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FD_INT_CALC_DETAIL WHERE WARRANT_DATE>='"+FirstDate+"' AND WARRANT_DATE<='"+LastDate+"' ", FinanceGlobal.FinURL)+1;
                    String DocNo = data.getStringValueFromDB("SELECT DOC_NO FROM D_FD_INT_CALC_DETAIL WHERE WARRANT_DATE>='"+FirstDate+"' AND WARRANT_DATE<='"+LastDate+"' ", FinanceGlobal.FinURL);
                    rsWarrantD.moveToInsertRow();
                    rsWarrantD.updateLong("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                    rsWarrantD.updateString("DOC_NO", DocNo);
                    rsWarrantD.updateInt("SR_NO", SrNo);
                    rsWarrantD.updateString("RECEIPT_NO", ReceiptNo);
                    rsWarrantD.updateInt("INTEREST_DAYS", totalDays);
                    rsWarrantD.updateDouble("INTEREST_RATE", Rate);
                    rsWarrantD.updateString("PARTY_CODE", PartyCode);
                    rsWarrantD.updateString("MAIN_ACCOUNT_CODE", MainAccountCode);
                    rsWarrantD.updateString("INT_MAIN_ACCOUNT_CODE", IntMainCode);
                    rsWarrantD.updateString("WARRANT_NO", "0000000");
                    rsWarrantD.updateInt("MICR_NO", 0);
                    rsWarrantD.updateDouble("INTEREST_AMOUNT", interestAmount);
                    rsWarrantD.updateDouble("TDS_AMOUNT", TDSAmount);
                    double netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount, 2);
                    rsWarrantD.updateDouble("NET_INTEREST", netInterest);
                    rsWarrantD.updateString("WARRANT_DATE", interestCalcDate);
                    rsWarrantD.updateString("WARRANT_CLEAR", "N");
                    rsWarrantD.updateString("CREATED_BY", "Admin");
                    rsWarrantD.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateString("MODIFIED_BY", "Admin");
                    rsWarrantD.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateBoolean("CHANGED", true);
                    rsWarrantD.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateBoolean("CANCELLED", false);
                    rsWarrantD.insertRow();
                                        //int interestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'", FinanceGlobal.FinURL);
                    interestCalcDate = clsCalcInterest.addMonthToDate(interestCalcDate, Months);
                    ++SubRecord;
                }
                System.out.println("Receipt No = " + ReceiptNo + " Counter = " + Counter + " SubRecord = " + SubRecord);
                ++Counter;
                rsDeposit.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nFinished posting to warrant table. "+Counter+" records with subrecord = "+SubRecord+" has been posted to warrant.\n");
    }
    private String getNextWarrantNo(String WarrantNo, String WarrantDate) {
        String nextWarrantNo = "";
        String FinYear = "";
        try {
            int intMonth = Integer.parseInt(WarrantDate.substring(5, 7));
            int intYear = Integer.parseInt(WarrantDate.substring(0, 4));
            if(intMonth >= 4) {
                FinYear = Integer.toString(intYear);
            } else {
                FinYear = Integer.toString(--intYear);
            }
            FinYear = FinYear.substring(2, 4);
            nextWarrantNo = "M"+FinYear+WarrantNo.substring(2,6);
        } catch(Exception e) {
            return nextWarrantNo;
        }
        return nextWarrantNo;
    }
    
    private String getInterestDate(int DepositTypeID, String EffectiveDate, String ReceiptNo) {
        String InterestDate = "";
        String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"", FinanceGlobal.FinURL);
        String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"", FinanceGlobal.FinURL);
        try {
            if(DepositTypeID == 2) {
                int interestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'", FinanceGlobal.FinURL);
                InterestDate = clsCalcInterest.addMonthToDate(EffectiveDate, interestCalcPeriod);
            }
        } catch(Exception e) {
        }
        return InterestDate;
    }
    
    public static String getNextInterestDate(int CompanyID, String ReceiptNo) {
        String nextInterestCalcDate = "";
        int DepositType;
        String InterestCalcDate;
        String MaturityDate;
        String checkDate;
        DepositType = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"", FinanceGlobal.FinURL);
        InterestCalcDate = data.getStringValueFromDB("SELECT INT_CALC_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"", FinanceGlobal.FinURL);
        MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"", FinanceGlobal.FinURL);
        checkDate = EITLERPGLOBAL.addDaysToDate(InterestCalcDate, 1, "yyyy-MM-dd");
        if(java.sql.Date.valueOf(checkDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0) {
            return InterestCalcDate;
        }
        try {
            if(DepositType == 2) {
                int InterestCalcPeriod = data.getIntValueFromDB("SELECT A.INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER A, D_FD_DEPOSIT_MASTER B WHERE A.COMPANY_ID=B.COMPANY_ID AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.SCHEME_ID=B.SCHEME_ID AND B.RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                nextInterestCalcDate = clsCalcInterest.addMonthToDate(InterestCalcDate, InterestCalcPeriod);
                if(java.sql.Date.valueOf(nextInterestCalcDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0 || java.sql.Date.valueOf(nextInterestCalcDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    nextInterestCalcDate = clsDepositMaster.deductDays(MaturityDate, 1);
                }
            }
        } catch(Exception e) {
            return nextInterestCalcDate;
        }
        return nextInterestCalcDate;
    }
    
    public static double checkTDSAmount(String PartyCode, String ReceiptNo, String StartFinYear, String EndFinYear) {
        double interestAmount = 0.0;
        //String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear())+"-04-01";
        //String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1)+"-03-31";
        String EffectiveDate = "";
        String MaturityDate = "";
        String PMDate = "";
        int DiffofDays = 0;
        double Rate = 0.0;
        double Amount = 0.0;
        int DepositType = 0;
        String cReceiptNo = "";
        GregorianCalendar cal = new GregorianCalendar();
        try {
            ResultSet rsBeforeStart = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND EFFECTIVE_DATE<'"+StartFinYear+"' AND RECEIPT_NO<>'"+ReceiptNo+"' ORDER BY EFFECTIVE_DATE ",FinanceGlobal.FinURL); //AND MATURITY_DATE>='"+EndFinYear+"'
            rsBeforeStart.first();
            if(rsBeforeStart.getRow()>0) {
                while(!rsBeforeStart.isAfterLast()) {
                    cReceiptNo = rsBeforeStart.getString("RECEIPT_NO");
                    EffectiveDate = rsBeforeStart.getString("EFFECTIVE_DATE");
                    MaturityDate = rsBeforeStart.getString("MATURITY_DATE");
                    Rate = rsBeforeStart.getDouble("INTEREST_RATE");
                    if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf(EndFinYear)) || java.sql.Date.valueOf(MaturityDate).compareTo(java.sql.Date.valueOf(EndFinYear))==0) {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate));
                        ResultSet rsTmp = data.getResult("SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+cReceiptNo+"' ",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear));
                        ResultSet rsTmp = data.getResult("SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+cReceiptNo+"' ",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    }
                    if(cal.isLeapYear(Integer.parseInt(StartFinYear.substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsBeforeStart.next();
                }
            }
            
            ResultSet rsAfterStart = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND EFFECTIVE_DATE>='"+StartFinYear+"' AND RECEIPT_NO<>'"+ReceiptNo+"' ORDER BY EFFECTIVE_DATE ",FinanceGlobal.FinURL); //AND MATURITY_DATE>='"+EndFinYear+"'
            rsAfterStart.first();
            if(rsAfterStart.getRow()>0) {
                while(!rsAfterStart.isAfterLast()) {
                    cReceiptNo = rsAfterStart.getString("RECEIPT_NO");
                    EffectiveDate = rsAfterStart.getString("EFFECTIVE_DATE");
                    MaturityDate = rsAfterStart.getString("MATURITY_DATE");
                    Rate = rsAfterStart.getDouble("INTEREST_RATE");
                    if(java.sql.Date.valueOf(MaturityDate).after(java.sql.Date.valueOf(EndFinYear))) {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear));
                        ResultSet rsTmp = data.getResult("SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+cReceiptNo+"' ",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    }
                    if(cal.isLeapYear(Integer.parseInt(StartFinYear.substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsAfterStart.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestAmount;
    }
    
    public static double getCurrentInterest(String ReceiptNo, String IntCalcDate , String StartFinYear, String EndFinYear) {
        double currentInterest = 0.0;
        try {
            
            //String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()) +"-04-01";
            //String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1) +"-03-31";
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO="+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER A, D_FD_DEPOSIT_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND B.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double Rate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String SQLQuery = "";
            int Days = 1;
            if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(IntCalcDate))+1;
            } else {
                if(data.IsRecordExist("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL)) {
                    SQLQuery = "SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='" + ReceiptNo +"' ";
                    ResultSet rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                    rsResult.first();
                    while(!rsResult.isAfterLast()){
                        Amount += rsResult.getDouble("INTEREST_AMOUNT");
                        EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, Months);
                        rsResult.next();
                    }
                    rsResult.close();
                    EffectiveDate = EITLERPGLOBAL.addDaysToDate(EffectiveDate,1,"yyyy-MM-dd");
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(IntCalcDate))+1;
                } else {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(IntCalcDate))+1;
                }
            }
            GregorianCalendar cal = new GregorianCalendar();
            if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
            //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                currentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(366* 100)),2); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            } else {
                currentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(365* 100)),2); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            }
        } catch(Exception e) {
        }
        return currentInterest;
    }
}