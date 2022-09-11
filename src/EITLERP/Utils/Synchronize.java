/*
 * Synchronize.java
 *
 * Created on November 12, 2005, 12:02 PM
 */

package EITLERP.Utils;

import EITLERP.*;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import javax.sql.*;
import java.io.*;

/**
 *
 * @author  root
 */
public class Synchronize {
    
    private int SelCompanyID=0;
    private String FromYear="";
    private String SyncAction="1";
    private boolean UpdateFlags=false;
    
    private HashMap Tables=new HashMap();
    
    /** Creates a new instance of Synchronize */
    public Synchronize() {
    }
    
    /*
     Expected Parameters from Command Line
     1. Company ID
     2. Financial Year From
     3. Financial Year To
     4. Synchronization Action - 1 - Synchronize From.  2 - Synchronize To.
     5. Update Flags - Y/N
     */
    
    public static void main(String args[]) {
        
        
        if(args.length<5) {
            System.out.println("Insufficient Parameters");
            return;
        }
        
        
        data.OpenGlobalConnection();
        
        Synchronize objSync=new Synchronize();
        objSync.SelCompanyID=Integer.parseInt(args[0]);
        objSync.FromYear=args[1];
        objSync.SyncAction=args[3];
        
        if(args[4].equals("Y")) {
            objSync.UpdateFlags=true;
        }
        else {
            objSync.UpdateFlags=false;
        }
        
        if(args[3].equals("1")) {
            objSync.SyncDataFromSource();
        }
        
        if(args[3].equals("2")) {
            objSync.SyncDataToSource();
        }
        
    }
    
    
    
