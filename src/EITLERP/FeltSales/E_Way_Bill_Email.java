/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
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
public class E_Way_Bill_Email {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        data.Execute("DELETE FROM FINANCE.D_SAL_GSTR_INVOICE_ERP WHERE INVOICE_DATE >= DATE_SUB(NOW(), INTERVAL 10 DAY) AND INPUT_TYPE NOT IN (2) ");

        data.Execute("TRUNCATE TABLE FINANCE.TMP_GSTR_INVOICE_ERP ");

        data.Execute("INSERT INTO FINANCE.TMP_GSTR_INVOICE_ERP (COMPANY_ID,INPUT_TYPE,PARTY_CODE,INVOICE_NO,INVOICE_DATE,GSTIN_NO,INVOICE_VALUE,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,STATE_CODE,STATE_NAME,STATE_PIN_CODE,APPROVED,CANCELLED) SELECT 2,H.INVOICE_TYPE,H.PARTY_CODE,H.INVOICE_NO,H.INVOICE_DATE,GSTIN_NO,H.NET_AMOUNT,COALESCE(IGST_PER,0),COALESCE(IGST_AMT,0),COALESCE(CGST_PER,0) ,COALESCE(CGST_AMT,0),COALESCE(SGST_PER,0) ,COALESCE(SGST_AMT,0),STATE_GST_CODE,STATE,PINCODE,H.APPROVED,H.CANCELLED  FROM  DINESHMILLS.D_SAL_INVOICE_HEADER H,DINESHMILLS.D_SAL_PARTY_MASTER P WHERE H.INVOICE_DATE >= '2017-07-01'  AND INVOICE_TYPE IN (1,3) AND  INVOICE_NO NOT IN (SELECT INVOICE_NO FROM  FINANCE.D_SAL_GSTR_INVOICE_ERP ) AND P.PARTY_CODE = H.PARTY_CODE ");

        data.Execute("UPDATE FINANCE.TMP_GSTR_INVOICE_ERP E, FINANCE.D_SAL_GSTR_INVOICE C SET E.WH_CODE = C.WH_CODE, E.INVOICE_TYPE  = C.INVOICE_TYPE, E.ITEM_DESC = C.ITEM_DESC, E.GSTIN_NO= C.GSTIN_NO, E.HSN_CODE = C.HSN_CODE+0, E.TAXABLE_VALUE = C.TAXABLE_VALUE, E.PLACE_OF_SUPPLY = C.PLACE_OF_SUPPLY, E.RATE = C.RATE, E.REV_CHRG = C.REV_CHRG, E.E_COMM_GSTIN_NO  = C.E_COMM_GSTIN_NO WHERE E.INVOICE_NO = C.INVOICE_NO  AND E.PARTY_CODE = C.PARTY_CODE AND E.INVOICE_DATE = C.INVOICE_DATE AND E.INVOICE_VALUE= ROUND(C.INVOICE_VALUE+.0001,0) ");

        data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP E, FINANCE.D_SAL_GSTR_INVOICE C SET E.WH_CODE = C.WH_CODE, E.INVOICE_TYPE  = C.INVOICE_TYPE, E.ITEM_DESC = C.ITEM_DESC, E.GSTIN_NO= C.GSTIN_NO, E.HSN_CODE = C.HSN_CODE+0, E.TAXABLE_VALUE = C.TAXABLE_VALUE, E.PLACE_OF_SUPPLY = C.PLACE_OF_SUPPLY, E.RATE = C.RATE, E.REV_CHRG = C.REV_CHRG, E.E_COMM_GSTIN_NO  = C.E_COMM_GSTIN_NO WHERE E.INVOICE_NO = C.INVOICE_NO AND E.PARTY_CODE = C.PARTY_CODE AND E.INVOICE_DATE = C.INVOICE_DATE AND E.INVOICE_VALUE= ROUND(C.INVOICE_VALUE+.0001,0) AND E.TAXABLE_VALUE =0 ");

        data.Execute("UPDATE FINANCE.TMP_GSTR_INVOICE_ERP SET IGST_PER =0 WHERE IGST_AMT =0 ");
        data.Execute("UPDATE FINANCE.TMP_GSTR_INVOICE_ERP SET CGST_PER =0 WHERE CGST_AMT =0 ");
        data.Execute("UPDATE FINANCE.TMP_GSTR_INVOICE_ERP SET SGST_PER =0 WHERE SGST_AMT =0 ");

