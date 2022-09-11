/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltWarpingBeamOrder.clsWarpingBeamOrder;
import EITLERP.FeltSales.common.MailNotification;
import EITLERP.clsHierarchy;
import EITLERP.clsUser;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author Dharmendra
 */
public class frmGIDCRMStockReport {

    static String pMessage = "";
    static String pSubject = "Daily Felt Sales GIDC [SDF] Stock Notication : For Date " + data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d %b %Y')");

    public static void main(String[] args) {
        generateMessage();
        String recievers = "feltpp@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,brdfltfin@dineshmills.com";
//        String recievers = "dharmendra@dineshmills.com";
        //String recievers = "rishineekhra@dineshmills.com";
        //String recievers = "sdmlerp@dineshmills.com";
        String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, "sdmlerp@dineshmills.com");
    }

    private static void generateMessage() {
        try {
            pMessage = pMessage + "<style> table, th, td {"
                    + "  border: 1px solid black;"
                    + "  border-collapse: collapse;"
                    + "  font-family: 'Arial'"
                    + "} th, td {"
                    + "  padding: 5px;"
                    + "}</style><font face='Arial'>  <br><br><br><b>GIDC [SDF] RM Stock Detail :</b>";

            pMessage = pMessage + "<br><br><table border=1>";
            pMessage = pMessage + "<tr style='font-weight:bold'>";
            pMessage = pMessage + "<td>NO.</td>";
            pMessage = pMessage + "<td>RM Code</td>";
            pMessage = pMessage + "<td>Description</td>";
            pMessage = pMessage + "<td>Available</td>";
            pMessage = pMessage + "</tr>";

            Connection Conn1;
            Statement stmt1;
            String strSQL = "";
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            strSQL = "SELECT S.*,M.ITEM_DESCRIPTION FROM PRODUCTION.GIDC_FELT_RM_STOCK S "
                    + "LEFT JOIN DINESHMILLS.D_INV_ITEM_MASTER M "
                    + "ON S.ITEM_CODE=M.ITEM_ID";
            System.out.println("SQL " + strSQL);
            rsData1 = stmt1.executeQuery(strSQL);
            System.out.println("");
            rsData1.first();
            int srno = 1;
            if (rsData1.getRow() > 0) {
                while (!rsData1.isAfterLast()) {

                    pMessage = pMessage + "<tr>";
                    pMessage = pMessage + "<td align=right>" + (srno++) + "</td>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString("ITEM_CODE") + "</td>";
                    pMessage = pMessage + "<td align=left>" + rsData1.getString("ITEM_DESCRIPTION") + "</td>";
                    pMessage = pMessage + "<td align=right>" + rsData1.getString("AVAILABLE") + "</td>";
                    pMessage = pMessage + "</tr>";

                    rsData1.next();
                }
            }
             pMessage = pMessage + "</table></font>";

            System.out.println("MESSAGE : " + pMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
