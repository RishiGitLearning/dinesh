/*
 * clsImportWarrant.java
 *
 * Created on January 12, 2008, 1:31 PM
 */

package EITLERP.Finance.Util;

import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.*;

/**
 *
 * @author  Prathmesh Shah
 * DON'T ASK QUESTIONS IF YOU FIND ANY ERRORS. QUESTIONS ARE STRICTLY PROHIBITED.
 */
public class clsImportWarrantUnmatched {
    
    /** Creates a new instance of clsSOImport */
    public clsImportWarrantUnmatched() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportWarrantUnmatched objImport=new clsImportWarrantUnmatched();
            //objImport.ImportToLegacy("/data/nisarg/FixedDeposit/UNCWRT/UNC_WRT.TXT");
            objImport.ImportToWarrant();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("The End.");
    }
    
    private void ImportToLegacy(String DepositFile) {
        int Pointer = 0;
        long Counter = 1;
        Connection objConn=null;
        Statement stLegacy=null;
        ResultSet rsLegacy = null;
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            BufferedReader aFile=new BufferedReader(new FileReader(new File(DepositFile)));
            
            // Clear all past entry of legacy entry.
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int delete = stTmp.executeUpdate("DELETE FROM D_FD_WARRANT_LEGACY ");
            System.out.println("All Rows are deleted from legacy = " + delete);
            stTmp.close();
            // Table Cleared...
            
            rsLegacy=stLegacy.executeQuery("SELECT * FROM D_FD_WARRANT_LEGACY LIMIT 1");
            rsLegacy.first();
            System.out.println("Importing Records To Legacy Table...... ");
            while(true) {
                String FileRecord=aFile.readLine();
                rsLegacy.moveToInsertRow();
                Pointer=0;
                rsLegacy.updateLong("COUNTER", Counter);Pointer+=10;
                rsLegacy.updateString("BOOK_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=8;
                rsLegacy.updateString("DEPOSIT_TYPE", FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                rsLegacy.updateString("WARRANT_NO", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("MAIN_ACCOUNT_CODE", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("PARTY_CODE", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                //String ReceiptNo = "M"+FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("RECEIPT_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=12;
                String warrantDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("WARRANT_DATE",warrantDate);
                Pointer+=25;
                Pointer+=2;
                double grossInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+9)+"."+FileRecord.substring(Pointer+9,Pointer+11));
                rsLegacy.updateDouble("GROSS_INTEREST", grossInterest); Pointer+=11;
                //double Tax = Double.parseDouble(FileRecord.substring(Pointer,Pointer+5)+"."+FileRecord.substring(Pointer+5,Pointer+7));
                rsLegacy.updateDouble("TAX", 0.0); //Pointer+=7;
                //double netInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+6)+"."+FileRecord.substring(Pointer+6,Pointer+8));
                rsLegacy.updateDouble("NET_INTEREST", grossInterest); //Pointer+=9;
                Pointer+=1;
                String intMainCode = FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("INTEREST_MAIN_CODE", intMainCode);
                Pointer+=33;
                //rsLegacy.updateString("DEPOSIT_YEAR", FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                rsLegacy.updateString("CHALLAN_NO", ""); //Pointer+=6;
                rsLegacy.updateString("CHALLAN_DATE", "0000-00-00"); //Pointer+=6;
                rsLegacy.updateInt("MICR_TDS_NO", 0); //Pointer+=6;
                //Pointer+=3;
                rsLegacy.insertRow();
                //System.out.println("Record No. = " + Counter + " Receipt No.= " + ReceiptNo);
                ++Counter;
            }
        } catch(Exception e) {
        }
        --Counter;
        System.out.println("Finished posting to legacy table. " + Counter + " records has been posted to legacy.\n");
    }
    
    private void ImportToWarrant() {
        long Counter = 1;
        long SubRecord = 0;
        Connection objConn=null;
        Statement stWarrantH=null;
        Statement stWarrantD=null;
        Statement stLegacy = null;
        ResultSet rsWarrantH=null;
        ResultSet rsWarrantD=null;
        ResultSet rsLegacy=null;
        
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stWarrantH=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stWarrantD=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            String StartDate = "2004-03-01";
            String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
            String LastDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
            
            rsWarrantH=stWarrantH.executeQuery("SELECT * FROM D_FD_INT_CALC_HEADER");
            rsWarrantH.first();
            
            rsWarrantD=stWarrantD.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL");
            rsWarrantD.first();
            System.out.println("Importing Records To Warrant Table...... ");
            int SrNo = 0;
            
            //while(java.sql.Date.valueOf(StartDate).before(java.sql.Date.valueOf(CurrentDate))) {
            rsLegacy = data.getResult("SELECT * FROM D_FD_WARRANT_LEGACY WHERE WARRANT_DATE<='2004-03-31' ",FinanceGlobal.FinURL);
            rsLegacy.first();
            SrNo = 0;
            String DocNo="";
            if(rsLegacy.getRow()>0) {
                rsWarrantH.moveToInsertRow();
                rsWarrantH.updateLong("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                DocNo = getMaxDocNo();
                rsWarrantH.updateString("DOC_NO", DocNo);
                rsWarrantH.updateString("DOC_DATE",StartDate);
                rsWarrantH.updateString("EFFECTIVE_DATE",LastDate);
                rsWarrantH.updateString("REMARKS","");
                rsWarrantH.updateString("BOOK_CODE",rsLegacy.getString("BOOK_CODE"));
                rsWarrantH.updateBoolean("APPROVED",true);
                rsWarrantH.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsWarrantH.updateBoolean("REJECTED", false);
                rsWarrantH.updateString("REJECTED_DATE", "0000-00-00");
                rsWarrantH.updateString("REJECTED_REMARKS", "");
                rsWarrantH.updateString("CREATED_BY", "Admin");
                rsWarrantH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsWarrantH.updateString("MODIFIED_BY", "Admin");
                rsWarrantH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsWarrantH.updateInt("HIERARCHY_ID", 1060);
                rsWarrantH.updateBoolean("CHANGED", true);
                rsWarrantH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsWarrantH.updateBoolean("CANCELLED", false);
                rsWarrantH.insertRow();
                //rsLegacy.next();
                
                while(!rsLegacy.isAfterLast()) {
                    rsWarrantD.moveToInsertRow();
                    rsWarrantD.updateLong("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                    rsWarrantD.updateString("DOC_NO", DocNo);
                    ++SrNo;
                    rsWarrantD.updateInt("SR_NO", SrNo);
                    rsWarrantD.updateString("RECEIPT_NO","");
                    rsWarrantD.updateInt("INTEREST_DAYS",0);
                    rsWarrantD.updateDouble("INTEREST_RATE",0.0);
                    rsWarrantD.updateString("PARTY_CODE",rsLegacy.getString("PARTY_CODE"));
                    rsWarrantD.updateString("MAIN_ACCOUNT_CODE", rsLegacy.getString("MAIN_ACCOUNT_CODE"));
                    rsWarrantD.updateString("INT_MAIN_ACCOUNT_CODE", rsLegacy.getString("INTEREST_MAIN_CODE"));
                    String WarrantNo = rsLegacy.getString("WARRANT_NO");
                    String WarrantDate = rsLegacy.getString("WARRANT_DATE");
                    rsWarrantD.updateString("WARRANT_NO", getNextWarrantNo(WarrantNo,WarrantDate));
                    rsWarrantD.updateInt("MICR_NO", 0);
                    rsWarrantD.updateDouble("INTEREST_AMOUNT", rsLegacy.getDouble("GROSS_INTEREST"));
                    rsWarrantD.updateDouble("TDS_AMOUNT", rsLegacy.getDouble("TAX"));
                    rsWarrantD.updateDouble("NET_INTEREST", rsLegacy.getDouble("NET_INTEREST"));
                    rsWarrantD.updateString("WARRANT_DATE", WarrantDate);
                    rsWarrantD.updateString("WARRANT_CLEAR", "I");
                    rsWarrantD.updateString("CREATED_BY", "Admin");
                    rsWarrantD.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateString("MODIFIED_BY", "Admin");
                    rsWarrantD.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateBoolean("CHANGED", true);
                    rsWarrantD.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantD.updateBoolean("CANCELLED", false);
                    rsWarrantD.insertRow();
                    rsLegacy.next();
                }
            }
            System.out.println("SubRecord = "+SrNo+" has been posted.");
        } catch(Exception e) {
            e.printStackTrace();
        }
        //System.out.println("\nFinished posting to warrant table. " + Counter + " records with subrecord = "+ SubRecord + " has been posted to warrant.\n");
    }
    
    private String getMaxDocNo() {
        String MaxDocNo = "";
        String strSQL="SELECT MAX(SUBSTRING(DOC_NO,LENGTH('M')+1)) AS MAX_NO FROM D_FD_INT_CALC_HEADER WHERE DOC_NO LIKE 'M%'";
        int MaxNo = UtilFunctions.CInt(data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL))+1;
        String strMaxNo=Integer.toString(MaxNo);
        strMaxNo=EITLERPGLOBAL.Replicate("0", 5-strMaxNo.length())+strMaxNo;
        strMaxNo = "M"+strMaxNo;
        return strMaxNo;
    }
    
    private String getNextWarrantNo(String WarrantNo, String WarrantDate) {
        String nextWarrantNo = "";
        String FinYear = "";
        try {
            int intMonth = Integer.parseInt(WarrantDate.substring(5,7));
            int intYear = Integer.parseInt(WarrantDate.substring(0,4));
            if(intMonth>=4) {//April
                FinYear = Integer.toString(intYear);
            }
            else {
                intYear--;
                FinYear = Integer.toString(intYear);
            }
            FinYear= FinYear.substring(2,4);
            nextWarrantNo = "M" + FinYear + WarrantNo.substring(2,6);
        } catch(Exception e) {
            return nextWarrantNo;
        }
        return nextWarrantNo;
    }
    
    private String getInterestDate(int DepositTypeID, String EffectiveDate, String ReceiptNo) {
        String InterestDate = "";
        //String EffectiveDate = EffectiveDate;//
        String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+"",FinanceGlobal.FinURL);
        try {
            if(DepositTypeID==2) {
                //int interestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+EITLERPGLOBAL.getCombostrCode(cmbSchemeID).toString()+"'",FinanceGlobal.FinURL);
                //InterestDate=clsCalcInterest.addMonthToDate(EffectiveDate, interestCalcPeriod);
            } else if(DepositTypeID==1) {
                boolean Find = true;
                String tmpDate = "";
                int EffectiveYear = EITLERPGLOBAL.getYear(EffectiveDate);
                ResultSet rsDate = data.getResult("SELECT * FROM D_FD_INT_CALC_DATE ORDER BY INTEREST_MONTH",FinanceGlobal.FinURL);
                rsDate.first();
                while(!rsDate.isAfterLast() && Find) {
                    tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                    if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(EffectiveDate))) {
                        InterestDate=tmpDate;
                        Find=false;
                    }
                    rsDate.next();
                }
                
                if(Find) {
                    rsDate.first();
                    EffectiveYear++;
                    while(!rsDate.isAfterLast() && Find) {
                        tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                        if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(EffectiveDate))) {
                            InterestDate=tmpDate;
                            Find=false;
                        }
                        rsDate.next();
                    }
                }
                if(java.sql.Date.valueOf(InterestDate).compareTo(java.sql.Date.valueOf(MaturityDate))==0 || java.sql.Date.valueOf(InterestDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    InterestDate = clsDepositMaster.deductDays(MaturityDate,1);
                }
            }
        } catch(Exception e) {
        }
        return InterestDate;
    }
    
    public static String getNextInterestDate(int CompanyID, String ReceiptNo) {
        String nextInterestCalcDate="";
        try {
            int DepositType = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"", FinanceGlobal.FinURL);
            String InterestCalcDate = data.getStringValueFromDB("SELECT INT_CALC_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
            String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
            
            String checkDate = EITLERPGLOBAL.addDaysToDate(InterestCalcDate, 1, "yyyy-MM-dd");
            if(java.sql.Date.valueOf(checkDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0 ) {
                return InterestCalcDate;
            }
            
            if(DepositType == 2) {
                int InterestCalcPeriod = data.getIntValueFromDB("SELECT A.INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER A, D_FD_DEPOSIT_MASTER B WHERE A.COMPANY_ID=B.COMPANY_ID AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.SCHEME_ID=B.SCHEME_ID AND B.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                nextInterestCalcDate = clsCalcInterest.addMonthToDate(InterestCalcDate,InterestCalcPeriod);
                if(java.sql.Date.valueOf(nextInterestCalcDate).compareTo(java.sql.Date.valueOf(MaturityDate))==0 || java.sql.Date.valueOf(nextInterestCalcDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    nextInterestCalcDate = clsDepositMaster.deductDays(MaturityDate, 1);
                }
            } else if(DepositType==1) {
                boolean Find = true;
                String tmpDate = "";
                int EffectiveYear = EITLERPGLOBAL.getYear(InterestCalcDate);
                ResultSet rsDate = data.getResult("SELECT * FROM D_FD_INT_CALC_DATE ORDER BY INTEREST_MONTH",FinanceGlobal.FinURL);
                rsDate.first();
                
                while(!rsDate.isAfterLast() && Find) {
                    tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                    if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(InterestCalcDate))) {
                        nextInterestCalcDate=tmpDate;
                        Find=false;
                    }
                    rsDate.next();
                }
                
                if(Find) {
                    rsDate.first();
                    EffectiveYear++;
                    while(!rsDate.isAfterLast() && Find){
                        tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                        if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(InterestCalcDate))) {
                            nextInterestCalcDate=tmpDate;
                            Find=false;
                        }
                        rsDate.next();
                    }
                }
                
                if(java.sql.Date.valueOf(nextInterestCalcDate).compareTo(java.sql.Date.valueOf(MaturityDate))==0 || java.sql.Date.valueOf(nextInterestCalcDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    nextInterestCalcDate = clsDepositMaster.deductDays(MaturityDate,1);
                }
            }
        } catch(Exception e) {
            return nextInterestCalcDate;
        }
        return nextInterestCalcDate;
    }
}