/*
 * clsPolicyLCMasterImport.java
 *
 * Created on April 27, 2009, 12:36 PM
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

public class clsPolicyLCMasterImport {
    
    /** Creates a new instance of clsPolicyLCMasterImport */
    public clsPolicyLCMasterImport() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //0 - FileName
        
        
        
        //        if(args.length<1) {
        //            System.out.println("Insufficient arguments. Please specify \n1. Line sequential file name ");
        //            return;
        //        }
        //
        //        String FileName=args[0];
        
        
        
        String FileName="/data/SILM26.TXT";
        
        clsPolicyLCMasterImport objImport=new clsPolicyLCMasterImport();
        
        objImport.ImportLCMaster(FileName);
        
    }
    
    public void ImportLCMaster(String LCFile) {
        
        boolean Done=false;
        long Counter=0;
        
        try {
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
                        
            Connection objConn=data.getConn(dbURL);
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsLC=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_LC_MASTER LIMIT 1");
            int Pointer=0;
            
            BufferedReader aFile=new BufferedReader(new FileReader(new File(LCFile)));
            
            HashMap FileLines=new HashMap();
            int SrNo = 0;
            
            Done=false;
            
            while(!Done) {
                
                
                try {
                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                    
                    String PartyCode=FileRecord.substring(0,6);
                    String str = "SELECT * FROM D_SAL_POLICY_LC_MASTER "+
                    " WHERE PARTY_CODE='"+ PartyCode +"' ";
                    if(!data.IsRecordExist(str,dbURL)) {
                        
                        System.out.println("Importing LC Party "+PartyCode);
                        
                        Pointer=0;
                        
                        rsLC.moveToInsertRow();
                        rsLC.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsLC.updateString("PARTY_CODE",PartyCode);  Pointer+=6; //PARTY_CD
                        rsLC.updateString("LC_NO",FileRecord.substring(Pointer,Pointer+4));  Pointer+=4; //LC_NO
                        String ExpDate=FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //exp_date
                        rsLC.updateString("EXP_DATE", "20"+ExpDate.substring(4)+"-"+ExpDate.substring(2,4)+"-"+ExpDate.substring(0,2));                        
                        rsLC.updateString("IND",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1; //ind
                        rsLC.updateDouble("DISC",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/100); Pointer+=4; //DISC
                        rsLC.updateString("PARTY_NAME",FileRecord.substring(Pointer,Pointer+30)); Pointer+=30; //PARTY_NAME
                        String Address=FileRecord.substring(Pointer,Pointer+25); Pointer+=25; //add1
                        Address = Address.trim() + ", " + FileRecord.substring(Pointer,Pointer+25); Pointer+=25; //add2
                        rsLC.updateString("ADDRESS",Address);
                        rsLC.updateString("BANK_ID","");
                        rsLC.updateString("BANK_NAME",FileRecord.substring(Pointer,Pointer+30)); Pointer+=30; //BANK_NAME
                        String BankAddress=FileRecord.substring(Pointer,Pointer+25); Pointer+=25; //add1
                        BankAddress = BankAddress.trim() + ", " + FileRecord.substring(Pointer,Pointer+25); Pointer+=25; //add2
                        rsLC.updateString("BANK_ADDRESS",BankAddress); //BANK_ADDRESS
                        rsLC.updateString("BANK_CITY",""); 
                        rsLC.updateString("INST1",FileRecord.substring(Pointer,Pointer+40)); Pointer+=40;//INST1
                        rsLC.updateString("INST2",FileRecord.substring(Pointer,Pointer+40)); Pointer+=40;//INST2
                        rsLC.updateString("BNKHUN",FileRecord.substring(Pointer,Pointer+2)); Pointer+=2;//BNKHUN
                        rsLC.updateString("LOCADD1",FileRecord.substring(Pointer,Pointer+30)); Pointer+=30;//LOCADD1
                        rsLC.updateString("LOCADD2",FileRecord.substring(Pointer,Pointer+30)); Pointer+=30;//LOCADD2
                        rsLC.updateDouble("AMT",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+8))); Pointer+=8;//AMT
                        rsLC.updateString("BNGDOCIND",FileRecord.substring(Pointer,Pointer+1)); Pointer+=1;//BNGDOCIND
                        rsLC.updateString("LOCALBANK",FileRecord.substring(Pointer,Pointer+17)); Pointer+=17;//LOCALBANK
                        rsLC.updateBoolean("CANCELLED",false);
                        rsLC.updateString("CREATED_BY", "admin");
                        rsLC.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsLC.updateString("MODIFIED_BY", "admin");
                        rsLC.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsLC.updateBoolean("CHANGED", true);
                        rsLC.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsLC.updateInt("HIERARCHY_ID",0);
                        rsLC.updateBoolean("APPROVED", true);
                        rsLC.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsLC.updateBoolean("REJECTED", false);
                        rsLC.updateString("REJECTED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsLC.updateString("REJECTED_REMARKS", "");
                        Pointer +=1;//FILLER
                        
                        rsLC.insertRow();
                        
                        
                        System.out.println("LC Party Imported ");
                        
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
