/*
 * clsSalesInvoiceDueDateImport.java
 *
 * Created on May 15, 2008, 4:03 PM
 */

package EITLERP.Finance.Util;

/**
 *
 * @author  root
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Stores.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import EITLERP.Finance.*;
import EITLERP.Sales.*;
import javax.swing.*;
import java.awt.*;

public class clsSalesAdjustment {
    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
    /** Creates a new instance of clsSalesInvoiceDueDateImport */
    public clsSalesAdjustment() {
        
    }
    
    public static void main(String[] args) {
        
        String FileName="/root/Desktop/FELT.TXT";
        clsSalesAdjustment objImport=new clsSalesAdjustment();
        objImport.AdjVoucher(FileName);
    }
    
    public void AdjVoucher(String VoucherFile) {
        boolean Done=false;
        int Counter=0;
        
        int Pointer=0;
        int VoucherFound =0;
        int VoucherNotFound =0;
        int InvoiceNotFound =0;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(VoucherFile)));
            Done=false;
            ResultSet rsData = null;
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String PartyCode ="", VoucherNo = "",VoucherDate= "",LinkNo="";
                    double Amount = 0;
                    double VoucherAmount = 0;
                    PartyCode = FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                    VoucherNo = FileRecord.substring(Pointer,Pointer+20).trim();Pointer+=20;
                    VoucherDate =  FileRecord.substring(Pointer,Pointer+4)+"-"+FileRecord.substring(Pointer+4,Pointer+6) +"-"+FileRecord.substring(Pointer+6,Pointer+8);Pointer+=8;
                    Amount = Double.parseDouble(FileRecord.substring(Pointer,Pointer+13).trim());
                    LinkNo = FileRecord.substring(Pointer,Pointer+12);
                    int VoucherType = clsVoucher.getVoucherType(VoucherNo);
                    String SQL = "SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"' ";
                    if(data.IsRecordExist(SQL, FinanceGlobal.FinURL)) {
                        SQL = "SELECT COUNT(*) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' " +
                        "AND MAIN_ACCOUNT_CODE='210010' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND INVOICE_NO='' ";
                        int Count = data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                        if(Count > 1) {
                            System.out.println("Voucher No : " + VoucherNo + " Count = " + Count + " Voucher Type : " +VoucherType);
                            
                        } else if(Count==0) {
                            //System.out.println("Voucher No : " + VoucherNo + " Count = " + Count + " Voucher Type : " +VoucherType);
                            continue;
                        } else {
                            //System.out.println("Voucher No : " + VoucherNo + " Count = " + Count + " Voucher Type : " +VoucherType);
                            System.out.println("Voucher No : " + VoucherNo + " : " + " Voucher Type : " +VoucherType);
                            continue;
                        }
                        if(Count!=0) {
                            SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' " +
                            "AND MAIN_ACCOUNT_CODE='210010' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND INVOICE_NO='' ";
                            VoucherAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                            SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " +
                            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.GRN_NO='"+VoucherNo+"' AND B.EFFECT='D' " +
                            "AND B.MAIN_ACCOUNT_CODE='210010' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.CANCELLED=0 ";
                            double CanUseAmount = EITLERPGLOBAL.round(VoucherAmount - data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL),2);
                            if(CanUseAmount < Amount) {
                                System.out.println("Wrong Voucher No : " + VoucherNo + " Count = " + Count + " Voucher Type = " +VoucherType + " Can Used Amount = " + CanUseAmount + " Amount = " + Amount);
                                continue;
                            }
                        }
                        
                        /*if(VoucherType==FinanceGlobal.TYPE_RECEIPT 
                        || VoucherType == FinanceGlobal.TYPE_CASH_RECEIPT_VOUCHER 
                        || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                            if(!AdjAdvReceipt(VoucherNo,Amount,PartyCode,VoucherDate)) {
                                JOptionPane.showMessageDialog(null,VoucherNo +" Not Adjusted.");
                                continue;
                            }
                        }
                        
                        if(VoucherType==FinanceGlobal.TYPE_JOURNAL) {
                            if(!AdjAdvJournal(VoucherNo,Amount,PartyCode,VoucherDate)) {
                                JOptionPane.showMessageDialog(null,VoucherNo +" Not Adjusted.");
                                continue;
                            }
                        }*/
                    } else {
                        System.out.println("Voucher No : " + VoucherNo + " not Found.");
                        continue;
                    }
                } catch(Exception c){
                    Done=true;
                }
            }
            System.out.println("*********** Finished ***********");
        } catch(Exception e) {
        }
    }
    
    private boolean AdjAdvReceipt(String VoucherNo,double Amount,String PartyCode,String VoucherDate) {
        double ReceiptAmount = 0;
        String InvoiceNo="",InvoiceDate="",ValueDate="";
        int InvNo=0;
        try {
            String SQL = "SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' " +
            "AND MAIN_ACCOUNT_CODE='210010' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND INVOICE_NO='' ";
            int VoucherSrNo = data.getIntValueFromDB(SQL, FinanceGlobal.FinURL);
            SQL="SELECT MAX(SUBSTRING(INVOICE_NO,LENGTH('DUMF')+1)) AS MAX_NO FROM D_FIN_VOUCHER_DETAIL " +
            "WHERE INVOICE_NO LIKE 'DUMF%'";
            InvNo= data.getIntValueFromDB(SQL, FinanceGlobal.FinURL)+1;
            InvoiceNo = "DUMF"+EITLERPGLOBAL.padLeftEx(Integer.toString(InvNo), "0", 4);
            InvoiceDate = EITLERPGLOBAL.getCurrentDate();
            ValueDate = EITLERPGLOBAL.formatDate(VoucherDate);
            clsVoucher objReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(2,VoucherNo);
            for(int i=1;i<=objReceiptVoucher.colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(i));
                if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals("210010")&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(PartyCode) && objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                    //This is the item to be adjusted
                    ReceiptAmount=objItem.getAttribute("AMOUNT").getDouble();
                    
                    if(ReceiptAmount>Amount) {
                        //Split this line in two lines
                        objItem.setAttribute("AMOUNT",ReceiptAmount-Amount);
                        objItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objItem.setAttribute("GRN_NO",InvoiceNo);
                        objItem.setAttribute("GRN_DATE",InvoiceDate);
                        objItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            objItem.setAttribute("VALUE_DATE",ValueDate);
                        } else {
                            ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                        }
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        //Find Next Sr. No.
                        int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID=2 AND VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL)+1;
                        
                        //This item to be splitted in two
                        clsVoucherItem objNewItem=new clsVoucherItem();
                        
                        objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                        objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                        objNewItem.setAttribute("SR_NO",NewSrNo);
                        objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("AMOUNT",Amount);
                        objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                        objNewItem.setAttribute("PERCENTAGE",0);
                        objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                        objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                        objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                        objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                        objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                        objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                        objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                        objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        objNewItem.setAttribute("INVOICE_NO","");
                        objNewItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objNewItem.setAttribute("VALUE_DATE",ValueDate);
                        
                        objNewItem.setAttribute("GRN_NO","");
                        objNewItem.setAttribute("GRN_DATE","0000-00-00");
                        objNewItem.setAttribute("MODULE_ID",0);
                        objNewItem.setAttribute("REF_COMPANY_ID",0);
                        objNewItem.setAttribute("INVOICE_AMOUNT",0);
                        objNewItem.setAttribute("COMPANY_ID",0);
                        objNewItem.setAttribute("IS_DEDUCTION",0);
                        objNewItem.setAttribute("DEDUCTION_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_NO","");
                        objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objNewItem.setAttribute("REF_SR_NO",0);
                        
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(objReceiptVoucher.colVoucherItems.size()+1), objNewItem);
                        
                        Amount=0;
                        int CreditCount = 0;
                        int DebitCount = 0;
                        clsVoucher objNewReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(2,VoucherNo);
                        for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                            objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                            if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                                CreditCount++;
                                objItem.setAttribute("SR_NO",CreditCount);
                                objNewReceiptVoucher.colVoucherItems.put(Integer.toString(CreditCount), objItem);
                            }
                        }
                        DebitCount=CreditCount;
                        for(int j=1;j<=objReceiptVoucher.colVoucherItems.size();j++) {
                            objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(j));
                            if(objItem.getAttribute("EFFECT").getString().equals("D")) {
                                DebitCount++;
                                objItem.setAttribute("SR_NO",DebitCount);
                                objNewReceiptVoucher.colVoucherItems.put(Integer.toString(DebitCount), objItem);
                            }
                        }
                        objReceiptVoucher = objNewReceiptVoucher;
                        objNewReceiptVoucher=null;
                        break;
                        
                    } else {
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            objItem.setAttribute("VALUE_DATE",ValueDate);
                        }
                        objItem.setAttribute("INVOICE_NO","");
                        objItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objItem.setAttribute("GRN_NO","");
                        objItem.setAttribute("GRN_DATE","0000-00-00");
                        objItem.setAttribute("MODULE_ID",0);
                        objItem.setAttribute("REF_COMPANY_ID",0);
                        objItem.setAttribute("INVOICE_AMOUNT",0);
                        objItem.setAttribute("REF_VOUCHER_NO","");
                        objItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objItem.setAttribute("REF_SR_NO",0);
                        
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        Amount-=ReceiptAmount;
                        break;
                    }
                }
            }
            objReceiptVoucher.UpdateForAdjustment();
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
    private boolean AdjAdvJournal(String VoucherNo,double Amount,String PartyCode,String ValueDate) {
        double ReceiptAmount = 0;
        String InvoiceNo="",InvoiceDate="";
        int InvNo=0;
        try {
            String SQL = "SELECT SR_NO FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' " +
            "AND MAIN_ACCOUNT_CODE='210010' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND EFFECT='C' AND INVOICE_NO='' ";
            int VoucherSrNo = data.getIntValueFromDB(SQL, FinanceGlobal.FinURL);
            SQL="SELECT MAX(SUBSTRING(INVOICE_NO,LENGTH('DUMF')+1)) AS MAX_NO FROM D_FIN_VOUCHER_DETAIL " +
            "WHERE INVOICE_NO LIKE 'DUMF%'";
            InvNo= data.getIntValueFromDB(SQL, FinanceGlobal.FinURL)+1;
            InvoiceNo = "DUMF"+EITLERPGLOBAL.padLeftEx(Integer.toString(InvNo), "0", 4);
            InvoiceDate = EITLERPGLOBAL.getCurrentDate();
            ValueDate = EITLERPGLOBAL.formatDate(ValueDate);
            clsVoucher objReceiptVoucher=(clsVoucher)(new clsVoucher()).getObject(2,VoucherNo);
            for(int i=1;i<=objReceiptVoucher.colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)objReceiptVoucher.colVoucherItems.get(Integer.toString(i));
                if(objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().equals("210010")&&objItem.getAttribute("SUB_ACCOUNT_CODE").getString().equals(PartyCode) && objItem.getAttribute("SR_NO").getInt()==VoucherSrNo) {
                    //This is the item to be adjusted
                    ReceiptAmount=objItem.getAttribute("AMOUNT").getDouble();
                    
                    if(ReceiptAmount>Amount) {
                        //Split this line in two lines
                        objItem.setAttribute("AMOUNT",ReceiptAmount-Amount);
                        objItem.setAttribute("INVOICE_NO",InvoiceNo);
                        objItem.setAttribute("INVOICE_DATE",InvoiceDate);
                        objItem.setAttribute("GRN_NO",InvoiceNo);
                        objItem.setAttribute("GRN_DATE",InvoiceDate);
                        objItem.setAttribute("MODULE_ID",clsSalesInvoice.ModuleID);
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            objItem.setAttribute("VALUE_DATE",ValueDate);
                        } else {
                            ValueDate = objItem.getAttribute("VALUE_DATE").getString();
                        }
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        //Find Next Sr. No.
                        int NewSrNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID=2 AND VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL)+1;
                        
                        //This item to be splitted in two
                        clsVoucherItem objNewItem=new clsVoucherItem();
                        
                        objNewItem.setAttribute("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                        objNewItem.setAttribute("VOUCHER_NO",objItem.getAttribute("VOUCHER_NO").getString());
                        objNewItem.setAttribute("SR_NO",NewSrNo);
                        objNewItem.setAttribute("EFFECT",objItem.getAttribute("EFFECT").getString());
                        objNewItem.setAttribute("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                        objNewItem.setAttribute("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                        objNewItem.setAttribute("AMOUNT",Amount);
                        objNewItem.setAttribute("APPLICABLE_AMOUNT",0);
                        objNewItem.setAttribute("PERCENTAGE",0);
                        objNewItem.setAttribute("REMARKS",objItem.getAttribute("REMARKS").getString());
                        objNewItem.setAttribute("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                        objNewItem.setAttribute("CREATED_DATE",objItem.getAttribute("CREATED_DATE").getString());
                        objNewItem.setAttribute("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                        objNewItem.setAttribute("MODIFIED_DATE",objItem.getAttribute("MODIFIED_DATE").getString());
                        objNewItem.setAttribute("CANCELLED",objItem.getAttribute("CANCELLED").getInt());
                        objNewItem.setAttribute("CHANGED",objItem.getAttribute("CHANGED").getInt());
                        objNewItem.setAttribute("CHANGED_DATE",objItem.getAttribute("CHANGED_DATE").getString());
                        objNewItem.setAttribute("PO_NO",objItem.getAttribute("PO_NO").getString());
                        objNewItem.setAttribute("PO_DATE",objItem.getAttribute("PO_DATE").getString());
                        objNewItem.setAttribute("INVOICE_NO","");
                        objNewItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objNewItem.setAttribute("VALUE_DATE",ValueDate);
                        
                        objNewItem.setAttribute("GRN_NO","");
                        objNewItem.setAttribute("GRN_DATE","0000-00-00");
                        objNewItem.setAttribute("MODULE_ID",0);
                        objNewItem.setAttribute("REF_COMPANY_ID",0);
                        objNewItem.setAttribute("INVOICE_AMOUNT",0);
                        objNewItem.setAttribute("COMPANY_ID",0);
                        objNewItem.setAttribute("IS_DEDUCTION",0);
                        objNewItem.setAttribute("DEDUCTION_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_NO","");
                        objNewItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objNewItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objNewItem.setAttribute("REF_SR_NO",0);
                        
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(objReceiptVoucher.colVoucherItems.size()+1), objNewItem);
                        break;
                        
                    } else {
                        if(objItem.getAttribute("VALUE_DATE").getString().equals("")) {
                            objItem.setAttribute("VALUE_DATE",ValueDate);
                        }
                        objItem.setAttribute("INVOICE_NO","");
                        objItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objItem.setAttribute("GRN_NO","");
                        objItem.setAttribute("GRN_DATE","0000-00-00");
                        objItem.setAttribute("MODULE_ID",0);
                        objItem.setAttribute("REF_COMPANY_ID",0);
                        objItem.setAttribute("INVOICE_AMOUNT",0);
                        objItem.setAttribute("REF_VOUCHER_NO","");
                        objItem.setAttribute("REF_VOUCHER_TYPE",0);
                        objItem.setAttribute("REF_VOUCHER_COMPANY_ID",0);
                        objItem.setAttribute("REF_SR_NO",0);
                        
                        objReceiptVoucher.colVoucherItems.put(Integer.toString(i),objItem);
                        
                        Amount-=ReceiptAmount;
                        break;
                    }
                }
            }
            objReceiptVoucher.UpdateForAdjustment();
        } catch(Exception e) {
            return false;
        }
        return true;
    }
}
