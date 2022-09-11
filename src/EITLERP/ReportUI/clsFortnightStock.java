/*
 * clsFortnightStock.java
 *
 * Created on October 13, 2007, 12:35 PM
 */

package EITLERP.ReportUI;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Stores.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import EITLERP.Finance.*;
import java.sql.*;
import java.net.*;
import net.sf.jasperreports.engine.*;
import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.sun.mail.smtp.*;

public class clsFortnightStock {
    
    
    public static void main(String[] args) {
        try {
            
            
            if(args.length<6) {
                System.out.println("Please specify (1) Company ID (2) Database URL (3) Start Date (4) Company Name (5) Job Sr. No. (6) Override Date ");
                return;
            }
            
            EITLERPGLOBAL.gCompanyID=Integer.parseInt(args[0]);
            EITLERPGLOBAL.DatabaseURL=args[1];
            String AsOnDate=args[2];
            String CompanyName=args[3];
            int JobSrNo=Integer.parseInt(args[4]);
            String File="FortnightStock.pdf";
            String FromDate="",ToDate="";
            
            ToDate=AsOnDate;
            FromDate=EITLERPGLOBAL.addDaysToDate(ToDate,-15,"yyyy-MM-dd");
            
            //Override Settings of As On Date
            if(args[5].equals("Y")) {
                AsOnDate=EITLERPGLOBAL.getCurrentDateDB();
                ToDate=EITLERPGLOBAL.getCurrentDateDB();
                FromDate=EITLERPGLOBAL.addDaysToDate(ToDate,-15,"yyyy-MM-dd");
            }
            
            System.out.println("Generating Report as on :"+AsOnDate);
            
            clsFortnightStock objNew=new clsFortnightStock();
            
            System.out.println("Started Generating Report ... ");
            
            objNew.GenerateFortnightReport(FromDate,ToDate);
            
            System.out.println("Report Generated ... ");
            
            //****** Report Generation ***********//
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection tmpConn=DriverManager.getConnection(EITLERPGLOBAL.DatabaseURL,EITLERPGLOBAL.DBUserName,EITLERPGLOBAL.DBPassword);
            
            File reportFile = new File("/EITLERPReports/rptFortnightStockEx.jasper");
            
            Map parameters = new HashMap();
            
            parameters.put("COMPANY_NAME",CompanyName);
            parameters.put("FROM_DATE", FromDate);
            parameters.put("TO_DATE", ToDate);
            
            byte[] bytes =
            JasperRunManager.runReportToPdf(
            reportFile.getPath(),
            parameters,
            tmpConn
            );
            
            FileOutputStream theFile=new FileOutputStream(new File(File));
            theFile.write(bytes);
            theFile.flush();
            theFile.close();
            
            System.out.println("PDF File created ... ");
            //************************************//
            
            //********* EMail this document ***********//
            HashMap recList=new HashMap();  //will contain list of receipients
            Statement stJob;
            ResultSet rsJob;
            
            String PONo="";
            String MailText="";
            String smtpHost=EITLERPGLOBAL.SMTPHostIP;
            String Subject="";
            String From="";
            String CC="";
            String BCC="";
            
            
            stJob=tmpConn.createStatement();
            rsJob=stJob.executeQuery("SELECT * FROM D_COM_SCHEDULE_JOBS WHERE SR_NO="+JobSrNo);
            rsJob.first();
            
            if(rsJob.getRow()>0) {
                
                MailText=rsJob.getString("CONTENT");
                From="noreply@dineshmills.com";
                Subject=rsJob.getString("SUBJECT");
                
                String SendTo=rsJob.getString("SEND_TO");
                
                if(!SendTo.trim().equals("")) {
                    String[] SendToList=SendTo.split(",");
                    
                    for(int l=0;l<SendToList.length;l++) {
                        recList.put(Integer.toString(recList.size()+1),SendToList[l]);
                    }
                }
            }
            
            
            
            for(int i=1;i<=recList.size();i++) {
                
                String to=(String)recList.get(Integer.toString(i));
                
                // Get system properties
                Properties props = System.getProperties();
                
                // Setup mail server
                props.put("mail.smtp.host", smtpHost);
                props.put("mail.smtp.auth", "true");
                
                
                // Get session
                Session Objsession =
                Session.getInstance(props);
                
                
                // Define message
                MimeMessage message = new MimeMessage(Objsession);
                message.setFrom(new InternetAddress(From));
                
                
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                
                if(!CC.trim().equals("")) {
                    message.addRecipient(Message.RecipientType.CC,new InternetAddress(CC));
                }
                
                if(!BCC.trim().equals("")) {
                    message.addRecipient(Message.RecipientType.BCC,new InternetAddress(BCC));
                }
                
                message.setSubject(Subject);
                
                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();
                
                // Fill the message
                messageBodyPart.setText(MailText);
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                
                
                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(File);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(File);
                multipart.addBodyPart(messageBodyPart);
                
                // Put parts in message
                message.setContent(multipart);
                
                // Send message
                Transport tr=Objsession.getTransport("smtp");
                tr.connect(smtpHost,"eitl@dineshmills.com","123456");
                tr.sendMessage(message, message.getAllRecipients());
                tr.close();
            }
            
            System.out.println("Mail sent successfully. ");
            //*****************************************//
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void GenerateFortnightReport(String AsOnDate) {
        try {
            
            String ItemID="";
            String ItemName="";
            double OpeningQty=0,InwardQty=0,IssueQty=0,ClosingQty=0;
            double InspectionQty=0,LastIssueQty=0,CumIssueQty=0,AvgIssueQty=0,LastYearIssueQty=0;
            String FromDate="",ToDate="";
            String LastFromDate="",LastToDate="",LastYearFromDate="",LastYearToDate="";
            String OpeningDate="",FinYearStartDate="";
            int Counter=0;
            
            clsItemStock objItemStock=new clsItemStock();
            
            ToDate=AsOnDate;
            FromDate=EITLERPGLOBAL.addDaysToDate(ToDate,-15,"yyyy-MM-dd");
            
            LastFromDate=EITLERPGLOBAL.addDaysToDate(FromDate,-15,"yyyy-MM-dd");
            LastToDate=EITLERPGLOBAL.addDaysToDate(FromDate,-1,"yyyy-MM-dd");
            
            LastYearFromDate=(EITLERPGLOBAL.getCurrentYear()-1)+"-04-01";
            LastYearToDate=(EITLERPGLOBAL.getCurrentYear())+"-03-31";
            
            FinYearStartDate=EITLERPGLOBAL.getCurrentFinYear()+"-04-01";
            
            Connection tmpConn=data.getConn();
            Statement stTemp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            stTemp.execute("DELETE FROM TMP_GENERAL");
            
            ResultSet rsTemp=stTemp.executeQuery("SELECT * FROM TMP_GENERAL");
            
            
            ResultSet rsItem=data.getResult("SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ABC='A' AND CANCELLED=0 ");
            rsItem.first();
            
            
            if(rsItem.getRow()>0) {
                while(!rsItem.isAfterLast()) {
                    
                    ItemID=UtilFunctions.getString(rsItem,"ITEM_ID","");
                    ItemName=UtilFunctions.getString(rsItem,"ITEM_DESCRIPTION","");
                    
                    OpeningQty=objItemStock.getOnHandQtyOnEx(EITLERPGLOBAL.gCompanyID, ItemID,EITLERPGLOBAL.addDaysToDate(FromDate, -1,"yyyy-MM-dd"));
                    
                    if(OpeningQty<0) {
                        OpeningQty=0;
                    }
                    
                    //InwardQty=EITLERPGLOBAL.round(objItemStock.getMIRInwardQty(ItemID, FromDate, ToDate),2);
                    
                    IssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID,FromDate,ToDate),2);
                    
                    LastIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID, LastFromDate,LastToDate),2);
                    
                    ClosingQty=OpeningQty+InwardQty-IssueQty;
                    
                    //InspectionQty=EITLERPGLOBAL.round(objItemStock.getInspectionQty(ItemID, FromDate,ToDate),2);
                    
                    CumIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID, FinYearStartDate, FromDate),2);
                    
