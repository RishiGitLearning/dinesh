/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltInvReport.NumWord;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author root
 */
public class clsCNVoucherAmountUpdation {
    
    public static void main(String[] args) {
        AmountInWords();
    }
    
    private static void AmountInWords()  {
        String SQL = " SELECT * FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_FIN_VOUCHER_NO LIKE 'CN2114%' AND CNH_TYPE LIKE 'FELTCASHADV' ";
//        System.out.println("SQL = " + SQL);
        try {
            Connection connection;
            Statement statement;
            ResultSet resultSet,rsHeader;
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            
            while (resultSet.next()) {
                 
                 try{ 
                     String cnhNo = resultSet.getString("CNH_NO");
                     String mainCode = resultSet.getString("CNH_MAIN_CODE");
                     String partyCode = resultSet.getString("CNH_SUB_ACCOUNT_CODE"); 
                     String voucherNo = resultSet.getString("CNH_FIN_VOUCHER_NO");
                     
                     NumWord num = new NumWord();                     
                     String rsInWord = num.convertNumToWord(Math.round(resultSet.getFloat("CNH_VOUCHER_AMOUNT")));
                     
                     data.Execute("UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_VOUCHER_AMOUNT_IN_WORDS='"+rsInWord+"' WHERE CNH_NO='"+cnhNo+"' AND CNH_MAIN_CODE='"+mainCode+"' AND CNH_SUB_ACCOUNT_CODE='"+partyCode+"' AND CNH_FIN_VOUCHER_NO='"+voucherNo+"' ");
                        
                 }catch(Exception e)
                 {
                     e.printStackTrace();
                 }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
