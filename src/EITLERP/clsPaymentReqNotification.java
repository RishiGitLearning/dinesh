/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP;

import EITLERP.FeltSales.common.JavaMail;
import EITLERP.FeltSales.common.MailNotification;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author root
 */
public class clsPaymentReqNotification {

    public static void main(String[] args) {
        // TODO code application logic here

        notificationForPaymentRequisition();

    }

    private static void notificationForPaymentRequisition() {
        System.out.println("MIR date + Cr days has been over = ");
        String pBody = "", pSubject = "", recievers = "", pcc = "";

        pSubject = "Notification : MIR date + Cr days has been over  ";
        pBody = "Dear User,<br><br>";

        try {

            pBody += "Notification mail to generate Payment Requisition Slip  <br>";

            pBody += "<br>";

            pBody = pBody + "<style> table, th, td {"
                    + "  border: 1px solid black;"
                    + "  border-collapse: collapse;"
                    + "  font-family: 'Arial'"
                    + "} th, td {"
                    + "  padding: 5px;"
                    + "}</style><font face='Arial'>  <br>";
            pBody += "<table border=1>";
            pBody += "<tr>"
                    + "<td align='center'><b>Sr. No</b></td>"
                    + "<td align='center'><b>Supplier Code</b></td>"
                    + "<td align='center'><b>Supplier Name</b></td>"
                    + "<td align='center'><b>MIR No</b></td>"
                    + "<td align='center'><b>MIR Date</b></td>"
                    + "<td align='center'><b>Credit Days</b></td>"
                    + "<td align='center'><b>Due Date</b></td>"
                    + "<td align='center'><b>GRN No</b></td>"
                    + "<td align='center'><b>GRN Date</b></td>"
                    + "<td align='center'><b>PJV No</b></td>"
                    + "<td align='center'><b>PJV Date</b></td>"
                    + "<td align='center'><b>Bill/Inv No</b></td>"
                    + "<td align='center'><b>Bill/Inv Date</b></td>"
                    + "<td align='center'><b>PO No</b></td>"
                    + "<td align='center'><b>PO Date</b></td>"
                    + "<td align='center'><b>Payment Terms</b></td>"
                    + "</tr>";

            int srNo = 0;
            //String sql1 = "SELECT * FROM D_INV_PAYMENT_REQ WHERE PAY_REQ_NO='" + txtPayReqSlipNo.getText() + "'";
            /*String sql1 = "SELECT * FROM "
             + "(SELECT DISTINCT H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE,D.INVOICE_AMOUNT,D.GRN_NO,D.PO_NO,D.PO_DATE,SUB_ACCOUNT_CODE FROM FINANCE.D_FIN_VOUCHER_DETAIL D,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO=D.VOUCHER_NO "
             + " AND H.APPROVED=1 AND H.CANCELLED=0 "
             //+ " #AND D.SUB_ACCOUNT_CODE='619258' \n"
             + " AND SUBSTRING(H.VOUCHER_NO,1,2) IN ('PY','PJ') GROUP BY INVOICE_NO,INVOICE_DATE HAVING COUNT(INVOICE_NO)=1 ORDER BY INVOICE_NO,INVOICE_DATE) AS A WHERE VOUCHER_NO LIKE 'PJ%' AND INVOICE_NO!='' AND GRN_NO!='' AND SUB_ACCOUNT_CODE!='' AND PO_NO LIKE 'RM%'";//  AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"'";
             */
            String sql1 = "SELECT *  FROM (SELECT H.VOUCHER_NO,H.VOUCHER_DATE,D.INVOICE_NO,D.INVOICE_DATE,D.INVOICE_AMOUNT,D.GRN_NO,D.GRN_DATE,D.PO_NO,D.PO_DATE,SUB_ACCOUNT_CODE FROM FINANCE.D_FIN_VOUCHER_DETAIL D,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO=D.VOUCHER_NO  AND H.APPROVED=1 AND H.CANCELLED=0 AND "
                    + "SUBSTRING(H.VOUCHER_NO,1,2) IN ('PY','PJ') AND VOUCHER_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND SUB_ACCOUNT_CODE!='' "
                    + " AND GRN_NO!='' AND GRN_NO LIKE 'R%' AND INVOICE_NO!='' GROUP BY H.VOUCHER_NO HAVING COUNT(INVOICE_NO)=1 ORDER BY INVOICE_NO,INVOICE_DATE) AS A WHERE VOUCHER_NO LIKE 'PJ%' ";
            System.out.println(sql1);
            ResultSet rsdata = data.getResult(sql1);
            rsdata.first();
            if (rsdata.getRow() > 0) {

                label:
                while (!rsdata.isAfterLast()) {
                    //String suppID=data.getStringValueFromDB("SELECT SUPP_ID FROM DINESHMILLS.D_INV_MIR_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_NO!='' AND INVOICE_DATE!='0000-00-00' AND INVOICE_NO='"+rsdata.getString("INVOICE_NO")+"' AND INVOICE_DATE='"+rsdata.getString("INVOICE_DATE")+"' ");
                    //if(suppID.equals(rsdata.getString("SUB_ACCOUNT_CODE"))){
                    int paymentDays = 0;
                    String supplierName = data.getStringValueFromDB("SELECT SUPP_NAME FROM DINESHMILLS.D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='" + rsdata.getString("SUB_ACCOUNT_CODE") + "'");
                    String paymentTerm = data.getStringValueFromDB("SELECT PAYMENT_TERM FROM DINESHMILLS.D_PUR_PO_HEADER WHERE PO_NO='" + rsdata.getString("PO_NO") + "' AND PO_DATE='" + rsdata.getString("PO_DATE") + "'");
                    paymentDays = data.getIntValueFromDB("SELECT PAYMENT_DAYS FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='" + rsdata.getString("SUB_ACCOUNT_CODE") + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "");
                    System.out.println(paymentDays);
                        //System.out.println("SELECT MIR_DATE FROM DINESHMILLS.D_INV_MIR_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_NO!='' AND INVOICE_DATE!='0000-00-00' AND INVOICE_NO='"+rsdata.getString("INVOICE_NO")+"' AND INVOICE_DATE='"+rsdata.getString("INVOICE_DATE")+"'");
                    //String mirDate=data.getStringValueFromDB("SELECT MIR_DATE FROM DINESHMILLS.D_INV_MIR_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_NO!='' AND INVOICE_DATE!='0000-00-00' AND INVOICE_NO='"+rsdata.getString("INVOICE_NO")+"' AND INVOICE_DATE='"+rsdata.getString("INVOICE_DATE")+"'");
                    System.out.println(rsdata.getString("GRN_NO"));
                    String mirDate = data.getStringValueFromDB("SELECT MIR_DATE FROM DINESHMILLS.D_INV_MIR_HEADER WHERE MIR_NO IN (SELECT MIR_NO FROM DINESHMILLS.D_INV_GRN_DETAIL WHERE GRN_NO='" + rsdata.getString("GRN_NO") + "')");
                    String mirNo = data.getStringValueFromDB("SELECT MIR_NO FROM DINESHMILLS.D_INV_MIR_HEADER WHERE MIR_NO IN (SELECT MIR_NO FROM DINESHMILLS.D_INV_GRN_DETAIL WHERE GRN_NO='" + rsdata.getString("GRN_NO") + "')");
                    System.out.println(mirDate);
                    String dueDate = "";
                    if (mirDate.equals("")) {
                        rsdata.next();
                        continue;
                    } else {
                        //if (paymentDays <= 0) {
                        //    rsdata.next();
                        //    continue;
                        //} else {
                            dueDate = EITLERPGLOBAL.formatDate(EITLERPGLOBAL.addDaysToDate(mirDate, paymentDays, "yyyy-MM-dd"));
                        //}
                    }
                    dueDate = EITLERPGLOBAL.formatDateDB(dueDate);
                    System.out.println(dueDate);
                    String currentDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
                    System.out.println(currentDate);
                    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
                    Date d1 = sdformat.parse(dueDate);
                    Date d2 = sdformat.parse(currentDate);
                        //Date d1 = sdformat.parse(dueDate);                        

                    System.out.println("The date 1 is: " + sdformat.format(d1));
                    System.out.println("The date 2 is: " + sdformat.format(d2));
                    if (d1.compareTo(d2) > 0) {
                        System.out.println("Date 1 occurs after Date 2");
                    } else if (d1.compareTo(d2) < 0) {
                        srNo++;
                        System.out.println("Date 1 occurs before Date 2");
                        pBody += "<tr>";
                        pBody += "<td>" + srNo + "</td>";
                        pBody += "<td>" + rsdata.getString("SUB_ACCOUNT_CODE") + "</td>";
                        pBody += "<td>" + supplierName + "</td>";
                        pBody += "<td>" + mirNo + "</td>";
                        pBody += "<td>" + EITLERPGLOBAL.formatDate(mirDate) + "</td>";
                        pBody += "<td>" + paymentDays + "</td>";
                        pBody += "<td>" + EITLERPGLOBAL.formatDate(dueDate) + "</td>";
                        pBody += "<td>" + rsdata.getString("GRN_NO") + "</td>";
                        pBody += "<td>" + EITLERPGLOBAL.formatDate(rsdata.getString("GRN_DATE")) + "</td>";
                        pBody += "<td>" + rsdata.getString("VOUCHER_NO") + "</td>";
                        pBody += "<td>" + EITLERPGLOBAL.formatDate(rsdata.getString("VOUCHER_DATE")) + "</td>";
                        pBody += "<td>" + rsdata.getString("INVOICE_NO") + "</td>";
                        pBody += "<td>" + EITLERPGLOBAL.formatDate(rsdata.getString("INVOICE_DATE")) + "</td>";
                        if (rsdata.getString("PO_NO").equals("")) {
                            pBody += "<td></td>";
                            pBody += "<td></td>";
                        } else {
                            pBody += "<td>" + rsdata.getString("PO_NO") + "</td>";
                            pBody += "<td>" + EITLERPGLOBAL.formatDate(rsdata.getString("PO_DATE")) + "</td>";
                        }
                        pBody += "<td>" + paymentTerm + "</td>";
                        pBody += "</tr>";
                    } else if (d1.compareTo(d2) == 0) {
                        System.out.println("Both dates are equal");
                    }

                    //}
                    //String mirDate=data.getStringValueFromDB("SELECT MIR_DATE FROM DINESHMILLS.D_INV_MIR_HEADER WHERE A")
                    //srNo++;
                    rsdata.next();
                }
                pBody += "</table>";
                pBody += "<br>";
            }

            pBody += "<br><br>";

            recievers = "ashutosh@dineshmills.com"; //ashutosh@dineshmills.com
            /*HashMap hmSendToList;
             hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.getComboCode(cmbHierarchy), EITLERPGLOBAL.gNewUserID, true);
             for (int i = 1; i <= hmSendToList.size(); i++) {
             clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
             int U_ID = ObjUser.getAttribute("USER_ID").getInt();

             String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);

             System.out.println("USERID : " + U_ID + ", send_to : " + to);
             if (!to.equals("")) {
             recievers = recievers + "," + to;
             }
                
             }*/
            //String to=clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, data.getIntValueFromDB("SELECT USER_ID FROM D_COM_DOC_DATA WHERE MODULE_ID=204 AND TYPE='C' AND DOC_NO='"+txtPayReqSlipNo.getText()+"'"));

            //if (!to.equals("")) {
            //        recievers = recievers + "," + to;
            //    }
            pBody += "<br><br><br>**** This is an auto-generated email, please do not reply ****<br><br>";

            //String responce = MailNotification.sendNotificationMail(811, pSubject, pBody, recievers, pcc, EITLERPGLOBAL.getComboCode(cmbHierarchy));            
            //System.out.println("Send Mail Responce : " + responce);
            JavaMail.SendMail(recievers, pBody, pSubject, pcc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
