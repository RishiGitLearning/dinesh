/*
 * clsPolicyPartyGroupingImport.java
 *
 * Created on May 11, 2009, 12:12 PM
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
public class clsPolicyPartyGroupingImport {
    
    /** Creates a new instance of clsPolicyPartyGroupingImport */
    public clsPolicyPartyGroupingImport() {
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
        
        String FileName="/data/Sales Accounting/partygrouping/partyGrouping_new.txt";
        
        clsPolicyPartyGroupingImport objImport=new clsPolicyPartyGroupingImport();        
        
        objImport.ImportPartyGrouping(FileName);         
    }
    
    public void ImportPartyGrouping(String FileName) {
        
        boolean Done=false;
        long Counter=0;
        
        try {            
            //String dbURL="jdbc:mysql://localhost:3306/DINESHMILLS";
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            Connection objConn=data.getConn(dbURL);
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='1' LIMIT 1");            
            //ResultSet rsTmp=data.getResult("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='1' LIMIT 1");            
            //rsTmp.first();

	    Statement stTmpH=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmpH=stTmpH.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_H WHERE PARENT_PARTY_ID='1' LIMIT 1");            
            //rsTmpH.first();

	    int Pointer=0;
            BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
            
            HashMap FileLines=new HashMap();
            int SrNo = 0;
            
            Done=false;
            
            while(!Done) {                                
                try {                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                                        
                    int nNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_POLICY_PARTY_GROUPING ",dbURL);
                    SrNo = nNo + 1;
		    String ChildParty = FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //PARTY_CD1
                    String ParentParty = FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //PARTY_CD2
                    if(!data.IsRecordExist("SELECT PARENT_PARTY_ID FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='"+ParentParty+"' AND CHILD_PARTY_ID='"+ChildParty+"' ",dbURL)) {
                        
                        System.out.println("Importing Parent Party = "+ParentParty+" and Child Party = "+ChildParty);
                        
                        //Pointer=0;                        
                        
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateInt("SR_NO",SrNo);
                        rsTmp.updateString("PARENT_PARTY_ID",ParentParty);  //Pointer+=6; //PARTY_CD2                        
                        rsTmp.updateString("CHILD_PARTY_ID",ChildParty);  //Pointer+=6; //PARTY_CD1
                        rsTmp.updateString("FROM_DATE", "2009-04-01");
                        rsTmp.updateString("TO_DATE", "2010-03-31");
                        rsTmp.updateBoolean("CANCELLED",false);
                        rsTmp.updateString("CREATED_BY", "admin");
                        rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateString("MODIFIED_BY", "admin");
                        rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());                        
                        rsTmp.updateBoolean("CHANGED", true);
                        rsTmp.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());                      
                        rsTmp.insertRow();          
                        
			rsTmpH.moveToInsertRow();
                        rsTmpH.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmpH.updateInt("REVISION_NO",1);
                        rsTmpH.updateInt("SR_NO",SrNo);
                        rsTmpH.updateString("PARENT_PARTY_ID",ParentParty);  //Pointer+=6; //PARTY_CD2                        
                        rsTmpH.updateString("CHILD_PARTY_ID",ChildParty);  //Pointer+=6; //PARTY_CD1
                        rsTmpH.updateString("FROM_DATE", "2009-04-01");
                        rsTmpH.updateString("TO_DATE", "2010-03-31");
                        rsTmpH.updateBoolean("CANCELLED",false);
                        rsTmpH.updateString("CREATED_BY", "admin");
                        rsTmpH.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsTmpH.updateString("MODIFIED_BY", "admin");
                        rsTmpH.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());                        
                        rsTmpH.updateBoolean("CHANGED", true);
                        rsTmpH.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());                      
                        rsTmpH.insertRow();                        
                        
                        System.out.println("Party Grouping List ");                        
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
