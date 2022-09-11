/*
 * clsReportsConfig.java
 *
 * Created on December 22, 2007, 12:09 PM
 */

package EITLERP.Finance.Config;

/**
 *
 * @author  root
 */
import java.util.*;
import java.net.*;
import EITLERP.*;
import org.xmlpull.v1.*;
import java.io.*;
import java.sql.*;
import java.io.IOException;
import java.io.StringReader;

public class clsReportsConfig {
    
    /** Creates a new instance of clsReportsConfig */
    public clsReportsConfig() {
    }
    
    public static HashMap getReportNamesForTR(String TRCode) {
        HashMap TRReports=new HashMap();
        
        try {
            
            //Load the Report Configuration from the webserver
            String ConfigURL="http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Finance/Config/TRReports.conf";
            
            System.out.println("Downloading From "+ConfigURL);
            
            //========== Download the File ===============//
            OutputStream out = null;
            URLConnection conn = null;
            InputStream  in = null;
            
            URL url = new URL(ConfigURL);
            out = new BufferedOutputStream(
            new FileOutputStream("tmpoutput.tmp"));
            
            
            conn = url.openConnection();
            
            int length=conn.getContentLength();
            
            in = conn.getInputStream();
            byte[] buffer = new byte[length];
            int numRead;
            long numWritten = 0;
            
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            
            out.flush();
            out.close();
            
            //===========================================//
            
            BufferedReader File;
            File=new BufferedReader(new FileReader(new File("tmpoutput.tmp")));
            
            //XML Pull Parser Factory Object
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            
            //Create a new instance of Parser
            XmlPullParser xpp = factory.newPullParser();
            
            //Give FileName
            xpp.setInput(File);
            
            //xpp.setInput(new StringReader("Nothing"));
            int eventType = xpp.getEventType();
            
            
            //Add Built In Variables
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType==XmlPullParser.END_TAG) {
                    System.out.println("End Tag");
                }
                
                
                if(eventType==XmlPullParser.START_TAG) {
                    if(xpp.getName().trim().equals("TR")) {
                        String wTRCode=xpp.getAttributeValue(0);//Read Attribute 1 Value i.e. code
                        String wReportFileName=xpp.getAttributeValue(1);//Read Attribute 1 Value i.e. FileName
                        String wReportName=xpp.getAttributeValue(2);//Read Attribute 1 Value i.e. FileName
                        int ProcessType=Integer.parseInt(xpp.getAttributeValue(3));
                        int IsDefault=Integer.parseInt(xpp.getAttributeValue(4));
                        
                        if(wTRCode.equals(TRCode)) {
                            clsConfig objConfig=new clsConfig();
                            objConfig.TRCode=TRCode;
                            objConfig.ReportFileName=wReportFileName;
                            objConfig.ReportName=wReportName;
                            objConfig.ProcessType=ProcessType;
                            objConfig.IsDefault=IsDefault;
                            
                            TRReports.put(Integer.toString(TRReports.size()+1), objConfig);
                        }
                        
                    }
                    
                }
                
                try {
                    eventType = xpp.next();
                }
                catch(Exception c) {
                    
                }
            }
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return TRReports;
    }
    
    
    public static clsConfig getDefaultReportNameForTR(String TRCode) {
        clsConfig objConfig=new clsConfig();
        
        try {
            
            //Load the Report Configuration from the webserver
            String ConfigURL="http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Finance/Config/TRReports.conf";
            
            System.out.println("Downloading From "+ConfigURL);
            
            //========== Download the File ===============//
            OutputStream out = null;
            URLConnection conn = null;
            InputStream  in = null;
            
            URL url = new URL(ConfigURL);
            out = new BufferedOutputStream(
            new FileOutputStream("tmpoutput.tmp"));
            
            
            conn = url.openConnection();
            
            int length=conn.getContentLength();
            
            in = conn.getInputStream();
            byte[] buffer = new byte[length];
            int numRead;
            long numWritten = 0;
            
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            
            out.flush();
            out.close();
            
            //===========================================//
            
            BufferedReader File;
            File=new BufferedReader(new FileReader(new File("tmpoutput.tmp")));
            
            //XML Pull Parser Factory Object
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            
            //Create a new instance of Parser
            XmlPullParser xpp = factory.newPullParser();
            
            //Give FileName
            xpp.setInput(File);
            
            //xpp.setInput(new StringReader("Nothing"));
            int eventType = xpp.getEventType();
            
            
            //Add Built In Variables
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType==XmlPullParser.END_TAG) {
                    System.out.println("End Tag");
                }
                
                
                if(eventType==XmlPullParser.START_TAG) {
                    if(xpp.getName().trim().equals("TR")) {
                        String wTRCode=xpp.getAttributeValue(0);//Read Attribute 1 Value i.e. code
                        String wReportFileName=xpp.getAttributeValue(1);//Read Attribute 1 Value i.e. FileName
                        String wReportName=xpp.getAttributeValue(2);//Read Attribute 1 Value i.e. FileName
                        int ProcessType=Integer.parseInt(xpp.getAttributeValue(3));
                        int IsDefault=Integer.parseInt(xpp.getAttributeValue(4));
                        
                        if(wTRCode.equals(TRCode)) {
                            if(IsDefault==1) {
                                objConfig.TRCode=TRCode;
                                objConfig.ReportFileName=wReportFileName;
                                objConfig.ReportName=wReportName;
                                objConfig.ProcessType=ProcessType;
                                objConfig.IsDefault=IsDefault;
                            }
                        }
                    }
                }
                try {
                    eventType = xpp.next();
                }
                catch(Exception c) {
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return objConfig;
    }
    
}
