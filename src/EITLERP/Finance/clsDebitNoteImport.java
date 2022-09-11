/*
 * clsDebitNoteImport.java
 *
 * Created on September 8, 2009, 12:56 PM
 */

package EITLERP.Finance;

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

public class clsDebitNoteImport {
    
    private static String BookCode="";
    private static String DeductionCode="";
    /** Creates a new instance of clsDebitNoteImport */
    public clsDebitNoteImport() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //0 - FileName
        //1 - maincode
        //2 - bookcode
        
        if(args.length<3) {
            System.out.println("Insufficient arguments. Please specify \n 1. Line sequential file name \n 2. Main Code \n 3. Book Code");
            return;
        }
        
        String FileName=args[0].trim();
        String MainCode=args[1].trim();
        String pBookCode=args[2].trim();
        
        //        String FileName="/root/Desktop/debitnote.txt";
        //        String MainCode="210027";
        //        String pBookCode="12";
        
        clsDebitNoteImport objImport=new clsDebitNoteImport();
        
        if(MainCode.equals("210027")) {
            BookCode = pBookCode;
            DeductionCode="311090";
            objImport.ImportDebitNote(FileName,MainCode);
        }
        
        if(MainCode.equals("210010")) {
            BookCode = pBookCode;
            DeductionCode="311100";
            objImport.ImportDebitNote(FileName,MainCode);
        }
        
