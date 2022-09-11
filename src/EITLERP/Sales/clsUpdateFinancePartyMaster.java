/*
 * clsUpdateFinancePartyMaster.java
 *
 * Created on November 20, 2009, 3:27 PM
 */

package EITLERP.Sales;
import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.Stores.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  Prathmesh Shah
 */
public class clsUpdateFinancePartyMaster {
    
    /** Creates a new instance of clsUpdateFinancePartyMaster */
    public clsUpdateFinancePartyMaster() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        boolean Done=false;
        String str;
        try{
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            String FileName="/root/Desktop/FF.txt";
            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File(FileName)));
            
            String MainAccountCode="210072";
            
            
            Connection objConn=data.getConn(dbURL);
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String Qry="SELECT * FROM D_SAL_PARTY_MASTER WHERE APPROVED=1 AND PARTY_CODE NOT LIKE 'NEWD%' AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' ORDER BY PARTY_CODE";
            ResultSet rsSales = data.getResult(Qry,dbURL);
            ResultSet rsFin, rsFin1;
            System.out.println("PartyCode" + "~" +"PartyName"+ "~"+ "~" +"Party Address"+ "~" +"PartyCity"+ "~" +"PartyPinCode"+ "~" +"PartyCreditDays" );
            String SalesPartyCode,SalesPartyName, SalesPartyAddress, SalesPartyCity, SalesPartyPinCode,SalesPartyChargeCode1,SalesPartyChargeCode2,FinPartyChargeCode1,FinPartyChargeCode2, SalesPartyState,FinPartyState;
            String FinPartyCode,FinPartyName, FinPartyAddress, FinPartyCity, FinPartyPinCode;
            
            int SalesPartyCreditDays,FinPartyCreditDays;
            rsSales.first();
            while(!rsSales.isAfterLast()) {
                SalesPartyCode=EITLERPGLOBAL.padRight(6,rsSales.getString("PARTY_CODE").trim(),"0");
                SalesPartyName=rsSales.getString("PARTY_NAME");
                SalesPartyName = SalesPartyName.replace("'", "\"");
                SalesPartyAddress=rsSales.getString("ADDRESS1")+","+UtilFunctions.getString(rsSales,"ADDRESS2","");
                SalesPartyAddress = SalesPartyAddress.replace("'", "\"");
                SalesPartyCity=UtilFunctions.getString(rsSales,"CITY_ID","");
                SalesPartyPinCode=UtilFunctions.getString(rsSales, "PINCODE","");
                SalesPartyCreditDays=rsSales.getInt("CREDIT_DAYS");
                SalesPartyChargeCode1=UtilFunctions.getString(rsSales,"CHARGE_CODE","");
                SalesPartyChargeCode2=UtilFunctions.getString(rsSales,"CHARGE_CODE_II","");
                SalesPartyState=UtilFunctions.getString(rsSales,"DISTRICT","");
                System.out.println(SalesPartyCode);
                
                rsFin=stTmp.executeQuery("SELECT * FROM FINANCE.D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE ='"+SalesPartyCode+"'");
                rsFin.first();
                if (rsFin.getRow()>0){
                    //FinPartyCode=rsFin.getString("PARTY_CODE");
                    FinPartyCode=EITLERPGLOBAL.padRight(6,rsFin.getString("PARTY_CODE").trim(),"0");
                    FinPartyName=UtilFunctions.getString(rsFin,"PARTY_NAME","");
                    FinPartyAddress=UtilFunctions.getString(rsFin, "ADDRESS","");
                    FinPartyCity=UtilFunctions.getString(rsFin,"CITY","");
                    FinPartyPinCode=UtilFunctions.getString(rsFin,"PINCODE","");
                    FinPartyCreditDays=rsFin.getInt("CREDIT_DAYS");
                    FinPartyChargeCode1=UtilFunctions.getString(rsFin,"CHARGE_CODE","");
                    FinPartyChargeCode2=UtilFunctions.getString(rsFin,"CHARGE_CODE_II","");
                    FinPartyState=UtilFunctions.getString(rsSales,"STATE","");
                    
                    String qry ="UPDATE D_FIN_PARTY_MASTER SET PARTY_NAME = '"+SalesPartyName+"', " +
                    " ADDRESS = '"+SalesPartyAddress+"', "+
                    " CITY ='"+SalesPartyCity+"', "+
                    " PINCODE ='"+SalesPartyPinCode+"', "+
                    " STATE   ='"+SalesPartyState+"', "+
                    " COUNTRY =''  ,"+
                    " CREDIT_DAYS ="+SalesPartyCreditDays+ "," +
                    " CHARGE_CODE ='"+SalesPartyChargeCode1+"' ,"+
                    " CHARGE_CODE_II ='"+SalesPartyChargeCode2+"' "+
                    "WHERE APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE ='"+SalesPartyCode+"' ";
                    
                   // data.Execute(qry,FinanceGlobal.FinURL);
                    
                }
                else{
                    FinPartyCode="";
                    FinPartyName="";
                    FinPartyAddress="";
                    FinPartyCity="";
                    FinPartyPinCode="";
                    FinPartyCreditDays=0;
                    FinPartyChargeCode1="";
                    FinPartyChargeCode2="";
                    FinPartyState="";
                }
                
                
                rsSales.next();
                System.out.println(SalesPartyCode + "~" +SalesPartyName+ "~"+ "~" +SalesPartyAddress+ "~" +SalesPartyCity+ "~" +SalesPartyPinCode+ "~" +SalesPartyCreditDays+"~" );
                System.out.println(FinPartyCode + "~" +FinPartyName+ "~"+ "~" +FinPartyAddress+ "~" +FinPartyCity+ "~" +FinPartyPinCode+ "~" +FinPartyCreditDays+"~");
                System.out.println("");
                //String str=SalesPartyCode + "~" +SalesPartyName+ "~"+ "~" +SalesPartyAddress+ "~" +SalesPartyCity+ "~" +SalesPartyPinCode+ "~" +SalesPartyCreditDays;
                
                str=SalesPartyCode + "~" +SalesPartyName+ "~"+FinPartyName;
                aFile.write(str);
                aFile.newLine();
                
                str=" " + "~" +SalesPartyAddress+ "~"+FinPartyAddress;
                aFile.write(str);
                aFile.newLine();
                
                str=" " + "~" +SalesPartyCity+ "~"+FinPartyCity;
                aFile.write(str);
                aFile.newLine();
                
                str=" " + "~" +SalesPartyState+ "~"+FinPartyState;
                aFile.write(str);
                aFile.newLine();
                
                if (SalesPartyPinCode.trim().equals("") && FinPartyPinCode.trim().equals("")){
                    str=" " + "~" +SalesPartyPinCode+ "~"+FinPartyPinCode;
                    aFile.write(str);
                    aFile.newLine();
                }
                
                str=" " + "~"+"Credit Days :"+ SalesPartyCreditDays+ "~"+"Credit Days :"+FinPartyCreditDays;
                aFile.write(str);
                aFile.newLine();
                
                str=" "  + "~"+"ChargeCode 1->:"+ SalesPartyChargeCode1+ " ,ChargeCode 2->"+SalesPartyChargeCode2 +"" ;
                str+=" " + "~"+"ChargeCode 1->:"+ FinPartyChargeCode1  + " ,ChargeCode 2->"+FinPartyChargeCode2 +"" ;
                aFile.write(str);
                aFile.newLine();
                
            }
            aFile.close();
            
        }
        catch(Exception c){
            c.printStackTrace();
            Done=true;
        }
        
    }
    
}
