/*
 * test3.java
 *
 * Created on March 12, 2012, 4:43 PM
 */

package EITLERP.Finance;

import java.sql.*;
import EITLERP.data;

/**
 *
 * @author  mathaker
 */
public class test3 {
    
    /** Creates a new instance of test3 */
    public test3() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new test3().getMismatchVoucher();
    }
    
    private void getMismatchVoucher() {
        try {
            int InvoiceType = 2;
            String MainCode = "";
            if(InvoiceType==1) {
                MainCode = "210027";
            } else if(InvoiceType==2) {
                MainCode = "210010";
            }
            
            
            String SQL = "SELECT VOUCHER_NO,SUB_ACCOUNT_CODE,EFFECT,SUM(AMOUNT) AS AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND ENTRY_NO=2 AND INVOICE_TYPE="+InvoiceType+" AND MATCHED=0 " +
            "GROUP BY VOUCHER_NO,SUB_ACCOUNT_CODE,EFFECT ORDER BY VOUCHER_DATE ";
            ResultSet rsData = data.getResult(SQL,FinanceGlobal.FinURL);
            rsData.first();
            while(!rsData.isAfterLast()) {
                String VoucherNo = rsData.getString("VOUCHER_NO");
                String PartyCode = rsData.getString("SUB_ACCOUNT_CODE");
                String Effect= rsData.getString("EFFECT");
                double Amount = rsData.getDouble("AMOUNT");
                
                SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND VOUCHER_NO='"+VoucherNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='"+Effect+"' AND (MATCHED=0 OR MATCHED IS NULL)";
                double VoucherAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                
                if(Amount!=VoucherAmount ) {
                    System.out.println(VoucherNo);
                }
                rsData.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** Finished ***");
    }
}
