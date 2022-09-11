/*
 * clsServer.java
 *
 * Created on September 26, 2011, 12:34 PM
 */

package EITLERP.Finance;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import EITLERP.*;
import java.math.BigDecimal;
import EITLERP.Sales.clsSalesInvoice;



/**
 *
 * @author  root
 */
public class clsServer {
    static final int serverPort = 50000;
    static final int packetSize = 1024;
    
    /** Creates a new instance of clsServer */
    public clsServer() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    throws SocketException{
        
        DatagramPacket packet;
        DatagramSocket socket;
        byte[] data;    // For data to be  Sent in packets
        int clientPort;
        InetAddress address;
        String str;
        
        socket = new DatagramSocket(serverPort);
        
        for(;;){
            data = new byte[packetSize];
            
            // Create packets to receive the message
            
            packet = new DatagramPacket(data,packetSize);
            System.out.println("JAVA SERVER STARTED: \nLISTENING PORT:50000");
            System.out.println("\nWaiting to receive the packets from user.....");
            try{
                
                // wait infinetely for arrive of the packet
                socket.receive(packet);
                
            }catch(IOException ie) {
                System.out.println(" Could not Receive  :"+ie.getMessage());
                System.exit(0);
            }
            
            // get data about client in order to  echo data back
            
            address = packet.getAddress();
            clientPort = packet.getPort();
            
            // print string that was received  on server's console
            str = new String(data,0,0,packet.getLength());
            System.out.println("Message  :"+ str.trim());
            System.out.println("From   :"+address);
            
            // echo data back to the client
            // Create packets to send to the client
            clsServer objServer = new clsServer();
            objServer.createFile(Integer.parseInt(str.trim()));
            objServer.TransferFile(Integer.parseInt(str.trim()));
            
            packet = new DatagramPacket(data,packetSize,address,clientPort);
            //packet = new DatagramPacket("Done...".getBytes(),packetSize,address,clientPort);
            
            try {
                // sends packet
                
                socket.send(packet);
                
            }
            catch(IOException ex) {
                System.out.println("Could not Send   "+ex.getMessage());
                System.exit(0);
            }
            
        } // for loop
        
    } // main
    
