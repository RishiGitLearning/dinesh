/*
 * Test.java
 *
 * Created on February 13, 2009, 5:43 PM
 */

package EITLERP.Finance;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import java.util.*;
import java.io.*;
import EITLERP.Sales.clsSalesInvoice;
/**
 *
 * @author  nitin
 */
public class Test1 {
    
    /** Creates a new instance of Test */
    public Test1() {
        
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //new Test1().FindPartyLedger();
        //new Test1().updateVoucherDate();
        //new Test1().updateLegacyNo();
        //new Test1().duplicateJV();
        //new Test1().updatevoucher();
        //new Test1().FindSupplier();
        new Test1().checkDeposit();
    }
     
    private void checkDeposit() {
        try {
            String SQL = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_DATE < '2012-10-01' AND DEPOSIT_STATUS=0 AND MATURITY_DATE<'2012-10-01' AND MAIN_ACCOUNT_CODE='115012'";
            ResultSet rsData = data.getResult(SQL,FinanceGlobal.FinURL);
            rsData.first();
            
            while(!rsData.isAfterLast()){
                String recNo = rsData.getString("RECEIPT_NO");
                SQL = "SELECT * FROM D_FD_DEPOSIT_MASTER WHERE OLD_RECEIPT_NO='"+recNo+"' AND APPROVED=1 AND CANCELLED=0";
                if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    System.out.println("renew recNo : "+ recNo);
                }
                rsData.next();
            }
            
            rsData.first();
            while(!rsData.isAfterLast()){
                String recNo = rsData.getString("RECEIPT_NO");
                SQL = "SELECT * FROM D_FD_DEPOSIT_REFUND WHERE RECEIPT_NO='"+recNo+"' AND APPROVED=1 AND CANCELLED=0";
                if(data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                    System.out.println("refund recNo : "+ recNo);
                }
                rsData.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            
        }
        System.out.println("*** Finished ***");
        
    }
    
    private void updatevoucher() {
        try {
            String SQL = "SELECT DISTINCT VOUCHER_NO,SUB_ACCOUNT_CODE,AMOUNT,GRN_NO,GRN_DATE FROM D_FIN_VOUCHER_DETAIL " +
            "WHERE VOUCHER_NO IN('PY105800459') " +
            "AND MATCHED=0 AND MAIN_ACCOUNT_CODE='210027' " +
            "ORDER BY VOUCHER_NO,SR_NO";
            ResultSet rsData = data.getResult(SQL,FinanceGlobal.FinURL);
            rsData.first();
            int counter0=0;
            int counter1=0;
            int counterM=0;
            while(!rsData.isAfterLast()){
                String VoucherNo = rsData.getString("VOUCHER_NO");
                String PartyCode = rsData.getString("SUB_ACCOUNT_CODE");
                double Amount = rsData.getDouble("AMOUNT");
                String GNo = rsData.getString("GRN_NO");
                String GDate = rsData.getString("GRN_DATE");
                SQL = "SELECT COUNT(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO='' AND GRN_NO='' AND EFFECT='C' AND MATCHED=0 AND VOUCHER_NO='"+ GNo +"' AND AMOUNT="+Amount+" AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                "AND MAIN_ACCOUNT_CODE='210027' ";
                int Count = data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                if(Count==1) {
                    counter1++;
                    SQL = "SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO='' AND GRN_NO='' AND EFFECT='C' AND MATCHED=0 AND VOUCHER_NO='"+ GNo +"' AND AMOUNT="+Amount+" AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                    "AND MAIN_ACCOUNT_CODE='210027' ";
                    int srno = data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                    SQL = "SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ";
                    String VDate = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                    SQL = "UPDATE D_FIN_VOUCHER_DETAIL SET GRN_NO='"+VoucherNo+"', GRN_DATE='"+VDate+"' WHERE SR_NO="+srno+" AND VOUCHER_NO='"+GNo+"'";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                    
                    SQL = "SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL_EX WHERE INVOICE_NO='' AND GRN_NO='' AND EFFECT='C' AND MATCHED=0 AND VOUCHER_NO='"+ GNo +"' AND AMOUNT="+Amount+" AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                    "AND MAIN_ACCOUNT_CODE='210027' ";
                    srno = data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                    SQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET GRN_NO='"+VoucherNo+"', GRN_DATE='"+VDate+"' WHERE SR_NO="+srno+" AND VOUCHER_NO='"+GNo+"'";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                    System.out.println("Single : VoucherNo : " + VoucherNo + " GNo : " + GNo);
                } else if(Count>1) {
                    counterM++;
                    System.out.println("Multiple : VoucherNo : " + VoucherNo + " GNo : " + GNo);
                } else if(Count==0) {
                    counter0++;
                    System.out.println("Zero : VoucherNo : " + VoucherNo + " GNo : " + GNo);
                }
                rsData.next();
            }
            System.out.println("Zero "+ counter0+ " counter1 : " + counter1 + " counterM : " + counterM);
        } catch(Exception e) {
            e.printStackTrace();
            
        }
        System.out.println("*** Finished ***");
    }
    
