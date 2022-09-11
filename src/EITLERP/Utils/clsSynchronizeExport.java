/*
 * clsSynchronizeExport.java
 *
 * Created on December 7, 2005, 11:56 AM
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


public class clsSynchronizeExport {
    
    private int SelCompanyID=0;
    private String FromYear="";
    private String SyncAction="1";
    private boolean UpdateFlags=false;
    private String ExportFile="";
    private HashMap Tables=new HashMap();
    private String startURL="";
    
    
    /** Creates a new instance of clsSynchronizeExport */
    public clsSynchronizeExport() {
    }
    
    /*
     Expected Parameters from Command Line
     0. File Name - to which export to
     1. Company ID
     2. Financial Year From
     3. Financial Year To
     4. Update Flags - Y/N
     5. Start URL of MySQL Database
     */
    
    public static void main(String args[]) {
        
        if(args.length<6) {
            System.out.println("Insufficient parameters.");
            return;
        }
        
        data.OpenGlobalConnection();
        
        clsSynchronizeExport objSync=new clsSynchronizeExport();
        
        objSync.ExportFile=args[0];
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
        
        objSync.StartExport();
    }
    
    
    private void StartExport() {
        
        
        BufferedWriter dataFile=null;
        
        try {
            
            System.out.println("Start Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            Connection tmpConn=null,srcConn=null;
            Statement stTmp=null,stSrc=null,stDel=null,stFlags=null;
            ResultSet rsTmp=null,rsSrc=null;
            ResultSetMetaData rsInfo;
            String Condition="";
            int ColType=0,RecCount=0,RecPointer=0;
            
            System.out.println("Starting .... ");
            
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
            stFlags=srcConn.createStatement();
            
            srcConn.setAutoCommit(false);
            
            //========= Open Text File ==========//
            dataFile=new BufferedWriter(new FileWriter(new File(ExportFile)));
            
            //====================== Gather Database Information ================//
            String[] names = {"TABLE"};
            HashMap Tables=new HashMap();
            int Counter=0;
            int PKCounter=0;
            ResultSet tableNames = dbInfo.getTables(null,"%", "%", names);
            
            System.out.println("Retrieving Database Information");
            
            tableNames.first();
            while (!tableNames.isAfterLast()) {
                Counter++;
                Tables.put(Integer.toString(Counter),tableNames.getString("TABLE_NAME"));
                String TableName=tableNames.getString("TABLE_NAME");
                
                System.out.println("Analyzing Table "+TableName);
                
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
                    rsTmp=data.getResult("SELECT "+Column+" FROM "+TableName+" LIMIT 1",EITLERPGLOBAL.DatabaseURL);
                    rsInfo=rsTmp.getMetaData();
                    
                    ObjPrimaryKey.ColumnType=rsInfo.getColumnType(1);
                    //=============================================//
                    
                    ObjTable.colPrimaryKeys.put(Integer.toString(PKCounter),ObjPrimaryKey);
                }
                
                Tables.put(TableName,ObjTable);
                
                
                
                String FieldList="";
                String ValueList="";
                
                //======= Now Generate Data =========/
                rsTmp=data.getResult("SELECT COUNT(*) AS RECCOUNT FROM "+TableName+" WHERE CHANGED=1 ",currentURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    
                    if(rsTmp.getLong("RECCOUNT")>0) {
                        
                        System.out.println("Writing Table : "+TableName);
                        
                        FieldList="";
                        ValueList="";
                        
                        //======== Write Field List =========//
                        rsTmp=data.getResult("SELECT * FROM "+TableName+" WHERE CHANGED=1 ",currentURL);
                        rsInfo=rsTmp.getMetaData();
                        
                        for(int f=1;f<=rsInfo.getColumnCount();f++) {
                            if(f==rsInfo.getColumnCount()) {
                                FieldList+=rsInfo.getColumnName(f).toUpperCase();
                            }
                            else {
                                FieldList+=rsInfo.getColumnName(f).toUpperCase()+",";
                            }
                            
                        }
                        FieldList="("+FieldList+")";
                    }
                }
                //====================================//
                
                
                                
                //======= Writing Record Values============//
                rsTmp=data.getResult("SELECT * FROM "+TableName+" WHERE CHANGED=1 ",currentURL);
                
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {

                        //==============Building Primary Key condition==================//
                        Condition="";
                        
                        //============= Building the Condition ===============//
                        for(int p=1;p<=ObjTable.colPrimaryKeys.size();p++) {
                            clsPrimaryKey ObjPrimaryKey=(clsPrimaryKey)ObjTable.colPrimaryKeys.get(Integer.toString(p));
                            
                            String PKName=ObjPrimaryKey.ColumnName;
                            ColType=ObjPrimaryKey.ColumnType;
                            
                            
                            //Now based on the type we have to retrieve the value of field
                            switch(ColType) {
                                case -5: //Long
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getLong(PKName);
                                    break;
                                case 4: //Integer,Small int
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getInt(PKName);
                                    break;
                                case 5: //Integer,Small int
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getInt(PKName);
                                    break;
                                case -6: //Integer,Small int
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getInt(PKName);
                                    break;
                                case 16: //Boolean
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getShort(PKName);
                                    break;
                                case 91: //Date
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getDate(PKName);
                                    break;
                                case 8: //Double
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getDouble(PKName);
                                    break;
                                case 6: //Float
                                    Condition=Condition+" AND "+PKName+"="+rsTmp.getFloat(PKName);
                                    break;
                                case 12 ://Varchar
                                    Condition=Condition+" AND "+PKName+"='"+rsTmp.getString(PKName)+"'";
                                    break;
                                case 1 ://char
                                    Condition=Condition+" AND "+PKName+"='"+rsTmp.getString(PKName)+"'";
                                    break;
                                default : //Varchar
                                    Condition=Condition+" AND "+PKName+"='"+rsTmp.getString(PKName)+"'";
                                    break;
                            } //Switch
                            
                        }
                        //=============== Building Condition Completed =====================//
                        
                        if(!Condition.trim().equals("")) {
                            Condition=" WHERE "+Condition.substring(5); //Removing 'AND' from begining of the statement
                        }
                        
                        
                        ValueList="";
                        rsInfo=rsTmp.getMetaData();
                        
                        for(int f=1;f<=rsInfo.getColumnCount();f++) {
                            if(f==rsInfo.getColumnCount()) {
                                               
                                int ColumnType=rsInfo.getColumnType(f);
                                

                                //Now based on the type we have to retrieve the value of field
                                switch(ColumnType) {
                                    case -5: //Long
                                        ValueList+=rsTmp.getLong(f);
                                        break;
                                    case 4: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f);
                                        break;
                                    case 5: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f);
                                        break;
                                    case -6: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f);
                                        break;
                                    case 16: //Boolean
                                        if(rsTmp.getBoolean(f)) {
                                            ValueList+="1";
                                        }
                                        else {
                                            ValueList+="0";
                                        }
                                        break;
                                    case 91: //Date
                                        ValueList+="'"+rsTmp.getString(f)+"'";
                                        break;
                                    case 8: //Double
                                        ValueList+=rsTmp.getDouble(f);
                                        break;
                                    case 6: //Float
                                        ValueList+=rsTmp.getDouble(f);
                                        break;
                                    case 12 ://Varchar
                                        ValueList+="'"+rsTmp.getString(f)+"'";
                                        break;
                                    case 1 ://char
                                        ValueList+="'"+rsTmp.getString(f)+"'";
                                        break;
                                    default : //Varchar
                                        ValueList+="'"+rsTmp.getString(f)+"'";
                                        break;
                                } //Switch
                            }
                            else {
                                int ColumnType=rsInfo.getColumnType(f);
                
                                //Now based on the type we have to retrieve the value of field
                                switch(ColumnType) {
                                    case -5: //Long
                                        ValueList+=rsTmp.getLong(f)+",";
                                        break;
                                    case 4: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f)+",";
                                        break;
                                    case 5: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f)+",";
                                        break;
                                    case -6: //Integer,Small int
                                        ValueList+=rsTmp.getInt(f)+",";
                                        break;
                                    case 16: //Boolean
                                        if(rsTmp.getBoolean(f)) {
                                            ValueList+="1";
                                        }
                                        else {
                                            ValueList+="0";
                                        }
                                        break;
                                    case 91: //Date
                                        ValueList+="'"+rsTmp.getString(f)+"',";
                                        break;
                                    case 8: //Double
                                        ValueList+=rsTmp.getDouble(f)+",";
                                        break;
                                    case 6: //Float
                                        ValueList+=rsTmp.getDouble(f)+",";
                                        break;
                                    case 12 ://Varchar
                                        ValueList+="'"+rsTmp.getString(f)+"',";
                                        break;
                                    case 1 ://char
                                        ValueList+="'"+rsTmp.getString(f)+"',";
                                        break;
                                    default : //Varchar
                                        ValueList+="'"+rsTmp.getString(f)+"',";
                                        break;
                                } //Switch
                            }
                            
                        }
                        
                        
                        
                        if(UpdateFlags)
                        {
                          stFlags.executeUpdate("UPDATE "+TableName.toUpperCase()+" SET CHANGED=1 "+Condition.toUpperCase());  
                        }
                        
                        dataFile.write("REPLACE INTO "+TableName.toUpperCase()+" "+FieldList+" VALUES ("+ValueList+");");
                        dataFile.newLine();
                        dataFile.write("UPDATE "+TableName.toUpperCase()+" SET CHANGED=0 "+Condition.toUpperCase()+";");
                        dataFile.newLine();
                        
                        rsTmp.next();
                    }
                }
                
                Tables.put(TableName,ObjTable);
                tableNames.next();
            }
            //=============== Done Gathering Database Information ==================//
            

            //Now Actually Update the data
            srcConn.commit();
            
            System.out.println("Done, Successfully.");
            System.out.println("End Time : "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
            
            dataFile.close();
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
    
    
}
