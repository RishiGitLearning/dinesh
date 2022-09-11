/*
 * clsSOImport.java
 *
 * Created on July 21, 2008, 1:31 PM
 */

package EITLERP.Sales;
import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  Prathmesh Shah
 * DON'T ASK QUESTIONS IF YOU FIND ANY ERRORS. QUESTIONS ARE STRICTLY PROHIBITED.
 */
public class clsSalesSeasonImport_old {
    
    /** Creates a new instance of clsSOImport */
    public String NewOrdNO="";
    public int totalUnit=0;
    public int of41totalUnit=0;
    
    public clsSalesSeasonImport_old() {
        System.gc();
    }
    
    public static void main(String args[]) {
        try {
            
            /*if(args.length<=0) {
                System.out.println("Please specify the directory to put exported file");
                return;
            }*/
            
            clsSalesSeasonImport objImport=new clsSalesSeasonImport();
            
            //objImport.ImportSalesOrders(args[0]);
            objImport.ImportSalesOrders("/data/transfer");
            System.out.println("THE END");
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean ImportSalesOrders(String InitialDir) {
        try {
            
            String dbURL="jdbc:mysql://localhost:3306/DINESHMILLS";
            //String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            data.OpenGlobalConnection(dbURL);
            Connection objConn=data.getConn();
            
            String FileName="Winter2009_Final";
            BufferedReader objBR=  new BufferedReader(new FileReader(InitialDir+"/"+FileName+".csv"));
            
            Statement stPriceListDtl=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsPriceListDtl=stPriceListDtl.executeQuery("SELECT * FROM D_SAL_PRICE_LIST_DETAIL");
            
            Statement stPriceListRate=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsPriceListRate=stPriceListRate.executeQuery("SELECT * FROM D_SAL_PRICE_LIST_RATE");
            
            Statement stQualityMst=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsQualityMst=stQualityMst.executeQuery("SELECT * FROM D_SAL_QUALITY_MASTER");
            
            //int Counter =1;
            System.out.println("Importing Records Start ...... ");
            String Line;
            String[] LineCol;
            
            //price list detail table
            while(true) {
                Line= objBR.readLine();
                LineCol=Line.split("~");
                
                try {
                    rsPriceListDtl.moveToInsertRow();
                    rsPriceListDtl.updateInt("COMPANY_ID", 2);
                    rsPriceListDtl.updateString("PRICE_LIST_NO", "PLW09");
                    rsPriceListDtl.updateInt("SR_NO", Integer.parseInt(LineCol[2].toString().trim()));
                    rsPriceListDtl.updateString("SUB_SR_NO", LineCol[2]);
                    rsPriceListDtl.updateString("BRAND_ID", LineCol[0]);
                    rsPriceListDtl.updateString("QUALITY_ID", LineCol[3]);
                    rsPriceListDtl.updateDouble("EX_MILL_RATE", Double.parseDouble(LineCol[5].toString().trim()));
                    rsPriceListDtl.updateString("DELIVERY_FROM_DATE", ConvertDateDB(LineCol[8]));
                    rsPriceListDtl.updateString("DELIVERY_TO_DATE", ConvertDateDB(LineCol[9]));
                    
                    //code for shade saperated by comma
                    String Shades="";
                    for (int i=10;i<LineCol.length;i++) {
                        if (! LineCol[i].equals("")) {
                            Shades += LineCol[i] + ",";
                        }
                    }
                    
                    Shades = Shades.substring(0,Shades.length()-1);
                    
                    rsPriceListDtl.updateString("SHADE_ID", Shades);
                    rsPriceListDtl.updateString("EFFECTIVE_FROM_DATE", ConvertDateDB(LineCol[8]));
                    
                    rsPriceListDtl.updateString("CREATED_BY", "1");
                    rsPriceListDtl.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsPriceListDtl.updateString("MODIFIED_BY", "");
                    rsPriceListDtl.updateString("MODIFIED_DATE", "0000-00-00");
                    rsPriceListDtl.updateInt("HIERARCHY_ID", 0);
                    rsPriceListDtl.updateInt("APPROVED", 1);
                    rsPriceListDtl.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsPriceListDtl.updateInt("REJECTED", 0);
                    rsPriceListDtl.updateString("REJECTED_DATE", "0000-00-00");
                    rsPriceListDtl.updateInt("CHANGED", 1);
                    rsPriceListDtl.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsPriceListDtl.updateInt("CANCELLED", 0);
                    
                    rsPriceListDtl.insertRow();
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                //price list rate
                
                try {
		    double Rate1 = Double.parseDouble(LineCol[5].toString().trim());
                    double Rate2 = Double.parseDouble(LineCol[6].toString().trim());
                    if (Rate1 == Rate2) {
			rsPriceListRate.moveToInsertRow();
                        rsPriceListRate.updateInt("COMPANY_ID", 2);
                        rsPriceListRate.updateString("PRICE_LIST_NO", "PLW09");
                        rsPriceListRate.updateInt("PRICE_LIST_SR_NO", Integer.parseInt(LineCol[2].toString().trim()));
                        rsPriceListRate.updateInt("SR_NO", 1);
                        rsPriceListRate.updateString("EFFECTIVE_DATE", ConvertDateDB(LineCol[8]));
                        rsPriceListRate.updateDouble("EX_MILL_RATE", Double.parseDouble(LineCol[5].toString().trim()));

                        rsPriceListRate.updateString("CREATED_BY", "1");
                        rsPriceListRate.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPriceListRate.updateString("MODIFIED_BY", "");
                        rsPriceListRate.updateString("MODIFIED_DATE", "0000-00-00");
                        rsPriceListRate.updateInt("CHANGED", 1);
                        rsPriceListRate.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                        rsPriceListRate.insertRow();
                    }
                    else if (Rate1 != Rate2) {
                        rsPriceListRate.moveToInsertRow();
                        rsPriceListRate.updateInt("COMPANY_ID", 2);
                        rsPriceListRate.updateString("PRICE_LIST_NO", "PLW09");
                        rsPriceListRate.updateInt("PRICE_LIST_SR_NO", Integer.parseInt(LineCol[2].toString().trim()));
                        rsPriceListRate.updateInt("SR_NO", 1);
                        rsPriceListRate.updateString("EFFECTIVE_DATE", "2009-06-16");//EITLERPGLOBAL.formatDateDB(ConvertDateDB(LineCol[8])));
                        rsPriceListRate.updateDouble("EX_MILL_RATE", Double.parseDouble(LineCol[5].toString().trim()));
                        rsPriceListRate.updateString("CREATED_BY", "1");
                        rsPriceListRate.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPriceListRate.updateString("MODIFIED_BY", "");
                        rsPriceListRate.updateString("MODIFIED_DATE", "0000-00-00");
                        rsPriceListRate.updateInt("CHANGED", 1);
                        rsPriceListRate.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPriceListRate.insertRow();
                        
                        rsPriceListRate.moveToInsertRow();
                        rsPriceListRate.updateInt("COMPANY_ID", 2);
                        rsPriceListRate.updateString("PRICE_LIST_NO", "PLW09");
                        rsPriceListRate.updateInt("PRICE_LIST_SR_NO", Integer.parseInt(LineCol[2].toString().trim()));
                        rsPriceListRate.updateInt("SR_NO", 2);
                        rsPriceListRate.updateString("EFFECTIVE_DATE", "2009-09-01");//EITLERPGLOBAL.formatDateDB(ConvertDateDB(LineCol[8])));
                        rsPriceListRate.updateDouble("EX_MILL_RATE", Double.parseDouble(LineCol[6].toString().trim()));
                        rsPriceListRate.updateString("CREATED_BY", "1");
                        rsPriceListRate.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPriceListRate.updateString("MODIFIED_BY", "");
                        rsPriceListRate.updateString("MODIFIED_DATE", "0000-00-00");
                        rsPriceListRate.updateInt("CHANGED", 1);
                        rsPriceListRate.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                        rsPriceListRate.insertRow();
                    }
                    
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                //quality master
                
                try {
                    rsQualityMst.moveToInsertRow();
                    rsQualityMst.updateInt("COMPANY_ID", 2);
                    rsQualityMst.updateString("QUALITY_ID", LineCol[3]);
                    rsQualityMst.updateString("QUALITY_NAME", LineCol[3]);
                    rsQualityMst.updateString("WH_ID", "01");
                    rsQualityMst.updateString("BRAND_ID", LineCol[0]);
                    rsQualityMst.updateString("SEASON_ID", "W09");
                    rsQualityMst.updateString("MSR_UNIT_ID", "");
                    rsQualityMst.updateString("UNIT_ID", LineCol[4]);
                    rsQualityMst.updateInt("WIDTH", 0);
                    
                    rsQualityMst.updateString("CREATED_BY", "1");
                    rsQualityMst.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsQualityMst.updateString("MODIFIED_BY", "");
                    rsQualityMst.updateString("MODIFIED_DATE", "0000-00-00");
                    rsQualityMst.updateInt("HIERARCHY_ID", 0);
                    rsQualityMst.updateInt("APPROVED", 1);
                    rsQualityMst.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsQualityMst.updateInt("REJECTED", 0);
                    rsQualityMst.updateString("REJECTED_DATE", "0000-00-00");
                    rsQualityMst.updateInt("CHANGED", 1);
                    rsQualityMst.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsQualityMst.updateInt("CANCELLED", 0);
                    
                    rsQualityMst.insertRow();                    
                    
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }                
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public String ConvertDateDB(String Date) {
        if (Date.length()<6) {
            return "0000-00-00";
        }
        else {
            return Date.substring(6,10) + "-" + Date.substring(3,5) + "-" + Date.substring(0,2);
        }
    }
    
}