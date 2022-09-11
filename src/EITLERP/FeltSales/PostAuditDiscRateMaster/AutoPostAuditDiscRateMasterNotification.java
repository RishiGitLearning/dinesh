/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.PostAuditDiscRateMaster;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class AutoPostAuditDiscRateMasterNotification {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        Auto_PA_Data();        
    }

    private static void Auto_PA_Data() {
        String mbody;
        String sql, toemail;
        ResultSet tmpr;

        try {
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();

            rsData1 = stmt1.executeQuery("SELECT *,CASE WHEN SELECTED_FLAG=0 THEN 'NOT AUDITED' ELSE 'AUDIT OK' END AS STATUS,CASE WHEN COALESCE(PARTY_CODE,'')='' THEN GROUP_CODE ELSE PARTY_CODE END AS CODE,CASE WHEN COALESCE(PARTY_CODE,'')='' THEN GROUP_NAME ELSE PARTY_NAME END AS NAME FROM PRODUCTION.POST_AUDIT_DISC_RATE_MASTER_DATA WHERE FINAL_APPROVAL_DATE=CURDATE() ORDER BY DOC_NO ");
            rsData1.first();

            if (rsData1.getRow() > 0) {

                mbody = "<html><body> Dear All,<br>"
                        + "<br><p>We are pleased to inform you that following Discount Rate Master has been Post Audited.</p><br>"
                        + "<table border='1'><tr>"
                        + "<th align='center'> STATUS </th><th align='center'> MASTER NO </th>"
                        + "<th align='center'> PARTY/GROUP CODE </th><th align='center'> NAME </th>"
                        + "<th align='center'> AUDIT REMARK </th>"
                        + "</tr>";

                while (!rsData1.isAfterLast()) {

                    mbody = mbody + "<tr>"
                            + "<td>"
                            + "<p>" + rsData1.getString("STATUS") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("MASTER_NO") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("CODE") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("NAME") + "</p>"
                            + "</td>"
                            + "<td>"
                            + "<p>" + rsData1.getString("AUDIT_REMARK") + "</p>"
                            + "</td>";

                    rsData1.next();
                }
                
                mbody = mbody + "</table></body></html>";
                
                toemail = "felts@dineshmills.com";

                try {
                    JavaMail.SendMail_Felts(toemail, mbody, "Notification : Post Audit of Discount Rate Master", "felts@dineshmills.com");
                } catch (Exception s) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
