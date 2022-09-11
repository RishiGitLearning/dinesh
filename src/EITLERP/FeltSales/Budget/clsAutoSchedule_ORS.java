/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Budget;

import EITLERP.FeltSales.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class clsAutoSchedule_ORS {

    public static void main(String[] args) {
        try {
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
//            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='2022' AND DOC_NO LIKE 'N2223%'");
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='2022' AND DOC_NO LIKE 'B2223%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");
            rsData1.first();

//            int rCount = data.getIntValueFromDB("SELECT COUNT(*) AS R_COUNT FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='2022' AND DOC_NO LIKE 'N2223%'");
            int rCount = data.getIntValueFromDB("SELECT COUNT(*) AS R_COUNT FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='2022' AND DOC_NO LIKE 'B2223%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");

            if (rsData1.getRow() > 0) {
                System.out.println("Process Start on : " + EITLERPGLOBAL.getCurrentDateTimeDB());
                int srNo = 0;
                while (!rsData1.isAfterLast()) {
                    srNo++;
                    String pUPN = rsData1.getString("UPN");
                    AutoScheduling(2022, pUPN);
//                    System.out.println("UPN : " + pUPN + " process done. Total " + srNo + " out of " + rCount);
                    rsData1.next();
                }
                System.out.println("Process End on : " + EITLERPGLOBAL.getCurrentDateTimeDB());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void AutoScheduling(int finyear, String upn) {

        String SQL;
        HashMap mmonth, emonth;
        mmonth = new HashMap();
        mmonth.put(1, "JAN");
        mmonth.put(2, "FEB");
        mmonth.put(3, "MAR");
        mmonth.put(4, "APR");
        mmonth.put(5, "MAY");
        mmonth.put(6, "JUN");
        mmonth.put(7, "JUL");
        mmonth.put(8, "AUG");
        mmonth.put(9, "SEP");
        mmonth.put(10, "OCT");
        mmonth.put(11, "NOV");
        mmonth.put(12, "DEC");

        emonth = new HashMap();
        emonth.put(1, "Jan");
        emonth.put(2, "Feb");
        emonth.put(3, "Mar");
        emonth.put(4, "Apr");
        emonth.put(5, "May");
        emonth.put(6, "Jun");
        emonth.put(7, "Jul");
        emonth.put(8, "Aug");
        emonth.put(9, "Sep");
        emonth.put(10, "Oct");
        emonth.put(11, "Nov");
        emonth.put(12, "Dec");

        int tSchedule, tQty;
        for (int i = 4; i <= 12; i++) {
            tSchedule = tQty = 0;

            tSchedule = data.getIntValueFromDB("SELECT SUM(" + mmonth.get(i).toString() + "_BUDGET) FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    //                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ");
                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");
            tQty = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                    + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' "
                    + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + finyear + "' AND OPR_STATUS='INSERT' ");

            if (tSchedule > tQty) {
                //
                int vMaxDumNo = 0;
                String dSql = "";
                vMaxDumNo = data.getIntValueFromDB("SELECT MAX(RIGHT(DUMMY_PIECE_NO,5)) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER WHERE FIN_FROM_YEAR='" + finyear + "' ");
                for (int j = 0; j < (tSchedule - tQty); j++) {
                    vMaxDumNo++;
                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "(FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, OPR_STATUS, INSERT_DATE, DELETE_DATE, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE) "
                            + "SELECT YEAR_FROM, YEAR_TO, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, CONCAT('DUM',RIGHT(YEAR_FROM,2),RIGHT(YEAR_TO,2),LPAD(" + vMaxDumNo + ",5,0)) AS D_NO, 'INSERT', CURDATE(), '0000-00-00', "
                            + "'" + emonth.get(i).toString() + " - " + finyear + "', LAST_DAY('" + finyear + "-" + i + "-01'), "
                            + "'" + emonth.get(i).toString() + " - " + finyear + "', LAST_DAY('" + finyear + "-" + i + "-01') "
                            //                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ";
                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
                    data.Execute(dSql);

                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                            + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, OPR_STATUS, INSERT_DATE, DELETE_DATE, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE) "
                            + "SELECT NOW(), YEAR_FROM, YEAR_TO, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, CONCAT('DUM',RIGHT(YEAR_FROM,2),RIGHT(YEAR_TO,2),LPAD(" + vMaxDumNo + ",5,0)) AS D_NO, 'INSERT', CURDATE(), '0000-00-00', "
                            + "'" + emonth.get(i).toString() + " - " + finyear + "', LAST_DAY('" + finyear + "-" + i + "-01'), "
                            + "'" + emonth.get(i).toString() + " - " + finyear + "', LAST_DAY('" + finyear + "-" + i + "-01') "
                            //                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ";
                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
                    data.Execute(dSql);
                }
            }

            if (tSchedule < tQty) {
                //
                String vDumNo = "";
                String dSql = "";
                for (int j = 0; j < (tQty - tSchedule); j++) {
                    vDumNo = data.getStringValueFromDB("SELECT MAX(DUMMY_PIECE_NO) AS DUMMY FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND OPR_STATUS='INSERT' "
                            + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + finyear + "' ");

                    dSql = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "SET OPR_STATUS='DELETE', DELETE_DATE=CURDATE() "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND OPR_STATUS='INSERT' AND DUMMY_PIECE_NO='" + vDumNo + "' ";
                    data.Execute(dSql);

                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                            + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE) "
                            + "SELECT NOW(), FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE "
                            + "FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND DUMMY_PIECE_NO='" + vDumNo + "' ";
                    data.Execute(dSql);
                }
            }
        }
        for (int i = 1; i <= 3; i++) {
            tSchedule = tQty = 0;

            tSchedule = data.getIntValueFromDB("SELECT SUM(" + mmonth.get(i).toString() + "_BUDGET) FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                    //                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ");
                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");
            tQty = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                    + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' "
                    + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + (finyear + 1) + "' AND OPR_STATUS='INSERT' ");

            if (tSchedule > tQty) {
                //
                int vMaxDumNo = 0;
                String dSql = "";
                vMaxDumNo = data.getIntValueFromDB("SELECT MAX(RIGHT(DUMMY_PIECE_NO,5)) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER WHERE FIN_FROM_YEAR='" + finyear + "' ");
                for (int j = 0; j < (tSchedule - tQty); j++) {
                    vMaxDumNo++;
                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "(FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, OPR_STATUS, INSERT_DATE, DELETE_DATE, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE) "
                            + "SELECT YEAR_FROM, YEAR_TO, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, CONCAT('DUM',RIGHT(YEAR_FROM,2),RIGHT(YEAR_TO,2),LPAD(" + vMaxDumNo + ",5,0)) AS D_NO, 'INSERT', CURDATE(), '0000-00-00', "
                            + "'" + emonth.get(i).toString() + " - " + (finyear + 1) + "', LAST_DAY('" + (finyear + 1) + "-" + i + "-01'), "
                            + "'" + emonth.get(i).toString() + " - " + (finyear + 1) + "', LAST_DAY('" + (finyear + 1) + "-" + i + "-01') "
                            //                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ";
                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
                    data.Execute(dSql);

                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                            + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, OPR_STATUS, INSERT_DATE, DELETE_DATE, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE) "
                            + "SELECT NOW(), YEAR_FROM, YEAR_TO, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, CONCAT('DUM',RIGHT(YEAR_FROM,2),RIGHT(YEAR_TO,2),LPAD(" + vMaxDumNo + ",5,0)) AS D_NO, 'INSERT', CURDATE(), '0000-00-00', "
                            + "'" + emonth.get(i).toString() + " - " + (finyear + 1) + "', LAST_DAY('" + (finyear + 1) + "-" + i + "-01'), "
                            + "'" + emonth.get(i).toString() + " - " + (finyear + 1) + "', LAST_DAY('" + (finyear + 1) + "-" + i + "-01') "
                            //                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ";
                            + "FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ";
                    data.Execute(dSql);
                }
            }

            if (tSchedule < tQty) {
                //
                String vDumNo = "";
                String dSql = "";
                for (int j = 0; j < (tQty - tSchedule); j++) {
                    vDumNo = data.getStringValueFromDB("SELECT MAX(DUMMY_PIECE_NO) AS DUMMY FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND OPR_STATUS='INSERT' "
                            + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + (finyear + 1) + "' ");

                    dSql = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "SET OPR_STATUS='DELETE', DELETE_DATE=CURDATE() "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND OPR_STATUS='INSERT' AND DUMMY_PIECE_NO='" + vDumNo + "' ";
                    data.Execute(dSql);

                    dSql = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                            + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE) "
                            + "SELECT NOW(), FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                            + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                            + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE "
                            + "FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                            + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' AND DUMMY_PIECE_NO='" + vDumNo + "' ";
                    data.Execute(dSql);
                }
            }
        }

        SQL = "DROP TABLE PRODUCTION.TEMP_FELT_ORS_PR_PIECES_" + upn;
        data.Execute(SQL);

        SQL = "CREATE TABLE PRODUCTION.TEMP_FELT_ORS_PR_PIECES_" + upn + " "
                + "SELECT * FROM ( "
                + "SELECT *,COALESCE(LAST_DAY(STR_TO_DATE(CONCAT('00 - ',ORS_MONTH),'%d-%b-%Y')),'0000-00-00') AS ORS_LAST_DATE,"
                + "" + finyear + " AS FIN_FROM_YEAR, " + (finyear + 1) + " AS FIN_TO_YEAR, '                        ' AS DUMMY_PIECE_NO FROM ( "
                + "SELECT PR_UPN, PR_PARTY_CODE, PR_MACHINE_NO, PR_POSITION_NO, PR_PIECE_NO, PR_PIECE_STAGE, "
                + "CASE WHEN PR_PIECE_STAGE='BSR' THEN PR_PACKED_DATE WHEN PR_PIECE_STAGE='IN STOCK' THEN PR_FNSG_DATE WHEN PR_PIECE_STAGE='DIVERTED_FNSG_STOCK' THEN PR_DFS_IN_DATE "
                + "WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP NOT IN ('HDS','SDF') THEN PR_NDL_DATE WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP IN ('HDS') THEN PR_SEAM_DATE WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP IN ('SDF') THEN PR_SDF_ASSEMBLED_DATE "
                + "WHEN PR_PIECE_STAGE='NEEDLING' THEN PR_MND_DATE WHEN PR_PIECE_STAGE='SEAMING' THEN PR_MND_DATE WHEN PR_PIECE_STAGE='HEAT_SETTING' THEN PR_MND_DATE WHEN PR_PIECE_STAGE='MENDING' THEN PR_WVG_DATE "
                + "WHEN PR_PIECE_STAGE='WEAVING' THEN PR_WARP_DATE WHEN PR_PIECE_STAGE='ASSEMBLY' THEN PR_SDF_SPIRALED_DATE WHEN PR_PIECE_STAGE='SPIRALLING' THEN PR_SDF_INSTRUCT_DATE "
                + "WHEN PR_PIECE_STAGE='PLANNING' THEN PR_OC_LAST_DDMMYY WHEN PR_PIECE_STAGE='BOOKING' THEN PR_REQ_MTH_LAST_DDMMYY END AS PR_STAGE_DATE, "
                + "CASE WHEN PR_PIECE_STAGE='BSR' THEN 1 WHEN PR_PIECE_STAGE='IN STOCK' THEN 2 WHEN PR_PIECE_STAGE='DIVERTED_FNSG_STOCK' THEN 3 "
                + "WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP NOT IN ('HDS','SDF') THEN 4 WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP IN ('HDS') THEN 4 WHEN PR_PIECE_STAGE='FINISHING' AND PR_GROUP IN ('SDF') THEN 4 "
                + "WHEN PR_PIECE_STAGE='NEEDLING' THEN 5 WHEN PR_PIECE_STAGE='SEAMING' THEN 6 WHEN PR_PIECE_STAGE='HEAT_SETTING' THEN 6 WHEN PR_PIECE_STAGE='MENDING' THEN 7 "
                + "WHEN PR_PIECE_STAGE='WEAVING' THEN 8 WHEN PR_PIECE_STAGE='ASSEMBLY' THEN 9 WHEN PR_PIECE_STAGE='SPIRALLING' THEN 10 "
                + "WHEN PR_PIECE_STAGE='PLANNING' THEN 11 WHEN PR_PIECE_STAGE='BOOKING' THEN 12 END AS PRIORITY, "
                + "CASE WHEN COALESCE(PR_CURRENT_SCH_LAST_DDMMYY,'0000-00-00')!='0000-00-00' THEN PR_CURRENT_SCH_MONTH WHEN COALESCE(PR_OC_LAST_DDMMYY,'0000-00-00')!='0000-00-00' THEN PR_OC_MONTHYEAR WHEN  COALESCE(PR_REQ_MTH_LAST_DDMMYY,'0000-00-00')!='0000-00-00' THEN PR_REQUESTED_MONTH END AS ORS_MONTH, "
                + "PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER  "
                + "WHERE PR_PIECE_STAGE IN ('BOOKING','PLANNING','WEAVING','MENDING','NEEDLING','SEAMING','ASSEMBLY','SPIRALLING','FINISHING','IN STOCK','BSR','DIVERTED_FNSG_STOCK','HEAT_SETTING','MARKING','SPLICING') "
                + "AND COALESCE(PR_DELINK,'')!='OBSOLETE' AND COALESCE(PR_REJECTED_FLAG,0)=0 AND PR_UPN='" + upn + "' "
                + "AND COALESCE(PR_HOLD_FLAG,'')!='HOLD' "
                + " "
                + "UNION ALL "
                + " "
                + "SELECT PR_UPN, PR_PARTY_CODE, PR_MACHINE_NO, PR_POSITION_NO, PR_PIECE_NO, PR_PIECE_STAGE, "
                + "PR_INVOICE_DATE AS PR_STAGE_DATE, "
                + "0 AS PRIORITY, "
                + "CONCAT(DATE_FORMAT(PR_INVOICE_DATE,'%b'),' - ',YEAR(PR_INVOICE_DATE)) AS ORS_MONTH, "
                + "PR_REQUESTED_MONTH, PR_REQ_MTH_LAST_DDMMYY, PR_OC_MONTHYEAR, PR_OC_LAST_DDMMYY, PR_CURRENT_SCH_MONTH, PR_CURRENT_SCH_LAST_DDMMYY "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                + "WHERE PR_PIECE_STAGE IN ('INVOICED','EXP-INVOICE') "
                + "AND PR_INVOICE_DATE >='" + finyear + "-04-01' AND PR_INVOICE_DATE <='" + (finyear + 1) + "-03-31' "
                + "AND PR_UPN='" + upn + "' "
                + ""
                + ""
                //                + "AND PR_CURRENT_SCH_LAST_DDMMYY>='" + finyear + "-04-01' "
                + ") A "
                + "WHERE COALESCE(ORS_MONTH,'')!=''  "
                + ") B "
                + "WHERE ORS_LAST_DATE>='" + finyear + "-04-01' "
                + "ORDER BY PR_UPN ";
        data.Execute(SQL);

        SQL = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                + "SET ASSIGNED_PIECE_NO='', "
                //                + "PLANNED_REQUIREMENT_SCHEDULE='', PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE='0000-00-00', "
                + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE='', CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE='0000-00-00', "
                + "ORDER_REQUIREMENT_SCHEDULE='', ORDER_REQUIREMENT_SCHEDULE_LAST_DATE='0000-00-00', "
                + "FUP_CPRS_OPEN_DATE='0000-00-00', FUP_CPRS_CLOSE_DATE='0000-00-00' "
                + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' AND PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE>='" + finyear + "-04-01' "
                + "AND COALESCE(OPR_STATUS,'')!='DELETE' ";
        data.Execute(SQL);

        int bqty, pcqty;

        for (int i = 4; i <= 12; i++) {
//            if (i >= EITLERPGLOBAL.getCurrentMonth() && EITLERPGLOBAL.getCurrentMonth() >= 4) {

                bqty = pcqty = 0;
                bqty = data.getIntValueFromDB("SELECT " + mmonth.get(i).toString() + "_BUDGET FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                        //                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ");
                        + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");
                pcqty = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                        + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' "
                        + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + finyear + "' AND OPR_STATUS='INSERT' ");

                SQL = "UPDATE "
                        + "PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER Z, "
                        + "(SELECT @a := @a+1 as S_SR_NO,X.* FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER AS X,(SELECT @a:= 0) AS a "
                        + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' AND COALESCE(ASSIGNED_PIECE_NO,'')='' AND OPR_STATUS='INSERT' "
                        + "ORDER BY UPN,PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE,DUMMY_PIECE_NO "
                        + ") A, "
                        + "(SELECT @b := @b+1 as P_SR_NO,X.*  FROM PRODUCTION.TEMP_FELT_ORS_PR_PIECES_" + upn + " X,(SELECT @b:= 0) AS b "
                        //                    + "WHERE PR_OC_LAST_DDMMYY<=LAST_DAY('" + finyear + "-" + i + "-01') "
                        + "WHERE 1=1 "
                        + "AND ORS_LAST_DATE>='" + finyear + "-04-01' AND ORS_LAST_DATE<='" + (finyear + 1) + "-03-31' "
                        + "AND PR_PIECE_NO NOT IN (SELECT ASSIGNED_PIECE_NO FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                        + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' AND COALESCE(OPR_STATUS,'')='INSERT' ) "
                        + "ORDER BY PRIORITY,PR_PIECE_NO "
                        + "LIMIT " + bqty + " "
                        + ") B "
                        + "SET Z.ASSIGNED_PIECE_NO=PR_PIECE_NO, "
                        //                    + "Z.ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_REQUESTED_MONTH,'')='' THEN PR_OC_MONTHYEAR ELSE PR_REQUESTED_MONTH END, "
                        //                    + "Z.ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_REQUESTED_MONTH,'')='' THEN PR_OC_LAST_DDMMYY ELSE PR_REQ_MTH_LAST_DDMMYY END, "
                        //                    + "Z.PLANNED_REQUIREMENT_SCHEDULE=DATE_FORMAT('" + finyear + "-" + i + "-01', '%b - %Y'), Z.PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=LAST_DAY('" + finyear + "-" + i + "-01'), "
                        //                    + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE=PR_CURRENT_SCH_MONTH, Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=PR_CURRENT_SCH_LAST_DDMMYY, "
                        //                    + "Z.ORDER_REQUIREMENT_SCHEDULE=PR_OC_MONTHYEAR, Z.ORDER_REQUIREMENT_SCHEDULE_LAST_DATE=PR_OC_LAST_DDMMYY "
                        + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_CURRENT_SCH_MONTH,'')='' THEN PR_REQUESTED_MONTH ELSE PR_CURRENT_SCH_MONTH END , "
                        + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_CURRENT_SCH_MONTH,'')='' THEN PR_REQ_MTH_LAST_DDMMYY ELSE PR_CURRENT_SCH_LAST_DDMMYY END , "
                        + "Z.ORDER_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_OC_MONTHYEAR,'')='' THEN PR_REQUESTED_MONTH ELSE PR_OC_MONTHYEAR END , "
                        + "Z.ORDER_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_OC_MONTHYEAR,'')='' THEN PR_REQ_MTH_LAST_DDMMYY ELSE PR_OC_LAST_DDMMYY END "
                        + "WHERE A.FIN_FROM_YEAR=" + finyear + " AND A.UPN=B.PR_UPN AND P_SR_NO=S_SR_NO "
                        + "AND A.UPN=Z.UPN AND A.DUMMY_PIECE_NO=Z.DUMMY_PIECE_NO AND A.FIN_FROM_YEAR=Z.FIN_FROM_YEAR ";
                data.Execute(SQL);
