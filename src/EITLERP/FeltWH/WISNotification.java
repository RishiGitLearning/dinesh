/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltWH;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.FeltInvCalc;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.Finance.UtilFunctions;
import EITLERP.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Dharmendra
 */
public class WISNotification {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here

//        String mFromDate = data.getStringValueFromDB("SELECT SUBDATE(CURDATE(), INTERVAL 3 MONTH) FROM DUAL");
        String mFromDate = "2019-07-09";
        String mToDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
        
        String mIBNP, mPBNG, mGBNO;
        mIBNP = "SELECT H.PARTY_CODE AS 'Party Code',H.PARTY_NAME AS 'Party Name',D.PIECE_NO AS 'Piece No',D.BALE_NO AS 'Bale No',H.INVOICE_NO AS 'Invoice No',DATE_FORMAT(H.INVOICE_DATE,'%d/%m/%Y') AS 'Invoice Date',H.INVOICE_AMT AS 'Invoice Amount' FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H  "
                + "LEFT JOIN PRODUCTION.FELT_SAL_INVOICE_DETAIL D on H.INVOICE_NO=D.INVOICE_NO AND H.INVOICE_DATE=SUBSTRING(D.INVOICE_DATE,1,10) "
                + "WHERE H.APPROVED=1 AND H.CANCELLED=0  "
                + "AND H.INVOICE_DATE>='" + mFromDate + "' AND H.INVOICE_DATE<='" + mToDate + "'  "
                + "AND CONCAT(H.INVOICE_NO,H.INVOICE_DATE) NOT IN (SELECT CONCAT(INV_NO,INV_DATE) FROM PRODUCTION.FELT_POST_INVOICE_DATA)";
//        mPBNG = "SELECT * FROM PRODUCTION.FELT_POST_INVOICE_DATA D "
        mPBNG = "SELECT PARTY_CODE AS 'Party Code',PARTY_NAME AS 'Party Name',PI_PIECE_NO AS 'Piece No',D.BALE_NO AS 'Bale No',INV_NO AS 'Invoice No',DATE_FORMAT(INV_DATE,'%d/%m/%Y') AS 'Invoice Date',INVOICE_AMT AS 'Invoice Amount' FROM PRODUCTION.FELT_POST_INVOICE_DATA D "
                + "LEFT JOIN (SELECT INVOICE_NO,INVOICE_DATE,BALE_NO,INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ) AS INV ON INVOICE_NO=INV_NO AND INVOICE_DATE=INV_DATE AND INV.BALE_NO=D.BALE_NO  "
                + "WHERE CONCAT(INV_NO,INV_DATE) NOT IN "
                + "(SELECT CONCAT(WH_INVOICE_NO,WH_INVOICE_DATE) FROM DINESHMILLS.D_INV_NRGP_DETAIL D "
                + "LEFT JOIN DINESHMILLS.D_INV_NRGP_HEADER H ON H.GATEPASS_NO=D.GATEPASS_NO "
                + "WHERE  H.APPROVED=1 AND COALESCE(H.CANCELED,0)=0 AND D.GATEPASS_NO LIKE 'FGP%') "
                + " AND INV_DATE>='" + mFromDate + "' AND INV_DATE<='" + mToDate + "'  ";
//        mGBNO = "SELECT * FROM DINESHMILLS.D_INV_NRGP_DETAIL H "
        mGBNO = "SELECT WH_PARTY_CODE AS 'Party Code',WH_PARTY_NAME AS 'Party Name',PI_PIECE_NO AS 'Piece No',GATEPASS_NO AS 'Gatepass No',WH_BALE_NO AS 'Bale No',INV_NO AS 'Invoice No',DATE_FORMAT(INV_DATE,'%d/%m/%Y') AS 'Invoice Date',INVOICE_AMT AS 'Invoice Amount' FROM DINESHMILLS.D_INV_NRGP_DETAIL H "
                + "LEFT JOIN PRODUCTION.FELT_POST_INVOICE_DATA D ON D.INV_NO=H.WH_INVOICE_NO AND D.INV_DATE=H.WH_INVOICE_DATE "
                + "LEFT JOIN (SELECT INVOICE_NO,INVOICE_DATE,BALE_NO,INVOICE_AMT FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ) AS INV ON INVOICE_NO=INV_NO AND INVOICE_DATE=INV_DATE AND INV.BALE_NO=D.BALE_NO "
                + "WHERE GATEPASS_NO LIKE 'FGP%'"
                + " AND WH_INVOICE_DATE>='" + mFromDate + "' AND WH_INVOICE_DATE<='" + mToDate + "'  "
                + " AND GATEPASS_NO NOT IN (SELECT GATEPASS_NO FROM DINESHMILLS.D_INV_GATE_OUTWARD_HEADER WHERE APPROVED=1 AND COALESCE(CANCELLED,0)=0) ";

//        String recievers = "yrpatel@dineshmills.com,amitkanti@dineshmills.com,feltpp@dineshmills.com,brdfltneedle@dineshmills.com,balgondapatil@dineshmills.com,brdfltfin@dineshmills.com,narendramotiani@dineshmills.com";
        String recievers = "felts@dineshmills.com,feltwh@dineshmills.com";

        exprt.fillData(mGBNO + "#" + mPBNG + "#" + mIBNP, new File("/Email_Attachment/WISNotification.xls"), "Gatepass But Not Outword#Post Invoiced But Not Gatepass#Invoiced But Not Post Invoice");

        String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d/%m/%Y') FROM DUAL");

        String responce = sendNotificationMailWithAttachement("WIS NOTIFICATION", "WIS NOTIFICATION AS ON " + mdate, recievers, "sdmlerp@dineshmills.com", "/Email_Attachment/WISNotification.xls", "WISNotification.xls");
        System.out.println("Mail Response:" + responce);

    }

    public static String sendNotificationMailWithAttachement(String pSubject, String pMessage, String recievers, String pcc, String Path, String PFiles) {
        try {
            System.out.println("Recivers : " + recievers);
            System.out.println("pSubject : " + pSubject);
            System.out.println("pMessage : " + pMessage);
            System.out.println("pCC      : " + pcc);
            System.out.println("Files    : " + PFiles);

            JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
        } catch (Exception e) {
            System.out.println("Error Msg " + e.getMessage());
            e.printStackTrace();
        }
        return "Mail Sending Done....!";
    }

    public static void writeToZipFile(String path, ZipOutputStream zipStream) throws FileNotFoundException, IOException {
        System.out.println("Writing file : '" + path + "' to zip file");
        File aFile = new File(path);
        FileInputStream fis = new FileInputStream(aFile);
        ZipEntry zipEntry = new ZipEntry(path);
        zipStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }
        zipStream.closeEntry();
        fis.close();
    }

}
