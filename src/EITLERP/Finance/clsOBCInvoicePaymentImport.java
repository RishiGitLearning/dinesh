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
public class clsOBCInvoicePaymentImport {
    
    
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    public clsOBCInvoicePaymentImport() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            
            /*if(args.length<=0) {
                System.out.println("Please specify the directory to put exported file");
                return;
            }*/
            
            clsOBCInvoicePaymentImport objImport=new clsOBCInvoicePaymentImport();
            
            //objImport.ImportOBCInvoicePayment(args[0]);
            objImport.ImportOBCInvoicePayment("/data/OBCMIGRATION/brel08412.txt","/data/OBCMIGRATION/bnkv08412.txt");
            //objImport.ImportOBCInvoicePayment("/data/OBCMIGRATION/bnkv0812.txt");
            //System.out.println("THE END");
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ImportOBCInvoicePayment(String invoiceFile,String intFile) {
        
        boolean Done=false;
        long Counter=0;
        
        try {
            
            String dbFinURL="jdbc:mysql://200.0.0.227:3306/FINANCE";
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            Connection objConn=data.getConn(dbFinURL);
            
            Statement stOBC=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBC=stOBC.executeQuery("SELECT * FROM D_FIN_OBC WHERE DOC_NO='1' ");
            
            Statement stOBC_h=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOBC_h=stOBC_h.executeQuery("SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='1' ");
            
            int Pointer=0;
            String Draft_no,Draft_date,Amount,Bank_ref_No,Bank_ref_date,Main_acc_code,Party_code,Payment_date,Ref_sr,Agent_alpha;
            int CompanyID = 2;
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
            
            HashMap FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                
                
                try {
                    
                    String FileRecord=aFile.readLine();
                    
                    String qry = "SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID = " + clsOBC.ModuleID;
                    ResultSet rsFF = data.getResult(qry,dbURL);
                    rsFF.first();
                    
                    if(rsFF.getRow()>0) {
                        SelPrefix=rsFF.getString("PREFIX_CHARS");
                        SelSuffix=rsFF.getString("SUFFIX_CHARS");
                        FFNo=rsFF.getInt("FIRSTFREE_NO");
                    }
                    String Doc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBC.ModuleID,FFNo,false);
                    
                    int Hierarchy_id = data.getIntValueFromDB("SELECT HIERARCHY_ID FROM D_COM_HIERARCHY WHERE MODULE_ID=" + clsOBC.ModuleID + " Limit 1" ,dbURL);
                    
                    
                    Pointer=0;
                    Pointer+=2;//BP_DIV_CODE
                    Pointer+=2;//BP_BOOK_CODE
                    Pointer+=6;//BP_BANK_CODE
                    Bank_ref_date = ConvertDateDB(FileRecord.substring(Pointer,Pointer+6));
                    Pointer+=6;//BP_DATE1(DDMMYY)
                    Bank_ref_No = FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//BP_REF_AL
                    Bank_ref_No = Bank_ref_No + FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BP_REF_NO
                    Draft_no = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BP_DRAFT_NO
                    Agent_alpha = FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//BP_AG_AL
                    Agent_alpha = Agent_alpha.trim() + "/" + FileRecord.substring(Pointer,Pointer+4);
                    Pointer+=4;//BP_AGSR
                    Pointer+=2;//BP_YR
                    Main_acc_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BP_MAIN_CODE
                    Party_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//BP_SUB_CODE
                    Pointer+=20;//BP_PARTY_NAME
                    Pointer+=10;//BP_STATION
                    Amount = FileRecord.substring(Pointer,Pointer+10);
                    Pointer+=10;//TR_AMT
                    Amount = Amount + "." + FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//TR_AMT
                    Pointer+=4;//BP_INV_YEAR1
                    Pointer+=2;//BP_IND
                    Pointer+=2;//BP_DDHUN_AL
                    Pointer+=6;//BP_DDHUN_NO
                    Pointer+=17;//BP_FILLER
                    Payment_date = "0000-00-00";
                    Draft_date = "0000-00-00";
                    
                    String sBank_ref_No = "";
                    sBank_ref_No = Bank_ref_No.substring(2);
                    int lenRefNo = sBank_ref_No.length();
                    if (lenRefNo < 6) {
                        Bank_ref_No = Bank_ref_No.substring(0,2) + EITLERPGLOBAL.padLeft(6,sBank_ref_No,"0");
                    }
                    
                    if(!data.IsRecordExist("SELECT DOC_NO FROM D_FIN_OBC WHERE DOC_NO='"+Doc_no+"'",dbFinURL)) {
                        
                        Doc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBC.ModuleID,FFNo,true);
                        System.out.println("Importing Doc No "+Doc_no);
                        
                        rsOBC.moveToInsertRow();
                        rsOBC.updateLong("COMPANY_ID",CompanyID);
                        rsOBC.updateString("DOC_NO",Doc_no);
                        rsOBC.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                        rsOBC.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                        rsOBC.updateString("DRAFT_NO",Draft_no);//Draft_no);
                        rsOBC.updateString("DRAFT_DATE", Draft_date);
                        rsOBC.updateString("MAIN_CODE",Main_acc_code);
                        rsOBC.updateString("PARTY_CODE",Party_code);
                        rsOBC.updateString("AGENT_ALPHA",Agent_alpha);
                        rsOBC.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsOBC.updateString("REMARKS","");
                        rsOBC.updateLong("HIERARCHY_ID",Hierarchy_id);
                        rsOBC.updateString("CREATED_BY","admin" );
                        rsOBC.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC.updateString("MODIFIED_BY","admin");
                        rsOBC.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC.updateBoolean("CHANGED",true);
                        rsOBC.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC.updateBoolean("APPROVED",true);
                        rsOBC.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC.updateBoolean("REJECTED",false);
                        rsOBC.updateString("REJECTED_DATE","0000-00-00");
                        rsOBC.updateString("REJECTED_REMARKS","");
                        rsOBC.updateBoolean("CANCELLED",false);
                        rsOBC.updateDouble("INTEREST_AMOUNT",0);
                        rsOBC.updateString("PAYMENT_RECEIVED_DATE",Payment_date);
                        rsOBC.insertRow();
                        
                        rsOBC_h.moveToInsertRow();
                        rsOBC_h.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                        rsOBC_h.updateString("UPDATED_BY","admin" );
                        rsOBC_h.updateString("APPROVAL_STATUS","F");
                        rsOBC_h.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateString("APPROVER_REMARKS","");
                        rsOBC_h.updateLong("COMPANY_ID",CompanyID);
                        rsOBC_h.updateString("DOC_NO",Doc_no);
                        rsOBC_h.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                        rsOBC_h.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                        rsOBC_h.updateString("DRAFT_NO",Draft_no);//Draft_no);
                        rsOBC_h.updateString("DRAFT_DATE", Draft_date);
                        rsOBC_h.updateString("MAIN_CODE",Main_acc_code);
                        rsOBC_h.updateString("PARTY_CODE",Party_code);
                        rsOBC_h.updateString("AGENT_ALPHA",Agent_alpha);
                        rsOBC_h.updateDouble("AMOUNT",Double.parseDouble(Amount));
                        rsOBC_h.updateString("REMARKS","");
                        rsOBC_h.updateLong("HIERARCHY_ID",Hierarchy_id);
                        rsOBC_h.updateString("CREATED_BY","admin");
                        rsOBC_h.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateString("MODIFIED_BY","admin");
                        rsOBC_h.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateBoolean("CHANGED",true);
                        rsOBC_h.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateBoolean("APPROVED",true);
                        rsOBC_h.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsOBC_h.updateBoolean("REJECTED",false);
                        rsOBC_h.updateString("REJECTED_DATE","0000-00-00");
                        rsOBC_h.updateString("REJECTED_REMARKS","");
                        rsOBC_h.updateBoolean("CANCELLED",false);
                        rsOBC_h.updateDouble("INTEREST_AMOUNT",0);
                        rsOBC_h.updateString("PAYMENT_RECEIVED_DATE",Payment_date);
                        rsOBC_h.insertRow();
                        
                        System.out.println("Invoice Imported ");
                        
                    }
                    
                }
                catch(Exception c){
                    c.printStackTrace();
                    Done=true;
                }
            }
            
            stOBC=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOBC=stOBC.executeQuery("SELECT * FROM D_FIN_OBC WHERE DOC_NO='1' ");
            
            stOBC_h=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOBC_h=stOBC_h.executeQuery("SELECT * FROM D_FIN_OBC_H WHERE DOC_NO='1' ");
            
            Pointer=0;
            Draft_no="";Draft_date="";Amount="";Bank_ref_No="";Bank_ref_date="";Main_acc_code="";
            Party_code="";Payment_date="";Ref_sr="";Agent_alpha="";
            CompanyID = 2;
            
            aFile=new BufferedReader(new FileReader(new File(intFile)));
            
            FileLines=new HashMap();
            
            Done=false;
            
            while(!Done) {
                
                
                try {
                    
                    String FileRecord=aFile.readLine();
                    
                    String qry = "SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID = " + clsOBC.ModuleID;
                    ResultSet rsFF = data.getResult(qry,dbURL);
                    rsFF.first();
                    
                    if(rsFF.getRow()>0) {
                        SelPrefix=rsFF.getString("PREFIX_CHARS");
                        SelSuffix=rsFF.getString("SUFFIX_CHARS");
                        FFNo=rsFF.getInt("FIRSTFREE_NO");
                    }
                    String Doc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBC.ModuleID,FFNo,false);
                    
                    int Hierarchy_id = data.getIntValueFromDB("SELECT HIERARCHY_ID FROM D_COM_HIERARCHY WHERE MODULE_ID=" + clsOBC.ModuleID + " Limit 1" ,dbURL);
                    
                    Pointer=0;
                    Pointer+=2;//TR_REC_TYPE
                    Pointer+=6;//TR_IN_AGSR
                    Pointer+=2;//TR_DIV_CODE
                    Pointer+=2;//TR_BOOK_CODE
                    Bank_ref_date = ConvertDateDBR(FileRecord.substring(Pointer,Pointer+6));
                    Pointer+=6;//TR_DT1(YYMMDD)
                    //Bank_ref_No = FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//TR_REF_AL
                    //Bank_ref_No = Bank_ref_No + FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//TR_REF_NO
                    Main_acc_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//TR_MAIN_CODE
                    Party_code = FileRecord.substring(Pointer,Pointer+6);
                    Pointer+=6;//TR_SUB_CODE
                    Bank_ref_No = FileRecord.substring(Pointer,Pointer+8);
                    
                    Pointer+=8;//TR_CHEQUE_NO
                    Ref_sr = FileRecord.substring(Pointer,Pointer+4);
                    Pointer+=4;//TR_REF_SR
                    Draft_date = "0000-00-00";
                    Payment_date = Bank_ref_date;
                    Pointer+=6;//TR_CHEQ_BILL_DATE
                    Agent_alpha = FileRecord.substring(Pointer,Pointer+25);
                    if (Agent_alpha.lastIndexOf("/") > 0) {
                        Agent_alpha = Agent_alpha.substring(0,Agent_alpha.lastIndexOf("/"));
                        if (Agent_alpha.lastIndexOf("/") > 0) {
                            Agent_alpha = Agent_alpha.substring(0,Agent_alpha.lastIndexOf("/"));
                        }
                    }
                    else {
                        Agent_alpha = "";
                        Bank_ref_No = FileRecord.substring(Pointer,Pointer+25).trim();
                    }
                    
                    Bank_ref_No = Bank_ref_No.replace(" ", "");
                    String sBank_ref_No = "";
                    sBank_ref_No = Bank_ref_No.substring(2);
                    int lenRefNo = sBank_ref_No.length();
                    if (lenRefNo < 6) {
                        Bank_ref_No = Bank_ref_No.substring(0,2) + EITLERPGLOBAL.padLeft(6,sBank_ref_No,"0");
                    }
                    
                    Pointer+=25;//TR_DESCRIPTION
                    Pointer+=2;//TR_SALETAX_CODE
                    Amount = FileRecord.substring(Pointer,Pointer+9);
                    Pointer+=9;//TR_AMT
                    Amount = Amount + "." + FileRecord.substring(Pointer,Pointer+2);
                    Pointer+=2;//TR_AMT
                    Pointer+=1;//TR_AMT_SIGN
                    Pointer+=6;//TR_ALLOC_CODE
                    Pointer+=2;//TR_PO_AL
                    Pointer+=6;//TR_PO_NO
                    Pointer+=2;//TR_PO_FLAG
                    Pointer+=2;//TR_BILL_AL
                    Pointer+=6;//TR_BILL_NO
                    Pointer+=2;//TR_PO_FL
                    Pointer+=2;//TR_INV_YY1
                    Pointer+=2;//TR_INV_YY2
                    Pointer+=1;//TR_DIR_IND
                    Pointer+=2;//TR_TRAN_CODE
                    
                    
                    
                    if (Ref_sr.trim().equals("9999")) {
                        
                        if(!data.IsRecordExist("SELECT DOC_NO FROM D_FIN_OBC WHERE DOC_NO='"+Doc_no+"'",dbFinURL)) {
                            
                            Doc_no = clsFirstFree.getNextFreeNo(CompanyID,clsOBC.ModuleID,FFNo,true);
                            System.out.println("Importing Doc No "+Doc_no);
                            
                            rsOBC.moveToInsertRow();
                            rsOBC.updateLong("COMPANY_ID",CompanyID);
                            rsOBC.updateString("DOC_NO",Doc_no);
                            rsOBC.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                            rsOBC.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                            rsOBC.updateString("DRAFT_NO","");//Draft_no);
                            rsOBC.updateString("DRAFT_DATE", Draft_date);
                            rsOBC.updateString("MAIN_CODE",Main_acc_code);
                            rsOBC.updateString("PARTY_CODE",Party_code);
                            rsOBC.updateString("AGENT_ALPHA",Agent_alpha);
                            rsOBC.updateDouble("AMOUNT",0);
                            rsOBC.updateString("REMARKS","");
                            rsOBC.updateLong("HIERARCHY_ID",Hierarchy_id);
                            rsOBC.updateString("CREATED_BY","admin" );
                            rsOBC.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC.updateString("MODIFIED_BY","admin");
                            rsOBC.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC.updateBoolean("CHANGED",true);
                            rsOBC.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC.updateBoolean("APPROVED",true);
                            rsOBC.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC.updateBoolean("REJECTED",false);
                            rsOBC.updateString("REJECTED_DATE","0000-00-00");
                            rsOBC.updateString("REJECTED_REMARKS","");
                            rsOBC.updateBoolean("CANCELLED",false);
                            rsOBC.updateDouble("INTEREST_AMOUNT",Double.parseDouble(Amount));
                            rsOBC.updateString("PAYMENT_RECEIVED_DATE",Payment_date);
                            rsOBC.insertRow();
                            
                            
                            rsOBC_h.moveToInsertRow();
                            rsOBC_h.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                            rsOBC_h.updateString("UPDATED_BY","admin" );
                            rsOBC_h.updateString("APPROVAL_STATUS","F");
                            rsOBC_h.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateString("APPROVER_REMARKS","");
                            rsOBC_h.updateLong("COMPANY_ID",CompanyID);
                            rsOBC_h.updateString("DOC_NO",Doc_no);
                            rsOBC_h.updateString("DOC_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateString("BANK_REFERENCE_NO",Bank_ref_No);
                            rsOBC_h.updateString("BANK_REFERENCE_DATE", Bank_ref_date);
                            rsOBC_h.updateString("DRAFT_NO","");//Draft_no);
                            rsOBC_h.updateString("DRAFT_DATE", Draft_date);
                            rsOBC_h.updateString("MAIN_CODE",Main_acc_code);
                            rsOBC_h.updateString("PARTY_CODE",Party_code);
                            rsOBC_h.updateString("AGENT_ALPHA",Agent_alpha);
                            rsOBC_h.updateDouble("AMOUNT",0);
                            rsOBC_h.updateString("REMARKS","");
                            rsOBC_h.updateLong("HIERARCHY_ID",Hierarchy_id);
                            rsOBC_h.updateString("CREATED_BY","admin");
                            rsOBC_h.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateString("MODIFIED_BY","admin");
                            rsOBC_h.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateBoolean("CHANGED",true);
                            rsOBC_h.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateBoolean("APPROVED",true);
                            rsOBC_h.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsOBC_h.updateBoolean("REJECTED",false);
                            rsOBC_h.updateString("REJECTED_DATE","0000-00-00");
                            rsOBC_h.updateString("REJECTED_REMARKS","");
                            rsOBC_h.updateBoolean("CANCELLED",false);
                            rsOBC_h.updateDouble("INTEREST_AMOUNT",Double.parseDouble(Amount));
                            rsOBC_h.updateString("PAYMENT_RECEIVED_DATE",Payment_date);
                            rsOBC_h.insertRow();
                            
                            
                            System.out.println("Invoice Imported ");
                        }
                    }
                    data.Execute("UPDATE D_FIN_OBC SET PAYMENT_RECEIVED_DATE='"+ Payment_date +"' WHERE COMPANY_ID="+CompanyID+" AND BANK_REFERENCE_NO='"+Bank_ref_No+"'",dbFinURL);
                    
                    data.Execute("UPDATE D_FIN_OBC_H SET PAYMENT_RECEIVED_DATE='"+ Payment_date +"' WHERE COMPANY_ID="+CompanyID+" AND BANK_REFERENCE_NO='"+Bank_ref_No+"'",dbFinURL);
                    
                }
                catch(Exception c){
                    System.out.println(" bank_ref_no="+Bank_ref_No+  "    ="+c.getMessage());
                    c.printStackTrace();
                    Done=true;
                }
            }
            
            System.out.println("Finished");
            
        }
        catch(Exception e) {
            
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
    
    public String ConvertDateDBR(String Date) {
        if (Date.length()<6) {
            return "0000-00-00";
        }
        else {
            //YYMMDD
            return "20" + Date.substring(0,2) + "-" + Date.substring(2,4) + "-" + Date.substring(4,6);
        }
    }
    
}