
/*
 * clsDebitorsMatchSD.java
 *
 * Created on July 07, 2016, 4:31 PM
 */

package EITLERP.Finance;

import EITLERP.*;
import java.sql.*;
/**
 *
 * @author  root
 */
public class clsDebitorsMatchSD {
    String FromDate = "";
    String ToDate = "";
    String strSQL = "";
    ResultSet rsSJ = null;
    ResultSet rsCredit = null;
    ResultSet rsReceipt = null;
    String MainAccountCode = "";
    int InvoiceType = 0;
    /** Creates a new instance of clsCreditorsMatch */
    public clsDebitorsMatchSD(String MainCode) {
        MainAccountCode = MainCode;
        
        if(MainAccountCode.equals("132803")) {
            InvoiceType = 2;
        } else if(MainAccountCode.equals("132802")) {
            InvoiceType = 2;
        } else{
            return;
        }
        
        //FromDate = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB());
        FromDate = data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<=CURDATE() ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
        ToDate = EITLERPGLOBAL.getCurrentDateDB();
        System.out.println("Outstanding process started...");
        System.out.println("From Date : " + EITLERPGLOBAL.formatDate(FromDate) + " To Date : " + EITLERPGLOBAL.formatDate(ToDate));
        System.out.println("First Invoice process started...");
        FindMatchInvoice();
        System.out.println("First Invoice process ended...");
        System.out.println("Second Match Debit process started...");
        FindMatchDebits();
        System.out.println("Second Match Debit process ended...");
        FindMatchDebitsFromClosing(); //MainCode, PartyCode, FromDate, ToDate
    }
    
    public clsDebitorsMatchSD() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // original start
        if(args.length<1) {
            System.out.println("Insuficient argument. \nInsert Main Account Code.");
            return;
        }
        
        new clsDebitorsMatchSD(args[0]);
        // original end
        
