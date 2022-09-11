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

public class clsSalesPartyImport {
    
    /** Creates a new instance of clsSalesInvoiceImport */
    public clsSalesPartyImport() {
        
    }
    
    
    public static void main(String[] args) {
        //0 - File name of the sales party master. in csv format, seperated by ~
        
        if(args.length<3) {
            System.out.println("Insufficient arguments. Please specify full filename (.csv)");
            return;
        }
        
        //arty Master TYpes : "SUITING",  "FF",  "FELT"
        
        String FileName=args[0];
        String MainCode=args[1];
        String PartyMasterType=args[2];
        
       
        
        clsSalesPartyImport objImport=new clsSalesPartyImport();
        
        if ((PartyMasterType.equals("SUITING"))  || (PartyMasterType.equals("FF"))) {
            objImport.ImportSalesPartiesSuiting(FileName, MainCode,PartyMasterType);
        }else if (PartyMasterType.equals("FELT"))
        {
            objImport.ImportSalesPartiesFelt(FileName, MainCode,PartyMasterType);            
        }
        
        
        
    }
    
    
    private void ImportSalesPartiesSuiting(final String FileName, final String MainCode, final String PartyMasterType) {
        
        new Thread() {
            
            public void run(){
                boolean Done=false;
                long Counter=0;
                long LineNo=0;
                
                try {
                    
                    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
                    Connection objConn= data.getConn(dbURL);
                    Statement stParty=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsParty=stParty.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER");
                    
                    Connection objFinConn = data.getConn(FinanceGlobal.FinURL);
                    Statement stFinParty = objFinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsFinParty= stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
                    
                    
                    Counter=0;
                    
                    BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
                    
                    
                    while(!Done) {
                        
                        try {
                            
                            String FileRecord=aFile.readLine();
                            String[] ArrRecord=FileRecord.split("~");
                            
                            
                            String PartyCode=ArrRecord[0];
                            String AreaID="";
                            
                            Statement stArea=objConn.createStatement();
                            ResultSet rsArea=stArea.executeQuery("SELECT AREA_ID FROM D_SAL_AREA_MASTER WHERE AGENT_PREFIX='"+PartyCode.substring(0,2)+"'");
                            rsArea.first();
                            
                            if(rsArea.getRow()>0) {
                                AreaID=rsArea.getString("AREA_ID");
                            }
                            
                            //Check for the Existance of the record
                            Statement stTemp=objConn.createStatement();
                            ResultSet rsTemp=stTemp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'");
                            rsTemp.first();
                            
                            if(rsTemp.getRow()>0) //Party Exist
                            {
                                System.out.println("Party Code "+PartyCode+" already exist. Updating Record ");
                                
                                stParty=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                rsParty=stParty.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' LIMIT 1");
                                rsParty.first();
                                
                                if(ArrRecord[1].trim().equals("A")) {
                                    rsParty.updateInt("PARTY_TYPE",1);
                                }
                                else {
                                    rsParty.updateInt("PARTY_TYPE",2);
                                }
                                
                                rsParty.updateString("AREA_ID",AreaID);
                                rsParty.updateString("PARTY_NAME",ArrRecord[2]);
                                rsParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[4]));
                                rsParty.updateString("AGENT_PREFIX",ArrRecord[5]);
                                rsParty.updateString("ADDRESS1",ArrRecord[6]);
                                rsParty.updateString("ADDRESS2",ArrRecord[7]);
                                rsParty.updateString("PINCODE",ArrRecord[8]);
                                rsParty.updateString("CITY_ID",ArrRecord[9]);
                                rsParty.updateString("DISTRICT","");
                                rsParty.updateString("PHONE_NO","");
                                rsParty.updateString("MOBILE_NO","");
                                rsParty.updateString("EMAIL","");
                                rsParty.updateString("URL","");
                                rsParty.updateString("CONTACT_PERSON","");
                                rsParty.updateInt("BANK_ID",UtilFunctions.CInt(ArrRecord[23]));
                                rsParty.updateString("CHARGE_CODE",ArrRecord[12]);
                                rsParty.updateString("DOCUMENT_THROUGH",ArrRecord[10]);
                                rsParty.updateInt("TRANSPORTER_ID",UtilFunctions.CInt(ArrRecord[13]));
                                rsParty.updateDouble("LC_LIMIT",UtilFunctions.CDbl(ArrRecord[27]));
                                rsParty.updateDouble("AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
                                rsParty.updateDouble("CRITICAL_AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[28]));
                                
                                if(ArrRecord.length>=33) {
                                    rsParty.updateString("ECC_NO",ArrRecord[32]);
                                }
                                rsParty.updateString("ECC_DATE","");
                                rsParty.updateString("MODIFIED_BY","admin");
                                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("CHANGED",1);
                                rsParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("CANCELLED",0);
                                rsParty.updateRow();
                                
                            }
                            else {
                                
                                System.out.println("New party Code "+PartyCode+". Inserting Record ");
                                
                                rsParty.moveToInsertRow();
                                rsParty.updateInt("COMPANY_ID",2);
                                rsParty.updateString("PARTY_CODE",ArrRecord[0]);
                                rsParty.updateString("PARENT_PARTY_CODE","");
                                
                                if(ArrRecord[1].trim().equals("A")) {
                                    rsParty.updateInt("PARTY_TYPE",1);
                                }
                                else {
                                    rsParty.updateInt("PARTY_TYPE",2);
                                }
                                rsParty.updateString("AREA_ID",AreaID);
                                rsParty.updateString("PARTY_NAME",ArrRecord[2]);
                                rsParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[4]));
                                rsParty.updateString("AGENT_PREFIX",ArrRecord[5]);
                                rsParty.updateString("ADDRESS1",ArrRecord[6]);
                                rsParty.updateString("ADDRESS2",ArrRecord[7]);
                                rsParty.updateString("PINCODE",ArrRecord[8]);
                                rsParty.updateString("CITY_ID",ArrRecord[9]);
                                rsParty.updateString("DISTRICT","");
                                rsParty.updateString("PHONE_NO","");
                                rsParty.updateString("MOBILE_NO","");
                                rsParty.updateString("EMAIL","");
                                rsParty.updateString("URL","");
                                rsParty.updateString("CONTACT_PERSON","");
                                rsParty.updateInt("BANK_ID",UtilFunctions.CInt(ArrRecord[23]));
                                rsParty.updateString("CHARGE_CODE",ArrRecord[12]);
                                rsParty.updateString("DOCUMENT_THROUGH",ArrRecord[10]);
                                rsParty.updateInt("TRANSPORTER_ID",UtilFunctions.CInt(ArrRecord[13]));
                                rsParty.updateDouble("LC_LIMIT",UtilFunctions.CDbl(ArrRecord[27]));
                                rsParty.updateDouble("AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
                                rsParty.updateDouble("CRITICAL_AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[28]));
                                
                                if(ArrRecord.length>=33) {
                                    rsParty.updateString("ECC_NO",ArrRecord[32]);
                                }
                                rsParty.updateString("ECC_DATE","");
                                rsParty.updateString("CREATED_BY","admin");
                                rsParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateString("MODIFIED_BY","admin");
                                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("HIERARCHY_ID",0);
                                rsParty.updateInt("APPROVED",1);
                                rsParty.updateString("APPROVED_DATE","");
                                rsParty.updateInt("REJECTED",0);
                                rsParty.updateString("REJECTED_DATE","");
                                rsParty.updateInt("CHANGED",1);
                                rsParty.updateString("CHANGED_DATE","");
                                rsParty.updateInt("CANCELLED",0);
                                rsParty.insertRow();
                            }
                            
                            
                            if (data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE ='"+ArrRecord[0]+"'",FinanceGlobal.FinURL)) {
                                
                            }
                            else {
                                
                                System.out.println("Inserting Party in Finance Party Master"+MainCode + " "+ArrRecord[0] );
                                long  PartyID = data.getMaxID("D_FIN_PARTY_MASTER", "PARTY_ID", FinanceGlobal.FinURL);
                                rsFinParty.moveToInsertRow();
                                rsFinParty.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsFinParty.updateString("PARTY_CODE", ArrRecord[0]);
                                rsFinParty.updateString("MAIN_ACCOUNT_CODE", MainCode);
                                rsFinParty.updateLong("PARTY_ID",PartyID);
                                rsFinParty.updateString("PARTY_NAME", ArrRecord[2]);
                                rsFinParty.updateString("SH_NAME","");
                                rsFinParty.updateString("REMARKS", "");
                                rsFinParty.updateString("SH_CODE", "");
                                rsFinParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[4]));
                                rsFinParty.updateDouble("CREDIT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
                                rsFinParty.updateDouble("DEBIT_LIMIT",0);
                                rsFinParty.updateString("TIN_NO", "");
                                rsFinParty.updateString("TIN_DATE", "");
                                rsFinParty.updateString("CST_NO", "");
                                rsFinParty.updateString("CST_DATE", "");
                                if(ArrRecord.length>=33) {
                                    rsFinParty.updateString("ECC_NO",ArrRecord[32]);
                                }
                                rsFinParty.updateString("ECC_DATE", "");
                                
                                rsFinParty.updateString("SERVICE_TAX_NO", "");
                                rsFinParty.updateString("SERVICE_TAX_DATE", "");
                                rsFinParty.updateString("SSI_CATEGORY", "");
                                rsFinParty.updateString("SSI_NO", "");
                                rsFinParty.updateString("SSI_DATE", "");
                                rsFinParty.updateString("ESI_NO", "");
                                rsFinParty.updateString("ESI_DATE", "");
                                
                                rsFinParty.updateString("ADDRESS", ArrRecord[6]+","+ArrRecord[7]);
                                rsFinParty.updateString("CITY", ArrRecord[9]);
                                rsFinParty.updateString("PINCODE",ArrRecord[8]);
                                rsFinParty.updateString("STATE", "");
                                rsFinParty.updateString("COUNTRY", "");
                                rsFinParty.updateString("PHONE", "");
                                rsFinParty.updateString("FAX", "");
                                rsFinParty.updateString("MOBILE", "");
                                rsFinParty.updateString("EMAIL", "");
                                rsFinParty.updateString("URL", "");
                                rsFinParty.updateInt("APPROVED",1);
                                rsFinParty.updateString("APPROVED_DATE","");
                                rsFinParty.updateInt("REJECTED",0);
                                rsFinParty.updateString("REJECTED_DATE","");
                                rsFinParty.updateString("REJECTED_REMARKS","");
                                rsFinParty.updateString("HIERARCHY_ID","");
                                
                                rsFinParty.updateInt("CANCELLED",0);
                                rsFinParty.updateInt("BLOCKED",0);
                                rsFinParty.updateString("PAN_NO","");
                                rsFinParty.updateString("PAN_DATE","");
                                rsFinParty.updateInt("CHANGED",1);
                                rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsFinParty.updateString("CREATED_BY","Admin");
                                rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                
                                rsFinParty.updateString("MODIFIED_BY","Admin");
                                rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsFinParty.updateDouble("CLOSING_BALANCE",0);
                                rsFinParty.updateString("CLOSING_EFFECT","D");
                                rsFinParty.insertRow();
                            }
                        }
                        catch(Exception c){
                            c.printStackTrace();
                            Done=true;
                        }
                    }
                    
                    System.out.println("Completed .... ");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

      private void ImportSalesPartiesFelt(final String FileName, final String MainCode, final String PartyMasterType) 
      {
        
        new Thread() {
            
            public void run(){
                boolean Done=false;
                long Counter=0;
                long LineNo=0;
                
                try {
                    
                    String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
                    Connection objConn= data.getConn(dbURL);
                    Statement stParty=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsParty=stParty.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER");
                    
                    Connection objFinConn = data.getConn(FinanceGlobal.FinURL);
                    Statement stFinParty = objFinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsFinParty= stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
                    
                    
                    Counter=0;
                    
                    BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
                    
                    
                    while(!Done) {
                        
                        try {
                            
                            String FileRecord=aFile.readLine();
                            String[] ArrRecord=FileRecord.split("~");
                            
                            String PartyCode=ArrRecord[0];
                            String AreaID="";

                            //Check for the Existance of the record
                            Statement stTemp=objConn.createStatement();
                            ResultSet rsTemp=stTemp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'");
                            rsTemp.first();
                            
                            if(rsTemp.getRow()>0) //Party Exist
                            {
                                System.out.println("Party Code "+PartyCode+" already exist. Updating Record ");
                                
                                stParty=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                rsParty=stParty.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' LIMIT 1");
                                rsParty.first();

                                rsParty.updateInt("PARTY_TYPE",0);
                                rsParty.updateString("AREA_ID",AreaID);
                                rsParty.updateString("PARTY_NAME",ArrRecord[1]);
                                rsParty.updateString("ADDRESS1",ArrRecord[2]);
                                rsParty.updateString("ADDRESS2",ArrRecord[3]);
                                rsParty.updateString("PINCODE","");
                                rsParty.updateString("CITY_ID",ArrRecord[4]);
                                rsParty.updateString("DISTRICT","");
                                rsParty.updateString("PHONE_NO","");
                                rsParty.updateString("MOBILE_NO","");
                                rsParty.updateString("EMAIL","");
                                rsParty.updateString("URL","");
                                rsParty.updateString("CONTACT_PERSON","");
                                rsParty.updateString("DOCUMENT_THROUGH",ArrRecord[5]);                                
                                rsParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[12]));
                                rsParty.updateInt("TRANSPORTER_ID",UtilFunctions.CInt(ArrRecord[14]));                                
                                
                                
                                rsParty.updateInt("BANK_ID",UtilFunctions.CInt(ArrRecord[18]));

//                                rsParty.updateDouble("LC_LIMIT",UtilFunctions.CDbl(ArrRecord[27]));
//                                rsParty.updateDouble("AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
//                                rsParty.updateDouble("CRITICAL_AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[28]));
                                
                              if(ArrRecord.length>=21) {
                                    rsParty.updateString("ECC_NO",ArrRecord[20]);
                             }

                                rsParty.updateString("ECC_DATE","");
                                rsParty.updateString("MODIFIED_BY","admin");
                                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("CHANGED",1);
                                rsParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("CANCELLED",0);
                                rsParty.updateRow();
                                
                            }
                            else {
                                
                                System.out.println("New party Code "+PartyCode+". Inserting Record ");
                                
                                rsParty.moveToInsertRow();
                                rsParty.updateInt("COMPANY_ID",2);
                                rsParty.updateString("PARTY_CODE",ArrRecord[0]);
                                rsParty.updateString("PARENT_PARTY_CODE","");
                                
                                rsParty.updateInt("PARTY_TYPE",0);
                                rsParty.updateString("AREA_ID",AreaID);
                                rsParty.updateString("PARTY_NAME",ArrRecord[1]);
                                rsParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[12]));
                                //rsParty.updateString("AGENT_PREFIX",ArrRecord[5]);
                                rsParty.updateString("ADDRESS1",ArrRecord[2]);
                                rsParty.updateString("ADDRESS2",ArrRecord[3]);
                                rsParty.updateString("PINCODE","");
                                rsParty.updateString("CITY_ID",ArrRecord[4]);
                                rsParty.updateString("DISTRICT","");
                                rsParty.updateString("PHONE_NO","");
                                rsParty.updateString("MOBILE_NO","");
                                rsParty.updateString("EMAIL","");
                                rsParty.updateString("URL","");
                                rsParty.updateString("CONTACT_PERSON","");
                                rsParty.updateInt("BANK_ID",UtilFunctions.CInt(ArrRecord[18]));
                                //rsParty.updateString("CHARGE_CODE",ArrRecord[12]);
                                rsParty.updateString("DOCUMENT_THROUGH",ArrRecord[5]);
                                rsParty.updateInt("TRANSPORTER_ID",UtilFunctions.CInt(ArrRecord[14]));
                                
//                                rsParty.updateDouble("LC_LIMIT",UtilFunctions.CDbl(ArrRecord[27]));
//                                rsParty.updateDouble("AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
//                                rsParty.updateDouble("CRITICAL_AMOUNT_LIMIT",UtilFunctions.CDbl(ArrRecord[28]));
                                
                                if(ArrRecord.length>=21) {
                                    
                                    rsParty.updateString("ECC_NO",ArrRecord[20]);
                               }
                                rsParty.updateString("ECC_DATE","");
                                rsParty.updateString("CREATED_BY","admin");
                                rsParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateString("MODIFIED_BY","admin");
                                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsParty.updateInt("HIERARCHY_ID",0);
                                rsParty.updateInt("APPROVED",1);
                                rsParty.updateString("APPROVED_DATE","");
                                rsParty.updateInt("REJECTED",0);
                                rsParty.updateString("REJECTED_DATE","");
                                rsParty.updateInt("CHANGED",1);
                                rsParty.updateString("CHANGED_DATE","");
                                rsParty.updateInt("CANCELLED",0);
                                rsParty.insertRow();
                            }
                            
                            
                            if (data.IsRecordExist("SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE ='"+ArrRecord[0]+"'",FinanceGlobal.FinURL)) {
                                
                            }
                            else {
                                
                                System.out.println("Inserting Party in Finance Party Master"+MainCode + " "+ArrRecord[0] );
                                long  PartyID = data.getMaxID("D_FIN_PARTY_MASTER", "PARTY_ID", FinanceGlobal.FinURL);
                                rsFinParty.moveToInsertRow();
                                rsFinParty.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                rsFinParty.updateString("PARTY_CODE", ArrRecord[0]);
                                rsFinParty.updateString("MAIN_ACCOUNT_CODE", MainCode);
                                rsFinParty.updateLong("PARTY_ID",PartyID);
                                rsFinParty.updateString("PARTY_NAME", ArrRecord[1]);
                                rsFinParty.updateString("SH_NAME","");
                                rsFinParty.updateString("REMARKS", "");
                                rsFinParty.updateString("SH_CODE", "");
                                rsFinParty.updateDouble("CREDIT_DAYS",UtilFunctions.CDbl(ArrRecord[12]));
//                                rsFinParty.updateDouble("CREDIT_LIMIT",UtilFunctions.CDbl(ArrRecord[25])/100);
                                rsFinParty.updateDouble("DEBIT_LIMIT",0);
                                rsFinParty.updateString("TIN_NO", "");
                                rsFinParty.updateString("TIN_DATE", "");
                                rsFinParty.updateString("CST_NO", "");
                                rsFinParty.updateString("CST_DATE", "");
//                                if(ArrRecord.length>=33) {
                                    rsFinParty.updateString("ECC_NO",ArrRecord[20]);
//                                }
                                rsFinParty.updateString("ECC_DATE", "");
                                
                                rsFinParty.updateString("SERVICE_TAX_NO", "");
                                rsFinParty.updateString("SERVICE_TAX_DATE", "");
                                rsFinParty.updateString("SSI_CATEGORY", "");
                                rsFinParty.updateString("SSI_NO", "");
                                rsFinParty.updateString("SSI_DATE", "");
                                rsFinParty.updateString("ESI_NO", "");
                                rsFinParty.updateString("ESI_DATE", "");
                                
                                rsFinParty.updateString("ADDRESS", ArrRecord[2]+","+ArrRecord[3]);
                                rsFinParty.updateString("CITY", ArrRecord[4]);
                                rsFinParty.updateString("PINCODE","");
                                rsFinParty.updateString("STATE", "");
                                rsFinParty.updateString("COUNTRY", "");
                                rsFinParty.updateString("PHONE", "");
                                rsFinParty.updateString("FAX", "");
                                rsFinParty.updateString("MOBILE", "");
                                rsFinParty.updateString("EMAIL", "");
                                rsFinParty.updateString("URL", "");
                                rsFinParty.updateInt("APPROVED",1);
                                rsFinParty.updateString("APPROVED_DATE","");
                                rsFinParty.updateInt("REJECTED",0);
                                rsFinParty.updateString("REJECTED_DATE","");
                                rsFinParty.updateString("REJECTED_REMARKS","");
                                rsFinParty.updateString("HIERARCHY_ID","");
                                
                                rsFinParty.updateInt("CANCELLED",0);
                                rsFinParty.updateInt("BLOCKED",0);
                                rsFinParty.updateString("PAN_NO","");
                                rsFinParty.updateString("PAN_DATE","");
                                rsFinParty.updateInt("CHANGED",1);
                                rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsFinParty.updateString("CREATED_BY","Admin");
                                rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                
                                rsFinParty.updateString("MODIFIED_BY","Admin");
                                rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                rsFinParty.updateDouble("CLOSING_BALANCE",0);
                                rsFinParty.updateString("CLOSING_EFFECT","D");
                                rsFinParty.insertRow();
                            }
                        }
                        catch(Exception c){
                            c.printStackTrace();
                            Done=true;
                        }
                    }
                    
                    System.out.println("Completed .... ");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
  
    
    
    
}



