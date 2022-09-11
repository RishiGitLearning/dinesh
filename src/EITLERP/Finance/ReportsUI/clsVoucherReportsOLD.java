/*
 * clsVoucherReports.java
 *
 * Created on January 11, 2008, 1:25 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import EITLERP.Finance.*;
import EITLERP.*;
import TReportWriter.SimpleDataProvider.*;
import TReportWriter.*;
import java.util.*;
import java.sql.*;
import EITLERP.Sales.*;


public class clsVoucherReportsOLD {
    
    /** Creates a new instance of clsVoucherReports */
    public clsVoucherReportsOLD() {
        
        
    }
    
    public static TTable getVoucherReport(String VoucherNo,int ProcessType) {
        switch(ProcessType) {
            case 1: return getGeneralVoucherReport(VoucherNo);
            case 2: return getPaymentVoucherReport(VoucherNo,true);
            case 3: return getPaymentVoucherReport(VoucherNo,true);
            case 4: return getBriefPJVReport(VoucherNo);
            case 5: return getCashVoucherReport(VoucherNo);
            case 6: return getDebitVoucherReport(VoucherNo);
            case 7: return getCashReceiptVoucherReport(VoucherNo);
            case 8: return getReceiptVoucherReport(VoucherNo);
            case 9: return getLCJVVoucherReport(VoucherNo);
            case 10: return getSalesDebitVoucherReport(VoucherNo);
            case 11: return getSalesDebitVoucherReportNew(VoucherNo);
            case 12: return getCashPaymentAdvice(VoucherNo);
            
            default:return getGeneralVoucherReport(VoucherNo);
        }
    }
    
