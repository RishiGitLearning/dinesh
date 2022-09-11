/*
 * clsVerifyInvoiceHeaderDetail.java
 *
 * Created on September 20, 2010, 3:10 PM
 */

package EITLERP.Sales.Utils;


import EITLERP.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;
/**
 *
 * @author  user
 */
public class clsVerifyInvoiceHeaderDetail {
    
    /** Creates a new instance of clsVerifyInvoiceHeaderDetail */
    public clsVerifyInvoiceHeaderDetail() {
        String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
        ResultSet rs, rsdetail;
        
        //BufferedWriter bw = new BufferedWriter();
        try{
            
            PrintStream out = new PrintStream(new FileOutputStream("/data/transfer/stgqlt11.txt"));
            double grsAmt, netAmt,dtlgrsAmt, dtlnetAmt;
            grsAmt=0;
            netAmt=0;
            
            String StrQry = "SELECT * FROM D_SAL_INVOICE_HEADER WHERE INVOICE_TYPE=1 AND INVOICE_DATE>='2010-04-19' AND INVOICE_DATE <='2010-04-30' AND APPROVED=1 AND CANCELLED=0 ORDER BY INVOICE_DATE";
            rs = data.getResult(StrQry,dbURL);
            String InvoiceNo="";
            int  InvoiceType;
            String InvoiceDate;
            try{
                rs.first();
                while (!rs.isAfterLast()){
                    InvoiceNo   = rs.getString("INVOICE_NO" );
                    InvoiceDate = rs.getString("INVOICE_DATE");
                    InvoiceType = rs.getInt("INVOICE_TYPE");
                    
                    //System.out.println("Invoice No."+InvoiceNo);
                    grsAmt=EITLERPGLOBAL.round(rs.getDouble("TOTAL_GROSS_AMOUNT"),2);
                    netAmt=EITLERPGLOBAL.round(rs.getDouble("TOTAL_NET_AMOUNT"),2);
                    
                    
                    dtlgrsAmt=0;
                    dtlnetAmt=0;
                    String str ="SELECT * FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO ="+ InvoiceNo+ " AND INVOICE_DATE = '"+InvoiceDate+"' AND INVOICE_TYPE ="+InvoiceType;
                    rsdetail = data.getResult("SELECT * FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO ='"+ InvoiceNo+ "' AND INVOICE_DATE = '"+InvoiceDate+"' AND INVOICE_TYPE ="+InvoiceType);
                    rsdetail.first();
                    if (rsdetail.getRow()>0){
                        while (!rsdetail.isAfterLast()){
                            dtlgrsAmt = dtlgrsAmt + rsdetail.getDouble("GROSS_AMOUNT");
                            dtlnetAmt = dtlnetAmt + rsdetail.getDouble("NET_AMOUNT");
                            rsdetail.next();
                        }
                    }
                    dtlgrsAmt = EITLERPGLOBAL.round(dtlgrsAmt,2);
                    if (grsAmt !=dtlgrsAmt){
                        
                        String line="Invoice No. :" +InvoiceNo + " InvoiceDate : "+ InvoiceDate + " Header Gross Amount :" + grsAmt + " Detail Gross Amount :"+dtlgrsAmt;
                        out.println("Value at: " + line);
                        
                        //   System.out.println("Invoice No. :" +InvoiceNo + " InvoiceDate : "+ InvoiceDate + " Header Gross Amount :" + grsAmt + " Detail Gross Amount :"+dtlgrsAmt);
                    }
                    
                    
                    rs.next();
                }
                out.close();
                //System.out.print("End of program");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        clsVerifyInvoiceHeaderDetail obj = new clsVerifyInvoiceHeaderDetail();
        //clsVerifyInvoiceHeaderDetail();
    }
    
}
