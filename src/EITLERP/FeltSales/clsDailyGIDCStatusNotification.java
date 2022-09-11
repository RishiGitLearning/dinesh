/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.clsSales_Party;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class clsDailyGIDCStatusNotification {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        InstructionMFGNotification();
        SpiralNotification();
        AssemblyNotification();
        ReadyToDespatchNotification();
    }
    
    private static void InstructionMFGNotification() {
            try {

                String pSubject = "GIDC SDF Notification : Daily Pending for Manufacturing Piece Detail at GIDC for Date - "+EITLERPGLOBAL.getCurrentDate()+" "+data.getStringValueFromDB("SELECT CURTIME()");
                String pMessage = "";
//                String cc = "rishineekhra@dineshmills.com,gaurang@dineshmills.com,dharmendra@dineshmills.com,ashutosh@dineshmills.com";
                String cc = "sdmlerp@dineshmills.com";
                int SrNo = 1;
                
                pMessage = pMessage + "<br>TO,<br>ADMINISTRATION<br>PLEASE NOTE THAT FOLLOWING PIECES ARE PENDING FOR MANUFACTURING AT GIDC<br><br>";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER WHERE GIDC_STAGE='YET_TO_MFG'");
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> PIECE NO </th>"
                        + "<th align='center'> PARTY CODE </th>"
                        + "<th align='center'> PARTY NAME </th>"
                        + "<th align='center'> PRODUCT GROUP </th>"
                        + "<th align='center'> GREY LENGTH </th>"
                        + "<th align='center'> GREY WIDTH </th>"
                        + "<th align='center'> TH.GREY WEIGHT </th>"
                        + "<th align='center'> GSM </th>"
                        + "<th align='center'> SQMTR </th>"
                        + "<th align='center'> REMARK </th>"
                        + "<th align='center'> SDF INSTRUCTION DATE </th>"
                        + "<th align='center'> DAYS DIFF TILL DATE </th>"
                        + "</tr>";
                
                
                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_PIECE_NO") + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_PARTY_CODE") + " </td>"
                                + "<td align='left'> " + clsSales_Party.getPartyName(2, rsData.getString("GIDC_PARTY_CODE")) + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_GROUP") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_LENGTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_WIDTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_THORITICAL_WEIGHT") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_GSM") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_SQMTR") + " </td>"
                                + "<td align='left'> " + rsData.getString("GIDC_PIECE_REMARK") + " </td>";
                        if (rsData.getString("GIDC_SDF_INST_DATE").equals("0000-00-00") || rsData.getString("GIDC_SDF_INST_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SDF_INST_DATE")) + " </td>";
                            pMessage = pMessage + "<td align='right'> "+data.getStringValueFromDB("SELECT DATEDIFF(CURDATE(),'"+rsData.getString("GIDC_SDF_INST_DATE")+"')")+" </td>";
                        }
                        pMessage = pMessage + "</tr>";
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>"
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>"
//                                + "</tr>";
                        
                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "feltpp@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,brdfltfin@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

