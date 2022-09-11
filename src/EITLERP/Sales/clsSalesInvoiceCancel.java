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

public class clsSalesInvoiceCancel {
    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
    /** Creates a new instance of clsSalesInvoiceDueDateImport */
    public clsSalesInvoiceCancel() {
        
    }
    
    public static void main(String[] args) {
        String FileName = "";
        if(args.length<1) {
            System.out.println("Insufficient arguments. \nPlease specify Line sequential file name.");
            return;
        }
        
        clsSalesInvoiceCancel objImport=new clsSalesInvoiceCancel();
        FileName = args[0];
        //FileName = "/root/erpcaninv.txt";
        System.out.println(FileName);
        objImport.ImportCancelInvoices(FileName);
    }
    
    public void ImportCancelInvoices(String invoiceFile) {
        boolean Done=false;
        
        int InvoiceType=0;
        String InvoiceNo = "",InvoiceDate="",PartyCode="",PadChar="";
        BufferedReader aFile=null;
        try {
            aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            Connection objConn=data.getConn(dbURL);
            Statement stCanInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsCanInvoice=stCanInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_CANCEL LIMIT 1");
            int Pointer=0;
            int Counter=0;
            while(!Done) {
                String FileRecord=aFile.readLine();
                Pointer=0;
                Counter++;
                System.out.println("Counter : " + Counter);
                InvoiceType = Integer.parseInt(FileRecord.substring(Pointer,Pointer+1)); 
                Pointer+=1;
                if(InvoiceType==2) {
                    PadChar = "F";
                } else if(InvoiceType==3) {
                    PadChar = "B";
                } else {
                    PadChar = "";
                }
                InvoiceNo = PadChar + FileRecord.substring(Pointer,Pointer+6);  
                Pointer+=6;
                InvoiceDate = FileRecord.substring(Pointer+4,Pointer+8)+ "-" + FileRecord.substring(Pointer+2,Pointer+4)+ "-" + FileRecord.substring(Pointer,Pointer+2);
                Pointer+=8;
                PartyCode = FileRecord.substring(Pointer,Pointer+6);Pointer+=6;
                if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE="+InvoiceType+" AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' AND PARTY_CODE='"+PartyCode+"' ")) {
                    continue;
                }
                long SrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_INVOICE_CANCEL") + 1;
                rsCanInvoice.moveToInsertRow();
                rsCanInvoice.updateLong("SR_NO", SrNo);
                rsCanInvoice.updateInt("INVOICE_TYPE",InvoiceType);
                rsCanInvoice.updateString("INVOICE_NO",InvoiceNo);
                rsCanInvoice.updateString("INVOICE_DATE",InvoiceDate);
                rsCanInvoice.updateString("PARTY_CODE",PartyCode);
                rsCanInvoice.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsCanInvoice.insertRow();
            }
            aFile.close();
        } catch(Exception e) {
//            try {
//                aFile.close();
//            } catch(Exception ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
            Done=true;
        }
        System.out.println("*********** Finished ***********");
    }
}
