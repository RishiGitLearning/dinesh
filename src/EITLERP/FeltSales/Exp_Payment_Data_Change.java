/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;

/**
 *
 * @author Dharmendra
 */
public class Exp_Payment_Data_Change {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String sql;
        if (EITLERPGLOBAL.getCurrentDay() == 1) {
            sql = "INSERT INTO PRODUCTION.FELT_PAY_DATE_CHAGE_DETAIL  SELECT PR_PIECE_NO,DATE_ADD(CURDATE(), INTERVAL CASE WHEN WEEKDAY(CURDATE())=6 THEN 6 ELSE 5-WEEKDAY(CURDATE()) END DAY) "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE PR_EXP_PAY_CHQRC_DATE<=SUBDATE(CURDATE(),INTERVAL 1 DAY) AND COALESCE(PR_EXP_PAY_CHQRC_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00'";
            data.Execute(sql);
            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "SET PR_EXP_PAY_CHQRC_DATE_ORIGINAL=CASE WHEN COALESCE(PR_EXP_PAY_CHQRC_DATE_ORIGINAL,'')='' THEN PR_EXP_PAY_CHQRC_DATE ELSE PR_EXP_PAY_CHQRC_DATE_ORIGINAL END,"
                    + "PR_EXP_PAY_CHQRC_DATE=DATE_ADD(CURDATE(), INTERVAL CASE WHEN WEEKDAY(CURDATE())=6 THEN 6 ELSE 5-WEEKDAY(CURDATE()) END DAY) "
                    + "WHERE PR_EXP_PAY_CHQRC_DATE<=SUBDATE(CURDATE(),INTERVAL 1 DAY) AND COALESCE(PR_EXP_PAY_CHQRC_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00'";
            data.Execute(sql);
        }
        if (data.getStringValueFromDB("SELECT DAYNAME(CURDATE()) FROM DUAL").equalsIgnoreCase("Tuesday")) {
            sql = "INSERT INTO PRODUCTION.FELT_PAY_DATE_CHAGE_DETAIL  SELECT PR_PIECE_NO,CASE WHEN MONTH(CURDATE())-MONTH(DATE_ADD(CURDATE(), INTERVAL 4 DAY))=0 THEN DATE_ADD(CURDATE(), INTERVAL 4 DAY) ELSE LAST_DAY(CURDATE()) END "
                    + " FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "WHERE PR_EXP_PAY_CHQRC_DATE<=SUBDATE(CURDATE(),INTERVAL 3 DAY) AND COALESCE(PR_EXP_PAY_CHQRC_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00'";
            data.Execute(sql);
            sql = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER "
                    + "SET PR_EXP_PAY_CHQRC_DATE_ORIGINAL=CASE WHEN COALESCE(PR_EXP_PAY_CHQRC_DATE_ORIGINAL,'')='' THEN PR_EXP_PAY_CHQRC_DATE ELSE PR_EXP_PAY_CHQRC_DATE_ORIGINAL END,"
                    + "PR_EXP_PAY_CHQRC_DATE=CASE WHEN MONTH(CURDATE())-MONTH(DATE_ADD(CURDATE(), INTERVAL 4 DAY))=0 THEN DATE_ADD(CURDATE(), INTERVAL 4 DAY) ELSE LAST_DAY(CURDATE()) END "
                    + "WHERE PR_EXP_PAY_CHQRC_DATE<=SUBDATE(CURDATE(),INTERVAL 3 DAY) AND COALESCE(PR_EXP_PAY_CHQRC_DATE,'0000-00-00')!='0000-00-00' "
                    + "AND COALESCE(PR_INVOICE_DATE,'0000-00-00')='0000-00-00'";
            data.Execute(sql);
        }

    }
}
