/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import static EITLERP.FeltSales.common.JavaMail.SendMail;
import EITLERP.data;
import java.io.File;
import java.sql.ResultSet;

/**
 *
 * @author Dharmendra
 */
public class Trail_Piece_Notification {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();

    public static void main(String[] args) {
        // TODO code application logic here
        if (EITLERPGLOBAL.getCurrentDay() == 1) {
            try {
                String recievers, pMessage, pSubject, pcc;

                recievers = "harshsingh@dineshmills.com,abhishekpandey@dineshmills.com,sumankumar@dineshmills.com,nagarajum@dineshmills.com,radhesharma@dineshmills.com,sdmlerp@dineshmills.com";
                pcc = "spmalik@dineshmills.com,vdshanbhag@dineshmills.com,soumen@dineshmills.com";
                pSubject = "Pending Mounting Data Updation for Trial Piece";
                pMessage = "<br>";
                pMessage += "Pending Mounting Data Updation for Trial Piece";
                pMessage += "<br>";
                pMessage += "<table border=1>";
                pMessage += "<tr><td align='center'><b>Sr. No.</b></td>"
                        + "<td align='center'><b>Piece No.</b></td>"
                        + "<td align='center'><b>Party Code.</b></td>"
                        + "<td align='center'><b>Name</b></td>"
                        + "<td align='center'><b>UPN</b></td>"
                        + "<td align='center'><b>Invoice No.</b></td>"
                        + "<td align='center'><b>Invoice Date</b></td>"
                        + "</tr>";
                ResultSet tr = data.getResult("SELECT DISTINCT PR_PIECE_NO,PR_PARTY_CODE,PARTY_NAME,PR_UPN,PR_INVOICE_NO,DATE_FORMAT(PR_INVOICE_DATE,'%d/%m/%Y') AS INVOICE_DATE FROM PRODUCTION.FELT_SALES_PIECE_REGISTER "
                        + "LEFT JOIN DINESHMILLS.D_SAL_PARTY_MASTER ON PR_PARTY_CODE=PARTY_CODE "
                        + "LEFT JOIN PRODUCTION.FELT_TRAIL_PIECE_DISPATCH D "
                        + "ON PR_PIECE_NO=D.FT_PIECE_NO "
                        + "LEFT JOIN PRODUCTION.FELT_TRAIL_PIECE_SELECTION S "
                        + "ON PR_PIECE_NO=S.FT_PIECE_NO "
                        + "WHERE COALESCE(S.CANCELED,0)=0 AND PR_PIECETRIAL_FLAG=1 AND ADDDATE(PR_INVOICE_DATE, INTERVAL 30 DAY)<CURDATE() "
                        + "AND COALESCE(S.MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00' "
                        + "AND COALESCE(D.MOUNTING_PLAN_DATE,'0000-00-00')='0000-00-00'");
                tr.first();
                if (tr.getRow() > 0) {
                    int j = 1;
                    while (!tr.isAfterLast()) {
                        pMessage += "<tr>";
                        pMessage += "<td>" + j + "</td>";
                        pMessage += "<td>" + tr.getString("PR_PIECE_NO") + "</td>";
                        pMessage += "<td>" + tr.getString("PR_PARTY_CODE") + "</td>";
                        pMessage += "<td>" + tr.getString("PARTY_NAME") + "</td>";
                        pMessage += "<td>" + tr.getString("PR_UPN") + "</td>";
                        pMessage += "<td>" + tr.getString("PR_INVOICE_NO") + "</td>";
                        pMessage += "<td>" + tr.getString("INVOICE_DATE") + "</td>";
                        pMessage += "</tr>";
                        tr.next();
                        j++;
                    }
                    pMessage += "</table>";
                    pMessage += "<br><br>";
                    pMessage = pMessage + "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";
                    recievers += ",vdshanbhag@dineshmills.com,soumen@dineshmills.com";
                    pcc = "aditya@dineshmills.com,abtewary@dineshmills.com";
                    SendMail(recievers, pMessage, pSubject, pcc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
