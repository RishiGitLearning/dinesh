/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.AutoPI;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class AutoPIUnTickedNotification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        UnTicked_Auto_PI_Data();
    }

    private static void UnTicked_Auto_PI_Data() {
        String mbody;
        String sql, toemail;
        ResultSet tmpr;

        try {
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();

            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_AUTO_PI_DATA WHERE FINAL_APPROVAL_DATE=CURDATE() AND PI_SELECTED_FLAG=0 AND PI_GENERATED_FLAG=0 AND PI_MAILED_FLAG=0 ORDER BY PARTY_CODE,PIECE_NO ");
            rsData1.first();

            if (rsData1.getRow() > 0) {

                mbody = "<html><body> Dear All,<br>"
                        + "<br><p>We are pleased to inform you that your following felt has been made ready and Auto PI Selection was not Ticked to send Auto PI.</p><br>"
                        + "<table border='1'><tr>"
                        + "<th align='center'> PARTY CODE </th><th align='center'> NAME </th>"
                        + "<th> PC NO </th><th align='center'> PRODUCT CODE </th>"
                        + "<th align='center'> LENGTH </th><th align='center'> WIDTH </th>"
                        + "<th align='center'> WEIGHT </th><th align='center'> VALUE(RS) </th>"
                        + "</tr>";

                while (!rsData1.isAfterLast()) {

                    mbody = mbody + "<tr>"
                            + "<td>"
                            + "<p>" + rsData1.getString("PARTY_CODE") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("PARTY_NAME") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("PIECE_NO") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("PRODUCT_CODE") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("LENGTH") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("WIDTH") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("WEIGHT") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("PI_VALUE") + "</p>"
                            + "</td>";

                    rsData1.next();
                }
                
                mbody = mbody + "</table></body></html>";
                
                toemail = "felts@dineshmills.com";

                try {
                    JavaMail.SendMail_Felts(toemail, mbody, "Notification : List - Auto PI Selection not Ticked", "felts@dineshmills.com");
                } catch (Exception s) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