//    public static TTable getMemVoucherReport(String VoucherNo,String VoucherDate,int ProcessType) {
//        switch(ProcessType) {
//            
//            case 12: return getMemorandumJVReport(VoucherNo,VoucherDate); 
//            default:return getMemorandumJVReport(VoucherNo,VoucherDate);
//        }
//    }
    
    public static TTable getSalesDebitVoucherReportNew(String VoucherNo) {
        TTable objData=new TTable();
        
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("DAYS");
        objData.AddColumn("INTEREST_RATE");
        objData.AddColumn("PAYMENT_DATE");
        objData.AddColumn("PAYMENT_AMOUNT");
        try{
            
            double TotalAmount=0.0;
            String strSQL="SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE DEBITNOTE_VOUCHER_NO = '" + VoucherNo + "' ORDER BY INVOICE_DATE,INVOICE_NO";
            ResultSet rsVoucher = data.getResult(strSQL,FinanceGlobal.FinURL);
            if(rsVoucher.getRow()>0) {
                while(!rsVoucher.isAfterLast()) {
                    TRow objRow=new TRow();
                    
                    objRow.setValue("INVOICE_NO",clsSalesInvoice.getAgentAlphaSrNo(rsVoucher.getString("INVOICE_NO"),rsVoucher.getString("INVOICE_DATE")));
                    objRow.setValue("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsVoucher.getString("INVOICE_DUE_DATE")));
                    objRow.setValue("DAYS",rsVoucher.getString("DAYS"));
                    int InvoiceType = clsSalesInvoice.getInvoiceType(rsVoucher.getString("INVOICE_NO"), rsVoucher.getString("INVOICE_DATE"));
                    String ChargeCode = clsSalesInvoice.getInvoiceChargeCode(rsVoucher.getString("INVOICE_NO"), rsVoucher.getString("INVOICE_DATE"));
                    String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO = '" + VoucherNo + "' AND EFFECT = 'D'",FinanceGlobal.FinURL);
                    double InterestPercentage = clsSalesInvoice.getDebitNotePercentage(rsVoucher.getString("INVOICE_DATE"),InvoiceType,ChargeCode,PartyCode);
                    
                    objRow.setValue("INTEREST_RATE",InterestPercentage + "%");
                    objRow.setValue("PAYMENT_DATE",EITLERPGLOBAL.formatDate(rsVoucher.getString("VALUE_DATE")));
                    objRow.setValue("PAYMENT_AMOUNT",rsVoucher.getString("DEBIT_NOTE_AMOUNT"));

                    objData.AddRow(objRow);
                    rsVoucher.next();
                }
            }
        }
        catch(Exception e) {
        }
        return objData;
    }

    public static TTable getSalesDebitVoucherReport(String VoucherNo) {
        TTable objData=new TTable();
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("PARTY_CODE");
        objData.AddColumn("PARTY_NAME");               objData.AddColumn("FULL_ACCOUNT_NAME");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        objData.AddColumn("TOTAL_DEBIT_AMOUNT");
        objData.AddColumn("LINK_NO");
        objData.AddColumn("AGENT_ALPHA");
        try {
            String strSQL="";
            String FullAccountName="";
            String PartyName="",PartyCode="";
            ResultSet rsVoucher;
            ResultSet rsParty;
            rsParty = data.getResult("SELECT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO ='"+VoucherNo+"' AND EFFECT = 'D' GROUP BY SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE LIMIT 1",FinanceGlobal.FinURL);
            PartyName=clsPartyMaster.getAccountName(rsParty.getString("MAIN_ACCOUNT_CODE"),rsParty.getString("SUB_ACCOUNT_CODE"));
            PartyCode = rsParty.getString("SUB_ACCOUNT_CODE");
            FullAccountName = clsPartyMaster.getAccountNameAddress(rsParty.getString("MAIN_ACCOUNT_CODE"),rsParty.getString("SUB_ACCOUNT_CODE"));
            double TotalDebitAmount=data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO ='"+VoucherNo+"' AND EFFECT = 'D' ",FinanceGlobal.FinURL);
            strSQL+=" SELECT A.REMARKS AS HEADER_REMARKS,A.CHEQUE_NO,A.CHEQUE_DATE,A.CHEQUE_AMOUNT,B.EFFECT, "+
            "IF(A.REMARKS <> '',A.REMARKS, B.REMARKS)  AS REMARKS,A.VOUCHER_NO,A.VOUCHER_DATE, "+
            "IF(B.SUB_ACCOUNT_CODE<>'',B.SUB_ACCOUNT_CODE,B.MAIN_ACCOUNT_CODE) AS ACCOUNT_CODE, "+
            "IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'', "+
            "IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) AS ACCOUNT_NAME, "+
            "IF(EFFECT='C',AMOUNT,0) AS CR_AMOUNT,IF(EFFECT='D',AMOUNT,0) AS DR_AMOUNT,B.PO_NO,B.PO_DATE,B.INVOICE_NO, " +
            "B.INVOICE_DATE,A.BOOK_CODE,A.ST_CATEGORY, " +
            "IF(EX.EX_ACCOUNT_NAME IS NULL, IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'', " +
            "IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) ,EX_ACCOUNT_NAME) AS ACCOUNT_NAME_EX, " +
            "B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,BOOK.BOOK_NAME,B.LINK_NO ";
            strSQL+=" FROM ";
            strSQL+=" D_FIN_VOUCHER_HEADER A ";
            strSQL+=" LEFT JOIN D_FIN_BOOK_MASTER BOOK ON (BOOK.BOOK_CODE=A.BOOK_CODE), ";
            strSQL+=" D_FIN_VOUCHER_DETAIL B ";
            strSQL+=" LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_PARTY_MASTER AS PARTY ON (PARTY.PARTY_CODE=B.SUB_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_EX_ACCOUNT_MASTER AS EX ON ( SUBSTRING(B.MAIN_ACCOUNT_CODE,LENGTH(B.MAIN_ACCOUNT_CODE)-1)=EX.EX_ACCOUNT_CODE) WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND EFFECT ='D' GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO";
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            if(rsVoucher.getRow()>0) {
                while(!rsVoucher.isAfterLast()) {
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE", rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("DR_AMOUNT")));
                    objRow.setValue("CR_AMOUNT","0");
                    objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                    objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",EITLERPGLOBAL.formatDate(rsVoucher.getString("INVOICE_DATE")));
                    objRow.setValue("HEADER_REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objRow.setValue("REMARKS",rsVoucher.getString("REMARKS").trim());
                    objRow.setValue("PARTY_CODE",PartyCode);
                    objRow.setValue("PARTY_NAME",PartyName);
                    objRow.setValue("FULL_ACCOUNT_NAME",FullAccountName);
                    objRow.setValue("TOTAL_DEBIT_AMOUNT",Double.toString(TotalDebitAmount));
                    objRow.setValue("PARTY_MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    
                    String LinkNo="";
                    if(rsVoucher.getString("LINK_NO").equals(null) || rsVoucher.getString("LINK_NO").equals("")) {
                        LinkNo = "";//LinkNo.substring(0,LinkNo.indexOf("/")+1)+Year;
                    }
                    else {
                        LinkNo = rsVoucher.getString("LINK_NO");
                        String Year = LinkNo.substring(LinkNo.indexOf("/")+1);
                        Year = Year.substring(2)+Year.substring(0,2);
                        LinkNo = LinkNo.substring(0,LinkNo.indexOf("/")+1)+Year;
                    }
                    //String LinkNo = rsVoucher.getString("LINK_NO");
                    //String Year = LinkNo.substring(LinkNo.indexOf("/")+1);
                    //Year = Year.substring(2)+Year.substring(0,2);
                    //LinkNo = LinkNo.substring(0,LinkNo.indexOf("/")+1)+Year;
                    objRow.setValue("LINK_NO",LinkNo);
                    objRow.setValue("AGENT_ALPHA",clsSalesParty.getAgentAlpha(PartyCode));
                    objData.AddRow(objRow);
                    rsVoucher.next();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return objData;
    }
    
    public static TTable getPaymentVoucherReport(String VoucherNo,boolean IncludeAdditions) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("GRN_NO");
        objData.AddColumn("GRN_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("FULL_ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("ST_CATEGORY");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        objData.AddColumn("HEADER_REMARKS");
        objData.AddColumn("LINK_NO");
        
        try {
            
            int BlockNo=0;
            String InvoiceNo="";
            ResultSet rsInvoices;
            ResultSet rsVoucher;
            ResultSet rsVoucherHeader;
            ResultSet rsTmp;
            String FootNote="";
            double ChequeAmount=0;
            int VoucherSrNo=0;
            
            rsVoucherHeader=data.getResult("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
            rsVoucherHeader.first();
            
            //Get the Invoice nos.
            rsInvoices=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND IS_DEDUCTION=0" ,FinanceGlobal.FinURL);
            rsInvoices.first();
            
            if(rsInvoices.getRow()>0) {
                
                while(!rsInvoices.isAfterLast()) {
                    
                    //Get the Maximum Amount
                    InvoiceNo=rsInvoices.getString("INVOICE_NO");
                    
                    VoucherSrNo=rsInvoices.getInt("SR_NO");
                    
                    
                    if(data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL A WHERE VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+VoucherSrNo+" AND EFFECT='D' AND SUB_ACCOUNT_CODE<>'' AND (IS_DEDUCTION=0) ORDER BY SR_NO ",FinanceGlobal.FinURL)) {
                        rsVoucher=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL A WHERE VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+VoucherSrNo+" AND EFFECT='D' AND SUB_ACCOUNT_CODE<>'' AND (IS_DEDUCTION=0) ORDER BY SR_NO ",FinanceGlobal.FinURL);
                    }
                    else {
                        rsVoucher=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL A WHERE VOUCHER_NO='"+VoucherNo+"' AND SR_NO="+VoucherSrNo+" AND EFFECT='D' AND (IS_DEDUCTION=0) ORDER BY SR_NO ",FinanceGlobal.FinURL);
                        
                    }
                    
                    rsVoucher.first();
                    
                    if(rsVoucher.getRow()>0) {
                        
                        boolean updated=false;
                        while(!rsVoucher.isAfterLast()) {
                            ChequeAmount+=rsVoucher.getDouble("AMOUNT");
                            
                            BlockNo=1;
                            
                            TRow objRow=new TRow();
                            objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("BOOK_CODE", rsVoucherHeader.getString("BOOK_CODE"));
                            objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucherHeader.getString("BOOK_CODE")));
                            objRow.setValue("VOUCHER_NO", VoucherNo);
                            objRow.setValue("VOUCHER_DATE",rsVoucherHeader.getString("VOUCHER_DATE"));
                            objRow.setValue("GRN_NO", rsVoucher.getString("GRN_NO"));
                            objRow.setValue("GRN_DATE",rsVoucher.getString("GRN_DATE"));
                            objRow.setValue("CHEQUE_NO",rsVoucherHeader.getString("CHEQUE_NO"));
                            objRow.setValue("CHEQUE_DATE",rsVoucherHeader.getString("CHEQUE_DATE"));
                            objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucherHeader.getDouble("CHEQUE_AMOUNT")));
                            objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                            objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                            objRow.setValue("HEADER_REMARKS",rsVoucherHeader.getString("REMARKS").trim());
                            
                            String AccountName=clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE"));
                            
                            objRow.setValue("ACCOUNT_NAME",AccountName);
                            objRow.setValue("FULL_ACCOUNT_NAME",clsPartyMaster.getAccountNameAddress(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                            objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT")));
                            objRow.setValue("CR_AMOUNT","0");
                            objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                            objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                            objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                            objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                            objRow.setValue("ST_CATEGORY",rsVoucherHeader.getString("ST_CATEGORY"));
                            objRow.setValue("LINK_NO",rsVoucher.getString("LINK_NO"));
                            
                            //Decide Invoice Amount
                            FootNote="";
                            double InvoiceAmount=0;
                            
                            
                        /*if((!InvoiceNo.equals(""))&&rsVoucher.getInt("MODULE_ID")!=0&&rsVoucher.getDouble("INVOICE_AMOUNT")==0) {
                            switch(rsVoucher.getInt("MODULE_ID")) {
                                case 7:
                                    InvoiceAmount=data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_GRN_HEADER WHERE GRN_NO='"+rsVoucher.getString("GRN_NO")+"' AND GRN_TYPE=1");
                                    break;
                                case 8:
                                    InvoiceAmount=data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_GRN_HEADER WHERE GRN_NO='"+rsVoucher.getString("GRN_NO")+"' AND GRN_TYPE=2");
                                    break;
                                case 48:
                                    InvoiceAmount=data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_JOB_HEADER WHERE JOB_NO='"+rsVoucher.getString("GRN_NO")+"'");
                                    break;
                                case 63:
                                    InvoiceAmount=data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_FIN_EXPENSE_TRANSACTION WHERE DOC_NO='"+rsVoucher.getString("GRN_NO")+"'",FinanceGlobal.FinURL);
                                    break;
                            }
                        }*/
                            
                            InvoiceAmount=rsVoucher.getDouble("AMOUNT");
                            objRow.setValue("INVOICE_AMOUNT",Double.toString(InvoiceAmount));
                            
                            if(!InvoiceNo.equals("")&&InvoiceAmount>0) {
                                //FootNote="Net Amount "+InvoiceAmount;
                            }
                            
                            //Turn of Remarks. Find Other Deductions
                            //rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND INVOICE_NO='"+InvoiceNo+"' AND EFFECT='C' AND IS_DEDUCTION=1",FinanceGlobal.FinURL);
                            rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+VoucherSrNo+" AND EFFECT='C' AND IS_DEDUCTION=1",FinanceGlobal.FinURL);
                            rsTmp.first();
                            
                            if(rsTmp.getRow()>0) {
                                FootNote="";
                                
                                if(!updated) {
                                    updated=true;
                                    while(!rsTmp.isAfterLast()) {
                                        InvoiceAmount=InvoiceAmount+rsTmp.getDouble("AMOUNT");
                                        
                                        
                                        FootNote=FootNote+" Less "+clsAccount.getAccountName( rsTmp.getString("MAIN_ACCOUNT_CODE"), "")+" "+rsTmp.getDouble("AMOUNT");
                                        
                                        if(!UtilFunctions.getString(rsTmp,"REMARKS","").trim().equals("")) {
                                            FootNote=FootNote+" "+UtilFunctions.getString(rsTmp,"REMARKS","").trim();
                                        }
                                        rsTmp.next();
                                    }
                                }
                            }
                            
                            
                            //Find any Debit Notes for this invoice
                            rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_DOCS WHERE VOUCHER_NO='"+VoucherNo+"' AND VOUCHER_SR_NO IN (SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND VOUCHER_SR_NO="+VoucherSrNo+" )",FinanceGlobal.FinURL);
                            rsTmp.first();
                            
                            if(rsTmp.getRow()>0) {
                                
                                while(!rsTmp.isAfterLast()) {
                                    InvoiceAmount=InvoiceAmount+rsTmp.getDouble("AMOUNT");
                                    
                                    FootNote=FootNote+" Less Debit Note No. "+UtilFunctions.getString(rsTmp,"DOC_NO","")+" Date "+EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DOC_DATE","0000-00-00"))+" Rs. "+rsTmp.getDouble("AMOUNT");
                                    
                                    rsTmp.next();
                                }
                                
                            }
                            
                            if(!FootNote.trim().equals("")) {
                                FootNote="Total Amount "+(InvoiceAmount)+FootNote;
                            }
                            
                            objRow.setValue("REMARKS",FootNote.trim());
                            objData.AddRow(objRow);
                            
                            rsVoucher.next();
                        }
                    }
                    rsInvoices.next();
                }
                
                
                
                //*********** Add Bank Charges/Other Charges *****//
                if(IncludeAdditions) {
                    rsInvoices=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX A WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND IS_DEDUCTION=2 ORDER BY SR_NO ",FinanceGlobal.FinURL);
                    rsInvoices.first();
                    if(rsInvoices.getRow()>0) {
                        while(!rsInvoices.isAfterLast()) {
                            TRow objRow=new TRow();
                            objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("BOOK_CODE", rsVoucherHeader.getString("BOOK_CODE"));
                            objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucherHeader.getString("BOOK_CODE")));
                            objRow.setValue("VOUCHER_NO", VoucherNo);
                            objRow.setValue("VOUCHER_DATE",rsVoucherHeader.getString("VOUCHER_DATE"));
                            objRow.setValue("CHEQUE_NO",rsVoucherHeader.getString("CHEQUE_NO"));
                            objRow.setValue("CHEQUE_DATE",rsVoucherHeader.getString("CHEQUE_DATE"));
                            objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucherHeader.getDouble("CHEQUE_AMOUNT")));
                            objRow.setValue("MAIN_ACCOUNT_CODE",rsInvoices.getString("MAIN_ACCOUNT_CODE"));
                            objRow.setValue("SUB_ACCOUNT_CODE",rsInvoices.getString("SUB_ACCOUNT_CODE"));
                            objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            objRow.setValue("FULL_ACCOUNT_NAME",clsAccount.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            objRow.setValue("DR_AMOUNT",Double.toString(rsInvoices.getDouble("AMOUNT")));
                            objRow.setValue("CR_AMOUNT","0");
                            objRow.setValue("PO_NO",clsPartyMaster.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            //objRow.setValue("PO_NO",rsInvoices.getString("PO_NO"));
                            objRow.setValue("PO_DATE",rsInvoices.getString("PO_DATE"));
                            objRow.setValue("INVOICE_NO",rsInvoices.getString("INVOICE_NO"));
                            objRow.setValue("INVOICE_DATE",rsInvoices.getString("INVOICE_DATE"));
                            objRow.setValue("ST_CATEGORY",rsVoucherHeader.getString("ST_CATEGORY"));
                            objRow.setValue("REMARKS",rsVoucherHeader.getString("REMARKS").trim());
                            objRow.setValue("LINK_NO",rsInvoices.getString("LINK_NO"));
                            objData.AddRow(objRow);
                            
                            ChequeAmount+=rsInvoices.getDouble("AMOUNT");
                            
                            rsInvoices.next();
                        }
                    }
                    
                }
                
                
                //Update ChequeAmount in all Rows
                for(int i=1;i<=objData.getRowCount();i++) {
                    objData.Rows(i).setValue("CHEQUE_AMOUNT", Double.toString(ChequeAmount));
                }
                
                
            }
            else {
                
                //If no entry found for invoices, find additions entries only
                if(IncludeAdditions) {
                    rsInvoices=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX A WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND IS_DEDUCTION=2 ORDER BY SR_NO ",FinanceGlobal.FinURL);
                    rsInvoices.first();
                    if(rsInvoices.getRow()>0) {
                        while(!rsInvoices.isAfterLast()) {
                            TRow objRow=new TRow();
                            objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                            objRow.setValue("BOOK_CODE", rsVoucherHeader.getString("BOOK_CODE"));
                            objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucherHeader.getString("BOOK_CODE")));
                            objRow.setValue("VOUCHER_NO", VoucherNo);
                            objRow.setValue("VOUCHER_DATE",rsVoucherHeader.getString("VOUCHER_DATE"));
                            objRow.setValue("CHEQUE_NO",rsVoucherHeader.getString("CHEQUE_NO"));
                            objRow.setValue("CHEQUE_DATE",rsVoucherHeader.getString("CHEQUE_DATE"));
                            objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucherHeader.getDouble("CHEQUE_AMOUNT")));
                            objRow.setValue("MAIN_ACCOUNT_CODE",rsInvoices.getString("MAIN_ACCOUNT_CODE"));
                            objRow.setValue("SUB_ACCOUNT_CODE",rsInvoices.getString("SUB_ACCOUNT_CODE"));
                            objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            objRow.setValue("FULL_ACCOUNT_NAME",clsAccount.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            objRow.setValue("DR_AMOUNT",Double.toString(rsInvoices.getDouble("AMOUNT")));
                            objRow.setValue("CR_AMOUNT","0");
                            objRow.setValue("PO_NO",clsPartyMaster.getAccountName(rsInvoices.getString("MAIN_ACCOUNT_CODE"),""));
                            //objRow.setValue("PO_NO",rsInvoices.getString("PO_NO"));
                            objRow.setValue("PO_DATE",rsInvoices.getString("PO_DATE"));
                            objRow.setValue("INVOICE_NO",rsInvoices.getString("INVOICE_NO"));
                            objRow.setValue("INVOICE_DATE",rsInvoices.getString("INVOICE_DATE"));
                            objRow.setValue("ST_CATEGORY",rsVoucherHeader.getString("ST_CATEGORY"));
                            objRow.setValue("HEADER_REMARKS",rsVoucherHeader.getString("REMARKS").trim());
                            objRow.setValue("LINK_NO",rsInvoices.getString("LINK_NO"));
                            objData.AddRow(objRow);
                            
                            ChequeAmount+=rsInvoices.getDouble("AMOUNT");
                            
                            rsInvoices.next();
                        }
                    }
                    
                    for(int i=1;i<=objData.getRowCount();i++) {
                        objData.Rows(i).setValue("CHEQUE_AMOUNT", Double.toString(ChequeAmount));
                    }
                    
                }
                
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getCashReceiptVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("ST_CATEGORY");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL="SELECT *,A.REMARKS AS HEADER_REMARKS FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
            strSQL+="AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' ";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT")));
                    objRow.setValue("CR_AMOUNT","0");
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getCashPaymentAdvice(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("ST_CATEGORY");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL="SELECT *,A.REMARKS AS HEADER_REMARKS FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
            strSQL+="AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' ";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT")));
                    objRow.setValue("CR_AMOUNT","0");
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }

    
    public static TTable getReceiptVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("REMARKS");
        objData.AddColumn("LINK_NO");
        objData.AddColumn("PAYMENT_MODE");
        objData.AddColumn("PAYMENT_MODE_DESC");
        objData.AddColumn("CUSTOMER_BANK");
        
        try {
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL = "SELECT A.COMPANY_ID,B.EFFECT,A.VOUCHER_NO,A.VOUCHER_DATE, B.MAIN_ACCOUNT_CODE," +
            "IF(B.SUB_ACCOUNT_CODE<>'',B.SUB_ACCOUNT_CODE,'') AS SUB_ACCOUNT_CODE, "+
            "IF(IF(B.SUB_ACCOUNT_CODE<>'', "+
            "PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'', "+
            "PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) AS ACCOUNT_NAME,AMOUNT AS CR_AMOUNT, "+
            "A.BOOK_CODE ,A.REMARKS,A.CUSTOMER_BANK, "+
            "B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.SR_NO,A.CHEQUE_NO,A.CHEQUE_DATE,B.LINK_NO, "+
            "A.PAYMENT_MODE,D.PARA_DESCRIPTION,B.GRN_NO "+
            "FROM    D_FIN_VOUCHER_HEADER A, D_FIN_PARAMETER_MASTER D,  D_FIN_VOUCHER_DETAIL B  "+
            "LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE)  "+
            "LEFT JOIN D_FIN_PARTY_MASTER AS PARTY ON (PARTY.PARTY_CODE=B.SUB_ACCOUNT_CODE) "+
            "LEFT JOIN D_FIN_EX_ACCOUNT_MASTER AS EX ON ( SUBSTRING(B.MAIN_ACCOUNT_CODE, "+
            "LENGTH(B.MAIN_ACCOUNT_CODE)-1)=EX.EX_ACCOUNT_CODE) "+
            "WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT ='C' AND  "+
            " D.PARA_ID='PAYMENT_MODE' AND A.PAYMENT_MODE=D.PARA_CODE AND  " +
            "A.VOUCHER_NO='"+VoucherNo+"' GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO ";
            
            System.out.println(strSQL);
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    objRow.setValue("CR_AMOUNT",Double.toString(rsVoucher.getDouble("CR_AMOUNT")));
                    String MainCode = rsVoucher.getString("MAIN_ACCOUNT_CODE");
                    String SubCode = rsVoucher.getString("SUB_ACCOUNT_CODE");
                    if (!rsVoucher.getString("INVOICE_NO").trim().equals("")) {
                        String BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO ='"+rsVoucher.getString("GRN_NO").trim()+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                        if (BookCode.trim().equals("10")) {
                            objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                        } else if(rsVoucher.getString("GRN_NO").startsWith("OB")) {
                            String RefNo = data.getStringValueFromDB("SELECT BANK_REFERENCE_NO FROM D_FIN_OBC WHERE DOC_NO='"+rsVoucher.getString("GRN_NO")+"' ",FinanceGlobal.FinURL);
                            objRow.setValue("INVOICE_NO",RefNo);
                        } else if(MainCode.equals("210027") || MainCode.equals("210072")) {
                            String InvNo = frmReceiptVoucher.getLinkNo(rsVoucher.getString("INVOICE_NO"),rsVoucher.getString("INVOICE_DATE"),VoucherNo,SubCode);
                            objRow.setValue("INVOICE_NO",InvNo);
                        } else {
                            objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                        }
                    }
                    else {
                        objRow.setValue("INVOICE_NO","");
                    }
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("REMARKS").trim());
                    objRow.setValue("CUSTOMER_BANK",rsVoucher.getString("CUSTOMER_BANK"));
                    if(MainCode.equals("210027") || MainCode.equals("210072") || MainCode.equals("210010") || MainCode.equals("210048")) {
                        objRow.setValue("LINK_NO",rsVoucher.getString("LINK_NO"));
                    }
                    else {
                        objRow.setValue("LINK_NO","");
                    }
                    
                    objRow.setValue("PAYMENT_MODE",rsVoucher.getString("PAYMENT_MODE"));
                    objRow.setValue("PAYMENT_MODE_DESC",rsVoucher.getString("PARA_DESCRIPTION"));
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getLCJVVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("SR_NO");
        objData.AddColumn("COMPANY_ID");
        //objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        //objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("EFFECT");
        //objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        //objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        //objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("LINK_NO");
        //objData.AddColumn("ST_CATEGORY");
        //objData.AddColumn("PARTY_NAME");
        //objData.AddColumn("ACCOUNT_CODE");
        //objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL = "SELECT A.COMPANY_ID,B.EFFECT,A.VOUCHER_NO,A.VOUCHER_DATE, B.MAIN_ACCOUNT_CODE," +
            "IF(B.SUB_ACCOUNT_CODE<>'',B.SUB_ACCOUNT_CODE,'') AS SUB_ACCOUNT_CODE, "+
            "IF(IF(B.SUB_ACCOUNT_CODE<>'', "+
            "PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'', "+
            "PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) AS ACCOUNT_NAME,AMOUNT AS CR_AMOUNT, "+
            "A.BOOK_CODE ,A.REMARKS,B.EFFECT, "+
            "B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.SR_NO,A.CHEQUE_NO,A.CHEQUE_DATE,B.LINK_NO "+
            "FROM    D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B  "+
            "LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE)  "+
            "LEFT JOIN D_FIN_PARTY_MASTER AS PARTY ON (PARTY.PARTY_CODE=B.SUB_ACCOUNT_CODE) "+
            "LEFT JOIN D_FIN_EX_ACCOUNT_MASTER AS EX ON ( SUBSTRING(B.MAIN_ACCOUNT_CODE, "+
            "LENGTH(B.MAIN_ACCOUNT_CODE)-1)=EX.EX_ACCOUNT_CODE) "+
            "WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND "+
            "A.VOUCHER_NO='"+VoucherNo+"' GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO ";
            
            //strSQL="SELECT *,A.REMARKS AS HEADER_REMARKS FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B ";
            //strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
            //strSQL+="AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' ";
            System.out.println(strSQL);
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            int Counter = 0;
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    Counter++;
                    TRow objRow=new TRow();
                    objRow.setValue("SR_NO",Integer.toString(Counter));
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    //objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    //objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("EFFECT",rsVoucher.getString("EFFECT"));
                    //objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    //objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT")));
                    objRow.setValue("CR_AMOUNT",Double.toString(rsVoucher.getDouble("CR_AMOUNT")));
                    //objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    String MainCode = rsVoucher.getString("MAIN_ACCOUNT_CODE");
                    String SubCode = rsVoucher.getString("SUB_ACCOUNT_CODE");
                    if (rsVoucher.getString("EFFECT").trim().equals("C")) {
                        if(MainCode.equals("210027") || MainCode.equals("210072")) {
                            String InvNo = frmReceiptVoucher.getLinkNo(rsVoucher.getString("INVOICE_NO"),rsVoucher.getString("INVOICE_DATE"),VoucherNo,SubCode);
                            objRow.setValue("INVOICE_NO",InvNo);
                        } else {
                            objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                        }
                    } else {
                        objRow.setValue("INVOICE_NO","");
                    }
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("REMARKS").trim());
                    objRow.setValue("LINK_NO",rsVoucher.getString("LINK_NO"));
                    objData.AddRow(objRow);
                    rsVoucher.next();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getGeneralVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("ST_CATEGORY");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        objData.AddColumn("LINK_NO");
        objData.AddColumn("ST");
        objData.AddColumn("ST_RS");
        objData.AddColumn("REMARK_TDS");
        objData.AddColumn("REMARK_ESI");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL+=" SELECT A.REMARKS,A.CHEQUE_NO,A.CHEQUE_DATE,A.CHEQUE_AMOUNT,B.LINK_NO,B.EFFECT,A.VOUCHER_NO,A.VOUCHER_DATE, IF(B.SUB_ACCOUNT_CODE<>'',B.SUB_ACCOUNT_CODE,B.MAIN_ACCOUNT_CODE) AS ACCOUNT_CODE, IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) AS ACCOUNT_NAME,IF(EFFECT='C',AMOUNT,0) AS CR_AMOUNT,IF(EFFECT='D',AMOUNT,0) AS DR_AMOUNT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,A.BOOK_CODE,A.ST_CATEGORY, IF(EX.EX_ACCOUNT_NAME IS NULL, IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) ,EX_ACCOUNT_NAME) AS ACCOUNT_NAME_EX,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,BOOK.BOOK_NAME ";
            strSQL+=" FROM ";
            strSQL+=" D_FIN_VOUCHER_HEADER A ";
            strSQL+=" LEFT JOIN D_FIN_BOOK_MASTER BOOK ON (BOOK.BOOK_CODE=A.BOOK_CODE), ";
            strSQL+=" D_FIN_VOUCHER_DETAIL B ";
            strSQL+=" LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_PARTY_MASTER AS PARTY ON (PARTY.PARTY_CODE=B.SUB_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_EX_ACCOUNT_MASTER AS EX ON ( SUBSTRING(B.MAIN_ACCOUNT_CODE,LENGTH(B.MAIN_ACCOUNT_CODE)-1)=EX.EX_ACCOUNT_CODE) WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO";
            
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE", rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    
                    if(!rsVoucher.getString("SUB_ACCOUNT_CODE").trim().equals("")) {
                        PartyName=clsAccount.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE"));
                        PartyMainAccountCode=rsVoucher.getString("MAIN_ACCOUNT_CODE");
                    }
                    
                    objRow.setValue("ACCOUNT_CODE",rsVoucher.getString("ACCOUNT_CODE"));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("DR_AMOUNT")));
                    objRow.setValue("CR_AMOUNT",Double.toString(rsVoucher.getDouble("CR_AMOUNT")));
                    objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                    objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("ST_CATEGORY",rsVoucher.getString("ST_CATEGORY"));
                    objRow.setValue("REMARKS",rsVoucher.getString("REMARKS").trim());
                    objRow.setValue("LINK_NO",rsVoucher.getString("LINK_NO"));
                    
                    //============================================
                    
                    String STMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND LENGTH(MAIN_ACCOUNT_CODE)=9 AND EFFECT='D'",FinanceGlobal.FinURL);
                    double STAmount = 0;
                    String STCode = "";
                    if(!STMainCode.equals("")) {
                        STAmount = data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND LENGTH(MAIN_ACCOUNT_CODE)=9 AND EFFECT='D'",FinanceGlobal.FinURL);
                        System.out.println("st code :" + STMainCode.substring(STMainCode.indexOf(".")+1));
                        STCode = STMainCode.substring(STMainCode.indexOf(".")+1);
                    }
                    objRow.setValue("ST",STCode);
                    objRow.setValue("ST_RS",Double.toString(STAmount));
                    
                    String RemarkTDS = "";
                    String RemarkESI = "";
                    ResultSet rsTmp1 = data.getResult("SELECT MAIN_ACCOUNT_CODE,APPLICABLE_AMOUNT,PERCENTAGE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND EFFECT='C' AND IS_DEDUCTION=1",FinanceGlobal.FinURL);
                    rsTmp1.first();
                    if(rsTmp1.getRow() > 0 ) {
                        while(!rsTmp1.isAfterLast()) {
                            if(rsTmp1.getString("MAIN_ACCOUNT_CODE").trim().equals("127176")) {
                                RemarkTDS = "TDS ON Rs. " + rsTmp1.getString("APPLICABLE_AMOUNT") + " @ " + rsTmp1.getString("PERCENTAGE");
                            } else if(rsTmp1.getString("MAIN_ACCOUNT_CODE").equals("127035")) {
                                RemarkESI = "ESI ON Rs. " + rsTmp1.getString("APPLICABLE_AMOUNT") + " @ " + rsTmp1.getString("PERCENTAGE");
                            }
                            rsTmp1.next();
                        }
                    }
                    objRow.setValue("REMARK_TDS",RemarkTDS);
                    objRow.setValue("REMARK_ESI",RemarkESI);
                    //================================================
                    
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
                
                for(int i=1;i<=objData.getRowCount();i++) {
                    objData.Rows(i).setValue("PARTY_NAME",PartyName);
                    objData.Rows(i).setValue("PARTY_MAIN_ACCOUNT_CODE",PartyMainAccountCode);
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getMemorandumJVReport(String VoucherNo,String VoucherDate) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("LINK_NO");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
           
            strSQL+=" SELECT A.REMARKS,B.LINK_NO,B.EFFECT,A.VOUCHER_NO,A.VOUCHER_DATE, IF(EFFECT='C',AMOUNT,0) AS CR_AMOUNT ";
            strSQL+=" ,IF(EFFECT='D',AMOUNT,0) AS DR_AMOUNT,GL.ACCOUNT_NAME,A.BOOK_CODE,B.MAIN_ACCOUNT_CODE,BOOK.BOOK_NAME ";
            strSQL+=" FROM   D_FIN_MEM_HEADER A ";
            strSQL+=" LEFT JOIN D_FIN_BOOK_MASTER BOOK ON (BOOK.BOOK_CODE=A.BOOK_CODE), ";
            strSQL+=" D_FIN_MEM_DETAIL B ";
            strSQL+=" LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE) ";
            strSQL+=" WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND A.VOUCHER_DATE='"+EITLERPGLOBAL.formatDateDB(VoucherDate)+"' ";
            strSQL+=" GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO ";
            
             
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE", rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),""));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("DR_AMOUNT")));
                    objRow.setValue("CR_AMOUNT",Double.toString(rsVoucher.getDouble("CR_AMOUNT")));
                    objRow.setValue("REMARKS",rsVoucher.getString("REMARKS").trim());
                    objRow.setValue("LINK_NO",rsVoucher.getString("LINK_NO"));
                   
                    
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
                
               
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getBriefPJVReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("REMARKS");
        objData.AddColumn("EFFECT");
        objData.AddColumn("ST");
        objData.AddColumn("ST_RS");
        objData.AddColumn("REMARK_TDS");
        objData.AddColumn("REMARK_ESI");
        
        try {
            
            int CompanyID=clsVoucher.getVoucherCompanyID(VoucherNo);
            clsVoucher objVoucher=(clsVoucher)(new clsVoucher()).getObject(CompanyID, VoucherNo);
            
            double CrTotal=0;
            double DrTotal=0;
            String PartyMainCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SUB_ACCOUNT_CODE<>'' AND EFFECT='C'",FinanceGlobal.FinURL);
            String PartySubCode=data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+VoucherNo+"' AND SUB_ACCOUNT_CODE<>'' AND EFFECT='C'",FinanceGlobal.FinURL);
            String PartyName=clsPartyMaster.getAccountName(PartyMainCode,PartySubCode);
            
            HashMap colVoucherItemsEx=new HashMap();
            
            colVoucherItemsEx.clear();
            
            //!! ============================ Add remaining Entries =================================//
            ResultSet rsTmp=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL A WHERE VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    boolean Continue=true;
                    
                    //Search for the existence of this account code in data
                    for(int j=1;j<=objData.getRowCount();j++) {
                        String strMainCode=rsTmp.getString("MAIN_ACCOUNT_CODE").substring(0,6);
                        String dataMainCode=objData.Rows(j).getValue("ACCOUNT_CODE").substring(0,6);
                        String strEffect=rsTmp.getString("EFFECT");
                        String dataEffect=objData.Rows(j).getValue("EFFECT");
                        
                        if(dataMainCode.equals(strMainCode)&&dataEffect.equals(strEffect)) //Both are equal. Merge both
                        {
                            Continue=false;
                            if(dataEffect.equals("C")) {
                                double Amount=UtilFunctions.CDbl(objData.Rows(j).getValue("CR_AMOUNT"));
                                Amount+=rsTmp.getDouble("AMOUNT");
                                objData.Rows(j).setValue("CR_AMOUNT",Double.toString(Amount));
                            }
                            else {
                                double Amount=UtilFunctions.CDbl(objData.Rows(j).getValue("DR_AMOUNT"));
                                Amount+=rsTmp.getDouble("AMOUNT");
                                objData.Rows(j).setValue("DR_AMOUNT",Double.toString(Amount));
                            }
                        }
                        
                    }
                    
                    
                    if(Continue) {
                        TReportWriter.SimpleDataProvider.TRow objRow=objData.newRow();
                        
                        objRow.setValue("EFFECT",rsTmp.getString("EFFECT"));
                        objRow.setValue("COMPANY_ID", Integer.toString(objVoucher.getAttribute("COMPANY_ID").getInt()));
                        objRow.setValue("COMPANY_NAME",clsCompany.getCompanyName(objVoucher.getAttribute("COMPANY_ID").getInt()));
                        objRow.setValue("BOOK_CODE",objVoucher.getAttribute("BOOK_CODE").getString());
                        objRow.setValue("VOUCHER_NO",objVoucher.getAttribute("VOUCHER_NO").getString());
                        objRow.setValue("VOUCHER_DATE",objVoucher.getAttribute("VOUCHER_DATE").getString());
                        objRow.setValue("PARTY_MAIN_ACCOUNT_CODE",PartyMainCode);
                        objRow.setValue("PARTY_NAME",PartyName);
                        String MainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                        String SubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                        objRow.setValue("ACCOUNT_CODE",MainCode);
                        objRow.setValue("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,SubCode));
                        if(!(SubCode.equals(""))) {
                            objRow.setValue("ACCOUNT_CODE",SubCode);
                        }
                        else {
                            objRow.setValue("ACCOUNT_CODE",MainCode);
                        }
                        if(UtilFunctions.getString(rsTmp,"EFFECT","").equals("C")) {
                            
                            objRow.setValue("CR_AMOUNT",Double.toString(UtilFunctions.getDouble(rsTmp,"AMOUNT",0)));
                            objRow.setValue("DR_AMOUNT","");
                            
                            
                        }
                        else {
                            objRow.setValue("DR_AMOUNT",Double.toString(UtilFunctions.getDouble(rsTmp,"AMOUNT",0)));
                            objRow.setValue("CR_AMOUNT","");
                        }
                        
                        objRow.setValue("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                        objRow.setValue("PO_DATE",UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00"));
                        objRow.setValue("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objRow.setValue("INVOICE_DATE",UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00"));
                        objRow.setValue("REMARKS",objVoucher.getAttribute("REMARKS").getString().trim());
                        
                        //============================================
                        
                        String STMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND LENGTH(MAIN_ACCOUNT_CODE)=9 AND EFFECT='D'",FinanceGlobal.FinURL);
                        double STAmount = 0;
                        String STCode = "";
                        if(!STMainCode.equals("")) {
                            STAmount = data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND LENGTH(MAIN_ACCOUNT_CODE)=9 AND EFFECT='D'",FinanceGlobal.FinURL);
                            System.out.println("st code :" + STMainCode.substring(STMainCode.indexOf(".")+1));
                            STCode = STMainCode.substring(STMainCode.indexOf(".")+1);
                        }
                        objRow.setValue("ST",STCode);
                        objRow.setValue("ST_RS",Double.toString(STAmount));
                        
                        String RemarkTDS = "";
                        String RemarkESI = "";
                        ResultSet rsTmp1 = data.getResult("SELECT MAIN_ACCOUNT_CODE,APPLICABLE_AMOUNT,PERCENTAGE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo.trim()+"' AND EFFECT='C' AND IS_DEDUCTION=1",FinanceGlobal.FinURL);
                        rsTmp1.first();
                        if(rsTmp1.getRow() > 0 ) {
                            while(!rsTmp1.isAfterLast()) {
                                if(rsTmp1.getString("MAIN_ACCOUNT_CODE").trim().equals("127176")) {
                                    RemarkTDS = "TDS ON Rs. " + rsTmp1.getString("APPLICABLE_AMOUNT") + " @ " + rsTmp1.getString("PERCENTAGE");
                                } else if(rsTmp1.getString("MAIN_ACCOUNT_CODE").equals("127035")) {
                                    RemarkESI = "ESI ON Rs. " + rsTmp1.getString("APPLICABLE_AMOUNT") + " @ " + rsTmp1.getString("PERCENTAGE");
                                }
                                rsTmp1.next();
                            }
                        }
                        objRow.setValue("REMARK_TDS",RemarkTDS);
                        objRow.setValue("REMARK_ESI",RemarkESI);
                        //================================================
                        
                        objData.AddRow(objRow);
                    }
                    
                    rsTmp.next();
                }
            }
            // ======================================================================================//
            
        }
        catch(Exception e) {
            
        }
        
        return objData;
    }
    
    
    
    
    public static TTable getCashVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CHEQUE_NO");
        objData.AddColumn("CHEQUE_DATE");
        objData.AddColumn("CHEQUE_AMOUNT");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("SUB_ACCOUNT_CODE");
        objData.AddColumn("ACCOUNT_NAME");
        objData.AddColumn("DR_AMOUNT");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("ST_CATEGORY");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("ACCOUNT_CODE");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        
        try {
            
            String strSQL="";
            String PartyName="";
            String PartyMainAccountCode="";
            ResultSet rsVoucher;
            
            strSQL="SELECT *,A.REMARKS AS HEADER_REMARKS FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B ";//_EX
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
            strSQL+="AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='D' AND IS_DEDUCTION<>1";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    double dAmount = data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND REF_SR_NO="+rsVoucher.getInt("SR_NO")+" AND EFFECT='"+rsVoucher.getString("EFFECT")+"' ",FinanceGlobal.FinURL);
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    objRow.setValue("DR_AMOUNT","0");
                    objRow.setValue("CR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT") + dAmount));
                    objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                    objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
            }
            
            strSQL="SELECT *,A.REMARKS AS HEADER_REMARKS FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B "; //_EX
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
            strSQL+="AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' AND IS_DEDUCTION=1";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE",rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("CHEQUE_NO",rsVoucher.getString("CHEQUE_NO"));
                    objRow.setValue("CHEQUE_DATE",rsVoucher.getString("CHEQUE_DATE"));
                    objRow.setValue("CHEQUE_AMOUNT",Double.toString(rsVoucher.getDouble("CHEQUE_AMOUNT")));
                    objRow.setValue("MAIN_ACCOUNT_CODE",rsVoucher.getString("MAIN_ACCOUNT_CODE"));
                    objRow.setValue("SUB_ACCOUNT_CODE",rsVoucher.getString("SUB_ACCOUNT_CODE"));
                    objRow.setValue("ACCOUNT_NAME",clsPartyMaster.getAccountName(rsVoucher.getString("MAIN_ACCOUNT_CODE"),rsVoucher.getString("SUB_ACCOUNT_CODE")));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("AMOUNT")));
                    objRow.setValue("CR_AMOUNT","0");
                    objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                    objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    public static TTable getDebitVoucherReport(String VoucherNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("BOOK_CODE");
        objData.AddColumn("BOOK_NAME");
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("CR_AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("PO_DATE");
        objData.AddColumn("INVOICE_NO");
        objData.AddColumn("INVOICE_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("REMARKS");
        objData.AddColumn("PARTY_NAME");
        objData.AddColumn("FULL_ACCOUNT_NAME");
        objData.AddColumn("PARTY_MAIN_ACCOUNT_CODE");
        objData.AddColumn("TOTAL_DEBIT_AMOUNT");
        
        try {
            
            String strSQL="";
            String FullAccountName="";
            String PartyName="";
            ResultSet rsVoucher;
            ResultSet rsParty;
            
            rsParty = data.getResult("SELECT SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO ='"+VoucherNo+"' AND EFFECT = 'D' GROUP BY SUB_ACCOUNT_CODE,MAIN_ACCOUNT_CODE LIMIT 1",FinanceGlobal.FinURL);
            PartyName=clsPartyMaster.getAccountName(rsParty.getString("MAIN_ACCOUNT_CODE"),rsParty.getString("SUB_ACCOUNT_CODE"));
            FullAccountName = clsPartyMaster.getAccountNameAddress(rsParty.getString("MAIN_ACCOUNT_CODE"),rsParty.getString("SUB_ACCOUNT_CODE"));
            double TotalDebitAmount=data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO ='"+VoucherNo+"' AND EFFECT = 'D' ",FinanceGlobal.FinURL);
            
            strSQL+=" SELECT A.REMARKS AS HEADER_REMARKS,A.CHEQUE_NO,A.CHEQUE_DATE,A.CHEQUE_AMOUNT,B.EFFECT,B.REMARKS AS SUB_REMARKS,A.VOUCHER_NO,A.VOUCHER_DATE, IF(B.SUB_ACCOUNT_CODE<>'',B.SUB_ACCOUNT_CODE,B.MAIN_ACCOUNT_CODE) AS ACCOUNT_CODE, IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) AS ACCOUNT_NAME,IF(EFFECT='C',AMOUNT,0) AS CR_AMOUNT,IF(EFFECT='D',AMOUNT,0) AS DR_AMOUNT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,A.BOOK_CODE,A.ST_CATEGORY, IF(EX.EX_ACCOUNT_NAME IS NULL, IF(IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME) IS NULL,'',IF(B.SUB_ACCOUNT_CODE<>'',PARTY.PARTY_NAME,GL.ACCOUNT_NAME)) ,EX_ACCOUNT_NAME) AS ACCOUNT_NAME_EX,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,BOOK.BOOK_NAME ";
            strSQL+=" FROM ";
            strSQL+=" D_FIN_VOUCHER_HEADER A ";
            strSQL+=" LEFT JOIN D_FIN_BOOK_MASTER BOOK ON (BOOK.BOOK_CODE=A.BOOK_CODE), ";
            strSQL+=" D_FIN_VOUCHER_DETAIL B ";
            strSQL+=" LEFT JOIN D_FIN_GL AS GL ON (GL.MAIN_ACCOUNT_CODE=B.MAIN_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_PARTY_MASTER AS PARTY ON (PARTY.PARTY_CODE=B.SUB_ACCOUNT_CODE) ";
            strSQL+=" LEFT JOIN D_FIN_EX_ACCOUNT_MASTER AS EX ON ( SUBSTRING(B.MAIN_ACCOUNT_CODE,LENGTH(B.MAIN_ACCOUNT_CODE)-1)=EX.EX_ACCOUNT_CODE) WHERE  A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND EFFECT ='D' GROUP BY VOUCHER_NO,SR_NO ORDER BY VOUCHER_NO,SR_NO";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("COMPANY_NAME", clsCompany.getCompanyName(EITLERPGLOBAL.gCompanyID));
                    objRow.setValue("BOOK_CODE", rsVoucher.getString("BOOK_CODE"));
                    objRow.setValue("BOOK_NAME", clsBook.getBookName(EITLERPGLOBAL.gCompanyID,rsVoucher.getString("BOOK_CODE")));
                    objRow.setValue("VOUCHER_NO", VoucherNo);
                    objRow.setValue("VOUCHER_DATE",rsVoucher.getString("VOUCHER_DATE"));
                    objRow.setValue("DR_AMOUNT",Double.toString(rsVoucher.getDouble("DR_AMOUNT")));
                    objRow.setValue("CR_AMOUNT","0");
                    objRow.setValue("PO_NO",rsVoucher.getString("PO_NO"));
                    objRow.setValue("PO_DATE",rsVoucher.getString("PO_DATE"));
                    objRow.setValue("INVOICE_NO",rsVoucher.getString("INVOICE_NO"));
                    objRow.setValue("INVOICE_DATE",rsVoucher.getString("INVOICE_DATE"));
                    objRow.setValue("HEADER_REMARKS",rsVoucher.getString("HEADER_REMARKS").trim());
                    objRow.setValue("REMARKS",rsVoucher.getString("SUB_REMARKS").trim());
                    objRow.setValue("PARTY_NAME",PartyName);
                    objRow.setValue("FULL_ACCOUNT_NAME",FullAccountName);
                    objRow.setValue("TOTAL_DEBIT_AMOUNT",Double.toString(TotalDebitAmount));
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
                
                
                
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
}
