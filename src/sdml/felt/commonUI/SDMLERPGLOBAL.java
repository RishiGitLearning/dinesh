/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.applet.*;
import java.net.*;
import java.util.Date;
import java.math.BigDecimal;

public class SDMLERPGLOBAL {
    
    public static final int ADD=1;
    public static final int EDIT=2;
    public static final int AMEND=3;
    
    public static final int OnRecivedDate=1;
    public static final int OnDocDate=2;
    public static final int OnDocNo=3;
    
    public static boolean PAGE_BREAK = false;
    public static boolean IsConnected=false;
    
    //Session related variables
    public static int gCompanyID=2;
    public static int gUserID=1;
    public static int gAuthorityUserID=1;
    public static int gNewUserID=1;
    public static int gDeptID=0;
     
    public static String gPassword="";
    public static String gLoginID="";
    public static String AppPath="";
    public static String DBName="DINESHMILLS";
    
    //public static String DBUserName="?user=SDMLerpsync;password=SDMLerpSync";
    public static String DBUserName="?user=root&zeroDateTimeBehavior=convertToNull";
    public static String DBPassword="SdmlErp@227";
    //public static String DBUserName="";
    public static String MsgServerIP="200.0.0.227";
    
    public static HashMap Clients=new HashMap(); //Stores all information of connected clients
    public static int Port=5000;
    public static ServerSocket theServer;
    
    public static boolean UseCurrentFormula=true;
    
    //Financial Year specific global variables//
    public static int FinYearFrom=2016;
    public static int FinYearTo=2017;
    public static String FinFromDate="01/04/2016"; //Financial Year From Date. in DD/MM/YYYY
    public static String FinToDate="31/03/2017"; //Financial Year To Date. in DD/MM/YYYY
    //public static String FinFromDateDB=""; //Financial Year From Date. in YYYY-MM-DD
    //public static String FinToDateDB=""; //Financial To From Date. in YYYY-MM-DD
    public static String FinFromDateDB="2016-04-01"; //Financial Year From Date. in YYYY-MM-DD
    public static String FinToDateDB="2017-03-31"; //Financial To From Date. in YYYY-MM-DD
         
    
    public static String SMTPHostIP="184.106.240.198";
    
    public static boolean YearIsOpen=true; //Flag indicating year is open/closed.
    public static boolean UsePrevYearData=false; //Flag indicating whether to use previous year data in references
    public static String SystemDBURL=""; //URL for system database. will be updated
    
    //public static String DatabaseURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS"+SDMLERPGLOBAL.DBUserName; //Database URL for current financial year

    public static String DatabaseURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS"+SDMLERPGLOBAL.DBUserName; //Database URL for current financial year

    public static String DatabaseURL_Production="jdbc:mysql://200.0.0.227:3306/PRODUCTION"+SDMLERPGLOBAL.DBUserName;
//    
    
    public static String SystemLogURL="jdbc:mysql://200.0.0.227:3306/SYSTEMLOG"+SDMLERPGLOBAL.DBUserName; //Database URL for current financial year
    //public static String DatabaseURL=""; //Database URL for current financial year
    public static String PrevYearURL=""; //Database URL for previous year's database
    public static String CodeBasePath="";
    
    public static String HostIP="200.0.0.227:8080/jmx-console/dinesh";
    
    public static String ServerIP="200.0.0.227";
    
    public static AppletContext loginContext;
    
    public static String RecordLimit=" LIMIT 1000 ";
    
    public static Connection gConn;
    
    public static int gUserDeptID=0;
    
    public static boolean DoNotShowWarning=false;
    
    public static MessageClient ObjClient ;
    public static boolean LoggedIn=true;
    //=======================================//
    
    
    //Collection storing images
    public static HashMap tlbImages=new HashMap();
    
    public static ImageIcon getImage(String pName) {
       
        return (ImageIcon) tlbImages.get(pName);
    }
    