                    int MonthCount=EITLERPGLOBAL.getMonthCount()-1;
                    
                    int Day=UtilFunctions.CInt(AsOnDate.substring(8));
                    
                    if(Day<=15) {
                        MonthCount--;
                    }
                    
                    AvgIssueQty=EITLERPGLOBAL.round(CumIssueQty/MonthCount,2);
                    
                    LastYearIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueQty(ItemID, LastYearFromDate,LastYearToDate)/12,2);
                    
                    Counter++;
                    
                    rsTemp.moveToInsertRow();
                    rsTemp.updateLong("SR_NO",Counter);
                    rsTemp.updateString("COLUMN_1",ItemID);
                    rsTemp.updateString("COLUMN_2",ItemName);
                    rsTemp.updateString("COLUMN_3",Double.toString(InspectionQty));
                    rsTemp.updateString("COLUMN_4",Double.toString(InwardQty));
                    rsTemp.updateString("COLUMN_5",Double.toString(IssueQty));
                    rsTemp.updateString("COLUMN_6",Double.toString(ClosingQty));
                    rsTemp.updateString("COLUMN_7",Double.toString(LastIssueQty));
                    rsTemp.updateString("COLUMN_8",Double.toString(CumIssueQty));
                    rsTemp.updateString("COLUMN_9",Double.toString(AvgIssueQty));
                    rsTemp.updateString("COLUMN_10",Double.toString(LastYearIssueQty));
                    rsTemp.updateString("COLUMN_11",Double.toString(OpeningQty));
                    rsTemp.insertRow();
                    
                    
                    rsItem.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
    }
    
    public void GenerateFortnightReport(String FromDate,String ToDate) {
        try {
            
            String ItemID="";
            String ItemName="";
            double OpeningQty=0,InwardQty=0,IssueQty=0,ClosingQty=0;
            double InspectionQty=0,LastIssueQty=0,CumIssueQty=0,AvgIssueQty=0,LastYearIssueQty=0;
            String LastFromDate="",LastToDate="",LastYearFromDate="",LastYearToDate="";
            String OpeningDate="",FinYearStartDate="";
            int Counter=0;
            
            clsItemStock objItemStock=new clsItemStock();
            
            LastFromDate=EITLERPGLOBAL.addDaysToDate(FromDate,-15,"yyyy-MM-dd");
            LastToDate=EITLERPGLOBAL.addDaysToDate(FromDate,-1,"yyyy-MM-dd");
            
            LastYearFromDate=(EITLERPGLOBAL.getCurrentYear()-1)+"-04-01";
            LastYearToDate=(EITLERPGLOBAL.getCurrentYear())+"-03-31";
            
            FinYearStartDate=EITLERPGLOBAL.getCurrentFinYear()+"-04-01";
            
            Connection tmpConn=data.getConn();
            Statement stTemp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            stTemp.execute("DELETE FROM TMP_GENERAL");
            
            ResultSet rsTemp=stTemp.executeQuery("SELECT * FROM TMP_GENERAL");
            
            
            ResultSet rsItem=data.getResult("SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ABC='A' AND CANCELLED=0 ");
            rsItem.first();
            
            
            if(rsItem.getRow()>0) {
                while(!rsItem.isAfterLast()) {
                    
                    ItemID=UtilFunctions.getString(rsItem,"ITEM_ID","");
                    ItemName=UtilFunctions.getString(rsItem,"ITEM_DESCRIPTION","");
                    
                    if(ItemID.equals("20201052")) {
                        boolean halt=true;
                    }
                    
                    OpeningQty=objItemStock.getOnHandQtyOnEx(EITLERPGLOBAL.gCompanyID, ItemID,EITLERPGLOBAL.addDaysToDate(FromDate, -1,"yyyy-MM-dd"));
                    
                    /*if(OpeningQty<0) {
                        OpeningQty=0;
                    }*/
                    
                    //InwardQty=EITLERPGLOBAL.round(objItemStock.getMIRInwardQty(ItemID, FromDate, ToDate),2);
                    
                    IssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID,FromDate,ToDate),2);
                    
                    LastIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID, LastFromDate,LastToDate),2);
                    
                    ClosingQty=OpeningQty+InwardQty-IssueQty;
                    
                    //InspectionQty=EITLERPGLOBAL.round(objItemStock.getInspectionQty(ItemID, FromDate,ToDate),2);
                    
                    CumIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueReqQty(ItemID, FinYearStartDate, FromDate),2);
                    
                    AvgIssueQty=EITLERPGLOBAL.round(CumIssueQty/EITLERPGLOBAL.getMonthCount(),2);
                    
                    LastYearIssueQty=EITLERPGLOBAL.round(objItemStock.getIssueQty(ItemID, LastYearFromDate,LastYearToDate)/12,2);
                    
                    Counter++;
                    
                    rsTemp.moveToInsertRow();
                    rsTemp.updateLong("SR_NO",Counter);
                    rsTemp.updateString("COLUMN_1",ItemID);
                    rsTemp.updateString("COLUMN_2",ItemName);
                    rsTemp.updateString("COLUMN_3",Double.toString(InspectionQty));
                    rsTemp.updateString("COLUMN_4",Double.toString(InwardQty));
                    rsTemp.updateString("COLUMN_5",Double.toString(IssueQty));
                    rsTemp.updateString("COLUMN_6",Double.toString(ClosingQty));
                    rsTemp.updateString("COLUMN_7",Double.toString(LastIssueQty));
                    rsTemp.updateString("COLUMN_8",Double.toString(CumIssueQty));
                    rsTemp.updateString("COLUMN_9",Double.toString(AvgIssueQty));
                    rsTemp.updateString("COLUMN_10",Double.toString(LastYearIssueQty));
                    rsTemp.updateString("COLUMN_11",Double.toString(OpeningQty));
                    rsTemp.insertRow();
                    
                    
                    rsItem.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
    }
    
    public void GenerateFortnightReport(int FortnightNo, String FromDate,String ToDate, String Year) {
        try {
            //=====Fortnight Items Detail Data=======//
            String ItemID="",ItemName="",ItemDesc="",Unit="";
            double RECMN_MONTHLY_CONS=0,RECMN_STOCK_LEVEL=0;
            //-----Fortnight Items Detail Data-------//
            
            //=====Fortnight Header Data=======//
            double OpeningQty=0,MIRQty=0,GRNQty=0,IssueQty=0,STMQty=0,ClosingQty=0;
            double IssuedLastFortnightQty=0,CumulativeIssuedQty=0,AvgIssuePerMonth=0,LastYearAvgConsumption=0;
            String FinYearStartDate="",LastFromDate="",LastToDate="",LastYearFromDate="",LastYearToDate="";
            //-----Fortnight Header Data------//
            
            int Counter=0;
            String strSQL = "";
            
            clsItemStock objItemStock = new clsItemStock();
            clsStockInfo objStockInfo = new clsStockInfo();
            Connection ConnFortnight=data.getConn();
            Statement stFortnight=ConnFortnight.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stFortnight.execute("DELETE FROM D_INV_FORTNIGHT_HEADER WHERE FORTNIGHT_NO="+FortnightNo);
            ResultSet rsFortnight=stFortnight.executeQuery("SELECT * FROM D_INV_FORTNIGHT_HEADER ");
            
            ResultSet rsItem=data.getResult("SELECT * FROM D_INV_FORTNIGHT_ITEMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY ITEM_ID");
            rsItem.first();
            
            Connection ConnFortnightD=data.getConn();
            Statement stFortnightD=ConnFortnightD.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stFortnightD.execute("DELETE FROM D_INV_FORTNIGHT_DETAIL WHERE FORTNIGHT_NO="+FortnightNo+" AND YEAR='"+Year+"' ");
            ResultSet rsFortnightD=stFortnightD.executeQuery("SELECT * FROM D_INV_FORTNIGHT_DETAIL ");
            
            FinYearStartDate=EITLERPGLOBAL.getFinYearStartDate(FromDate);
            LastYearFromDate = EITLERPGLOBAL.getFinYearStartDate(clsCalcInterest.addMonthToDate(FromDate,-12));
            LastYearToDate = EITLERPGLOBAL.getFinYearEndDate(clsCalcInterest.addMonthToDate(FromDate,-12));
            
            if(rsItem.getRow()>0) {
                while(!rsItem.isAfterLast()) {
                    
                    ItemID = UtilFunctions.getString(rsItem,"ITEM_ID","");
//                    if(ItemID.equals("20201015")) {
//                        System.out.println();
//                    }
                    ItemName = UtilFunctions.getString(rsItem,"ITEM_NAME","");
                    ItemDesc = UtilFunctions.getString(rsItem,"ITEM_DESC","");
                    Unit = UtilFunctions.getString(rsItem,"UNIT","");
                    RECMN_MONTHLY_CONS = UtilFunctions.getDouble(rsItem,"RECMN_MONTHLY_CONS",0);
                    RECMN_STOCK_LEVEL = UtilFunctions.getDouble(rsItem,"RECMN_STOCK_LEVEL",0);
                    
                    objStockInfo = objItemStock.getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID,EITLERPGLOBAL.addDaysToDate(ToDate, 1,"yyyy-MM-dd"));
                    ClosingQty = EITLERPGLOBAL.round(objStockInfo.StockQty,3);
                    
                    MIRQty = EITLERPGLOBAL.round(objItemStock.getMIRQtyForFortnight(ItemID, ToDate),3);
                    
                    GRNQty = EITLERPGLOBAL.round(objItemStock.getGRNQtyForFortnight(ItemID, FromDate, ToDate),3);
                    
                    IssueQty=EITLERPGLOBAL.round(objItemStock.getIssueQtyForFortnight(ItemID,FromDate,ToDate),3);
                    
                    STMQty=EITLERPGLOBAL.round(objItemStock.getSTMQtyForFortnight(ItemID,FinYearStartDate,ToDate),3);
                    
                    OpeningQty = ClosingQty - GRNQty + IssueQty + STMQty;
                    
                    strSQL = "SELECT ISSUED_LAST_FORTNIGHT FROM D_INV_FORTNIGHT_HEADER WHERE FORTNIGHT_NO="+(FortnightNo-1)+" AND ITEM_ID='"+ItemID+"' AND ITEM_ID='"+ItemID+"'";
                    IssuedLastFortnightQty=EITLERPGLOBAL.round(data.getDoubleValueFromDB(strSQL),3);
                    
                    CumulativeIssuedQty=EITLERPGLOBAL.round(objItemStock.getIssueQtyForFortnight(ItemID,FinYearStartDate,ToDate),3);
                    
                    double NoofMonth = (double)FortnightNo/2;
                    AvgIssuePerMonth=EITLERPGLOBAL.round(CumulativeIssuedQty/NoofMonth,3);;
                    
                    LastYearAvgConsumption=EITLERPGLOBAL.round(objItemStock.getIssueQty(ItemID, LastYearFromDate,LastYearToDate)/12,3);
                    
                    Counter++;
                    
                    rsFortnight.moveToInsertRow();
                    rsFortnight.updateLong("FORTNIGHT_NO",FortnightNo);
                    rsFortnight.updateString("YEAR",Year);
                    rsFortnight.updateInt("SR_NO",Counter);
                    rsFortnight.updateString("ITEM_ID",ItemID);
                    rsFortnight.updateString("ITEM_DESC",ItemDesc);
                    rsFortnight.updateString("UNIT_OF_MESUREMENT",Unit);
                    rsFortnight.updateDouble("OPENING_STOCK_QTY",OpeningQty);
                    rsFortnight.updateDouble("MIR_QTY",MIRQty);
                    rsFortnight.updateDouble("GRN_QTY",GRNQty);
                    rsFortnight.updateDouble("ISSUE_QTY",IssueQty);
                    rsFortnight.updateDouble("STM_QTY",STMQty);
                    rsFortnight.updateDouble("CLOSING_STOCK_QTY",ClosingQty);
                    rsFortnight.updateDouble("ISSUED_LAST_FORTNIGHT",IssuedLastFortnightQty);
                    rsFortnight.updateDouble("CUMULATIVE_ISSUE_QTY",CumulativeIssuedQty);
                    rsFortnight.updateDouble("AVG_ISSUE_PER_MONTH",AvgIssuePerMonth);
                    rsFortnight.updateDouble("LAST_YEAR_AVG_CONSUMPTION",LastYearAvgConsumption);
                    rsFortnight.updateDouble("RECMN_MONTHLY_CONS",RECMN_MONTHLY_CONS);
                    rsFortnight.updateDouble("RECMN_STOCK_LEVEL",RECMN_STOCK_LEVEL);
                    rsFortnight.updateBoolean("LOCKED",false);
                    rsFortnight.updateString("LOCKED_DATE","0000-00-00");
                    rsFortnight.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsFortnight.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFortnight.updateBoolean("CHANGED",true);
                    rsFortnight.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFortnight.insertRow();
                    
                    
                    String strPendingPO = "SELECT A.PO_NO, A.PO_DATE, B.ITEM_ID, B.SR_NO, B.DELIVERY_DATE, " +
                    "SUM(IF(N.RECEIVED_QTY-N.REJECTED_QTY IS NULL,0,N.RECEIVED_QTY-N.REJECTED_QTY)) + SUM(IF(M.RECEIVED_QTY-M.REJECTED_QTY IS NULL,0,M.RECEIVED_QTY-M.REJECTED_QTY)) AS RECD_QTY," +
                    "B.QTY,D.TOLERANCE_LIMIT FROM D_PUR_PO_HEADER A, D_PUR_PO_DETAIL B " +
                    "LEFT JOIN D_INV_ITEM_MASTER AS D ON (B.COMPANY_ID = D.COMPANY_ID AND B.ITEM_ID = D.ITEM_ID) " +
                    "LEFT JOIN D_INV_JOB_DETAIL N ON (N.PO_NO=B.PO_NO  AND N.PO_SR_NO=B.SR_NO AND N.PO_TYPE=B.PO_TYPE " +
                    "AND N.JOB_NO IN (SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE JOB_NO=N.JOB_NO  AND APPROVED=1 AND CANCELLED=0)) " +
                    "LEFT JOIN D_INV_MIR_DETAIL M ON (M.PO_NO=B.PO_NO  AND M.PO_SR_NO=B.SR_NO AND M.PO_TYPE=B.PO_TYPE " +
                    "AND M.MIR_NO IN (SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO=M.MIR_NO  AND APPROVED=1 AND CANCELLED=0)) " +
                    "WHERE B.COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" AND A.COMPANY_ID = B.COMPANY_ID AND B.PO_NO = A.PO_NO AND A.APPROVED =1 " +
                    "AND A.CANCELLED=0 AND A.PO_DATE >= '"+FromDate+"' AND A.PO_DATE <= '"+ToDate+"' AND B.ITEM_ID='"+ ItemID + "' " +
                    "GROUP BY B.PO_NO,B.PO_TYPE,B.SR_NO " +
                    "HAVING (B.QTY-(SUM(IF(N.RECEIVED_QTY-N.REJECTED_QTY IS NULL,0,N.RECEIVED_QTY-N.REJECTED_QTY)) " +
                    "+SUM(IF(M.RECEIVED_QTY-M.REJECTED_QTY IS NULL,0,M.RECEIVED_QTY-M.REJECTED_QTY))))>((B.QTY*D.TOLERANCE_LIMIT)/100) " +
                    "ORDER BY A.PO_DATE,A.PO_NO,B.SR_NO ";
                    
                    ResultSet rsPendingPO = data.getResult(strPendingPO);
                    rsPendingPO.first();
                    int DetailCounter=0;
                    if(rsPendingPO.getRow() > 0) {
                        while(!rsPendingPO.isAfterLast()) {
                            DetailCounter++;
                            
                            rsFortnightD.moveToInsertRow();
                            rsFortnightD.updateLong("FORTNIGHT_NO",FortnightNo);
                            rsFortnightD.updateString("YEAR",Year);
                            rsFortnightD.updateInt("SR_NO",DetailCounter);
                            rsFortnightD.updateString("ITEM_ID",ItemID);
                            rsFortnightD.updateString("PO_NO",rsPendingPO.getString("PO_NO"));
                            rsFortnightD.updateString("PO_DATE",rsPendingPO.getString("PO_DATE"));
                            rsFortnightD.updateDouble("PO_QTY",rsPendingPO.getDouble("QTY"));
                            double BalanceQty = EITLERPGLOBAL.round(rsPendingPO.getDouble("QTY")-rsPendingPO.getDouble("RECD_QTY"), 2);
                            rsFortnightD.updateDouble("BAL_QTY", BalanceQty);
                            rsFortnightD.updateString("DELIVERY_DATE",rsPendingPO.getString("DELIVERY_DATE"));
                            rsFortnightD.insertRow();
                            
                            rsPendingPO.next();
                        }
                    }
                    rsItem.next();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
