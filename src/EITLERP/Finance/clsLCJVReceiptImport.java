/*
 * clsLCJVReceiptImport.java
 *
 * Created on July 9, 2009, 11:50 AM
 */

package EITLERP.Finance;

import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class clsLCJVReceiptImport {
    
    /** Creates a new instance of clsLCJVReceiptImport */
    public clsLCJVReceiptImport() {
        System.gc();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        clsLCJVReceiptImport objImport=new clsLCJVReceiptImport();
        
        objImport.ImportLCJVReceipt("/root/Desktop");
        System.out.println("THE END");
    }
    
    public boolean ImportLCJVReceipt(String InitialDir) {
        try {
            
            //String dbURL="jdbc:mysql://localhost:3306/FINANCE";
            String dbURL="jdbc:mysql://200.0.0.227:3306/FINANCE";
            data.OpenGlobalConnection(dbURL);
            Connection objConn=data.getConn();
            
            String FileName="LCJV";
            BufferedReader objBR=  new BufferedReader(new FileReader(InitialDir+"/"+FileName+".csv"));
            
            //int Counter =1;
            System.out.println("Importing Records Start ...... ");
            String Line;
            String[] LineCol;
            int cnt = 0;
            
            //price list detail table
            while(true) {
                Line= objBR.readLine();
                LineCol=Line.split("~");
                String LCNo = LineCol[0].toString().trim();
                String OldRCNo = LineCol[4].toString().trim();
                String NewRCNo = LineCol[6].toString().trim();
                String Qry = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID=2 AND GRN_NO='"+OldRCNo+"' AND MODULE_ID="+clsVoucher.ReceiptVoucherModuleID;
                cnt++;
                if (data.IsRecordExist(Qry,dbURL)) {
                    System.out.println("count="+cnt+" Qry = "+ Qry);
                    String str = "UPDATE D_FIN_VOUCHER_DETAIL SET GRN_NO='"+NewRCNo+"' WHERE COMPANY_ID=2 AND GRN_NO='"+OldRCNo+"' AND MODULE_ID="+clsVoucher.ReceiptVoucherModuleID;
                    data.Execute(str,dbURL);
                    str = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET GRN_NO='"+NewRCNo+"' WHERE COMPANY_ID=2 AND GRN_NO='"+OldRCNo+"' AND MODULE_ID="+clsVoucher.ReceiptVoucherModuleID;
                    data.Execute(str,dbURL);
                }
                else {
                    System.out.println("lcno="+LCNo);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