    public static void LoadImages(URL pPath) {
        ImageIcon img;
        //Loading Toolbar Images
        try {
            System.out.println("P Path = "+pPath+"Preview.jpg");
            
            tlbImages.put("PREVIEW",new ImageIcon(new java.net.URL(pPath+"Preview.jpg")));
            tlbImages.put("PRINT",new ImageIcon(new java.net.URL(pPath+"Print.jpg")));
            tlbImages.put("FIND",new ImageIcon(new java.net.URL(pPath+"Find.jpg")));
            tlbImages.put("UNDO",new ImageIcon(new java.net.URL(pPath+"Undo.jpg")));
            tlbImages.put("SAVE",new ImageIcon(new java.net.URL(pPath+"Save.jpg")));
            tlbImages.put("DELETE",new ImageIcon(new java.net.URL(pPath+"Delete.jpg")));
            tlbImages.put("EDIT",new ImageIcon(new java.net.URL(pPath+"Edit.jpg")));
            tlbImages.put("NEW",new ImageIcon(new java.net.URL(pPath+"New.jpg")));
            tlbImages.put("LAST",new ImageIcon(new java.net.URL(pPath+"Last.jpg")));
            tlbImages.put("NEXT",new ImageIcon(new java.net.URL(pPath+"Next.jpg")));
            tlbImages.put("BACK",new ImageIcon(new java.net.URL(pPath+"Back.jpg")));
            tlbImages.put("TOP",new ImageIcon(new java.net.URL(pPath+"Top.jpg")));
            tlbImages.put("MAINLOGO",new ImageIcon(new java.net.URL(pPath+"MainLogo.JPG")));
            tlbImages.put("EXIT",new ImageIcon(new java.net.URL(pPath+"exit.jpg")));
            tlbImages.put("EMAIL",new ImageIcon(new java.net.URL(pPath+"email.jpg")));
          //  tlbImages.put("TITLE",new ImageIcon(new java.net.URL(pPath+"Title.jpg")));
        //    tlbImages.put("RIGHTPANE",new ImageIcon(new java.net.URL(pPath+"RightPane.jpg")));
            tlbImages.put("BULLET",new ImageIcon(new java.net.URL(pPath+"bullet.gif")));
            tlbImages.put("FELTIMAGE",new ImageIcon(new java.net.URL(pPath+"FeltImage.jpg")));
          //  tlbImages.put("SAVETREE",new ImageIcon(new java.net.URL(pPath+"savetree.jpg")));
          //  tlbImages.put("DIWALI",new ImageIcon(new java.net.URL(pPath+"Diwali.jpg")));
        }
        catch(Exception e)
        {
            System.out.println("Error to load image : "+e.getMessage());
        
        }
    }
    
