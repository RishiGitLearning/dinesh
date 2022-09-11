/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.IncrementProposal;

/**
 *
 * @author Dharmendra
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class ReadingDataFromBlobandClob {
   public static void main(String args[]) throws Exception {
      //Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Getting the connection
      String mysqlUrl = "jdbc:mysql://200.0.0.230/SDMLATTPAY";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
      //Inserting values
      String query = "UPDATE INCREMENT_ENTRY_DETAIL SET IED_DOC=? WHERE IED_PAY_EMP_NO='BRD101539'";
      PreparedStatement pstmt = con.prepareStatement(query);
      
      FileReader fileReader = new FileReader("E:\\Production.xls");
      pstmt.setClob(1, fileReader);
      //InputStream inputStream = new FileInputStream("E:\\images\\javafx_logo.jpg");
      //pstmt.setBlob(3, inputStream);
      pstmt.execute();
      System.out.println("Record inserted......");
      //Retrieving the results
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * from INCREMENT_ENTRY_DETAIL WHERE IED_PAY_EMP_NO='BRD101539'");
      while(rs.next()) {
         String name = rs.getString("IED_EMP_NAME");
         Clob clob = rs.getClob("IED_DOC");
         //Blob blob = rs.getBlob("Logo");
         System.out.println("Name: "+name);
         System.out.println("Clob value: "+clob);
        // System.out.println("Blob value: "+blob);
         System.out.println("");
         System.out.print("Clob data is stored at: ");
         //Storing clob to a file
         int i, j =0;
         Reader r = clob.getCharacterStream();
         String filePath = "E:\\BRD101539.XLS";
         FileWriter writer = new FileWriter(filePath);
         while ((i=r.read())!=-1) {
               writer.write(i);
         }
         writer.close();
         System.out.println(filePath);
         j++;
//         System.out.print("Blob data is stored at: ");
//         InputStream is = blob.getBinaryStream();
//         byte byteArray[] = new byte[is.available()];
//         is.read(byteArray);
//         filePath = "E:\\output\\"+name+"_article_logo.jpg";
//         FileOutputStream outPutStream = new FileOutputStream(filePath);
//         outPutStream.write(byteArray);
//         System.out.println(filePath);
      }
   }
}