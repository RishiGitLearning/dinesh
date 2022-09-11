/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.clsFirstFree;
import EITLERP.data;
import java.io.File;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class ObsoleteUnMapped_180 {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        Auto_UnMapped_180();
    }

    private static void Auto_UnMapped_180() {
        String sql = "", pDocNo = "", pDocDate = "";

        try {

            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                    + "WHERE UPN_ASSIGN_STATUS='UNMAPPED' AND COALESCE(MAPPING_DOC_NO,'') NOT IN ('') "
                    + "AND PIECE_STAGE NOT IN ('DIVERTED','INVOICED','EXP-INVOICE','SCRAP','DIVIDED','RETURN','JOINED','CANCELED') "
                    + "AND DATEDIFF(CURDATE(),ENTRY_DATE)>180 AND COALESCE(AUTO_UNMAPPED_IND,0)=6 GROUP BY PIECE_NO ");

            data.Execute("DROP TABLE PRODUCTION.TEMP_OMUS");
            data.Execute("CREATE TABLE PRODUCTION.TEMP_OMUS "
                    + "SELECT * FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                    + "WHERE UPN_ASSIGN_STATUS='UNMAPPED' AND COALESCE(MAPPING_DOC_NO,'') NOT IN ('') "
                    + "AND PIECE_STAGE NOT IN ('DIVERTED','INVOICED','EXP-INVOICE','SCRAP','DIVIDED','RETURN','JOINED','CANCELED') "
                    + "AND DATEDIFF(CURDATE(),ENTRY_DATE)>180 AND COALESCE(AUTO_UNMAPPED_IND,0)=6 GROUP BY PIECE_NO ");

            rsData1.first();

            if (rsData1.getRow() > 0) {
                String curDt = data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d-%m-%Y') AS DATE FROM DUAL");
                while (!rsData1.isAfterLast()) {
                    pDocNo = clsFirstFree.getNextFreeNo(2, 662, 364, true);
                    pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");

                    String pPieceNo = rsData1.getString("PIECE_NO");
                    String pRejReason = "UNMAPPED SINCE 180 DAYS";
                    String rejDocNo = rsData1.getString("MAPPING_DOC_NO");
                    String rejDocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER WHERE DOC_NO='" + rejDocNo + "' ");

                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_SCRAP "
                            + "(DOC_NO, DOC_DATE, PIECE_NO, REF_ENTRY_FORM, "
                            + "REF_DOC_NO, REF_DOC_DATE, REF_SCRAP_REASON, "
                            + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, APPROVED, APPROVED_DATE, REJECTED, REJECTED_DATE, CANCELED, "
                            + "HIERARCHY_ID, CHANGED, CHANGED_DATE) "
                            + "VALUES('" + pDocNo + "', '" + pDocDate + "', '" + pPieceNo + "', 'AUTO GENERATED', "
                            + "'" + rejDocNo + "', '" + rejDocDate + "', '" + pRejReason + "', "
                            + "338, '" + pDocDate + "', 0, '0000-00-00', 0, '0000-00-00', 0, '0000-00-00', 0, "
                            + "4435, 1, '" + pDocDate + "')";
                    System.out.println("Insert Into Obsolete Piece Scrap :" + sql);
                    data.Execute(sql);

                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_SCRAP_H "
                            + "(REVISION_NO, UPDATED_BY, ENTRY_DATE, APPROVAL_STATUS, APPROVER_REMARKS, "
                            + "DOC_NO, DOC_DATE, PIECE_NO, REF_ENTRY_FORM, "
                            + "REF_DOC_NO, REF_DOC_DATE, REF_SCRAP_REASON, "
                            + "HIERARCHY_ID, CHANGED, CHANGED_DATE) "
                            + "VALUES (1, 338, '" + pDocDate + "', 'W', '', "
                            + "'" + pDocNo + "', '" + pDocDate + "', '" + pPieceNo + "', 'AUTO GENERATED', "
                            + "'" + rejDocNo + "', '" + rejDocDate + "', '" + pRejReason + "', "
                            + "4435, 1, '" + pDocDate + "')";
                    System.out.println("Insert Into History of Obsolete Piece Scrap :" + sql);
                    data.Execute(sql);

                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,"
                            + "TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,"
                            + "ACTION_DATE,CHANGED,CHANGED_DATE) "
                            + "SELECT 662,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, "
                            + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'FROM GOODS RETURN',SR_NO,0,'','" + pDocDate + "',"
                            + "'0000-00-00',1,'" + pDocDate + "'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS  WHERE HIERARCHY_ID = 4435 ";
                    System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                    data.Execute(sql);

                    data.Execute("UPDATE PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                            + "SET AUTO_UNMAPPED_IND=7,MAPPING_DOC_NO=CONCAT(COALESCE(MAPPING_DOC_NO,''),'-AUTO SCRAP ENTRY AFTER 180') "
                            + "WHERE PIECE_NO='" + pPieceNo + "' ");

                    rsData1.next();
                }

                String pMessage = "Respected Sir, <br><br> Auto entry in Obsolete Piece Scrap after being in Unmapped more than 180 days. <br> Please find attached document of piece details herewith. <br><br>";

                String recievers = "abtewary@dineshmills.com,amitkanti@dineshmills.com,yrpatel@dineshmills.com,brdfltdesign@dineshmills.com,vdshanbhag@dineshmills.com,soumen@dineshmills.com,feltpp@dineshmills.com,feltwh@dineshmills.com";
//                String recievers = "rishineekhra@dineshmills.com";

                String pCC = "aditya@dineshmills.com,sdmlerp@dineshmills.com";
//                String pCC = "sdmlerp@dineshmills.com";

                String sqlMail = "SELECT PIECE_NO, PARTY_CODE, PIECE_STAGE, ENTRY_DATE, MAPPING_DOC_NO, STYLE, PROD_GROUP, GSM, SYN_PER FROM PRODUCTION.TEMP_OMUS ORDER BY PARTY_CODE,PIECE_NO";

                exprt.fillData(sqlMail, new File("/Email_Attachment/UnMapped_Scraping_after180days_" + curDt + ".xls"), "ScrapingPieceList");

                String responce = sendNotificationMailWithAttachement("Scrap list of Unmapped after 180 days", pMessage, recievers, pCC, "/Email_Attachment/UnMapped_Scraping_after180days_" + curDt + ".xls", "UnMapped_Scraping_after180days_" + curDt + ".xls");
                System.out.println("Mail Response:" + responce);
            }
        } catch (Exception e) {
            System.out.println("Error while Saving : " + e.getMessage());
        }
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
}
