/*
 * TReport.java
 *
 * Created on June 26, 2007, 4:17 PM
 *
 *
 */

package TReportWriter;

/**
 *
 * @author  root
 */


import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.io.IOException;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;
import java.net.URLConnection;
import java.net.URL;

public class TReport {
    
    //**** Report Properties ****//
    //**Database Connection**//
    public DBConnection objDBConn=new DBConnection();
    
    //**Page Properties**//
    public TReportPage objReportPage=new TReportPage();
    
    //**Database Query**//
    public String ReportQuery="";
    
    //**Input Parameters**/
    public HashMap colParameters=new HashMap();
    
    //** Report Variables **//
    public HashMap colVariables=new HashMap();
    
    //**Report Bands**//
    public HashMap colBands=new HashMap();
    
    public String ReportFile ="";
    
    public static final int EvaluateNow=0;
    public static final int EvaluateOnReport=1;
    public static final int EvaluateOnGroup=2;
    public static final int EvaluateOnPage=3;
    public static final int EvaluateNone=4;
    
    public boolean Dirty=false;
    
    public static void main(String[] args) {
        TReport.OpenDesigner();
    }
    
    /** Creates a new instance of TReport */
    public TReport() {
        
        //** Add Default Bands **//
        colBands.clear();
        
        //Report Header
        TBand objBand=new TBand();
        objBand.BandName="Report Header";
        objBand.BandHeight=0;
        objBand.BandType=TBand.ReportHeader;
        objBand.SrNo=1;
        colBands.put(Integer.toString(objBand.SrNo),objBand);
        
        //Page Header
        objBand=new TBand();
        objBand.BandName="Page Header";
        objBand.BandHeight=5;
        objBand.BandType=TBand.PageHeader;
        objBand.SrNo=2;
        colBands.put(Integer.toString(objBand.SrNo),objBand);
        
        //Detail
        objBand=new TBand();
        objBand.BandName="Detail";
        objBand.BandHeight=5;
        objBand.BandType=TBand.Detail;
        objBand.SrNo=3;
        colBands.put(Integer.toString(objBand.SrNo),objBand);
        
        //Page Footer
        objBand=new TBand();
        objBand.BandName="Page Footer";
        objBand.BandHeight=2;
        objBand.BandType=TBand.PageFooter;
        objBand.SrNo=4;
        colBands.put(Integer.toString(objBand.SrNo),objBand);
        
        //Report Footer
        objBand=new TBand();
        objBand.BandName="Report Footer";
        objBand.BandHeight=0;
        objBand.BandType=TBand.ReportFooter;
        objBand.SrNo=5;
        colBands.put(Integer.toString(objBand.SrNo),objBand);
        
        objReportPage.PageWidth=132; //132 Columns wide
        objReportPage.PageHeight=80; //80 Columns Long
        
        //Add Built in variables
        
        //(1) Page No.
        TVariable objVariable;
        
        objVariable=new TVariable();
        objVariable.VariableName="PAGE_NO";
        objVariable.EvaluationTime=TReport.EvaluateOnPage;
        objVariable.Expression="PAGE_NO";
        objVariable.BuiltInVariable=true;
        
        colVariables.put(Integer.toString(colVariables.size()+1), objVariable);
        
        //(2) Total Pages
        objVariable=new TVariable();
        objVariable.VariableName="TOTAL_PAGES";
        objVariable.EvaluationTime=TReport.EvaluateOnReport;
        objVariable.Expression="TOTAL_PAGES";
        objVariable.BuiltInVariable=true;
        
        colVariables.put(Integer.toString(colVariables.size()+1), objVariable);
        
        //(3) Current DateTime
        objVariable=new TVariable();
        objVariable.VariableName="DATE";
        objVariable.EvaluationTime=TReport.EvaluateNow;
        objVariable.Expression="DATE";
        objVariable.BuiltInVariable=true;
        
        colVariables.put(Integer.toString(colVariables.size()+1), objVariable);
        
    }
    
    
    public boolean LoadReportFromFile(String FileName) {
        try {
            
            boolean IsEnd=false;
            
            TTextField objTextField=new TTextField();
            TDBField objDBField=new TDBField();
            TParameter objParameter=new TParameter();
            TVariable objVariable=new TVariable();
            
            String currentLoading="";
            
            BufferedReader File;
            
            
            if(FileName.startsWith("http")) {
                
                
                OutputStream out = null;
                URLConnection conn = null;
                InputStream  in = null;
                
                
                URL url = new URL(FileName);
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

                
                File=new BufferedReader(new FileReader(new File("tmpoutput.tmp")));
                
                
            }
            else {
                File=new BufferedReader(new FileReader(new File(FileName)));
            }
            
            
            
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            
            xpp.setInput(File);
            
            //xpp.setInput(new StringReader("Nothing"));
            int eventType = xpp.getEventType();
            
            colBands.clear();
            colParameters.clear();
            colVariables.clear();
            
            //Add Built In Variables
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType==XmlPullParser.END_TAG) {
                    
                    if(currentLoading.equals("TextField")) {
                        if(IsEnd) {
                            TBand objBand=(TBand)colBands.get(Integer.toString(colBands.size()));
                            objBand.colTextFields.put(Integer.toString(objBand.colTextFields.size()+1), objTextField);
                            colBands.put(Integer.toString(colBands.size()), objBand);
                            IsEnd=false;
                        }
                    }
                    
                    
                    if(currentLoading.equals("DBField")) {
                        if(IsEnd) {
                            TBand objBand=(TBand)colBands.get(Integer.toString(colBands.size()));
                            objBand.colDBFields.put(Integer.toString(objBand.colDBFields.size()+1), objDBField);
                            colBands.put(Integer.toString(colBands.size()), objBand);
                            IsEnd=false;
                        }
                    }
                    
                    if(currentLoading.equals("Parameter")) {
                        if(IsEnd) {
                            colParameters.put(Integer.toString(colParameters.size()+1),objParameter);
                            IsEnd=false;
                        }
                    }
                    
                    
                    if(currentLoading.equals("Variable")) {
                        if(IsEnd) {
                            colVariables.put(Integer.toString(colVariables.size()+1),objVariable);
                            IsEnd=false;
                        }
                    }
                    
                }
                
                
                if(eventType==XmlPullParser.START_TAG) {
                    if(!xpp.getName().trim().equals("property")) {
                        currentLoading=xpp.getName().trim();
                    }
                    
                    if(xpp.getName().trim().equals("TextField")) {
                        objTextField=new TTextField();
                    }
                    
                    if(xpp.getName().trim().equals("DBField")) {
                        objDBField=new TDBField();
                    }
                    
                    if(xpp.getName().trim().equals("Parameter")) {
                        objParameter=new TParameter();
                    }
                    
                    if(xpp.getName().trim().equals("Variable")) {
                        objVariable=new TVariable();
                    }
                    
                    
                    //** Loading Band Properties **//
                    if(currentLoading.equals("Band")) {
                        TBand objBand=new TBand();
                        
                        objBand.BandHeight=ConvertToInt(xpp.getAttributeValue(0));
                        objBand.SplitBand=ConvertStringToBool(xpp.getAttributeValue(1));
                        objBand.BandType=ConvertToInt(xpp.getAttributeValue(2));
                        objBand.BandStartRow=ConvertToInt(xpp.getAttributeValue(3));
                        objBand.BandName=xpp.getAttributeValue(4);
                        objBand.GroupExpression=xpp.getAttributeValue(5);
                        objBand.StartGroupOnNewPage=ConvertStringToBool(xpp.getAttributeValue(6));
                        
                        colBands.put(Integer.toString(colBands.size()+1),objBand);
                    }
                    
                    
                    if(currentLoading.equals("ReportQuery")) {
                        ReportQuery=xpp.getAttributeValue(0);
                    }
                    
                    if(currentLoading.equals("DBInfo")) {
                        objDBConn.ConnectionString=xpp.getAttributeValue(0);
                    }
                    
                    if(xpp.getName().trim().equals("property")) {
                        
                        //*** Loading Variables ***//
                        if(currentLoading.equals("Variable")) {
                            if(xpp.getAttributeValue(0).equals("VariableName")) {
                                objVariable.VariableName=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Expression")) {
                                objVariable.Expression=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Function")) {
                                objVariable.Function=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("EvaluationTime")) {
                                objVariable.EvaluationTime=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("EvaluationGroup")) {
                                objVariable.EvaluationGroup=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("BuiltInVariable")) {
                                objVariable.BuiltInVariable=ConvertStringToBool(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("VariableValue")) {
                                objVariable.VariableValue=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("ResetOn")) {
                                objVariable.ResetOn=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("ResetGroupName")) {
                                objVariable.ResetGroupName=xpp.getAttributeValue(1);
                                IsEnd=true;
                            }
                            
                        }
                        
                        //*** Loading Input Parameter***//
                        if(currentLoading.equals("Parameter")) {
                            if(xpp.getAttributeValue(0).equals("ParameterName")) {
                                objParameter.ParameterName=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("DataType")) {
                                objParameter.DataType=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("ParameterValue")) {
                                objParameter.ParameterValue=xpp.getAttributeValue(1);
                                IsEnd=true;
                            }
                        }
                        
                        //** Loading DB Info **//
                        if(currentLoading.equals("DBInfo")) {
                            if(xpp.getAttributeValue(0).equals("ConnectionString")) {
                                objDBConn.ConnectionString=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("UserName")) {
                                objDBConn.UserName=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Password")) {
                                objDBConn.Password=xpp.getAttributeValue(1);
                            }
                        }
                        
                        
                        
                        //** Loading Page Properties **//
                        if(currentLoading.equals("ReportPage")) {
                            if(xpp.getAttributeValue(0).equals("PageHeight")) {
                                objReportPage.PageHeight=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PageWidth")) {
                                objReportPage.PageWidth=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PageSize")) {
                                objReportPage.PageSize=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PaperOrientation")) {
                                objReportPage.PaperOrientation=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("LeftMargin")) {
                                objReportPage.LeftMargin=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("RightMargin")) {
                                objReportPage.RightMargin=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("TopMargin")) {
                                objReportPage.TopMargin=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("BottomMargin")) {
                                objReportPage.BottomMargin=ConvertToInt(xpp.getAttributeValue(1));
                            }
                        }
                        
                        
                        //*** Loading Text Field ***//
                        if(currentLoading.equals("TextField")) {
                            if(xpp.getAttributeValue(0).equals("Text")) {
                                objTextField.Text=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("MultiLine")) {
                                objTextField.MultiLine=ConvertStringToBool(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Width")) {
                                objTextField.Width=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PositionX")) {
                                objTextField.PositionX=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PositionY")) {
                                objTextField.PositionY=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            
                            if(xpp.getAttributeValue(0).equals("PrintCondition")) {
                                objTextField.PrintCondition=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Alignment")) {
                                objTextField.Alignment=ConvertToInt(xpp.getAttributeValue(1));
                                IsEnd=true;
                            }
                            
                        }
                        
                        //*** Loading DB Field ***//
                        if(currentLoading.equals("DBField")) {
                            if(xpp.getAttributeValue(0).equals("Text")) {
                                objDBField.Text=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("MultiLine")) {
                                objDBField.MultiLine=ConvertStringToBool(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Width")) {
                                objDBField.Width=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PositionX")) {
                                objDBField.PositionX=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PositionY")) {
                                objDBField.PositionY=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("PrintCondition")) {
                                objDBField.PrintCondition=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("DataType")) {
                                objDBField.DataType=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("FormatString")) {
                                objDBField.FormatString=xpp.getAttributeValue(1);
                            }
                            
                            if(xpp.getAttributeValue(0).equals("Alignment")) {
                                objDBField.Alignment=ConvertToInt(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("BlankWhenNull")) {
                                objDBField.BlankWhenNull=ConvertStringToBool(xpp.getAttributeValue(1));
                            }
                            
                            if(xpp.getAttributeValue(0).equals("ConvertToWords")) {
                                objDBField.ConvertToWords=ConvertStringToBool(xpp.getAttributeValue(1));
                                IsEnd=true;
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
            
            this.ReportFile=FileName;
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public void SaveToFile(String FileName) {
        try {
            //Write XML File
            BufferedWriter File=new BufferedWriter(new FileWriter(new File(FileName)));
            
            writeline(File,"<?xml version=\"1.0\" encoding=\"UTF-8  ?>\"");
            writeline(File,"<!-- TReportWriter Document - A Textbased Report writer -->");
            
            //** Write DB Connection Info **//
            writeline(File,"<DBInfo value=\""+objDBConn.ConnectionString+"\"/>");
            
            //** Write Database Query **//
            writeline(File,"<ReportQuery value=\""+ReportQuery+"\"/>");
            
            //** Write Page Properties **//
            writeline(File, "<ReportPage>");
            writeline(File,"<property name=\"PageHeight\" value=\""+objReportPage.PageHeight+"\"/>");
            writeline(File,"<property name=\"PageWidth\" value=\""+objReportPage.PageWidth+"\"/>");
            writeline(File,"<property name=\"PageSize\" value=\""+objReportPage.PageSize+"\"/>");
            writeline(File,"<property name=\"PaperOrientation\" value=\""+objReportPage.PaperOrientation+"\"/>");
            writeline(File,"<property name=\"LeftMargin\" value=\""+objReportPage.LeftMargin+"\"/>");
            writeline(File,"<property name=\"RightMargin\" value=\""+objReportPage.RightMargin+"\"/>");
            writeline(File,"<property name=\"TopMargin\" value=\""+objReportPage.TopMargin+"\"/>");
            writeline(File,"<property name=\"BottomMargin\" value=\""+objReportPage.BottomMargin+"\"/>");
            writeline(File,"</ReportPage>");
            
            //** Write Page Properties **//
            writeline(File, "<InputParameters count=\""+colParameters.size()+"\">");
            for(int i=1;i<=colParameters.size();i++) {
                TParameter objParam=(TParameter) colParameters.get(Integer.toString(i));
                writeline(File, "<Parameter>");
                writeline(File,"<property name=\"ParameterName\" value=\""+objParam.ParameterName+"\"/>");
                writeline(File,"<property name=\"DataType\" value=\""+objParam.DataType+"\"/>");
                writeline(File,"<property name=\"ParameterValue\" value=\""+objParam.ParameterValue+"\"/>");
                writeline(File, "</Parameter>");
            }
            writeline(File,"</InputParameters>");
            
            
            //** Write Report Variables
            writeline(File, "<Variables count=\""+colVariables.size()+"\">");
            for(int i=1;i<=colVariables.size();i++) {
                TVariable objVar=(TVariable)colVariables.get(Integer.toString(i));
                writeline(File, "<Variable>");
                writeline(File,"<property name=\"VariableName\" value=\""+objVar.VariableName+"\"/>");
                writeline(File,"<property name=\"Expression\" value=\""+objVar.Expression+"\"/>");
                writeline(File,"<property name=\"Function\" value=\""+objVar.Function+"\"/>");
                writeline(File,"<property name=\"EvaluationTime\" value=\""+objVar.EvaluationTime+"\"/>");
                writeline(File,"<property name=\"EvaluationGroup\" value=\""+objVar.EvaluationGroup+"\"/>");
                writeline(File,"<property name=\"BuiltInVariable\" value=\""+ConvertBoolToString(objVar.BuiltInVariable)+"\"/>");
                writeline(File,"<property name=\"VariableValue\" value=\""+objVar.VariableValue+"\"/>");
                writeline(File,"<property name=\"ResetOn\" value=\""+objVar.ResetOn+"\"/>");
                writeline(File,"<property name=\"ResetGroupName\" value=\""+objVar.ResetGroupName+"\"/>");
                writeline(File, "</Variable>");
            }
            writeline(File,"</Variables>");
            
            
            //** Write Document Bands along with contained objects
            for(int i=1;i<=colBands.size();i++) {
                TBand objBand=(TBand)colBands.get(Integer.toString(i));
                
                writeline(File, "<Band BandHeight=\""+objBand.BandHeight+"\" SplitBand=\""+ConvertBoolToString(objBand.SplitBand)+"\" BandType=\""+objBand.BandType+"\" BandStartRow=\""+objBand.BandStartRow+"\" BandName=\""+objBand.BandName+"\" GroupExpression=\""+objBand.GroupExpression+"\" StartGroupOnNewPage=\""+ConvertBoolToString(objBand.StartGroupOnNewPage)+"\" >");
                
                //Write Text Fields
                for(int j=1;j<=objBand.colTextFields.size();j++) {
                    TTextField objField=(TTextField)objBand.colTextFields.get(Integer.toString(j));
                    
                    writeline(File, "<TextField>");
                    writeline(File,"<property name=\"Text\" value=\""+objField.Text+"\"/>");
                    writeline(File,"<property name=\"MultiLine\" value=\""+ConvertBoolToString(objField.MultiLine)+"\"/>");
                    writeline(File,"<property name=\"Width\" value=\""+objField.Width+"\"/>");
                    writeline(File,"<property name=\"PositionX\" value=\""+objField.PositionX+"\"/>");
                    writeline(File,"<property name=\"PositionY\" value=\""+objField.PositionY+"\"/>");
                    writeline(File,"<property name=\"PrintCondition\" value=\""+objField.PrintCondition+"\"/>");
                    writeline(File,"<property name=\"Alignment\" value=\""+objField.Alignment+"\"/>");
                    writeline(File, "</TextField>");
                }
                
                //Write DB Fields
                for(int j=1;j<=objBand.colDBFields.size();j++) {
                    TDBField objField=(TDBField)objBand.colDBFields.get(Integer.toString(j));
                    
                    writeline(File, "<DBField>");
                    writeline(File,"<property name=\"Text\" value=\""+objField.Text+"\"/>");
                    writeline(File,"<property name=\"DataType\" value=\""+objField.DataType+"\"/>");
                    writeline(File,"<property name=\"MultiLine\" value=\""+ConvertBoolToString(objField.MultiLine)+"\"/>");
                    writeline(File,"<property name=\"Width\" value=\""+objField.Width+"\"/>");
                    writeline(File,"<property name=\"PositionX\" value=\""+objField.PositionX+"\"/>");
                    writeline(File,"<property name=\"PositionY\" value=\""+objField.PositionY+"\"/>");
                    writeline(File,"<property name=\"PrintCondition\" value=\""+objField.PrintCondition+"\"/>");
                    writeline(File,"<property name=\"FormatString\" value=\""+objField.FormatString+"\"/>");
                    writeline(File,"<property name=\"Alignment\" value=\""+objField.Alignment+"\"/>");
                    writeline(File,"<property name=\"BlankWhenNull\" value=\""+ConvertBoolToString(objField.BlankWhenNull)+"\"/>");
                    writeline(File,"<property name=\"ConvertToWords\" value=\""+ConvertBoolToString(objField.ConvertToWords)+"\"/>");
                    writeline(File, "</DBField>");
                }
                
                writeline(File, "</Band>");
            }
            
            
            File.close();
            
            this.ReportFile=FileName;
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void writeline(BufferedWriter File,String writeText) {
        try {
            File.write(writeText);
            File.newLine();
        }
        catch(Exception e) {
            
        }
    }
    
    
    public double ConvertToDouble(String pValue) {
        try {
            return TGLOBAL.round(Double.parseDouble(pValue),4);
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public float ConvertToFloat(String pValue) {
        try {
            
            return Float.parseFloat(pValue);
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public int ConvertToInt(String pValue) {
        try {
            return Integer.parseInt(pValue);
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public String ConvertBoolToString(boolean pValue) {
        try {
            if(pValue) {
                return "true";
            }
            else {
                return "false";
            }
            
        }
        catch(Exception e) {
            return "false";
        }
    }
    
    
    public boolean ConvertStringToBool(String pValue) {
        try {
            if(pValue.trim().equals("true")) {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public TBand getGroupHeaderBand(String BandName) {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.GroupHeader&&fBand.BandName.equals(BandName)) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getGroupFooterBand(String BandName) {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.GroupFooter&&fBand.BandName.equals(BandName)) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getReportHeaderBand() {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.ReportHeader) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getReportFooterBand() {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.ReportFooter) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getPageHeaderBand() {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.PageHeader) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getPageFooterBand() {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.PageFooter) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    public TBand getDetailBand() {
        TBand objBand=new TBand();
        try {
            for(int f=1;f<=colBands.size();f++) {
                TBand fBand=(TBand)colBands.get(Integer.toString(f));
                
                if(fBand.BandType==TBand.Detail) {
                    objBand=fBand;
                }
            }
        }
        catch(Exception e) {
            
        }
        return objBand;
    }
    
    public static void OpenDesigner() {
        try {
            frmDesigner objDesigner=new frmDesigner();
            objDesigner.ShowDesigner();
            
        }
        catch(Exception e) {
//            e.printStackTrace();
        }
    }
    
    public static void PreviewReport(String ReportFileName,Connection dbConn,HashMap Parameters) {
        try {
            TReportEngine objEngine=new TReportEngine();
            
            objEngine.PreviewReport(ReportFileName,dbConn,Parameters);
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    
    public static boolean RunReportToFile(String ReportFileName,Connection dbConn,HashMap Parameters,String OutputFile) {
        try {
            TReportEngine objEngine=new TReportEngine();
            
            return objEngine.RunReportToFile(ReportFileName,dbConn, Parameters, OutputFile);
            
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    
    
}