    private void SyncDataFromSource() {
        
        HashMap Tables=new HashMap();
        
        new Thread(){
            public void run() {
                BufferedWriter aFile=null;
                
                Connection srcConn=null,destConn=null;
                try {
                    
                    String lastTable="";
                    EITLERPGLOBAL.DBUserName="";
                    String logFile="f"+Integer.toString(EITLERPGLOBAL.getCurrentYear())+Integer.toString(EITLERPGLOBAL.getCurrentMonth())+Integer.toString(EITLERPGLOBAL.getCurrentDay())+".txt";
                    
                    aFile=new BufferedWriter(new FileWriter(new File("/var/log/eitlerplogs/"+logFile)));
                    
                    System.out.println("Start Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    
                    Connection tmpConn;
                    Statement stTmp,stSrc,stDest,stDel;
                    ResultSet rsTmp,rsSrc,rsDest;
                    ResultSetMetaData rsInfo;
                    String Condition="";
                    int ColType=0,RecCount=0,RecPointer=0;
                    
                    System.out.println("Starting - Synchronizing data from source");
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    String currentURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear);
                    String srcURL=clsFinYear.getSyncURL((int)nCompanyID,tmpFromYear);
                    //===============================================================//
                    
                    
                    aFile.write("Synchronizing from company id "+nCompanyID);
                    aFile.newLine();
                    aFile.write("Synchronization start time "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    aFile.newLine();
                    
                    EITLERPGLOBAL.DatabaseURL=currentURL;
                    
                    System.out.println("Establishing Connection with target database");
                    
                    tmpConn=data.getCreatedConn();
                    
                    
                    DatabaseMetaData dbInfo=tmpConn.getMetaData();
                    
                    srcConn=data.getConn(srcURL);
                    
                    
                    System.out.println("connecting to ... "+currentURL);
                    destConn=data.getConn(currentURL);
                    
                    
                    destConn.setTransactionIsolation(destConn.TRANSACTION_READ_UNCOMMITTED);
                    destConn.setAutoCommit(false); //Turning AutoCommit off
                    
                    
                    srcConn.setTransactionIsolation(srcConn.TRANSACTION_READ_UNCOMMITTED);
                    srcConn.setAutoCommit(false);
                    
                    
                    //====================== Gather Database Information ================//
                    String[] names = {"TABLE"};
                    HashMap Tables=new HashMap();
                    int Counter=0;
                    int PKCounter=0;
                    ResultSet tableNames = dbInfo.getTables(null,"%", "%", names);
                    
                    System.out.println("Retrieving Database Information");
                    
                    while (tableNames.next()) {
                        Counter++;
                        Tables.put(Integer.toString(Counter),tableNames.getString("TABLE_NAME"));
                        String TableName=tableNames.getString("TABLE_NAME");
                        
                        clsTableInfo ObjTable=new clsTableInfo();
                        ObjTable.TableName=TableName;
                        
                        ResultSet rsPrimary=dbInfo.getPrimaryKeys(null,null, TableName);
                        
                        PKCounter=0;
                        while(rsPrimary.next()) {
                            PKCounter++;
                            String Column=rsPrimary.getString("COLUMN_NAME");
                            
                            clsPrimaryKey ObjPrimaryKey=new clsPrimaryKey();
                            
                            ObjPrimaryKey.ColumnName=Column;
                            
                            //=========== Get the Primary Key Datatype =====//
                            rsTmp=data.getResult("SELECT "+Column+" FROM "+TableName+" LIMIT 1");
                            rsInfo=rsTmp.getMetaData();
                            
                            ObjPrimaryKey.ColumnType=rsInfo.getColumnType(1);
                            //=============================================//
                            
                            ObjTable.colPrimaryKeys.put(Integer.toString(PKCounter),ObjPrimaryKey);
                        }
                        
                        Tables.put(Integer.toString(Counter),ObjTable);
                    }
                    //=============== Done Gathering Database Information ==================//
                    
                    
                    System.gc();
                    System.out.println("Retrieving data from Tables");
                    
                    //=Step (1) Get the data from Source and Update it to the current Database //
                    for(int i=1;i<=Tables.size();i++) {
                        clsTableInfo ObjTable=(clsTableInfo)Tables.get(Integer.toString(i));
                        String TableName=ObjTable.TableName;
                        
                        lastTable=TableName;
                        
                        try {
                            
                            System.out.println("Table "+TableName);
                            
                            String strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+" WHERE CHANGED=1";
                            stSrc=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                            rsSrc=stSrc.executeQuery(strSQL);
                            rsSrc.first();
                            
                            RecCount=rsSrc.getInt("RECCOUNT");
                            
                            //rsSrc.close();
                            
                            strSQL="SELECT * FROM "+TableName+" WHERE CHANGED=1";
                            stSrc=srcConn.createStatement();
                            rsSrc=stSrc.executeQuery(strSQL);
                            rsSrc.first();
                            
                            rsInfo=rsSrc.getMetaData();
                            
                            if(rsSrc.getRow()>0) {
                                //Open Destination Recordset for insertion
                                stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                rsDest=stDest.executeQuery("SELECT * FROM "+TableName+" LIMIT 1");
                                
                                while(!rsSrc.isAfterLast()) {
                                    
                                    System.out.println("Processing Row "+rsSrc.getRow());
                                    
                                    //Check the Record Existence in destination
                                    //If record exist - Remove it as source database is
                                    //Dominant database
                                    Condition="";
                                    
                                    //========== Check the record for any updation required -----------------//
                                    for(int p=1;p<=rsInfo.getColumnCount();p++) {
                                        String FieldName=rsInfo.getColumnName(p);
                                        
                                        //Pass it through Interceptor class
                                        Variant objNewValue=clsCheckRecord.checkRecord(destConn, TableName, rsSrc, FieldName);
                                        
                                        if(objNewValue!=null) //If any updation has been made
                                        {
                                            
                                            switch(rsInfo.getColumnType(p)) {
                                                case -5: //Long
                                                    rsDest.updateLong(FieldName,(long)objNewValue.getVal());
                                                    break;
                                                case 4: //Integer,Small int
                                                    rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                    break;
                                                case 5: //Integer,Small int
                                                    rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                    break;
                                                case -6: //Integer,Small int
                                                    rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                    break;
                                                case 16: //Boolean
                                                    rsDest.updateBoolean(FieldName,objNewValue.getBool());
                                                    break;
                                                case 91: //Date
                                                    rsDest.updateDate(FieldName,java.sql.Date.valueOf((String)objNewValue.getObj()));
                                                    break;
                                                case 8: //Double
                                                    rsDest.updateDouble(FieldName,objNewValue.getVal());
                                                    break;
                                                case 6: //Float
                                                    rsDest.updateDouble(FieldName,objNewValue.getVal());
                                                    break;
                                                case 12 ://Varchar
                                                    rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                    break;
                                                case 1 ://char
                                                    rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                    break;
                                                default : //Varchar
                                                    rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                    break;
                                            } //Switch
                                            
                                        }
                                    }
                                    //=======================================================================//
                                    
                                    
                                    
                                    
                                    
                                    //============= Building the Condition ===============//
                                    for(int p=1;p<=ObjTable.colPrimaryKeys.size();p++) {
                                        clsPrimaryKey ObjPrimaryKey=(clsPrimaryKey)ObjTable.colPrimaryKeys.get(Integer.toString(p));
                                        
                                        String PKName=ObjPrimaryKey.ColumnName;
                                        ColType=ObjPrimaryKey.ColumnType;
                                        
                                        //Now based on the type we have to retrieve the value of field
                                        switch(ColType) {
                                            case -5: //Long
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getLong(PKName);
                                                break;
                                            case 4: //Integer,Small int
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                                break;
                                            case 5: //Integer,Small int
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                                break;
                                            case -6: //Integer,Small int
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                                break;
                                            case 16: //Boolean
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getShort(PKName);
                                                break;
                                            case 91: //Date
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getDate(PKName);
                                                break;
                                            case 8: //Double
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getDouble(PKName);
                                                break;
                                            case 6: //Float
                                                Condition=Condition+" AND "+PKName+"="+rsSrc.getFloat(PKName);
                                                break;
                                            case 12 ://Varchar
                                                Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                                break;
                                            case 1 ://char
                                                Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                                break;
                                            default : //Varchar
                                                Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                                break;
                                        } //Switch
                                    }
                                    //=============== Building Condition Completed =====================//
                                    
                                    if(!Condition.trim().equals("")) {
                                        Condition=" WHERE "+Condition.substring(5); //Removing 'AND' from begining of the statement
                                    }
                                    
                                    
                                    boolean RecordExist=false;
                                    
                                    
                                    //====== Search for the similar record in target database and if that record has changed flag 1 ==== //
                                    //-- Don't replace that record -- //
                                    boolean IsUpdatedRecord=false;
                                    
                                    strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+Condition+" AND CHANGED=1";
                                    
                                    stDest=destConn.createStatement();
                                    rsTmp=stDest.executeQuery(strSQL);
                                    rsTmp.first();
                                    
                                    if(rsTmp.getRow()>0) {
                                        
                                        int ERecCount=rsTmp.getInt("RECCOUNT");
                                        
                                        if(ERecCount>0) {
                                            RecordExist=true; //Record in Destination database exist
                                            
                                            IsUpdatedRecord=true;
                                            
                                        }
                                    }
                                    //=========================================================//
                                    
                                    
                                    
                                    if(!IsUpdatedRecord) {
                                        strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+Condition;
                                        
                                        stDest=destConn.createStatement();
                                        rsTmp=stDest.executeQuery(strSQL);
                                        rsTmp.first();
                                        
                                        if(rsTmp.getRow()>0) {
                                            
                                            int ERecCount=rsTmp.getInt("RECCOUNT");
                                            
                                            if(ERecCount>0) {
                                                RecordExist=true; //Record in Destination database exist
                                                
                                                //Remove it
                                                strSQL="DELETE FROM "+TableName+" "+Condition;
                                                
                                                stDel=destConn.createStatement();
                                                stDel.executeUpdate(strSQL);
                                            }
                                        }
                                        
                                        //rsTmp.close();
                                    }
                                    //============= Record Existence and Removal Completed ===============//
                                    
                                    
                                    
                                    //========== Insert Actual Record in destination database =============//
                                    if(!IsUpdatedRecord) {
                                        System.out.println("Starting Insertion");
                                        
                                        rsDest.moveToInsertRow();
                                        
                                        for(int f=1;f<=rsInfo.getColumnCount();f++) {
                                            //Now based on the type update the Fields
                                            
                                            //Pass it through Interceptor to alter any primary keys to avoid conflicts
                                            
                                            ColType=rsInfo.getColumnType(f);
                                            switch(ColType) {
                                                case -5: //Long
                                                    rsDest.updateLong(f,rsSrc.getLong(f));
                                                    break;
                                                case 4: //Integer,Small int
                                                    rsDest.updateInt(f,rsSrc.getInt(f));
                                                    break;
                                                case 5: //Integer,Small int
                                                    rsDest.updateInt(f,rsSrc.getInt(f));
                                                    break;
                                                case -6: //Integer,Small int
                                                    rsDest.updateInt(f,rsSrc.getInt(f));
                                                    break;
                                                case 16: //Boolean
                                                    rsDest.updateBoolean(f,rsSrc.getBoolean(f));
                                                    break;
                                                case 91: //Date
                                                    rsDest.updateDate(f,rsSrc.getDate(f));
                                                    break;
                                                case 8: //Double
                                                    rsDest.updateDouble(f,rsSrc.getDouble(f));
                                                    break;
                                                case 6: //Float
                                                    rsDest.updateFloat(f,rsSrc.getFloat(f));
                                                    break;
                                                case 12 ://Varchar
                                                    rsDest.updateString(f,rsSrc.getString(f));
                                                    break;
                                                case 1 ://char
                                                    rsDest.updateString(f,rsSrc.getString(f));
                                                    break;
                                                default : //Varchar
                                                    rsDest.updateString(f,rsSrc.getString(f));
                                                    break;
                                            } //Switch
                                            
                                        }
                                        
                                        //Now Explicitly set the Changed Flag to False
                                        rsDest.updateBoolean("CHANGED",false);
                                        rsDest.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                        rsDest.insertRow();
                                    }
                                    //=====================================================================//
                                    
                                    //Read the Record and Update the destination
                                    
                                    if(UpdateFlags)
                                    {
                                      
                                      strSQL="UPDATE "+TableName+" SET CHANGED=0 "+Condition;
                                      
                                      System.out.println("Updating Flag");
                                      stSrc.executeUpdate(strSQL);
                                      
                                      //rsSrc.updateBoolean("CHANGED",false);  
                                      //rsSrc.updateRow();
                                      
                                    }
                                                                        
                                    rsSrc.next();
                                    
                                }
                                
                            }
                            
                            
                        }
                        catch(Exception table) {
                            System.out.println("Table does not exist "+lastTable);
                            
                        }
                        
                    }
                    //=========================================================================//
                    
                    
                    //===============Updating the States of Source Database =============//
//                    if(UpdateFlags) {
//                        System.out.println("Updating States of database");
//                        
//                        for(int i=1;i<=Tables.size();i++) {
//                            
//                            clsTableInfo ObjTable=(clsTableInfo)Tables.get(Integer.toString(i));
//                            String TableName=ObjTable.TableName;
//                            
//                            System.out.println("Updating "+TableName);
//                            
//                            stSrc=srcConn.createStatement();
//                            stSrc.executeUpdate("UPDATE "+TableName+" SET CHANGED=0 WHERE CHANGED=1");
//                        }
//                    }
                    //==================================================================//
                    
                    
                    System.out.println("Now Commitng changes ... ");
                    //Now Actually Update the data
                    destConn.commit();
                    srcConn.commit();
                    
                    System.out.println("Done, Successfully.");
                    System.out.println("End Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    
                    aFile.write("Synchronization completed successfully.");
                    aFile.newLine();
                    
                    aFile.write("Synchronization end time "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    aFile.newLine();
                    
                    aFile.close();
                }
                catch(Exception c) {
                    
                    
                    try
                    {
                    destConn.rollback();
                    srcConn.rollback();
                    System.out.println("Rollback Succeeded");
                    }
                    catch(Exception r)
                    {
                      System.out.println("Rollback failed ");    
                    }
                    
                    System.out.println("Error occured in the process ");
                    c.printStackTrace();
                    
                    try {
                        aFile.write("Error occured in process. Rolled back all the changes");
                        aFile.close();
                    }
                    catch(Exception f) {
                        
                    }
                }
            }
        }.start();
    }
    
    
    
    private void SyncDataToSource() {
        
        HashMap Tables=new HashMap();
        
        new Thread(){
            public void run() {
                BufferedWriter aFile=null;
                
                Connection srcConn=null,destConn=null;
                
                try {
                    EITLERPGLOBAL.DBUserName="";
                    String logFile="t"+Integer.toString(EITLERPGLOBAL.getCurrentYear())+Integer.toString(EITLERPGLOBAL.getCurrentMonth())+Integer.toString(EITLERPGLOBAL.getCurrentDay())+".txt";
                    
                    aFile=new BufferedWriter(new FileWriter(new File("/var/log/eitlerplogs/"+logFile)));
                    
                    System.out.println("Start Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    
                    Connection tmpConn;
                    Statement stTmp,stSrc,stDest,stDel;
                    ResultSet rsTmp,rsSrc,rsDest;
                    ResultSetMetaData rsInfo;
                    String Condition="";
                    int ColType=0,RecCount=0,RecPointer=0;
                    
                    System.out.println("Synchronizing to Source");
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    String currentURL=clsFinYear.getSyncURL((int)nCompanyID,tmpFromYear);
                    String srcURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear);
                    //===============================================================//
                    
                    System.out.println("Establishing Connection with target database");
                    
                    aFile.write("Synchronizing to company id "+nCompanyID);
                    aFile.newLine();
                    aFile.write("Synchronization start time "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    aFile.newLine();
                    
                    EITLERPGLOBAL.DatabaseURL=srcURL;
                    
                    tmpConn=data.getCreatedConn();
                    DatabaseMetaData dbInfo=tmpConn.getMetaData();
                    
                    srcConn=data.getConn(srcURL);
                    destConn=data.getConn(currentURL);
                    
                    
                    destConn.setTransactionIsolation(destConn.TRANSACTION_READ_UNCOMMITTED);
                    destConn.setAutoCommit(false); //Turning AutoCommit off
                    
                    srcConn.setTransactionIsolation(srcConn.TRANSACTION_READ_UNCOMMITTED);
                    srcConn.setAutoCommit(false);
                    
                    
                    
                    //====================== Gather Database Information ================//
                    String[] names = {"TABLE"};
                    HashMap Tables=new HashMap();
                    int Counter=0;
                    int PKCounter=0;
                    ResultSet tableNames = dbInfo.getTables(null,"%", "%", names);
                    
                    System.out.println("Retrieving Database Information");
                    
                    while (tableNames.next()) {
                        Counter++;
                        Tables.put(Integer.toString(Counter),tableNames.getString("TABLE_NAME"));
                        String TableName=tableNames.getString("TABLE_NAME");
                        
                        clsTableInfo ObjTable=new clsTableInfo();
                        ObjTable.TableName=TableName;
                        
                        ResultSet rsPrimary=dbInfo.getPrimaryKeys(null,null, TableName);
                        
                        PKCounter=0;
                        while(rsPrimary.next()) {
                            PKCounter++;
                            String Column=rsPrimary.getString("COLUMN_NAME");
                            
                            clsPrimaryKey ObjPrimaryKey=new clsPrimaryKey();
                            
                            ObjPrimaryKey.ColumnName=Column;
                            
                            //=========== Get the Primary Key Datatype =====//
                            rsTmp=data.getResult("SELECT "+Column+" FROM "+TableName+" LIMIT 1");
                            rsInfo=rsTmp.getMetaData();
                            
                            ObjPrimaryKey.ColumnType=rsInfo.getColumnType(1);
                            //=============================================//
                            
                            ObjTable.colPrimaryKeys.put(Integer.toString(PKCounter),ObjPrimaryKey);
                        }
                        
                        Tables.put(Integer.toString(Counter),ObjTable);
                    }
                    //=============== Done Gathering Database Information ==================//
                    
                    
                    System.gc();
                    
                    System.out.println("Retrieving data from Tables");
                    
                    //=Step (1) Get the data from Source and Update it to the current Database //
                    for(int i=1;i<=Tables.size();i++) {
                        clsTableInfo ObjTable=(clsTableInfo)Tables.get(Integer.toString(i));
                        String TableName=ObjTable.TableName;
                        
                        try
                        {
                            
                        
                        String strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+" WHERE CHANGED=1";
                        stSrc=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        rsSrc=stSrc.executeQuery(strSQL);
                        rsSrc.first();
                        
                        RecCount=rsSrc.getInt("RECCOUNT");
                        
                        rsSrc.close();
                        
                        System.out.println("Table "+TableName);
                        
                        
                        strSQL="SELECT * FROM "+TableName+" WHERE CHANGED=1";
                        stSrc=srcConn.createStatement();
                        rsSrc=stSrc.executeQuery(strSQL);
                        rsSrc.first();
                        
                        rsInfo=rsSrc.getMetaData();
                        
                        if(rsSrc.getRow()>0) {
                            //Open Destination Recordset for insertion
                            stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsDest=stDest.executeQuery("SELECT * FROM "+TableName+" LIMIT 1");
                            
                            while(!rsSrc.isAfterLast()) {
                                
                                System.out.println("Processing row no. "+rsSrc.getRow());
                                
                                //Check the Record Existence in destination
                                //If record exist - Remove it as source database is
                                //Dominant database
                                Condition="";
                                
                                
                                //========== Check the record for any updation required -----------------//
                                for(int p=1;p<=rsInfo.getColumnCount();p++) {
                                    String FieldName=rsInfo.getColumnName(p);
                                    
                                    //Pass it through Interceptor class
                                    Variant objNewValue=clsCheckRecord.checkRecord(destConn, TableName, rsSrc, FieldName);
                                    
                                    if(objNewValue!=null) //If any updation has been made
                                    {
                                        
                                        switch(rsInfo.getColumnType(p)) {
                                            case -5: //Long
                                                rsDest.updateLong(FieldName,(long)objNewValue.getVal());
                                                break;
                                            case 4: //Integer,Small int
                                                rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                break;
                                            case 5: //Integer,Small int
                                                rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                break;
                                            case -6: //Integer,Small int
                                                rsDest.updateInt(FieldName,(int)objNewValue.getVal());
                                                break;
                                            case 16: //Boolean
                                                rsDest.updateBoolean(FieldName,objNewValue.getBool());
                                                break;
                                            case 91: //Date
                                                rsDest.updateDate(FieldName,java.sql.Date.valueOf((String)objNewValue.getObj()));
                                                break;
                                            case 8: //Double
                                                rsDest.updateDouble(FieldName,objNewValue.getVal());
                                                break;
                                            case 6: //Float
                                                rsDest.updateDouble(FieldName,objNewValue.getVal());
                                                break;
                                            case 12 ://Varchar
                                                rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                break;
                                            case 1 ://char
                                                rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                break;
                                            default : //Varchar
                                                rsDest.updateString(FieldName,(String)objNewValue.getObj());
                                                break;
                                        } //Switch
                                        
                                    }
                                }
                                //=======================================================================//
                                
                                
                                //============= Building the Condition ===============//
                                for(int p=1;p<=ObjTable.colPrimaryKeys.size();p++) {
                                    clsPrimaryKey ObjPrimaryKey=(clsPrimaryKey)ObjTable.colPrimaryKeys.get(Integer.toString(p));
                                    
                                    String PKName=ObjPrimaryKey.ColumnName;
                                    ColType=ObjPrimaryKey.ColumnType;
                                    
                                    //Now based on the type we have to retrieve the value of field
                                    switch(ColType) {
                                        case -5: //Long
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getLong(PKName);
                                            break;
                                        case 4: //Integer,Small int
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                            break;
                                        case 5: //Integer,Small int
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                            break;
                                        case -6: //Integer,Small int
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getInt(PKName);
                                            break;
                                        case 16: //Boolean
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getShort(PKName);
                                            break;
                                        case 91: //Date
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getDate(PKName);
                                            break;
                                        case 8: //Double
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getDouble(PKName);
                                            break;
                                        case 6: //Float
                                            Condition=Condition+" AND "+PKName+"="+rsSrc.getFloat(PKName);
                                            break;
                                        case 12 ://Varchar
                                            Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                            break;
                                        case 1 ://char
                                            Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                            break;
                                        default : //Varchar
                                            Condition=Condition+" AND "+PKName+"='"+rsSrc.getString(PKName)+"'";
                                            break;
                                    } //Switch
                                }
                                //=============== Building Condition Completed =====================//
                                
                                if(!Condition.trim().equals("")) {
                                    Condition=" WHERE "+Condition.substring(5); //Removing 'AND' from begining of the statement
                                }
                                
                                
                                boolean RecordExist=false;
                                
                                
                                //====== Search for the similar record in target database and if that record has changed flag 1 ==== //
                                //-- Don't replace that record
                                boolean IsUpdatedRecord=false;
                                
                                strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+Condition+" AND CHANGED=1";
                                
                                stDest=destConn.createStatement();
                                rsTmp=stDest.executeQuery(strSQL);
                                rsTmp.first();
                                
                                if(rsTmp.getRow()>0) {
                                    
                                    int ERecCount=rsTmp.getInt("RECCOUNT");
                                    
                                    if(ERecCount>0) {
                                        RecordExist=true; //Record in Destination database exist
                                        
                                        IsUpdatedRecord=true;
                                        
                                    }
                                }
                                //=========================================================//
                                
                                
                                if(!IsUpdatedRecord) {
                                    strSQL="SELECT COUNT(*) AS RECCOUNT FROM "+TableName+Condition;
                                    
                                    stDest=destConn.createStatement();
                                    rsTmp=stDest.executeQuery(strSQL);
                                    rsTmp.first();
                                    
                                    if(rsTmp.getRow()>0) {
                                        
                                        int ERecCount=rsTmp.getInt("RECCOUNT");
                                        
                                        if(ERecCount>0) {
                                            RecordExist=true; //Record in Destination database exist
                                            
                                            //Remove it
                                            strSQL="DELETE FROM "+TableName+" "+Condition;
                                            stDel=destConn.createStatement();
                                            stDel.executeUpdate(strSQL);
                                        }
                                    }
                                    
                                    rsTmp.close();
                                }
                                //============= Record Existence and Removal Completed ===============//
                                
                                
                                //========== Insert Actual Record in destination database =============//
                                if(!IsUpdatedRecord) {
                                    System.out.println("Starting Insertion");
                                    
                                    rsDest.moveToInsertRow();
                                    
                                    for(int f=1;f<=rsInfo.getColumnCount();f++) {
                                        //Now based on the type update the Fields
                                        ColType=rsInfo.getColumnType(f);
                                        switch(ColType) {
                                            case -5: //Long
                                                rsDest.updateLong(f,rsSrc.getLong(f));
                                                break;
                                            case 4: //Integer,Small int
                                                rsDest.updateInt(f,rsSrc.getInt(f));
                                                break;
                                            case 5: //Integer,Small int
                                                rsDest.updateInt(f,rsSrc.getInt(f));
                                                break;
                                            case -6: //Integer,Small int
                                                rsDest.updateInt(f,rsSrc.getInt(f));
                                                break;
                                            case 16: //Boolean
                                                rsDest.updateBoolean(f,rsSrc.getBoolean(f));
                                                break;
                                            case 91: //Date
                                                rsDest.updateDate(f,rsSrc.getDate(f));
                                                break;
                                            case 8: //Double
                                                rsDest.updateDouble(f,rsSrc.getDouble(f));
                                                break;
                                            case 6: //Float
                                                rsDest.updateFloat(f,rsSrc.getFloat(f));
                                                break;
                                            case 12 ://Varchar
                                                rsDest.updateString(f,rsSrc.getString(f));
                                                break;
                                            case 1 ://char
                                                rsDest.updateString(f,rsSrc.getString(f));
                                                break;
                                            default : //Varchar
                                                rsDest.updateString(f,rsSrc.getString(f));
                                                break;
                                        } //Switch
                                        
                                    }
                                    
                                    //Now Explicitly set the Changed Flag to False
                                    rsDest.updateBoolean("CHANGED",false);
                                    rsDest.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                    rsDest.insertRow();
                                }
                                //=====================================================================//
                                
                                //Read the Record and Update the destination
                                    if(UpdateFlags)
                                    {
                                        
                                      strSQL="UPDATE "+TableName+" SET CHANGED=0 "+Condition;
                                      
                                      System.out.println("Updating Flag");
                                      stSrc.executeUpdate(strSQL);
                                        
                                      //rsSrc.updateBoolean("CHANGED",false);  
                                      //rsSrc.updateRow();
                                    }
                                
                                rsSrc.next();
                                
                                
                            }
                            
                        }
                        }
                        catch(Exception table)
                        {
                            table.printStackTrace();
                            System.out.println("Table does not exist ");
                        }
                        
                    }
                    //=========================================================================//
                    
                    //===============Updating the States of Source Database =============//
//                    if(UpdateFlags) {
//                        
//                        System.out.println("Updating States of database");
//                        
//                        
//                        for(int i=1;i<=Tables.size();i++) {
//                            
//                            
//                            clsTableInfo ObjTable=(clsTableInfo)Tables.get(Integer.toString(i));
//                            String TableName=ObjTable.TableName;
//                            
//                            System.out.println("Updating "+TableName);
//                            
//                            stSrc=srcConn.createStatement();
//                            stSrc.executeUpdate("UPDATE "+TableName+" SET CHANGED=0 WHERE CHANGED=1");
//                        }
//                    }
                    //==================================================================//
                    
                    
                    
                    //Now Actually Update the database
                    destConn.commit();
                    srcConn.commit();
                                        
                    System.out.println("Done, Successfully");
                    System.out.println("End Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    
                    aFile.write("Synchronization completed successfully.");
                    aFile.newLine();
                    
                    aFile.write("Synchronization end time "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                    aFile.newLine();
                    
                    aFile.close();
                    
                }
                catch(Exception c) {
                    
                    try
                    {
                    destConn.rollback();
                    srcConn.rollback();
                    System.out.println("Rollback Succeeded");
                    }
                    catch(Exception r)
                    {
                      System.out.println("Rollback failed ");    
                    }
                    
                    System.out.println("Error occured in process");
                    c.printStackTrace();
                    
                    try {
                        aFile.write("Error occured in process. Rolled back all the changes");
                        aFile.close();
                    }
                    catch(Exception f) {
                        
                    }
                    
                }
            }
        }.start();
    }
    
    
}