//            } else {
//            }
        }
        for (int i = 1; i <= 3; i++) {
//            if (i >= EITLERPGLOBAL.getCurrentMonth() && EITLERPGLOBAL.getCurrentMonth() <= 3) {
                bqty = pcqty = 0;
                bqty = data.getIntValueFromDB("SELECT " + mmonth.get(i).toString() + "_BUDGET FROM PRODUCTION.FELT_BUDGET_REVIEW_DETAIL "
                        //                    + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'N" + (finyear - 2000) + "%' ");
                        + "WHERE YEAR_FROM='" + finyear + "' AND UPN='" + upn + "' AND DOC_NO LIKE 'B" + (finyear - 2000) + "%' AND COALESCE(APPROVED,0)=0 AND COALESCE(CANCELED,0)=0 ");
                pcqty = data.getIntValueFromDB("SELECT COUNT(*) FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                        + "WHERE FIN_FROM_YEAR='" + finyear + "' AND UPN='" + upn + "' "
                        + "AND PLANNED_REQUIREMENT_SCHEDULE='" + mmonth.get(i).toString() + " - " + (finyear + 1) + "' AND OPR_STATUS='INSERT' ");

                SQL = "UPDATE "
                        + "PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER Z, "
                        + "(SELECT @a := @a+1 as S_SR_NO,X.* FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER AS X,(SELECT @a:= 0) AS a "
                        + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' AND COALESCE(ASSIGNED_PIECE_NO,'')='' AND OPR_STATUS='INSERT' "
                        + "ORDER BY UPN,PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE,DUMMY_PIECE_NO "
                        + ") A, "
                        + "(SELECT @b := @b+1 as P_SR_NO,X.*  FROM PRODUCTION.TEMP_FELT_ORS_PR_PIECES_" + upn + " X,(SELECT @b:= 0) AS b "
                        //                    + "WHERE PR_OC_LAST_DDMMYY<=LAST_DAY('" + (finyear + 1) + "-" + i + "-01') "
                        + "WHERE 1=1 "
                        + "AND ORS_LAST_DATE>='" + finyear + "-04-01' AND ORS_LAST_DATE<='" + (finyear + 1) + "-03-31' "
                        + "AND PR_PIECE_NO NOT IN (SELECT ASSIGNED_PIECE_NO FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                        + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' AND COALESCE(OPR_STATUS,'')='INSERT' ) "
                        + "ORDER BY PRIORITY,PR_PIECE_NO "
                        + "LIMIT " + bqty + " "
                        + ") B "
                        + "SET Z.ASSIGNED_PIECE_NO=PR_PIECE_NO, "
                        //                    + "Z.ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_REQUESTED_MONTH,'')='' THEN PR_OC_MONTHYEAR ELSE PR_REQUESTED_MONTH END, "
                        //                    + "Z.ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_REQUESTED_MONTH,'')='' THEN PR_OC_LAST_DDMMYY ELSE PR_REQ_MTH_LAST_DDMMYY END, "
                        //                    + "Z.PLANNED_REQUIREMENT_SCHEDULE=DATE_FORMAT('" + (finyear + 1) + "-" + i + "-01', '%b - %Y'), Z.PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=LAST_DAY('" + (finyear + 1) + "-" + i + "-01'), "
                        //                    + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE=PR_CURRENT_SCH_MONTH, Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=PR_CURRENT_SCH_LAST_DDMMYY, "
                        //                    + "Z.ORDER_REQUIREMENT_SCHEDULE=PR_OC_MONTHYEAR, Z.ORDER_REQUIREMENT_SCHEDULE_LAST_DATE=PR_OC_LAST_DDMMYY "
                        + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_CURRENT_SCH_MONTH,'')='' THEN PR_REQUESTED_MONTH ELSE PR_CURRENT_SCH_MONTH END , "
                        + "Z.CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_CURRENT_SCH_MONTH,'')='' THEN PR_REQ_MTH_LAST_DDMMYY ELSE PR_CURRENT_SCH_LAST_DDMMYY END , "
                        + "Z.ORDER_REQUIREMENT_SCHEDULE=CASE WHEN COALESCE(PR_OC_MONTHYEAR,'')='' THEN PR_REQUESTED_MONTH ELSE PR_OC_MONTHYEAR END , "
                        + "Z.ORDER_REQUIREMENT_SCHEDULE_LAST_DATE=CASE WHEN COALESCE(PR_OC_MONTHYEAR,'')='' THEN PR_REQ_MTH_LAST_DDMMYY ELSE PR_OC_LAST_DDMMYY END "
                        + "WHERE A.FIN_FROM_YEAR=" + finyear + " AND A.UPN=B.PR_UPN AND P_SR_NO=S_SR_NO "
                        + "AND A.UPN=Z.UPN AND A.DUMMY_PIECE_NO=Z.DUMMY_PIECE_NO AND A.FIN_FROM_YEAR=Z.FIN_FROM_YEAR ";
                data.Execute(SQL);
