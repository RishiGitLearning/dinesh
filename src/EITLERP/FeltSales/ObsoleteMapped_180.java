/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class ObsoleteMapped_180 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Auto_Mapped_180();
    }

    private static void Auto_Mapped_180() {
        String sql = "", pDocNo = "", pDocDate = "";

        try {
            
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP "
                    + "WHERE UPN_ASSIGN_STATUS='MAPPED' AND COALESCE(MAPPING_DOC_NO,'') NOT IN ('') "
                    + "AND PIECE_STAGE NOT IN ('DIVERTED','INVOICED','EXP-INVOICE','SCRAP','DIVIDED','RETURN','JOINED') "
                    + "AND DATEDIFF(CURDATE(),ENTRY_DATE)>180 AND COALESCE(AUTO_MAPPED_IND,0)=0 ");
            rsData1.first();

            if (rsData1.getRow() > 0) {
                pDocNo = clsFirstFree.getNextFreeNo(2, 661, 363, true);
                pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
                int SrNo = 0;

                sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER "
                        + "(DOC_NO, DOC_DATE, DOCUMENT_NAME, REMARK, "
                        + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE, REJECTED, REJECTED_BY, "
                        + "REJECTED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, CANCELED, HIERARCHY_ID) "
                        + "VALUES('" + pDocNo + "', '" + pDocDate + "', '180 DAYS MAPPING ENTRY', 'AUTO GENERATED', "
                        + "338, '" + pDocDate + "', 0, '0000-00-00', 1, '" + pDocDate + "', 0, '', "
                        + "'0000-00-00', 0, '', '0000-00-00', 0, 4439)";
                System.out.println("Insert Into Header Data :" + sql);
                data.Execute(sql);

                sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_HEADER_H "
                        + "(REVISION_NO, DOC_NO, DOC_DATE, DOCUMENT_NAME, REMARK, "
                        + "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, CHANGED, CHANGED_DATE, REJECTED, REJECTED_BY, "
                        + "REJECTED_DATE, APPROVED, APPROVED_BY, APPROVED_DATE, CANCELED, HIERARCHY_ID, "
                        + "REJECTED_REMARKS, FROM_IP, UPDATED_BY, UPDATED_DATE, APPROVAL_STATUS, ENTRY_DATE, APPROVER_REMARKS) "
                        + "VALUES(1, '" + pDocNo + "', '" + pDocDate + "', '180 DAYS MAPPING ENTRY', 'AUTO GENERATED', "
                        + "338, '" + pDocDate + "', 0, '0000-00-00', 1, '" + pDocDate + "', 0, '', "
                        + "'0000-00-00', 0, '', '0000-00-00', 0, 4439, "
                        + "'', '200.0.0.227', 338, '" + pDocDate + "', 'W', '" + pDocDate + "', '')";
                System.out.println("Insert Into Header History Data :" + sql);
                data.Execute(sql);

                while (!rsData1.isAfterLast()) {

                    String pPieceNo = rsData1.getString("PIECE_NO");
                    String pPartyCode = rsData1.getString("PARTY_CODE");
                    String pMapDocNo = rsData1.getString("MAPPING_DOC_NO");
                    String pPieceStage = rsData1.getString("PIECE_STAGE");
                    SrNo = SrNo + 1;

                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL "
                            + "(SR_NO, DOC_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                            + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                            + "REMARK, PIECE_STAGE, UPN_ASSIGN_STATUS) "
                            + "SELECT " + SrNo + ", '" + pDocNo + "', PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                            + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                            + "'NOT DIVERTED SINCE 180 DAYS', '" + pPieceStage + "', UPN_ASSIGN_STATUS "
                            + "FROM PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL "
                            + "WHERE DOC_NO='" + pMapDocNo + "' AND PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "' ";
                    System.out.println("Insert Into Detail Data :" + sql);
                    data.Execute(sql);

                    sql = "INSERT INTO PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL_H "
                            + "(REVISION_NO, SR_NO, DOC_NO, PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                            + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                            + "REMARK, UPN_ASSIGN_STATUS) "
                            + "SELECT 1, " + SrNo + ", '" + pDocNo + "', PIECE_NO, PARTY_CODE, PARTY_NAME, UPN, DIVISION_POSSIBILITY, "
                            + "UPN1, UPN2, UPN3, UPN4, UPN5, UPN6, UPN7, UPN8, UPN9, UPN10, UPN11, UPN12, UPN13, UPN14, UPN15, UPN16, UPN17, UPN18, UPN19, UPN20, "
                            + "'NOT DIVERTED SINCE 180 DAYS', UPN_ASSIGN_STATUS "
                            + "FROM PRODUCTION.FELT_OBSOLETE_MAPPING_DETAIL "
                            + "WHERE DOC_NO='" + pMapDocNo + "' AND PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "' ";
                    System.out.println("Insert Into Detail History Data :" + sql);
                    data.Execute(sql);
                    
                    data.Execute("UPDATE PRODUCTION.OBSOLETE_MAPPED_UNMAPPED_SCRAP SET AUTO_MAPPED_IND=1 "
                            + "WHERE MAPPING_DOC_NO='" + pMapDocNo + "' AND PIECE_NO='" + pPieceNo + "' AND PARTY_CODE='" + pPartyCode + "' ");

                    rsData1.next();
                }

                sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                        + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,"
                        + "TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,"
                        + "ACTION_DATE,CHANGED,CHANGED_DATE) "
                        + "SELECT 661,'" + pDocNo + "','" + pDocDate + "',USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, "
                        + "CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'180 DAYS MAPPING ENTRY',SR_NO,0,'','" + pDocDate + "',"
                        + "'0000-00-00',1,'" + pDocDate + "'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS  WHERE HIERARCHY_ID = 4439 ";
                System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                data.Execute(sql);
            }
        } catch (Exception e) {
            System.out.println("Error while Saving : " + e.getMessage());
        }
    }
}
