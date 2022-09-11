/*
 * clsImportSalesDeposit.java
 *
 * Created on February 10, 2009, 1:31 PM
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
 * @author  Mrugesh 
 */

public class clsImportSalesDeposit {
    
    /** Creates a new instance of clsSOImport */
    public clsImportSalesDeposit() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportSalesDeposit objImport=new clsImportSalesDeposit();
            objImport.ImportToLegacy("/root/Desktop/temp.fsd");
            objImport.ImportToDeposit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("The End");
    }
    
    private void ImportToLegacy(String DepositFile) {
        int Pointer = 0;
        int CompanyID = 2;
        long Counter = 1;
        Connection objConn=null;
        Statement stLegacy=null;
        ResultSet rsLegacy=null;
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            BufferedReader aFile=new BufferedReader(new FileReader(new File(DepositFile)));
            
            // Clear all past entry of legacy entry.
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int delete = stTmp.executeUpdate("DELETE FROM D_FD_DEPOSIT_LEGACY");
            if (delete!=0) {
                System.out.println("All Rows are deleted = " + delete);
            }
            stTmp.close();
            // Table Cleared...
            
            rsLegacy=stLegacy.executeQuery("SELECT * FROM D_FD_DEPOSIT_LEGACY");
            rsLegacy.first();
            System.out.println("Importing Records...... ");
            while(true) {
                
                String FileRecord=aFile.readLine();
                rsLegacy.moveToInsertRow();
                Pointer=0;
                rsLegacy.updateLong("COUNTER", Counter);
                String RecType = FileRecord.substring(Pointer,Pointer+2);
                rsLegacy.updateString("REC_TYPE", FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                String LegacyNo = FileRecord.substring(Pointer,Pointer+6);
                if(LegacyNo.equals("000000")) {
                    System.out.println("WRONG RECEIPT NO: " + LegacyNo);
                    rsLegacy.updateString("REC_LEGACY_NO", LegacyNo); Pointer+=6;
                } else {
                    rsLegacy.updateString("REC_LEGACY_NO", LegacyNo); Pointer+=6;
                }
                rsLegacy.updateString("REC_YEAR", FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                int year = Integer.parseInt(FileRecord.substring(Pointer+4,Pointer+6));
                String IssueDate="";
                if(year <= 50 && year>=0) {
                    IssueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                } else {
                    IssueDate = "19"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                }
                rsLegacy.updateString("ISSUE_DATE",IssueDate);
                rsLegacy.updateString("TITLE",FileRecord.substring(Pointer,Pointer+7).trim()); Pointer+=7;
                rsLegacy.updateString("NAME",FileRecord.substring(Pointer,Pointer+40).trim()); Pointer+=40;
                rsLegacy.updateString("ADR1",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                rsLegacy.updateString("ADR2",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                rsLegacy.updateString("ADR3",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                String PinCode = FileRecord.substring(Pointer,Pointer+6).trim();
                if(PinCode.equals("000000")) {
                    rsLegacy.updateString("PINCODE",""); Pointer+=6;
                } else {
                    rsLegacy.updateString("PINCODE",PinCode); Pointer+=6;
                }
                
                rsLegacy.updateString("JOINT_HD1",FileRecord.substring(Pointer,Pointer+30).trim()); Pointer+=30;
                rsLegacy.updateString("JOINT_HD2",FileRecord.substring(Pointer,Pointer+30).trim()); Pointer+=30;
                rsLegacy.updateString("JOINT_HD3",FileRecord.substring(Pointer,Pointer+30).trim()); Pointer+=30; //City
                
                rsLegacy.updateDouble("AMOUNT",Double.parseDouble(FileRecord.substring(Pointer,Pointer+8))); Pointer+=8;
                String ChequeNo = FileRecord.substring(Pointer,Pointer+8);
                if(ChequeNo.equals("00000000")) {
                    rsLegacy.updateString("CHEQUE_NO",""); Pointer+=8;
                } else {
                    rsLegacy.updateString("CHEQUE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;
                }
                
                String ChequeDate = FileRecord.substring(Pointer,Pointer+2)+"/"+FileRecord.substring(Pointer+2,Pointer+4)+"/"+"20"+FileRecord.substring(Pointer+4,Pointer+6);
                if(EITLERPGLOBAL.isDate(ChequeDate)) {
                    ChequeDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                    rsLegacy.updateString("CHEQUE_DATE", ChequeDate);
                } else {
                    Pointer+=6;
                    rsLegacy.updateString("CHEQUE_DATE", "0000-00-00");
                }
                
                int Period = Integer.parseInt(FileRecord.substring(Pointer,Pointer+2));
                if(Period == 0) {
                    rsLegacy.updateInt("INT_PERIOD",0); Pointer+=2;
                } else {
                    rsLegacy.updateInt("INT_PERIOD",Period); Pointer+=2;
                }
                
                double InterestRate = Double.parseDouble(FileRecord.substring(Pointer,Pointer+2)+"."+FileRecord.substring(Pointer+2,Pointer+4)); Pointer+=4;
                rsLegacy.updateDouble("INT_RATE",InterestRate);
                
                year = Integer.parseInt(FileRecord.substring(Pointer+4,Pointer+6));
                String ReceiptDate="";
                if(year <= 8 && year>=0) {
                    ReceiptDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                } else {
                    ReceiptDate = "19"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                }
                rsLegacy.updateString("REC_DATE",ReceiptDate);
                
                
                String DueDate = FileRecord.substring(Pointer,Pointer+6);
                if(DueDate.equals("000000")) {
                    DueDate = "0000-00-00";
                } else {
                    DueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                }
                rsLegacy.updateString("MATURITY_DATE",DueDate); Pointer+=6;
                
                String PartyCode = FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("PARTY_CODE",PartyCode); Pointer+=6;
                rsLegacy.updateString("PERTICULAR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                rsLegacy.updateString("OLD_REC_TYPE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                
                String oRecNo=FileRecord.substring(Pointer,Pointer+6);
                if(oRecNo.equals("000000")) {
                    rsLegacy.updateString("OLD_REC_LEGACY_NO",""); Pointer+=6;
                } else {
                    rsLegacy.updateString("OLD_REC_LEGACY_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                }
                String OldReceiptYear = FileRecord.substring(Pointer,Pointer+4);
                if(OldReceiptYear.equals("0000")) {
                    rsLegacy.updateString("OLD_REC_YEAR",""); Pointer+=4;
                } else {
                    rsLegacy.updateString("OLD_REC_YEAR",FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                }
                
                rsLegacy.updateString("CATAGORY",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                rsLegacy.updateString("LF_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                String empNo = FileRecord.substring(Pointer,Pointer+6);
                if(empNo.equals("000000")) {
                    rsLegacy.updateString("EMP_NO", ""); Pointer+=6;
                } else {
                    rsLegacy.updateString("EMP_NO", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                }
                
                String StatusCode = FileRecord.substring(Pointer,Pointer+1);
                rsLegacy.updateString("STATUS_CODE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                
                String BCode = FileRecord.substring(Pointer,Pointer+3);
                if(BCode.equals("000") || BCode.equals("")) {
                    rsLegacy.updateString("BROKER_CODE",""); Pointer+=3;
                } else {
                    rsLegacy.updateString("BROKER_CODE",BCode); Pointer+=3;
                }
                
                rsLegacy.updateInt("DEPOSIT_STATUS",0);
                
                String MainAccountCode = "";
                String InterestMainCode = "";
                if(RecType.equals("DD")) {
                    MainAccountCode = "132642";
                    InterestMainCode = "133155";
                } else if(RecType.equals("ND")) {
                    MainAccountCode = "132666";
                    InterestMainCode = "133203";
                } else if(RecType.equals("SL")) {
                    if(PartyCode.startsWith("6")) {
                        MainAccountCode = "132714";
                    } else {
                        MainAccountCode = "132635";
                    }
                    InterestMainCode = "133162";
                }
                rsLegacy.updateString("MAIN_ACCOUNT_CODE",MainAccountCode);
                rsLegacy.updateString("BOOK_CODE","");
                
                Pointer+=55;
                
                rsLegacy.insertRow();
                //System.out.println("Record No. = " + Counter + " with Receipt No.= "+LegacyNo+" has been posted. Pointer = " + Pointer);
                ++Counter;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        --Counter;
        System.out.println("Record No. = " + Counter + " has been posted.");
        System.out.println("FINISHED POSTING TO LEGACY TABLE...");
    }
    
    private void ImportToDeposit() {
        long Counter = 1;
        try {
            Connection objConn=data.getConn(FinanceGlobal.FinURL);
            Connection objConn1=data.getConn();
            
            Statement stDeposit=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDeposit = stDeposit.executeQuery("SELECT * FROM D_FD_SALES_DEPOSIT_MASTER LIMIT 1");
            rsDeposit.first();
            
            Statement stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsLegacy = stLegacy.executeQuery("SELECT * FROM D_FD_DEPOSIT_LEGACY ORDER BY COUNTER");
            rsLegacy.first();
            
            /*Statement stdtmp = objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //For Open Data
            int delete = stdtmp.executeUpdate("DELETE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO LIKE 'M%' AND DEPOSIT_STATUS=0");
            System.out.println("All Rows are deleted from DEPOSIT DATA= " + delete);
            
            
            stdtmp.close();*/
            
            while(!rsLegacy.isAfterLast()) {
                String LegacyNo = "M"+rsLegacy.getString("REC_LEGACY_NO").trim();
                rsDeposit.moveToInsertRow();
                rsDeposit.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                String Date = rsLegacy.getString("REC_DATE").substring(8,10);
                String Month = rsLegacy.getString("REC_DATE").substring(5,7);
                
                LegacyNo = "M"+Date+Month+rsLegacy.getString("REC_LEGACY_NO").trim();//.substring(2);
                rsDeposit.updateString("RECEIPT_NO",LegacyNo);
                
                rsDeposit.updateString("RECEIPT_DATE", rsLegacy.getString("REC_DATE"));
                rsDeposit.updateString("TITLE", rsLegacy.getString("TITLE"));
                rsDeposit.updateString("APPLICANT_NAME", rsLegacy.getString("NAME"));
                rsDeposit.updateString("ADDRESS1", rsLegacy.getString("ADR1"));
                rsDeposit.updateString("ADDRESS2", rsLegacy.getString("ADR2"));
                rsDeposit.updateString("ADDRESS3", rsLegacy.getString("ADR3"));
                rsDeposit.updateString("CITY", rsLegacy.getString("JOINT_HD3"));
                
                rsDeposit.updateString("PINCODE", rsLegacy.getString("PINCODE"));
                rsDeposit.updateString("CONTACT_NO", "");
                
                rsDeposit.updateDouble("AMOUNT", rsLegacy.getDouble("AMOUNT"));
                rsDeposit.updateString("CHEQUE_NO", rsLegacy.getString("CHEQUE_NO"));
                rsDeposit.updateString("CHEQUE_DATE", rsLegacy.getString("CHEQUE_DATE"));
                rsDeposit.updateString("FUND_TRANSFER_FROM", "");
                rsDeposit.updateString("BANK_MAIN_CODE", "");
                rsDeposit.updateString("BANK_NAME", "");
                rsDeposit.updateString("BANK_ADDRESS", "");
                rsDeposit.updateString("BANK_CITY", "");
                rsDeposit.updateString("BANK_PINCODE", "");
                rsDeposit.updateString("REALIZATION_DATE", rsLegacy.getString("REC_DATE"));
                rsDeposit.updateString("EFFECTIVE_DATE", rsLegacy.getString("REC_DATE"));
                rsDeposit.updateString("REFUND_DATE", "0000-00-00");
                rsDeposit.updateString("INT_CALC_DATE", "2012-03-31");
                String MainAccountCode = rsLegacy.getString("MAIN_ACCOUNT_CODE");
                String InterestMainCode = "";
                if(MainAccountCode.equals("132642")) {
                    InterestMainCode = "133155";
                } else if(MainAccountCode.equals("132666")) {
                    InterestMainCode = "133203";
                } else if(MainAccountCode.equals("132635")) {
                    InterestMainCode = "133162";
                } else if(MainAccountCode.equals("132714")) {
                    InterestMainCode = "133162";
                }
                String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE INT_MAIN_ACCOUNT_CODE='"+InterestMainCode+"' AND DEPOSIT_MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("SCHEME_ID", SchemeID);
                
                rsDeposit.updateInt("DEPOSIT_TYPE_ID",1);
                rsDeposit.updateDouble("INTEREST_RATE",rsLegacy.getDouble("INT_RATE"));
                
                rsDeposit.updateInt("DEPOSITER_STATUS",4);
                rsDeposit.updateInt("DEPOSITER_CATEGORY",1);
                rsDeposit.updateString("DEPOSITER_CATEGORY_OTHERS","");
                rsDeposit.updateInt("DEPOSIT_PAYABLE_TO",1);
                rsDeposit.updateInt("TAX_EX_FORM_RECEIVED",0);
                rsDeposit.updateInt("TDS_APPLICABLE",1);
                rsDeposit.updateString("PAN_NO","");
                rsDeposit.updateString("PAN_DATE","0000-00-00");
                rsDeposit.updateString("PARTICULARS","");
                rsDeposit.updateString("PARTY_CODE",rsLegacy.getString("PARTY_CODE"));
                
                
                rsDeposit.updateString("MAIN_ACCOUNT_CODE", MainAccountCode);
                rsDeposit.updateString("INTEREST_MAIN_CODE", InterestMainCode);
                rsDeposit.updateInt("DEPOSIT_STATUS",0);
                
                //Approval Specific
                rsDeposit.updateBoolean("APPROVED",true);
                rsDeposit.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDeposit.updateBoolean("REJECTED", false);
                rsDeposit.updateString("REJECTED_DATE", "0000-00-00");
                rsDeposit.updateString("REJECTED_REMARKS", "");
                rsDeposit.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsDeposit.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDeposit.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsDeposit.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                
                rsDeposit.updateInt("HIERARCHY_ID", 1088);
                rsDeposit.updateBoolean("CHANGED", true);
                rsDeposit.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDeposit.updateBoolean("CANCELLED", false);
                rsDeposit.insertRow();
                //System.out.println("==>Record to ERP " + Counter + ": with legacy no : "+LegacyNo +"  posted.");
                ++Counter;
                rsLegacy.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        --Counter;
        System.out.println("Record No. = " + Counter + " has been posted.");
        System.out.println("FINISHED POSTING TO DEPOSIT TABLE...");
    }
}