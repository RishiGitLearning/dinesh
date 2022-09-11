/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common.file_management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import sdml.felt.commonUI.data;

/**
 *
 * @author root
 */
public class clsDocManagement {
    
    int CD_ID;
    String CD_NAME;
    FileInputStream CD_CIRCUIT_DIAGRAM;
    String CD_MACHINE_DOC_NO;
    String CD_DOC_NAME;

    public int getCD_ID() {
        return CD_ID;
    }

    public void setCD_ID(int CD_ID) {
        this.CD_ID = CD_ID;
    }

    public String getCD_NAME() {
        return CD_NAME;
    }

    public void setCD_NAME(String CD_NAME) {
        this.CD_NAME = CD_NAME;
    }

    public FileInputStream getCD_CIRCUIT_DIAGRAM() {
        return CD_CIRCUIT_DIAGRAM;
    }

    public void setCD_CIRCUIT_DIAGRAM(FileInputStream CD_CIRCUIT_DIAGRAM) {
        this.CD_CIRCUIT_DIAGRAM = CD_CIRCUIT_DIAGRAM;
    }

    public String getCD_MACHINE_DOC_NO() {
        return CD_MACHINE_DOC_NO;
    }

    public void setCD_MACHINE_DOC_NO(String CD_MACHINE_DOC_NO) {
        this.CD_MACHINE_DOC_NO = CD_MACHINE_DOC_NO;
    }

    public String getCD_DOC_NAME() {
        return CD_DOC_NAME;
    }

    public void setCD_DOC_NAME(String CD_DOC_NAME) {
        this.CD_DOC_NAME = CD_DOC_NAME;
    }
    
    public void saveMachineFile(int length)
    {
        try{   
            PreparedStatement statement = data.getConn().prepareStatement("INSERT INTO DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM (CD_NAME,CD_CIRCUIT_DIAGRAM,CD_MACHINE_DOC_NO,CD_DOC_NAME) values (?,?,?,?)");
            
            statement.setString(1,CD_NAME);
            statement.setBinaryStream(2,CD_CIRCUIT_DIAGRAM,length);
            statement.setString(3,CD_MACHINE_DOC_NO);
            statement.setString(4,CD_DOC_NAME);
            statement.executeUpdate();
            statement.close();
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getFileFromDatabase(String CD_MACHINE_DOC_NO)
    {
       try{
            ResultSet rs = null;
            rs = data.getResult("SELECT CD_CIRCUIT_DIAGRAM FROM PRODUCTION.FILE_EXAMPLE WHERE CD_MACHINE_DOC_NO='"+CD_MACHINE_DOC_NO+"'");

            String File_Name = data.getStringValueFromDB("SELECT CD_NAME FROM PRODUCTION.FILE_EXAMPLE WHERE CD_MACHINE_DOC_NO='"+CD_MACHINE_DOC_NO+"'");
            File file = new File("/root/Daxesh/File/"+File_Name);
            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);
            System.out.println("Writing to file " + file.getAbsolutePath());
            rs.first();
            byte[] imagebytes = rs.getBytes("CD_CIRCUIT_DIAGRAM.");
            output.write(imagebytes);
            output.close();

         }catch(Exception e)
         {
             e.printStackTrace();
         }
    }
    
    public ArrayList<clsDocManagement> getStatus(String CD_MACHINE_DOC_NO)
    {
        ArrayList<clsDocManagement> dataList = new ArrayList<clsDocManagement>();
        try{
                ResultSet rs;
                rs=data.getResult("SELECT * FROM DOC_MGMT.FELT_MACHINE_CIRCUIT_DIAGRAM where CD_MACHINE_DOC_NO='"+CD_MACHINE_DOC_NO+"'");

                
                rs.first();
                System.out.println("Row no."+rs.getRow());
                
                if (rs.getRow() > 0) {
                    int cnt = 0;
        
                    while (!rs.isAfterLast()) {

                        clsDocManagement Obj = new clsDocManagement();    

                        Obj.setCD_ID(rs.getInt("CD_ID"));
                        Obj.setCD_NAME(rs.getString("CD_NAME"));
                        Obj.setCD_DOC_NAME(rs.getString("CD_DOC_NAME"));
                        //rs.getString("CD_MACHINE_DOC_NO");
                        dataList.add(Obj);
                        rs.next();
                    }
                    System.out.println("Completed : Row"+(cnt++));
                }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return dataList;
    }
}