    private void createFile(int InvoiceType) {
        try {
            System.out.println("InvoiceType : " + InvoiceType);
            double MainBalance=0, SubBalance=0;
            String GroupMainParty="", GroupSubParty="",Record="";
            String InvoiceNo="",InvoiceDate="",strMainBalance="",strBalance="",strDecimal="",MainCode="";
            ResultSet rsMainParty = null,rsSubParty = null;
            //String FileName = "/data/Balance_Transfer_Cobol/"+EITLERPGLOBAL.getCurrentDateDB().substring(8,10)+
            //EITLERPGLOBAL.getCurrentDateDB().substring(5,7)+EITLERPGLOBAL.getCurrentDateDB().substring(2,4);
            String FileName = "/data/Balance_Transfer_Cobol/ost";
            HashMap List = new HashMap();
            if(InvoiceType==1) {
                MainCode = "210027";
                FileName = FileName+"s.txt";
            } else if(InvoiceType==2) {
                MainCode = "210010";
                FileName = FileName+"f.txt";
            }
            System.out.println("FileName : " + FileName);
            System.out.println("URL : " + FinanceGlobal.FinURL);
            rsMainParty = data.getResult("SELECT GROUP_MAIN_PARTY FROM D_FIN_PARTY_GROUPING_HEADER WHERE INVOICE_TYPE=" +InvoiceType+ " ORDER BY GROUP_MAIN_PARTY",FinanceGlobal.FinURL);
            rsMainParty.first();
            if(rsMainParty.getRow()>0) {
                while(!rsMainParty.isAfterLast()) {
                    Record = "";
                    MainBalance=0;
                    SubBalance=0;
                    GroupMainParty = rsMainParty.getString("GROUP_MAIN_PARTY");
                    //MainBalance=BalanceTransfer(MainCode, GroupMainParty);
                    
                    rsSubParty = data.getResult("SELECT GROUP_SUB_PARTY FROM D_FIN_PARTY_GROUPING_DETAIL WHERE GROUP_MAIN_PARTY='"+GroupMainParty+"' AND INVOICE_TYPE=" +InvoiceType+ " ORDER BY GROUP_SUB_PARTY",FinanceGlobal.FinURL);
                    rsSubParty.first();
                    if(rsSubParty.getRow()>0) {
                        while(!rsSubParty.isAfterLast()) {
                            GroupSubParty = rsSubParty.getString("GROUP_SUB_PARTY");
                            SubBalance = EITLERPGLOBAL.round(SubBalance+BalanceTransfer(MainCode, GroupSubParty),2);
                            System.out.println("GroupSubParty : " + GroupSubParty + " SubBalance : " + SubBalance);
                            rsSubParty.next();
                        }
                    }
                    
                    //strMainBalance = new BigDecimal(MainBalance+SubBalance).setScale(2,BigDecimal.ROUND_HALF_UP).toString();;
                    strMainBalance = new BigDecimal(SubBalance).setScale(2,BigDecimal.ROUND_HALF_UP).toString();//MainBalance+
                    strBalance = strMainBalance.substring(0,strMainBalance.indexOf("."));
                    strDecimal = strMainBalance.substring(strMainBalance.indexOf(".")+1);
                    if(strBalance.length()<10) {
                        strBalance = EITLERPGLOBAL.padLeftEx(strBalance, "0", 10);
                    } else {
                        //                        JOptionPane.showMessageDialog(null,"Party Code : " + GroupMainParty + " " +
                        //                        "\nBalance more then 10+2 format. \n File can not generated." +
                        //                        "\nContact Administrator and EDP head.");
                        return;
                    }
                    
                    if(strDecimal.length()<2) {
                        strDecimal = EITLERPGLOBAL.padRightEx(strDecimal, "0", 2);
                    }
                    
                    Record = GroupMainParty+strBalance+strDecimal+"000000000000";
                    System.out.println(GroupMainParty + " " +strMainBalance);
                    List.put(Integer.toString(List.size()+1), Record);
                    rsMainParty.next();
                }
            }
            
            String PrintLine = "";
            // WRITE TO FILE
            boolean sucess = new File("/data/Balance_Transfer_Cobol/").mkdir();
            if(sucess) {
                //JOptionPane.showMessageDialog(null,"Directory created...");
            }
            //String FileName = "/data/Balance_Transfer_Cobol/"+EITLERPGLOBAL.getCurrentDateDB().substring(8,10)+EITLERPGLOBAL.getCurrentDateDB().substring(5,7)+EITLERPGLOBAL.getCurrentDateDB().substring(0,4)+".txt";
            
            File file = new File(FileName);
            
            boolean exists = file.createNewFile();
            if(!exists) {
                //JOptionPane.showMessageDialog(null,"File Already exists...");
            }
            BufferedWriter aFile=new BufferedWriter(new FileWriter(file));
            
            for(int i=1;i<=List.size();i++) {
                PrintLine = List.get(Integer.toString(i)).toString();
                //System.out.println(PrintLine);
                aFile.write(PrintLine);
                aFile.newLine();
            }
            aFile.close();
            System.out.println("File Created sucessfully...");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private double BalanceTransfer(String MainCode,String PartyCode) {
        String SQL="", FromDate="", ToDate="",InvoiceNo="",InvoiceDate="",BookCode="",ChargeCode="";
        ResultSet rsInvoice=null;
        int InvoiceType = 0,EntryNo=0;
        double TotalBalance =0;
        try {
            if(MainCode.equals("210010")) {
                InvoiceType = 2;
                BookCode = " AND BOOK_CODE IN ('09') "; //,'18'
                //ChargeCode = " AND CHARGE_CODE IN ('02','08') ";
            } else if(MainCode.equals("210027")) {
                InvoiceType = 1;
                BookCode = " AND BOOK_CODE IN ('01') "; //,'12','16'
                //ChargeCode = " AND CHARGE_CODE IN ('02','04','08') ";
            }
            // SET LAST CLOSING DATE & TO DATE
            ToDate=EITLERPGLOBAL.getCurrentDateDB();
            FromDate=EITLERPGLOBAL.FinFromDateDB;
            SQL="SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+ToDate+"' ORDER BY ENTRY_DATE DESC";
            EntryNo=data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
            FromDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
            // ------------------------------
            
            // GET PARTY'S INVOICE NO,INVOICE DATE USING UNION FROM OUTSTANDING DETAIL AND VOUCHER TABLES
            SQL = "(SELECT BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT FROM FINANCE.D_FIN_DR_OPENING_OUTSTANDING_DETAIL " +
            "WHERE INVOICE_TYPE="+InvoiceType+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' AND ENTRY_NO="+EntryNo+" AND EFFECT='D' AND (MATCHED_DATE>'"+ToDate+"' OR MATCHED_DATE='0000-00-00') " + BookCode + " ) " +
            "UNION ALL " +
            "(SELECT A.BOOK_CODE,A.VOUCHER_NO,A.VOUCHER_DATE,A.LEGACY_NO,B.INVOICE_NO,B.INVOICE_DATE,B.LINK_NO,B.AMOUNT,B.EFFECT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B " +
            "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND A.VOUCHER_DATE >'"+FromDate+"' AND A.VOUCHER_DATE <='"+ToDate+"' AND (B.MATCHED_DATE>'"+ToDate+"' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " + BookCode + " ) " +
            "ORDER BY VOUCHER_DATE ";
            
            rsInvoice = data.getResult(SQL,FinanceGlobal.FinURL);
            rsInvoice.first();
            InvoiceNo="";
            InvoiceDate="";
            // ---------------------------------------------------------------------
            
            if(rsInvoice.getRow()>0) {
                while(!rsInvoice.isAfterLast()) {
                    
                    String VoucherNo = UtilFunctions.getString(rsInvoice,"VOUCHER_NO","");
                    InvoiceNo = UtilFunctions.getString(rsInvoice,"INVOICE_NO","");
                    InvoiceDate = UtilFunctions.getString(rsInvoice,"INVOICE_DATE","0000-00-00");
                    double DebitAmount=0;
                    double AdjustedAmount = 0;
                    if(clsVoucher.getVoucherType(VoucherNo)!=FinanceGlobal.TYPE_SALES_JOURNAL) { //&& clsVoucher.getVoucherType(VoucherNo)!=FinanceGlobal.TYPE_DEBIT_NOTE
                        rsInvoice.next();
                        continue;
                    }
                    if(InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if(!clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8")) {
                            rsInvoice.next();
                            continue;
                        }
                    } else if(InvoiceType == 1 && clsVoucher.getVoucherType(VoucherNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if(!clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")) {
                            rsInvoice.next();
                            continue;
                        }
                    }
                    
                    if(!data.IsRecordExist("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE DEBITNOTE_VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL) &&
                    clsVoucher.getVoucherType(VoucherNo)==FinanceGlobal.TYPE_DEBIT_NOTE) {
                        rsInvoice.next();
                        continue;
                    }
                    
                    if(clsVoucher.getVoucherType(VoucherNo)==FinanceGlobal.TYPE_SALES_JOURNAL) {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " +
                        "AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' " +
                        "AND (MATCHED_DATE>'"+ToDate+"' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                        
                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 " +
                        "AND (B.MATCHED_DATE>'"+ToDate+"' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " +
                        "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                        "AND B.INVOICE_DATE='"+InvoiceDate+"' AND B.INVOICE_NO='"+InvoiceNo+"' " ;
                        
                        AdjustedAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    } else {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' " +
                        "AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"' " +
                        "AND (MATCHED_DATE>'"+ToDate+"' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                        
                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A " +
                        "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 " +
                        "AND (B.MATCHED_DATE>'"+ToDate+"' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " +
                        "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='C' " +
                        "AND B.GRN_NO='"+VoucherNo+"' ";
                        AdjustedAmount = data.getDoubleValueFromDB(SQL,FinanceGlobal.FinURL);
                    }
                    if(DebitAmount==AdjustedAmount) {
                        rsInvoice.next();
                        continue;
                    }
                    
                    TotalBalance=EITLERPGLOBAL.round(TotalBalance + EITLERPGLOBAL.round(DebitAmount-AdjustedAmount,2),2);
                    rsInvoice.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return TotalBalance;
        }
        return TotalBalance;
    }
    
    private void TransferFile(int InvoiceType) {
        try {
            if(InvoiceType==2) {
                Process proc = Runtime.getRuntime().exec("sh /script/TranFelt.sh");
                proc.waitFor();
            } else if(InvoiceType==1) {
                Process proc = Runtime.getRuntime().exec("sh /script/TranSuiting.sh");
                proc.waitFor();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