//            } else {
//            }
        }

        SQL = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                + "SET FUP_CPRS_OPEN_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 2 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 2 MONTH))-11 DAY), "
                + "FUP_CPRS_CLOSE_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH))-10 DAY) "
                + "WHERE UPN=MM_UPN_NO AND MM_GRUP NOT IN ('SDF') AND UPN='" + upn + "' ";
        data.Execute(SQL);

        SQL = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                + "SET FUP_CPRS_OPEN_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 1 MONTH))-11 DAY), "
                + "FUP_CPRS_CLOSE_DATE=DATE_SUB(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 0 MONTH), INTERVAL DAY(DATE_SUB(PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, INTERVAL 0 MONTH))-10 DAY) "
                + "WHERE UPN=MM_UPN_NO AND MM_GRUP IN ('SDF') AND UPN='" + upn + "' ";
        data.Execute(SQL);

        SQL = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                + "SET PRS_QUARTER = CASE WHEN FUP_CPRS_OPEN_DATE>='2022-02-11' AND FUP_CPRS_OPEN_DATE<='2022-05-10' THEN 'Q1' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-05-11' AND FUP_CPRS_OPEN_DATE<='2022-08-10' THEN 'Q2' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-08-11' AND FUP_CPRS_OPEN_DATE<='2022-11-10' THEN 'Q3' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-11-11' AND FUP_CPRS_OPEN_DATE<='2023-02-10' THEN 'Q4' END "
                + "WHERE UPN=MM_UPN_NO AND MM_GRUP NOT IN ('SDF') AND UPN='" + upn + "' ";
        data.Execute(SQL);

        SQL = "UPDATE PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER, PRODUCTION.FELT_MACHINE_MASTER_DETAIL "
                + "SET PRS_QUARTER = CASE WHEN FUP_CPRS_OPEN_DATE>='2022-03-11' AND FUP_CPRS_OPEN_DATE<='2022-06-10' THEN 'Q1' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-06-11' AND FUP_CPRS_OPEN_DATE<='2022-09-10' THEN 'Q2' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-09-11' AND FUP_CPRS_OPEN_DATE<='2022-12-10' THEN 'Q3' "
                + "WHEN FUP_CPRS_OPEN_DATE>='2022-12-11' AND FUP_CPRS_OPEN_DATE<='2023-03-10' THEN 'Q4' END "
                + "WHERE UPN=MM_UPN_NO AND MM_GRUP IN ('SDF') AND UPN='" + upn + "' ";
        data.Execute(SQL);

        SQL = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE, PRS_QUARTER) "
                + "SELECT NOW(), FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE, COALESCE(PRS_QUARTER,'') AS PRS_QUARTER "
                + "FROM PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER "
                + "WHERE FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' "
                //+ "AND PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE>='" + finyear + "-04-01' "
                + "AND COALESCE(OPR_STATUS,'')!='DELETE' ";
        data.Execute(SQL);

        SQL = "DROP TABLE PRODUCTION.TEMP_FELT_ORS_PR_PIECES_" + upn;
        data.Execute(SQL);

        SQL = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER ORS "
                + "SET PR_CURRENT_SCH_MONTH=PLANNED_REQUIREMENT_SCHEDULE,PR_CURRENT_SCH_LAST_DDMMYY=PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE,"
                + "PR_PIECE_CURRENT_MONTH_IT_REMARK=CONCAT('RESCHEDULE UPDATION FROM  ',PR_CURRENT_SCH_MONTH,' To ',PLANNED_REQUIREMENT_SCHEDULE,' ',COALESCE(PR_PIECE_CURRENT_MONTH_IT_REMARK,'')) "
                + "WHERE PR_PIECE_NO=ASSIGNED_PIECE_NO AND OPR_STATUS='INSERT' "
                + "AND COALESCE(ASSIGNED_PIECE_NO,'')!='' AND COALESCE(PR_PIECE_STAGE,'') NOT IN ('INVOICED','EXP-INVOICE') "
                + "AND PLANNED_REQUIREMENT_SCHEDULE!=PR_CURRENT_SCH_MONTH";
        data.Execute(SQL);
        SQL = "UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER PR,PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER ORS "
                + "SET WIP_CURRENT_SCH_MONTH=PLANNED_REQUIREMENT_SCHEDULE,WIP_CURRENT_SCH_LAST_DDMMYY=PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE,"
                + "WIP_PIECE_IT_DEPT_REMARK=CONCAT('RESCHEDULE UPDATION FROM  ',WIP_CURRENT_SCH_MONTH,' To ',PLANNED_REQUIREMENT_SCHEDULE,' ',COALESCE(WIP_PIECE_IT_DEPT_REMARK,'')) "
                + "WHERE WIP_PIECE_NO=ASSIGNED_PIECE_NO AND OPR_STATUS='INSERT' "
                + "AND COALESCE(ASSIGNED_PIECE_NO,'')!='' AND COALESCE(WIP_PIECE_STAGE,'') NOT IN ('INVOICED','EXP-INVOICE') "
                + "AND PLANNED_REQUIREMENT_SCHEDULE!=WIP_CURRENT_SCH_MONTH";
        data.Execute(SQL);
        SQL = "INSERT INTO PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER_H "
                + "(ENTRY_DATETIME, FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "CURRENT_PLANNED_REQUIREMENT_SCHEDULE, CURRENT_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "OPR_STATUS, INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE, PRS_QUARTER) "
                + "SELECT NOW(), FIN_FROM_YEAR, FIN_TO_YEAR, UPN, PARTY_CODE, MACHINE_NO, POSITION_NO, DUMMY_PIECE_NO, ASSIGNED_PIECE_NO, "
                + "ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE, ORIGINAL_PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "PLANNED_REQUIREMENT_SCHEDULE, PLANNED_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "ORDER_REQUIREMENT_SCHEDULE, ORDER_REQUIREMENT_SCHEDULE_LAST_DATE, "
                + "'PR UPDATE', INSERT_DATE, DELETE_DATE, FUP_CPRS_OPEN_DATE, FUP_CPRS_CLOSE_DATE, COALESCE(PRS_QUARTER,'') AS PRS_QUARTER "
                + "FROM PRODUCTION.FELT_SALES_PIECE_REGISTER PR,PRODUCTION.FELT_ORDER_REQUIREMENT_PIECE_REGISTER ORS "
                + "WHERE PR_PIECE_NO=ASSIGNED_PIECE_NO AND OPR_STATUS='INSERT' AND COALESCE(ASSIGNED_PIECE_NO,'')!='' AND PLANNED_REQUIREMENT_SCHEDULE!=PR_CURRENT_SCH_MONTH "
                + "AND FIN_FROM_YEAR=" + finyear + " AND UPN='" + upn + "' ";

        data.Execute(SQL);
    }
}
