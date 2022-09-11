/*
 * clsSOExport.java
 *
 * Created on June 25, 2008, 12:22 PM
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

public class clsSOExport {
    
    
    /** Creates a new instance of clsSOExport */
    public clsSOExport() {
    }
    
    public static void main(String[] args) {
        try {
            
            if(args.length<=0) {
                System.out.println("Please specify the directory to put exported file");
                return;
            }
            
            clsSOExport objExport=new clsSOExport();
            
            objExport.ExportSalesOrders(args[0]);
            
            /*clsSOExport objExport=new clsSOExport();
            
            objExport.ExportSalesOrders("/root/Desktop");*/
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public boolean ExportSalesOrders(String InitialDir) {
        try {
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            String FileName="so"+Integer.toString(EITLERPGLOBAL.getCurrentYear())+Integer.toString(EITLERPGLOBAL.getCurrentMonth())+Integer.toString(EITLERPGLOBAL.getCurrentDay());
            BufferedWriter objFile=new BufferedWriter(new FileWriter(new File(InitialDir+"/"+FileName),true));
            
            
            System.out.println("Opening database connection ");
            data.OpenGlobalConnection(dbURL);
            System.out.println("Database connection open");
            
            //Read the Newly placed orders
           // ResultSet rsOrder=data.getResult("SELECT * FROM D_SAL_ORDER_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND (STATUS='' OR STATUS IS NULL) ");
            
            //Rishi 
              ResultSet rsOrder=data.getResult("SELECT DISTINCT A.ORDER_NO,A.PARTY_CODE,ORDER_DATE,CITY_ID,TRANSPORTER_ID FROM D_SAL_ORDER_HEADER A,D_SAL_ORDER_DETAIL B WHERE A.ORDER_NO = B.ORDER_NO AND APPROVED=1 AND  A.CANCELLED=0 AND QUALITY_ID NOT LIKE ('72%') AND (STATUS='' OR STATUS IS NULL) ");
  
            
            rsOrder.first();
            
            if(rsOrder.getRow()>0) {
                while(!rsOrder.isAfterLast()) {
                    //Check for the
                    
                    System.out.println("Exporting Order No. "+rsOrder.getString("ORDER_NO"));
                    
                    String UPDTCode="A";
                    String TransCode="OF";
                    String PartyCode= EITLERPGLOBAL.padRightEx(rsOrder.getString("PARTY_CODE")," ",6);
                    String OrderNo="";
                    if(rsOrder.getString("ORDER_NO").length()==8){                    
                    OrderNo=rsOrder.getString("ORDER_NO").substring(2,8);
                    }
                    if(rsOrder.getString("ORDER_NO").length()==7){                    
                    OrderNo=rsOrder.getString("ORDER_NO").substring(1,7);
                    }
                    String OrderDate=rsOrder.getString("ORDER_DATE").substring(8,10)+rsOrder.getString("ORDER_DATE").substring(5,7)+rsOrder.getString("ORDER_DATE").substring(2,4);
                    String Station=EITLERPGLOBAL.padRightEx(rsOrder.getString("CITY_ID")," ", 15);
                    String TransMode="2";
                    
                    if(rsOrder.getInt("TRANSPORTER_ID")==95) //Angadia
                    {
                        TransMode="5";
                    }
                    
                    if(rsOrder.getInt("TRANSPORTER_ID")==99) //By Air
                    {
                        TransMode="6";
                    }
                    
                    
                  //  ResultSet rsOrderDetail=data.getResult("SELECT * FROM D_SAL_ORDER_DETAIL WHERE ORDER_NO='"+rsOrder.getString("ORDER_NO")+"'");
            
                    //RISHI 
                    ResultSet rsOrderDetail=data.getResult("SELECT * FROM D_SAL_ORDER_DETAIL WHERE QUALITY_ID NOT LIKE ('72%') AND ORDER_NO='"+rsOrder.getString("ORDER_NO")+"'");
                    
                    
                    rsOrderDetail.first();
                    
                    if(rsOrderDetail.getRow()>0) {
                        
                        while(!rsOrderDetail.isAfterLast()) {
                            
                            String QualityNo=EITLERPGLOBAL.padRightEx(rsOrderDetail.getString("QUALITY_ID"), "0", 7);
                            
                            String TotalUnits=EITLERPGLOBAL.padLeftEx(Integer.toString(rsOrderDetail.getInt("TOTAL_UNITS")), "0", 4);
                            String UnitCode =EITLERPGLOBAL.padLeftEx(rsOrderDetail.getString("UNIT_ID"), "0", 2);
                            String Discount="0000";
                            String DeliveryFrom=rsOrderDetail.getString("DELIVERY_DATE").substring(5,7)+rsOrderDetail.getString("DELIVERY_DATE").substring(2,4);
                            String DeliveryTo=rsOrderDetail.getString("DELIVERY_TO_DATE").substring(5,7)+rsOrderDetail.getString("DELIVERY_TO_DATE").substring(2,4);
                            String FileNo="   ";
                            String SelInd=" ";
                            String PSTNo=EITLERPGLOBAL.padRightEx(rsOrderDetail.getString("MSR_UNIT_ID_1").trim()+","+rsOrderDetail.getString("MSR_UNIT_ID_2").trim(), " ", 12);
                            
                            
                            //String shades[]=rsOrderDetail.getString("SHADE_ID").split(",");
                            
                            //below line is commented by vivek on 17-01-2013 due to space problem in shade
                            //String shades[]=(rsOrderDetail.getString("SHADE_ID").replace(".",",")).split(",");
                            
                            //below line is added by vivek on 17-01-2013 due to space problem in shade
                            String shades[]=(rsOrderDetail.getString("SHADE_ID").replace(" ","").replace(".",",")).split(",");

                            
                            for(int i=0;i<shades.length;i+=10) {
                                
                                int ShadeCounter=i;
                                int Counter = 0;
                                
                                String Sh1="  ";
                                String Unit1="  ";
                                String Sh2="  ";
                                String Unit2="  ";
                                String Sh3="  ";
                                String Unit3="  ";
                                String Sh4="  ";
                                String Unit4="  ";
                                String Sh5="  ";
                                String Unit5="  ";
                                String Sh6="  ";
                                String Unit6="  ";
                                String Sh7="  ";
                                String Unit7="  ";
                                String Sh8="  ";
                                String Unit8="  ";
                                String Sh9="  ";
                                String Unit9="  ";
                                String Sh10="  ";
                                String Unit10="  ";
                                
                                
                                //(1)
                                if(ShadeCounter<shades.length) {
                                    
                                    Sh1=shades[ShadeCounter];
                                    if(Sh1.length()<=2)
                                    {Sh1=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit1="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh1.split("/");
                                        Sh1=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit1=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(2)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh2=shades[ShadeCounter];
                                    
                                    if(Sh2.length()<=2)
                                    {Sh2=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit2="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh2.split("/");
                                        Sh2=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit2=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(3)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh3=shades[ShadeCounter];
                                    if(Sh3.length()<=2)
                                    {   Sh3=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit3="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh3.split("/");
                                        Sh3=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit3=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(4)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh4 = shades[ShadeCounter];
                                    if(Sh4.length()<=2)
                                    {   Sh4=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit4="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh4.split("/");
                                        Sh4=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit4=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(5)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh5=shades[ShadeCounter];
                                    
                                    if(Sh5.length()<=2)
                                    {  Sh5=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit5="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh5.split("/");
                                        Sh5=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit5=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(6)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh6 =shades[ShadeCounter];
                                    if(Sh6.length()<=2)
                                    {Sh6=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit6="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh6.split("/");
                                        Sh6=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit6=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(7)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    
                                    Sh7=shades[ShadeCounter];
                                    if(Sh7.length()<=2)
                                    {Sh7=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit7="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh7.split("/");
                                        Sh7=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit7=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(8)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh8=shades[ShadeCounter];
                                    
                                    if(Sh8.length()<=2)
                                    {Sh8=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit8="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh8.split("/");
                                        Sh8=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit8=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(9)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh9=shades[ShadeCounter];
                                    if(Sh9.length()<=2)
                                    {Sh9=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit9="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh9.split("/");
                                        Sh9=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit9=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                //(10)
                                if(ShadeCounter+1<shades.length) {
                                    ShadeCounter++;
                                    Sh10=shades[ShadeCounter];
                                    
                                    if(Sh10.length()<=2)
                                    {Sh10=EITLERPGLOBAL.padLeftEx(shades[ShadeCounter], "0", 2);
                                        Unit10="01";}
                                    else
                                    {   
                                        String ShUnit[] =Sh10.split("/");
                                        Sh10=EITLERPGLOBAL.padLeftEx(ShUnit[Counter].trim(), "0", 2);
                                        Unit10=EITLERPGLOBAL.padLeftEx(ShUnit[Counter+1].trim(), "0", 2);
                                    }    
                                }
                                
                                
                                //********* Write Final Record to the file **********//
                                objFile.write(UPDTCode);
                                objFile.write(TransCode);
                                objFile.write(PartyCode);
                                objFile.write(OrderNo);
                                objFile.write(OrderDate);
                                objFile.write(Station);
                                objFile.write(QualityNo);
                                objFile.write(TotalUnits);
                                objFile.write(UnitCode);
                                objFile.write(Discount);
                                objFile.write(DeliveryFrom);
                                objFile.write(DeliveryTo);
                                objFile.write(Sh1);
                                objFile.write(Unit1);
                                objFile.write(Sh2);
                                objFile.write(Unit2);
                                objFile.write(Sh3);
                                objFile.write(Unit3);
                                objFile.write(Sh4);
                                objFile.write(Unit4);
                                objFile.write(Sh5);
                                objFile.write(Unit5);
                                objFile.write(Sh6);
                                objFile.write(Unit6);
                                objFile.write(Sh7);
                                objFile.write(Unit7);
                                objFile.write(Sh8);
                                objFile.write(Unit8);
                                objFile.write(Sh9);
                                objFile.write(Unit9);
                                objFile.write(Sh10);
                                objFile.write(Unit10);
                                objFile.write(TransMode);
                                objFile.write(FileNo);
                                objFile.write(SelInd);
                                objFile.write(PSTNo);
                                objFile.newLine();
                                //***************************************************//
                            }
                            
                            rsOrderDetail.next();
                        }
                    }
                    
                    data.Execute("UPDATE D_SAL_ORDER_HEADER SET STATUS='S' WHERE ORDER_NO='"+rsOrder.getString("ORDER_NO")+"'");
                    
                    rsOrder.next();
                }
                
            }
            
            System.out.println("Order Export completed successfully ...");
            objFile.close();
        
            return true;
        }
        catch(Exception e) {
            
            e.printStackTrace();
            
            return false;
        }
        
    }
    
}
