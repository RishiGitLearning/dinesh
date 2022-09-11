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
public class clsERPWarrantOrder {
    
    /** Creates a new instance of clsSOImport */
    public clsERPWarrantOrder() {
        System.gc();
    }
    
    public static void main(String args[]) {
        new clsERPWarrantOrder().changeOrder();
        System.out.println("The End");
    }
    
    private void changeOrder() {
        try {
            String WarrantNo = "0900001";
            String SQL = "SELECT DOC_NO,SR_NO,RECEIPT_NO, PARTY_CODE, LEGACY_WARRANT_NO, WARRANT_NO " +
            "FROM D_FD_INT_CALC_DETAIL WHERE WARRANT_DATE>='2009-04-01' AND WARRANT_DATE<='2010-03-31' " +
            "AND WARRANT_NO<>'0000000' ORDER BY LEGACY_WARRANT_NO";
            ResultSet rsWOrder = data.getResult(SQL,FinanceGlobal.FinURL);
            rsWOrder.first();
            
            while(!rsWOrder.isAfterLast()){
                //update
                System.out.println("Warrant No. = " + WarrantNo);
                WarrantNo = getNextWarrantNo(WarrantNo);
                rsWOrder.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String getNextWarrantNo(String previousWarrantNo) {
        String  nextWarrantNo="";
        String FinYear = "09";
        int nextNo = 0;
        try {
            nextNo = Integer.parseInt(previousWarrantNo.substring(2,7)) + 1;
            String strNextNo=Integer.toString(nextNo);
            strNextNo=EITLERPGLOBAL.Replicate("0", 5-strNextNo.length())+strNextNo;
            nextWarrantNo=FinYear+strNextNo;
        } catch (Exception e) {
        }
        return nextWarrantNo;
    }
}