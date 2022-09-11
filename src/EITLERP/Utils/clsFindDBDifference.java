/*
 * clsFindDBDifference.java
 *
 * Created on January 18, 2006, 5:06 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import EITLERP.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import javax.sql.*;
import java.io.*;

/* List outs database differences */

public class clsFindDBDifference {
    
    /** Creates a new instance of clsFindDBDifference */
    public clsFindDBDifference() {
    }
    
    /* Expected Arguments
     * 1 - Source Database URL
     * 2 - Destination Database URL
     *
     */
    public static void main(String args[]) {
        if(args.length<2) {
            System.out.println("Insufficient Parameters. Specify source and destination database URL");
            return;
        }
        
        String srcURL=args[0];
        String destURL=args[1];
        
        try {
            System.out.println("Process is going on ...");
            
            //Logging Differences
            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File("Diff.txt"))) ;
            
            aFile.write("Table Differences ");
            aFile.newLine();
            
            Connection srcConn=data.getConn(srcURL);
            Connection destConn=data.getConn(destURL);
            
            DatabaseMetaData srcInfo=srcConn.getMetaData();
            DatabaseMetaData destInfo=destConn.getMetaData();
            
            
            //======Finding Tables Differences ==========//
            
            String[] names = {"TABLE"};
            String[] destnames = {"TABLE"};
            ResultSet tableNames = srcInfo.getTables(null,"%", "%", names);
            
            
            while (tableNames.next()) {
                String TableName=tableNames.getString("TABLE_NAME");
                
                boolean TableFound=false;
                
                ResultSet desttableNames = destInfo.getTables(null,"%", "%", destnames);
                while(desttableNames.next()) {
                    String destTable=desttableNames.getString("TABLE_NAME");
                    
                    if(destTable.trim().equals(TableName.trim())) {
                        TableFound=true;
                    }
                }
                
                if(!TableFound) {
                    aFile.write("TABLE NOT FOUND : "+TableName);
                    aFile.newLine();
                }
                else {
                    //Now find out field differences
                    Statement stSource=srcConn.createStatement();
                    ResultSet rsSource=stSource.executeQuery("SELECT * FROM "+TableName+" LIMIT 1");
                    ResultSetMetaData rsInfoSource=rsSource.getMetaData();
                    
                    Statement stDest=destConn.createStatement();
                    ResultSet rsDest=stDest.executeQuery("SELECT * FROM "+TableName+" LIMIT 1");
                    ResultSetMetaData rsInfoDest=rsDest.getMetaData();

                    boolean TableEntryMade=false;
                    
                    //Loop through each field
                    for(int s=1;s<=rsInfoSource.getColumnCount();s++) {
                        boolean FieldFound=false;
                        String srcField=rsInfoSource.getColumnName(s);
                        
                        for(int d=1;d<=rsInfoDest.getColumnCount();d++) {
                            String destField=rsInfoDest.getColumnName(d);
                            
                            if(destField.trim().equals(srcField.trim())) {
                                FieldFound=true;
                            }
                        }
                        
                        if(!FieldFound) {
                            
                            if(!TableEntryMade)
                            {
                                TableEntryMade=true;
                                aFile.write(TableName);
                                aFile.newLine();
                                aFile.write("===============================");
                                aFile.newLine();
                            }
                            aFile.write(srcField);
                            aFile.newLine();
                        }
                    }
                    
                }
            }

            aFile.close();
            
            System.out.println("Done...");
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
