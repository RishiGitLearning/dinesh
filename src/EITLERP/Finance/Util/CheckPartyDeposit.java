/*
 * CheckPartyDeposit.java
 *
 * Created on August 23, 2013, 12:46 PM
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
 * @author  root
 */
public class CheckPartyDeposit {
    
    /** Creates a new instance of CheckPartyDeposit */
    public CheckPartyDeposit() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            CheckPartyDeposit objImport=new CheckPartyDeposit();
            //objImport.ImportToLegacy("/root/Desktop/TXT.txt");
            objImport.ImportToLegacy("/root/Desktop/txt1.txt");
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
            
            
            
            
            System.out.println("Importing Records...... ");
            while(true) {
                //if(Counter == 5530) {
                    //System.out.println("Test...");
                //}
                String FileRecord=aFile.readLine();
                Pointer=0;
                String MainCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=7;
                String SubCode = FileRecord.substring(Pointer,Pointer+6);
                if(SubCode.equals("080696")) {
                    boolean halt=true;
                }
                if(!data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' ",FinanceGlobal.FinURL)) {
                    System.out.println("MainCode : " + MainCode + " SubCode : " + SubCode);
                }
                
                ++Counter;
            }
        } catch(Exception e) {
            //e.printStackTrace();
        }
        --Counter;
        System.out.println("Record No. = " + Counter + " has been posted.");
        System.out.println("FINISHED POSTING TO LEGACY TABLE...");
    }
    
}
