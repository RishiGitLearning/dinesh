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
public class clsImportWarrant {
    
    /** Creates a new instance of clsSOImport */
    public clsImportWarrant() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportWarrant objImport=new clsImportWarrant();
            /* current code for amount updation
             
            
            */
            //objImport.ImportAmount("/root/Desktop/WARRANT.TXT");
            objImport.FindData();
            
            
            //objImport.ImportToLegacy("/data/nisarg/FixedDeposit/warrant.txt");
            //objImport.ImportToWarrant();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("The End.");
    }
    
    private void FindData() {
        long Counter = 0;
        Connection objConn = null;
        Statement stLegacy = null;
        ResultSet rsLegacy = null;
        String CWarrantNo = "", CWarrantDate="", CPartyCode="", EWarrantNo = "", EWarrantDate="", EPartyCode="", SQL="";
        double CGAmount=0,EGAmount=0,ETax=0,CTax=0,ENet=0,CNet=0  ;
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsLegacy=stLegacy.executeQuery("SELECT * FROM D_FD_WARRANT_LEGACY WHERE WARRANT_DATE<='2009-03-31' ORDER BY WARRANT_DATE");
            rsLegacy=stLegacy.executeQuery("SELECT * FROM D_FD_WARRANT_LEGACY WHERE WARRANT_DATE>='2009-04-01' ORDER BY WARRANT_DATE");
            rsLegacy.first();
            while(!rsLegacy.isAfterLast()) {
                Counter++;
                CWarrantNo = rsLegacy.getString("WARRANT_NO");
                CWarrantDate = rsLegacy.getString("WARRANT_DATE");
                CPartyCode = rsLegacy.getString("PARTY_CODE");
                CGAmount = rsLegacy.getDouble("GROSS_INTEREST");
                CTax = rsLegacy.getDouble("TAX");
                CNet = rsLegacy.getDouble("NET_INTEREST");
                //SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE<='2009-03-31' "; 
                SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE>='2009-04-01' ";
                if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    //SQL = "SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"'  AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE<='2009-03-31' ";
                    SQL = "SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"'  AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE>='2009-04-01' "; 
                    EGAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    //SQL = "SELECT TDS_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE<='2009-03-31' "; 
                    SQL = "SELECT TDS_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE>='2009-04-01' "; 
                    ETax = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    //SQL = "SELECT NET_INTEREST FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE<='2009-03-31' ";
                    SQL = "SELECT NET_INTEREST FROM D_FD_INT_CALC_DETAIL WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"' AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE>='2009-04-01' ";
                    ENet = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    
                    //SQL = "UPDATE D_FD_INT_CALC_DETAIL SET INTEREST_AMOUNT="+CGAmount+", TDS_AMOUNT="+ CTax +", NET_INTEREST="+ CNet +" " +
                    //"WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"'  AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE<='2009-03-31' "; 
                    
                    SQL = "UPDATE D_FD_INT_CALC_DETAIL SET INTEREST_AMOUNT="+CGAmount+", TDS_AMOUNT="+ CTax +", NET_INTEREST="+ CNet +" " +
                    "WHERE LEGACY_WARRANT_NO='"+CWarrantNo+"'  AND PARTY_CODE='"+CPartyCode+"' AND WARRANT_DATE>='2009-04-01' "; 
                    data.Execute(SQL, FinanceGlobal.FinURL);
                    
                    if((EITLERPGLOBAL.round(EGAmount - CGAmount,2) >= 1) || (EITLERPGLOBAL.round(ETax - CTax,2) >= 1) || (EITLERPGLOBAL.round(ENet - CNet,2) >= 1) ) { 
                        System.out.println("COUNTER = " + Counter +" WARRANT NO = " + CWarrantNo + " GROSS DIFF = "+ EITLERPGLOBAL.round(EGAmount - CGAmount,2)  + " TDS DIFF = " + EITLERPGLOBAL.round(ETax - CTax,2) + " NET DIFF = " + EITLERPGLOBAL.round(ENet - CNet,2));
                    }
                } else {
                    System.out.println("COUNTER = " + Counter +" Record Not Found... "+ CWarrantNo + " " + CWarrantDate + " " + CPartyCode ); //+ " " + EWarrantNo + " " + EWarrantDate + " " + EPartyCode + " "
                }
                rsLegacy.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void ImportAmount(String wrtFile) {
        int Pointer = 0;
        long Counter = 0;
        Connection objConn = null;
        Statement stLegacy = null;
        ResultSet rsLegacy = null;
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stLegacy=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            BufferedReader aFile=new BufferedReader(new FileReader(new File(wrtFile)));
            rsLegacy=stLegacy.executeQuery("SELECT * FROM D_FD_WARRANT_LEGACY LIMIT 1");
            
            // Clear all past entry of legacy entry.
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int delete = stTmp.executeUpdate("DELETE FROM D_FD_WARRANT_LEGACY ");
            System.out.println("All Rows are deleted from legacy = " + delete);
            stTmp.close();
            // Table Cleared...
            
            while(true) {
                String FileRecord=aFile.readLine();
                rsLegacy.moveToInsertRow();
                Pointer=0;
                Counter++;
                rsLegacy.updateLong("COUNTER", Counter);
                rsLegacy.updateString("DEPOSIT_TYPE", FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                String ReceiptNo = "M"+FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("RECEIPT_NO",ReceiptNo); Pointer+=6;
                rsLegacy.updateString("DEPOSIT_YEAR", FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                rsLegacy.updateString("PARTY_CODE", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                String mainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(mainAccountCode.equals("")) {
                    mainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE LEGACY_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                }
                rsLegacy.updateString("MAIN_ACCOUNT_CODE", mainAccountCode);
                String intMainCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(intMainCode.equals("")) {
                    intMainCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE LEGACY_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                }
                rsLegacy.updateString("INTEREST_MAIN_CODE", intMainCode);
                rsLegacy.updateString("WARRANT_NO", Integer.toString(Integer.parseInt(FileRecord.substring(Pointer,Pointer+6)))); Pointer+=6;
                rsLegacy.updateString("CHALLAN_NO", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("CHALLAN_DATE", "0000-00-00"); Pointer+=6;
                rsLegacy.updateInt("MICR_TDS_NO", Integer.parseInt(FileRecord.substring(Pointer,Pointer+6))); Pointer+=6;
                double grossInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+6)+"."+FileRecord.substring(Pointer+6,Pointer+8));
                rsLegacy.updateDouble("GROSS_INTEREST", grossInterest); Pointer+=8;
                double Tax = Double.parseDouble(FileRecord.substring(Pointer,Pointer+5)+"."+FileRecord.substring(Pointer+5,Pointer+7));
                rsLegacy.updateDouble("TAX", Tax); Pointer+=7;
                double netInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+6)+"."+FileRecord.substring(Pointer+6,Pointer+8));
                rsLegacy.updateDouble("NET_INTEREST", netInterest); Pointer+=9;
                String warrantDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("WARRANT_DATE",warrantDate);
                Pointer+=3;
                rsLegacy.insertRow();
            }
        }catch(Exception e) {
        }
        System.out.println("The End...");
    }
    
    private void updateData() {
        Connection objConn=null;
        Statement stLegacy=null;
        ResultSet rsLegacy = null;
        
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
                rsLegacy.updateLong("COUNTER", Counter);
                rsLegacy.updateString("DEPOSIT_TYPE", FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                String ReceiptNo = "M"+FileRecord.substring(Pointer,Pointer+6);
                rsLegacy.updateString("RECEIPT_NO",ReceiptNo); Pointer+=6;
                rsLegacy.updateString("DEPOSIT_YEAR", FileRecord.substring(Pointer,Pointer+4)); Pointer+=4;
                rsLegacy.updateString("PARTY_CODE", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                String mainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(mainAccountCode.equals("")) {
                    mainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE LEGACY_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                }
                rsLegacy.updateString("MAIN_ACCOUNT_CODE", mainAccountCode);
                String intMainCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(intMainCode.equals("")) {
                    intMainCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE LEGACY_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                }
                rsLegacy.updateString("INTEREST_MAIN_CODE", intMainCode);
                rsLegacy.updateString("WARRANT_NO", Integer.toString(Integer.parseInt(FileRecord.substring(Pointer,Pointer+6)))); Pointer+=6;
                rsLegacy.updateString("CHALLAN_NO", FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                rsLegacy.updateString("CHALLAN_DATE", "0000-00-00"); Pointer+=6;
                rsLegacy.updateInt("MICR_TDS_NO", Integer.parseInt(FileRecord.substring(Pointer,Pointer+6))); Pointer+=6;
                double grossInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+6)+"."+FileRecord.substring(Pointer+6,Pointer+8));
                rsLegacy.updateDouble("GROSS_INTEREST", grossInterest); Pointer+=8;
                double Tax = Double.parseDouble(FileRecord.substring(Pointer,Pointer+5)+"."+FileRecord.substring(Pointer+5,Pointer+7));
                rsLegacy.updateDouble("TAX", Tax); Pointer+=7;
                double netInterest = Double.parseDouble(FileRecord.substring(Pointer,Pointer+6)+"."+FileRecord.substring(Pointer+6,Pointer+8));
                rsLegacy.updateDouble("NET_INTEREST", netInterest); Pointer+=9;
                String warrantDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                rsLegacy.updateString("WARRANT_DATE",warrantDate);
                Pointer+=3;
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
        ResultSet rsWarrantH=null;
        ResultSet rsWarrantD=null;
        
        try {
            objConn=data.getConn(FinanceGlobal.FinURL);
            stWarrantH=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stWarrantD=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            String StartDate = "2005-01-01";
            String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
            String LastDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
            
            // Clear all past entry of legacy entry.
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int deleteH = stTmp.executeUpdate("DELETE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO LIKE 'M%'");
            int deleteD = stTmp.executeUpdate("DELETE FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO LIKE 'M%'");
            System.out.println("All Rows are deleted from header table = " + deleteH);
            System.out.println("All Rows are deleted from detail table = " + deleteD);
            stTmp.close();
            // Table Cleared...
            Connection objConn1=data.getConn();
            Statement stdtmp = objConn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int deleteDoc = stdtmp.executeUpdate("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO LIKE 'M%' AND MODULE_ID="+clsCalcInterest.ModuleID);
            System.out.println("All Rows are deleted from DOC DATA = " + deleteDoc);
            stdtmp.close();
            objConn1.close();
            
            rsWarrantH=stWarrantH.executeQuery("SELECT * FROM D_FD_INT_CALC_HEADER");
            rsWarrantH.first();
            
            rsWarrantD=stWarrantD.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL");
            rsWarrantD.first();
            System.out.println("Importing Records To Warrant Table...... ");
            int SrNo = 0;
            
            while(java.sql.Date.valueOf(StartDate).before(java.sql.Date.valueOf(CurrentDate))) {
                ResultSet rsLegacy = data.getResult("SELECT * FROM D_FD_WARRANT_LEGACY WHERE WARRANT_DATE>='"+StartDate+"' AND WARRANT_DATE<='"+LastDate+"' ",FinanceGlobal.FinURL);
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
                    rsWarrantH.updateString("BOOK_CODE","");
                    rsWarrantH.updateBoolean("APPROVED",false);
                    rsWarrantH.updateString("APPROVED_DATE","0000-00-00");
                    rsWarrantH.updateBoolean("REJECTED", false);
                    rsWarrantH.updateString("REJECTED_DATE", "0000-00-00");
                    rsWarrantH.updateString("REJECTED_REMARKS", "");
                    rsWarrantH.updateString("CREATED_BY", "Admin");
                    rsWarrantH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantH.updateString("MODIFIED_BY", "Admin");
                    rsWarrantH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantH.updateInt("HIERARCHY_ID", 1063);
                    rsWarrantH.updateBoolean("CHANGED", true);
                    rsWarrantH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsWarrantH.updateBoolean("CANCELLED", false);
                    rsWarrantH.insertRow();
                    
                    while(!rsLegacy.isAfterLast()) {
                        rsWarrantD.moveToInsertRow();
                        rsWarrantD.updateLong("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsWarrantD.updateString("DOC_NO", DocNo);
                        ++SrNo;
                        int totalDays=0;
                        String NextDate = "";
                        rsWarrantD.updateInt("SR_NO", SrNo);
                        String ReceiptNo = rsLegacy.getString("RECEIPT_NO");
                        if(data.IsRecordExist("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL)) {
                            String DepositType = rsLegacy.getString("DEPOSIT_TYPE");
                            if(DepositType.equals("FD") || DepositType.equals("LD")) {
                                String SQLQuery = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
                                String eDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                                if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)){
                                    //change Receipt Date for Date Difference if record already exits
                                    SQLQuery = "SELECT INTEREST_DAYS FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='" + ReceiptNo +"' ";
                                    ResultSet rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                                    rsResult.first();
                                    while(!rsResult.isAfterLast()) {
                                        eDate = EITLERPGLOBAL.addDaysToDate(eDate, rsResult.getInt("INTEREST_DAYS"),"yyyy-MM-dd");
                                        rsResult.next();
                                    }
                                    rsResult.close();
                                    NextDate = getInterestDate(1,eDate,ReceiptNo);
                                    totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(eDate), java.sql.Date.valueOf(NextDate)) +1;
                                } else {
                                    NextDate = getInterestDate(1,eDate,ReceiptNo);
                                    totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(eDate), java.sql.Date.valueOf(NextDate)) +1;
                                }
                            }
                        }
                        rsWarrantD.updateString("RECEIPT_NO",ReceiptNo);
                        double interestRate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                        rsWarrantD.updateInt("INTEREST_DAYS",totalDays);
                        rsWarrantD.updateDouble("INTEREST_RATE",interestRate);
                        rsWarrantD.updateString("PARTY_CODE",rsLegacy.getString("PARTY_CODE"));
                        rsWarrantD.updateString("MAIN_ACCOUNT_CODE", rsLegacy.getString("MAIN_ACCOUNT_CODE"));
                        rsWarrantD.updateString("INT_MAIN_ACCOUNT_CODE", rsLegacy.getString("INTEREST_MAIN_CODE"));
                        
                        String WarrantNo = rsLegacy.getString("WARRANT_NO");
                        String WarrantDate = rsLegacy.getString("WARRANT_DATE");
                        rsWarrantD.updateString("WARRANT_NO", getNextWarrantNo(WarrantNo,WarrantDate));
                        
                        rsWarrantD.updateInt("MICR_NO", rsLegacy.getInt("MICR_TDS_NO"));
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
                        //if(StartDate.equals("2007-03-01")) {
                        //System.out.println("");
                        //System.out.println("Doc No : " + DocNo + " Receipt No : " + ReceiptNo + " Sr No : " + SrNo);
                        //}
                        rsLegacy.next();
                    }
                    
                    //======== Update the Approval Flow =========
                    ApprovalFlow ObjFlow = new ApprovalFlow();
                    ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
                    ObjFlow.ModuleID=clsCalcInterest.ModuleID;
                    ObjFlow.DocNo=DocNo;
                    ObjFlow.From = 1;
                    ObjFlow.To = 1;
                    ObjFlow.Status="F";
                    ObjFlow.TableName="D_FD_INT_CALC_HEADER";
                    ObjFlow.IsCreator=true;
                    ObjFlow.HierarchyID=1063;
                    ObjFlow.Remarks="";
                    ObjFlow.FieldName="DOC_NO";
                    ObjFlow.UseSpecifiedURL=true;
                    ObjFlow.SpecificURL=FinanceGlobal.FinURL;
                    
                    if(ObjFlow.Status.equals("H")) {
                        ObjFlow.Status="A";
                        ObjFlow.To=ObjFlow.From;
                        ObjFlow.UpdateFlow();
                    }
                    else {
                        if(!ObjFlow.UpdateFlow()) {
                            JOptionPane.showMessageDialog(null,"Error in Legacy No.:" + DocNo +"\n Error is : " + ObjFlow.LastError);
                        }
                    }
                    ObjFlow.UseSpecifiedURL=false;
                    //--------- Approval Flow Update complete -----------//
                }
                SubRecord += SrNo;
                System.out.println("Record No. = " + Counter + " on Start Date = " + StartDate + " and Last Date = "+ LastDate +" with SubRecord = "+SrNo+" has been posted.");
                ++Counter;
                StartDate = clsCalcInterest.addMonthToDate(StartDate, 1);
                LastDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        --Counter;
        System.out.println("\nFinished posting to warrant table. " + Counter + " records with subrecord = "+ SubRecord + " has been posted to warrant.\n");
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