    //Usage Sample
    //txtECCDate.setText(SDMLERPGLOBAL.formatDate((Date) ObjCompany.getAttribute("ECC_DATE").getObj()));
    public static String formatDate(java.sql.Date pDate) {
        try {
            return pDate.toString().substring(8,10)+"/"+pDate.toString().substring(5,7)+"/"+pDate.toString().substring(0,4);
        }
        catch(Exception ex) {
            return "";
        }
    }
    public static int compareDate(String date1,String date2) {
        try{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date firstDate = df.parse(date1);
            Date secondDate = df.parse(date2);
            if(firstDate.before(secondDate)) {
                return -1;
            }else if(firstDate.after(secondDate)) {
                return 1;
            }
            else
                return 0;
        }
        catch(Exception e) {
            return 99;
        }
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
    
    
    public static String formatDateEx(String pDate) {
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
                    if(pDate.equals("0000-00-00")) {
                        return "00/00/0000";
                    }
                    else {
                        return "";
                    }
                }
            }
        }
        catch(Exception ex) {
            return "";
        }
    }
    
    
    //ObjCompany.setAttribute("ECC_DATE",SDMLERPGLOBAL.formatDateDB(txtECCDate.getText()));
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
    
    
    public static String formatDateDB2(String pDate) {
        
        if (pDate.length()<10) {
            return "";
        }
        else {
            //if(isDate(pDate)) {
            return pDate.substring(6,10) + "-" + pDate.substring(0,2)+ "-" + pDate.substring(3,5);
            //}
            //else {
            //    return "";
            //}
        }
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
    
    
    public static int getDayOfWeek(int Day,int Month,int Year) {
        Calendar cal=new GregorianCalendar(Year,Month-1,Day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    
    public static HashMap getSundays(int Month,int Year) {
        HashMap Sundays=new HashMap();
        
        Calendar cal=new GregorianCalendar(Year,Month-1,1);
        
        // Get the number of days in that month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        for(int i=1;i<=days;i++) {
            Calendar tmpCal=new GregorianCalendar(Year,Month-1,i);
            
            if(tmpCal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
                Sundays.put(Integer.toString(Sundays.size()+1), Integer.toString(i));
                System.out.println("Sunday is on "+i);
            }
        }
        
        return Sundays;
    }
    
    
    public static int getCurrentMonth() {
        /*Calendar cal=new GregorianCalendar();
        int Month=cal.get(Calendar.MONTH)+1;
        return Month;*/
        int Month=data.getIntValueFromDB("SELECT MONTH(CURDATE()) FROM DUAL");
        return Month;
    }
    
    
    public static int getCurrentYear() {
        /*Calendar cal=new GregorianCalendar();
        int Year=cal.get(Calendar.YEAR);
        return Year;*/
        int Year=data.getIntValueFromDB("SELECT YEAR(CURDATE()) FROM DUAL");
        return Year;
    }
    
    
    public static int getCurrentDay() {
        /*Calendar cal=new GregorianCalendar();
        int DAY=cal.get(Calendar.DATE);
        return DAY;*/
        int DAY=data.getIntValueFromDB("SELECT DAY(CURDATE()) FROM DUAL");
        return DAY;
    }
    
    public static int getMonthCount() {
        Calendar cal=new GregorianCalendar();
        
        int Month=cal.get(Calendar.MONTH)+1;
        int Count=1;
        
        if(Month==4) {
            Count=1;
        }
        
        if(Month>4&&Month<=12) {
            Count=Month-3;
        }
        
        if(Month<4) {
            Count=9+Month;
        }
        
        return Count;
    }
    
    
    //Returns Date in DD/MM/YYYY format
    public static String getCurrentDate() {
        /*Calendar cal=new GregorianCalendar();
         
        String Day="";
        String Month="";
        String Year="";*/
        String strDate=data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d/%m/%Y') AS DATE FROM DUAL");
        
        /*Day=Integer.toString(cal.get(Calendar.DATE));
        Month=Integer.toString(cal.get(Calendar.MONTH)+1);
        Year=Integer.toString(cal.get(Calendar.YEAR));
         
        Day=Replicate("0",2-Day.length())+Day;
        Month=Replicate("0",2-Month.length())+Month;
        Year=Replicate("0",2-Year.length())+Year;
         
        strDate=Day+"/"+Month+"/"+Year;*/
        
        
        
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
    
    //Returns Date in DD/MM/YYYY format
    public static String getCurrentDateDB() {
        /*Calendar cal=new GregorianCalendar();
         
        String Day="";
        String Month="";
        String Year="";*/
        String strDate=data.getStringValueFromDB("SELECT CURDATE() AS DATE FROM DUAL");
        
        /*Day=Integer.toString(cal.get(Calendar.DATE));
        Month=Integer.toString(cal.get(Calendar.MONTH)+1);
        Year=Integer.toString(cal.get(Calendar.YEAR));
         
         
        Day=Replicate("0",2-Day.length())+Day;
        Month=Replicate("0",2-Month.length())+Month;
        Year=Replicate("0",2-Year.length())+Year;
         
        strDate=Day+"/"+Month+"/"+Year;
         
        strDate=formatDateDB(strDate);*/
        
        return strDate;
    }
    
    
    public static double ConvertToDouble(String pValue) {
        try {
            return Double.parseDouble(pValue);
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static int getComboCode(JComboBox pCombo) {
        int rValue=0;
        
        try {
            SDMLComboModel aModel=(SDMLComboModel) pCombo.getModel();
            
            rValue=(int)aModel.getCode((long)pCombo.getSelectedIndex());
            
            return rValue;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String getCombostrCode(JComboBox pCombo) {
        String rValue="";
        
        try {
            SDMLComboModel aModel=(SDMLComboModel) pCombo.getModel();
            
            rValue=aModel.getstrCode(pCombo.getSelectedIndex());
            
            return rValue;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public static void setComboIndex(JComboBox pCombo,int pValue) {
        try {
            SDMLComboModel aModel=(SDMLComboModel) pCombo.getModel();
            
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
            
            SDMLComboModel aModel=(SDMLComboModel) pCombo.getModel();
            
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
    
    
    public static boolean IsNumber(String pNumber) {
        try {
            double aNumber=Double.parseDouble(pNumber);
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    
    public static String Padding(String pString, int No,String pPadd) {
        String tmpString="";
        try{
            for(int i=pString.length();i<No;i++) {
                tmpString = tmpString+pPadd;
            }
            tmpString=tmpString + pString;
            return tmpString;
        }
        catch(Exception e) {
            return tmpString;
        }
    }
    
    public static boolean isDate(String pDate) {
        SimpleDateFormat aFormat=new SimpleDateFormat("dd/MM/yyyy");
        
        if(pDate.trim().equals("")) {
            return true;
        }
        
        if(pDate.trim().length()<10 || pDate.trim().length()>10) {
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
    
    public static double roundEx(double val) {
        double newVal = 0;
        String Rupee = Double.toString(val);
        int index = Rupee.indexOf(".");
        newVal = Double.parseDouble(Rupee.substring(0,index));
        double paise = Double.parseDouble(Rupee.substring(index));
        if(paise >= 0.01) {
            newVal = newVal+1;
        } else {
            newVal = newVal;
        }
        return newVal;
    }
    
    /*public static double BigRound(BigDecimal val, int places) {
        //long factor = (long)Math.pow(10,places);
     
     
        // Shift the decimal the correct number of places
        // to the right.
        //val = val * factor;
        val = val.multiply(
     
        // Round to the nearest integer.
        long tmp = Math.round(val);
     
        // Shift the decimal the correct number of places
        // back to the left.
        return (double)tmp / factor;
    }*/
    
    public static double doublevalue(double val, int places) {
        double result=0;
        String str = Double.toString(val);
        String first="",second="";
        first = str.substring(0,str.indexOf("."));
        second = str.substring(str.indexOf(".")+1);
        if (second.length() >= places) {
            second = second.substring(0,places);
        }
        else {
            second = second;
        }
        result = Double.parseDouble(first+"."+second);
        
        return result;
    }
    
    
    public static long formatLNumber(String pNumber) {
        try {
            if (pNumber.trim().equals("")) {
                return 0;
            }
            return Long.parseLong(pNumber);
        }
        catch(Exception e) {
            return 0;
        }
    }
    public static double formatDNumber(String pNumber) {
        try {
            if (pNumber.trim().equals("")) {
                return 0.00;
            }
            return Double.parseDouble(pNumber);
        }
        catch(Exception e) {
            return 0.00;
        }
    }
    
    
    public static int DateDiff(java.sql.Date StartDate,java.sql.Date EndDate) {
        try {
            int difInDays = (int) ((EndDate.getTime() - StartDate.getTime())/(1000*60*60*24));
            return difInDays;
            
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static String AddDaysToDate(java.sql.Date theDate,int Days) {
        try {
            GregorianCalendar theCal=new GregorianCalendar(theDate.getYear(),theDate.getMonth(),theDate.getDate());
            theCal.add(Calendar.DAY_OF_MONTH, Days);
            String addDate= theCal.get(Calendar.YEAR)+"-"+theCal.get(Calendar.MONTH)+"-"+theCal.get(Calendar.DAY_OF_MONTH);
            
            return addDate;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String DeductDaysFromDate(java.sql.Date theDate,int Days) {
        try {
            
            GregorianCalendar theCal=new GregorianCalendar(Integer.parseInt(theDate.toString().substring(0,4)),Integer.parseInt(theDate.toString().substring(5,7)),Integer.parseInt(theDate.toString().substring(8)));
            theCal.add(Calendar.DAY_OF_MONTH, -1*Days);
            String addDate= theCal.get(Calendar.YEAR)+"-"+theCal.get(Calendar.MONTH)+"-"+theCal.get(Calendar.DAY_OF_MONTH);
            
            return addDate;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static void ChangeCursorToWait(JApplet pApp) {
        pApp.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }
    
    public static void ChangeCursorToDefault(JApplet pApp) {
        pApp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public static int GetQueryResult(String pQuery) {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery(pQuery);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                int Result=rsTmp.getInt(1);
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return Result;
            }
            else {
                
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return 0;
            }
            
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            
            return 0;
        }
    }
    
    
    public static String makeString(int pLen,String pChar) {
        String newString="";
        try {
            for(int i=1;i<=pLen;i++) {
                newString=newString+pChar;
            }
        }
        catch(Exception e) {
            
        }
        
        return newString;
    }
    
    
    public static String padRight(int pMaxLen,String pString,String pPadChar) {
        return pString+SDMLERPGLOBAL.makeString(pMaxLen-pString.trim().length(),pPadChar);
    }
    
    
    public static String padLeft(int pMaxLen,String pString,String pPadChar) {
        return SDMLERPGLOBAL.makeString(pMaxLen-pString.trim().length(),pPadChar)+pString;
    }
    
    
    public static String addDaysToDate(String sdate1, int Days, String fmt) {
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        
        java.util.Date date1  = null;
        
        
        try {
            date1 = df.parse(sdate1);
        }
        catch (Exception e) {
        }
        
        Calendar cal1 = null;
        
        cal1=Calendar.getInstance();
        
        // different date might have different offset
        cal1.setTime(date1);
        
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);
        
        
        cal1.add(5,Days);
        
        
        
        
        String Year=Integer.toString(cal1.get(Calendar.YEAR));
        Year=SDMLERPGLOBAL.Replicate("0",4-Year.length())+Year;
        
        String Month=Integer.toString((cal1.getTime().getMonth()+1));
        Month=SDMLERPGLOBAL.Replicate("0", 2-Month.length())+Month;
        
        String Day=Integer.toString(cal1.get(Calendar.DAY_OF_MONTH));
        Day=SDMLERPGLOBAL.Replicate("0",2-Day.length())+Day;
        
        String NewDate=Year+"-"+Month+"-"+Day;
        
        return NewDate;
        
    }
    
    
    public static int getDayDifference(String sdate1, String sdate2,String fmt) {
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        
        java.util.Date date1  = null;
        java.util.Date date2  = null;
        
        try {
            date1 = df.parse(sdate1);
            date2 = df.parse(sdate2);
        }
        catch (Exception e) {
        }
        
        Calendar cal1 = null;
        Calendar cal2 = null;
        
        cal1=Calendar.getInstance();
        cal2=Calendar.getInstance();
        
        
        // different date might have different offset
        cal1.setTime(date1);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);
        
        cal2.setTime(date2);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);
        
        // Use integer calculation, truncate the decimals
        int hr1   = (int)(ldate1/3600000); //60*60*1000
        int hr2   = (int)(ldate2/3600000);
        
        int days1 = (int)hr1/24;
        int days2 = (int)hr2/24;
        
        
        int dateDiff  = days2 - days1;
        int weekOffset = (cal2.get(Calendar.DAY_OF_WEEK) - cal1.get(Calendar.DAY_OF_WEEK))<0 ? 1 : 0;
        int weekDiff  = dateDiff/7 + weekOffset;
        int yearDiff  = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
        
        return dateDiff;
        
    }
    
    public static int getCurrentFinYear() {
        int curYear=0;
        int curMonth=0;
        try {
            
            
            curYear=getCurrentYear();
            curMonth=getCurrentMonth();
            
            if(curMonth>=4) //April
            {
                
            }
            else {
                curYear--;
            }
            
        }
        catch(Exception e) {
            
        }
        
        return curYear;
    }
    
    
    public static HashMap Split(String pStr,String pSeperator) {
        HashMap list=new HashMap();
        String strValue="";
        
        try {
            
            for(int i=0;i<=pStr.length();i++) {
                
                if(i<pStr.length()) {
                    if(pStr.substring(i,i+1).equals(pSeperator)) {
                        list.put(Integer.toString(list.size()+1),strValue);
                        strValue="";
                    }
                    else {
                        strValue=strValue+pStr.substring(i,i+1);
                    }
                }
            }
            
            list.put(Integer.toString(list.size()+1),strValue);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return list;
        
    }
    
    
    
    public static String SplitFirst(String pStr,String pSeperator) {
        HashMap list=new HashMap();
        String strValue="";
        
        try {
            
            for(int i=0;i<=pStr.length();i++) {
                if(pStr.substring(i,i+1).equals(pSeperator)) {
                    return strValue;
                }
                else {
                    strValue=strValue+pStr.substring(i,i+1);
                }
            }
            
            
            
        }
        catch(Exception e) {
            
        }
        
        return strValue;
        
    }
    
    public static java.sql.Date ConvertDate(String  pDate) {
        try {
            if(pDate==null) {
                return null;
            }
            
            if(pDate.trim().equals("")||pDate.trim().equals("0000-00-00")) {
                return null;
            }
            else {
                return java.sql.Date.valueOf(pDate);
            }
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public static String formatNumber(double pNumber) {
        try {
            DecimalFormat dFormat=new DecimalFormat("###0.00");
            return dFormat.format(pNumber);
        }
        catch(Exception e) {
            return "0.00";
        }
    }
    
    
    public static String addMonthToDate(String Date) {
        
        String ToDate="";
        
        int Month=SDMLERPGLOBAL.getMonth(Date);
        int Year=SDMLERPGLOBAL.getYear(Date);
        ToDate = SDMLERPGLOBAL.addDaysToDate(Date, SDMLERPGLOBAL.getDaysInMonth(Month,Year)-1,"yyyy-MM-dd");
        
        return ToDate;
    }
    
    
    public static int getMonth(String Date) {
        
        
        int Month =Integer.parseInt(Date.substring(5,7));
        
        return Month;
    }
    
    public static int getYear(String Date) {
        
        int Year = Integer.parseInt(Date.substring(0,4));
        
        return Year;
    }
    
    public static int getDaysInMonth(int Month,int Year) {
        
        Calendar cal=new GregorianCalendar(Year,Month-1,1);
        
        // Get the number of days in that month
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        return days;
    }
    
    
    public static String padRightEx(String pString,String padChar,int Length) {
        try {
            if(pString.length()>=Length) {
                return pString.substring(0,Length);  //Trim it
            }
            
            if(pString.length()<Length) {
                return pString+SDMLERPGLOBAL.Replicate(padChar, Length-pString.length());
            }
            
            return "";
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String padLeftEx(String pString,String padChar,int Length) {
        try {
            if(pString.length()>=Length) {
                return pString.substring(0,Length);  //Trim it
            }
            
            if(pString.length()<Length) {
                return SDMLERPGLOBAL.Replicate(padChar, Length-pString.length())+pString;
            }
            
            return "";
            
        }
        catch(Exception e) {
            return "";
        }
        
        
    }
    
    
    public static boolean isWithinDate(String strDocDate,int FinYearFrom) {
        try {
            String strFinFromDate=Integer.toString(FinYearFrom)+"-04-01";
            String strFinToDate=Integer.toString(FinYearFrom+1)+"-03-31";
            
            java.sql.Date FinFromDate=java.sql.Date.valueOf(strFinFromDate);
            java.sql.Date FinToDate=java.sql.Date.valueOf(strFinToDate);
            java.sql.Date DocDate=java.sql.Date.valueOf(strDocDate);
            
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(Exception e) {
            return true;
        }
    }
    
    public static String FinancialYear(String strDocDate) {
        try {
            String FinYear = "";
            java.sql.Date DocDate=java.sql.Date.valueOf(strDocDate);
            java.sql.Date FromDate = java.sql.Date.valueOf(strDocDate.substring(0,4)+"-04-01");
            java.sql.Date ToDate = java.sql.Date.valueOf(Integer.toString(Integer.parseInt(strDocDate.substring(0,4)))+"-03-31");
            if (DocDate.after(FromDate)||DocDate.compareTo(FromDate)==0) {
                String InvYear = strDocDate.substring(2,4);
                int l_InvYear = Integer.parseInt(strDocDate.substring(2,4)) + 1;
                String last_InvYear="";
                if (Integer.toString(l_InvYear).length()==1) {
                    last_InvYear = "0" + l_InvYear;
                }
                else {
                    last_InvYear = Integer.toString(l_InvYear);
                }
                FinYear=InvYear+last_InvYear;
            }
            if (DocDate.before(ToDate)||DocDate.compareTo(ToDate)==0) {
                String InvYear = strDocDate.substring(2,4);
                int l_InvYear = Integer.parseInt(strDocDate.substring(2,4)) - 1;
                String last_InvYear="";
                if (Integer.toString(l_InvYear).length()==1) {
                    last_InvYear = "0" + l_InvYear;
                }
                else {
                    last_InvYear = Integer.toString(l_InvYear);
                }
                FinYear=last_InvYear+InvYear;
            }
            return FinYear;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    /**     Returns financial year start date according to Date provided in parameter. <BR>
     *      Required Format is yyyy-MM-dd to pass as parameter. <BR>
     *      Return Format is yyyy-MM-dd.
     **/
    public static String getFinYearStartDate(String pDate) {
        try {
            int Month = getMonth(pDate);
            int Year = getYear(pDate);
            String StartDate = "";
            if(Month<4) {
                Year--;
                StartDate = Year + "-04-01";
            } else {
                StartDate = Year + "-04-01";
            }
            return StartDate;
        }catch(Exception e) {
            return "";
        }
    }
    
    /**     Returns financial year end date according to Date provided in parameter. <BR>
     *      Required Format is yyyy-MM-dd to pass as parameter. <BR>
     *      Return Format is yyyy-MM-dd.
     **/
    public static String getFinYearEndDate(String pDate) {
        try {
            int Month = getMonth(pDate);
            int Year = getYear(pDate);
            String EndDate = "";
            if(Month<4) {
                EndDate = Year + "-03-31";
            } else {
                Year++;
                EndDate = Year + "-03-31";
            }
            return EndDate;
        }catch(Exception e) {
            return "";
        }
    }
    public static String getMonthName(String sDate) {
        String Month = "";
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(sDate.substring(0,4)),Integer.parseInt(sDate.substring(5,7))-1,Integer.parseInt(sDate.substring(8,10)));
            java.util.Date dt = new java.util.Date(cal.getTimeInMillis());
            Month = new SimpleDateFormat("MMM").format(dt).toUpperCase();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Month;
    }
    
    public static int getDayOfMonth(String strDate) {
        //        Calendar cal = Calendar.getInstance();
        //        int DayOfMonth = cal.get(Calendar.DATE);
        int DayOfMonth = Integer.parseInt(strDate.substring(8,10));
        return (DayOfMonth);
    }
    
    public static boolean isDateFromOpenYear(String strDate) {
        int Year = 0;
        strDate = SDMLERPGLOBAL.formatDateDB(strDate);
        Year = Integer.parseInt(SDMLERPGLOBAL.getFinYearStartDate(strDate).substring(0,4));
         if(data.IsRecordExist("SELECT * FROM D_COM_FIN_YEAR WHERE YEAR_FROM="+Year+" AND OPEN_STATUS='O' AND COMPANY_ID="+SDMLERPGLOBAL.gCompanyID)) {
             return true;
         } else {
             return false;
         }
    }
    
    public static int getMonthDifference(String sdate1, String sdate2) {
        
        return data.getIntValueFromDB("SELECT ABS(PERIOD_DIFF(DATE_FORMAT('" + sdate1 + "','%Y%m'),DATE_FORMAT('" + sdate2 + "','%Y%m'))) FROM DUAL");
        
    }

}