        data.Execute("INSERT INTO FINANCE.D_SAL_GSTR_INVOICE_ERP SELECT * FROM FINANCE.TMP_GSTR_INVOICE_ERP ");

        String SQL1 = "DELETE FROM DINESHMILLS.D_SAL_EWAY_REPORT WHERE DOC_DATE >= DATE_SUB(NOW() ,INTERVAL 4 DAY) ";
        System.out.println("SQL1 = " + SQL1);
        data.Execute(SQL1);

        String SQL2 = "TRUNCATE TABLE DINESHMILLS.TMP_SAL_EWAY_REPORT ";
        System.out.println("SQL2 = " + SQL2);
        data.Execute(SQL2);

        String SQL3 = "INSERT INTO DINESHMILLS.TMP_SAL_EWAY_REPORT ";
        SQL3 += "(SUPPLY_TYPE,SUB_TYPE,DOC_TYPE,INVOICE_TYPE,DOC_NO,DOC_DATE,PARTY_CODE,IGST_PER,CGST_PER,SGST_PER,IGST_AMOUNT,CGST_AMOUNT,SGST_AMOUNT,ASSESSABLE_VALUE,APPROVED,CANCELLED,FROM_OTHER_PARTY_NAME,FROM_GSTIN,FROM_ADDRESS1,FROM_ADDRESS2,FROM_PLACE,FROM_PINCODE,FROM_STATE) ";
        SQL3 += "(SELECT 'Outward','Supply','Tax Invoice',INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,IGST_PER,CGST_PER,SGST_PER,IGST_AMT,CGST_AMT,SGST_AMT,NET_AMOUNT,APPROVED,CANCELLED,'SHRI DINESH MILLS LIMITED','24AADCS3115Q1Z8','PO BOX NO 2501','PADRA ROAD AKOTA','VADODARA','390020','GUJARAT' ";
        SQL3 += "FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_DATE >= '2018-01-01' ";
        SQL3 += "AND CONCAT(INVOICE_NO,INVOICE_DATE)  NOT IN ( SELECT CONCAT(DOC_NO,DOC_DATE)  FROM DINESHMILLS.D_SAL_EWAY_REPORT)) ";
        System.out.println("SQL3 = " + SQL3);
        data.Execute(SQL3);

        String SQL4 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT A ,DINESHMILLS.D_SAL_PARTY_MASTER B ";
        SQL4 += "SET A.TO_OTHER_PARTY_NAME = B.PARTY_NAME, ";
        SQL4 += "A.TO_GSTIN = LTRIM(RTRIM(B.GSTIN_NO)), ";
        SQL4 += "A.TO_ADDRESS1= B.ADDRESS1, ";
        SQL4 += "A.TO_ADDRESS2 =B.ADDRESS2, ";
        SQL4 += "A.TO_PLACE= B.DISPATCH_STATION, ";
        SQL4 += "A.TO_STATE = B.STATE, ";
        SQL4 += "A.TO_PINCODE =B.PINCODE, ";
        SQL4 += "A.DISTANCE_LEVEL_KM = B.DISTANCE_KM, ";
        SQL4 += "A.TRANS_MODE ='Road', ";
        SQL4 += "A.CESS_AMOUNT =0, ";
        SQL4 += "A.TRANSPORTER_ID = B.TRANSPORTER_ID ";
        SQL4 += "WHERE A.PARTY_CODE = B.PARTY_CODE ";
        System.out.println("SQL4 = " + SQL4);
        data.Execute(SQL4);

        String SQL5 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT A ,DINESHMILLS.D_SAL_TRANSPORTER_MASTER B ";
        SQL5 += "SET A.TRANSPORTER_NAME = B.TRANSPORTER_NAME ,A.TRANSPORTER_GSTIN_ID = B.GSTIN_NO ";
        SQL5 += "WHERE A.TRANSPORTER_ID= B.TRANSPORTER_ID ";
        System.out.println("SQL5 = " + SQL5);
        data.Execute(SQL5);

        String SQL6 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT SET MAIN_ACCOUNT_CODE = CASE WHEN INVOICE_TYPE =1 THEN 210027 WHEN INVOICE_TYPE =2 THEN 210010 WHEN INVOICE_TYPE =3 THEN 210072 END ";
        System.out.println("SQL6 = " + SQL6);
        data.Execute(SQL6);