        // test start
        //new clsDebitorsMatchSD("210027");
        // test end
        System.out.println("Finished...");
    }
    
    private void FindMatchInvoice() {
        try {
            strSQL = "SELECT DISTINCT A.VOUCHER_NO,A.VOUCHER_DATE,B.INVOICE_NO, B.INVOICE_DATE,B.SUB_ACCOUNT_CODE,B.AMOUNT " +
            "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
            "AND A.VOUCHER_DATE>'"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' " +
            "AND (B.MATCHED=0 OR B.MATCHED IS NULL) AND B.EFFECT='D' " + //AND A.BOOK_CODE<>10
            //"AND A.VOUCHER_TYPE=" +FinanceGlobal.TYPE_SALES_JOURNAL + " " + //"AND B.SUB_ACCOUNT_CODE='832476' " +
            "AND (A.VOUCHER_TYPE=" +FinanceGlobal.TYPE_SALES_JOURNAL+" OR A.VOUCHER_TYPE="+FinanceGlobal.TYPE_JOURNAL+") " + 
            "GROUP BY A.VOUCHER_NO,B.INVOICE_NO,B.INVOICE_DATE " +
            "ORDER BY A.VOUCHER_NO,B.INVOICE_NO,B.INVOICE_DATE ";
            
            rsSJ = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsSJ.first();
            if(rsSJ.getRow() > 0) {
                while(!rsSJ.isAfterLast()) {
                    String VoucherNo = UtilFunctions.getString(rsSJ, "VOUCHER_NO", "");
                    String PartyCode = UtilFunctions.getString(rsSJ, "SUB_ACCOUNT_CODE", "");
                    String InvoiceNo = UtilFunctions.getString(rsSJ, "INVOICE_NO", "");
                    String InvoiceDate = UtilFunctions.getString(rsSJ, "INVOICE_DATE","0000-00-00");
                    double SJAmount = UtilFunctions.getDouble(rsSJ, "AMOUNT", 0);
                    /*if(InvoiceNo.equals("F001820")) {
                        boolean halt =true;
                    }
                     */
                    strSQL = "SELECT SUM(B.AMOUNT) AS AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                    "AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                    "AND B.INVOICE_DATE = '"+InvoiceDate+"' AND B.INVOICE_NO ='"+InvoiceNo+"' " +
                    "AND A.APPROVED=1 AND A.CANCELLED=0 " +
                    "ORDER BY A.VOUCHER_DATE ";
                    double PaidAmount = data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    System.out.println("Voucher No : " + VoucherNo +" PartyCode : " + PartyCode + " Invoice No : " + InvoiceNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate));
                    if(SJAmount == PaidAmount) {
                        System.out.println("Update Match Invoice...");
                        strSQL = "UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1,MATCHED_DATE=CURDATE() " +
                        "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' ";
                        data.Execute(strSQL, FinanceGlobal.FinURL);
                        
                        strSQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1,MATCHED_DATE=CURDATE() " +
                        "WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND INVOICE_DATE ='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' ";
                        data.Execute(strSQL, FinanceGlobal.FinURL);
                        
                        strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+VoucherNo+"' ";
                        data.Execute(strSQL, FinanceGlobal.FinURL);
                        
                        strSQL = "SELECT A.VOUCHER_NO,B.SR_NO,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                        "AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                        "AND B.INVOICE_DATE = '"+InvoiceDate+"' AND B.INVOICE_NO ='"+InvoiceNo+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
                        "ORDER BY A.VOUCHER_DATE ";
                        
                        rsReceipt = data.getResult(strSQL, FinanceGlobal.FinURL);
                        rsReceipt.first();
                        if(rsReceipt.getRow()>0) {
                            while(!rsReceipt.isAfterLast()) {
                                String ReceiptNo = rsReceipt.getString("VOUCHER_NO");
                                int vSrNo = rsReceipt.getInt("SR_NO");
                                double vAmount = rsReceipt.getDouble("AMOUNT");
                                
                                System.out.println("Update Match Credits... " + ReceiptNo);
                                
                                strSQL = "UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' " +
                                "AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                                "AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' AND AMOUNT="+vAmount; //AND SR_NO="+vSrNo+"
                                data.Execute(strSQL,FinanceGlobal.FinURL);
                                
                                strSQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' " +
                                "AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                                "AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' AND AMOUNT="+vAmount; //AND SR_NO="+vSrNo+"
                                data.Execute(strSQL,FinanceGlobal.FinURL);
                                
                                strSQL = "UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+ReceiptNo+"' ";
                                data.Execute(strSQL,FinanceGlobal.FinURL);
                                
                                int EntryNo = data.getIntValueFromDB("SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+EITLERPGLOBAL.getCurrentDateDB()+"' ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
                                if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount+" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT",FinanceGlobal.FinURL)) {
                                    System.out.println("Credit Found in ouststanding... " + ReceiptNo);
                                    int CountRecord = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount +" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                    int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount +" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                    double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                    if(OSAmount > vAmount) {
                                        ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                        Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                        ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                        
                                        int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                        
                                        rsOPOSData.moveToInsertRow();
                                        rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                        rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                        rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                        rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                        rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                        rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                        rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                        rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                        rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                        rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                        rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                        rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                        rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                        rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                        rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                        rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                        rsOPOSData.updateBoolean("MATCHED",false);
                                        rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                        rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                        rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                        rsOPOSData.updateBoolean("CHANGED",true);
                                        rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                        rsOPOSData.insertRow();
                                        
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        System.out.println("Credit Found in ouststanding split... " + ReceiptNo);
                                    } else {
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        System.out.println("Credit Found in ouststanding without split... " + ReceiptNo);
                                    }
                                } else if(data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT<="+vAmount+" AND INVOICE_TYPE="+InvoiceType,FinanceGlobal.FinURL)>=vAmount) {
                                    /*while(vAmount!= 0 ) {
                                        int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT<="+vAmount + " AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                        double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+OSAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                    }*/
                                    while(vAmount> 0 ) {
                                        int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL); //AND AMOUNT<="+vAmount + "
                                        double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        
                                        if(OSAmount > vAmount) {
                                            ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                            Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                            Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                            ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                            
                                            int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                            
                                            rsOPOSData.moveToInsertRow();
                                            rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                            rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                            rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                            rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                            rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                            rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                            rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                            rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                            rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                            rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                            rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                            rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                            rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                            rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                            rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                            rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                            rsOPOSData.updateBoolean("MATCHED",false);
                                            rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                            rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                            rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                            rsOPOSData.updateBoolean("CHANGED",true);
                                            rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                            rsOPOSData.insertRow();
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        } else {
                                            //data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+OSAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        }
                                    }
                                }
                                rsReceipt.next();
                            }
                        }
                    } else {
                        System.out.println("Match Credit not found...");
                    }
                    rsSJ.next();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void FindMatchDebits() {
        try {
            String strSQL="SELECT A.VOUCHER_NO,A.VOUCHER_DATE,B.SUB_ACCOUNT_CODE " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_DATE>'"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' " +
            "AND B.EFFECT='D' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
            "AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND A.VOUCHER_TYPE<>" +FinanceGlobal.TYPE_SALES_JOURNAL + " " +
            "AND (B.MATCHED=0 OR B.MATCHED IS NULL) " + //" AND A.VOUCHER_NO='PY125800456' " +
            "GROUP BY B.VOUCHER_NO,B.SUB_ACCOUNT_CODE " +
            "ORDER BY A.VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    String DebitNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String PartyCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                    
                    strSQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' " +
                    "AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ";
                    double DebitAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    System.out.println("Voucher No : " + DebitNo +" PartyCode : " + PartyCode );
                    
                    strSQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.GRN_NO='"+DebitNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                    "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 ";
                    double AdjustedAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    if(AdjustedAmount==DebitAmount) {
                        // UPDATE MATCH RECORD
                        System.out.println("Update Match Debit...");
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+DebitNo+"' ",FinanceGlobal.FinURL);
                        
                        strSQL = "SELECT A.VOUCHER_NO,B.SR_NO,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.GRN_NO='"+DebitNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 ";
                        rsCredit = data.getResult(strSQL,FinanceGlobal.FinURL);
                        rsCredit.first();
                        if(rsCredit.getRow() > 0) {
                            while(!rsCredit.isAfterLast()) {
                                String ReceiptNo = rsCredit.getString("VOUCHER_NO");
                                int vSrNo = rsCredit.getInt("SR_NO");
                                double vAmount = rsCredit.getDouble("AMOUNT");
                                
                                System.out.println("Update Match Credits... " + ReceiptNo);
                                
                                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND GRN_NO='"+DebitNo+"' ",FinanceGlobal.FinURL);
                                data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND GRN_NO='"+DebitNo+"' ",FinanceGlobal.FinURL);
                                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                                
                                int EntryNo = data.getIntValueFromDB("SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+EITLERPGLOBAL.getCurrentDateDB()+"' ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
                                if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount +" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT" ,FinanceGlobal.FinURL)) {
                                    System.out.println("Credit Found in ouststanding... " + ReceiptNo);
                                    int CountRecord = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount +" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT",FinanceGlobal.FinURL);
                                    int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount +" AND INVOICE_TYPE="+InvoiceType+" ORDER BY AMOUNT",FinanceGlobal.FinURL);
                                    double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                    if(OSAmount > vAmount) {
                                        ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                        Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                        ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                        
                                        int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                        
                                        rsOPOSData.moveToInsertRow();
                                        rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                        rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                        rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                        rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                        rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                        rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                        rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                        rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                        rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                        rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                        rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                        rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                        rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                        rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                        rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                        rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                        rsOPOSData.updateBoolean("MATCHED",false);
                                        rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                        rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                        rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                        rsOPOSData.updateBoolean("CHANGED",true);
                                        rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                        rsOPOSData.insertRow();
                                        
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        System.out.println("Credit Found in ouststanding split... " + ReceiptNo);
                                    } else {
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        System.out.println("Credit Found in ouststanding without split... " + ReceiptNo);
                                    }
                                } else if(data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT<="+vAmount+" AND INVOICE_TYPE="+InvoiceType,FinanceGlobal.FinURL)>=vAmount) {
                                    /*while(vAmount!= 0 ) {
                                        int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT<="+vAmount + " AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                        double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+OSAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                        vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                    }*/
                                    while(vAmount> 0 ) {
                                        int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL); //AND AMOUNT<="+vAmount + "
                                        double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        
                                        if(OSAmount > vAmount) {
                                            ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                            Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                            Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                            ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                            
                                            int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                            
                                            rsOPOSData.moveToInsertRow();
                                            rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                            rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                            rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                            rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                            rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                            rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                            rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                            rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                            rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                            rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                            rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                            rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                            rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                            rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                            rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                            rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                            rsOPOSData.updateBoolean("MATCHED",false);
                                            rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                            rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                            rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                            rsOPOSData.updateBoolean("CHANGED",true);
                                            rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                            rsOPOSData.insertRow();
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        } else {
                                            //data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+OSAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        }
                                    }
                                }
                                rsCredit.next();
                            }
                        }
                    } else {
                        System.out.println("Match Credit not found...");
                    }
                    
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
        }
    }
    
    private void FindMatchDebitsFromClosing() { //final String MainAccountCode,final String PartyCode,final String FromDate,final String ToDate
        try {
            String SQL="SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+ToDate+"' ORDER BY ENTRY_DATE DESC";
            int EntryNo=data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
            System.out.println("FindMatchDebitsFromClosing : Started ");
            strSQL="SELECT VOUCHER_NO,VOUCHER_DATE,SUB_ACCOUNT_CODE " +
            "FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE ENTRY_NO="+EntryNo+" AND INVOICE_TYPE=" +InvoiceType+" " +
            "AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " + //AND SUB_ACCOUNT_CODE='062000'
            "AND (MATCHED=0 OR MATCHED IS NULL) " +
            "GROUP BY VOUCHER_NO,SUB_ACCOUNT_CODE " +
            "ORDER BY VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    String DebitNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String PartyCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                    double DebitAmount = 0;
                    double AdjustedAmount = 0;
                    String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+DebitNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode.trim()+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='D' ",FinanceGlobal.FinURL);
                    String InvoiceDate = data.getStringValueFromDB("SELECT INVOICE_DATE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+DebitNo+"' AND MAIN_ACCOUNT_CODE='"+MainAccountCode.trim()+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='D' ",FinanceGlobal.FinURL);
                    /*if(DebitNo.equals("OP090331881000256")) {
                        boolean halt=true;
                    }
                     */
                    if(clsVoucher.getVoucherType(DebitNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                        strSQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' " +
                        "AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_NO='"+InvoiceNo+"' " +
                        "AND INVOICE_DATE='"+InvoiceDate+"'";
                        DebitAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        
                        strSQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                        "AND B.INVOICE_NO='"+InvoiceNo+"' AND B.INVOICE_DATE='"+InvoiceDate+"' " +
                        "AND A.APPROVED=1 AND A.CANCELLED=0 ";
                        AdjustedAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    } else {
                        strSQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' " +
                        "AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ";
                        DebitAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        
                        strSQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.GRN_NO='"+DebitNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 ";
                        AdjustedAmount = data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    }
                    
                    if(AdjustedAmount==DebitAmount) {
                        // UPDATE MATCH RECORD
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+DebitNo+"' AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+DebitNo+"' ",FinanceGlobal.FinURL);
                        
                        if(data.IsRecordExist("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE ENTRY_NO="+ EntryNo + " AND VOUCHER_NO='"+DebitNo+"' AND MATCHED=0 AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL)) { //AMOUNT="+DebitAmount+" AND
                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE ENTRY_NO="+ EntryNo + " AND VOUCHER_NO='"+DebitNo+"' AND MATCHED=0 AND EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL); //AMOUNT="+DebitAmount+" AND
                        }
                        
                        ResultSet rsCredit = null;
                        if(clsVoucher.getVoucherType(DebitNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                            strSQL = "SELECT A.VOUCHER_NO,B.SR_NO,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
                            "AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                            "AND B.INVOICE_DATE = '"+InvoiceDate+"' AND B.INVOICE_NO ='"+InvoiceNo+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
                            "ORDER BY A.VOUCHER_DATE ";
                            rsCredit = data.getResult(strSQL,FinanceGlobal.FinURL);
                        } else {
                            strSQL = "SELECT A.VOUCHER_NO,B.SR_NO,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.GRN_NO='"+DebitNo+"' AND B.MAIN_ACCOUNT_CODE='"+MainAccountCode+"' " +
                            "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 ";
                            rsCredit = data.getResult(strSQL,FinanceGlobal.FinURL);
                        }
                        rsCredit.first();
                        if(rsCredit.getRow() > 0 ) {
                            while(!rsCredit.isAfterLast()) {
                                String ReceiptNo = rsCredit.getString("VOUCHER_NO");
                                int vSrNo = rsCredit.getInt("SR_NO");
                                double vAmount = rsCredit.getDouble("AMOUNT");
                                /*if(ReceiptNo.equals("JV102101302")) {
                                    boolean halt=true;
                                }
                                 */
                                if(clsVoucher.getVoucherType(DebitNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                    strSQL = "UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' " +
                                    "AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                                    "AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' AND AMOUNT="+vAmount; //AND SR_NO="+vSrNo+"
                                    data.Execute(strSQL,FinanceGlobal.FinURL);
                                    
                                    strSQL = "UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' " +
                                    "AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                                    "AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_NO ='"+InvoiceNo+"' AND AMOUNT="+vAmount; //AND SR_NO="+vSrNo+"
                                    data.Execute(strSQL,FinanceGlobal.FinURL);
                                } else {
                                    data.Execute("UPDATE D_FIN_VOUCHER_DETAIL SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND GRN_NO='"+DebitNo+"' AND AMOUNT="+vAmount,FinanceGlobal.FinURL);
                                    data.Execute("UPDATE D_FIN_VOUCHER_DETAIL_EX SET MATCHED=1, MATCHED_DATE=CURDATE() WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND GRN_NO='"+DebitNo+"' AND AMOUNT="+vAmount,FinanceGlobal.FinURL);
                                }
                                
                                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET CHANGED=1 WHERE VOUCHER_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                                
                                if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount+" AND INVOICE_TYPE="+InvoiceType,FinanceGlobal.FinURL)) {
                                    int CountRecord = data.getIntValueFromDB("SELECT COUNT(*) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount + " AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                    int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT>="+vAmount + " AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL);
                                    double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                    if(OSAmount > vAmount) {
                                        ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                        Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                        ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                        
                                        int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                        
                                        rsOPOSData.moveToInsertRow();
                                        rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                        rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                        rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                        rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                        rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                        rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                        rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                        rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                        rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                        rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                        rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                        rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                        rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                        rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                        rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                        rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                        rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                        rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                        rsOPOSData.updateBoolean("MATCHED",false);
                                        rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                        rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                        rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                        rsOPOSData.updateBoolean("CHANGED",true);
                                        rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                        rsOPOSData.insertRow();
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                    } else {
                                        data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                    }
                                } else if(data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND AMOUNT<="+vAmount+" AND INVOICE_TYPE="+InvoiceType,FinanceGlobal.FinURL)>=vAmount) {
                                    while(vAmount> 0 ) {
                                        int SrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND INVOICE_TYPE="+InvoiceType +" ORDER BY AMOUNT ",FinanceGlobal.FinURL); //AND AMOUNT<="+vAmount + "
                                        double OSAmount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND INVOICE_TYPE="+InvoiceType +" AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                        
                                        if(OSAmount > vAmount) {
                                            ResultSet rsOSData = data.getResult("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE VOUCHER_NO='"+ReceiptNo+"' AND EFFECT='C' AND ENTRY_NO=" + EntryNo + " AND MATCHED=0 AND SR_NO="+SrNo ,FinanceGlobal.FinURL);
                                            Connection OSConn = data.getConn(FinanceGlobal.FinURL);
                                            Statement OSStmt = OSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                            ResultSet rsOPOSData=OSStmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO=" + EntryNo + " LIMIT 1");
                                            
                                            int MaxSrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND ENTRY_NO="+ EntryNo,FinanceGlobal.FinURL)+1;
                                            
                                            rsOPOSData.moveToInsertRow();
                                            rsOPOSData.updateInt("COMPANY_ID",UtilFunctions.getInt(rsOSData,"COMPANY_ID",0));
                                            rsOPOSData.updateInt("ENTRY_NO",UtilFunctions.getInt(rsOSData,"ENTRY_NO",0));
                                            rsOPOSData.updateInt("SR_NO",MaxSrNo);
                                            rsOPOSData.updateInt("INVOICE_TYPE",UtilFunctions.getInt(rsOSData,"INVOICE_TYPE",0));
                                            rsOPOSData.updateString("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"MAIN_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOSData,"SUB_ACCOUNT_CODE",""));
                                            rsOPOSData.updateString("BOOK_CODE",UtilFunctions.getString(rsOSData,"BOOK_CODE",""));
                                            rsOPOSData.updateString("CHARGE_CODE",UtilFunctions.getString(rsOSData,"CHARGE_CODE",""));
                                            rsOPOSData.updateString("INVOICE_NO",UtilFunctions.getString(rsOSData,"INVOICE_NO",""));
                                            rsOPOSData.updateString("INVOICE_DATE",UtilFunctions.getString(rsOSData,"INVOICE_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LINK_NO",UtilFunctions.getString(rsOSData,"LINK_NO",""));
                                            rsOPOSData.updateString("VOUCHER_NO",UtilFunctions.getString(rsOSData,"VOUCHER_NO",""));
                                            rsOPOSData.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOSData,"VOUCHER_DATE","0000-00-00"));
                                            rsOPOSData.updateString("LEGACY_NO",UtilFunctions.getString(rsOSData,"LEGACY_NO",""));
                                            rsOPOSData.updateDouble("AMOUNT",EITLERPGLOBAL.round(OSAmount - vAmount,2));
                                            rsOPOSData.updateString("EFFECT",UtilFunctions.getString(rsOSData,"EFFECT",""));
                                            rsOPOSData.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOSData,"C_BOOK_CODE",""));
                                            rsOPOSData.updateString("BALE_NO",UtilFunctions.getString(rsOSData,"BALE_NO",""));
                                            rsOPOSData.updateString("LR_NO",UtilFunctions.getString(rsOSData,"LR_NO",""));
                                            rsOPOSData.updateString("OBC_NO",UtilFunctions.getString(rsOSData,"OBC_NO",""));
                                            rsOPOSData.updateBoolean("MATCHED",false);
                                            rsOPOSData.updateString("MATCHED_DATE","0000-00-00");
                                            rsOPOSData.updateString("CREATED_BY",UtilFunctions.getString(rsOSData,"CREATED_BY",""));
                                            rsOPOSData.updateString("CREATED_DATE",UtilFunctions.getString(rsOSData,"CREATED_DATE","0000-00-00"));
                                            rsOPOSData.updateBoolean("CHANGED",true);
                                            rsOPOSData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                            rsOPOSData.insertRow();
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        } else {
                                            //data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+vAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            data.Execute("UPDATE D_FIN_DR_OPENING_OUTSTANDING_DETAIL SET AMOUNT="+OSAmount+",MATCHED=1,MATCHED_DATE=CURDATE() WHERE INVOICE_TYPE="+InvoiceType+" AND EFFECT='C' AND ENTRY_NO="+ EntryNo + " AND SR_NO="+SrNo,FinanceGlobal.FinURL);
                                            vAmount=EITLERPGLOBAL.round(EITLERPGLOBAL.round(vAmount,2)-EITLERPGLOBAL.round(OSAmount,2),2);
                                        }
                                    }
                                }
                                rsCredit.next();
                            }
                        }
                    }
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("FindMatchDebitsFromClosing : Ended ");
    }
    
}