    private void updateLegacyNo() {
        try {
            ResultSet localSQL = data.getResult("SELECT LEGACY_NO,VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE BETWEEN '2011-04-01' AND '2012-03-31' AND BOOK_CODE IN(31) AND VOUCHER_NO LIKE 'PY%' AND APPROVED=1 AND CANCELLED=0 ORDER BY VOUCHER_NO", "jdbc:mysql://200.0.0.227:3306/FIN1");
            localSQL.first();
            int unmatchCounter = 0;
            int matchCounter = 0;
            while(!localSQL.isAfterLast()) {
                String VoucherNo = localSQL.getString("VOUCHER_NO") ;
                String localLegacyNo = localSQL.getString("LEGACY_NO");
                String liveLNo = data.getStringValueFromDB("SELECT LEGACY_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                if(!localLegacyNo.equals(liveLNo)){
                    unmatchCounter++;
                    System.out.println("Unmatch Counter : " + unmatchCounter + " Local Voucher : " + localLegacyNo +" Live Legacy No : " + liveLNo);
                    String SQL = "UPDATE D_FIN_VOUCHER_HEADER SET LEGACY_NO='"+localLegacyNo+"',CHANGED=1 WHERE VOUCHER_NO='"+VoucherNo+"'";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                }/*else if(java.sql.Date.valueOf(lVoucherDate).compareTo(java.sql.Date.valueOf(liveVDATE))==0){
                    matchCounter++;
                    System.out.println("Match Counter : " + matchCounter + " Local Voucher : " + lVoucherNo + " Local Voucher Date: " + lVoucherDate + " Live Voucher Date : " + liveVDATE);
                }*/
                localSQL.next();
            }
            
    /*private void updateVoucherDate() {
        try {
            ResultSet localSQL = data.getResult("SELECT VOUCHER_NO,VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE BETWEEN '2011-04-01' AND '2012-03-31' AND BOOK_CODE IN(33) AND VOUCHER_NO LIKE 'PY%' AND APPROVED=1 AND CANCELLED=0 ORDER BY VOUCHER_NO", "jdbc:mysql://200.0.0.227:3306/FIN1");
            localSQL.first();
            int unmatchCounter = 0;
            int matchCounter = 0;
            while(!localSQL.isAfterLast()) {
                String lVoucherNo = localSQL.getString("VOUCHER_NO") ;
                String lVoucherDate = localSQL.getString("VOUCHER_DATE");
                String liveVDATE = data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+lVoucherNo+"'",FinanceGlobal.FinURL);
                if(java.sql.Date.valueOf(lVoucherDate).compareTo(java.sql.Date.valueOf(liveVDATE))!=0){
                    unmatchCounter++;
                    System.out.println("Unmatch Counter : " + unmatchCounter + " Local Voucher : " + lVoucherNo + " Local Voucher Date: " + lVoucherDate + " Live Voucher Date : " + liveVDATE);
                    String SQL = "UPDATE D_FIN_VOUCHER_HEADER SET VOUCHER_DATE='"+lVoucherDate+"',CHANGED=1 WHERE VOUCHER_NO='"+lVoucherNo+"'";
                    data.Execute(SQL,FinanceGlobal.FinURL);
                }else if(java.sql.Date.valueOf(lVoucherDate).compareTo(java.sql.Date.valueOf(liveVDATE))==0){
                    matchCounter++;
                    System.out.println("Match Counter : " + matchCounter + " Local Voucher : " + lVoucherNo + " Local Voucher Date: " + lVoucherDate + " Live Voucher Date : " + liveVDATE);
                }
                localSQL.next();
            }*/
            
            System.out.println("*** Finished ***");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void duplicateJV() {
        try {
            //String local= ("SELECT LEGACY_NO,VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_DATE BETWEEN '2011-04-01' AND '2012-03-31' AND BOOK_CODE IN(31) AND VOUCHER_NO LIKE 'PY%' AND APPROVED=1 AND CANCELLED=0 ORDER BY VOUCHER_NO");
            String local="SELECT B.AMOUNT,A.VOUCHER_NO,B.SUB_ACCOUNT_CODE,A.REMARKS,B.GRN_NO,ROUND(SUM(AMOUNT)/2,2) AMT " +
            "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_DATE='2012-04-01' " +
            "AND A.BOOK_CODE=22 AND  B.SUB_ACCOUNT_CODE<>'' " +
            "AND A.REMARKS LIKE '%INTEREST PROVISION REVERSE ENTRY ON 01/04/2012%' AND A.VOUCHER_NO LIKE 'JV1222%' "+
            "GROUP BY SUB_ACCOUNT_CODE,A.REMARKS,B.GRN_NO HAVING COUNT(A.VOUCHER_NO)>1";
            
            ResultSet localSQL=data.getResult(local,FinanceGlobal.FinURL);
            localSQL.first();
            while(!localSQL.isAfterLast()) {
                System.out.println("Party Code: "+ localSQL.getString("SUB_ACCOUNT_CODE"));
                String rs ="SELECT B.* FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                "AND B.MAIN_ACCOUNT_CODE=115160 AND B.SUB_ACCOUNT_CODE='" + localSQL.getString("SUB_ACCOUNT_CODE")+"' AND A.VOUCHER_DATE ='2012-04-01' AND A.BOOK_CODE=22 " +
                "AND A.REMARKS LIKE '%INTEREST PROVISION REVERSE ENTRY ON 01/04/2012%' AND B.GRN_NO='" + localSQL.getString("GRN_NO") + "'";
                ResultSet rs1=data.getResult(rs,FinanceGlobal.FinURL);
                rs1.first();
                int Count=0;
                while(!rs1.isAfterLast()){
                    
                    if(Count==0) {
                        System.out.print("Original:" + rs1.getString("VOUCHER_NO") + " ");
                        
                    } else {
                        System.out.println("Deleted:" + rs1.getString("VOUCHER_NO"));
                        String SQL = "DELETE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        //data.Execute(SQL,FinanceGlobal.FinURL);
                        SQL = "DELETE FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        //data.Execute(SQL,FinanceGlobal.FinURL);
                        SQL = "DELETE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        //data.Execute(SQL,FinanceGlobal.FinURL);
                        SQL = "DELETE FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        //data.Execute(SQL,FinanceGlobal.FinURL);
                        SQL = "DELETE FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        //data.Execute(SQL,FinanceGlobal.FinURL);
                        //SQL = "DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='"+ rs1.getString("VOUCHER_NO") +"'";
                        data.Execute(SQL,FinanceGlobal.FinURL);
                    }
                    Count++;
                    rs1.next();
                }
                
                localSQL.next();
                
            }
            System.out.println("*** Finished ***");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void FindSupplier(){
        try{
        String strSQL="SELECT * FROM D_COM_SUPP_MASTER ORDER BY SUPPLIER_CODE LIMIT 71";
        ResultSet rsSupp=data.getResult(strSQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
        
        rsSupp.first();
        int Count=1;
        
        while(!rsSupp.isAfterLast()){
            String suppid = rsSupp.getString("SUPP_ID");
            System.out.println(Count);
            System.out.println("SUPPLIER ID:" + suppid);
            
            
                         String SQL = "SELECT * FROM D_COM_SUPP_TERMS WHERE SUPP_ID='"+ suppid +"'";
                         ResultSet rs1=data.getResult(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         if(rs1.getRow() > 0){
                            System.out.println("Supplier found D_COM_SUPP_TERMS:" + rs1.getString("SUPP_ID")); 
                            SQL="DELETE FROM D_COM_SUPP_TERMS WHERE SUPP_ID='"+ rs1.getString("SUPP_ID") +"'";
                            data.Execute(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         }
                         
                       
                         SQL="SELECT * FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID='"+ suppid +"'";
                         rs1=data.getResult(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         if(rs1.getRow() > 0){
                            System.out.println("Supplier Found D_COM_SUPP_ITEMCATEGORY:" + rs1.getString("SUPP_ID")) ;
                            SQL="DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID='"+ rs1.getString("SUPP_ID") +"'";
                            data.Execute(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         }
                     
                         SQL = "SELECT * FROM D_COM_SUPP_CHILDS WHERE SUPP_ID='" + suppid + "'";
                         rs1=data.getResult(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         if(rs1.getRow() > 0){
                            System.out.println("Supplier Found D_COM_SUPP_CHILDS :" + rs1.getString("SUPP_ID"));
                            SQL="DELETE FROM D_COM_SUPP_CHILDS WHERE SUPP_ID='"+ rs1.getString("SUPP_ID") +"'";
                            data.Execute(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         }
                       
                         SQL="SELECT * FROM D_COM_DOC_DATA WHERE DOC_NO='" + suppid + "'";
                         rs1=data.getResult(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         if(rs1.getRow() > 0){
                            System.out.println("Supplier Found D_COM_DOC_DATA:" + rs1.getString("DOC_NO")) ;
                            SQL="DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='"+ rs1.getString("DOC_NO") +"'";
                            data.Execute(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                         }
                         
                          SQL="DELETE FROM D_COM_SUPP_MASTER WHERE SUPP_ID='"+ rsSupp.getString("SUPP_ID") +"'";
                          data.Execute(SQL,"jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
                          
                        Count++;
                        
                        rsSupp.next();
                       
             }  
        
        }    
    
        catch(Exception e){
                e.printStackTrace();
        }
    
}
    private void FindPartyLedger() {
        try {
            
            String SQL = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_GL WHERE IS_SUBSIDAIRY=1 AND APPROVED=1 AND CANCELLED=0 ORDER BY MAIN_ACCOUNT_CODE";
            ResultSet rsGL = data.getResult(SQL,FinanceGlobal.FinURL);
            ResultSet rsMainCode = null;
            rsGL.first();
            while(!rsGL.isAfterLast()) {
                String MainCode = rsGL.getString("MAIN_ACCOUNT_CODE");
                //System.out.println("MainCode : " + MainCode);
                SQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<CURDATE() ORDER BY ENTRY_DATE DESC";
                int EntryNo=data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                SQL="SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo;
                String OpeningDate=data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                SQL="(SELECT DISTINCT MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE FROM D_FIN_OPENING_DETAIL " +
                "WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE<>'' AND ENTRY_NO="+EntryNo+") " +
                "UNION " +
                "(SELECT DISTINCT B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B " +
                "WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>'"+OpeningDate+"' AND A.VOUCHER_DATE<=CURDATE() " +
                "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 ) " +
                "ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE ";
                rsMainCode = data.getResult(SQL,FinanceGlobal.FinURL);
                rsMainCode.first();
                if(rsMainCode.getRow()>0) {
                    while(!rsMainCode.isAfterLast()) {
                        String SubCode = rsMainCode.getString("SUB_ACCOUNT_CODE");
                        if(!data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL)) {
                            System.out.println("Party Code : " + SubCode + " Main Code : " + MainCode);
                        }
                        rsMainCode.next();
                    }
                }
                rsGL.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** Find PartyLedger : Finished ***");
    }
    
    private void findPartyID() {
        try {
            int Counter=0,PartyID=0,realPartyID=0;
            int need=2;
            String SQL = "SELECT PARTY_ID FROM D_FIN_PARTY_MASTER WHERE PARTY_ID>0 AND APPROVED=1 AND CANCELLED=0 ORDER BY PARTY_ID";
            ResultSet rsPartyID = data.getResult(SQL,FinanceGlobal.FinURL);
            rsPartyID.first();
            while(!rsPartyID.isAfterLast() && (Counter!=need)) {
                PartyID = rsPartyID.getInt("PARTY_ID");
                realPartyID++;
                if(PartyID!=realPartyID) {
                    System.out.println(realPartyID);
                    Counter++;
                    realPartyID++;
                }
                rsPartyID.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** findPartyID : Finished ***");
    }
    
}
