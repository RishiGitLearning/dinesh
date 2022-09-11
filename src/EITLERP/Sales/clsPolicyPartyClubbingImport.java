/*
 * clsPolicyPartyClubbing.java
 *
 * Created on June 18, 2009, 11:26 AM
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
public class clsPolicyPartyClubbingImport {
    
    /** Creates a new instance of clsPolicyPartyClubbing */
    public clsPolicyPartyClubbingImport() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String FileName="/data/Sales Accounting/partygrouping/partyClubbing.txt";
        
        clsPolicyPartyClubbingImport objImport=new clsPolicyPartyClubbingImport();        
        
        objImport.ImportPartyClubbing(FileName);         
    }
    
    public void ImportPartyClubbing(String FileName) {
        
        boolean Done=false;
        long Counter=0;
        
        try {            
            //String dbURL="jdbc:mysql://localhost:3306/DINESHMILLS";
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            
            Connection objConn=data.getConn(dbURL);
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING WHERE PARTY_ID='1' LIMIT 1");            
            //ResultSet rsTmp=data.getResult("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING WHERE PARENT_PARTY_ID='1' LIMIT 1");            
            //rsTmp.first();

	    Statement stTmpH=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmpH=stTmpH.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_CLUBBING_H WHERE PARTY_ID='1' LIMIT 1");            
            //rsTmpH.first();

	    int Pointer=0;
            BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
            
            HashMap FileLines=new HashMap();
            int SrNo = 0;
            int DocNo=0;
            String FirstParty="";
            String SecondParty="";
            
            Done=false;
            
            while(!Done) {                                
                try {                    
                    String FileRecord=aFile.readLine();
                    Pointer=0;
                                                            
		    FirstParty = FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //PARTY_CD1
                    SecondParty = FileRecord.substring(Pointer,Pointer+6); Pointer+=6; //PARTY_CD2
                    if(!data.IsRecordExist("SELECT PARTY_ID FROM D_SAL_POLICY_PARTY_CLUBBING WHERE PARTY_ID='"+SecondParty+"' ",dbURL)) {
                        
                        System.out.println("Importing First Party = "+FirstParty+" Second Party = "+SecondParty);
                                                
                        int nNo=data.getIntValueFromDB("SELECT MAX(DOC_NO) FROM D_SAL_POLICY_PARTY_CLUBBING ",dbURL);
                        DocNo = nNo + 1;
                        SrNo = 0;
                        SrNo ++;
                    
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateInt("DOC_NO",DocNo);
                        rsTmp.updateInt("SR_NO",SrNo);
                        rsTmp.updateString("PARTY_ID",FirstParty);  //Pointer+=6; //PARTY_CD2                                                
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
                        rsTmpH.updateInt("DOC_NO",DocNo);
                        rsTmpH.updateInt("SR_NO",SrNo);
                        rsTmpH.updateString("PARTY_ID",FirstParty);  //Pointer+=6; //PARTY_CD2                        
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
                        
                        SrNo ++;
                    
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateInt("DOC_NO",DocNo);
                        rsTmp.updateInt("SR_NO",SrNo);
                        rsTmp.updateString("PARTY_ID",SecondParty);  //Pointer+=6; //PARTY_CD2                                                
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
                        rsTmpH.updateInt("DOC_NO",DocNo);
                        rsTmpH.updateInt("SR_NO",SrNo);
                        rsTmpH.updateString("PARTY_ID",SecondParty);  //Pointer+=6; //PARTY_CD2                        
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
                        
                        System.out.println("Party Clubbing List ");                        
                    }    
                    else {
                        System.out.println("Importing First Party = "+FirstParty+" Second Party = "+SecondParty);
                                                
                        SrNo ++;
                    
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateInt("DOC_NO",DocNo);
                        rsTmp.updateInt("SR_NO",SrNo);
                        rsTmp.updateString("PARTY_ID",FirstParty);  //Pointer+=6; //PARTY_CD2                                                
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
                        rsTmpH.updateInt("DOC_NO",DocNo);
                        rsTmpH.updateInt("SR_NO",SrNo);
                        rsTmpH.updateString("PARTY_ID",FirstParty);  //Pointer+=6; //PARTY_CD2                        
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
                        
                        System.out.println("Party Clubbing List "); 
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
