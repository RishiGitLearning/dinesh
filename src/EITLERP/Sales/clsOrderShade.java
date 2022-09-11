/*
 * clsOrderShade.java
 * Created on December 21, 2011, 12:21:00 PM
 */

package EITLERP.Sales;
/**
 *
 * @author  root
 */
import java.util.*;
import java.sql.*;
import java.io.*;
import EITLERP.*;

public class clsOrderShade {
    /** Creates a new instance of clsOrderShade */
    public static String FromDate ="";
    public static String ToDate ="";
    public clsOrderShade() {
    }
    
    public static void main(String[] args) {
        try {
            if(args.length<2) {
                System.out.println("Insufficient arguments. Please specify \n 1. Season Start Date (DD/MM/YYYY)  \n2. Season End Date (DD/MM/YYYY) ");
                return;
            }
            
            FromDate=EITLERPGLOBAL.formatDateDB(args[0]);
            ToDate=EITLERPGLOBAL.formatDateDB(args[1]);
            
            clsOrderShade objExport=new clsOrderShade();
            objExport.ExportSalesOrders();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean ExportSalesOrders() {
        try {
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            System.out.println("Opening database connection ");
            data.OpenGlobalConnection(dbURL);
            System.out.println("Database connection open");
            
            Connection objConn2=data.getConn();
            Statement stTmpDel=objConn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            int delete = stTmpDel.executeUpdate("DELETE FROM TMP_ORDER_SHADE");
            if (delete==0) {
                System.out.println("All Rows are deleted");
            }
            objConn2.close();
            
            //Read the Newly placed orders
            ResultSet rsOrder=data.getResult("SELECT * FROM D_SAL_ORDER_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND ORDER_DATE>='"+FromDate+"' AND ORDER_DATE=<'"+ToDate+"' "); //AND (STATUS='' OR STATUS IS NULL)
            rsOrder.first();
            
            if(rsOrder.getRow()>0) {
                while(!rsOrder.isAfterLast()) {
                    //Check for the
                    
                    System.out.println("Exporting Order No. "+rsOrder.getString("ORDER_NO"));
                    String OrderNo="";
                    if(rsOrder.getString("ORDER_NO").length()==8){
                        OrderNo=rsOrder.getString("ORDER_NO");
                    }
                    if(rsOrder.getString("ORDER_NO").length()==7){
                        OrderNo=rsOrder.getString("ORDER_NO");
                    }
                    String OrderDate=rsOrder.getString("ORDER_DATE").substring(8,10)+rsOrder.getString("ORDER_DATE").substring(5,7)+rsOrder.getString("ORDER_DATE").substring(2,4);
                    
                    ResultSet rsOrderDetail=data.getResult("SELECT * FROM D_SAL_ORDER_DETAIL WHERE ORDER_NO='"+rsOrder.getString("ORDER_NO")+"'");
                    rsOrderDetail.first();
                    
                    Connection objConn=data.getConn();
                    Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    ResultSet rsTmp=stTmp.executeQuery("SELECT * FROM TMP_ORDER_SHADE");
                    rsTmp.first();
                    
                    if(rsOrderDetail.getRow()>0) {
                        while(!rsOrderDetail.isAfterLast()) {
                            
                            String QualityNo=EITLERPGLOBAL.padRightEx(rsOrderDetail.getString("QUALITY_ID"), "0", 6);
                            String OrderShades=(rsOrderDetail.getString("SHADE_ID"));
                            String shades[]=(rsOrderDetail.getString("SHADE_ID").replace(".",",")).split(",");
                            
                            for(int i=0;i<shades.length;i++) {
                                int ShadeCounter=i;
                                int Counter = 0;
                                String Sh1="  ";
                                String Unit1="  ";
                                String TotUnits=" ";
                                //(1)
                                if(ShadeCounter<shades.length) {
                                    Sh1=shades[ShadeCounter];
                                    if(Sh1.length()<=2) {
                                        Sh1=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit1="01";
                                        TotUnits="0001";
                                    } else {
                                        String ShUnit[] =Sh1.split("/");
                                        Sh1=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit1=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                        TotUnits=EITLERPGLOBAL.padLeftEx(Unit1, "0", 4);
                                    }
                                }
                                String shade_1;
                                shade_1=Sh1+Unit1;
                                rsTmp.moveToInsertRow();
                                //********* Write Final Record to the TMP_ORDER_SHADE TABLE **********//
                                rsTmp.updateString("ORDER_NO", OrderNo);
                                rsTmp.updateString("QUALITY_ID", QualityNo);
                                rsTmp.updateString("TOTAL_SHADE", OrderShades);
                                rsTmp.updateString("SHADE_1", shade_1);
                                rsTmp.updateString("SHADE", Sh1);
                                rsTmp.updateString("UNITS", Unit1);
                                rsTmp.updateString("TOTAL_UNITS", TotUnits);
                                rsTmp.insertRow();
                            }
                            rsOrderDetail.next();
                        }
                    }
                    rsOrder.next();
                }
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}