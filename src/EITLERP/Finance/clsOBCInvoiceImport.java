/*
 * clsSOImport.java
 *
 * Created on July 21, 2008, 1:31 PM
 */

package EITLERP.Finance;

import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  Prathmesh Shah
 * DON'T ASK QUESTIONS IF YOU FIND ANY ERRORS. QUESTIONS ARE STRICTLY PROHIBITED.
 */
public class clsOBCInvoiceImport {
    
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    public clsOBCInvoiceImport() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            
            /*if(args.length<=0) {
                System.out.println("Please specify the directory to put exported file");
                return;
            }*/
            
            clsOBCInvoiceImport objImport=new clsOBCInvoiceImport();
            
            //objImport.ImportOBCInvoice(args[0]);
            //objImport.ImportOBCInvoice("/transfer/OBCInv.txt");
            //objImport.ImportOBCInvoice("/data/OBCMIGRATION/bnst08411.txt");
            //objImport.ImportOBCInvoice("/data/OBCMIGRATION/bnst0812.txt");
            objImport.ImportOBCInvoice("/data/OBCMIGRATION/bnft_felt_0809.lin");
            System.out.println("THE END");
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ImportOBCInvoice(String invoiceFile) {
        
        boolean Done=false;
        long Counter=0;
        
        try {
            String dbFinURL="jdbc:mysql://200.0.0.227:3306/FINANCE";
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            Connection objConn=data.getConn(dbFinURL);
            
            Statement stOBCInvoice=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBCInvoice=stOBCInvoice.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER WHERE DOC_NO='1' ");
            
            Statement stOBCInvoiceDtl=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBCInvoiceDtl=stOBCInvoiceDtl.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL WHERE DOC_NO='1' ");
            
            Statement stOBCInvoice_h=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBCInvoice_h=stOBCInvoice_h.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_HEADER_H WHERE DOC_NO='1' ");
            
            Statement stOBCInvoiceDtl_h=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBCInvoiceDtl_h=stOBCInvoiceDtl_h.executeQuery("SELECT * FROM D_FIN_OBC_INVOICE_DETAIL_H WHERE DOC_NO='1' ");
            
            int Pointer=0;
            String Invoice_no,Invoice_date,Amount,Bank_ref_No,Bank_ref_date,Main_acc_code,Party_code,nDoc_no="",nBank_ref_no="";
            int cnt = 1;
            int CompanyID = 2;
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                try {
                    
                    String FileRecord=aFile.readLine();
                    
                    String qry = "SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID = " + clsOBCInvoice.ModuleID;
                    ResultSet rsFF = data.getResult(qry,dbURL);
                    rsFF.first();
                    
                    if(rsFF.getRow()>0) {
                        SelPrefix=rsFF.getString("PREFIX_CHARS");
                        SelSuffix=rsFF.getString("SUFFIX_CHARS");
                        FFNo=rsFF.getInt("FIRSTFREE_NO");
                    }
                    String Doc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBCInvoice.ModuleID,FFNo,false);
                    
                    int Hierarchy_id = data.getIntValueFromDB("SELECT HIERARCHY_ID FROM D_COM_HIERARCHY WHERE MODULE_ID=" + clsOBCInvoice.ModuleID + " LIMIT 1" ,dbURL);
                    
                    Pointer=0;
                    Pointer+=4;//BNG_DIV_CODE,BNG_BOOK_CODE
                    Pointer+=6;//BNG_BANK_CODE
                    Pointer+=6;//BNG_DATE
                    Pointer+=2;//BNG_REF_ALPHA
                    Pointer+=6;//BNG_REF_NO
                    Invoice_no = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BNG_INV_NO
                    Pointer+=2;//BNG_AG_ALPHA
                    Pointer+=4;//BNG_AG_SR
                    Pointer+=2;//BNG_YR
                    Main_acc_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BNG_ACC_CODE
                    Party_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BNG_PARTY_CODE
                    Pointer+=20;//BNG_PARTY_NAME
                    Pointer+=10;//BNG_STATION
                    Amount = FileRecord.substring(Pointer,Pointer+10);
                    Pointer+=10;//BNG_AMOUNT
                    Amount = Amount + "." + FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//BNG_AMOUNT
                    Bank_ref_date = ConvertDateDB(FileRecord.substring(Pointer,Pointer+6));
                    Invoice_date = ConvertDateDB(FileRecord.substring(Pointer,Pointer+6));
                    Pointer+=6;//BNG_INV_DATE
                    Pointer+=2;//BNG_DD_HUN_ALPHA
                    Pointer+=6;//BNG_DD_HUN_NO
                    Pointer+=1;//BNG_FL1
                    Bank_ref_No = FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//BNG_ADV_ALPHA
                    Bank_ref_No = Bank_ref_No + FileRecord.substring(Pointer,Pointer+6);
                    
                    Bank_ref_No = Bank_ref_No.replace(" ", "");
                    String sBank_ref_No = "";
                    sBank_ref_No = Bank_ref_No.substring(2);
                    int lenRefNo = sBank_ref_No.length();
                    if (lenRefNo < 6) {
                        Bank_ref_No = Bank_ref_No.substring(0,2) + EITLERPGLOBAL.padLeft(6,sBank_ref_No,"0");
                    }
                    
                    Pointer+=6;//BNG_ADV_NO
                    Pointer+=2;//BNG_BANK_CD
                    Pointer+=2;//BNG_BANK_NO
                    Pointer+=4;//FILLER
                    
                    
                    if (!nBank_ref_no.equals(Bank_ref_No)) {
                        if(!data.IsRecordExist("SELECT DOC_NO FROM D_FIN_OBC_INVOICE_HEADER WHERE DOC_NO='"+Doc_no+"'",dbFinURL)) {
                            
                            System.out.println("Importing Doc No "+Doc_no);
                            nDoc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBCInvoice.ModuleID,FFNo,true);
                            
                            rsOBCInvoice.moveToInsertRow();
                            rsOBCInvoice.updateLong("COMPANY_ID",CompanyID);
                            rsOBCInvoice.updateString("DOC_NO",nDoc_no);
                            //rsOBCInvoice.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice.updateString("DOC_DATE", Bank_ref_date);
                            rsOBCInvoice.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                            rsOBCInvoice.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                            rsOBCInvoice.updateString("MAIN_ACCOUNT_CODE",Main_acc_code);
                            rsOBCInvoice.updateString("PARTY_CODE",Party_code);
                            rsOBCInvoice.updateString("REMARKS","");
                            rsOBCInvoice.updateLong("HIERARCHY_ID",Hierarchy_id);
                            rsOBCInvoice.updateString("CREATED_BY","admin" );
                            rsOBCInvoice.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice.updateString("MODIFIED_BY","admin");
                            rsOBCInvoice.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice.updateBoolean("CHANGED",true);
                            rsOBCInvoice.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice.updateBoolean("APPROVED",true);
                            rsOBCInvoice.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice.updateBoolean("REJECTED",false);
                            rsOBCInvoice.updateString("REJECTED_DATE","0000-00-00");
                            rsOBCInvoice.updateBoolean("CANCELLED",false);
                            rsOBCInvoice.insertRow();
                            
                            rsOBCInvoice_h.moveToInsertRow();
                            rsOBCInvoice_h.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                            rsOBCInvoice_h.updateString("UPDATED_BY","admin" );
                            rsOBCInvoice_h.updateString("APPROVAL_STATUS","F");
                            rsOBCInvoice_h.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateString("APPROVER_REMARKS","");
                            rsOBCInvoice_h.updateLong("COMPANY_ID",CompanyID);
                            rsOBCInvoice_h.updateString("DOC_NO",nDoc_no);
                            //rsOBCInvoice_h.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateString("DOC_DATE", Bank_ref_date);
                            rsOBCInvoice_h.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                            rsOBCInvoice_h.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                            rsOBCInvoice_h.updateString("MAIN_ACCOUNT_CODE",Main_acc_code);
                            rsOBCInvoice_h.updateString("PARTY_CODE",Party_code);
                            rsOBCInvoice_h.updateString("REMARKS","");
                            rsOBCInvoice_h.updateLong("HIERARCHY_ID",Hierarchy_id);
                            rsOBCInvoice_h.updateString("CREATED_BY","admin");
                            rsOBCInvoice_h.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateString("MODIFIED_BY","admin");
                            rsOBCInvoice_h.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateBoolean("CHANGED",true);
                            rsOBCInvoice_h.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateBoolean("APPROVED",true);
                            rsOBCInvoice_h.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoice_h.updateBoolean("REJECTED",false);
                            rsOBCInvoice_h.updateString("REJECTED_DATE","0000-00-00");
                            rsOBCInvoice_h.updateBoolean("CANCELLED",false);
                            rsOBCInvoice_h.insertRow();
                            
                            nBank_ref_no = Bank_ref_No;
                            
                            cnt  = 1 ;
                            
                            rsOBCInvoiceDtl.moveToInsertRow();
                            rsOBCInvoiceDtl.updateInt("COMPANY_ID",CompanyID);
                            rsOBCInvoiceDtl.updateString("DOC_NO",nDoc_no);
                            rsOBCInvoiceDtl.updateInt("SR_NO",cnt);
                            rsOBCInvoiceDtl.updateString("INVOICE_NO",Invoice_no);
                            rsOBCInvoiceDtl.updateString("INVOICE_DATE",Invoice_date);
                            rsOBCInvoiceDtl.updateDouble("INVOICE_AMOUNT",Double.parseDouble(Amount));
                            rsOBCInvoiceDtl.updateString("REMARKS","");
                            rsOBCInvoiceDtl.updateString("CREATED_BY","admin");
                            rsOBCInvoiceDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl.updateString("MODIFIED_BY","admin");
                            rsOBCInvoiceDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl.updateBoolean("CHANGED",true);
                            rsOBCInvoiceDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl.updateInt("CANCELLED",0);
                            rsOBCInvoiceDtl.insertRow();
                            
                            rsOBCInvoiceDtl_h.moveToInsertRow();
                            rsOBCInvoiceDtl_h.updateInt("REVISION_NO",1);
                            rsOBCInvoiceDtl_h.updateInt("COMPANY_ID",CompanyID);
                            rsOBCInvoiceDtl_h.updateString("DOC_NO",nDoc_no);
                            rsOBCInvoiceDtl_h.updateInt("SR_NO",cnt);
                            rsOBCInvoiceDtl_h.updateString("INVOICE_NO",Invoice_no);
                            rsOBCInvoiceDtl_h.updateString("INVOICE_DATE",Invoice_date);
                            rsOBCInvoiceDtl_h.updateDouble("INVOICE_AMOUNT",Double.parseDouble(Amount));
                            rsOBCInvoiceDtl_h.updateString("REMARKS","");
                            rsOBCInvoiceDtl_h.updateString("CREATED_BY","admin");
                            rsOBCInvoiceDtl_h.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl_h.updateString("MODIFIED_BY","admin");
                            rsOBCInvoiceDtl_h.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl_h.updateBoolean("CHANGED",true);
                            rsOBCInvoiceDtl_h.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBCInvoiceDtl_h.updateInt("CANCELLED",0);
                            rsOBCInvoiceDtl_h.insertRow();
                            
                            System.out.println("Invoice Imported ");
                        }
                    } else {
                        cnt  = cnt + 1;
                        rsOBCInvoiceDtl.moveToInsertRow();
                        rsOBCInvoiceDtl.updateInt("COMPANY_ID",CompanyID);
                        rsOBCInvoiceDtl.updateString("DOC_NO",nDoc_no);
                        rsOBCInvoiceDtl.updateInt("SR_NO",cnt);
                        rsOBCInvoiceDtl.updateString("INVOICE_NO",Invoice_no);
                        rsOBCInvoiceDtl.updateString("INVOICE_DATE",Invoice_date);
                        rsOBCInvoiceDtl.updateDouble("INVOICE_AMOUNT",Double.parseDouble(Amount));
                        rsOBCInvoiceDtl.updateString("REMARKS","");
                        rsOBCInvoiceDtl.updateString("CREATED_BY","admin");
                        rsOBCInvoiceDtl.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl.updateString("MODIFIED_BY","admin");
                        rsOBCInvoiceDtl.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl.updateBoolean("CHANGED",true);
                        rsOBCInvoiceDtl.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl.updateInt("CANCELLED",0);
                        rsOBCInvoiceDtl.insertRow();
                        
                        rsOBCInvoiceDtl_h.moveToInsertRow();
                        rsOBCInvoiceDtl_h.updateInt("REVISION_NO",1);
                        rsOBCInvoiceDtl_h.updateInt("COMPANY_ID",CompanyID);
                        rsOBCInvoiceDtl_h.updateString("DOC_NO",nDoc_no);
                        rsOBCInvoiceDtl_h.updateInt("SR_NO",cnt);
                        rsOBCInvoiceDtl_h.updateString("INVOICE_NO",Invoice_no);
                        rsOBCInvoiceDtl_h.updateString("INVOICE_DATE",Invoice_date);
                        rsOBCInvoiceDtl_h.updateDouble("INVOICE_AMOUNT",Double.parseDouble(Amount));
                        rsOBCInvoiceDtl_h.updateString("REMARKS","");
                        rsOBCInvoiceDtl_h.updateString("CREATED_BY","admin");
                        rsOBCInvoiceDtl_h.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl_h.updateString("MODIFIED_BY","admin");
                        rsOBCInvoiceDtl_h.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl_h.updateBoolean("CHANGED",true);
                        rsOBCInvoiceDtl_h.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBCInvoiceDtl_h.updateInt("CANCELLED",0);
                        rsOBCInvoiceDtl_h.insertRow();
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
    
    public String ConvertDateDB(String Date) {
        if (Date.length()<6) {
            return "0000-00-00";
        }
        else {
            //DDMMYY
            return "20" + Date.substring(4,6) + "-" + Date.substring(2,4) + "-" + Date.substring(0,2);
        }
    }
    
}