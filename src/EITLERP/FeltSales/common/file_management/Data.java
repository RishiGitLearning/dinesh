/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common.file_management;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class Data {
    private int file_id;
    private String file_name;
    private InputStream file;

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
    
    public void getFileFromDatabase(int ID)
    {
       try{
            ResultSet rs = null;
            rs = data.getResult("SELECT FILE FROM PRODUCTION.FILE_EXAMPLE WHERE FILE_ID="+ID);

            String File_Name = data.getStringValueFromDB("SELECT FILE_NAME FROM PRODUCTION.FILE_EXAMPLE WHERE FILE_ID="+ID);
            File file = new File("/root/Daxesh/File/"+File_Name);
            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);

            System.out.println("Writing to file " + file.getAbsolutePath());

            rs.first();

            byte[] imagebytes = rs.getBytes("FILE");
            output.write(imagebytes);
            output.close();

         }catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    public void saveFile(FileInputStream fis,int length){
        try{   
            PreparedStatement statement = data.getConn().prepareStatement("INSERT INTO PRODUCTION.FILE_EXAMPLE (FILE_NAME,FILE) values (?,?)");
        
            statement.setString(1,file_name);
            statement.setBinaryStream(2,fis,length);
            statement.executeUpdate();
 
            statement.close();
            fis.close();
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
