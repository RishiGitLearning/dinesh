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
public class clsImportLegacyNoToDeposit {
    
    /** Creates a new instance of clsSOImport */
    public clsImportLegacyNoToDeposit() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            clsImportLegacyNoToDeposit objImport=new clsImportLegacyNoToDeposit();
            objImport.ImportToLegacy("/root/Desktop/DEPOSIT/MAY/MAY1.TXT");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("The End");
    }
    
    private void ImportToLegacy(String DepositFile) {
        int Pointer = 0;
        int CompanyID = 2;
        long FCounter = 0;
        long NFCounter = 0;
        Connection objConn=null;
        Statement stLegacy=null;
        ResultSet rsLegacy=null;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(DepositFile)));
            
            System.out.println("Importing Records...... ");
            while(true) {
                String FileRecord=aFile.readLine();
                
                Pointer=0;
                //FCounter++;
                String ReceiptType = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                String LegacyNo = FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                String ReceiptYear = FileRecord.substring(Pointer,Pointer+4); Pointer+=4;
                
                String IssueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                double Amount = Double.parseDouble(FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;
                String Period = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                
                double InterestRate = Double.parseDouble(FileRecord.substring(Pointer,Pointer+2)+"."+FileRecord.substring(Pointer+2,Pointer+4)); Pointer+=4;

                String ReceiptDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
//                String DueDate = "20"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+2,Pointer+4)+"-"+FileRecord.substring(Pointer,Pointer+2); Pointer+=6;
                String PartyCode = FileRecord.substring(Pointer,Pointer+6); Pointer+=6;

                String SQL = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_DATE='"+ReceiptDate+"' AND PARTY_CODE='"+PartyCode+"' AND AMOUNT="+Amount+" AND INTEREST_RATE="+InterestRate+" AND DEPOSIT_PERIOD="+Period+" AND APPROVED=1 AND CANCELLED=0";
                if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET LEGACY_NO='M"+LegacyNo+"' " +
                    "WHERE RECEIPT_DATE='"+ReceiptDate+"' AND PARTY_CODE='"+PartyCode+"' AND AMOUNT="+Amount+" AND INTEREST_RATE="+InterestRate+" AND DEPOSIT_PERIOD="+Period+"",FinanceGlobal.FinURL);
                    FCounter++;
                } else {
                    System.out.println("Receipt No. = " + LegacyNo);
                    ++NFCounter;
                }
                
            }
        } catch(Exception e) {
        }
        
        System.out.println("Record No. = " + FCounter + " has been posted.");
        System.out.println("Record No. = " + NFCounter + " has not been posted.");
        System.out.println("FINISHED POSTING LEGACY NO...");
    }
}