        String SQL7 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT SET TO_STATE ='ORISSA' WHERE TO_STATE ='ODISHA' ";
        System.out.println("SQL7 = " + SQL7);
        data.Execute(SQL7);

        String SQL8 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT SET CGST_PER =0 ,SGST_PER =0 WHERE IGST_AMOUNT > 0 ";
        System.out.println("SQL8 = " + SQL8);
        data.Execute(SQL8);

        String SQL9 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT SET IGST_PER =0 WHERE IGST_AMOUNT = 0";
        System.out.println("SQL9 = " + SQL9);
        data.Execute(SQL9);

        String SQL10 = "UPDATE DINESHMILLS.TMP_SAL_EWAY_REPORT SET TAX_RATE = CONCAT(SGST_PER,'+',CGST_PER,'+',IGST_PER,'+0')";
        System.out.println("SQL10 = " + SQL10);
        data.Execute(SQL10);

        String SQL11 = "UPDATE  DINESHMILLS.TMP_SAL_EWAY_REPORT E,FINANCE.D_SAL_GSTR_INVOICE_ERP  D ";
        SQL11 += "SET E.HSN = D.HSN_CODE, ";
        SQL11 += "E.ASSESSABLE_VALUE = ROUND(D.TAXABLE_VALUE,2) ";
        SQL11 += "WHERE D.INVOICE_NO = E.DOC_NO ";
        SQL11 += "AND D.INVOICE_DATE = E.DOC_DATE ";
        System.out.println("SQL11 = " + SQL11);
        data.Execute(SQL11);

        String SQL12 = "UPDATE DINESHMILLS.D_COM_HSN_SAC_MASTER H , DINESHMILLS.TMP_SAL_EWAY_REPORT E ";
        SQL12 += "SET PRODUCT = HSN_SAC_DESC, ";
        SQL12 += "DESCRIPTION = HSN_SAC_DESC ";
        SQL12 += "WHERE HSN_SAC_CODE = HSN ";
        SQL12 += "AND H.MAIN_ACCOUNT_CODE = E.MAIN_ACCOUNT_CODE ";
        System.out.println("SQL12 = " + SQL12);
        data.Execute(SQL12);

        String SQL13 = "UPDATE  DINESHMILLS.TMP_SAL_EWAY_REPORT E,PRODUCTION.FELT_SAL_INVOICE_HEADER H ";
        SQL13 += "SET UNIT = CASE WHEN RATE_UNIT ='MTR' THEN 'SQUARE METRE'  ELSE 'KILO GRAMS' END, ";
        SQL13 += "QTY = CASE WHEN RATE_UNIT ='MTR' THEN SQMTR  ELSE ACTUAL_WEIGHT  END ";
        SQL13 += "WHERE E.DOC_NO = H.INVOICE_NO ";
        SQL13 += "AND E.DOC_DATE = H.INVOICE_DATE ";
        SQL13 += "AND E.INVOICE_TYPE =2 ";
        System.out.println("SQL13 = " + SQL13);
        data.Execute(SQL13);

        String SQL14 = "UPDATE  DINESHMILLS.TMP_SAL_EWAY_REPORT E,DINESHMILLS.D_SAL_INVOICE_HEADER H ";
        SQL14 += "SET UNIT = 'NUMBERS/UNITS', ";
        SQL14 += "QTY = TOTAL_NET_QTY ";
        SQL14 += "WHERE E.DOC_NO = H.INVOICE_NO ";
        SQL14 += "AND E.DOC_DATE = H.INVOICE_DATE ";
        SQL14 += "AND E.INVOICE_TYPE =1 AND WAREHOUSE_CODE =4 ";
        System.out.println("SQL14 = " + SQL1);
        data.Execute(SQL14);

        String SQL15 = "UPDATE  DINESHMILLS.TMP_SAL_EWAY_REPORT E,DINESHMILLS.D_SAL_INVOICE_HEADER H ";
        SQL15 += "SET UNIT = 'METERS', ";
        SQL15 += "QTY = ROUND(TOTAL_NET_QTY,2) ";
        SQL15 += "WHERE E.DOC_NO = H.INVOICE_NO ";
        SQL15 += "AND E.DOC_DATE = H.INVOICE_DATE ";
        SQL15 += "AND E.INVOICE_TYPE =1 AND WAREHOUSE_CODE !=4 ";
        System.out.println("SQL15 = " + SQL15);
        data.Execute(SQL15);

