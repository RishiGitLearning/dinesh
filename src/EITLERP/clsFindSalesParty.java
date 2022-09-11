/*
 * clsFindSalesParty.java
 *
 * Created on February 16, 2011, 1:18 PM
 */

package EITLERP;

import java.sql.*;
import java.util.StringTokenizer;
import EITLERP.Finance.*;
/**
 *
 * @author  user
 */
public class clsFindSalesParty {
    
    /** Creates a new instance of clsFindSalesParty */
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FindParty();
    }
    public static void FindParty() {
        try {
            System.out.println("------start------");
            String dburl="jdbc:mysql://200.0.0.227:3306/DINESHMILLS?user=root";
            
            String Qry= "SELECT *  FROM D_SAL_PARTY_MASTER WHERE APPROVED_DATE >= '2010-04-01'  " +
            "AND APPROVED= 1 AND CANCELLED= 0 AND PARTY_CODE <> '485572' AND MAIN_ACCOUNT_CODE = '210027' ";
            ResultSet rsSales = data.getResult(Qry,dburl);
            
            while(!rsSales.isAfterLast()) {
                String AcountCode=rsSales.getString("ACCOUNT_CODES");
                String PartyCode = rsSales.getString("PARTY_CODE");
                StringTokenizer MainCode = new StringTokenizer(AcountCode,",");
                while(MainCode.hasMoreTokens()) {
                    //System.out.println(MainCode.nextToken());
                    String MainAccountCode = MainCode.nextToken();
                    Qry ="SELECT COUNT(*) FROM FINANCE.D_FIN_PARTY_MASTER " +
                    "WHERE PARTY_CODE=  '" + PartyCode + "' AND MAIN_ACCOUNT_CODE = '" + MainAccountCode + "'";
                    int counter = data.getIntValueFromDB(Qry,dburl);
                    if(counter <=0) {
                        System.out.println("Party Code : " + PartyCode + " Main AccountCode: " + MainAccountCode );
                        AddPartyToFinance(PartyCode,MainAccountCode,"210027");
                    }
                    
                }
                rsSales.next();
            }
            System.out.println("------finished------");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    public static void AddPartyToFinance(String PartyCode,String MainAccCode,String MainCode) {
        //========== Add Party to Finance Party Master ===============//
        String dburlF="jdbc:mysql://200.0.0.227:3306/FINANCE?user=root";
        clsSales_Party ObjParty=new clsSales_Party();
        String sMainCode[] = MainAccCode.trim().split(",");
        ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ MainCode +"' ");
        for(int n=0;n<sMainCode.length;n++) {
            String Main_Code = sMainCode[n].trim();
            try {
                if(data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",dburlF)) {
                    data.Execute("DELETE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",dburlF);
                }
                
                Connection FinConn;
                Statement stFinParty;
                ResultSet rsFinParty;
                
                FinConn=data.getConn(dburlF);
                stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
                
                long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", dburlF);
                
                rsFinParty.moveToInsertRow();
                rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsFinParty.updateString("PARTY_CODE",PartyCode);
                rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
                rsFinParty.updateLong("PARTY_ID",Counter);
                rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
                rsFinParty.updateString("SH_NAME","");
                rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
                rsFinParty.updateString("SH_CODE","");
                rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                rsFinParty.updateDouble("CREDIT_LIMIT",0);
                rsFinParty.updateDouble("DEBIT_LIMIT",0);
                rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
                rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
                rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
                rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
                rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
                rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
                rsFinParty.updateString("SERVICE_TAX_NO","");
                rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
                rsFinParty.updateString("SSI_NO","");
                rsFinParty.updateString("SSI_DATE","0000-00-00");
                rsFinParty.updateString("ESI_NO","");
                rsFinParty.updateString("ESI_DATE","0000-00-00");
                rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
                rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
                rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
                rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
                rsFinParty.updateString("COUNTRY","");
                rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
                rsFinParty.updateString("FAX","");
                rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
                rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
                rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
                rsFinParty.updateInt("APPROVED",1);
                rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateInt("REJECTED",0);
                rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                rsFinParty.updateString("REJECTED_REMARKS","");
                rsFinParty.updateInt("HIERARCHY_ID",0);
                rsFinParty.updateInt("CANCELLED",0);
                rsFinParty.updateInt("BLOCKED",0);
                rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
                rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
                rsFinParty.updateInt("CHANGED",0);
                rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("CREATED_BY","System");
                rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("MODIFIED_BY","System");
                rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateDouble("CLOSING_BALANCE",0);
                rsFinParty.updateString("CLOSING_EFFECT","D");
                rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
                rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
                rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
                rsFinParty.insertRow();
                
                //rsFinParty.close();
                //stFinParty.close();
                //FinConn.close();
                
            } catch(Exception p) {
                p.printStackTrace();
            }
        }
        //            }
        //            else {
        //                String Main_Code = sMainCode[n].trim();
        //                try {
        //
        //                    //data.Execute("DELETE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",FinanceGlobal.FinURL);
        //
        //                    Connection FinConn;
        //                    Statement stFinParty;
        //                    ResultSet rsFinParty;
        //
        //                    FinConn=data.getConn(FinanceGlobal.FinURL);
        //                    stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        //                    rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
        //
        //                    long Counter=data.getLongValueFromDB("SELECT MAPARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
        //                    ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ MainCode +"' ");
        //
        //                    rsFinParty.moveToInsertRow();
        //                    rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        //                    rsFinParty.updateString("PARTY_CODE",PartyCode);
        //                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
        //                    rsFinParty.updateLong("PARTY_ID",Counter);
        //                    rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
        //                    rsFinParty.updateString("SH_NAME","");
        //                    rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
        //                    rsFinParty.updateString("SH_CODE","");
        //                    rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
        //                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
        //                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
        //                    rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
        //                    rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
        //                    rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
        //                    rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
        //                    rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
        //                    rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
        //                    rsFinParty.updateString("SERVICE_TAX_NO","");
        //                    rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
        //                    rsFinParty.updateString("SSI_NO","");
        //                    rsFinParty.updateString("SSI_DATE","0000-00-00");
        //                    rsFinParty.updateString("ESI_NO","");
        //                    rsFinParty.updateString("ESI_DATE","0000-00-00");
        //                    rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
        //                    rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
        //                    rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
        //                    rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
        //                    rsFinParty.updateString("COUNTRY","");
        //                    rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
        //                    rsFinParty.updateString("FAX","");
        //                    rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
        //                    rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
        //                    rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
        //                    rsFinParty.updateInt("APPROVED",1);
        //                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateInt("REJECTED",0);
        //                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
        //                    rsFinParty.updateString("REJECTED_REMARKS","");
        //                    rsFinParty.updateInt("HIERARCHY_ID",0);
        //                    rsFinParty.updateInt("CANCELLED",0);
        //                    rsFinParty.updateInt("BLOCKED",0);
        //                    rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
        //                    rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
        //                    rsFinParty.updateInt("CHANGED",0);
        //                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateString("CREATED_BY","System");
        //                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateString("MODIFIED_BY","System");
        //                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
        //                    rsFinParty.updateString("CLOSING_EFFECT","D");
        //                    rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
        //                    rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
        //                    rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
        //                    rsFinParty.insertRow();
        //
        //                    //rsFinParty.close();
        //                    //stFinParty.close();
        //                    //FinConn.close();
        //                }
        //                catch(Exception p) {
        //                    p.printStackTrace();
        //                }
        //            }
        
        
       /* if(ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ sMainCode[0].trim() +"' ")) {
            for(int n=0;n<sMainCode.length;n++) {
                String Main_Code = sMainCode[n].trim();
                try {
                    Connection FinConn;
                    Statement stFinParty;
                    ResultSet rsFinParty;
        
                    FinConn=data.getConn(FinanceGlobal.FinURL);
                    stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
        
                    long Counter=data.getLongValueFromDB("SELECT MAPARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
        
                    rsFinParty.moveToInsertRow();
                    rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsFinParty.updateString("PARTY_CODE",ObjParty.getAttribute("PARTY_CODE").getString());
                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
                    rsFinParty.updateLong("PARTY_ID",Counter);
                    rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
                    rsFinParty.updateString("SH_NAME","");
                    rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
                    rsFinParty.updateString("SH_CODE","");
                    rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
                    rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
                    rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
                    rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
                    rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
                    rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
                    rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
                    rsFinParty.updateString("SERVICE_TAX_NO","");
                    rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
                    rsFinParty.updateString("SSI_NO","");
                    rsFinParty.updateString("SSI_DATE","0000-00-00");
                    rsFinParty.updateString("ESI_NO","");
                    rsFinParty.updateString("ESI_DATE","0000-00-00");
                    rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
                    rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
                    rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
                    rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
                    rsFinParty.updateString("COUNTRY","");
                    rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
                    rsFinParty.updateString("FAX","");
                    rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
                    rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
                    rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
                    rsFinParty.updateInt("APPROVED",1);
                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateInt("REJECTED",0);
                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                    rsFinParty.updateString("REJECTED_REMARKS","");
                    rsFinParty.updateInt("HIERARCHY_ID",0);
                    rsFinParty.updateInt("CANCELLED",0);
                    rsFinParty.updateInt("BLOCKED",0);
                    rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
                    rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
                    rsFinParty.updateInt("CHANGED",0);
                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("CREATED_BY","System");
                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("MODIFIED_BY","System");
                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
                    rsFinParty.updateString("CLOSING_EFFECT","D");
                    rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
                    rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
                    rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
                    rsFinParty.insertRow();
        
                    rsFinParty.close();
                    //stFinParty.close();
                    //FinConn.close();
                }
                catch(Exception p) {
        
                }
        
            }*/
        //============================================================//
        
    }
    
    
    
}
