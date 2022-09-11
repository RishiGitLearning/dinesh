/*
 * clsSalesInvoiceImport.java
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

public class clsSalesInvoiceImport {
    String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
    /** Creates a new instance of clsSalesInvoiceImport */
    public clsSalesInvoiceImport() {
        
    }
    
    
    public static void main(String[] args) {
        //0 - Invoice Type
        //1 - FileName
        
        
        // original start 1
        
        if(args.length<4) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt, 3 -Filter)  \n2. Line sequential file name \n3. Financial Year From \n4. Post SJ ? (Y or N)");
            return;
        }
        
        String Type=args[0];
        String FileName=args[1];
        int FinYearFrom=Integer.parseInt(args[2]);
        boolean PostSJ=false;
        
        if(args[3].equals("Y")) {
            PostSJ=true;
        }
        
        // original end 1
        
        
        // For Testing start 2
        
        /*String Type="1";
        String FileName="/root/Desktop/stg.STG";
        int FinYearFrom=2012;
        boolean PostSJ=true;*/
        
        // For Testing end 2
        
        /*EITLERPGLOBAL.FinYearFrom=FinYearFrom;
        EITLERPGLOBAL.FinYearTo=FinYearFrom+1;*/
        
        clsSalesInvoiceImport objImport=new clsSalesInvoiceImport();
        
        if(Type.equals("1")) {
            objImport.ImportInvoicesSuiting(FileName,PostSJ); 
        }
        
        if(Type.equals("2")) {
            objImport.ImportInvoicesFelt(FileName,PostSJ); 
        }
        
        if(Type.equals("3")) {
            objImport.ImportInvoicesFilter(FileName,PostSJ); 
        }
        
        if(Type.equals("4")) {
            objImport.ImportInvoicesBlanket(FileName,PostSJ); 
        }
        
        
        
    }
    
    public void ImportInvoicesSuiting(String invoiceFile, boolean pPostSJ) {
        
        boolean Done=false;
        boolean canPostSJ=false;
        boolean AutoAdj=false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Sutting/Inv";
        long Counter=0;
        
        try {
            clsSalesInvoice objInvoice = new clsSalesInvoice();
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()),"0",2);
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2);
            LogFileName = LogFileName+Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2)+".log";
            
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER LIMIT 1");
            int Pointer=0;
            
            clsLogFile log = new clsLogFile();
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    //String InvoiceNo=FileRecord.substring(1,7); 
                    String InvoiceNo="SU/"+FileRecord.substring(1,7);
                    
                  //  String InvoiceNo="FE"+FileRecord.substring(0,6);
                    // Added by mrugesh for auto adjustment start
                    String InvNo = InvoiceNo;
                    String InvDate = "";
                    String ChargeCode = "";
                    String Party_code="";
                    double InvoiceAmount = 0;
                    canPostSJ=false;
                    AutoAdj=false;
                    // Added by mrugesh for auto adjustment end
                    
                    Pointer+=1; //MIIN-ACC-TYPE
                    Pointer+=6; //MIIN-ACC-SRNO
                    Pointer+=7; //MIIN-QLTY-NO1
                    Pointer+=2; //MIIN-PATT-CD
                    Pointer+=8; //MIIN-PIEC-NO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //MIIN-INV-DATE
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
//                    if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
//                        continue;
//                    }
//                    if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE=1 AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ")) {
//                        continue;
//                    }
                    String str = "SELECT * FROM D_SAL_INVOICE_HEADER "+
                    " WHERE INVOICE_NO='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' ";
                    if(InvoiceDate.equals("2011-06-17") || InvoiceDate.equals("2011-06-18")) {
                        boolean halt=true;
                        continue;
                    }
                    if(!data.IsRecordExist(str,dbURL)) {
                        Pointer=0;
                        Pointer += 34;
                        Party_code= FileRecord.substring(Pointer,Pointer+6);
                        if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"' AND MAIN_ACCOUNT_CODE='210027' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL)) {
                            
                            System.out.println("Importing Invoice "+InvoiceNo);
                            
                            Pointer=0;
                            
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",1); Pointer+=1; //MIIN-ACC-TYPE   //1
                            //rsInvoice.updateString("INVOICE_NO",FileRecord.substring(Pointer,Pointer+6));  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //MIIN-ACC-SRNO  /2-7
                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //MIIN-QLTY-NO1 - S-T-DIGIT / OTHER-DIGIT  //8-14
                            rsInvoice.updateString("PATTERN_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //MIIN-PATT-CD //15-16
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8; //MIIN-PIEC-NO      //17-24
                            
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            InvDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                            
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2)); //25-30
                            rsInvoice.updateInt("AGENT_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4;    //31-34
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;              //35-40
                            rsInvoice.updateString("STATION_CODE", FileRecord.substring(Pointer,Pointer+15)); Pointer+=15;         //41-55
                            rsInvoice.updateString("PAYMENT_TERM",FileRecord.substring(Pointer,Pointer+26)); Pointer+=26;          //56-81
                            
                            //Pointer++; //MIIN-BANK-CH
                            //rsInvoice.updateInt("TRANSPORT_MODE",Integer.parseInt(FileRecord.substring(Pointer,Pointer+1))); Pointer++;
                            ChargeCode = FileRecord.substring(Pointer,Pointer+2);             
                            
                            rsInvoice.updateInt("PAYMENT_TERM_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2; //82-83
                            rsInvoice.updateString("AGENT_LAST_INVOICE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;    //84-85
                            rsInvoice.updateInt("AGENT_LAST_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+3))); Pointer+=3;   //86-88
                            rsInvoice.updateInt("FIN_YEAR_FROM",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;     //89-90
                            rsInvoice.updateInt("FIN_YEAR_TO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;       //93-92
                            rsInvoice.updateInt("WAREHOUSE_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;      //93
                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;                       //96-99
                            
                            String PackingDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;     
                            
                            rsInvoice.updateString("PACKING_DATE", "20"+PackingDate.substring(4)+"-"+PackingDate.substring(2,4)+"-"+PackingDate.substring(0,2));   //102-105
                            rsInvoice.updateString("EXPORT_SUB_CATEGORY",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;      //108-113
                            rsInvoice.updateString("EXPORT_CATEGORY",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;           //116-116
                            rsInvoice.updateString("GATEPASS_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;              // 119-122
                            
                            String GatepassDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("GATEPASS_DATE", "20"+GatepassDate.substring(4)+"-"+GatepassDate.substring(2,4)+"-"+GatepassDate.substring(0,2)); //125-128
                            rsInvoice.updateString("DRAFT_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;     //131-134
                            
                            String DraftDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; 
                            
                            rsInvoice.updateString("DRAFT_DATE", "20"+DraftDate.substring(4)+"-"+DraftDate.substring(2,4)+"-"+DraftDate.substring(0,2));   //137-140
                            
                            rsInvoice.updateDouble("COLUMN_1_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //143-144
                            rsInvoice.updateDouble("COLUMN_2_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //147-148
                            rsInvoice.updateDouble("COLUMN_3_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //151-152
                            rsInvoice.updateDouble("COLUMN_4_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //155-156
                            rsInvoice.updateDouble("COLUMN_5_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //159-160
                            rsInvoice.updateDouble("COLUMN_6_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //163-164
                            
                            rsInvoice.updateDouble("CGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //165-171
                            
                            rsInvoice.updateDouble("SGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_7_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //172-178
                            
                            
                            rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //179-185
                            rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //186-192
                            rsInvoice.updateDouble("COLUMN_10_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //193-199
                            rsInvoice.updateDouble("VAT1",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100);
                            rsInvoice.updateDouble("COLUMN_11_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;         //200-205                        
                            rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //206-212
                            rsInvoice.updateDouble("CST5",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_13_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;          //215-219
                            
                            
                            rsInvoice.updateDouble("IGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_14_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //220-226
                            
                            
                            rsInvoice.updateDouble("TOTAL_SQ_MTR",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;          //229-236
                            rsInvoice.updateDouble("TOTAL_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;              //237-244
                            rsInvoice.updateDouble("TOTAL_GROSS_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;         //245-250
                            
                            Pointer+=2; //FLAG                                 //251-252
                            
                            rsInvoice.updateDouble("TOTAL_NET_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;            //253-258
                            rsInvoice.updateDouble("TOTAL_GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;       //259-267
                            rsInvoice.updateDouble("COLUMN_15_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //268-275
                            rsInvoice.updateDouble("COLUMN_16_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //276-283
                            rsInvoice.updateDouble("COLUMN_17_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //284-291
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;          //292-300
                            rsInvoice.updateDouble("EXCISABLE_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;           //301-308
                            rsInvoice.updateDouble("COLUMN_1_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //309-315
                            rsInvoice.updateDouble("COLUMN_2_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //316-322
                            rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //323-329
                            rsInvoice.updateDouble("COLUMN_4_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;             //330-336
                            rsInvoice.updateDouble("COLUMN_5_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //337-343
                            rsInvoice.updateDouble("COLUMN_18_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;             //344-350
                            rsInvoice.updateDouble("TOTAL_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;               //351-359
                            InvoiceAmount = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7));
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;                 //360-366
                            
                            Pointer++;                                   //367
                            
                            rsInvoice.updateString("IC_TYPE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                                      //368
                            rsInvoice.updateDouble("COLUMN_19_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;            //369-375
                            rsInvoice.updateString("QUALITY_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                            //376
                            rsInvoice.updateString("FILLER",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                                      //377
                            
                            Pointer+=96;                                                                                                               //378-472
                            
                            rsInvoice.updateInt("BANK_CLASS", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;                //473
                            rsInvoice.updateInt("BANK_CODE", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;                //474-475
                            rsInvoice.updateString("HUNDI_NO",FileRecord.substring(Pointer,Pointer+7)); Pointer+=7;                                   //476-482     
                            rsInvoice.updateDouble("GROSS_WEIGHT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+5))/100); Pointer+=5;       //483-487
                            rsInvoice.updateInt("TRANSPORTER_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;          //488-489
                            rsInvoice.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;                                      //490-495   
                            rsInvoice.updateString("BNG_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                              //496
                            rsInvoice.updateString("INS_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                              //497
                            
                            Pointer+=1; //INV-TYPE                                                                                                   //498     
                            rsInvoice.updateDouble("CASH_DISC_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100);  
                            rsInvoice.updateDouble("COLUMN_20_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;    //499-505
                               //499-505
                            
                            rsInvoice.updateDouble("COLUMN_21_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;     //506-512
                            rsInvoice.updateDouble("COLUMN_22_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;     //513-519
                            rsInvoice.updateDouble("COLUMN_23_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;    //520-526
                            rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateBoolean("CANCELLED",false);
                            
                            String DueDate = "";
                            if(ChargeCode.startsWith("2")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("4")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+15+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("5")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+35, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("8")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            }
                            else if(ChargeCode.startsWith("1")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, (30+10), "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("3")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 3, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("6")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 180, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("7")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("9")) {
                                DueDate = InvDate;
                            }
                            
                            
                            double cgst_per = 2.50;
                            double sgst_per = 2.50;
                            double igst_per = 5.00;
                            
                            rsInvoice.updateString("DUE_DATE",DueDate);
                            
                            rsInvoice.updateString("COLUMN_1_CAPTION","CE DUTY");
                            rsInvoice.updateString("COLUMN_9_CAPTION","INS CHRG");
                            rsInvoice.updateString("COLUMN_10_CAPTION","BANK CHRG");
                            rsInvoice.updateString("COLUMN_14_CAPTION","IGST AMOUNT");
                            rsInvoice.updateString("COLUMN_6_CAPTION","CGST AMOUNT");
                            rsInvoice.updateString("COLUMN_7_CAPTION","SGST AMOUNT");
                            
                            rsInvoice.updateString("COLUMN_20_CAPTION","CASH DISCOUNT AMOUNT");
                            
                            rsInvoice.updateDouble("CGST_PER",cgst_per);
                            rsInvoice.updateDouble("SGST_PER",sgst_per);
                            rsInvoice.updateDouble("IGST_PER",igst_per);
                            
                            
                             
                            
                            rsInvoice.insertRow();
                            if(ChargeCode.startsWith("9")) {
                                double availableAmount  = clsAccount.get09AmountByParty("210027", Party_code, InvDate);
                                if(InvoiceAmount > availableAmount) {
                                    canPostSJ=false;
                                    AutoAdj=false;
                                    data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" of party "+ Party_code +" with Invoice Amount is ("+InvoiceAmount+") and advance amount is ("+availableAmount+") \n" +
                                    "before invoice date, so invoice not imported.";
                                    log.logToFile(LogFileName, Msg, 2);
                                   System.out.println("Invoice not Imported ");
                                } else {
                                    canPostSJ=true;
                                    AutoAdj=true;
                                    //System.out.println("Invoice Imported ");
                                }
                            } else {
                                canPostSJ=true;
                                AutoAdj=false;
                                System.out.println("Invoice Imported ");
                            }
                            
                            
                            if(pPostSJ && canPostSJ) {
                                System.out.println("Posting SJ");
                                objInvoice=(clsSalesInvoice)objInvoice.getObject(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,dbURL);
                                
                                if(objInvoice.PostSJTypeSuiting(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,AutoAdj)) {
                                    System.out.println("Invoice "+InvoiceNo+" Posted.");
                                    Msg = "SJ has been posted for Invoice No ("+InvNo+") on date "+InvDate;
                                    log.logToFile(LogFileName, Msg, 1);
                                } else {
                                    Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(objInvoice.LastError);
                                }
                            }
                        } else {
                            Msg = "Invoice no ("+InvNo+") on date "+InvDate+" party code does not exists";
                            log.logToFile(LogFileName, Msg, 2);
                        }
                    }
                } catch(Exception c){
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done=true;
                }
            }
            System.out.println("Finished");
        } catch(Exception e) {
        }
    }
    

    
    
    public void ImportInvoicesSuitingold(String invoiceFile, boolean pPostSJ) {
        
        boolean Done=false;
        boolean canPostSJ=false;
        boolean AutoAdj=false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Sutting/Inv";
        long Counter=0;
        
        try {
            clsSalesInvoice objInvoice = new clsSalesInvoice();
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()),"0",2);
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2);
            LogFileName = LogFileName+Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2)+".log";
            
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER LIMIT 1");
            int Pointer=0;
            
            clsLogFile log = new clsLogFile();
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo=FileRecord.substring(1,7);
                    // Added by mrugesh for auto adjustment start
                    String InvNo = InvoiceNo;
                    String InvDate = "";
                    String ChargeCode = "";
                    String Party_code="";
                    double InvoiceAmount = 0;
                    canPostSJ=false;
                    AutoAdj=false;
                    // Added by mrugesh for auto adjustment end
                    
                    Pointer+=1; //MIIN-ACC-TYPE
                    Pointer+=6; //MIIN-ACC-SRNO
                    Pointer+=7; //MIIN-QLTY-NO1
                    Pointer+=2; //MIIN-PATT-CD
                    Pointer+=8; //MIIN-PIEC-NO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //MIIN-INV-DATE
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
//                    if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
//                        continue;
//                    }
//                    if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE=1 AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ")) {
//                        continue;
//                    }
                    String str = "SELECT * FROM D_SAL_INVOICE_HEADER "+
                    " WHERE INVOICE_NO='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' ";
                    if(InvoiceDate.equals("2011-06-17") || InvoiceDate.equals("2011-06-18")) {
                        boolean halt=true;
                        continue;
                    }
                    if(!data.IsRecordExist(str,dbURL)) {
                        Pointer=0;
                        Pointer += 34;
                        Party_code= FileRecord.substring(Pointer,Pointer+6);
                        if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"' AND MAIN_ACCOUNT_CODE='210027' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL)) {
                            
                            System.out.println("Importing Invoice "+InvoiceNo);
                            
                            Pointer=0;
                            
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",1); Pointer+=1; //MIIN-ACC-TYPE
                            //rsInvoice.updateString("INVOICE_NO",FileRecord.substring(Pointer,Pointer+6));  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //MIIN-QLTY-NO1 - S-T-DIGIT / OTHER-DIGIT
                            rsInvoice.updateString("PATTERN_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //MIIN-PATT-CD
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8; //MIIN-PIEC-NO
                            
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            InvDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                            
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2));
                            rsInvoice.updateInt("AGENT_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4;
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            rsInvoice.updateString("STATION_CODE", FileRecord.substring(Pointer,Pointer+15)); Pointer+=15;
                            rsInvoice.updateString("PAYMENT_TERM",FileRecord.substring(Pointer,Pointer+26)); Pointer+=26;
                            
                            //Pointer++; //MIIN-BANK-CH
                            //rsInvoice.updateInt("TRANSPORT_MODE",Integer.parseInt(FileRecord.substring(Pointer,Pointer+1))); Pointer++;
                            ChargeCode = FileRecord.substring(Pointer,Pointer+2);
                            
                            rsInvoice.updateInt("PAYMENT_TERM_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("AGENT_LAST_INVOICE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                            rsInvoice.updateInt("AGENT_LAST_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+3))); Pointer+=3;
                            rsInvoice.updateInt("FIN_YEAR_FROM",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateInt("FIN_YEAR_TO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateInt("WAREHOUSE_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;
                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String PackingDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("PACKING_DATE", "20"+PackingDate.substring(4)+"-"+PackingDate.substring(2,4)+"-"+PackingDate.substring(0,2));
                            rsInvoice.updateString("EXPORT_SUB_CATEGORY",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;
                            rsInvoice.updateString("EXPORT_CATEGORY",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;
                            rsInvoice.updateString("GATEPASS_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String GatepassDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("GATEPASS_DATE", "20"+GatepassDate.substring(4)+"-"+GatepassDate.substring(2,4)+"-"+GatepassDate.substring(0,2));
                            rsInvoice.updateString("DRAFT_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String DraftDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("DRAFT_DATE", "20"+DraftDate.substring(4)+"-"+DraftDate.substring(2,4)+"-"+DraftDate.substring(0,2));
                            
                            rsInvoice.updateDouble("COLUMN_1_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_2_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_3_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_4_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_5_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_6_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_7_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_10_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("VAT1",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100);
                            rsInvoice.updateDouble("COLUMN_11_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;                            
                            rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("CST5",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_13_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_14_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("TOTAL_SQ_MTR",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_GROSS_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            
                            Pointer+=2; //FLAG
                            
                            rsInvoice.updateDouble("TOTAL_NET_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            rsInvoice.updateDouble("TOTAL_GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            rsInvoice.updateDouble("COLUMN_15_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_16_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_17_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            rsInvoice.updateDouble("EXCISABLE_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_1_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_2_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_4_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_5_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_18_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("TOTAL_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            InvoiceAmount = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7));
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;
                            
                            Pointer++;
                            
                            rsInvoice.updateString("IC_TYPE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateDouble("COLUMN_19_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;
                            rsInvoice.updateString("QUALITY_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateString("FILLER",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            
                            Pointer+=96;
                            
                            rsInvoice.updateInt("BANK_CLASS", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;
                            rsInvoice.updateInt("BANK_CODE", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("HUNDI_NO",FileRecord.substring(Pointer,Pointer+7)); Pointer+=7;
                            rsInvoice.updateDouble("GROSS_WEIGHT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+5))/100); Pointer+=5;
                            rsInvoice.updateInt("TRANSPORTER_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            rsInvoice.updateString("BNG_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateString("INS_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            
                            Pointer+=1; //INV-TYPE
                            
                            rsInvoice.updateDouble("COLUMN_20_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_21_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_22_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_23_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateBoolean("CANCELLED",false);
                            
                            String DueDate = "";
                            if(ChargeCode.startsWith("2")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("4")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+15+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("5")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+35, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("8")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            }
                            else if(ChargeCode.startsWith("1")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, (30+10), "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("3")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 3, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("6")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 180, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("7")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("9")) {
                                DueDate = InvDate;
                            }
                            
                            rsInvoice.updateString("DUE_DATE",DueDate);
                            
                            rsInvoice.updateString("COLUMN_1_CAPTION","CE DUTY");
                            rsInvoice.updateString("COLUMN_9_CAPTION","INS CHRG");
                            rsInvoice.updateString("COLUMN_10_CAPTION","BANK CHRG");
                            rsInvoice.updateString("COLUMN_14_CAPTION","GST AMOUNT");
                            
                            rsInvoice.insertRow();
                            if(ChargeCode.startsWith("9")) {
                                double availableAmount  = clsAccount.get09AmountByParty("210027", Party_code, InvDate);
                                if(InvoiceAmount > availableAmount) {
                                    canPostSJ=false;
                                    AutoAdj=false;
                                    data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" of party "+ Party_code +" with Invoice Amount is ("+InvoiceAmount+") and advance amount is ("+availableAmount+") \n" +
                                    "before invoice date, so invoice not imported.";
                                    log.logToFile(LogFileName, Msg, 2);
                                   System.out.println("Invoice not Imported ");
                                } else {
                                    canPostSJ=true;
                                    AutoAdj=true;
                                    //System.out.println("Invoice Imported ");
                                }
                            } else {
                                canPostSJ=true;
                                AutoAdj=false;
                                System.out.println("Invoice Imported ");
                            }
                            
                            
                            if(pPostSJ && canPostSJ) {
                                System.out.println("Posting SJ");
                                objInvoice=(clsSalesInvoice)objInvoice.getObject(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,dbURL);
                                
                                if(objInvoice.PostSJTypeSuiting(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,AutoAdj)) {
                                    System.out.println("Invoice "+InvoiceNo+" Posted.");
                                    Msg = "SJ has been posted for Invoice No ("+InvNo+") on date "+InvDate;
                                    log.logToFile(LogFileName, Msg, 1);
                                } else {
                                    Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(objInvoice.LastError);
                                }
                            }
                        } else {
                            Msg = "Invoice no ("+InvNo+") on date "+InvDate+" party code does not exists";
                            log.logToFile(LogFileName, Msg, 2);
                        }
                    }
                } catch(Exception c){
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done=true;
                }
            }
            System.out.println("Finished");
        } catch(Exception e) {
        }
    }
    
    public void ImportInvoicesFilter(String invoiceFile, boolean pPostSJ) {
        
        boolean Done=false;
        boolean canPostSJ=false;
        boolean AutoAdj=false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Filter/Inv";
        long Counter=0;
        
        try {
            
            clsSalesInvoice objInvoice=new clsSalesInvoice();
            
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()),"0",2);
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2);
            LogFileName = LogFileName+Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2)+".log";
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER LIMIT 1");
            int Pointer=0;
            
            clsLogFile log = new clsLogFile();
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                  //  String InvoiceNo="B"+FileRecord.substring(1,7);
                    
                    String InvoiceNo="FF/"+FileRecord.substring(1,7);
                 
                    
                    
                    // Added by mrugesh for auto adjustment start
                    String InvNo = InvoiceNo;
                    String InvDate = "";
                    String ChargeCode = "";
                    String Party_code="";
                    double InvoiceAmount = 0;
                    canPostSJ=false;
                    AutoAdj=false;
                    // Added by mrugesh for auto adjustment end
                    
                    
                    Pointer+=1; //MIIN-ACC-TYPE
                    Pointer+=6; //MIIN-ACC-SRNO
                    Pointer+=7; //MIIN-QLTY-NO1
                    Pointer+=2; //MIIN-PATT-CD
                    Pointer+=8; //MIIN-PIEC-NO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //MIIN-INV-DATE
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                    
                    // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                    /*if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
                        continue;
                    }*/
                    // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                    
                    if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE=3 AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ")) {
                        continue;
                    }
                    String str = "SELECT * FROM D_SAL_INVOICE_HEADER "+
                    " WHERE INVOICE_NO='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' "; //AND CANCELLED=0 
                    
                    if(!data.IsRecordExist(str,dbURL)) {
                        
                        Pointer=0;
                        Pointer += 34;
                        Party_code= FileRecord.substring(Pointer,Pointer+6);
                        if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"' AND MAIN_ACCOUNT_CODE='210072' ",FinanceGlobal.FinURL)) {
                            
                            System.out.println("Importing Invoice "+InvoiceNo);
                            
                            Pointer=0;
                            
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",3); Pointer+=1; //MIIN-ACC-TYPE
                            //rsInvoice.updateString("INVOICE_NO",FileRecord.substring(Pointer,Pointer+6));  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //MIIN-QLTY-NO1 - S-T-DIGIT / OTHER-DIGIT
                            rsInvoice.updateString("PATTERN_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //MIIN-PATT-CD
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8; //MIIN-PIEC-NO
                            
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            InvDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                            
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2));
                            rsInvoice.updateInt("AGENT_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4;
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            rsInvoice.updateString("STATION_CODE", FileRecord.substring(Pointer,Pointer+15)); Pointer+=15;
                            rsInvoice.updateString("PAYMENT_TERM",FileRecord.substring(Pointer,Pointer+26)); Pointer+=26;
                            
                            //Pointer++; //MIIN-BANK-CH
                            //rsInvoice.updateInt("TRANSPORT_MODE",Integer.parseInt(FileRecord.substring(Pointer,Pointer+1))); Pointer++;
                            
                            ChargeCode = FileRecord.substring(Pointer,Pointer+2);
                            
                            rsInvoice.updateInt("PAYMENT_TERM_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("AGENT_LAST_INVOICE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;
                            rsInvoice.updateInt("AGENT_LAST_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+3))); Pointer+=3;
                            rsInvoice.updateInt("FIN_YEAR_FROM",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateInt("FIN_YEAR_TO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateInt("WAREHOUSE_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;
                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String PackingDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("PACKING_DATE", "20"+PackingDate.substring(4)+"-"+PackingDate.substring(2,4)+"-"+PackingDate.substring(0,2));
                            rsInvoice.updateString("EXPORT_SUB_CATEGORY",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;
                            rsInvoice.updateString("EXPORT_CATEGORY",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;
                            rsInvoice.updateString("GATEPASS_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String GatepassDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("GATEPASS_DATE", "20"+GatepassDate.substring(4)+"-"+GatepassDate.substring(2,4)+"-"+GatepassDate.substring(0,2));
                            rsInvoice.updateString("DRAFT_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            String DraftDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("DRAFT_DATE", "20"+DraftDate.substring(4)+"-"+DraftDate.substring(2,4)+"-"+DraftDate.substring(0,2));
                            
                            rsInvoice.updateDouble("COLUMN_1_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_2_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_3_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_4_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_5_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            rsInvoice.updateDouble("COLUMN_6_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;
                            
                            rsInvoice.updateDouble("CGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            
                            rsInvoice.updateDouble("SGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_7_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                           // rsInvoice.updateDouble("VAT4",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_10_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                           // rsInvoice.updateDouble("VAT1",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100);
                            rsInvoice.updateDouble("COLUMN_11_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                           // rsInvoice.updateDouble("CST5",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_13_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                          //  rsInvoice.updateDouble("CST2",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            
                            rsInvoice.updateDouble("IGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_14_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("TOTAL_SQ_MTR",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_GROSS_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            
                            Pointer+=2; //FLAG
                            
                            rsInvoice.updateDouble("TOTAL_NET_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            rsInvoice.updateDouble("TOTAL_GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            rsInvoice.updateDouble("COLUMN_15_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_16_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_17_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            rsInvoice.updateDouble("EXCISABLE_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;
                            rsInvoice.updateDouble("COLUMN_1_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_2_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_4_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_5_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_18_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;
                            rsInvoice.updateDouble("TOTAL_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;
                            InvoiceAmount = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7));
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;
                            
                            Pointer++;
                            
                            rsInvoice.updateString("IC_TYPE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateDouble("COLUMN_19_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;
                            rsInvoice.updateString("QUALITY_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateString("FILLER",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            
                            Pointer+=96;
                            
                            rsInvoice.updateInt("BANK_CLASS", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;
                            rsInvoice.updateInt("BANK_CODE", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("HUNDI_NO",FileRecord.substring(Pointer,Pointer+7)); Pointer+=7;
                            rsInvoice.updateDouble("GROSS_WEIGHT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+5))/100); Pointer+=5;
                            rsInvoice.updateInt("TRANSPORTER_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;
                            rsInvoice.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            rsInvoice.updateString("BNG_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            rsInvoice.updateString("INS_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;
                            
                            Pointer+=1; //INV-TYPE
                            
                            rsInvoice.updateDouble("COLUMN_20_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_21_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_22_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateDouble("COLUMN_23_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;
                            rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateBoolean("CANCELLED",false);
                            
                            rsInvoice.updateString("COLUMN_1_CAPTION","CE DUTY");
                            rsInvoice.updateString("COLUMN_9_CAPTION","INS CHRG");
                            rsInvoice.updateString("COLUMN_10_CAPTION","BANK CHRG");
                           // rsInvoice.updateString("COLUMN_14_CAPTION","GST AMOUNT");
                            
                            double cgst_per = 6.0;
                            double sgst_per = 6.0;
                            double igst_per = 12.0;                            
                            
                            rsInvoice.updateString("COLUMN_14_CAPTION","IGST AMOUNT");
                            rsInvoice.updateString("COLUMN_6_CAPTION","CGST AMOUNT");
                            rsInvoice.updateString("COLUMN_7_CAPTION","SGST AMOUNT");
                            
                            rsInvoice.updateDouble("CGST_PER",cgst_per);
                            rsInvoice.updateDouble("SGST_PER",sgst_per);
                            rsInvoice.updateDouble("IGST_PER",igst_per);
                            
                            
                            rsInvoice.insertRow();
                            
                          //  System.out.println("Invoice Imported ");
                            
                            if(ChargeCode.startsWith("9")) {
                                
                                /*data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                Msg = "Charge Code 9 Invoice not allowed to post till 31/05/2010";
                                log.logToFile(LogFileName, Msg, 2);
                                canPostSJ=false;
                                AutoAdj=false;*/
                                
                                double availableAmount  = clsAccount.get09AmountByParty("210072", Party_code, InvDate); 
                                if(InvoiceAmount > availableAmount) {
                                    canPostSJ=false;
                                    AutoAdj=false;
                                  data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" of party "+ Party_code +" with Invoice Amount is ("+InvoiceAmount+") and advance amount is ("+availableAmount+") \n" +
                                    "before invoice date, so invoice not imported.";
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(Msg);
                                    System.out.println("Invoice not Imported ");
                                    
                                } else {
                                    canPostSJ=true;
                                    AutoAdj=true;
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" is imported.";
                                    log.logToFile(LogFileName, Msg, 1);
                                    System.out.println("Invoice Imported 2");
                                }
                                
                            } else {
                                canPostSJ=true;
                                AutoAdj=false;
                                Msg = "Invoice No ("+InvNo+") on date "+InvDate+" is imported.";
                                log.logToFile(LogFileName, Msg, 1);
                                System.out.println("Invoice Imported3 ");
                            }
                            
                            if(pPostSJ && canPostSJ) {
                                
                                System.out.println("Posting SJ");
                                
                                objInvoice=(clsSalesInvoice)objInvoice.getObject(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,dbURL);
                                
                                if(objInvoice.PostSJTypeFilter(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,AutoAdj)) { 
                                    System.out.println("Invoice "+InvoiceNo+" Posted.");
                                    Msg = "SJ has been posted for Invoice No ("+InvNo+") on date "+InvDate;
                                    log.logToFile(LogFileName, Msg, 1);
                                } else {
                                    Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(objInvoice.LastError);
                                }
                            }
                        } else {
                            Msg = "Invoice no ("+InvNo+") on date "+InvDate+" party code does not exists";
                            log.logToFile(LogFileName, Msg, 2);
                        }
                    }
                }
                catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            System.out.println("Finished");
        } catch(Exception e) {
        }
    }
    
    public void ImportInvoicesFelt(String invoiceFile, boolean pPostSJ) {
        
        boolean Done=false;
        boolean canPostSJ=false;
        boolean AutoAdj=false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Felt/Inv";
        long Counter=0;
        
        try {
            
            clsSalesInvoice objInvoice=new clsSalesInvoice();
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()),"0",2);
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2);
            LogFileName = LogFileName+Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2)+".log";
            
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER LIMIT 1");
            int Pointer=0;
            
            clsLogFile log = new clsLogFile();
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            Done=false;
            
            while(!Done){
                
                try {
                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    String InvoiceNo="FE/"+FileRecord.substring(0,6);
                    
                    // Added by mrugesh for auto adjustment start
                    String InvNo = InvoiceNo;
                    String InvDate = "";
                    String ChargeCode = "";
                    String Party_code="";
                    String Filler="";
                    double InvoiceAmount = 0;
                    canPostSJ=false;
                    AutoAdj=false;
                    // Added by mrugesh for auto adjustment end
                    
                    
                    Pointer+=6; //MIIN-ACC-SRNO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                    
                    // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                    /*if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
                        continue;
                    }*/
                    // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                    
                    if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE=2 AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ")) {
                        continue;
                    }
                    
                    String str = "SELECT * FROM D_SAL_INVOICE_HEADER "+
                    " WHERE INVOICE_NO='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' "; 
                    
                    if(!data.IsRecordExist(str,dbURL)) {
                        
                        Pointer=0;
                        Pointer += 12;
                        Party_code= FileRecord.substring(Pointer,Pointer+6);
                        if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"' AND MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL)) {
                            
                            System.out.println("Importing Invoice "+InvoiceNo);
                            
                            Pointer=0;
                            
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",2);
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //MIIN-ACC-SRNO
                            
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            InvDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                            
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2));
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            
                            Pointer+=7;  //ITEM-CODE
                            
                            rsInvoice.updateString("PARTY_NAME",FileRecord.substring(Pointer,Pointer+17)); Pointer+=17;
                            rsInvoice.updateDouble("LENGTH",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            rsInvoice.updateDouble("WIDTH",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;
                            rsInvoice.updateString("ORDER_NO",FileRecord.substring(Pointer,Pointer+11)); Pointer+=11;
                            rsInvoice.updateDouble("COLUMN_13_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_tem_advance
                            rsInvoice.updateDouble("GROSS_WEIGHT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //fls_gross_wieght
                            rsInvoice.updateDouble("TOTAL_GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+10))/100); Pointer+=10;//fls_gross_value
                            rsInvoice.updateDouble("COLUMN_24_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_chem_trt_chg
                            rsInvoice.updateDouble("COLUMN_25_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_sprl-chg
                            double col3data=UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100;
                           // rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_tc_cess //RISHI
                          rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_tc_cess
                           // rsInvoice.updateDouble("COLUMN_4_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_sur_chg
                            rsInvoice.updateDouble("TOT_INV_SD_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_sur_chg
                          
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8; //fls_net_amount
                            //rsInvoice.updateDouble("COLUMN_5_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//FLS_TOTAL_EXCISE
                            rsInvoice.updateDouble("SGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100);//FLS_TOTAL_EXCISE
                            rsInvoice.updateDouble("COLUMN_1_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;//FLS_TOTAL_EXCISE
                            //rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_FREIGHT_CHG
                            rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_FREIGHT_CHG
                            //rsInvoice.updateDouble("COLUMN_7_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_INSURANCE_CHG
                            rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_INSURANCE_CHG
                            //rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_OEM_DIS_AMT                            
                            rsInvoice.updateDouble("COLUMN_8_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_OEM_DIS_AMT
                            //rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_HUNDI_CHG
                            rsInvoice.updateDouble("VAT1",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100);
                            rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_HUNDI_CHG
                           // rsInvoice.updateDouble("COLUMN_10_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;  //ang_chg
                            rsInvoice.updateDouble("SD_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;  //ang_chg
                           
                            rsInvoice.updateDouble("COLUMN_11_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;  // dis_amt
                            //rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_HNDLM_SLSCHG
                            rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;//FLS_HNDLM_SLSCHG
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+10))/100); Pointer+=10; //flstotal
                            InvoiceAmount = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8)); 
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))); Pointer+=8; //fls_rounded
                            
                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+8).trim()); Pointer+=8; //fls_cas_a
                            //Pointer+=8;
                            
                            rsInvoice.updateDouble("NO_OF_PIECES",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4; //FLS_NO_OF_PIECES
                            
                            String OrderDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //FLS_ORDER_DATE
                            
                            rsInvoice.updateString("ORDER_DATE", "20"+OrderDate.substring(4)+"-"+OrderDate.substring(2,4)+"-"+OrderDate.substring(0,2));
                            ChargeCode = FileRecord.substring(Pointer,Pointer+2); 
                            rsInvoice.updateInt("PAYMENT_TERM_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2; //FLS_CHG_CODE
                            rsInvoice.updateInt("TRANSPORTER_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2; //FLS_TRANSPORT_CODE
                            rsInvoice.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;//FLS_LC_NO
                            rsInvoice.updateString("FILLER",FileRecord.substring(Pointer,Pointer+2));  //FLS_FILLER
                            Filler = FileRecord.substring(Pointer,Pointer+2);  //FLS_FILLER
                            
                            rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateBoolean("CANCELLED",false);
                            
                            String DueDate = "";
                            if(ChargeCode.startsWith("2")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210010")+15, "yyyy-MM-dd");
                                
                            } else if(ChargeCode.startsWith("8")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210010")+6, "yyyy-MM-dd");
                            }
                            else if(ChargeCode.startsWith("1")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 30, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("4")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210010"), "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("7")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("9")) {
                                DueDate = InvDate;
                            }
                            
                            
                            
                            rsInvoice.updateString("DUE_DATE",DueDate);
                            //rsInvoice.updateString("COLUMN_1_CAPTION","CE DUTY"); //RISHI 
                            rsInvoice.updateString("COLUMN_1_CAPTION","SGST_AMT");
                            rsInvoice.updateString("COLUMN_3_CAPTION","IGST_CGST_AMT");
                            rsInvoice.updateString("COLUMN_9_CAPTION","INS CHRG");
                            rsInvoice.updateString("COLUMN_10_CAPTION","BANK CHRG");
                            rsInvoice.updateString("COLUMN_14_CAPTION","GST AMOUNT");
                            rsInvoice.updateString("COLUMN_8_CAPTION","VAT1");
                           // rsInvoice.updateString("COLUMN_10_CAPTION","VAT1");
                            
                            
                            /*
                            if(Filler.equals("TV")){
                                   rsInvoice.updateDouble("VAT4",col3data); 
                            }
                            if(Filler.equals("RC")){
                                     rsInvoice.updateDouble("CST2",col3data);
                            }
                            if(Filler.equals("RV")){
                                     rsInvoice.updateDouble("CST5",col3data);
                            }                            
                            
                            */
                            
                            Double cgst_per = 6.00;
                            Double sgst_per = 6.00;
                            Double igst_per = 12.00;
                            
                            if(Party_code.substring(0,3).equals("831")){
                                rsInvoice.updateDouble("CGST_AMT",col3data);
                                rsInvoice.updateDouble("CGST_PER",cgst_per);
                                rsInvoice.updateDouble("SGST_PER",sgst_per);
                                
                            }else{
                                //String tinno=data.getStringValueFromDB("SELECT TIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"'");
                                //if(tinno.equals("") || tinno.equals("applied") || tinno.equals("null")){
                                //    rsInvoice.updateDouble("CST5",col3data);
                                //}else{
                                    rsInvoice.updateDouble("IGST_AMT",col3data);
                                      rsInvoice.updateDouble("IGST_PER",igst_per);
                           
                           
                                //}
                            }
                            
                            
                            rsInvoice.insertRow();
                            
                            if(ChargeCode.startsWith("9")) {
                                //Remove the comment for Not posting 09 prathmesh 03-10-2010
                                /*
                                data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                Msg = "Charge Code 9 Invoice not allowed to post till 31/05/2010";
                                log.logToFile(LogFileName, Msg, 2);
                                canPostSJ=false;
                                AutoAdj=false;
                                 */
                                // Remove the comment for Not posting 09 prathmesh 03-10-2010
                                
                                double availableAmount  = clsAccount.get09AmountByParty("210010", Party_code, InvDate); 
                                 
                                if(InvoiceAmount > availableAmount) {
                                    canPostSJ=false;
                                    AutoAdj=false;
                                    data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" of party "+ Party_code +" with Invoice Amount is ("+InvoiceAmount+") and advance amount is ("+availableAmount+") \n" +
                                    "before invoice date, so invoice not imported.";
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(Msg);
                                    System.out.println("Invoice not Imported ");
                                } else {
                                    canPostSJ=true;
                                    AutoAdj=true;
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" is imported.";
                                    log.logToFile(LogFileName, Msg, 1);
                                    System.out.println("Invoice Imported ");
                                }
                                 // Remove the comment for posting 09 prathmesh 03-10-2010 end
                            } else {
                                canPostSJ=true;
                                AutoAdj=false;
                                Msg = "Invoice No ("+InvNo+") on date "+InvDate+" is imported.";
                                log.logToFile(LogFileName, Msg, 1);
                                System.out.println("Invoice Imported ");
                            }
                            
                            if(pPostSJ && canPostSJ) {
                                System.out.println("Posting SJ");
                                
                                objInvoice=(clsSalesInvoice)objInvoice.getObject(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,dbURL);
                                
                                 System.out.println("dddd 1");
                                 System.out.println("Invoice "+InvoiceNo+" 1");
                                 System.out.println("Invoice Date "+InvDate+" 1");
                                
                                if(objInvoice.PostSJTypeFelt(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,AutoAdj)) {
                                    Msg = "SJ has been posted for Invoice No ("+InvNo+") on date "+InvDate;
                                    log.logToFile(LogFileName, Msg, 1);
                                    System.out.println("Invoice "+InvoiceNo+" Posted.");
                                } else {
                                    Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(objInvoice.LastError);
                                }
                            }
                        } else {
                            Msg = "Invoice no ("+InvNo+") on date "+InvDate+" party code does not exists";
                            log.logToFile(LogFileName, Msg, 2);
                        }
                    }
                }
                catch(Exception c){
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done=true;
                }
            }
            System.out.println("Finished");
        } catch(Exception e) {
        }
    }
    
    
      public void ImportInvoicesBlanket(String invoiceFile, boolean pPostSJ) {
        
        boolean Done=false;
        boolean canPostSJ=false;
        boolean AutoAdj=false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Blanket/Inv";
        long Counter=0;
        
        try {
            clsSalesInvoice objInvoice = new clsSalesInvoice();
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()),"0",2);
            LogFileName = LogFileName+EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()),"0",2);
            LogFileName = LogFileName+Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2)+".log";
            
            Connection objConn=data.getConn(dbURL);
            Statement stInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice=stInvoice.executeQuery("SELECT * FROM D_SAL_INVOICE_HEADER LIMIT 1");
            int Pointer=0;
            
            clsLogFile log = new clsLogFile();
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    //String InvoiceNo=FileRecord.substring(1,7); 
                    String InvoiceNo="BL/"+FileRecord.substring(1,7);
                    
                  //  String InvoiceNo="FE"+FileRecord.substring(0,6);
                    // Added by mrugesh for auto adjustment start
                    String InvNo = InvoiceNo;
                    String InvDate = "";
                    String ChargeCode = "";
                    String Party_code="";
                    double InvoiceAmount = 0;
                    canPostSJ=false;
                    AutoAdj=false;
                    // Added by mrugesh for auto adjustment end
                    
                    Pointer+=1; //MIIN-ACC-TYPE
                    Pointer+=6; //MIIN-ACC-SRNO
                    Pointer+=7; //MIIN-QLTY-NO1
                    Pointer+=2; //MIIN-PATT-CD
                    Pointer+=8; //MIIN-PIEC-NO
                    String InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //MIIN-INV-DATE
                    InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
//                    if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
//                        continue;
//                    }
//                    if(data.IsRecordExist("SELECT * FROM D_SAL_INVOICE_CANCEL WHERE INVOICE_TYPE=1 AND INVOICE_NO='"+InvoiceNo+"' AND INVOICE_DATE='"+InvoiceDate+"' ")) {
//                        continue;
//                    }
                    String str = "SELECT * FROM D_SAL_INVOICE_HEADER "+
                    " WHERE INVOICE_NO='"+ InvoiceNo +"' AND INVOICE_DATE='"+InvoiceDate+"' ";
                    if(InvoiceDate.equals("2011-06-17") || InvoiceDate.equals("2011-06-18")) {
                        boolean halt=true;
                        continue;
                    }
                    if(!data.IsRecordExist(str,dbURL)) {
                        Pointer=0;
                        Pointer += 34;
                        Party_code= FileRecord.substring(Pointer,Pointer+6);
                        if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+Party_code+"' AND MAIN_ACCOUNT_CODE='210027' AND APPROVED=1 AND CANCELLED=0 ",FinanceGlobal.FinURL)) {
                            
                            System.out.println("Importing Invoice "+InvoiceNo);
                            
                            Pointer=0;
                            
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            rsInvoice.updateInt("INVOICE_TYPE",1); Pointer+=1; //MIIN-ACC-TYPE   //1
                            //rsInvoice.updateString("INVOICE_NO",FileRecord.substring(Pointer,Pointer+6));  Pointer+=6; //MIIN-ACC-SRNO
                            rsInvoice.updateString("INVOICE_NO",InvoiceNo);  Pointer+=6; //MIIN-ACC-SRNO  /2-7
                            rsInvoice.updateString("QUALITY_NO",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //MIIN-QLTY-NO1 - S-T-DIGIT / OTHER-DIGIT  //8-14
                            rsInvoice.updateString("PATTERN_CODE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2; //MIIN-PATT-CD //15-16
                            rsInvoice.updateString("PIECE_NO",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8; //MIIN-PIEC-NO      //17-24
                            
                            InvoiceDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            InvDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);
                            
                            rsInvoice.updateString("INVOICE_DATE", "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2)); //25-30
                            rsInvoice.updateInt("AGENT_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+4))); Pointer+=4;    //31-34
                            rsInvoice.updateString("PARTY_CODE",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;              //35-40
                            rsInvoice.updateString("STATION_CODE", FileRecord.substring(Pointer,Pointer+15)); Pointer+=15;         //41-55
                            rsInvoice.updateString("PAYMENT_TERM",FileRecord.substring(Pointer,Pointer+26)); Pointer+=26;          //56-81
                            
                            //Pointer++; //MIIN-BANK-CH
                            //rsInvoice.updateInt("TRANSPORT_MODE",Integer.parseInt(FileRecord.substring(Pointer,Pointer+1))); Pointer++;
                            ChargeCode = FileRecord.substring(Pointer,Pointer+2);             
                            
                            rsInvoice.updateInt("PAYMENT_TERM_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2; //82-83
                            rsInvoice.updateString("AGENT_LAST_INVOICE",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;    //84-85
                            rsInvoice.updateInt("AGENT_LAST_SR_NO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+3))); Pointer+=3;   //86-88
                            rsInvoice.updateInt("FIN_YEAR_FROM",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;     //89-90
                            rsInvoice.updateInt("FIN_YEAR_TO",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;       //93-92
                            rsInvoice.updateInt("WAREHOUSE_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;      //93
                            rsInvoice.updateString("BALE_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;                       //96-99
                            
                            String PackingDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;     
                            
                            rsInvoice.updateString("PACKING_DATE", "20"+PackingDate.substring(4)+"-"+PackingDate.substring(2,4)+"-"+PackingDate.substring(0,2));   //102-105
                            rsInvoice.updateString("EXPORT_SUB_CATEGORY",FileRecord.substring(Pointer,Pointer+8)); Pointer+=8;      //108-113
                            rsInvoice.updateString("EXPORT_CATEGORY",FileRecord.substring(Pointer,Pointer+3)); Pointer+=3;           //116-116
                            rsInvoice.updateString("GATEPASS_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;              // 119-122
                            
                            String GatepassDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                            
                            rsInvoice.updateString("GATEPASS_DATE", "20"+GatepassDate.substring(4)+"-"+GatepassDate.substring(2,4)+"-"+GatepassDate.substring(0,2)); //125-128
                            rsInvoice.updateString("DRAFT_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;     //131-134
                            
                            String DraftDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; 
                            
                            rsInvoice.updateString("DRAFT_DATE", "20"+DraftDate.substring(4)+"-"+DraftDate.substring(2,4)+"-"+DraftDate.substring(0,2));   //137-140
                            
                            rsInvoice.updateDouble("COLUMN_1_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //143-144
                            rsInvoice.updateDouble("COLUMN_2_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //147-148
                            rsInvoice.updateDouble("COLUMN_3_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //151-152
                            rsInvoice.updateDouble("COLUMN_4_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //155-156
                            rsInvoice.updateDouble("COLUMN_5_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //159-160
                            rsInvoice.updateDouble("COLUMN_6_PER",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4;         //163-164
                            
                            rsInvoice.updateDouble("CGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_6_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //165-171
                            
                            rsInvoice.updateDouble("SGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_7_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //172-178
                            
                            
                            rsInvoice.updateDouble("COLUMN_8_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //179-185
                            rsInvoice.updateDouble("COLUMN_9_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //186-192
                            rsInvoice.updateDouble("COLUMN_10_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //193-199
                            rsInvoice.updateDouble("VAT1",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100);
                            rsInvoice.updateDouble("COLUMN_11_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;         //200-205                        
                            rsInvoice.updateDouble("COLUMN_12_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //206-212
                            rsInvoice.updateDouble("CST5",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_13_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;          //215-219
                            
                            
                            rsInvoice.updateDouble("IGST_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100);
                            rsInvoice.updateDouble("COLUMN_14_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;         //220-226
                            
                            
                            rsInvoice.updateDouble("TOTAL_SQ_MTR",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;          //229-236
                            rsInvoice.updateDouble("TOTAL_KG",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;              //237-244
                            rsInvoice.updateDouble("TOTAL_GROSS_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;         //245-250
                            
                            Pointer+=2; //FLAG                                 //251-252
                            
                            rsInvoice.updateDouble("TOTAL_NET_QTY",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6;            //253-258
                            rsInvoice.updateDouble("TOTAL_GROSS_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;       //259-267
                            rsInvoice.updateDouble("COLUMN_15_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //268-275
                            rsInvoice.updateDouble("COLUMN_16_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //276-283
                            rsInvoice.updateDouble("COLUMN_17_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;             //284-291
                            rsInvoice.updateDouble("TOTAL_NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;          //292-300
                            rsInvoice.updateDouble("EXCISABLE_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))/100); Pointer+=8;           //301-308
                            rsInvoice.updateDouble("COLUMN_1_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //309-315
                            rsInvoice.updateDouble("COLUMN_2_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //316-322
                            rsInvoice.updateDouble("COLUMN_3_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //323-329
                            rsInvoice.updateDouble("COLUMN_4_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;             //330-336
                            rsInvoice.updateDouble("COLUMN_5_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;              //337-343
                            rsInvoice.updateDouble("COLUMN_18_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))/100); Pointer+=7;             //344-350
                            rsInvoice.updateDouble("TOTAL_VALUE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+9))/100); Pointer+=9;               //351-359
                            InvoiceAmount = UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7));
                            rsInvoice.updateDouble("NET_AMOUNT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;                 //360-366
                            
                            Pointer++;                                   //367
                            
                            rsInvoice.updateString("IC_TYPE",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                                      //368
                            rsInvoice.updateDouble("COLUMN_19_AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7))); Pointer+=7;            //369-375
                            rsInvoice.updateString("QUALITY_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                            //376
                            rsInvoice.updateString("FILLER",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                                      //377
                            
                            Pointer+=96;                                                                                                               //378-472
                            
                            rsInvoice.updateInt("BANK_CLASS", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;                //473
                            rsInvoice.updateInt("BANK_CODE", UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;                //474-475
                            rsInvoice.updateString("HUNDI_NO",FileRecord.substring(Pointer,Pointer+7)); Pointer+=7;                                   //476-482     
                            rsInvoice.updateDouble("GROSS_WEIGHT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+5))/100); Pointer+=5;       //483-487
                            rsInvoice.updateInt("TRANSPORTER_CODE",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+2))); Pointer+=2;          //488-489
                            rsInvoice.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+6)); Pointer+=6;                                      //490-495   
                            rsInvoice.updateString("BNG_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                              //496
                            rsInvoice.updateString("INS_INDICATOR",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;                              //497
                            
                            Pointer+=1; //INV-TYPE                                                                                                   //498     
                            rsInvoice.updateDouble("CASH_DISC_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100);  
                            rsInvoice.updateDouble("COLUMN_20_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;    //499-505
                               //499-505
                            
                            rsInvoice.updateDouble("COLUMN_21_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;     //506-512
                            rsInvoice.updateDouble("COLUMN_22_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;     //513-519
                            rsInvoice.updateDouble("COLUMN_23_AMT", UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+7) )/100); Pointer+=7;    //520-526
                            rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateBoolean("CANCELLED",false);
                            
                            String DueDate = "";
                            if(ChargeCode.startsWith("2")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("4")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+15+6, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("5")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+35, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("8")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsSalesInvoice.getCreditDays(Party_code, "210027")+5+6, "yyyy-MM-dd");
                            }
                            else if(ChargeCode.startsWith("1")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, (30+10), "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("3")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 3, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("6")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 180, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("7")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if(ChargeCode.startsWith("9")) {
                                DueDate = InvDate;
                            }
                            
                            
                            double cgst_per = 2.50;
                            double sgst_per = 2.50;
                            double igst_per = 5.00;
                            
                            rsInvoice.updateString("DUE_DATE",DueDate);
                            
                            rsInvoice.updateString("COLUMN_1_CAPTION","CE DUTY");
                            rsInvoice.updateString("COLUMN_9_CAPTION","INS CHRG");
                            rsInvoice.updateString("COLUMN_10_CAPTION","BANK CHRG");
                            rsInvoice.updateString("COLUMN_14_CAPTION","IGST AMOUNT");
                            rsInvoice.updateString("COLUMN_6_CAPTION","CGST AMOUNT");
                            rsInvoice.updateString("COLUMN_7_CAPTION","SGST AMOUNT");
                            
                            rsInvoice.updateString("COLUMN_20_CAPTION","CASH DISCOUNT AMOUNT");
                            
                            rsInvoice.updateDouble("CGST_PER",cgst_per);
                            rsInvoice.updateDouble("SGST_PER",sgst_per);
                            rsInvoice.updateDouble("IGST_PER",igst_per);
                            
                            
                             
                            
                            rsInvoice.insertRow();
                            if(ChargeCode.startsWith("9")) {
                                double availableAmount  = clsAccount.get09AmountByParty("210027", Party_code, InvDate);
                                if(InvoiceAmount > availableAmount) {
                                    canPostSJ=false;
                                    AutoAdj=false;
                                    data.Execute("DELETE FROM D_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+InvNo+"' AND INVOICE_DATE='"+InvDate+"' ");
                                    Msg = "Invoice No ("+InvNo+") on date "+InvDate+" of party "+ Party_code +" with Invoice Amount is ("+InvoiceAmount+") and advance amount is ("+availableAmount+") \n" +
                                    "before invoice date, so invoice not imported.";
                                    log.logToFile(LogFileName, Msg, 2);
                                   System.out.println("Invoice not Imported ");
                                } else {
                                    canPostSJ=true;
                                    AutoAdj=true;
                                    //System.out.println("Invoice Imported ");
                                }
                            } else {
                                canPostSJ=true;
                                AutoAdj=false;
                                System.out.println("Invoice Imported ");
                            }
                            
                            
                            if(pPostSJ && canPostSJ) {
                                System.out.println("Posting SJ");
                                objInvoice=(clsSalesInvoice)objInvoice.getObject(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,dbURL);
                                
                                if(objInvoice.PostSJTypeSuiting(EITLERPGLOBAL.gCompanyID,InvoiceNo,InvDate,AutoAdj)) {
                                    System.out.println("Invoice "+InvoiceNo+" Posted.");
                                    Msg = "SJ has been posted for Invoice No ("+InvNo+") on date "+InvDate;
                                    log.logToFile(LogFileName, Msg, 1);
                                } else {
                                    Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println(objInvoice.LastError);
                                }
                            }
                        } else {
                            Msg = "Invoice no ("+InvNo+") on date "+InvDate+" party code does not exists";
                            log.logToFile(LogFileName, Msg, 2);
                        }
                    }
                } catch(Exception c){
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done=true;
                }
            }
            System.out.println("Finished");
        } catch(Exception e) {
        }
    }
    

    
    
    
}
