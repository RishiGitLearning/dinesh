/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.TravelAdvance;

import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Dharmendra
 */
public class Doc_mail {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
//            if (data.getStringValueFromDB("SELECT DAYNAME(CURDATE()) FROM DUAL").equalsIgnoreCase("Saturday")) {
                ResultSet rs = null;
                rs = data.getResult("SELECT * FROM DOC_MGMT.TRAVEL_VOUCHER "
                        + "LEFT JOIN (SELECT DISTINCT TSD_DOC_NO,APPROVED,APPROVED_DATE,TSD_PAY_EMP_NO,TSD_NAME FROM SDMLATTPAY.TRAVEL_VOUCHER_DETAIL ) AS T "
                        + "ON DOCUMENT_DOC_NO=TSD_DOC_NO "
                        + "WHERE "
                        + "DOC_TYPE='REPORT' AND "
                        //+ "APPROVED=1 AND APPROVED_DATE>=DATE_SUB(CURDATE(),INTERVAL 6 DAY) AND APPROVED_DATE<=DATE_ADD(CURDATE(),INTERVAL 1 DAY)"
                        + "APPROVED=1 AND APPROVED_DATE=DATE_SUB(CURDATE(),INTERVAL 1 DAY) "
                        + "GROUP BY DOCUMENT_DOC_NO,DOCUMENT_SR_NO "
                        + "ORDER BY DOCUMENT_DOC_NO,DOCUMENT_SR_NO");
                rs.first();
                if (rs.getRow() > 0) {

                    while (!rs.isAfterLast()) {
                        String extension = "";
                        String fileName = rs.getString("DOC_NAME");

                        int i = fileName.lastIndexOf('.');
                        if (i > 0) {
                            extension = fileName.substring(i + 1);
                            fileName = fileName.substring(0, i);
                        }
                        File file = new File("/Email_Attachment/" + fileName + "_" + rs.getString("TSD_DOC_NO") + "_"
                                + rs.getString("TSD_PAY_EMP_NO") + "_" + rs.getString("TSD_NAME") + "." + extension);

                        try {
                            FileOutputStream output = new FileOutputStream(file);                            
                            byte[] imagebytes = rs.getBytes("DOCUMENT");
                            output.write(imagebytes);
                            output.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rs.next();
                    }
                    String zipFile = "/Email_Attachment/Travel_Doc.zip";

                    String[] srcFiles = new String[1000];
                    rs.first();
                    int f = 0;
                    while (!rs.isAfterLast()) {
                        String extension = "";
                        String fileName = rs.getString("DOC_NAME");

                        int i = fileName.lastIndexOf('.');
                        if (i > 0) {
                            extension = fileName.substring(i + 1);
                            fileName = fileName.substring(0, i);
                        }
                        srcFiles[f] = "/Email_Attachment/" + fileName + "_" + rs.getString("TSD_DOC_NO") + "_"
                                + rs.getString("TSD_PAY_EMP_NO") + "_" + rs.getString("TSD_NAME") + "." + extension;
                        rs.next();
                        f++;
                    }

                    // create byte buffer
                    byte[] buffer = new byte[1024];

                    FileOutputStream fos = new FileOutputStream(zipFile);

                    ZipOutputStream zos = new ZipOutputStream(fos);

                    for (int i = 0; i < f; i++) {

                        File srcFile = new File(srcFiles[i]);

                        FileInputStream fis = new FileInputStream(srcFile);

                        // begin writing a new ZIP entry, positions the stream to the start of the entry data
                        zos.putNextEntry(new ZipEntry(srcFile.getName()));

                        int length;

                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }

                        zos.closeEntry();

                        // close the InputStream
                        fis.close();

                    }

                    // close the ZipOutputStream
                    zos.close();
                    String recievers = "aditya@dineshmills.com";
                    //String recievers = "dharmendra@dineshmills.com";
                    //String body = "Weekly tour report submission from "
                    String body = "Tour report submission for "
                            + data.getStringValueFromDB("SELECT DATE_FORMAT(DATE_SUB(CURDATE(),INTERVAL 1 DAY),'%d/%m/%Y') FROM DUAL") ;
                            //+ " to "
                            //+ data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d/%m/%Y') FROM DUAL");
                    String Subject =body;
                    String cc = "sdmlerp@dineshmills.com";
                    String Path = "/Email_Attachment/Travel_Doc.zip";
                    String a_File = "Travel_Doc.zip";
                    JavaMail.SendMailwithAttachment(recievers, body, Subject, cc, Path, a_File);
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
