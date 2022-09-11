
/*
 * Test.java
 *
 * Created on February 5, 2009, 11:05 AM
 */

package EITLERP.Finance;
import EITLERP.*;
import java.sql.*;
import java.net.InetAddress;
import java.util.*;
import java.math.BigDecimal;
import java.text.*;
import java.io.*;

/**
 *
 * @author  Mrugesh-SDML
 */
public class Test {
    //double Amount = 50000.0;
    double Amount = 55000;
    double Rate = 10;
    double interestAmount = 0.0;
    int Months = 6;
    double nyear = Months/12.0;
    double nTimes = 12.0/Months;
    String EffectiveDate = "2010-08-31";
    String MaturityDate = "";
    public InetAddress inetAddress = null;
    /** Creates a new instance of Test */
    public Test() {
        /*try {
            inetAddress = InetAddress.getLocalHost();
            String strAddress = inetAddress.getHostAddress();
            String strName = inetAddress.getHostName();
            System.out.println("Address = " + strAddress);
            System.out.println("Name = " + strName);
        } catch(Exception e) {
        }*/
        
        
        //System.out.println(Double.parseDouble(Double.toString(Amount)));
        /*String str = "123456,654321,112233,332211";
        StringTokenizer Token = new StringTokenizer(str,",");
        while(Token.hasMoreTokens()) {
            String Tok = Token.nextToken(",");
            System.out.println(Tok);
        }*/
        //int tMaonths = 15*12;
        //=================roundEx Test
//        double val = 1000.01;
//        val = EITLERPGLOBAL.roundEx(val);
        //=================roundEx Test
        double pAmount = Amount;
        while(pAmount<=55000) {
            Amount = pAmount;
            MaturityDate = clsCalcInterest.addMonthToDate(EffectiveDate, 36);
            EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
            int Counter=0;
            System.out.println("Principle Amount = " + pAmount +" @ " + Rate +"%");
            System.out.println("Sr.No\tAmount\tInterest");
            while(!java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(MaturityDate))) {
                Counter++;
                interestAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                System.out.println(Counter+"\t"+ EITLERPGLOBAL.round(Amount,0) + "\t" +EITLERPGLOBAL.round(interestAmount,0));
                //System.out.println("Effective_date : " + EITLERPGLOBAL.formatDate(EffectiveDate) + "Counter = "+Counter+" "+ EITLERPGLOBAL.round(Amount,0) + " " +interestAmount+"    "+(EITLERPGLOBAL.round(Amount+interestAmount,0)));
                Amount = EITLERPGLOBAL.round(Amount+interestAmount,0) ;
                EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
            }
            EffectiveDate = "2007-10-29";
            pAmount=55000;
        }
        
        /*NumberFormat n = NumberFormat.getCurrencyInstance(new Locale("en","IN")); 
        double d = 100000000000.25;
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(4);
	String temp = bd.toString();
        System.out.println(temp);
        /*bd = bd.add(new BigDecimal(1000000000.678));
        bd.setScale(2, BigDecimal.ROUND_FLOOR);
        double money = bd.doubleValue();*/
        
        //System.out.println(EITLERPGLOBAL.round(bd.doubleValue(),2));
        //System.out.println(n.format(d));
        //int days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf("2007-02-24"),java.sql.Date.valueOf("2009-02-24")) + 1;
        
        
        /*String str = "003090";
        str = "M"+str;
        System.out.println("str = " + str);
        str = "MA" + str.substring(2);
        System.out.println("str = " + str);*/
    }
    
    public static void main(String [] agrs) {
        new Test();
    }
}
