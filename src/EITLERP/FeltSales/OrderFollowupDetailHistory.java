/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author root
 */
public class OrderFollowupDetailHistory {

    public static void main(String[] args) {
        runQuery();
    }

    private static void runQuery() {

        data.Execute("INSERT INTO PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL_HISTORY "
                + "(ENTRY_DATETIME, DOC_NO, SR_NO, DOC_MONTH, DOC_YEAR, SELECT_IND, ORDER_STATUS, UPN, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "PARTY_CODE, PARTY_NAME, CPRS_MONTH, CPRS_MONTH_DATE, OPRS_MONTH, OPRS_MONTH_DATE, INCHARGE, UNABLE_TO_CONTACT, "
                + "DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, CONTACTED_NO, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, "
                + "NEW_FOLLOW_UP_DATE, DELAY_REASON, FOLLOW_UP_DATE, ASSIGNED_CATEGORY, CPRS_QUARTER, CPRS_OPEN_DATE, CPRS_CLOSE_DATE, "
                + "PRODUCT_CATEGORY, SELECTED_DIVERSION_PIECE, EXPECTED_CPRS, EXPECTED_CPRS_REASON) "
                + "SELECT NOW(), DOC_NO, SR_NO, DOC_MONTH, DOC_YEAR, SELECT_IND, ORDER_STATUS, UPN, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "PARTY_CODE, PARTY_NAME, CPRS_MONTH, CPRS_MONTH_DATE, OPRS_MONTH, OPRS_MONTH_DATE, INCHARGE, UNABLE_TO_CONTACT, "
                + "DATE_OF_COMMUNICATION, MODE_OF_COMMUNICATION, CONTACT_PERSON, CONTACTED_NO, PARTY_JUSTIFICATION, AREA_MANAGER_COMMENT, "
                + "NEW_FOLLOW_UP_DATE, DELAY_REASON, FOLLOW_UP_DATE, ASSIGNED_CATEGORY, CPRS_QUARTER, CPRS_OPEN_DATE, CPRS_CLOSE_DATE, "
                + "PRODUCT_CATEGORY, SELECTED_DIVERSION_PIECE, EXPECTED_CPRS, EXPECTED_CPRS_REASON "
                + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL ");
//                + "WHERE NEW_FOLLOW_UP_DATE<=CURDATE() ");

        data.Execute("DELETE FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                + "WHERE DUMMY_PIECE_NO IN (SELECT DUMMY_PIECE_NO FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER WHERE OPR_STATUS='DELETE') ");

        data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                + "SET FOLLOW_UP_DATE = NEW_FOLLOW_UP_DATE, CPRS_OPEN_DATE = NEW_FOLLOW_UP_DATE "
                + "WHERE COALESCE(NEW_FOLLOW_UP_DATE,'0000-00-00') != '0000-00-00' AND NEW_FOLLOW_UP_DATE>CURDATE() ");

        data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                + " SET AREA_MANAGER_COMMENT='', PARTY_JUSTIFICATION='', CONTACTED_NO='', CONTACT_PERSON='', MODE_OF_COMMUNICATION='', "
                + " DATE_OF_COMMUNICATION='0000-00-00', UNABLE_TO_CONTACT='', DELAY_REASON='', ASSIGNED_PIECE_NO='', "
                + " NEW_FOLLOW_UP_DATE='0000-00-00' ");
//                + " WHERE MONTH(NEW_FOLLOW_UP_DATE)=MONTH(CURDATE()) AND YEAR(NEW_FOLLOW_UP_DATE)=YEAR(CURDATE()) ");//AND CUR_PIECE_STAGE IN ('IN STOCK','BSR') 

        data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                + " SET FOLLOW_UP_DATE = DATE_ADD(CURDATE(), INTERVAL 1 DAY) "
                + " WHERE COALESCE(FOLLOW_UP_DATE,'0000-00-00') <= CURDATE() ");

        data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
                + " SET FOLLOW_UP_DATE = CPRS_OPEN_DATE "
                + " WHERE COALESCE(FOLLOW_UP_DATE,'0000-00-00') < CPRS_OPEN_DATE ");

        data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL OFP, PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER OPR "
                + "SET OFP.ASSIGNED_PIECE_NO = OPR.ASSIGNED_PIECE_NO "
                + "WHERE OFP.UPN = OPR.UPN AND OFP.DUMMY_PIECE_NO=OPR.DUMMY_PIECE_NO ");

//        try {
//            Connection Conn1;
//            Statement stmt1;
//            ResultSet rsData1;
//            Conn1 = data.getConn();
//            stmt1 = Conn1.createStatement();
//            rsData1 = stmt1.executeQuery("SELECT UPN,CPRS_MONTH,COUNT(CPRS_MONTH) AS CPRS_CNT,EXPECTED_CPRS,COUNT(EXPECTED_CPRS) AS EXPECTED_CNT "
//                    + "FROM PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL WHERE CPRS_MONTH!=EXPECTED_CPRS GROUP BY UPN,CPRS_MONTH,EXPECTED_CPRS");
//            rsData1.first();
//
//            if (rsData1.getRow() > 0) {
//                while (!rsData1.isAfterLast()) {
//                    String pUPN = rsData1.getString("UPN");
//                    String pCPRS = rsData1.getString("CPRS_MONTH");
//                    String pCPRSCnt = rsData1.getString("CPRS_CNT");
//                    String pExpected = rsData1.getString("EXPECTED_CPRS");
//                    String pExpectedCnt = rsData1.getString("EXPECTED_CNT");
//
//                    data.Execute("UPDATE PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
//                            + "SET " + pExpected.substring(0, 3).toUpperCase() + "_BUDGET=(COALESCE(" + pExpected.substring(0, 3).toUpperCase() + "_BUDGET,0) + " + pExpectedCnt + ") , "
//                            + " " + pCPRS.substring(0, 3).toUpperCase() + "_BUDGET=(COALESCE(" + pCPRS.substring(0, 3).toUpperCase() + "_BUDGET,0) - " + pCPRSCnt + ")  "
//                            + "WHERE UPN='" + pUPN + "' AND DOC_NO LIKE ('N2223%') ");
//
//                    data.Execute("UPDATE PRODUCTION.FELT_ORDER_FOLLOWUP_DETAIL "
//                            + " SET CPRS_MONTH=EXPECTED_CPRS "
//                            + " WHERE UPN='" + pUPN + "' AND CPRS_MONTH='" + pCPRS + "' AND EXPECTED_CPRS='" + pExpected + "' ");
//
//                    rsData1.next();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
