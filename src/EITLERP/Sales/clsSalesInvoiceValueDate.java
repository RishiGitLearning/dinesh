/*
 * clsSalesInvoiceDueDateImport.java
 *
 * Created on May 15, 2008, 4:03 PM
 */

package EITLERP.Sales;

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

public class clsSalesInvoiceValueDate {
    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
    /** Creates a new instance of clsSalesInvoiceDueDateImport */
    public clsSalesInvoiceValueDate() {
        
    }
    
    public static void main(String[] args) {
        String Type="2";
        
        String FileName="/root/Desktop/BPAY_F28.TXT";
        
        clsSalesInvoiceValueDate objImport=new clsSalesInvoiceValueDate();
        if(Type.equals("1")) {
            objImport.ImportInvoicesSuiting(FileName);
        } else {
            objImport.ImportInvoicesFelt(FileName);
        }
    }
    
    public void ImportInvoicesSuiting(String invoiceFile) {
        boolean Done=false;
        int Counter=0;
        
        int Pointer=0;
        int VoucherFound =0;
        int VoucherNotFound =0;
        int InvoiceNotFound =0;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            ResultSet rsData = null;
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo ="", AgentAlpha = "",Yr1Yr2 = "",PartyCode = "",PartyAgentCode,InvoiceDate = "",ValueDate = "",
                    StartDate="",EndDate="";
                    int AgentSrNo = 0;
                    double Amount = 0,ReceiptAmount=0;
                    
                    AgentAlpha = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                    PartyCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                    AgentSrNo = Integer.parseInt(FileRecord.substring(Pointer,Pointer+6));Pointer+=6;
                    
                    Yr1Yr2 = FileRecord.substring(Pointer,Pointer+4);Pointer+=4;
                    StartDate = "20"+Yr1Yr2.substring(0,2)+"-04-01";
                    EndDate = "20"+Yr1Yr2.substring(2,4)+"-03-31";
                    
                    ValueDate = FileRecord.substring(Pointer,Pointer+8);Pointer+=8;
                    ValueDate = ValueDate.substring(0,4)+"-"+ValueDate.substring(4,6)+"-"+ValueDate.substring(6,8);
                    Pointer+=6;
                    
                    ReceiptAmount = Double.parseDouble(FileRecord.substring(Pointer,Pointer+8));Pointer+=8;
                    Pointer+=3;
                    
                    String Qry ="SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                    "AND AGENT_SR_NO='"+Integer.toString(AgentSrNo)+"' AND INVOICE_DATE>='"+StartDate+"' AND INVOICE_DATE<='"+EndDate+"' " +
                    "AND INVOICE_TYPE=1 AND APPROVED=1 AND CANCELLED=0";
                    Counter++;
                    
                    if(data.IsRecordExist(Qry, dbURL)) {
                        InvoiceNo = data.getStringValueFromDB(Qry, dbURL);
                        Qry ="SELECT INVOICE_DATE FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                        "AND AGENT_SR_NO='"+Integer.toString(AgentSrNo)+"' AND INVOICE_DATE>='"+StartDate+"' AND INVOICE_DATE<='"+EndDate+"' " +
                        "AND INVOICE_TYPE=1 AND APPROVED=1 AND CANCELLED=0";
                        InvoiceDate = data.getStringValueFromDB(Qry, dbURL);
                        
                        Qry = "SELECT A.VOUCHER_NO,A.VOUCHER_DATE,B.VALUE_DATE,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='210027' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.AMOUNT="+ReceiptAmount;
                        rsData = data.getResult(Qry,FinanceGlobal.FinURL);
                        String VoucherNo = "";
                        String VoucherDate = "";
                        String VoucherValueDate = "";
                        rsData.first();
                        rsData.last();
                        int Count = rsData.getRow();
                        rsData.first();
                        if(Count>1) {
                            System.out.println("Count = " + Count + " Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " AgentAlpha : " + AgentAlpha + " Voucher Date : " + VoucherDate + " PartyCode : " + PartyCode + " AgentSrNo : " + AgentSrNo + " Dbn. Sys. Value Date : " + ValueDate);
                            continue;
                        } else if(Count<=0) {
                            System.out.println("Count = " + Count + " Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " AgentAlpha : " + AgentAlpha + " Voucher Date : " + VoucherDate + " PartyCode : " + PartyCode + " AgentSrNo : " + AgentSrNo + " Dbn. Sys. Value Date : " + ValueDate);
                            continue;
                        }
                        rsData.first();
                        if(rsData.getRow() > 0) {
                            while(!rsData.isAfterLast()) {
                                VoucherNo = UtilFunctions.getString(rsData, "VOUCHER_NO", "");
                                VoucherDate = UtilFunctions.getString(rsData, "VOUCHER_DATE", "");
                                VoucherValueDate = UtilFunctions.getString(rsData, "VALUE_DATE", "");
                                VoucherFound++;
                                if(InvoiceNo.equals("") || InvoiceNo.equals("0") || InvoiceNo.equals("1")) {
                                    boolean halt=true;
                                }
                                //System.out.println("Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " Voucher No : " + VoucherNo + " Voucher Date : " + VoucherDate + " Voucher Value Date : " + VoucherValueDate + " Dbn. Sys. Value Date : " + ValueDate);
                                if(VoucherValueDate.equals("0000-00-00") || VoucherValueDate.equals("") || java.sql.Date.valueOf(VoucherValueDate).compareTo(java.sql.Date.valueOf(ValueDate))==0) {
                                    String SQL = "UPDATE D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B SET B.INVOICE_NO='"+InvoiceNo+"', " +
                                    "B.INVOICE_DATE='"+InvoiceDate+"', A.CHANGED=1, B.VALUE_DATE='"+ValueDate+"', B.GRN_NO='"+InvoiceNo+"', " +
                                    "B.GRN_DATE='"+InvoiceDate+"', B.MODULE_ID=80 " +
                                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' " +
                                    "AND B.MAIN_ACCOUNT_CODE='210027' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.AMOUNT="+ReceiptAmount;
                                    data.Execute(SQL,FinanceGlobal.FinURL);
                                } else {
                                    System.out.println(" Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " Voucher No : " + VoucherNo + " Voucher Date : " + VoucherDate + " Voucher Value Date : " + VoucherValueDate + " Dbn. Sys. Value Date : " + ValueDate);
                                }
                                
                                rsData.next();
                            }
                        }
                    } else {
                        InvoiceNotFound++;
                        System.out.println("Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " Invoice No Not Found.");
                    }
                } catch(Exception c){
                    Done=true;
                }
            }
            System.out.println("Voucher Found : " + VoucherFound + " Voucher Not Found : " + VoucherNotFound + " Invoice Not Found : " + InvoiceNotFound);
            System.out.println("*********** Finished ***********");
        } catch(Exception e) {
        }
    }
    public void ImportInvoicesFelt(String invoiceFile) {
        boolean Done=false;
        int Counter=0;
        
        int Pointer=0;
        int VoucherFound =0;
        int VoucherNotFound =0;
        int InvoiceNotFound =0;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            ResultSet rsData = null;
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo ="", AgentAlpha = "",Yr1Yr2 = "",PartyCode = "",PartyAgentCode,InvoiceDate = "",ValueDate = "",StartDate="",EndDate="";
                    int AgentSrNo = 0;
                    double Amount = 0,ReceiptAmount=0;
                    
                    AgentAlpha = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                    PartyCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                    AgentSrNo = Integer.parseInt(FileRecord.substring(Pointer,Pointer+6));Pointer+=6;
                    InvoiceNo = AgentAlpha.trim()+EITLERPGLOBAL.padLeftEx(Integer.toString(AgentSrNo), "0", 6);
                    Yr1Yr2 = FileRecord.substring(Pointer,Pointer+4);Pointer+=4;
                    StartDate = "20"+Yr1Yr2.substring(0,2)+"-04-01";
                    EndDate = "20"+Yr1Yr2.substring(2,4)+"-03-31";
                    
                    ValueDate = FileRecord.substring(Pointer,Pointer+8);Pointer+=8;
                    ValueDate = ValueDate.substring(0,4)+"-"+ValueDate.substring(4,6)+"-"+ValueDate.substring(6,8);
                    Pointer+=6;
                    
                    ReceiptAmount = Double.parseDouble(FileRecord.substring(Pointer,Pointer+8));Pointer+=8;
                    Pointer+=3;
                    
                    String Qry ="SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                    "AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE>='"+StartDate+"' AND INVOICE_DATE<='"+EndDate+"' " +
                    "AND INVOICE_TYPE=2 AND APPROVED=1 AND CANCELLED=0";
                    Counter++;
                    
                    if(data.IsRecordExist(Qry, dbURL)) {
                        InvoiceNo = data.getStringValueFromDB(Qry, dbURL);
                        Qry ="SELECT INVOICE_DATE FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                        "AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE>='"+StartDate+"' AND INVOICE_DATE<='"+EndDate+"' " +
                        "AND INVOICE_TYPE=2 AND APPROVED=1 AND CANCELLED=0";
                        InvoiceDate = data.getStringValueFromDB(Qry, dbURL);
                        
                        Qry = "SELECT A.VOUCHER_NO,A.VOUCHER_DATE,B.VALUE_DATE,B.AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='210010' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND B.INVOICE_NO='"+InvoiceNo+"' AND B.EFFECT='C' AND A.APPROVED=1 AND A.CANCELLED=0 AND B.AMOUNT="+ReceiptAmount;
                        rsData = data.getResult(Qry,FinanceGlobal.FinURL);
                        String VoucherNo = "";
                        String VoucherDate = "";
                        String VoucherValueDate = "";
                        rsData.first();
                        rsData.last();
                        int Count = rsData.getRow();
                        rsData.first();
                        if(Count>1) {
                            System.out.println("Count = " + Count + " Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " AgentAlpha : " + AgentAlpha + " Voucher Date : " + VoucherDate + " PartyCode : " + PartyCode + " AgentSrNo : " + AgentSrNo + " Dbn. Sys. Value Date : " + ValueDate);
                            continue;
                        } else if(Count<=0) {
                            System.out.println("Count = " + Count + " Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " AgentAlpha : " + AgentAlpha + " Voucher Date : " + VoucherDate + " PartyCode : " + PartyCode + " AgentSrNo : " + AgentSrNo + " Dbn. Sys. Value Date : " + ValueDate);
                            continue;
                        }
                        rsData.first();
                        
                        if(rsData.getRow() > 0) {
                            while(!rsData.isAfterLast()) {
                                VoucherNo = UtilFunctions.getString(rsData, "VOUCHER_NO", "");
                                VoucherDate = UtilFunctions.getString(rsData, "VOUCHER_DATE", "");
                                VoucherValueDate = UtilFunctions.getString(rsData, "VALUE_DATE", "");
                                VoucherFound++;
                                if(InvoiceNo.equals("") || InvoiceNo.equals("0") || InvoiceNo.equals("1")) {
                                    boolean halt=true;
                                }
                                if(VoucherValueDate.equals("0000-00-00") || VoucherValueDate.equals("") || java.sql.Date.valueOf(VoucherValueDate).compareTo(java.sql.Date.valueOf(ValueDate))==0) {
                                    String SQL = "UPDATE D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B SET B.INVOICE_NO='"+InvoiceNo+"', " +
                                    "B.INVOICE_DATE='"+InvoiceDate+"', A.CHANGED=1, B.VALUE_DATE='"+ValueDate+"', B.GRN_NO='"+InvoiceNo+"', " +
                                    "B.GRN_DATE='"+InvoiceDate+"', B.MODULE_ID=80 " +
                                    "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' AND B.EFFECT='C' " +
                                    "AND B.MAIN_ACCOUNT_CODE='210010' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.AMOUNT="+ReceiptAmount;
                                    data.Execute(SQL,FinanceGlobal.FinURL);
                                } else {
                                    System.out.println("== Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " Voucher No : " + VoucherNo + " Voucher Date : " + VoucherDate + " Voucher Value Date : " + VoucherValueDate + " Dbn. Sys. Value Date : " + ValueDate);
                                }
                                rsData.next();
                            }
                        }
                    } else {
                        InvoiceNotFound++;
                        System.out.println("Counter : " + EITLERPGLOBAL.padLeftEx(Integer.toString(Counter), "0", 3) + " Invoice No Not Found.");
                    }
                } catch(Exception c){
                    Done=true;
                }
            }
            System.out.println("Voucher Found : " + VoucherFound + " Voucher Not Found : " + VoucherNotFound + " Invoice Not Found : " + InvoiceNotFound);
            System.out.println("*********** Finished ***********");
        } catch(Exception e) {
        }
    }
}
