/*
 * clsImportDeposit.java
 *
 * Created on January 08, 2008, 1:31 PM
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
public class clsImportDeposit {
    
    /** Creates a new instance of clsSOImport */
    public clsImportDeposit() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportDeposit objImport=new clsImportDeposit();
            objImport.ImportToLegacy("/data/nisarg/FixedDeposit/DEPOSITLIVE.txt");
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
                //if(Counter == 5530) {
                //System.out.println("Test...");
                //}
                String FileRecord=aFile.readLine();
                rsLegacy.moveToInsertRow();
                Pointer=0;
                rsLegacy.updateLong("COUNTER", Counter);
                rsLegacy.updateString("REC_TYPE", FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                String LegacyNo = FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("REC_LEGACY_NO", LegacyNo); Pointer+=6;
                rsLegacy.updateString("REC_YEAR", FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                
                String IssueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("ISSUE_DATE",IssueDate);
                
                rsLegacy.updateString("TITLE",FileRecord.substring(Pointer,Pointer+7).trim()); Pointer+=7;
                
                rsLegacy.updateString("NAME",FileRecord.substring(Pointer,Pointer+40).trim()); Pointer+=40;
                
                rsLegacy.updateString("ADR1",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                rsLegacy.updateString("ADR2",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                rsLegacy.updateString("ADR3",FileRecord.substring(Pointer,Pointer+36).trim()); Pointer+=36;
                
                rsLegacy.updateString("PINCODE",FileRecord.substring(Pointer,Pointer+6).trim()); Pointer+=6;
                
                rsLegacy.updateString("JOINT_HD1",FileRecord.substring(Pointer,Pointer+30).trim()); Pointer+=30;
                rsLegacy.updateString("JOINT_HD2",FileRecord.substring(Pointer,Pointer+30).trim()); Pointer+=30;
                
                String JointHL3 = FileRecord.substring(Pointer,Pointer+30).trim();
                int lCounter = 0;
                boolean Find = true;
                while(lCounter<JointHL3.length() && Find) {
                    if ("0123456789".indexOf(JointHL3.charAt(lCounter)) != -1) {
                        Find=false;
                    }
                    lCounter++;
                }
                if(!Find) {
                    rsLegacy.updateString("JOINT_HD3",""); Pointer+=30;
                } else {
                    rsLegacy.updateString("JOINT_HD3",JointHL3); Pointer+=30;
                }
                
                rsLegacy.updateDouble("AMOUNT",Double.parseDouble(FileRecord.substring(Pointer,Pointer+8))); Pointer+=8;
                rsLegacy.updateString("CHEQUE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;
                String ChequeDate = FileRecord.substring(Pointer,Pointer+2)+"/"+FileRecord.substring(Pointer+2,Pointer+4)+"/"+"20"+FileRecord.substring(Pointer+4,Pointer+6);
                if(EITLERPGLOBAL.isDate(ChequeDate)) {
                    ChequeDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                    rsLegacy.updateString("CHEQUE_DATE", ChequeDate);
                } else {
                    Pointer+=6;
                    rsLegacy.updateString("CHEQUE_DATE", "0000-00-00");
                }
                rsLegacy.updateString("INT_PERIOD",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                
                double InterestRate = Double.parseDouble(FileRecord.substring(Pointer,Pointer+2)+"."+FileRecord.substring(Pointer+2,Pointer+4)); Pointer+=4;
                rsLegacy.updateDouble("INT_RATE",InterestRate);
                
                String ReceiptDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("REC_DATE",ReceiptDate);
                String DueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("MATURITY_DATE",DueDate);
                
                rsLegacy.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("PERTICULAR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                rsLegacy.updateString("OLD_REC_TYPE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                rsLegacy.updateString("OLD_REC_LEGACY_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("OLD_REC_YEAR",FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                rsLegacy.updateString("CATAGORY",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                rsLegacy.updateString("LF_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("EMP_NO", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("STATUS_CODE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                rsLegacy.updateString("BROKER_CODE",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;
                //if(DepositFile.substring(10,15).equals("CLOSE")) {
                    //rsLegacy.updateInt("DEPOSIT_STATUS",1);
                //} else if(DepositFile.substring(10,14).equals("LIVE")) {
                    rsLegacy.updateInt("DEPOSIT_STATUS",0);
                //}
                Pointer+=55;
                rsLegacy.updateString("MAIN_ACCOUNT_CODE", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                Pointer+=6;
                String BookCode = FileRecord.substring(Pointer,Pointer+2);
                if(BookCode.equals("39")) {
                    rsLegacy.updateString("BOOK_CODE", ""); Pointer+=2;
                } else {
                    rsLegacy.updateString("BOOK_CODE", BookCode); Pointer+=2;
                }
                
                Pointer+=11;
                rsLegacy.insertRow();
                //System.out.println("Record No. = " + Counter + " has been posted.");
                //System.out.println("Pointer = " + Integer.toString(Pointer));
                ++Counter;
            }
        } catch(Exception e) {
            //e.printStackTrace();
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
            ResultSet rsDeposit = stDeposit.executeQuery("SELECT * FROM D_FD_DEPOSIT_MASTER");
            rsDeposit.first();
            
            Statement stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsLegacy = stLegacy.executeQuery("SELECT * FROM D_FD_DEPOSIT_LEGACY");
            rsLegacy.first();
            
            Statement stdtmp = objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //For Close data
            //int delete = stdtmp.executeUpdate("DELETE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO LIKE 'M%' AND DEPOSIT_STATUS=1");
            //For Open Data
            int delete = stdtmp.executeUpdate("DELETE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO LIKE 'M%' AND DEPOSIT_STATUS=0");
            System.out.println("All Rows are deleted from DEPOSIT DATA= " + delete);
            
            
            stdtmp.close();
            
//            Statement stTmp=objConn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
//            delete = stTmp.executeUpdate("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO LIKE 'M%' AND MODULE_ID=85");
//            if (delete!=0) {
//            System.out.println("All Rows are deleted from DOC DATA = " + delete);
//            }
//            stTmp.close();
//            objConn1.close();
            
            while(!rsLegacy.isAfterLast()) {
                //if(Counter==280){
                //    System.out.println();
                //}
                rsDeposit.moveToInsertRow();
                rsDeposit.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                String LegacyNo = "M"+rsLegacy.getString("REC_LEGACY_NO").trim();
                rsDeposit.updateString("RECEIPT_NO",LegacyNo);
                rsDeposit.updateString("RECEIPT_DATE", rsLegacy.getString("REC_DATE"));
                rsDeposit.updateString("TITLE", rsLegacy.getString("TITLE"));
                rsDeposit.updateString("APPLICANT_NAME", rsLegacy.getString("NAME"));
                rsDeposit.updateString("ADDRESS1", rsLegacy.getString("ADR1"));
                rsDeposit.updateString("ADDRESS2", rsLegacy.getString("ADR2"));
                rsDeposit.updateString("ADDRESS3", rsLegacy.getString("ADR3"));
                rsDeposit.updateString("CITY", "");
                rsDeposit.updateString("PINCODE", rsLegacy.getString("PINCODE"));
                rsDeposit.updateString("CONTACT_NO", "");
                rsDeposit.updateString("APPLICANT2", rsLegacy.getString("JOINT_HD1"));
                rsDeposit.updateString("APPLICANT3", rsLegacy.getString("JOINT_HD2"));
                rsDeposit.updateString("APPLICANT4", rsLegacy.getString("JOINT_HD3"));
                rsDeposit.updateString("NOMINEE_1_NAME", "");
                rsDeposit.updateString("NOMINEE_2_NAME", "");
                rsDeposit.updateString("NOMINEE_3_NAME", "");
                
                //Other Detail
                if(rsLegacy.getString("REC_TYPE").equals("LD")) {
                    if(rsLegacy.getString("MAIN_ACCOUNT_CODE").equals("115036")) {
                        rsDeposit.updateString("SCHEME_ID", "000001");
                    } else if(rsLegacy.getString("MAIN_ACCOUNT_CODE").equals("115029")) {
                        rsDeposit.updateString("SCHEME_ID", "000002");
                    }
                    rsDeposit.updateInt("DEPOSITER_STATUS", 1);
                } else if(rsLegacy.getString("REC_TYPE").equals("CD")) {
                    if(rsLegacy.getString("MAIN_ACCOUNT_CODE").equals("115153")) {
                        rsDeposit.updateString("SCHEME_ID", "000004");
                    } else if(rsLegacy.getString("MAIN_ACCOUNT_CODE").equals("115177")) {
                        rsDeposit.updateString("SCHEME_ID", "000005");
                    }
                    rsDeposit.updateInt("DEPOSITER_STATUS", 4);
                } else if(rsLegacy.getString("REC_TYPE").equals("FD")) {
                    rsDeposit.updateString("SCHEME_ID", "000003");
                    rsDeposit.updateInt("DEPOSITER_STATUS", 4);
                }
                
                if(rsLegacy.getString("STATUS_CODE").equals("S")) {
                    rsDeposit.updateInt("DEPOSITER_CATEGORY", 2);
                } else if(rsLegacy.getString("STATUS_CODE").equals("D")) {
                    rsDeposit.updateInt("DEPOSITER_CATEGORY", 4);
                } else {
                    rsDeposit.updateInt("DEPOSITER_CATEGORY", 5);
                }
                
                if(rsLegacy.getString("REC_TYPE").equals("LD") || rsLegacy.getString("REC_TYPE").equals("FD")) {
                    rsDeposit.updateInt("DEPOSIT_TYPE_ID", 1);
                } else if(rsLegacy.getString("REC_TYPE").equals("CD")) {
                    rsDeposit.updateInt("DEPOSIT_TYPE_ID", 2);
                }
                
                if(!rsLegacy.getString("JOINT_HD1").trim().equals("") || !rsLegacy.getString("JOINT_HD2").trim().equals("") || !rsLegacy.getString("JOINT_HD3").trim().equals("")) {
                    rsDeposit.updateInt("DEPOSIT_PAYABLE_TO", 2);
                } else {
                    rsDeposit.updateInt("DEPOSIT_PAYABLE_TO", 1);
                }
                
                rsDeposit.updateString("MAIN_ACCOUNT_CODE", rsLegacy.getString("MAIN_ACCOUNT_CODE"));
                String MainAccountCode = rsLegacy.getString("MAIN_ACCOUNT_CODE");
                String IntMainAccountCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_SCHEME_MASTER WHERE DEPOSIT_MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("INTEREST_MAIN_CODE", IntMainAccountCode);
                rsDeposit.updateInt("DEPOSIT_PERIOD", rsLegacy.getInt("INT_PERIOD"));
                rsDeposit.updateDouble("INTEREST_RATE", rsLegacy.getDouble("INT_RATE"));
                
                rsDeposit.updateString("DEPOSITER_CATEGORY_OTHERS", "");
                rsDeposit.updateString("FOLIO_NO", rsLegacy.getString("LF_NO").trim());
                rsDeposit.updateString("EMPLOYEE_CODE", rsLegacy.getString("EMP_NO").trim());
                String LegacyDate = rsLegacy.getString("REC_DATE");
                rsDeposit.updateString("EFFECTIVE_DATE", LegacyDate);
                String MaturityDate = rsLegacy.getString("MATURITY_DATE");
                rsDeposit.updateString("MATURITY_DATE", MaturityDate);
                
                String NextInterestDate = "0000-00-00";
                if(rsLegacy.getInt("DEPOSIT_STATUS") == 1) {
                    NextInterestDate = getNextInterestDate(rsLegacy.getString("REC_LEGACY_NO").trim(),LegacyDate,MaturityDate);
                    rsDeposit.updateString("INT_CALC_DATE", NextInterestDate);
                } else {
                    NextInterestDate = getNextInterestDate(rsLegacy.getString("REC_LEGACY_NO").trim(),LegacyDate,MaturityDate);
                    rsDeposit.updateString("INT_CALC_DATE", NextInterestDate);
                }
                
                rsDeposit.updateInt("TAX_EX_FORM_RECEIVED",0);
                rsDeposit.updateString("PARTY_CODE", rsLegacy.getString("PARTY_CODE"));
                rsDeposit.updateInt("TDS_APPLICABLE", 0);
                rsDeposit.updateString("PAN_NO", "");
                rsDeposit.updateString("PAN_DATE", "0000-00-00");
                if(!rsLegacy.getString("OLD_REC_LEGACY_NO").trim().equals("")) {
                    rsDeposit.updateString("OLD_RECEIPT_NO", rsLegacy.getString("OLD_REC_LEGACY_NO").trim());
                } else {
                    rsDeposit.updateString("OLD_RECEIPT_NO", "");
                }
                String Particular = rsLegacy.getString("PERTICULAR").trim();
                if(Particular.equals("F")) {
                    rsDeposit.updateString("PARTICULARS", "FRESH");
                    rsDeposit.updateString("REALIZATION_DATE", rsLegacy.getString("REC_DATE"));
                } else {
                    rsDeposit.updateString("PARTICULARS", "RENEWAL");
                    rsDeposit.updateString("REALIZATION_DATE", "0000-00-00");
                }
                
                //Bank Detail
                rsDeposit.updateString("CHEQUE_NO", rsLegacy.getString("CHEQUE_NO"));
                rsDeposit.updateString("CHEQUE_DATE", rsLegacy.getString("CHEQUE_DATE"));
                
                rsDeposit.updateString("DEPOSIT_DATE", rsLegacy.getString("CHEQUE_DATE"));
                rsDeposit.updateDouble("AMOUNT", rsLegacy.getDouble("AMOUNT"));
                String BookCode = rsLegacy.getString("BOOK_CODE");
                String BankMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
                String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("BANK_MAIN_CODE", BankMainCode);
                rsDeposit.updateString("BANK_NAME", BankName);
                rsDeposit.updateString("BANK_ADDRESS", "");
                rsDeposit.updateString("BANK_CITY", "");
                rsDeposit.updateString("BANK_PINCODE", "");
                
                String BrokerCode = rsLegacy.getString("BROKER_CODE");
                rsDeposit.updateString("BROKER_CODE", BrokerCode);
                String BrokerName = data.getStringValueFromDB("SELECT BROKER_NAME FROM D_FD_BROKER_MASTER WHERE BROKER_CODE='"+BrokerCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("BROKER_NAME", BrokerName);
                String BrokerAddress = data.getStringValueFromDB("SELECT BROKER_ADDRESS FROM D_FD_BROKER_MASTER WHERE BROKER_CODE='"+BrokerCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("BROKER_ADDRESS", BrokerAddress);
                String BrokerCity = data.getStringValueFromDB("SELECT BROKER_CITY FROM D_FD_BROKER_MASTER WHERE BROKER_CODE='"+BrokerCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("BROKER_CITY", BrokerCity);
                String BrokerPincode = data.getStringValueFromDB("SELECT BROKER_PINCODE FROM D_FD_BROKER_MASTER WHERE BROKER_CODE='"+BrokerCode+"' ",FinanceGlobal.FinURL);
                rsDeposit.updateString("BROKER_PINCODE", BrokerPincode);
                
                //Deposit Releated Information
                if(Particular.equals("F")) {
                    rsDeposit.updateInt("DEPOSIT_ENTRY_TYPE",1);
                } else {
                    rsDeposit.updateInt("DEPOSIT_ENTRY_TYPE",2);
                }
                
                rsDeposit.updateInt("PREMATURE", 0);
                rsDeposit.updateString("PM_DATE", "0000-00-00");   // Blank for new entry
                rsDeposit.updateInt("DEPOSIT_STATUS", rsLegacy.getInt("DEPOSIT_STATUS"));
                
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
                
                rsDeposit.updateInt("HIERARCHY_ID", 971);
                rsDeposit.updateBoolean("CHANGED", true);
                rsDeposit.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsDeposit.updateBoolean("CANCELLED", false);
                rsDeposit.insertRow();
                /*
                //----------- Update Approval Flow -------------------//
                ApprovalFlow ObjFlow=new ApprovalFlow();
                ObjFlow.CompanyID = EITLERPGLOBAL.gCompanyID;
                ObjFlow.ModuleID = clsDepositMaster.ModuleID ;
                ObjFlow.DocNo = LegacyNo;
                ObjFlow.From = 1;
                ObjFlow.To = 1;
                ObjFlow.Status = "F";
                ObjFlow.TableName = "D_FD_DEPOSIT_MASTER";
                ObjFlow.IsCreator = true;
                ObjFlow.HierarchyID = 971;
                ObjFlow.Remarks = "";
                ObjFlow.FieldName ="RECEIPT_NO";
                ObjFlow.UseSpecifiedURL=true;
                ObjFlow.SpecificURL=FinanceGlobal.FinURL;
                
                if (ObjFlow.Status.equals("H")) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
                else {
                    if (!ObjFlow.UpdateFlow()) {
                        JOptionPane.showMessageDialog(null,"Error in Legacy No.:" + LegacyNo +"\n Error is : " + ObjFlow.LastError);
                    }
                }
                ObjFlow.UseSpecifiedURL=false;
                //------------------- End of Approval Flow ------------------// */
                System.out.println("Record to ERP " + Counter + ": with legacy no : "+LegacyNo+" posted.");
                ++Counter;
                rsLegacy.next();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        --Counter;
        System.out.println("Record No. = " + Counter + " has been posted.");
        System.out.println("FINISHED POSTING TO DEPOSIT TABLE...");
    }
    
    private String getNextInterestDate(String LegacyNo,String LegacyDate, String MaturityDate) {
        String nextIntDate="";
        boolean setDate = true;
        String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
        String Type = data.getStringValueFromDB("SELECT REC_TYPE FROM D_FD_DEPOSIT_LEGACY WHERE REC_LEGACY_NO='"+LegacyNo+"' ",FinanceGlobal.FinURL);
        int DepositStatus = data.getIntValueFromDB("SELECT DEPOSIT_STATUS FROM D_FD_DEPOSIT_LEGACY WHERE REC_LEGACY_NO='"+LegacyNo+"' ",FinanceGlobal.FinURL);
        if(Type.equals("CD")) {
            while(setDate) {
                if(DepositStatus==1) {
                    nextIntDate = clsDepositMaster.deductDays(MaturityDate, 1);
                    setDate = false;
                    continue;
                } else {
                    LegacyDate = clsCalcInterest.addMonthToDate(LegacyDate, 6);
                    if(java.sql.Date.valueOf(LegacyDate).after(java.sql.Date.valueOf("2008-11-30")) || java.sql.Date.valueOf(LegacyDate).compareTo(java.sql.Date.valueOf("2008-11-30"))==0) {
                        if(java.sql.Date.valueOf(LegacyDate).after(java.sql.Date.valueOf(MaturityDate))|| java.sql.Date.valueOf(LegacyDate).compareTo(java.sql.Date.valueOf("2008-11-30"))==0) {
                            nextIntDate = clsDepositMaster.deductDays(MaturityDate, 1);
                            setDate = false;
                            continue;
                        }
                        if(java.sql.Date.valueOf(LegacyDate).before(java.sql.Date.valueOf(MaturityDate))) {
                            nextIntDate = LegacyDate;
                            setDate = false;
                            continue;
                        }
                    }
                }
            }
        } else {
            if(DepositStatus==1) {
                nextIntDate = clsDepositMaster.deductDays(MaturityDate, 1);
                
            } else {
                if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf("2008-11-30"))) {
                    nextIntDate = clsDepositMaster.deductDays(MaturityDate, 1);
                }
                String EndFinYear = "2009-03-31";
                if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf(EndFinYear))) {
                    nextIntDate = clsDepositMaster.deductDays(MaturityDate, 1);
                }
                if(java.sql.Date.valueOf(MaturityDate).after(java.sql.Date.valueOf(EndFinYear))) {
                    nextIntDate = EndFinYear;
                }
            }
        }
        return nextIntDate;
    }
}