//                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }
    
    private static void SpiralNotification() {
            try {

                String pSubject = "GIDC SDF Notification : Daily Spiral Piece Detail at GIDC for Date - "+EITLERPGLOBAL.getCurrentDate()+" "+data.getStringValueFromDB("SELECT CURTIME()");
                String pMessage = "";
//                String cc = "rishineekhra@dineshmills.com,gaurang@dineshmills.com,dharmendra@dineshmills.com,ashutosh@dineshmills.com";
                String cc = "sdmlerp@dineshmills.com";
                int SrNo = 1;
                
                pMessage = pMessage + "<br>TO,<br>ADMINISTRATION<br>PLEASE NOTE THAT FOLLOWING PIECES ARE SPIRALED AT GIDC<br><br>";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER WHERE GIDC_STAGE='SPIRALED'");
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> PIECE NO </th>"
                        + "<th align='center'> PARTY CODE </th>"
                        + "<th align='center'> PARTY NAME </th>"
                        + "<th align='center'> PRODUCT GROUP </th>"
                        + "<th align='center'> GREY LENGTH </th>"
                        + "<th align='center'> GREY WIDTH </th>"
                        + "<th align='center'> TH.GREY WEIGHT </th>"
                        + "<th align='center'> GSM </th>"
                        + "<th align='center'> SQMTR </th>"
                        + "<th align='center'> REMARK </th>"
                        + "<th align='center'> SDF INSTRUCTION DATE </th>"
                        + "<th align='center'> SPIRALED DATE </th>"
                        + "<th align='center'> DAYS DIFF TILL DATE </th>"
                        + "</tr>";
                
                
                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_PIECE_NO") + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_PARTY_CODE") + " </td>"
                                + "<td align='left'> " + clsSales_Party.getPartyName(2, rsData.getString("GIDC_PARTY_CODE")) + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_GROUP") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_LENGTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_WIDTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_THORITICAL_WEIGHT") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_GSM") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_SQMTR") + " </td>"
                                + "<td align='left'> " + rsData.getString("GIDC_PIECE_REMARK") + " </td>";
                        if (rsData.getString("GIDC_SDF_INST_DATE").equals("0000-00-00") || rsData.getString("GIDC_SDF_INST_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SDF_INST_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_SPIRALING_DATE").equals("0000-00-00") || rsData.getString("GIDC_SPIRALING_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>";
                            pMessage = pMessage + "<td align='right'> "+data.getStringValueFromDB("SELECT DATEDIFF(CURDATE(),'"+rsData.getString("GIDC_SPIRALING_DATE")+"')")+" </td>";
                        }
                        pMessage = pMessage + "</tr>";
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>"
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>"
//                                + "</tr>";
                        
                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "feltpp@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,brdfltfin@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

//                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }
    
    private static void AssemblyNotification() {
            try {

                String pSubject = "GIDC SDF Notification : Daily Assembly Piece Detail at GIDC for Date - "+EITLERPGLOBAL.getCurrentDate()+" "+data.getStringValueFromDB("SELECT CURTIME()");
                String pMessage = "";
//                String cc = "rishineekhra@dineshmills.com,gaurang@dineshmills.com,dharmendra@dineshmills.com,ashutosh@dineshmills.com";
                String cc = "sdmlerp@dineshmills.com";
                int SrNo = 1;
                
                pMessage = pMessage + "<br>TO,<br>ADMINISTRATION<br>PLEASE NOTE THAT FOLLOWING PIECES ARE ASSEMBLED AT GIDC<br><br>";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER WHERE GIDC_STAGE='ASSEMBLED'");
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> PIECE NO </th>"
                        + "<th align='center'> PARTY CODE </th>"
                        + "<th align='center'> PARTY NAME </th>"
                        + "<th align='center'> PRODUCT GROUP </th>"
                        + "<th align='center'> GREY LENGTH </th>"
                        + "<th align='center'> GREY WIDTH </th>"
                        + "<th align='center'> TH.GREY WEIGHT </th>"
                        + "<th align='center'> GSM </th>"
                        + "<th align='center'> SQMTR </th>"
                        + "<th align='center'> REMARK </th>"
                        + "<th align='center'> SDF INSTRUCTION DATE </th>"
                        + "<th align='center'> SPIRALED DATE </th>"
                        + "<th align='center'> ASSEMBLED DATE </th>"
                        + "<th align='center'> DAYS DIFF TILL DATE </th>"
                        + "</tr>";
                
                
                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_PIECE_NO") + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_PARTY_CODE") + " </td>"
                                + "<td align='left'> " + clsSales_Party.getPartyName(2, rsData.getString("GIDC_PARTY_CODE")) + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_GROUP") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_LENGTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_WIDTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_THORITICAL_WEIGHT") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_GSM") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_SQMTR") + " </td>"
                                + "<td align='left'> " + rsData.getString("GIDC_PIECE_REMARK") + " </td>";
                        if (rsData.getString("GIDC_SDF_INST_DATE").equals("0000-00-00") || rsData.getString("GIDC_SDF_INST_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SDF_INST_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_SPIRALING_DATE").equals("0000-00-00") || rsData.getString("GIDC_SPIRALING_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_ASSEMBLY_DATE").equals("0000-00-00") || rsData.getString("GIDC_ASSEMBLY_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>";
                            pMessage = pMessage + "<td align='right'> "+data.getStringValueFromDB("SELECT DATEDIFF(CURDATE(),'"+rsData.getString("GIDC_ASSEMBLY_DATE")+"')")+" </td>";
                        }
                        pMessage = pMessage + "</tr>";
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>"
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>"
//                                + "</tr>";
                        
                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "feltpp@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,brdfltfin@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

//                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }
    
    private static void ReadyToDespatchNotification() {
            try {

                String pSubject = "GIDC SDF Notification : Daily Ready to Despatch Piece Detail at GIDC for Date - "+EITLERPGLOBAL.getCurrentDate()+" "+data.getStringValueFromDB("SELECT CURTIME()");
                String pMessage = "";
//                String cc = "rishineekhra@dineshmills.com,gaurang@dineshmills.com,dharmendra@dineshmills.com,ashutosh@dineshmills.com";
                String cc = "sdmlerp@dineshmills.com";
                int SrNo = 1;
                
                pMessage = pMessage + "<br>TO,<br>ADMINISTRATION<br>PLEASE NOTE THAT FOLLOWING PIECES ARE READY AT GIDC<br><br>";

                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                rsData = stmt.executeQuery("SELECT * FROM PRODUCTION.GIDC_FELT_PIECE_REGISTER WHERE GIDC_STAGE='READY_TO_DESPATCH'");
                
//                System.out.println("String StrSQL : "+strSQL);
                
                pMessage = pMessage + "<table border='1'>"
                        + "<tr>"
                        + "<th align='center'> SrNo </th>"
                        + "<th align='center'> PIECE NO </th>"
                        + "<th align='center'> PARTY CODE </th>"
                        + "<th align='center'> PARTY NAME </th>"
                        + "<th align='center'> PRODUCT GROUP </th>"
                        + "<th align='center'> GREY LENGTH </th>"
                        + "<th align='center'> GREY WIDTH </th>"
                        + "<th align='center'> TH.GREY WEIGHT </th>"
                        + "<th align='center'> GSM </th>"
                        + "<th align='center'> SQMTR </th>"
                        + "<th align='center'> REMARK </th>"
                        + "<th align='center'> SDF INSTRUCTION DATE </th>"
                        + "<th align='center'> SPIRALED DATE </th>"
                        + "<th align='center'> ASSEMBLED DATE </th>"
                        + "<th align='center'> READY TO DESPATCH DATE </th>"
                        + "<th align='center'> DAYS DIFF TILL DATE </th>"
                        + "</tr>";
                
                
                rsData.first();
                if (rsData.getRow() > 0) {
                    while (!rsData.isAfterLast()) {

                        pMessage = pMessage + ""
                                + "<tr>"
                                + "<td align='right'> " + (SrNo++) + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_PIECE_NO") + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_PARTY_CODE") + " </td>"
                                + "<td align='left'> " + clsSales_Party.getPartyName(2, rsData.getString("GIDC_PARTY_CODE")) + " </td>"
                                + "<td align='center'> " + rsData.getString("GIDC_GROUP") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_LENGTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_WIDTH") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_THORITICAL_WEIGHT") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_GSM") + " </td>"
                                + "<td align='right'> " + rsData.getString("GIDC_SQMTR") + " </td>"
                                + "<td align='left'> " + rsData.getString("GIDC_PIECE_REMARK") + " </td>";
                        if (rsData.getString("GIDC_SDF_INST_DATE").equals("0000-00-00") || rsData.getString("GIDC_SDF_INST_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SDF_INST_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_SPIRALING_DATE").equals("0000-00-00") || rsData.getString("GIDC_SPIRALING_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_ASSEMBLY_DATE").equals("0000-00-00") || rsData.getString("GIDC_ASSEMBLY_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>";
                        }
                        if (rsData.getString("GIDC_READY_TO_DESPATCH_DATE").equals("0000-00-00") || rsData.getString("GIDC_READY_TO_DESPATCH_DATE").equals("")) {
                            pMessage = pMessage + "<td align='center'>  </td>";
                            pMessage = pMessage + "<td align='right'>  </td>";
                        } else {
                            pMessage = pMessage + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_READY_TO_DESPATCH_DATE")) + " </td>";
                            pMessage = pMessage + "<td align='right'> "+data.getStringValueFromDB("SELECT DATEDIFF(CURDATE(),'"+rsData.getString("GIDC_READY_TO_DESPATCH_DATE")+"')")+" </td>";
                        }
                        pMessage = pMessage + "</tr>";
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_SPIRALING_DATE")) + " </td>"
//                                + "<td align='center'> " + EITLERPGLOBAL.formatDate(rsData.getString("GIDC_ASSEMBLY_DATE")) + " </td>"
//                                + "</tr>";
                        
                        rsData.next();
                    }
                }
                pMessage = pMessage + "</table>";
                
                pMessage += "<br>";

                String recievers = "sunil@dineshmills.com,admn@dineshmills.com,feltpp@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,brdfltfin@dineshmills.com";

//                pMessage = pMessage + "<br><br><br> : Email Send to : <br>";
//                hmSendToList = clsHierarchy.getUserList(EITLERPGLOBAL.gCompanyID, hierarchyId, userId, true);
//                for (int i = 1; i <= hmSendToList.size(); i++) {
//                    clsUser ObjUser = (clsUser) hmSendToList.get(Integer.toString(i));
//                    int U_ID = ObjUser.getAttribute("USER_ID").getInt();
//
//                    String to = clsUser.getExternalEMail(EITLERPGLOBAL.gCompanyID, U_ID);
//
//                    System.out.println("USERID : " + U_ID + ", send_to : " + to);
//                    if (!to.equals("")) {
//                        recievers = recievers + "," + to;
//                        pMessage = pMessage + "<br>" + ObjUser.getAttribute("USER_NAME").getString();
//                    }
//                }
//            recievers = recievers + ",vdshanbhag@dineshmills.com,manoj@dineshmills.com,hcpatel@dineshmills.com,mva@dineshmills.com,atulshah@dineshmills.com,rakeshdalal@dineshmills.com,soumen@dineshmills.com";
//                cc = "aditya@dineshmills.com";

//                pMessage = pMessage + "<br>vdshanbhag@dineshmills.com";
//                pMessage = pMessage + "<br>manoj@dineshmills.com";
//                pMessage = pMessage + "<br>hcpatel@dineshmills.com";
//                pMessage = pMessage + "<br>mva@dineshmills.com";
//                pMessage = pMessage + "<br>atulshah@dineshmills.com";
//                pMessage = pMessage + "<br>rakeshdalal@dineshmills.com";
//                pMessage = pMessage + "<br>soumen@dineshmills.com";

//                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****";// For Live
                pMessage = pMessage + "<br><br>**** This is an auto-generated email, please do not reply ****"; //For Test

                System.out.println("Recivers : " + recievers);
                System.out.println("pSubject : " + pSubject);
//                System.out.println("pMessage : " + pMessage);

                String responce = DailyMailNotification.sendNotificationMail(pSubject, pMessage, recievers, cc);
                System.out.println("Send Mail Responce : " + responce);

            } catch (Exception e) {
                System.out.println("Error on Mail: " + e.getMessage());
            }
    }   
    
}
