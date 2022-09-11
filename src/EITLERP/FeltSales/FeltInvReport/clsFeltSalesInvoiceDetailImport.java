/*
 * clsSalesInvoiceDetailImport.java
 *
 * Created on May 15, 2008, 4:03 PM
 */

package EITLERP.FeltSales.FeltInvReport;

/**
 *
 * @author  root
 */
//import EITLERP.Sales.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Stores.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import EITLERP.Finance.*;
import EITLERP.Sales.*;

public class clsFeltSalesInvoiceDetailImport {
    
    /** Creates a new instance of clsSalesInvoiceDetailImport */
    public clsFeltSalesInvoiceDetailImport() {
        
    }
    
    
    public static void main(String[] args) {
        //0 - Invoice Type
        //1 - FileName
        
        
        
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt, 3 -Filter)  \n2. Line sequential file name ");
            return;
        }
        
        String Type=args[0];
        String FileName=args[1];
        
        
        
        /*String Type="1";
        String FileName="/root/Desktop/t.qlt";*/
        
        clsFeltSalesInvoiceDetailImport objImport=new clsFeltSalesInvoiceDetailImport();
        
        if(Type.equals("2")) {
            objImport.ImportInvoicesFelt(FileName);
        }
        
        
    }
    
    public void ImportInvoicesFelt(String invoiceFile) {
        boolean Done=false;
        long Counter=0;
        try {
            clsSalesInvoice objInvoice=new clsSalesInvoice();
            //String dbURL="jdbc:mysql://localhost:3306/DINESHMILLS";
//            String dbURL="jdbc:mysql://200.0.0.230:3306/DINESHMILLS";
            String dbURL=EITLERPGLOBAL.DatabaseURL;
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_DETAIL LIMIT 1");
            int Pointer=0;
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            Done=false;
            while(!Done){
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo="F"+FileRecord.substring(0,6);
                    Pointer+=6; //FLS_INV_NO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //FLS-INV-DATE
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                    String PartyCode = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6; //FLS-PARTY-CODE
                    String QualityNo=FileRecord.substring(Pointer,Pointer+7);
                    Pointer+=7; //FLS-ITEM-CODE
                    Pointer+=17;//FLS-PARTY-NAME
                    Pointer+=6; //FLS-LENGTH
                    Pointer+=6; //FLS-WIDTH
                    String PieceNo = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6; //FLS-PIECE-NO
                    String str = "SELECT * FROM D_SAL_INVOICE_DETAIL "+
                    " where invoice_no='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' "+
                    " AND QUALITY_NO='"+QualityNo+"' "+
                    " AND PIECE_NO='"+PieceNo+"' AND PARTY_CODE='"+PartyCode+"' ";
                    if(!data.IsRecordExist(str,dbURL)) {
                        int SrNo = 0;
                        int nNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='"+InvoiceNo+"' ");
                        SrNo = nNo + 1;
                        if(!data.IsRecordExist("SELECT INVOICE_NO FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='"+InvoiceNo+"' AND SR_NO=" + SrNo ,dbURL)) {
                            System.out.println("Importing Invoice "+InvoiceNo);
                            Pointer=0;
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",2);
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //FLS-INV-NO
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //FLS-INV-DATE
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2));
                            rsInvoice.updateInt("SR_NO",SrNo);
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6; //FLS-PARTY-CODE
                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //FLS-ITEM-CODE
                            //rsInvoice.updateString("PATTERN_CODE","");
                            Pointer += 17;//FLS-PARTY-NAME
                            double Length = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100; Pointer += 6;//FLS-LENGTH
                            double Width = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100; Pointer += 6;//FLS-WIDTH
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6; //FLS-PIECE-NO
                            Pointer += 11;//FLS-ORDER-NO
                            Pointer += 8;//FLS-TEM_ADVANCE
                            rsInvoice.updateDouble("GROSS_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer += 6;//FLS-GROSS-WEIGTH
                            double GrossAmt = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+10))/100;
                            rsInvoice.updateDouble("GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+10))/100); Pointer+=10;//FLS-GROSS-VALUE
                            Pointer += 8;//FLS-CHEM-TRT-CHG
                            Pointer += 8;//FLS-SPRL-CHG
                            Pointer += 8;//FLS-TC-CESS
                            Pointer += 8;//FLS-SUR-CHG
                            Pointer+=8;//FLS-NET-AMOUNT
                            Pointer += 8;//FLS-TOTAL-EXCISE
                            Pointer += 6;//FLS-FREIGHT-CHG
                            Pointer += 6;//FLS-INSURANCE-CHG
                            double Per = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100;
                            rsInvoice.updateDouble("TRD_DISCOUNT",(GrossAmt*Per)/100);
                            Pointer += 6;//FLS-OEM-DIS-AMT
                            Pointer += 6;//FLS-HUNDI-CHG
                            Pointer += 6;//FLS-ANG-CHG
                            Pointer += 6;//FLS-LC-DIS-AMT
                            Pointer += 6;//FLS-HNDLM-SLS-CHG
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+10))/100); Pointer += 10;//FLS-TOTAL
                            Pointer+=8;//FLS-ROUNDED
                            Pointer+=2;//FLS-CASE-BAL-A
                            Pointer+=6;//FLS-CASE-BAL-N
                            double NoOfPiece = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4)); Pointer += 4;//FLS-NO-OF-PIECES
                            double SqrMtr = Length * Width * NoOfPiece;
                            rsInvoice.updateDouble("GROSS_SQ_MTR",SqrMtr);
                            rsInvoice.insertRow();
                            
                            System.out.println("Invoice Imported ");                                                   }
                    }
                }
                catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            System.out.println("Finished");                   }
        catch(Exception e) {
        }
    }
    
    //    public void ImportInvoicesFelt(String invoiceFile) {
    //
    //        boolean Done=false;
    //        long Counter=0;
    //
    //        try {
    //
    //            clsSalesInvoice objInvoice=new clsSalesInvoice();
    //
    //            String dbURL="jdbc:mysql://200.0.0.230:3306/DINESHMILLS";
    //
    //            Connection objConn=data.getConn(dbURL);
    //            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_DETAIL LIMIT 1");
    //            int Pointer=0;
    //
    //            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
    //
    //            Done=false;
    //
    //            while(!Done){
    //
    //                try {
    //
    //                    String FileRecord=aFile.readLine();
    //                    Pointer=0;
    //                    String InvoiceNo="F"+FileRecord.substring(0,6);
    //                    Pointer+=1; //QIIN-ACC-TYPE
    //                    Pointer+=6; //QIIN-ACC-SRNO
    //                    String QualityNo=FileRecord.substring(Pointer,Pointer+7);
    //                    Pointer+=7; //QIIN-QLTY-NO
    //                    String PatternCode = FileRecord.substring(Pointer,Pointer+2);
    //                    Pointer+=2; //QIIN-PATT-CD
    //                    String PieceNo = FileRecord.substring(Pointer,Pointer+8);
    //                    Pointer+=8; //QIIN-PIEC-NO
    //                    String PartyCode = FileRecord.substring(Pointer,Pointer+6);
    //                    Pointer+=6; //QIIN-PARTY-CD
    //                    Pointer+=111;
    //                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //QIIN-INV-DATE
    //                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
    //                    String str = "SELECT * FROM D_SAL_INVOICE_DETAIL "+
    //                    " where invoice_no='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' "+
    //                    " AND QUALITY_NO='"+QualityNo+"' AND PATTERN_CODE='"+PatternCode+"' "+
    //                    " AND PIECE_NO='"+PieceNo+"' AND PARTY_CODE='"+PartyCode+"' ";
    //                    if(!data.IsRecordExist(str,dbURL)) {
    //
    //                        int SrNo = 0;
    //
    //                        int nNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvoiceNo+"' ");
    //                        SrNo = nNo + 1;
    //
    //                        if(!data.IsRecordExist("SELECT INVOICE_NO FROM D_SAL_INVOICE_DETAIL WHERE INVOICE_NO='"+InvoiceNo+"' AND SR_NO=" + SrNo ,dbURL)) {
    //
    //                            System.out.println("Importing Invoice "+InvoiceNo);
    //
    //                            Pointer=0;
    //
    //                            rsInvoice.moveToInsertRow();
    //                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
    //                            rsInvoice.updateInt("INVOICE_TYPE",2); Pointer+=1; //QIIN-ACC-TYPE
    //                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //QIIN-ACC-SRNO
    //                            rsInvoice.updateInt("SR_NO",SrNo);
    //                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //QIIN-QLTY-NO
    //                            rsInvoice.updateString("PATTERN_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //QIIN-PATT-CD
    //                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8; //QIIN-PIEC-NO
    //                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6; //QIIN-PARTY-CD
    //                            rsInvoice.updateInt("WAREHOUSE_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1; //QIIN-WH-CODE
    //                            rsInvoice.updateString("UNIT_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //QIIN-UNIT-CD
    //                            rsInvoice.updateString("FLAG_DEF_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //QIIN-FLAG-DEF-CD
    //                            rsInvoice.updateDouble("RATE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //QIIN-RATE
    //                            Pointer+=1; //QIIN-UFILLER
    //                            rsInvoice.updateInt("TRD_DISC_TYPE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;//QIIN-TRD-DISC-TYPE
    //                            rsInvoice.updateString("SEASON_CODE",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;//QIIN-SEASON-CODE
    //                            rsInvoice.updateDouble("DEF_DISC_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4; //QIIN-DEF-DISC-P
    //                            rsInvoice.updateDouble("ADDL_DISC_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4; //QIIN-ADDL-DISC-P
    //                            rsInvoice.updateDouble("GROSS_SQ_MTR",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-GRS-SQ-MTR
    //                            rsInvoice.updateDouble("GROSS_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-GRS-KG
    //                            rsInvoice.updateDouble("GROSS_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//QIIN-GRS-QTY
    //                            rsInvoice.updateDouble("NET_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//QIIN-NET-QTY
    //                            rsInvoice.updateDouble("GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;//QIIN-GRS-AMT
    //                            rsInvoice.updateDouble("TRD_DISCOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-TRD-DSC
    //                            rsInvoice.updateDouble("DEF_DISCOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-DEF-DSC
    //                            rsInvoice.updateDouble("ADDL_DISCOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-ADDL-DSC
    //                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;//QIIN-NET-AMT
    //                            rsInvoice.updateDouble("EXCISABLE_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;//QIIN-EXC-DTY
    //                            Pointer+=1;//QIIN--STATUS-FLAG
    //                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;//QIIN-BALE-NO
    //                            rsInvoice.updateString("EXPORT_CATEGORY",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;//QIIN-EXC-CAT
    //                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //QIIN-INV-DATE
    //                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2));
    //                            Pointer+=1;//FILLER
    //                            String ReceiptDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;//QIIN-RCD-DATE
    //                            rsInvoice.updateString("RECEIPT_DATE", "20"+ReceiptDate.substring(4)+"-"+ReceiptDate.substring(2,4)+"-"+ReceiptDate.substring(0,2));
    //                            rsInvoice.updateDouble("BASIC_EXC",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-BASIC-EXC
    //                            rsInvoice.updateDouble("ADDITIONAL_DUTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//QIIN-ADDL-DUTY
    //                            rsInvoice.updateInt("AGENT_SR_NO", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4;//QIIN-AGT-NO
    //                            rsInvoice.updateString("AGENT_ALPHA",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;//QIIN-AG-ALP
    //                            rsInvoice.updateString("GATEPASS_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;//QIIN-ETP-NO
    //                            rsInvoice.updateBoolean("CANCELLED",false);
    //                            rsInvoice.updateString("CREATED_BY", "admin");
    //                            rsInvoice.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
    //                            rsInvoice.updateString("MODIFIED_BY", "admin");
    //                            rsInvoice.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
    //                            rsInvoice.updateString("REMARKS", "");
    //                            rsInvoice.updateBoolean("CHANGED", true);
    //                            rsInvoice.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
    //
    //                            Pointer +=6;//FILLER
    //                            Pointer +=5;//FILLER
    //                            Pointer +=1;//ABC-FLAG
    //
    //                            rsInvoice.insertRow();
    //
    //                            System.out.println("Invoice Imported ");
    //
    //
    //                        }
    //
    //                    }
    //
    //                }
    //                catch(Exception c){
    //                    c.printStackTrace();
    //                    Done=true;
    //                }
    //            }
    //
    //            System.out.println("Finished");
    //
    //
    //        }
    //        catch(Exception e) {
    //
    //        }
    //    }
    
    
}