        String SQL16 = "UPDATE  DINESHMILLS.TMP_SAL_EWAY_REPORT E,DINESHMILLS.D_SAL_INVOICE_HEADER H ";
        SQL16 += "SET UNIT = 'SQUARE METRE', ";
        SQL16 += "QTY = ROUND(TOTAL_SQ_MTR,2) ";
        SQL16 += "WHERE E.DOC_NO = H.INVOICE_NO ";
        SQL16 += "AND E.DOC_DATE = H.INVOICE_DATE ";
        SQL16 += "AND E.INVOICE_TYPE =3 ";
        System.out.println("SQL16 = " + SQL16);
        data.Execute(SQL16);

        String SQL17 = "INSERT INTO DINESHMILLS.D_SAL_EWAY_REPORT SELECT * FROM DINESHMILLS.TMP_SAL_EWAY_REPORT ";
        System.out.println("SQL17 = " + SQL17);
        data.Execute(SQL17);

        String sql = "SELECT @a:=@a+1 AS SR_NO,SUPPLY_TYPE,SUB_TYPE,DOC_TYPE,DOC_NO,DATE_FORMAT(DOC_DATE,'%d/%m/%Y') AS DOC_DATE,FROM_OTHER_PARTY_NAME,FROM_GSTIN,FROM_ADDRESS1,FROM_ADDRESS2,FROM_PLACE,FROM_PINCODE,"
                + "FROM_STATE,TO_OTHER_PARTY_NAME,TO_GSTIN,TO_ADDRESS1,TO_ADDRESS2,TO_PLACE,TO_PINCODE,TO_STATE,PRODUCT,DESCRIPTION,HSN,UNIT,QTY,ASSESSABLE_VALUE,"
                + "TAX_RATE,CGST_AMOUNT,SGST_AMOUNT,IGST_AMOUNT,CESS_AMOUNT,TRANS_MODE,DISTANCE_LEVEL_KM,TRANSPORTER_NAME,TRANSPORTER_GSTIN_ID,TRANSPORTER_DOC_NO,"
                + "CASE WHEN TRANSPORTER_DATE='0000-00-00' THEN '' ELSE TRANSPORTER_DATE END AS TRANSPORTER_DATE,VEHICLE_NO,ERROR_LIST "
                + " FROM (SELECT @a:=0) AS a,DINESHMILLS.D_SAL_EWAY_REPORT "
                + "WHERE DOC_DATE>=SUBDATE(CURDATE(),1) AND DOC_DATE <= SUBDATE(CURDATE(),1)  AND "
                + "INVOICE_TYPE=2 AND APPROVED=1 AND CANCELLED=0 "
                + "ORDER BY TRANSPORTER_ID,TRANSPORTER_NAME,DOC_DATE,DOC_NO";

        String recievers = "felts@dineshmills.com";

        String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(SUBDATE(CURDATE(),1),'%d/%m/%Y') FROM DUAL");
        String mdate1 = data.getStringValueFromDB("SELECT DATE_FORMAT(SUBDATE(CURDATE(),1),'%Y_%m_%d') FROM DUAL");
        exprt.fillData(sql, new File("/Email_Attachment/Felt_EWB_" + mdate1 + ".xls"), "EWB");

        String responce = sendNotificationMailWithAttachement("Felt EWB Data For Date:" + mdate, "Felt E-Way Bill Data for Date :" + mdate, recievers, "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com", "/Email_Attachment/Felt_EWB_" + mdate1 + ".xls", "Felt_EWB_" + mdate1 + ".xls");
        System.out.println("Mail Response:" + responce);

