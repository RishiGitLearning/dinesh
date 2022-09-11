/*
 * TGLOBAL.java
 *
 * Created on July 9, 2007, 1:24 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import javax.swing.JComboBox;
import javax.swing.JList;
//import TReportWriter.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
//import java.sql.*;
import java.math.BigDecimal;

public class TGLOBAL {
    
    /** Creates a new instance of TGLOBAL */
    public TGLOBAL() {
    }
     public static int getComboCode(JComboBox pCombo) {
        int rValue=0;
        
        try {
            TEITLComboModel aModel=(TEITLComboModel) pCombo.getModel();
            
            rValue=(int)aModel.getCode((long)pCombo.getSelectedIndex());
            
            return rValue;
        }
        catch(Exception e) {
            return 0;
        }
    }

     public static int getComboCode(JList pCombo) {
        int rValue=0;
        
        try {
            TEITLComboModel aModel=(TEITLComboModel) pCombo.getModel();
            
            rValue=(int)aModel.getCode((long)pCombo.getSelectedIndex());
            
            return rValue;
        }
        catch(Exception e) {
            return 0;
        }
    }
 
    public static void setComboIndex(JComboBox pCombo,int pValue) {
        try {
            TEITLComboModel aModel=(TEITLComboModel) pCombo.getModel();
            
            for(int i=0;i<aModel.getSize();i++) {
                if(aModel.getCode(i)==pValue) {
                    pCombo.setSelectedIndex(i);
                    return;
                }
            }
        }
        catch(Exception e) {
            
        }
        return;
    }
    
    
    public static void setComboIndex(JComboBox pCombo,String pValue) {
        try {
            
            TEITLComboModel aModel=(TEITLComboModel) pCombo.getModel();
            
            for(int i=0;i<aModel.getSize();i++) {
                if(aModel.getstrCode(i).equals(pValue)) {
                    pCombo.setSelectedIndex(i);
                    return;
                }
            }
        }
        catch(Exception e) {
            
        }
        return;
    }     
    
    public static String getCurrentDate() {
        Calendar cal=new GregorianCalendar();
        
        String Day="";
        String Month="";
        String Year="";
        String strDate="";
        
        Day=Integer.toString(cal.get(Calendar.DATE));
        Month=Integer.toString(cal.get(Calendar.MONTH)+1);
        Year=Integer.toString(cal.get(Calendar.YEAR));
        
        Day=Replicate("0",2-Day.length())+Day;
        Month=Replicate("0",2-Month.length())+Month;
        Year=Replicate("0",2-Year.length())+Year;
        
        strDate=Day+"/"+Month+"/"+Year;
        
        
        return strDate;
    }
    
    public static String getCurrentTime() {
        Calendar cal=new GregorianCalendar();
        
        String Hour="";
        String Minute="";
        String Second="";
        String AMPM="";
        String strTime="";
        
        Hour=Integer.toString(cal.get(Calendar.HOUR));
        Minute=Integer.toString(cal.get(Calendar.MINUTE));
        Second=Integer.toString(cal.get(Calendar.SECOND));
        if(cal.get(Calendar.AM_PM)==Calendar.AM) {
            AMPM="AM";
        }
        else {
            AMPM="PM";
        }
        
        strTime=Hour+":"+Minute+":"+Second+" "+AMPM;
        
        return strTime;
        
    }
    
   public static String Replicate(String pString,int No) {
        String tmpString="";
        try {
            for(int i=1;i<=No;i++) {
                tmpString=tmpString+pString;
            }
            return tmpString;
        }
        catch(Exception e) {
            return tmpString;
        }
    }    
   
    public static double round(double val, int places) {
        long factor = (long)Math.pow(10,places);
        
        // Shift the decimal the correct number of places
        // to the right.
        val = val * factor;
        
        // Round to the nearest integer.
        long tmp = Math.round(val);
        
        // Shift the decimal the correct number of places
        // back to the left.
        return (double)tmp / factor;
    }
   
    public static double roundNew(double val, int places) {
        
        BigDecimal aBigDecimal=new BigDecimal(val);
        aBigDecimal=aBigDecimal.setScale(places,BigDecimal.ROUND_UP);
        
        
        return aBigDecimal.doubleValue();
    }
    
  public static int CInt(String intValue) {
        int returnValue=0;
        
        try {
            returnValue=Integer.parseInt(intValue);
            
        }
        catch(Exception e) {
            returnValue=0;
        }
        
        return returnValue;
    }
    
    
    public static double CDbl(String dblValue) {
        double returnValue=0;
        
        try {
            returnValue=Double.parseDouble(dblValue);
            
        }
        catch(Exception e) {
            returnValue=0;
        }
        
        return returnValue;
    }    
    
    public static String formatDate(String pDate) {
        try {
            if(pDate.equals("")) {
                return "";
            }
            else {
                String formatedDate=pDate.substring(8,10)+"/"+pDate.substring(5,7)+"/"+pDate.substring(0,4);
                
                if(isDate(formatedDate)) {
                    return pDate.substring(8,10)+"/"+pDate.substring(5,7)+"/"+pDate.substring(0,4);
                }
                else {
                    return "";
                }
            }
        }
        catch(Exception ex) {
            return "";
        }
    }
    
    public static String formatDateDB(String pDate) {
        
        if (pDate.length()<10) {
            return "";
        }
        else {
            if(isDate(pDate)) {
                return pDate.substring(6,10) + "-" + pDate.substring(3,5) + "-" + pDate.substring(0,2);
            }
            else {
                return "";
            }
        }
    }
 
    public static boolean isDate(String pDate) {
        SimpleDateFormat aFormat=new SimpleDateFormat("dd/MM/yyyy");
        
        if(pDate.trim().equals("")) {
            return true;
        }
        
        if(pDate.trim().length()<10) {
            return false;
        }
        
        try {
            aFormat.setCalendar(new GregorianCalendar());
            aFormat.setLenient(false);
            aFormat.parse(pDate);
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
}
