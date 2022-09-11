/*
 * clsSynchronizeImport.java
 *
 * Created on December 6, 2005, 3:12 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */

import EITLERP.*;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import javax.sql.*;
import java.io.*;


public class clsSynchronizeImport {
    
    private int SelCompanyID=0;
    private String FromYear="";
    private String SyncAction="1";
    private boolean UpdateFlags=false;
    private String ImportFile="";
    private HashMap Tables=new HashMap();
    private String startURL="";
    
    /** Creates a new instance of clsSynchronizeImport */
    public clsSynchronizeImport() {
    }
    
    /*
     Expected Parameters from Command Line
     0. File Name
     1. Company ID
     2. Financial Year From
     3. Financial Year To
     4. Update Flags - Y/N
     5. Start URL
     */
    
    public static void main(String args[]) {
        
        if(args.length<6) {
            System.out.println("Insufficient parameters.");
            return;
        }
        
        data.OpenGlobalConnection();
        
        clsSynchronizeImport objSync=new clsSynchronizeImport();
        objSync.ImportFile=args[0];
        objSync.SelCompanyID=Integer.parseInt(args[1]);
        objSync.FromYear=args[2];
        objSync.SyncAction=args[3];
        
        if(args[4].equals("Y")) {
            objSync.UpdateFlags=true;
        }
        else {
            objSync.UpdateFlags=false;
        }
        
        objSync.startURL=args[5];
        
        objSync.StartImport();
        
    }
    
    
    private void StartImport() {
        HashMap Tables=new HashMap();
        
        new Thread(){
            public void run() {
                BufferedReader dataFile=null;
                
                try {
                    
                    System.out.println("Start Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    
                    Connection tmpConn=null,srcConn=null;
                    Statement stTmp=null,stSrc=null,stDel=null;
                    Statement stImp=null;
                    ResultSet rsTmp=null,rsSrc=null;
                    ResultSetMetaData rsInfo;
                    String Condition="";
                    int ColType=0,RecCount=0,RecPointer=0;
                    
                    System.out.println("Starting - Synchronizing data from source");
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    String currentURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear,startURL);
                    //===============================================================//
                    
                    
                    EITLERPGLOBAL.DatabaseURL=currentURL;
                    
                    System.out.println("Establishing Connection with target database");
                    
                    tmpConn=data.getConn(currentURL);
                    DatabaseMetaData dbInfo=tmpConn.getMetaData();
                    
                    srcConn=data.getConn(currentURL);
                    srcConn.setAutoCommit(false);
                    
                    stImp=srcConn.createStatement();
                    
                    //Open the File specified
                    dataFile=new BufferedReader(new FileReader(new File(ImportFile)));
                    
                    boolean Done=false;
                    
                    while(!Done) {
                        try {
                            String strSQL=dataFile.readLine();
                            System.out.println(strSQL);
                            
                            if(!strSQL.trim().equals("")) {
                                stImp.executeUpdate(strSQL);
                            }
                        }
                        catch(Exception re) {
                            //File must reach at end
                            Done=true;
                            re.printStackTrace();
                        }
                    }

                    
                    //Now Actually Update the data
                    srcConn.commit();
                    
                    System.out.println("Done, Successfully.");
                    System.out.println("End Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                }
                catch(Exception c) {
                    System.out.println("Error occured in the process ");
                    c.printStackTrace();
                    
                    try {
                    }
                    catch(Exception f) {
                        
                    }
                }
            }
        }.start();
    }
    
}
