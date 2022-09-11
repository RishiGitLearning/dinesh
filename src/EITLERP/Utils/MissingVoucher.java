/*
 * MissingVoucher.java
 *
 * Created on March 25, 2013, 4:13 PM
 */

package EITLERP.Utils;

import java.sql.*;
import EITLERP.*;


/**
 *
 * @author  root
 */
public class MissingVoucher {
    
    /** Creates a new instance of MissingVoucher */
    public MissingVoucher() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //new MissingVoucher().findVoucher();
        new MissingVoucher().findAVoucher();
    }
    
    public void findVoucher() {
        try {
            
            Connection connF = data.getConn("jdbc:mysql://200.0.0.227:3306/FINANCE");
            Statement stmtF = connF.createStatement();
            
            
            boolean transfer = false;
            Connection connC = data.getConn("jdbc:mysql://200.0.1.101:3306/FINANCE");
            Statement stmt = connC.createStatement();
            //ResultSet rs = stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE>='2012-04-01' AND VOUCHER_DATE<='2012-04-30' AND BOOK_CODE<>'99' AND APPROVED=1 AND CANCELLED=0 ORDER BY VOUCHER_DATE,VOUCHER_NO"); //AND APPROVED=0 AND CANCELLED=0
            ResultSet rs = stmt.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO IN ('PY125801785','PY125801786','PY125801787','PY125801788'," +
            "'PY125801789','PY125801790','PY125801791','PY125801792','PY125801793','PY125801794','PY125801795','PY125801797','PY125801798','PY125801799'," +
            "'PY125801800','PY125801801','PY125801802','PY125801803','PY125801804','PY125801808','PY125801809','PY125801810') ORDER BY VOUCHER_DATE,VOUCHER_NO"); //AND APPROVED=0 AND CANCELLED=0
            String VoucherNo = "";
            String VoucherDate = "";
            rs.first();
            if(rs.getRow()>0) {
                while(!rs.isAfterLast()) {
                    VoucherNo = rs.getString("VOUCHER_NO");
                    VoucherDate = rs.getString("VOUCHER_DATE");
                    //System.out.println("VOUCHER NO : " + rs.getString("VOUCHER_NO"));
                    transfer = false;
                    //stmt1.execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+VoucherNo+"'");
                    
                    if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ", "jdbc:mysql://200.0.0.227:3306/FINANCE")) {
                        
                        int  header_H_c = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        int  header_H_223 = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        if(header_H_c>header_H_223) {
                            System.out.println("Voucher No: " + VoucherNo + " header_h not match.");
                            transfer=true;
                        }
                        
                        int  detail_c = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        int  detail_223 = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        if(detail_c>detail_223) {
                            System.out.println("Voucher No: " + VoucherNo + " detail not match.");
                            transfer=true;
                        }
                        
                        int  detail_H_c = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        int  detail_H_223 = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        if(detail_H_c>detail_H_223) {
                            System.out.println("Voucher No: " + VoucherNo + " detail_h not match.");
                            transfer=true;
                        }
                        
                        int  detail_ex_c = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        int  detail_ex_223 = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' ","jdbc:mysql://200.0.0.227:3306/FINANCE");
                        if(detail_ex_c>detail_ex_223) {
                            System.out.println("Voucher No: " + VoucherNo + " detail_ex not match.");
                            transfer=true;
                        }
                    } else {
                        System.out.println("Voucher No: " + VoucherNo + " HEADER MISSING....");
                        transfer=true;
                    }
                    
                    rs.next();
                    
                    /*if(transfer) {
                        stmtF.addBatch("DELETE FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' ");
                        stmtF.addBatch("DELETE FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='"+VoucherNo+"' ");
                        stmtF.addBatch("DELETE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' ");
                        stmtF.addBatch("DELETE FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' ");
                        stmtF.addBatch("DELETE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ");
                     
                        stmtF.addBatch("INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX " +
                        "SELECT * FROM CLONEFINANCE.D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"'");
                     
                        stmtF.addBatch("INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_H " +
                        "SELECT * FROM CLONEFINANCE.D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='"+VoucherNo+"'");
                     
                        stmtF.addBatch("INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL " +
                        "SELECT * FROM CLONEFINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"'");
                     
                        stmtF.addBatch("INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER_H " +
                        "SELECT * FROM CLONEFINANCE.D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"'");
                     
                        stmtF.addBatch("INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER " +
                        "SELECT * FROM CLONEFINANCE.D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'");
                     
                        int[] count = stmtF.executeBatch();
                    }*/
                    transfer = false;
                }
                
                
                
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** Finished ***");
    }
    
    public void findAVoucher() {
        try {
            
            Connection connF = data.getConn("jdbc:mysql://200.0.0.227:3306/FINANCE");
            Statement stmtF = connF.createStatement();
            ResultSet rs = stmtF.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE APPROVED=0 AND CANCELLED=0 " +
            "AND VOUCHER_DATE>='2013-03-01' AND VOUCHER_NO LIKE 'SJ%' AND VOUCHER_DATE<='2013-03-31'  "); //AND APPROVED=0 AND CANCELLED=0
            String VoucherNo = "";
            String VoucherDate = "";
            rs.first();
            if(rs.getRow()>0) {
                while(!rs.isAfterLast()) {
                    VoucherNo = rs.getString("VOUCHER_NO");
                    VoucherDate = rs.getString("VOUCHER_DATE");
                    
                    if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' AND APPROVAL_STATUS='F'", "jdbc:mysql://200.0.0.227:3306/FINANCE")) {
                        System.out.println("Voucher No: " + VoucherNo + " DOC MISSING....");
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=1, CHANGED=1 WHERE VOUCHER_NO='"+VoucherNo+"' ", "jdbc:mysql://200.0.0.227:3306/FINANCE");
                    } 
                    
                    rs.next();
                    
                   
                    
                }
                
                
                
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** Finished ***");
    }
  
}
