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

public class clsSalesInvoiceDueDateImport {
    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
    /** Creates a new instance of clsSalesInvoiceDueDateImport */
    public clsSalesInvoiceDueDateImport() {
        
    }
    
    public static void main(String[] args) {
        //0 - Invoice Type
        //1 - FileName
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt)  \n2. Line sequential file name ");
            return;
        }
        
        String Type=args[0];
        String FileName=args[1];
        
        /*String Type="2";
        String FileName="/root/Desktop/chinv18.txt";*/
        
        clsSalesInvoiceDueDateImport objImport=new clsSalesInvoiceDueDateImport();
        
        if(Type.equals("1")) {
            objImport.ImportInvoicesSuiting(FileName);
        }
        
        if(Type.equals("2")) {
            objImport.ImportInvoicesFelt(FileName);
        }
    }
    
    public void ImportInvoicesSuiting(String invoiceFile) {
        boolean Done=false;
        long Counter=0;
        
        int Pointer=0;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo ="", AgentAlpha = "",Yr1Yr2 = "",PartyCode = "",PartyAgentCode,InvoiceDate = "",DueDate = "";
                    int AgentSrNo = 0;
                    double Amount = 0;
                    
                    AgentAlpha = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                    AgentSrNo = Integer.parseInt(FileRecord.substring(Pointer,Pointer+6));Pointer+=6;
                    Yr1Yr2 = FileRecord.substring(Pointer,Pointer+4);Pointer+=4;
                    PartyCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                    InvoiceDate = FileRecord.substring(Pointer,Pointer+4)+"-"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+6,Pointer+8);
                    Pointer+=8;
                    DueDate = FileRecord.substring(Pointer,Pointer+4)+"-"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+6,Pointer+8);
                    Pointer+=8;
                    Amount = EITLERPGLOBAL.round(Double.parseDouble(FileRecord.substring(Pointer,Pointer+11).replace(' ','0')),2);
                    Pointer+=11;
                    
                    String Qry ="SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                    "AND AGENT_SR_NO='"+Integer.toString(AgentSrNo)+"' AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_TYPE=1 AND APPROVED=1 AND CANCELLED=0";
                    Counter++;
                    if(data.IsRecordExist(Qry, dbURL)) {
                        InvoiceNo = data.getStringValueFromDB(Qry, dbURL);
                        Qry = "UPDATE D_SAL_INVOICE_HEADER SET DUE_DATE='"+DueDate+"' " +
                        "WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ";
                        data.Execute(Qry,dbURL);
                        System.out.println("Counter : " + Counter + " Party Code : " + PartyCode + " Agent Sr. No. : " + AgentSrNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Due Date : "+EITLERPGLOBAL.formatDate(DueDate) +" posted.");
                    } else {
                        System.out.println("Counter : " + Counter + " Party Code : " + PartyCode + " Agent Sr. No. : " + AgentSrNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Due Date not posted.");
                    }
                } catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            System.out.println("*********** Finished ***********");
        } catch(Exception e) {
        }
    }
    
    public void ImportInvoicesFelt(String invoiceFile) {
        
        boolean Done=false;
        long Counter=0;
        
        int Pointer=0;
        try {
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo ="", AgentAlpha = "",Yr1Yr2 = "",PartyCode = "",PartyAgentCode,InvoiceDate = "",DueDate = "";
                    String AgentSrNo = "";
                    double Amount = 0;
                    
                    AgentAlpha = FileRecord.substring(Pointer,Pointer+2); Pointer+=2;
                    AgentSrNo = "F"+FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                    Yr1Yr2 = FileRecord.substring(Pointer,Pointer+4);Pointer+=4;
                    PartyCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                    InvoiceDate = FileRecord.substring(Pointer,Pointer+4)+"-"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+6,Pointer+8);
                    Pointer+=8;
                    DueDate = FileRecord.substring(Pointer,Pointer+4)+"-"+FileRecord.substring(Pointer+4,Pointer+6)+"-"+FileRecord.substring(Pointer+6,Pointer+8);
                    Pointer+=8;
                    
                    Amount = EITLERPGLOBAL.round(Double.parseDouble(FileRecord.substring(Pointer,Pointer+11).replace(' ','0')),2);
                    Pointer+=11;
                    
                    String Qry ="SELECT INVOICE_NO FROM D_SAL_INVOICE_HEADER WHERE PARTY_CODE='"+PartyCode+"' " +
                    "AND INVOICE_NO='"+AgentSrNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND INVOICE_TYPE=2 AND APPROVED=1 AND CANCELLED=0";
                    Counter++;
                    if(data.IsRecordExist(Qry, dbURL)) {
                        //InvoiceNo = data.getStringValueFromDB(Qry, dbURL);
                        /*Qry = "UPDATE D_SAL_INVOICE_HEADER SET DUE_DATE='"+DueDate+"' " +
                        "WHERE INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ";*/
                        Qry = "UPDATE D_SAL_INVOICE_HEADER SET DUE_DATE='"+DueDate+"' " +
                        "WHERE INVOICE_NO='"+AgentSrNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ";
                        data.Execute(Qry,dbURL);
                        System.out.println("Counter : " + Counter + " Party Code : " + PartyCode + " Agent Sr. No. : " + AgentSrNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Due Date : "+EITLERPGLOBAL.formatDate(DueDate) +" posted.");
                    } else {
                        System.out.println("Counter : " + Counter + " Party Code : " + PartyCode + " Agent Sr. No. : " + AgentSrNo + " Invoice Date : " + EITLERPGLOBAL.formatDate(InvoiceDate) + " Due Date not posted.");
                    }
                } catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            System.out.println("*********** Finished ***********");
        } catch(Exception e) {
        }
    }
}
