/*
 * clsOSClosing.java
 *
 * Created on March 17, 2011, 3:17 PM
 */

package EITLERP.Finance;

import EITLERP.*;
import EITLERP.Finance.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import EITLERP.Sales.clsSalesInvoice;

/**
 *
 * @author  root
 */
public class clsOSClosing {
    /** Creates a new instance of clsOSClosing */
    public clsOSClosing() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
            if(!CurrentDate.substring(8,10).equals("03")) {
                return;
            }

            String ToDate=clsDepositMaster.deductDays(CurrentDate,1);
            String FromDate=EITLERPGLOBAL.FinFromDateDB;
            String SQL="SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+ToDate+"' ORDER BY ENTRY_DATE DESC";
            int EntryNo=data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
            FromDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
            EntryNo++;
            clsOSClosing objImport = new clsOSClosing();
            
            System.out.println("Start Suitting...");
            //objImport.getOSClosing(ToDate,FromDate,EntryNo,"210027",1);
            System.out.println("End Suitting...");
            
            System.out.println("Start Felt...");
            objImport.getOSClosing(ToDate,FromDate,EntryNo,"210010",2);
            System.out.println("End Felt...");
            
            System.out.println("Start Filter...");
            objImport.getOSClosing(ToDate,FromDate,EntryNo,"210072",3);
            System.out.println("End Filter...");

            Connection conn = data.getConn(FinanceGlobal.FinURL);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsHeader = stmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER LIMIT 1 ");

            rsHeader.moveToInsertRow();
            rsHeader.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHeader.updateInt("ENTRY_NO",EntryNo);
            rsHeader.updateString("ENTRY_DATE",FromDate);
            rsHeader.updateBoolean("STATUS",false);
            rsHeader.updateString("REMARKS","Outstanding of " + EITLERPGLOBAL.formatDate(FromDate));
            rsHeader.updateInt("MODIFIED_BY",1);
            rsHeader.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateInt("CREATED_BY",1);
            rsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.insertRow();
            
