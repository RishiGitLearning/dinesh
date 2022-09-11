/*
 * SynchronizeR.java
 *
 * Created on April 27, 2016, 3:33 AM
 */

package EITLERP.Utils;

import EITLERP.*;
import EITLERP.EITLERPGLOBAL.*;
//import EITLERP.Stores.*;
//import EITLERP.Purchase.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import javax.sql.*;
import java.io.*;


/**
 *
 * @author  root
 */
public class SynchronizeR {
    
    private int SelCompanyID=1; //1for clone,2 sdml,3 sdmlank,4 rshop
    private String FromYear="2016";
    private String SyncAction="1";
    private boolean UpdateFlags=false;
    private int ModuleID=0;
    private static HashMap connStack=new HashMap();
    private HashMap Tables=new HashMap();
    public String BuildDate="Build 20 April 2016 3:22 PM";
    /** Creates a new instance of SynchronizeR */
    public SynchronizeR() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         try {
            if(args.length<5) {
                System.out.println("Insufficient Parameters");
                return;
            }
            
            data.OpenGlobalConnection();
            
            SynchronizeR objSync=new SynchronizeR();
            objSync.SelCompanyID=Integer.parseInt(args[0]);
            objSync.FromYear=args[1];
            objSync.SyncAction=args[3];
            
            if(args[4].equals("Y")) {
                objSync.UpdateFlags=true;
            } else {
                objSync.UpdateFlags=false;
            }
            
            if(args.length>5) {
                objSync.ModuleID=Integer.parseInt(args[5]);
                
            }
            // TEST START
                /*objSync.SelCompanyID=3;
                objSync.FromYear="2013";
                objSync.SyncAction="1";
                objSync.UpdateFlags=true;
                objSync.ModuleID=59;*/
            // TEST END            
            if(args[3].equals("1")) {
                objSync.SyncDataFromSource(); // rshop to sdmlbaroda
            }
            
            if(args[3].equals("2")) {
                objSync.SyncDataToSource();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Connection getConn(String pURL) {        
        Connection Conn;        
        try {
            
            if(connStack.containsKey(pURL)) {
                Conn=(Connection)connStack.get(pURL);
            } else {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.setLoginTimeout(1000);
                //Conn=DriverManager.getConnection(pURL+EITLERPGLOBAL.DBUserName);
                //System.out.println(pURL+"?user=root;password=rs@108");
                //Conn=DriverManager.getConnection(pURL+EITLERPGLOBAL.DBUserName,EITLERPGLOBAL.DBUserName,"rs@108");
                //Conn=DriverManager.getConnection(pURL+"?user=root;password=rs@108");
                Conn=DriverManager.getConnection(pURL,"root","rs@221");
                connStack.put(pURL, Conn);
            }
            return Conn;
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Failed connection "+pURL+EITLERPGLOBAL.DBUserName);
            Conn=null;
            return Conn;
        }
    }
    
    private void SyncDataFromSource() {
        
        HashMap Tables=new HashMap();
        
        //new Thread(){
          //  public void run() {
                BufferedWriter aFile=null;
                
                Connection srcConn=null,destConn=null;
                Connection srcConnTable=null,destConnTable=null;
                
                //===========ResultSet Section============//
                ResultSet rsModules=null;
                Statement stModules=null;
                
                ResultSet rsChild=null;
                Statement stChild=null;
                
                ResultSet rsMain=null;
                Statement stMain=null;
                
                ResultSet rsTmp=null;
                Statement stTmp=null;
                
                ResultSet rsUpdate=null;
                Statement stUpdate=null;
                
                Statement stSource=null;
                Statement stDest=null;
                
                Statement stFields=null;
                
                DatabaseMetaData dbInfo=null;
                DatabaseMetaData dbInfoTable=null;
                ResultSetMetaData rsInfo=null;
                ResultSetMetaData rsInfoTable=null;
                //=======================================//
                
                
                HashMap HeaderFieldValues=new HashMap();
                HashMap MainFieldValues=new HashMap();
                
                String strSQL="";
                boolean SourceFlag=false;
                boolean DestFlag=false;
                java.sql.Date SourceDate=null;
                java.sql.Date DestDate=null;
                boolean ContinueUpdate=false;
                boolean HasDifferentDB=false;
                boolean ModuleHasDifferentDB=false;
                boolean HasDifferentDBChild=false;
                
                try {
                    
                    EITLERPGLOBAL.DBUserName="";
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    
                    String currentURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear);
                    String srcURL=clsFinYear.getSyncURL((int)nCompanyID,tmpFromYear);
                    
                    String currentURLTable=currentURL;
                    String srcURLTable=srcURL;
                    //===============================================================//
                    
                    
                    //=======Open the Source and Destination Connections ==========//
                    System.out.println(BuildDate);
                    System.out.println("");
                    System.out.println("Synchronizing Data from Source ");
                    System.out.println("Source Connection "+srcURL);
                    System.out.println("Destination Connection "+currentURL);
                    
                    //srcConn=data.getConn(srcURL);
                    srcConn=getConn(srcURL);
                    srcConn.setAutoCommit(true);
                    srcConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                    
                    System.out.println("Got the connection of Source");
                    
                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    stSource.setQueryTimeout(1000);
                    dbInfo=srcConn.getMetaData();
                    
                    destConn=data.getConn(currentURL);
                    System.out.println("Got the connection of destination");
                    destConn.setAutoCommit(true);
                    destConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                    
                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    stDest.setQueryTimeout(1000);
                    
                    //============================================================//
                    
                    System.out.println("Querying for Modules ... ");
                    
                    //======== Now Fetch Modules =============//
                    stModules=srcConn.createStatement();
                    
                    if(ModuleID==0) {
                        rsModules=stModules.executeQuery("SELECT * FROM D_COM_SYNC_MODULES");
                    }
                    else {
                        rsModules=stModules.executeQuery("SELECT * FROM D_COM_SYNC_MODULES WHERE MODULE_ID="+ModuleID);
                    }
                    rsModules.first();
                    
                    if(rsModules.getRow()>0) {
                        
                        rsModules.first();
                        
                        while(!rsModules.isAfterLast()) {
                            String ModuleName=rsModules.getString("MODULE_NAME");
                            HasDifferentDB=false;
                            ModuleHasDifferentDB=false;
                            HasDifferentDBChild=false;
                            
                            System.out.println("Module : "+ModuleName);
                            
                            try {
                                
                                boolean HasChilds=rsModules.getBoolean("HAS_CHILDS");
                                String MainTableName=rsModules.getString("MAIN_TABLE");
                                String mainCondition=rsModules.getString("ADDITIONAL_CONDITION");
                                
                                currentURLTable=rsModules.getString("DB_URL");
                                srcURLTable=rsModules.getString("SYNC_URL");
                                
                                
                                //*** Check Whether the table is in the Main database or in separate database **//
                                if(!currentURLTable.trim().equals("")) {
                                    //It has separate database
                                    HasDifferentDB=true;
                                    ModuleHasDifferentDB=true;
                                    
                                    System.out.println("Switching to destination "+currentURLTable);
                                    
                                    //srcConnTable=data.getConn(srcURLTable);
                                    srcConnTable=getConn(srcURLTable);
                                    srcConnTable.setAutoCommit(true);
                                    srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stSource.setQueryTimeout(1000);
                                    dbInfo=srcConnTable.getMetaData();
                                    
                                    destConnTable=data.getConn(currentURLTable);
                                    destConnTable.setAutoCommit(true);
                                    destConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stDest.setQueryTimeout(1000);
                                    
                                }
                                else {
                                    
                                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stSource.setQueryTimeout(1000);
                                    dbInfo=srcConn.getMetaData();
                                    
                                    System.out.println("Switching to destination "+currentURL);
                                    
                                    destConn=data.getConn(currentURL);
                                    destConn.setAutoCommit(true);
                                    destConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stDest.setQueryTimeout(1000);
                                    
                                }
                                //*** ----------------------------------------------------------------------- **//
                                
                                int ModuleSrNo=rsModules.getInt("SR_NO");
                                HeaderFieldValues=new HashMap();
                                
                                if(!mainCondition.trim().equals("")) {
                                    mainCondition=" AND "+mainCondition;
                                }
                                
                                HeaderFieldValues.clear();
                                
                                //Get the Primary Key Information for this table
                                ResultSet rsPrimary=dbInfo.getPrimaryKeys(null,null, MainTableName);
                                
                                while(rsPrimary.next()) {
                                    
                                    String Column=rsPrimary.getString("COLUMN_NAME");
                                    
                                    clsPrimaryKey ObjPrimaryKey=new clsPrimaryKey();
                                    
                                    ObjPrimaryKey.ColumnName=Column;
                                    
                                    
                                    //=========== Get the Primary Key Datatype =====//
                                    if(HasDifferentDB) {
                                        stTmp=srcConnTable.createStatement();
                                    }
                                    else {
                                        stTmp=srcConn.createStatement();
                                    }
                                    rsTmp=stTmp.executeQuery("SELECT "+Column+" FROM "+MainTableName+" LIMIT 1");
                                    rsInfo=rsTmp.getMetaData();
                                    ObjPrimaryKey.ColumnType=rsInfo.getColumnType(1);
                                    //=============================================//
                                    
                                    
                                    HeaderFieldValues.put(Column,ObjPrimaryKey);
                                }
                                
                                
                                System.out.println("Fetching changed records ");
                                
                                //Get the changed records from Main Table
                                if(HasDifferentDB) {
                                    stMain=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                }
                                else {
                                    stMain=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                }
                                
                                rsMain=stMain.executeQuery("SELECT * FROM "+MainTableName+" WHERE CHANGED=1 "+mainCondition);
                                rsMain.first();
                                if(rsMain.getRow()>0) {
                                    while(!rsMain.isAfterLast()) {
                                        try {
                                            System.out.println("Processing row "+rsMain.getRow());
                                            
                                            SourceFlag=rsMain.getBoolean("CHANGED");
                                            SourceDate=rsMain.getDate("CHANGED_DATE");
                                            
                                            MainFieldValues=new HashMap();
                                            
                                            MainFieldValues.clear();
                                            
                                            
                                            //ChangeHere
                                            if(HasDifferentDB) {
                                                stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                stSource.setQueryTimeout(1000);
                                                dbInfo=srcConnTable.getMetaData();
                                                
                                                stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                stDest.setQueryTimeout(1000);
                                                
                                            }
                                            
                                            
                                            //================================================//
                                            //==== Pass Header Field through Check ===========//
                                            //================================================//
                                            rsInfo=rsMain.getMetaData();
                                            
                                            for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                
                                                String FieldName=rsInfo.getColumnName(c);
                                                //Pass it through Interceptor class
                                                Variant objNewValue;
                                                if(HasDifferentDB) {
                                                    objNewValue=clsCheckRecord.checkRecord(destConnTable, MainTableName, rsMain, FieldName);
                                                }
                                                else {
                                                    objNewValue=clsCheckRecord.checkRecord(destConn, MainTableName, rsMain, FieldName);
                                                }
                                                
                                                clsPrimaryKey objField=new clsPrimaryKey();
                                                objField.ColumnName=rsInfo.getColumnName(c);
                                                objField.ColumnType=rsInfo.getColumnType(c);
                                                
                                                String Column=rsInfo.getColumnName(c);
                                                
                                                
                                                if(objNewValue!=null) //If any updation has been made
                                                {
                                                    
                                                    switch(rsInfo.getColumnType(c)) {
                                                        case -5: //Long
                                                            rsMain.updateLong(FieldName,(long)objNewValue.getVal());
                                                            break;
                                                        case 4: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case 5: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case -6: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case 16: //Boolean
                                                            rsMain.updateBoolean(FieldName,objNewValue.getBool());
                                                            break;
                                                        case 91: //Date
                                                            rsMain.updateDate(FieldName,java.sql.Date.valueOf((String)objNewValue.getObj()));
                                                            break;
                                                        case 8: //Double
                                                            rsMain.updateDouble(FieldName,objNewValue.getVal());
                                                            break;
                                                        case 6: //Float
                                                            rsMain.updateDouble(FieldName,objNewValue.getVal());
                                                            break;
                                                        case 12 ://Varchar
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                        case 1 ://char
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                        default : //Varchar
                                                            
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                    } //Switch
                                                    
                                                }
                                                
                                                //Fill up the column values
                                                switch(rsInfo.getColumnType(c)) {
                                                    case -5: //Long
                                                        objField.ColumnValue=new Variant(rsMain.getLong(FieldName));
                                                        break;
                                                    case 4: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case 5: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case -6: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case 16: //Boolean
                                                        objField.ColumnValue=new Variant(rsMain.getBoolean(FieldName));
                                                        break;
                                                    case 91: //Date
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    case 8: //Double
                                                        objField.ColumnValue=new Variant(rsMain.getDouble(FieldName));
                                                        break;
                                                    case 6: //Float
                                                        objField.ColumnValue=new Variant(rsMain.getDouble(FieldName));
                                                        break;
                                                    case 12 ://Varchar
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    case 1 ://char
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    default : //Varchar
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                } //Switch
                                                
                                                
                                                MainFieldValues.put(Column,objField);
                                            }
                                            //=================================================================//
                                            
                                            
                                            
                                            
                                            //========== Fill Up Primary Key values ============//
                                            String PKCondition="";
                                            
                                            Iterator iPK=HeaderFieldValues.keySet().iterator();
                                            
                                            while(iPK.hasNext()) {
                                                clsPrimaryKey objPK=(clsPrimaryKey)HeaderFieldValues.get(iPK.next());
                                                String Column=objPK.ColumnName;
                                                
                                                //====== Fill up the value of Header Field with the values of Main Table =====//
                                                switch(objPK.ColumnType) {
                                                    case -5: //Long
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getLong(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getLong(Column));
                                                        break;
                                                    case 4: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case 5: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case -6: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case 16: //Boolean
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getBoolean(Column));
                                                        break;
                                                    case 91: //Date
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getDate(Column));
                                                        break;
                                                    case 8: //Double
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getDouble(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getDouble(Column));
                                                        break;
                                                    case 6: //Float
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getDouble(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getDouble(Column));
                                                        break;
                                                    case 12 ://Varchar
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                    case 1 ://char
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                    default : //Varchar
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                } //Switch
                                                
                                                HeaderFieldValues.put(Column,objPK);
                                            }
                                            //=========== Filling of Primary Key Values completed ==================//
                                            
                                            
                                            
                                            
                                            //=============== Check whether recod exist in destination database ============//
                                            // Policy for overwrite
                                            // If Record is not changed at destination, simply overwrite it.
                                            // If Record is changed at destination, check for the updation date and overwrite if current date is latest than source
                                            // If Record is changed at destination and both the dates are equal, simply overwrite it.
                                            
                                            ContinueUpdate=false;
                                            
                                            if(!PKCondition.trim().equals("")) {
                                                PKCondition=" WHERE "+PKCondition.substring(4);
                                            }
                                            
                                            System.out.println("Checking for record existance ");
                                            
                                            if(HasDifferentDB) {
                                                stTmp=destConnTable.createStatement();
                                            }
                                            else {
                                                stTmp=destConn.createStatement();
                                            }
                                            
                                            System.out.println("SELECT CHANGED,CHANGED_DATE FROM "+MainTableName+" "+PKCondition);
                                            rsTmp=stTmp.executeQuery("SELECT CHANGED,CHANGED_DATE FROM "+MainTableName+" "+PKCondition);
                                            rsTmp.first();
                                            
                                            if(rsTmp.getRow()>0) {
                                                DestFlag=rsTmp.getBoolean("CHANGED");
                                                DestDate=rsTmp.getDate("CHANGED_DATE");
                                                
                                                if(DestFlag==true) //Same record at destination has changed.
                                                {
                                                    if(SourceDate.after(DestDate)) //Destination record is Latest
                                                    {
                                                        //Continue overwriting
                                                        ContinueUpdate=true;
                                                    }
                                                    else {
                                                        if(SourceDate.compareTo(DestDate)==0) //Both dates are equal
                                                        {
                                                            // Continue Overwriting
                                                            ContinueUpdate=true;
                                                        }
                                                        else {
                                                            
                                                        }
                                                    }
                                                    
                                                }
                                                else {
                                                    //No Problem continue overwriting
                                                    ContinueUpdate=true;
                                                }
                                                
                                            }
                                            else {
                                                //Continue update if not exist
                                                ContinueUpdate=true;
                                            }
                                            //==============================================================================//
                                            
                                            
                                            
                                            //==================Physical Writing of Records =====================//
                                            //===================================================================//
                                            if(ContinueUpdate) {
                                                
                                                System.out.println("Inserting/Overwriting Row ... ");
                                                
                                                //First Delete the record from Destination
                                                strSQL="DELETE FROM "+MainTableName+" "+PKCondition;
                                                stDest.executeUpdate(strSQL);
                                                
                                                // Prepare Recordset //
                                                if(HasDifferentDB) {
                                                    stUpdate=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                }
                                                else {
                                                    stUpdate=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                }
                                                rsUpdate=stUpdate.executeQuery("SELECT * FROM "+MainTableName+" LIMIT 1");
                                                
                                                
                                                try {
                                                    rsUpdate.first();
                                                    rsUpdate.moveToInsertRow();
                                                    
                                                    for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                        
                                                        String FieldName=rsInfo.getColumnName(c);
                                                        
                                                        switch(rsInfo.getColumnType(c)) {
                                                            case -5: //Long
                                                                rsUpdate.updateLong(FieldName,rsMain.getLong(FieldName));
                                                                break;
                                                            case 4: //Integer,Small int
                                                                rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                                break;
                                                            case 5: //Integer,Small int
                                                                rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                                break;
                                                            case -6: //Integer,Small int
                                                                rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                                break;
                                                            case 16: //Boolean
                                                                rsUpdate.updateBoolean(FieldName,rsMain.getBoolean(FieldName));
                                                                break;
                                                            case 91: //Date
                                                                rsUpdate.updateDate(FieldName,rsMain.getDate(FieldName));
                                                                break;
                                                            case 8: //Double
                                                                rsUpdate.updateDouble(FieldName,rsMain.getDouble(FieldName));
                                                                break;
                                                            case 6: //Float
                                                                rsUpdate.updateFloat(FieldName,rsMain.getFloat(FieldName));
                                                                break;
                                                            case 12 ://Varchar
                                                                rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                                break;
                                                            case 1 ://char
                                                                rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                                break;
                                                            default : //Varchar
                                                                rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                                break;
                                                        } //Switch
                                                    }
                                                    
                                                    rsUpdate.updateBoolean("CHANGED", false);
                                                    rsUpdate.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                                    rsUpdate.insertRow();
                                                }
                                                catch(Exception mainUpdate) {
                                                    mainUpdate.printStackTrace();
                                                }
                                                
                                              //new updation start  
                                            /*if(UpdateFlags) {
                                                System.out.println("Updating flag ");
                                                stSource.executeUpdate("UPDATE "+MainTableName+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+PKCondition);
                                            }*/
                                              //new updation stop  
                                                //======Main table row inserted =======//
                                                
                                                
                                                
                                                
                                                //====Handling records of Childs =========//
                                                if(HasChilds) {
                                                    System.out.println("Querying for child tables ... ");
                                                    
                                                    stChild=srcConn.createStatement();
                                                    rsChild=stChild.executeQuery("SELECT * FROM D_COM_SYNC_CHILDS WHERE HEADER_SR_NO="+ModuleSrNo);
                                                    rsChild.first();
                                                    
                                                    if(rsChild.getRow()>0) {
                                                        while(!rsChild.isAfterLast()) {
                                                            
                                                            HasDifferentDBChild=false;
                                                            
                                                            String ChildTable=rsChild.getString("TABLE_NAME");
                                                            
                                                            System.out.println("Processing child tables : "+ChildTable);
                                                            
                                                            String addCondition=rsChild.getString("ADDITIONAL_CONDITION");
                                                            currentURLTable=rsChild.getString("DB_URL");
                                                            srcURLTable=rsChild.getString("SYNC_URL");
                                                            
                                                            
                                                            //*** Check Whether the table is in the Main database or in separate database **//
                                                            if(!currentURLTable.trim().equals("")) {
                                                                
                                                                //It has separate database
                                                                HasDifferentDBChild=true;
                                                                
                                                                
                                                                System.out.println("Switching to destination "+currentURLTable);
                                                                
                                                                //srcConnTable=data.getConn(srcURLTable);
                                                                srcConnTable=getConn(srcURLTable);
                                                                srcConnTable.setAutoCommit(true);
                                                                srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                                
                                                                
                                                                stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stSource.setQueryTimeout(1000);
                                                                dbInfo=srcConnTable.getMetaData();
                                                                
                                                                destConnTable=data.getConn(currentURLTable);
                                                                destConnTable.setAutoCommit(true);
                                                                destConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                                
                                                                stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stDest.setQueryTimeout(1000);
                                                            }
                                                            else {
                                                                
                                                                System.out.println("Switching to destination "+currentURL);
                                                                
                                                                stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stSource.setQueryTimeout(1000);
                                                                dbInfo=srcConn.getMetaData();
                                                                
                                                                stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stDest.setQueryTimeout(1000);
                                                                
                                                            }
                                                            //*** ----------------------------------------------------------------------- **//
                                                            
                                                            int ChildSrNo=rsChild.getInt("SR_NO");
                                                            String Condition="";
                                                            
                                                            //=======Building Condition to fetch child records =======//
                                                            stFields=srcConn.createStatement();
                                                            ResultSet rsFields=stFields.executeQuery("SELECT * FROM D_COM_SYNC_FIELDS WHERE HEADER_SR_NO="+ModuleSrNo+" AND CHILD_SR_NO="+ChildSrNo);
                                                            rsFields.first();
                                                            System.out.println(rsFields.getRow());
                                                            if(rsFields.getRow()>0) {
                                                                while(!rsFields.isAfterLast()) {
                                                                    String HeaderFieldName=rsFields.getString("HEADER_FIELD");
                                                                    String ChildFieldName=rsFields.getString("CHILD_FIELD");
                                                                    int HeaderFieldType=rsFields.getInt("HEADER_FIELD_TYPE");
                                                                    int ChildFieldType=rsFields.getInt("CHILD_FIELD_TYPE");
                                                                    
                                                                    Condition=Condition+" AND "+ChildFieldName+"= ";
                                                                    
                                                                    
                                                                    if(HeaderFieldType!=ChildFieldType) {
                                                                        //Header is Numeric and Child is String
                                                                        if(HeaderFieldType==1&&ChildFieldType==2) {
                                                                            Condition=Condition+" '"+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal()+"' ";
                                                                        }
                                                                        
                                                                        //Header is string and child is numeric
                                                                        if(HeaderFieldType==2&&ChildFieldType==1) {
                                                                            Condition=Condition+" "+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+" ";
                                                                        }
                                                                        
                                                                        //Both are Numeric
                                                                        if(HeaderFieldType==1&&ChildFieldType==1) {
                                                                            Condition=Condition+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal();
                                                                        }
                                                                        
                                                                        
                                                                        //Both are String
                                                                        if(HeaderFieldType==2&&ChildFieldType==2) {
                                                                            Condition=Condition+" '"+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+"' ";
                                                                        }
                                                                        
                                                                    }
                                                                    else {
                                                                        switch(HeaderFieldType) {
                                                                            case 1: //Numeric
                                                                                Condition=Condition+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal();
                                                                                break;
                                                                            case 2: //String
                                                                                Condition=Condition+" '"+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+"' ";
                                                                                break;
                                                                        }
                                                                    }
                                                                    
                                                                    rsFields.next();
                                                                }
                                                            }
                                                            //========================================================//
                                                            
                                                            
                                                            if(!Condition.trim().equals("")) {
                                                                if(!addCondition.trim().equals("")) {
                                                                    Condition=" WHERE "+Condition.substring(4)+" AND "+addCondition;
                                                                }
                                                                else {
                                                                    Condition=" WHERE "+Condition.substring(4)+" ";
                                                                }
                                                            }
                                                            else {
                                                                Condition=" WHERE "+addCondition;
                                                            }
                                                            
                                                            
                                                            // ======  Fetch Child Records ==============//
                                                            strSQL=" DELETE FROM "+ChildTable+" "+Condition;
                                                            stDest.executeUpdate(strSQL);
                                                            
                                                            strSQL=" SELECT * FROM "+ChildTable+" "+Condition;
                                                            
                                                            System.out.println("Querying for child records ");
                                                            System.out.println(strSQL);
                                                            if(HasDifferentDBChild) {
                                                                stTmp=srcConnTable.createStatement();
                                                            }
                                                            else {
                                                                stTmp=srcConn.createStatement();
                                                            }
                                                            rsTmp=stTmp.executeQuery(strSQL);
                                                            rsTmp.first();
                                                            
                                                            if(rsTmp.getRow()>0) {
                                                                rsInfo=rsTmp.getMetaData();
                                                                
                                                                if(HasDifferentDBChild) {
                                                                    stUpdate=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                }
                                                                else {
                                                                    stUpdate=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                }
                                                                rsUpdate=stUpdate.executeQuery("SELECT * FROM "+ChildTable+" LIMIT 1");
                                                                rsUpdate.first();
                                                                
                                                                while(!rsTmp.isAfterLast()) {
                                                                    
                                                                    System.out.println("Processing row # "+rsTmp.getRow()+" of "+ChildTable);
                                                                    
                                                                    rsUpdate.moveToInsertRow();
                                                                    
                                                                    for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                                        String FieldName=rsInfo.getColumnName(c);
                                                                        
                                                                        switch(rsInfo.getColumnType(c)) {
                                                                            case -5: //Long
                                                                                rsUpdate.updateLong(FieldName,rsTmp.getLong(FieldName));
                                                                                break;
                                                                            case 4: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case 5: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case -6: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case 16: //Boolean
                                                                                rsUpdate.updateBoolean(FieldName,rsTmp.getBoolean(FieldName));
                                                                                break;
                                                                            case 91: //Date
                                                                                rsUpdate.updateDate(FieldName,rsTmp.getDate(FieldName));
                                                                                break;
                                                                            case 8: //Double
                                                                                rsUpdate.updateDouble(FieldName,rsTmp.getDouble(FieldName));
                                                                                break;
                                                                            case 6: //Float
                                                                                rsUpdate.updateFloat(FieldName,rsTmp.getFloat(FieldName));
                                                                                break;
                                                                            case 12 ://Varchar
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                            case 1 ://char
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                            default : //Varchar
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                        } //Switch
                                                                    }
                                                                    
                                                                    rsUpdate.updateBoolean("CHANGED",false);
                                                                    rsUpdate.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                                                    rsUpdate.insertRow();
                                                                    
                                                                    if(UpdateFlags) {
                                                                        stSource.executeUpdate("UPDATE "+ChildTable+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+Condition);
                                                                    }
                                                                    
                                                                    rsTmp.next();
                                                                }
                                                                
                                                                System.out.println("Done. Child Records ");
                                                            }
                                                            //=====Insert child records ==============//
                                                            
                                                            
                                                            rsChild.next();
                                                        }
                                                    }
                                                }
                                                
                                            }
                                            //=============== End of Updating Records ======================//
                                            
                                            if(UpdateFlags) {
                                                System.out.println("Updating flag ");
                                                
                                                if(ModuleHasDifferentDB) {
                                                    
                                                    currentURLTable=rsModules.getString("DB_URL");
                                                    srcURLTable=rsModules.getString("SYNC_URL");
                                                    
                                                    System.out.println("Switching to destination "+currentURLTable);
                                                    
                                                    //srcConnTable=data.getConn(srcURLTable);
                                                    srcConnTable=getConn(srcURLTable);
                                                    srcConnTable.setAutoCommit(true);
                                                    srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                    
                                                    
                                                    
                                                    stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stSource.setQueryTimeout(1000);
                                                    
                                                }
                                                else {
                                                    
                                                    System.out.println("Switching to destination "+currentURL);
                                                    
                                                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stSource.setQueryTimeout(1000);
                                                    dbInfo=srcConn.getMetaData();
                                                    
                                                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stDest.setQueryTimeout(1000);
                                                    
                                                }
                                                
                                                stSource.executeUpdate("UPDATE "+MainTableName+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+PKCondition);
                                            }
                                            
                                            
                                            
                                            
                                            System.out.println("Commiting module "+rsModules.getString("MODULE_NAME"));
                                            //srcConn.commit();
                                            //destConn.commit();
                                            
                                            try {
                                                //srcConnTable.commit();
                                                //destConnTable.commit();
                                            }
                                            catch(Exception ConnE){
                                                ConnE.printStackTrace();
                                            }
                                        }catch(Exception rsMainE) {
                                            rsMainE.printStackTrace();
                                        }
                                        rsMain.next();
                                    }
                                }
                                
                                //Commit at module level
                                System.out.println("Commiting Record ");
                                //srcConn.commit();
                                //destConn.commit();
                                
                                try {
                                    //srcConnTable.commit();
                                    //destConnTable.commit();
                                }
                                catch(Exception ConnE){}
                                
                            }
                            catch(Exception mod) {
                                mod.printStackTrace();
                                //srcConn.rollback();
                                //destConn.rollback();
                                
                            }
                            rsModules.next();
                        }
                    }
                    //========================================//
                    
                    
                    System.out.println("Now commiting changes ... ");
                    //srcConn.commit();
                    //destConn.commit();
                    System.out.println("Changes commited successfully.");
                    System.out.println("Process completed successfully");
                    
                    System.out.println("0");
                    
                }
                catch(Exception e) {
                    
                    System.out.println("Process failed. ");
                    try {
                        //srcConn.rollback();
                        //destConn.rollback();
                        System.out.println("Rollback succeeded");
                    }
                    catch(Exception d) {
                        System.out.println("Rollback failed");
                        System.exit(1);
                    }
                    
                    e.printStackTrace();
                    System.out.println("1");
                }
            //}
        //}.start();
    }
    
    private void SyncDataToSource() {
        
        HashMap Tables=new HashMap();
        
        new Thread(){
            public void run() {
                BufferedWriter aFile=null;
                
                Connection srcConn=null,destConn=null;
                Connection srcConnTable=null,destConnTable=null;
                
                //===========ResultSet Section============//
                ResultSet rsModules=null;
                Statement stModules=null;
                
                ResultSet rsChild=null;
                Statement stChild=null;
                
                ResultSet rsMain=null;
                Statement stMain=null;
                
                ResultSet rsTmp=null;
                Statement stTmp=null;
                
                ResultSet rsUpdate=null;
                Statement stUpdate=null;
                
                Statement stSource=null;
                Statement stDest=null;
                
                Statement stFields=null;
                
                DatabaseMetaData dbInfo=null;
                DatabaseMetaData dbInfoTable=null;
                ResultSetMetaData rsInfo=null;
                ResultSetMetaData rsInfoTable=null;
                //=======================================//
                
                
                HashMap HeaderFieldValues=new HashMap();
                HashMap MainFieldValues=new HashMap();
                
                String strSQL="";
                boolean SourceFlag=false;
                boolean DestFlag=false;
                java.sql.Date SourceDate=null;
                java.sql.Date DestDate=null;
                boolean ContinueUpdate=false;
                boolean HasDifferentDB=false;
                boolean ModuleHasDifferentDB=false;
                boolean HasDifferentDBChild=false;
                
                try {
                    
                    EITLERPGLOBAL.DBUserName="";
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    
                    String srcURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear);
                    String currentURL=clsFinYear.getSyncURL((int)nCompanyID,tmpFromYear);
                    String currentURLTable=currentURL;
                    String srcURLTable=srcURL;
                    //===============================================================//
                    
                    
                    //=======Open the Source and Destination Connections ==========//
                    System.out.println(BuildDate);
                    System.out.println("");
                    System.out.println("Synchronizing Data from Source ");
                    System.out.println("Source Connection "+srcURL);
                    System.out.println("Destination Connection "+currentURL);
                    
                    srcConn=data.getConn(srcURL);
                    srcConn.setAutoCommit(true);
                    srcConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                    
                    System.out.println("Got the connection of Source");
                    
                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    stSource.setQueryTimeout(1000);
                    dbInfo=srcConn.getMetaData();
                    
                    destConn=data.getConn(currentURL);
                    System.out.println("Got the connection of destination");
                    destConn.setAutoCommit(true);
                    destConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                    
                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    stDest.setQueryTimeout(1000);
                    
                    //============================================================//
                    
                    System.out.println("Querying for Modules ... ");
                    
                    //======== Now Fetch Modules =============//
                    stModules=srcConn.createStatement();
                    
                    if(ModuleID==0) {
                        rsModules=stModules.executeQuery("SELECT * FROM D_COM_SYNC_MODULES");
                    }
                    else {
                        rsModules=stModules.executeQuery("SELECT * FROM D_COM_SYNC_MODULES WHERE MODULE_ID="+ModuleID);
                    }
                    rsModules.first();
                    
                    if(rsModules.getRow()>0) {
                        
                        rsModules.first();
                        
                        while(!rsModules.isAfterLast()) {
                            String ModuleName=rsModules.getString("MODULE_NAME");
                            HasDifferentDB=false;
                            ModuleHasDifferentDB=false;
                            HasDifferentDBChild=false;
                            
                            System.out.println("Module : "+ModuleName);
                            
                            try {
                                
                                boolean HasChilds=rsModules.getBoolean("HAS_CHILDS");
                                String MainTableName=rsModules.getString("MAIN_TABLE");
                                String mainCondition=rsModules.getString("ADDITIONAL_CONDITION");
                                
                                srcURLTable=rsModules.getString("DB_URL");
                                currentURLTable=rsModules.getString("SYNC_URL");
                                
                                
                                //*** Check Whether the table is in the Main database or in separate database **//
                                if(!currentURLTable.trim().equals("")) {
                                    //It has separate database
                                    HasDifferentDB=true;
                                    ModuleHasDifferentDB=true;
                                    
                                    System.out.println("Switching to destination "+currentURLTable);
                                    
                                    srcConnTable=data.getConn(srcURLTable);
                                    srcConnTable.setAutoCommit(true);
                                    srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    
                                    stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stSource.setQueryTimeout(1000);
                                    dbInfo=srcConnTable.getMetaData();
                                    
                                    destConnTable=data.getConn(currentURLTable);
                                    destConnTable.setAutoCommit(true);
                                    destConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stDest.setQueryTimeout(1000);
                                    
                                }
                                else {
                                    
                                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stSource.setQueryTimeout(1000);
                                    dbInfo=srcConn.getMetaData();
                                    
                                    System.out.println("Switching to destination "+currentURL);
                                    
                                    destConn=data.getConn(currentURL);
                                    destConn.setAutoCommit(true);
                                    destConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                    
                                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                    stDest.setQueryTimeout(1000);
                                    
                                }
                                //*** ----------------------------------------------------------------------- **//
                                
                                int ModuleSrNo=rsModules.getInt("SR_NO");
                                HeaderFieldValues=new HashMap();
                                
                                if(!mainCondition.trim().equals("")) {
                                    mainCondition=" AND "+mainCondition;
                                }
                                
                                HeaderFieldValues.clear();
                                
                                //Get the Primary Key Information for this table
                                ResultSet rsPrimary=dbInfo.getPrimaryKeys(null,null, MainTableName);
                                
                                while(rsPrimary.next()) {
                                    
                                    String Column=rsPrimary.getString("COLUMN_NAME");
                                    
                                    clsPrimaryKey ObjPrimaryKey=new clsPrimaryKey();
                                    
                                    ObjPrimaryKey.ColumnName=Column;
                                    
                                    
                                    //=========== Get the Primary Key Datatype =====//
                                    if(HasDifferentDB) {
                                        stTmp=srcConnTable.createStatement();
                                    }
                                    else {
                                        stTmp=srcConn.createStatement();
                                    }
                                    rsTmp=stTmp.executeQuery("SELECT "+Column+" FROM "+MainTableName+" LIMIT 1");
                                    rsInfo=rsTmp.getMetaData();
                                    ObjPrimaryKey.ColumnType=rsInfo.getColumnType(1);
                                    //=============================================//
                                    
                                    
                                    HeaderFieldValues.put(Column,ObjPrimaryKey);
                                }
                                
                                
                                System.out.println("Fetching changed records ");
                                
                                //Get the changed records from Main Table
                                if(HasDifferentDB) {
                                    stMain=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                }
                                else {
                                    stMain=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                }
                                
                                rsMain=stMain.executeQuery("SELECT * FROM "+MainTableName+" WHERE CHANGED=1 "+mainCondition);
                                rsMain.first();
                                if(rsMain.getRow()>0) {
                                    while(!rsMain.isAfterLast()) {
                                        
                                        try {
                                            
                                            
                                            System.out.println("Processing row of Main Table "+rsMain.getRow());
                                            
                                            SourceFlag=rsMain.getBoolean("CHANGED");
                                            SourceDate=rsMain.getDate("CHANGED_DATE");
                                            
                                            MainFieldValues=new HashMap();
                                            
                                            MainFieldValues.clear();
                                            
                                            
                                            //ChangeHere
                                            if(HasDifferentDB) {
                                                stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                stSource.setQueryTimeout(1000);
                                                dbInfo=srcConnTable.getMetaData();
                                                
                                                stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                stDest.setQueryTimeout(1000);
                                                
                                            }
                                            
                                            
                                            //================================================//
                                            //==== Pass Header Field through Check ===========//
                                            //================================================//
                                            rsInfo=rsMain.getMetaData();
                                            
                                            for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                
                                                String FieldName=rsInfo.getColumnName(c);
                                                //Pass it through Interceptor class
                                                Variant objNewValue;
                                                if(HasDifferentDB) {
                                                    objNewValue=clsCheckRecord.checkRecord(destConnTable, MainTableName, rsMain, FieldName);
                                                }
                                                else {
                                                    objNewValue=clsCheckRecord.checkRecord(destConn, MainTableName, rsMain, FieldName);
                                                }
                                                
                                                clsPrimaryKey objField=new clsPrimaryKey();
                                                objField.ColumnName=rsInfo.getColumnName(c);
                                                objField.ColumnType=rsInfo.getColumnType(c);
                                                
                                                String Column=rsInfo.getColumnName(c);
                                                
                                                
                                                if(objNewValue!=null) //If any updation has been made
                                                {
                                                    
                                                    switch(rsInfo.getColumnType(c)) {
                                                        case -5: //Long
                                                            rsMain.updateLong(FieldName,(long)objNewValue.getVal());
                                                            break;
                                                        case 4: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case 5: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case -6: //Integer,Small int
                                                            rsMain.updateInt(FieldName,(int)objNewValue.getVal());
                                                            break;
                                                        case 16: //Boolean
                                                            rsMain.updateBoolean(FieldName,objNewValue.getBool());
                                                            break;
                                                        case 91: //Date
                                                            rsMain.updateDate(FieldName,java.sql.Date.valueOf((String)objNewValue.getObj()));
                                                            break;
                                                        case 8: //Double
                                                            rsMain.updateDouble(FieldName,objNewValue.getVal());
                                                            break;
                                                        case 6: //Float
                                                            rsMain.updateDouble(FieldName,objNewValue.getVal());
                                                            break;
                                                        case 12 ://Varchar
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                        case 1 ://char
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                        default : //Varchar
                                                            rsMain.updateString(FieldName,(String)objNewValue.getObj());
                                                            break;
                                                    } //Switch
                                                    
                                                }
                                                
                                                //Fill up the column values
                                                switch(rsInfo.getColumnType(c)) {
                                                    case -5: //Long
                                                        objField.ColumnValue=new Variant(rsMain.getLong(FieldName));
                                                        break;
                                                    case 4: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case 5: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case -6: //Integer,Small int
                                                        objField.ColumnValue=new Variant(rsMain.getInt(FieldName));
                                                        break;
                                                    case 16: //Boolean
                                                        objField.ColumnValue=new Variant(rsMain.getBoolean(FieldName));
                                                        break;
                                                    case 91: //Date
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    case 8: //Double
                                                        objField.ColumnValue=new Variant(rsMain.getDouble(FieldName));
                                                        break;
                                                    case 6: //Float
                                                        objField.ColumnValue=new Variant(rsMain.getDouble(FieldName));
                                                        break;
                                                    case 12 ://Varchar
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    case 1 ://char
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                    default : //Varchar
                                                        objField.ColumnValue=new Variant(rsMain.getString(FieldName));
                                                        break;
                                                } //Switch
                                                
                                                
                                                MainFieldValues.put(Column,objField);
                                            }
                                            //=================================================================//
                                            
                                            
                                            
                                            
                                            //========== Fill Up Primary Key values ============//
                                            String PKCondition="";
                                            
                                            Iterator iPK=HeaderFieldValues.keySet().iterator();
                                            
                                            while(iPK.hasNext()) {
                                                clsPrimaryKey objPK=(clsPrimaryKey)HeaderFieldValues.get(iPK.next());
                                                String Column=objPK.ColumnName;
                                                
                                                //====== Fill up the value of Header Field with the values of Main Table =====//
                                                switch(objPK.ColumnType) {
                                                    case -5: //Long
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getLong(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getLong(Column));
                                                        break;
                                                    case 4: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case 5: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case -6: //Integer,Small int
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getInt(Column));
                                                        break;
                                                    case 16: //Boolean
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getInt(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getBoolean(Column));
                                                        break;
                                                    case 91: //Date
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getDate(Column));
                                                        break;
                                                    case 8: //Double
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getDouble(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getDouble(Column));
                                                        break;
                                                    case 6: //Float
                                                        PKCondition=PKCondition+" AND "+Column+" = "+rsMain.getDouble(Column);
                                                        objPK.ColumnValue=new Variant(rsMain.getDouble(Column));
                                                        break;
                                                    case 12 ://Varchar
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                    case 1 ://char
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                    default : //Varchar
                                                        PKCondition=PKCondition+" AND "+Column+" = '"+rsMain.getString(Column)+"' ";
                                                        objPK.ColumnValue=new Variant(rsMain.getString(Column));
                                                        break;
                                                } //Switch
                                                
                                                HeaderFieldValues.put(Column,objPK);
                                            }
                                            //=========== Filling of Primary Key Values completed ==================//
                                            
                                            
                                            
                                            
                                            //=============== Check whether recod exist in destination database ============//
                                            // Policy for overwrite
                                            // If Record is not changed at destination, simply overwrite it.
                                            // If Record is changed at destination, check for the updation date and overwrite if current date is latest than source
                                            // If Record is changed at destination and both the dates are equal, simply overwrite it.
                                            
                                            ContinueUpdate=false;
                                            
                                            if(!PKCondition.trim().equals("")) {
                                                PKCondition=" WHERE "+PKCondition.substring(4);
                                            }
                                            
                                            System.out.println("Checking for record existance ");
                                            
                                            if(HasDifferentDB) {
                                                stTmp=destConnTable.createStatement();
                                            }
                                            else {
                                                stTmp=destConn.createStatement();
                                            }
                                            
                                            rsTmp=stTmp.executeQuery("SELECT CHANGED,CHANGED_DATE FROM "+MainTableName+" "+PKCondition);
                                            rsTmp.first();
                                            
                                            if(rsTmp.getRow()>0) {
                                                DestFlag=rsTmp.getBoolean("CHANGED");
                                                DestDate=rsTmp.getDate("CHANGED_DATE");
                                                
                                                if(DestFlag==true) //Same record at destination has changed.
                                                {
                                                    if(SourceDate.after(DestDate)) //Destination record is Latest
                                                    {
                                                        //Continue overwriting
                                                        ContinueUpdate=true;
                                                    }
                                                    else {
                                                        if(SourceDate.compareTo(DestDate)==0) //Both dates are equal
                                                        {
                                                            // Continue Overwriting
                                                            ContinueUpdate=true;
                                                        }
                                                    }
                                                    
                                                }
                                                else {
                                                    //No Problem continue overwriting
                                                    ContinueUpdate=true;
                                                }
                                                
                                            }
                                            else {
                                                //Continue update if not exist
                                                ContinueUpdate=true;
                                            }
                                            //==============================================================================//
                                            
                                            
                                            
                                            //==================Physical Writing of Records =====================//
                                            //===================================================================//
                                            if(ContinueUpdate) {
                                                
                                                System.out.println("Inserting/Overwriting Row ... ");
                                                
                                                //First Delete the record from Destination
                                                strSQL="DELETE FROM "+MainTableName+" "+PKCondition;
                                                stDest.executeUpdate(strSQL);
                                                
                                                // Prepare Recordset //
                                                if(HasDifferentDB) {
                                                    stUpdate=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                }
                                                else {
                                                    stUpdate=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                }
                                                rsUpdate=stUpdate.executeQuery("SELECT * FROM "+MainTableName+" LIMIT 1");
                                                rsUpdate.first();
                                                
                                                rsUpdate.moveToInsertRow();
                                                
                                                
                                                
                                                for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                    String FieldName=rsInfo.getColumnName(c);
                                                    
                                                    switch(rsInfo.getColumnType(c)) {
                                                        case -5: //Long
                                                            rsUpdate.updateLong(FieldName,rsMain.getLong(FieldName));
                                                            break;
                                                        case 4: //Integer,Small int
                                                            rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                            break;
                                                        case 5: //Integer,Small int
                                                            rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                            break;
                                                        case -6: //Integer,Small int
                                                            rsUpdate.updateInt(FieldName,rsMain.getInt(FieldName));
                                                            break;
                                                        case 16: //Boolean
                                                            rsUpdate.updateBoolean(FieldName,rsMain.getBoolean(FieldName));
                                                            break;
                                                        case 91: //Date
                                                            rsUpdate.updateDate(FieldName,rsMain.getDate(FieldName));
                                                            break;
                                                        case 8: //Double
                                                            rsUpdate.updateDouble(FieldName,rsMain.getDouble(FieldName));
                                                            break;
                                                        case 6: //Float
                                                            rsUpdate.updateFloat(FieldName,rsMain.getFloat(FieldName));
                                                            break;
                                                        case 12 ://Varchar
                                                            rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                            break;
                                                        case 1 ://char
                                                            rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                            break;
                                                        default : //Varchar
                                                            rsUpdate.updateString(FieldName,rsMain.getString(FieldName));
                                                            break;
                                                    } //Switch
                                                }
                                                
                                                rsUpdate.updateBoolean("CHANGED", false);
                                                rsUpdate.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                                rsUpdate.insertRow();
                                                
                                            /*if(UpdateFlags) {
                                                System.out.println("Updating flag ");
                                                stSource.executeUpdate("UPDATE "+MainTableName+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+PKCondition);
                                            }*/
                                                
                                                //======Main table row inserted =======//
                                                
                                                
                                                
                                                
                                                //====Handling records of Childs =========//
                                                if(HasChilds) {
                                                    System.out.println("Querying for child tables ... ");
                                                    
                                                    stChild=srcConn.createStatement();
                                                    rsChild=stChild.executeQuery("SELECT * FROM D_COM_SYNC_CHILDS WHERE HEADER_SR_NO="+ModuleSrNo);
                                                    rsChild.first();
                                                    
                                                    if(rsChild.getRow()>0) {
                                                        while(!rsChild.isAfterLast()) {
                                                            
                                                            HasDifferentDBChild=false;
                                                            
                                                            String ChildTable=rsChild.getString("TABLE_NAME");
                                                            
                                                            System.out.println("Processing child tables : "+ChildTable);
                                                            
                                                            String addCondition=rsChild.getString("ADDITIONAL_CONDITION");
                                                            srcURLTable=rsChild.getString("DB_URL");
                                                            currentURLTable=rsChild.getString("SYNC_URL");
                                                            
                                                            
                                                            //*** Check Whether the table is in the Main database or in separate database **//
                                                            if(!currentURLTable.trim().equals("")) {
                                                                
                                                                //It has separate database
                                                                HasDifferentDBChild=true;
                                                                
                                                                System.out.println("Switching to destination "+currentURLTable);
                                                                
                                                                srcConnTable=data.getConn(srcURLTable);
                                                                srcConnTable.setAutoCommit(true);
                                                                srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                                
                                                                
                                                                stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stSource.setQueryTimeout(1000);
                                                                dbInfo=srcConnTable.getMetaData();
                                                                
                                                                destConnTable=data.getConn(currentURLTable);
                                                                destConnTable.setAutoCommit(true);
                                                                destConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                                
                                                                stDest=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stDest.setQueryTimeout(1000);
                                                            }
                                                            else {
                                                                
                                                                System.out.println("Switching to destination "+currentURL);
                                                                
                                                                stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stSource.setQueryTimeout(1000);
                                                                dbInfo=srcConn.getMetaData();
                                                                
                                                                stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                stDest.setQueryTimeout(1000);
                                                                
                                                            }
                                                            //*** ----------------------------------------------------------------------- **//
                                                            
                                                            int ChildSrNo=rsChild.getInt("SR_NO");
                                                            String Condition="";
                                                            
                                                            //=======Building Condition to fetch child records =======//
                                                            stFields=srcConn.createStatement();
                                                            ResultSet rsFields=stFields.executeQuery("SELECT * FROM D_COM_SYNC_FIELDS WHERE HEADER_SR_NO="+ModuleSrNo+" AND CHILD_SR_NO="+ChildSrNo);
                                                            rsFields.first();
                                                            
                                                            if(rsFields.getRow()>0) {
                                                                while(!rsFields.isAfterLast()) {
                                                                    String HeaderFieldName=rsFields.getString("HEADER_FIELD");
                                                                    String ChildFieldName=rsFields.getString("CHILD_FIELD");
                                                                    int HeaderFieldType=rsFields.getInt("HEADER_FIELD_TYPE");
                                                                    int ChildFieldType=rsFields.getInt("CHILD_FIELD_TYPE");
                                                                    
                                                                    Condition=Condition+" AND "+ChildFieldName+"= ";
                                                                    
                                                                    
                                                                    if(HeaderFieldType!=ChildFieldType) {
                                                                        //Header is Numeric and Child is String
                                                                        if(HeaderFieldType==1&&ChildFieldType==2) {
                                                                            Condition=Condition+" '"+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal()+"' ";
                                                                        }
                                                                        
                                                                        //Header is string and child is numeric
                                                                        if(HeaderFieldType==2&&ChildFieldType==1) {
                                                                            Condition=Condition+" "+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+" ";
                                                                        }
                                                                        
                                                                        //Both are Numeric
                                                                        if(HeaderFieldType==1&&ChildFieldType==1) {
                                                                            Condition=Condition+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal();
                                                                        }
                                                                        
                                                                        
                                                                        //Both are String
                                                                        if(HeaderFieldType==2&&ChildFieldType==2) {
                                                                            Condition=Condition+" '"+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+"' ";
                                                                        }
                                                                        
                                                                    }
                                                                    else {
                                                                        switch(HeaderFieldType) {
                                                                            case 1: //Numeric
                                                                                Condition=Condition+((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getVal();
                                                                                break;
                                                                            case 2: //String
                                                                                Condition=Condition+" '"+(String)((clsPrimaryKey)MainFieldValues.get(HeaderFieldName)).ColumnValue.getObj()+"' ";
                                                                                break;
                                                                        }
                                                                    }
                                                                    
                                                                    rsFields.next();
                                                                }
                                                            }
                                                            //========================================================//
                                                            
                                                            
                                                            if(!Condition.trim().equals("")) {
                                                                if(!addCondition.trim().equals("")) {
                                                                    Condition=" WHERE "+Condition.substring(4)+" AND "+addCondition;
                                                                }
                                                                else {
                                                                    Condition=" WHERE "+Condition.substring(4)+" ";
                                                                }
                                                            }
                                                            else {
                                                                Condition=" WHERE "+addCondition;
                                                            }
                                                            
                                                            
                                                            // ======  Fetch Child Records ==============//
                                                            strSQL=" DELETE FROM "+ChildTable+" "+Condition;
                                                            stDest.executeUpdate(strSQL);
                                                            
                                                            strSQL=" SELECT * FROM "+ChildTable+" "+Condition;
                                                            
                                                            System.out.println("Querying for child records ");
                                                            System.out.println(strSQL);
                                                            
                                                            if(HasDifferentDBChild) {
                                                                stTmp=srcConnTable.createStatement();
                                                            }
                                                            else {
                                                                stTmp=srcConn.createStatement();
                                                            }
                                                            rsTmp=stTmp.executeQuery(strSQL);
                                                            rsTmp.first();
                                                            
                                                            if(rsTmp.getRow()>0) {
                                                                rsInfo=rsTmp.getMetaData();
                                                                
                                                                if(HasDifferentDBChild) {
                                                                    stUpdate=destConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                }
                                                                else {
                                                                    stUpdate=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                                }
                                                                rsUpdate=stUpdate.executeQuery("SELECT * FROM "+ChildTable+" LIMIT 1");
                                                                rsUpdate.first();
                                                                
                                                                while(!rsTmp.isAfterLast()) {
                                                                    
                                                                    System.out.println("Processing row # "+rsTmp.getRow()+" of "+ChildTable);
                                                                    
                                                                    rsUpdate.moveToInsertRow();
                                                                    
                                                                    for(int c=1;c<=rsInfo.getColumnCount();c++) {
                                                                        String FieldName=rsInfo.getColumnName(c);
                                                                        
                                                                        switch(rsInfo.getColumnType(c)) {
                                                                            case -5: //Long
                                                                                rsUpdate.updateLong(FieldName,rsTmp.getLong(FieldName));
                                                                                break;
                                                                            case 4: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case 5: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case -6: //Integer,Small int
                                                                                rsUpdate.updateInt(FieldName,rsTmp.getInt(FieldName));
                                                                                break;
                                                                            case 16: //Boolean
                                                                                rsUpdate.updateBoolean(FieldName,rsTmp.getBoolean(FieldName));
                                                                                break;
                                                                            case 91: //Date
                                                                                rsUpdate.updateDate(FieldName,rsTmp.getDate(FieldName));
                                                                                break;
                                                                            case 8: //Double
                                                                                rsUpdate.updateDouble(FieldName,rsTmp.getDouble(FieldName));
                                                                                break;
                                                                            case 6: //Float
                                                                                rsUpdate.updateFloat(FieldName,rsTmp.getFloat(FieldName));
                                                                                break;
                                                                            case 12 ://Varchar
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                            case 1 ://char
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                            default : //Varchar
                                                                                rsUpdate.updateString(FieldName,rsTmp.getString(FieldName));
                                                                                break;
                                                                        } //Switch
                                                                    }
                                                                    
                                                                    rsUpdate.updateBoolean("CHANGED",false);
                                                                    rsUpdate.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                                                                    rsUpdate.insertRow();
                                                                    
                                                                    if(UpdateFlags) {
                                                                        stSource.executeUpdate("UPDATE "+ChildTable+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+Condition);
                                                                    }
                                                                    
                                                                    rsTmp.next();
                                                                }
                                                                
                                                                System.out.println("Done. Child Records ");
                                                            }
                                                            //=====Insert child records ==============//
                                                            
                                                            
                                                            rsChild.next();
                                                        }
                                                    }
                                                }
                                                
                                            }
                                            //=============== End of Updating Records ======================//
                                            
                                            if(UpdateFlags) {
                                                System.out.println("Updating flag ");
                                                
                                                if(ModuleHasDifferentDB) {
                                                    
                                                    srcURLTable=rsModules.getString("DB_URL");
                                                    currentURLTable=rsModules.getString("SYNC_URL");
                                                    
                                                    System.out.println("Switching to destination "+currentURLTable);
                                                    
                                                    srcConnTable=data.getConn(srcURLTable);
                                                    srcConnTable.setAutoCommit(true);
                                                    srcConnTable.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                                                    
                                                    
                                                    
                                                    stSource=srcConnTable.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stSource.setQueryTimeout(1000);
                                                    
                                                }
                                                else {
                                                    
                                                    System.out.println("Switching to destination "+currentURL);
                                                    
                                                    stSource=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stSource.setQueryTimeout(1000);
                                                    dbInfo=srcConn.getMetaData();
                                                    
                                                    stDest=destConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                                    stDest.setQueryTimeout(1000);
                                                    
                                                }
                                                
                                                stSource.executeUpdate("UPDATE "+MainTableName+" SET CHANGED=0,CHANGED_DATE=CURDATE() "+PKCondition);
                                            }
                                            
                                            
                                            
                                            
                                            System.out.println("Commiting module "+rsModules.getString("MODULE_NAME"));
                                            //srcConn.commit();
                                            //destConn.commit();
                                            
                                            try {
                                                //srcConnTable.commit();
                                                //destConnTable.commit();
                                            }
                                            catch(Exception ConnE){}
                                            
                                        }catch(Exception rsMainE) {
                                            
                                        }
                                        
                                        rsMain.next();
                                    }
                                }
                                
                                //Commit at module level
                                System.out.println("Commiting Record ");
                                //srcConn.commit();
                                //destConn.commit();
                                
                                try {
                                    //srcConnTable.commit();
                                    //destConnTable.commit();
                                }
                                catch(Exception ConnE){}
                                
                            }
                            catch(Exception mod) {
                                mod.printStackTrace();
                                //srcConn.rollback();
                                //destConn.rollback();
                                mod.printStackTrace();
                            }
                            rsModules.next();
                        }
                    }
                    //========================================//
                    
                    
                    System.out.println("Now commiting changes ... ");
                    //srcConn.commit();
                    //destConn.commit();
                    System.out.println("Changes commited successfully.");
                    System.out.println("Process completed successfully");
                    
                    System.out.println("0");
                    
                }
                catch(Exception e) {
                    
                    System.out.println("Process failed. ");
                    try {
                        //srcConn.rollback();
                        //destConn.rollback();
                        System.out.println("Rollback succeeded");
                    }
                    catch(Exception d) {
                        System.out.println("Rollback failed");
                        System.exit(1);
                    }
                    
                    e.printStackTrace();
                    System.out.println("1");
                }
            }
        }.start();
    }
    
    
}
