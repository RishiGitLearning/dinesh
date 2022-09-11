/*
 * clsPolicyFeltPriceListImport.java
 *
 * Created on April 7, 2009, 11:49 AM
 */

package EITLERP.Sales;

import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Sales.*;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class clsPolicyFeltPriceListImport {
    
    /** Creates a new instance of clsPolicyFeltPriceListImport */
    public clsPolicyFeltPriceListImport() {
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
        
        //        String FileName=args[0];
        
        String FileName="/data/FIRM23.TXT";
        
        clsPolicyFeltPriceListImport objImport=new clsPolicyFeltPriceListImport();
        
        
        objImport.ImportFeltPriceList(FileName);
        
        
    }
    
    public void ImportFeltPriceList(String FileName) {
        
        boolean Done=false;
        long Counter=0;
        
        try {            
            String dbURL="jdbc:mysql://200.O.O.223:3306/DINESHMILLS";
                        
            Connection objConn=data.getConn(dbURL);
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST WHERE QUALITY_ID='1' LIMIT 1");
            int Pointer=0;
            rsTmp.first();
            BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
            
            HashMap FileLines=new HashMap();
            int SrNo = 0;
            
            Done=false;
            
            while(!Done) {                                
                try {                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                                        
                    int nNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_POLICY_FELT_PRICELIST ");
                    SrNo = nNo + 1;
                    Pointer=Pointer+1;
                    String QualityID = FileRecord.substring(Pointer,Pointer+7);
                    if(!data.IsRecordExist("SELECT QUALITY_ID FROM D_SAL_POLICY_FELT_PRICELIST WHERE QUALITY_ID='"+QualityID+"' " ,dbURL)) {
                        
                        System.out.println("Importing Quality "+QualityID);
                        
                        Pointer=0;                        
                        
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateInt("SR_NO",SrNo);
                        rsTmp.updateString("WH_CODE",FileRecord.substring(Pointer,Pointer+1));  Pointer+=1; //WH_CODE                        
                        rsTmp.updateString("QUALITY_ID",FileRecord.substring(Pointer,Pointer+7));  Pointer+=7; //QUALITY_ID
                        rsTmp.updateString("QUALITY_DESC",FileRecord.substring(Pointer,Pointer+35)); Pointer+=35; //QUALITY_DESC
                        rsTmp.updateDouble("SYN_PERC",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+4))/10); Pointer+=4; //SYN_PERC
                        rsTmp.updateDouble("SQM_RATE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //SQM_RATE
                        rsTmp.updateDouble("WT_RATE",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //WT_RATE
                        rsTmp.updateDouble("SQM_RATE_O",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //SQM_RATE_O
                        rsTmp.updateDouble("WT_RATE_O",UtilFunctions.CDbl(FileRecord.substring(Pointer,Pointer+6))/100); Pointer+=6; //WT_RATE_O
                        rsTmp.updateInt("CHEM_TRTIN",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1; //CHEM_TRTIN                        
                        rsTmp.updateInt("PIN_IND",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;//PIN_IND
                        rsTmp.updateInt("SPRL_IND",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;//SPRL_IND
                        rsTmp.updateInt("SUR_CHGIND",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1; //SUR_CHGIND
                        rsTmp.updateInt("SQM_IND",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1; //SQM_IND
                        rsTmp.updateInt("FILLER",UtilFunctions.CInt(FileRecord.substring(Pointer,Pointer+1))); Pointer+=1;//FILLER
                        rsTmp.updateBoolean("CANCELLED",false);
                        rsTmp.updateString("CREATED_BY", "admin");
                        rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateString("MODIFIED_BY", "admin");
                        rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());                        
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());                      
                        rsTmp.insertRow();                        
                        
                        System.out.println("Quality Price List ");
                        
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
