/*
 * clsCreditorsMatch.java
 *
 * Created on June 26, 2010, 4:31 PM
 */

package EITLERP.Finance;

import EITLERP.*;
import java.sql.*;
/**
 *
 * @author  root
 */
public class clsCreditorsMatch {
    String FromDate = "";
    String ToDate = "";
    String strSQL = "";
    ResultSet rsPJV = null;
    ResultSet rsPJVDetail = null;
    ResultSet rsPaymnet = null;
    /** Creates a new instance of clsCreditorsMatch */
    public clsCreditorsMatch() {
        FromDate = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB());
        ToDate = EITLERPGLOBAL.getCurrentDateDB();
        FindMatch();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new clsCreditorsMatch();
    }
    
    private void FindMatch() {
        try {
            strSQL = "SELECT DISTINCT A.VOUCHER_NO,B.PO_NO, B.INVOICE_NO,B.SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND B.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='125019' " +
            "AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND (B.MATCHED=0 OR B.MATCHED IS NULL) AND B.EFFECT='C' " +
            "GROUP BY A.VOUCHER_NO,B.PO_NO,B.INVOICE_NO " +
            "ORDER BY A.VOUCHER_NO,B.PO_NO,B.INVOICE_NO ";
            
            rsPJV = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsPJV.first();
            
            while(!rsPJV.isAfterLast()) {
                String VoucherNo = UtilFunctions.getString(rsPJV, "VOUCHER_NO", "");
                String PartyCode = UtilFunctions.getString(rsPJV, "SUB_ACCOUNT_CODE", "");
                String PONo = UtilFunctions.getString(rsPJV, "PO_NO", "");
                String InvoiceNo = UtilFunctions.getString(rsPJV, "INVOICE_NO", "");
                
                strSQL = "SELECT A.COMPANY_ID,A.VOUCHER_NO,A.VOUCHER_DATE,B.AMOUNT AS AMOUNT,B.GRN_NO,B.GRN_DATE,B.INVOICE_NO,B.INVOICE_DATE, " +
                "B.MODULE_ID,B.PO_NO,B.PO_DATE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                "AND A.VOUCHER_NO = '"+VoucherNo+"' AND B.MAIN_ACCOUNT_CODE='125019' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.EFFECT='C' ";
                
                rsPJVDetail = data.getResult(strSQL,FinanceGlobal.FinURL);
                rsPJVDetail.first();
                double PJVAmount = UtilFunctions.getDouble(rsPJVDetail, "AMOUNT", 0);
                
                strSQL = "SELECT SUM(B.AMOUNT) AS AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                "AND B.MAIN_ACCOUNT_CODE='125019' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND B.PO_NO = '"+PONo+"' " +
                "AND B.INVOICE_NO ='"+InvoiceNo+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
                "GROUP BY A.VOUCHER_NO " +
                "ORDER BY A.VOUCHER_DATE ";
                
                double PaidAmount = data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                if(PJVAmount == PaidAmount) {
                    strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+VoucherNo+"' ";
                    data.Execute(strSQL, FinanceGlobal.FinURL);
                    strSQL = "UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1,MATCHED_DATE=CURDATE() " +
                    "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='125019' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                    "AND PO_NO='"+PONo+"' AND INVOICE_NO ='"+InvoiceNo+"' ";
                    data.Execute(strSQL, FinanceGlobal.FinURL);
                    strSQL = "UPDATE D_FIN_VOUCHER_DETAIL_H SET MATCHED=1,MATCHED_DATE=CURDATE() " +
                    "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='125019' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                    "AND PO_NO='"+PONo+"' AND INVOICE_NO ='"+InvoiceNo+"' ";
                    data.Execute(strSQL, FinanceGlobal.FinURL);
                    strSQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1,MATCHED_DATE=CURDATE() " +
                    "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='125019' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                    "AND PO_NO='"+PONo+"' AND INVOICE_NO ='"+InvoiceNo+"' ";
                    data.Execute(strSQL, FinanceGlobal.FinURL);
                }
                rsPJV.next();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
