/*
 * TableInformation.java
 *
 * Created on April 14, 2010, 10:25 AM
 */

package EITLERP.Utils;

import java.sql.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import EITLERP.Finance.*;
import EITLERP.*;
/**
 *
 * @author  root
 */
public class TableInformation {
    
    /** Creates a new instance of TableInformation */
    public TableInformation() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            
            
            
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        
        try {
            
            BufferedWriter output = null;
            
            File file = new File("/root/Desktop/write.txt");
            output = new BufferedWriter(new FileWriter(file));
            Connection con = data.getConn();
            //Connection con = data.getConn();
            
            
            DatabaseMetaData metadata = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rsTables = metadata.getTables(null,null,"%",types);
            
            while (rsTables.next()) {
                
                String TableName = rsTables.getString("TABLE_NAME");
                output.write("Table Name" +"~" + TableName);
                output.newLine();
                ResultSet resultSet = metadata.getColumns(null, null, TableName, null);
                
                System.out.println("Table Name :  " + TableName);
                output.write("~"+"Column Name"+"~"+"Type"+"~"+"Size"+"~" + "Key" + "~" + "Description");
                output.newLine();
                while (resultSet.next()) {
                    String name = resultSet.getString("COLUMN_NAME");
                    String type = resultSet.getString("TYPE_NAME");
                    int size = resultSet.getInt("COLUMN_SIZE");
                    
                    String info="~"+name +"~"+type+"~"+ size+ "~";
                    System.out.println(info);
                    output.write(info);
                    output.newLine();
                    
                }
                
            }
            output.write("Finished");
            output.close();
            System.out.println("Finished....");
            /* Get Table Name
             String[] types = {"TABLE"};
            DatabaseMetaData metadata = con.getMetaData();
            ResultSet resultSet = metadata.getTables(null,null,"%",types);
            while (resultSet.next()) {
                String table = resultSet.getString("TABLE_NAME");
          System.out.println(table);
            }
             */
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    /*
     
     
            BufferedWriter output = null;
            String text = "Rajesh Kumar";
            File file = new File("/root/Desktop/write.txt");
            output = new BufferedWriter(new FileWriter(file));
            Connection con = data.getConn();
     
            DatabaseMetaData metadata = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rsTables = metadata.getTables(null,null,"%",types);
     
            while (rsTables.next()) {
     
                String TableName = rsTables.getString("TABLE_NAME");
                output.write("Table Name" +"~" + TableName);
                output.newLine();
                ResultSet resultSet = metadata.getColumns(null, null, TableName, null);
                System.out.println("Table Name :  " + TableName);
                output.write("~"+"Column Name"+"~"+"Type"+"~"+"Size");
                output.newLine();
                while (resultSet.next()) {
                    String name = resultSet.getString("COLUMN_NAME");
                    String type = resultSet.getString("TYPE_NAME");
                    int size = resultSet.getInt("COLUMN_SIZE");
                    String key = resultSet.getString("INDEX_NAME");
                    String info="~"+name +"~"+type+"~"+ size+ "~";
                    System.out.println(info);
                    output.write(info);
                    output.newLine();
     
                }
            }
     
     */
    
}