        /* closed on 20/11/2019
        
        sql = "SELECT @a:=@a+1 AS SR_NO,SUPPLY_TYPE,SUB_TYPE,DOC_TYPE,DOC_NO,DATE_FORMAT(DOC_DATE,'%d/%m/%Y') AS DOC_DATE,FROM_OTHER_PARTY_NAME,FROM_GSTIN,FROM_ADDRESS1,FROM_ADDRESS2,FROM_PLACE,FROM_PINCODE,"
                + "FROM_STATE,TO_OTHER_PARTY_NAME,TO_GSTIN,TO_ADDRESS1,TO_ADDRESS2,TO_PLACE,TO_PINCODE,TO_STATE,PRODUCT,DESCRIPTION,HSN,UNIT,QTY,ASSESSABLE_VALUE,"
                + "TAX_RATE,CGST_AMOUNT,SGST_AMOUNT,IGST_AMOUNT,CESS_AMOUNT,TRANS_MODE,DISTANCE_LEVEL_KM,TRANSPORTER_NAME,TRANSPORTER_GSTIN_ID,TRANSPORTER_DOC_NO,"
                + "CASE WHEN TRANSPORTER_DATE='0000-00-00' THEN '' ELSE TRANSPORTER_DATE END AS TRANSPORTER_DATE,VEHICLE_NO,ERROR_LIST "
                + " FROM (SELECT @a:=0) AS a,DINESHMILLS.D_SAL_EWAY_REPORT "
                + "WHERE DOC_DATE>=SUBDATE(CURDATE(),1) AND DOC_DATE <= SUBDATE(CURDATE(),1)  AND "
                + "INVOICE_TYPE=1 AND APPROVED=1 AND CANCELLED=0 "
                + "ORDER BY TRANSPORTER_ID,TRANSPORTER_NAME,DOC_DATE,DOC_NO";

        recievers = "atulshah@dineshmills.com,shitole@dineshmills.com,tusharjadhav@dineshmills.com";
        

        exprt.fillData(sql, new File("/Email_Attachment/Stg_EWB_" + mdate1 + ".xls"), "EWB");
        responce = sendNotificationMailWithAttachement("Stg EWB Data For Date:" + mdate, "Stg E-Way Bill Data for Date :" + mdate, recievers, "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com", "/Email_Attachment/Stg_EWB_" + mdate1 + ".xls", "Stg_EWB_" + mdate1 + ".xls");
        System.out.println("Mail Response:" + responce);

        sql = "SELECT @a:=@a+1 AS SR_NO,SUPPLY_TYPE,SUB_TYPE,DOC_TYPE,DOC_NO,DATE_FORMAT(DOC_DATE,'%d/%m/%Y') AS DOC_DATE,FROM_OTHER_PARTY_NAME,FROM_GSTIN,FROM_ADDRESS1,FROM_ADDRESS2,FROM_PLACE,FROM_PINCODE,"
                + "FROM_STATE,TO_OTHER_PARTY_NAME,TO_GSTIN,TO_ADDRESS1,TO_ADDRESS2,TO_PLACE,TO_PINCODE,TO_STATE,PRODUCT,DESCRIPTION,HSN,UNIT,QTY,ASSESSABLE_VALUE,"
                + "TAX_RATE,CGST_AMOUNT,SGST_AMOUNT,IGST_AMOUNT,CESS_AMOUNT,TRANS_MODE,DISTANCE_LEVEL_KM,TRANSPORTER_NAME,TRANSPORTER_GSTIN_ID,TRANSPORTER_DOC_NO,"
                + "CASE WHEN TRANSPORTER_DATE='0000-00-00' THEN '' ELSE TRANSPORTER_DATE END AS TRANSPORTER_DATE,VEHICLE_NO,ERROR_LIST "
                + " FROM (SELECT @a:=0) AS a,DINESHMILLS.D_SAL_EWAY_REPORT "
                + "WHERE DOC_DATE>=SUBDATE(CURDATE(),1) AND DOC_DATE <= SUBDATE(CURDATE(),1)  AND "
                + "INVOICE_TYPE=3 AND APPROVED=1 AND CANCELLED=0 "
                + "ORDER BY TRANSPORTER_ID,TRANSPORTER_NAME,DOC_DATE,DOC_NO";

        recievers = "sudhirpurohit@dineshmills.com,exportstg@dineshmills.com";

        exprt.fillData(sql, new File("/Email_Attachment/FF_EWB_" + mdate1 + ".xls"), "EWB");
        responce = sendNotificationMailWithAttachement("Filter Fabric EWB Data For Date:" + mdate, "Filter Fabric E-Way Bill Data for Date :" + mdate, recievers, "rishineekhra@dineshmills.com,sdmlerp@dineshmills.com", "/Email_Attachment/FF_EWB_" + mdate1 + ".xls", "FF_EWB_" + mdate1 + ".xls");
        System.out.println("Mail Response:" + responce);
                
        */        

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