        //        if(MainCode.equals("210072")) {
        //            BookCode = pBookCode;
        //            DeductionCode="";
        //            objImport.ImportDebitNoteFilter(FileName,MainCode);
        //        }
        
    }
    
    public void ImportDebitNote(String FileName, String MainCode) {
        
        boolean Done=false;
        long Counter=0;
        
        try {
            
            clsVoucher objVoucher=new clsVoucher();
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/FINANCE";
            
            Connection objConn=data.getConn(dbURL);
            Statement stVou=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsVou=stVou.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER LIMIT 1");
            
            Statement stVouDtl=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsVouDtl=stVouDtl.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL LIMIT 1");
            
            Statement stVouDtlEx=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsVouDtlEx=stVouDtlEx.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX LIMIT 1");
            
            int Pointer=0;
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String PartyCode=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //sub_code
                    Pointer+=25; //party_name
                    String DbnNo = FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //dbn_no
                    String Year = FileRecord.substring(Pointer,Pointer+4).trim(); Pointer+=4; //dbn_year
                    String DbnDate = FileRecord.substring(Pointer,Pointer+8); Pointer+=8; //dbn_date
                    String DbnDueDate = FileRecord.substring(Pointer,Pointer+8); Pointer+=8; //dbn_due_dt
                    String Amount = FileRecord.substring(Pointer,Pointer+11); Pointer+=11; //dbn_amt
                    
                    String VoucherNo = "M"+Year.substring(0,2)+BookCode+DbnNo;
                    String str = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO ='"+VoucherNo+"'";
                    
                    if (!data.IsRecordExist(str,FinanceGlobal.FinURL)) {
                        
                        System.out.println("Importing Debit Note "+VoucherNo);                        
                        rsVou.first();
                        rsVou.moveToInsertRow();
                        rsVou.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsVou.updateString("VOUCHER_NO",VoucherNo);
                        rsVou.updateInt("VOUCHER_TYPE",FinanceGlobal.TYPE_DEBIT_NOTE);
                        DbnDate = DbnDate.substring(0,4) + "-" + DbnDate.substring(4,6) + "-" + DbnDate.substring(6);
                        rsVou.updateString("VOUCHER_DATE", DbnDate);
                        rsVou.updateString("BOOK_CODE",BookCode);
                        rsVou.updateString("CHEQUE_NO","");
                        rsVou.updateString("CHEQUE_DATE","0000-00-00");
                        rsVou.updateString("BANK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,BookCode));
                        rsVou.updateString("ST_CATEGORY","");
                        DbnDueDate = DbnDueDate.substring(6) + "/" + DbnDueDate.substring(4,6) + "/" + DbnDueDate.substring(0,4)  ;
                        rsVou.updateString("REMARKS","DUE DATE = "+DbnDueDate);
                        rsVou.updateLong("HIERARCHY_ID",0);
                        rsVou.updateString("CREATED_BY","admin");
                        rsVou.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVou.updateString("MODIFIED_BY","admin");
                        rsVou.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVou.updateBoolean("CHANGED",true);
                        rsVou.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVou.updateBoolean("APPROVED",true);
                        rsVou.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVou.updateBoolean("REJECTED",false);
                        rsVou.updateString("REJECTED_DATE","0000-00-00");
                        rsVou.updateBoolean("CANCELLED",false);
                        //                    rsVou.updateDouble("CHEQUE_AMOUNT",0);
                        //                    rsVou.updateInt("PAYMENT_MODE",0);
                        //                    rsVou.updateString("LEGACY_NO","");
                        //                    rsVou.updateString("LINK_NO","");
                        //                    rsVou.updateString("LEGACY_DATE","0000-00-00");
                        //                    rsVou.updateInt("EXCLUDE_IN_ADJ",);
                        rsVou.updateString("CUSTOMER_BANK","");
                        rsVou.insertRow();
                        
                        Year = Year.substring(2)+Year.substring(0,2);
                        int nDbnNo = Integer.parseInt(DbnNo);
                        String LinkNo = nDbnNo+"/"+Year;
                        
                        rsVouDtl.first();
                        rsVouDtl.moveToInsertRow();
                        rsVouDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsVouDtl.updateString("VOUCHER_NO",VoucherNo);
                        rsVouDtl.updateInt("SR_NO",1);
                        rsVouDtl.updateString("EFFECT","D");
                        rsVouDtl.updateInt("ACCOUNT_ID",1);
                        rsVouDtl.updateString("MAIN_ACCOUNT_CODE",MainCode);
                        rsVouDtl.updateString("SUB_ACCOUNT_CODE",PartyCode);
                        rsVouDtl.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsVouDtl.updateDouble("APPLICABLE_AMOUNT",0);
                        rsVouDtl.updateDouble("PERCENTAGE",0);
                        rsVouDtl.updateString("REMARKS","");
                        rsVouDtl.updateString("CREATED_BY","admin");
                        rsVouDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateString("MODIFIED_BY","admin");
                        rsVouDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateBoolean("CHANGED",true);
                        rsVouDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateInt("CANCELLED",0);
                        rsVouDtl.updateString("PO_NO","");
                        rsVouDtl.updateString("PO_DATE","0000-00-00");
                        rsVouDtl.updateString("INVOICE_NO","");
                        rsVouDtl.updateString("INVOICE_DATE","0000-00-00");
                        rsVouDtl.updateString("GRN_NO","");
                        rsVouDtl.updateString("GRN_DATE","0000-00-00");
                        rsVouDtl.updateInt("MODULE_ID",0);
                        rsVouDtl.updateDouble("INVOICE_AMOUNT",0);
                        rsVouDtl.updateInt("IS_DEDUCTION",0);
                        rsVouDtl.updateString("LINK_NO",LinkNo);
                        rsVouDtl.insertRow();
                        
                        rsVouDtl.moveToInsertRow();
                        rsVouDtl.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsVouDtl.updateString("VOUCHER_NO",VoucherNo);
                        rsVouDtl.updateInt("SR_NO",2);
                        rsVouDtl.updateString("EFFECT","C");
                        rsVouDtl.updateInt("ACCOUNT_ID",1);
                        rsVouDtl.updateString("MAIN_ACCOUNT_CODE",DeductionCode);
                        rsVouDtl.updateString("SUB_ACCOUNT_CODE","");
                        rsVouDtl.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsVouDtl.updateDouble("APPLICABLE_AMOUNT",0);
                        rsVouDtl.updateDouble("PERCENTAGE",0);
                        rsVouDtl.updateString("REMARKS","");
                        rsVouDtl.updateString("CREATED_BY","admin");
                        rsVouDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateString("MODIFIED_BY","admin");
                        rsVouDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateBoolean("CHANGED",true);
                        rsVouDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtl.updateInt("CANCELLED",0);
                        rsVouDtl.updateString("PO_NO","");
                        rsVouDtl.updateString("PO_DATE","0000-00-00");
                        rsVouDtl.updateString("INVOICE_NO","");
                        rsVouDtl.updateString("INVOICE_DATE","0000-00-00");
                        rsVouDtl.updateString("GRN_NO","");
                        rsVouDtl.updateString("GRN_DATE","0000-00-00");
                        rsVouDtl.updateInt("MODULE_ID",0);
                        rsVouDtl.updateDouble("INVOICE_AMOUNT",0);
                        rsVouDtl.updateInt("IS_DEDUCTION",0);
                        rsVouDtl.updateString("LINK_NO",LinkNo);
                        rsVouDtl.insertRow();
                        
                        rsVouDtlEx.first();
                        rsVouDtlEx.moveToInsertRow();
                        rsVouDtlEx.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsVouDtlEx.updateString("VOUCHER_NO",VoucherNo);
                        rsVouDtlEx.updateInt("SR_NO",1);
                        rsVouDtlEx.updateString("EFFECT","D");
                        rsVouDtlEx.updateInt("ACCOUNT_ID",1);
                        rsVouDtlEx.updateString("MAIN_ACCOUNT_CODE",MainCode);
                        rsVouDtlEx.updateString("SUB_ACCOUNT_CODE",PartyCode);
                        rsVouDtlEx.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsVouDtlEx.updateDouble("APPLICABLE_AMOUNT",0);
                        rsVouDtlEx.updateDouble("PERCENTAGE",0);
                        rsVouDtlEx.updateString("REMARKS","");
                        rsVouDtlEx.updateString("CREATED_BY","admin");
                        rsVouDtlEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateString("MODIFIED_BY","admin");
                        rsVouDtlEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateBoolean("CHANGED",true);
                        rsVouDtlEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateInt("CANCELLED",0);
                        rsVouDtlEx.updateInt("BLOCK_NO",1);
                        rsVouDtlEx.updateString("PO_NO","");
                        rsVouDtlEx.updateString("PO_DATE","0000-00-00");
                        rsVouDtlEx.updateString("INVOICE_NO","");
                        rsVouDtlEx.updateString("INVOICE_DATE","0000-00-00");
                        rsVouDtlEx.updateString("GRN_NO","");
                        rsVouDtlEx.updateString("GRN_DATE","0000-00-00");
                        rsVouDtlEx.updateInt("MODULE_ID",0);
                        rsVouDtlEx.updateDouble("INVOICE_AMOUNT",0);
                        rsVouDtlEx.updateInt("IS_DEDUCTION",0);
                        rsVouDtlEx.updateString("LINK_NO",LinkNo);
                        rsVouDtlEx.insertRow();
                        
                        rsVouDtlEx.moveToInsertRow();
                        rsVouDtlEx.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsVouDtlEx.updateString("VOUCHER_NO",VoucherNo);
                        rsVouDtlEx.updateInt("SR_NO",2);
                        rsVouDtlEx.updateString("EFFECT","C");
                        rsVouDtlEx.updateInt("ACCOUNT_ID",1);
                        rsVouDtlEx.updateString("MAIN_ACCOUNT_CODE",DeductionCode);
                        rsVouDtlEx.updateString("SUB_ACCOUNT_CODE","");
                        rsVouDtlEx.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsVouDtlEx.updateDouble("APPLICABLE_AMOUNT",0);
                        rsVouDtlEx.updateDouble("PERCENTAGE",0);
                        rsVouDtlEx.updateString("REMARKS","");
                        rsVouDtlEx.updateString("CREATED_BY","admin");
                        rsVouDtlEx.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateString("MODIFIED_BY","admin");
                        rsVouDtlEx.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateBoolean("CHANGED",true);
                        rsVouDtlEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsVouDtlEx.updateInt("CANCELLED",0);
                        rsVouDtlEx.updateInt("BLOCK_NO",1);
                        rsVouDtlEx.updateString("PO_NO","");
                        rsVouDtlEx.updateString("PO_DATE","0000-00-00");
                        rsVouDtlEx.updateString("INVOICE_NO","");
                        rsVouDtlEx.updateString("INVOICE_DATE","0000-00-00");
                        rsVouDtlEx.updateString("GRN_NO","");
                        rsVouDtlEx.updateString("GRN_DATE","0000-00-00");
                        rsVouDtlEx.updateInt("MODULE_ID",0);
                        rsVouDtlEx.updateDouble("INVOICE_AMOUNT",0);
                        rsVouDtlEx.updateInt("IS_DEDUCTION",0);
                        rsVouDtlEx.updateString("LINK_NO",LinkNo);
                        rsVouDtlEx.insertRow();
                        
                        System.out.println("Debit Note Imported ");
                    }
                }
                catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            
            
            System.out.println("Finished");
            
        }
        catch(Exception e) {
            
        }
    }
}