            System.out.println("*** Finished ***");
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void getOSClosing(String ToDate,String FromDate,int EntryNo,String MainCode,int InvoiceType) {
        String SQL="", PartyName="", InvoiceNo="",InvoiceDate="",PartyCode="";
        ResultSet rsParty = null;
        try {
            SQL = "(SELECT DISTINCT SUB_ACCOUNT_CODE FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL " +
            "WHERE INVOICE_TYPE="+InvoiceType+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND ENTRY_NO="+EntryNo+" " +
            "AND (MATCHED_DATE>'"+ToDate+"' OR MATCHED_DATE='0000-00-00')) " +
            "UNION " +
            "(SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
            "AND (B.MATCHED_DATE>'"+ToDate+"' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL) " +
            "AND A.VOUCHER_DATE>'"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND A.APPROVED=1 AND A.CANCELLED=0) " +
            "ORDER BY SUB_ACCOUNT_CODE ";
            rsParty = data.getResult(SQL,FinanceGlobal.FinURL);
            rsParty.first();
            int SrNo = 0;
            Connection conn = data.getConn(FinanceGlobal.FinURL);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsRecord = stmt.executeQuery("SELECT * FROM D_FIN_DR_OPENING_OUTSTANDING_DETAIL LIMIT 1 ");
            while(!rsParty.isAfterLast()) {
                PartyCode=rsParty.getString("SUB_ACCOUNT_CODE");
                PartyName = clsAccount.getAccountName(MainCode, PartyCode);
                if(PartyCode.equals("650131")) {
                    boolean halt = true;
                }
                // -----------------------------
                
                // GET PARTY'S INVOICE NO,INVOICE DATE USING UNION FROM OUTSTANDING DETAIL AND VOUCHER TABLES
                SQL = "SELECT BOOK_CODE,CHARGE_CODE,INVOICE_NO,INVOICE_DATE,LINK_NO,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,AMOUNT,EFFECT, " +
                "C_BOOK_CODE,BALE_NO,LR_NO,OBC_NO,MATCHED,MATCHED_DATE " +
                "FROM FINANCE.D_FIN_DR_OPENING_OUTSTANDING_DETAIL WHERE INVOICE_TYPE="+InvoiceType+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                "AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND ENTRY_NO="+EntryNo+" AND (MATCHED_DATE>'"+ToDate+"' OR MATCHED_DATE='0000-00-00') " +
                "ORDER BY VOUCHER_DATE ";
                
                ResultSet rsOpening = data.getResult(SQL,FinanceGlobal.FinURL);
                rsOpening.first();
                if(rsOpening.getRow()>0) {
                    while(!rsOpening.isAfterLast()) {
                        SrNo++;
                        rsRecord.moveToInsertRow();
                        rsRecord.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsRecord.updateInt("ENTRY_NO",EntryNo);
                        rsRecord.updateInt("SR_NO",SrNo);
                        rsRecord.updateInt("INVOICE_TYPE",InvoiceType);
                        rsRecord.updateString("MAIN_ACCOUNT_CODE",MainCode);
                        rsRecord.updateString("SUB_ACCOUNT_CODE",PartyCode);
                        rsRecord.updateString("BOOK_CODE",UtilFunctions.getString(rsOpening, "BOOK_CODE", ""));
                        rsRecord.updateString("INVOICE_NO",UtilFunctions.getString(rsOpening, "INVOICE_NO", ""));
                        rsRecord.updateString("INVOICE_DATE",UtilFunctions.getString(rsOpening, "INVOICE_DATE","0000-00-00"));
                        rsRecord.updateString("CHARGE_CODE",UtilFunctions.getString(rsOpening, "CHARGE_CODE", ""));
                        rsRecord.updateString("LINK_NO",UtilFunctions.getString(rsOpening, "LINK_NO", ""));
                        rsRecord.updateString("VOUCHER_NO",UtilFunctions.getString(rsOpening, "VOUCHER_NO", ""));
                        rsRecord.updateString("VOUCHER_DATE",UtilFunctions.getString(rsOpening, "VOUCHER_DATE","0000-00-00"));
                        rsRecord.updateString("LEGACY_NO",UtilFunctions.getString(rsOpening, "LEGACY_NO", ""));
                        rsRecord.updateDouble("AMOUNT",UtilFunctions.getDouble(rsOpening, "AMOUNT", 0));
                        rsRecord.updateString("EFFECT",UtilFunctions.getString(rsOpening, "EFFECT", ""));
                        rsRecord.updateString("C_BOOK_CODE",UtilFunctions.getString(rsOpening, "C_BOOK_CODE", ""));
                        rsRecord.updateString("BALE_NO",UtilFunctions.getString(rsOpening, "BALE_NO", ""));
                        rsRecord.updateString("LR_NO",UtilFunctions.getString(rsOpening, "LR_NO", ""));
                        rsRecord.updateString("OBC_NO",UtilFunctions.getString(rsOpening, "OBC_NO", ""));
                        rsRecord.updateBoolean("MATCHED",UtilFunctions.getBoolean(rsOpening, "MATCHED", false));
                        rsRecord.updateString("MATCHED_DATE",UtilFunctions.getString(rsOpening, "MATCHED_DATE", "0000-00-00"));
                        rsRecord.updateString("CREATED_BY","Admin");
                        rsRecord.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsRecord.updateBoolean("CHANGED",true);
                        rsRecord.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsRecord.insertRow();
                        
                        rsOpening.next();
                    }
                }
                
                SQL = "SELECT A.BOOK_CODE,B.INVOICE_NO,B.INVOICE_DATE,B.LINK_NO,A.VOUCHER_NO,A.VOUCHER_DATE,A.LEGACY_NO,B.AMOUNT,B.EFFECT, " +
                "B.MATCHED,B.MATCHED_DATE " +
                "FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B " +
                "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                "AND A.APPROVED=1 AND A.CANCELLED=0 AND A.VOUCHER_DATE >'"+FromDate+"' AND A.VOUCHER_DATE <='"+ToDate+"' " +
                "AND (B.MATCHED_DATE>'"+ToDate+"' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " +
                "ORDER BY A.VOUCHER_DATE ";
                
                ResultSet rsVoucher = data.getResult(SQL,FinanceGlobal.FinURL);
                rsVoucher.first();
                if(rsVoucher.getRow()>0) {
                    while(!rsVoucher.isAfterLast()) {
                        SrNo++;
                        rsRecord.moveToInsertRow();
                        rsRecord.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsRecord.updateInt("ENTRY_NO",EntryNo);
                        rsRecord.updateInt("SR_NO",SrNo);
                        rsRecord.updateInt("INVOICE_TYPE",InvoiceType);
                        rsRecord.updateString("MAIN_ACCOUNT_CODE",MainCode);
                        rsRecord.updateString("SUB_ACCOUNT_CODE",PartyCode);
                        rsRecord.updateString("BOOK_CODE",UtilFunctions.getString(rsVoucher, "BOOK_CODE", ""));
                        rsRecord.updateString("INVOICE_NO",UtilFunctions.getString(rsVoucher, "INVOICE_NO", ""));
                        rsRecord.updateString("INVOICE_DATE",UtilFunctions.getString(rsVoucher, "INVOICE_DATE","0000-00-00"));
                        String ChargeCode = "";
                        if(clsVoucher.getVoucherType(UtilFunctions.getString(rsVoucher, "VOUCHER_NO", ""))==FinanceGlobal.TYPE_SALES_JOURNAL) {
                            ChargeCode = clsSalesInvoice.getInvoiceChargeCode(UtilFunctions.getString(rsVoucher, "INVOICE_NO", ""), UtilFunctions.getString(rsVoucher, "INVOICE_DATE","0000-00-00"));
                        }
                        rsRecord.updateString("CHARGE_CODE",ChargeCode);
                        rsRecord.updateString("LINK_NO",UtilFunctions.getString(rsVoucher, "LINK_NO", ""));
                        rsRecord.updateString("VOUCHER_NO",UtilFunctions.getString(rsVoucher, "VOUCHER_NO", ""));
                        rsRecord.updateString("VOUCHER_DATE",UtilFunctions.getString(rsVoucher, "VOUCHER_DATE","0000-00-00"));
                        rsRecord.updateString("LEGACY_NO",UtilFunctions.getString(rsVoucher, "LEGACY_NO", ""));
                        rsRecord.updateDouble("AMOUNT",UtilFunctions.getDouble(rsVoucher, "AMOUNT", 0));
                        rsRecord.updateString("EFFECT",UtilFunctions.getString(rsVoucher, "EFFECT", ""));
                        rsRecord.updateString("C_BOOK_CODE","");
                        rsRecord.updateString("BALE_NO","");
                        rsRecord.updateString("LR_NO","");
                        String OBCNo = "";
                        if((ChargeCode.startsWith("2")||ChargeCode.startsWith("8")) && InvoiceType==2) {
                            SQL = "SELECT A.BANK_REFERENCE_NO FROM D_FIN_OBC_INVOICE_HEADER A,D_FIN_OBC_INVOICE_DETAIL B " +
                            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.DOC_NO AND A.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                            "AND A.PARTY_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
                            "AND B.INVOICE_NO='"+UtilFunctions.getString(rsVoucher, "INVOICE_NO", "")+"' " +
                            "AND B.INVOICE_DATE='"+UtilFunctions.getString(rsVoucher, "INVOICE_DATE","0000-00-00")+"' ";
                            OBCNo = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        } else if((ChargeCode.startsWith("2")||ChargeCode.startsWith("8") || ChargeCode.startsWith("4")) && InvoiceType==1) {
                            SQL = "SELECT A.BANK_REFERENCE_NO FROM D_FIN_OBC_INVOICE_HEADER A,D_FIN_OBC_INVOICE_DETAIL B " +
                            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.DOC_NO=B.DOC_NO AND A.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                            "AND A.PARTY_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
                            "AND B.INVOICE_NO='"+UtilFunctions.getString(rsVoucher, "INVOICE_NO", "")+"' " +
                            "AND B.INVOICE_DATE='"+UtilFunctions.getString(rsVoucher, "INVOICE_DATE","0000-00-00")+"' ";
                            OBCNo = data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                        } else {
                            OBCNo = "";
                        }
                        rsRecord.updateString("OBC_NO",OBCNo);
                        rsRecord.updateBoolean("MATCHED",UtilFunctions.getBoolean(rsVoucher, "MATCHED", false));
                        rsRecord.updateString("MATCHED_DATE",UtilFunctions.getString(rsVoucher, "MATCHED_DATE", "0000-00-00"));
                        rsRecord.updateString("CREATED_BY","Admin");
                        rsRecord.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsRecord.updateBoolean("CHANGED",true);
                        rsRecord.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsRecord.insertRow();
                        rsVoucher.next();
                    }
                }
                rsParty.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}