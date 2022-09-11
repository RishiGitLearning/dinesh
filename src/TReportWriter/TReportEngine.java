/*
 * clsReportTest.java
 *
 * Created on November 2, 2007, 4:23 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */

import javax.swing.*;
import java.sql.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.*;
import java.text.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import java.io.*;
import TReportWriter.SimpleDataProvider.*;

public class TReportEngine {
    
    Connection objConn;
    Statement stData;
    ResultSet rsData;
    TReport objReport;
    
    public HashMap PrintPages=new HashMap();
    TReportPage objPage;
    private JEP myParser=new JEP();
    
    int RunningLine=0;
    int RunningPage=0;
    
    HashMap RunningGroups=new HashMap();
    boolean PageEndReached=false;
    
    private HashMap colBufferLines=new HashMap();
    
    private TReportWriter.SimpleDataProvider.TTable Data=new TReportWriter.SimpleDataProvider.TTable();
    
    public boolean UseCustomDataProvider=false;
    
    private int DataRowCounter=0;
    
    private boolean DoNotPrintBuffer=false;
    
    /** Creates a new instance of clsReportTest */
    public TReportEngine() {
        
    }
    
    public void setDataProvider(TReportWriter.SimpleDataProvider.TTable objData) {
        this.Data=objData;
    }
    
    public TReportWriter.SimpleDataProvider.TTable getDataProvider() {
        return this.Data;
    }
    
    public void PreviewReport(String ReportFileName) {
        try {
            TReportEngine objEngine=new TReportEngine();
            objEngine.RunReport(ReportFileName);
            
            frmPrintPreview objPreview=new frmPrintPreview();
            objPreview.objReportEngine=objEngine;
            
            objPreview.ShowPreview();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void PreviewReport(String ReportFileName,Connection dbConn) {
        try {
            TReportEngine objEngine=new TReportEngine();
            objEngine.RunReport(ReportFileName,dbConn);
            
            frmPrintPreview objPreview=new frmPrintPreview();
            objPreview.objReportEngine=objEngine;
            
            objPreview.ShowPreview();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void PreviewReport(String ReportFileName,Connection dbConn,HashMap Parameters) {
        try {
            TReportEngine objEngine=new TReportEngine();
            objEngine.RunReport(ReportFileName,dbConn,Parameters);
            
            frmPrintPreview objPreview=new frmPrintPreview();
            objPreview.objReportEngine=objEngine;
            
            objPreview.ShowPreview();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void PreviewReport(String ReportFileName,HashMap Parameters,TReportWriter.SimpleDataProvider.TTable CustomData) {
        try {
            
            TReportEngine objEngine=new TReportEngine();
            objEngine.RunReport(ReportFileName,Parameters,CustomData);
            
            
            frmPrintPreview objPreview=new frmPrintPreview();
            objPreview.objReportEngine=objEngine;
            
            objPreview.ShowPreview();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void RunReport(String ReportFileName) {
        try {
            
            boolean HasNoData=false;
            
            //Load Report
            objReport=new TReport();
            objReport.LoadReportFromFile(ReportFileName);
            
            if(!UseCustomDataProvider) {
                //Open Connection
                objConn=data.getConn(objReport.objDBConn.ConnectionString);
                
                //Replace Input Parameters
                objReport.ReportQuery=UpdateReportQuery(objReport);
                
                //Open ResultSet
                stData=objConn.createStatement();
                rsData=stData.executeQuery(objReport.ReportQuery);
                rsData.first();
                
                if(rsData.getRow()<=0) {
                    HasNoData=true;
                }
            }
            else {
                DataRowCounter=1;
                
                if(Data.getRowCount()<=0) {
                    HasNoData=true;
                }
            }
            
            if(!HasNoData) {
                
                InitializeFiller();
                
                //Explode Printing Pages and Out to system console
                for(int i=1;i<=(PrintPages.size());i++) {
                    TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                    
                    for(int p=1;p<=objPage.PrintLines.size();p++) {
                        
                        if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                            TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                            
                            String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                            
                            if(objLine!=null) {
                                Iterator Keys=objLine.colElements.keySet().iterator();
                                
                                while(Keys.hasNext()) {
                                    String key=(String)Keys.next();
                                    
                                    TElement objElement=(TElement)objLine.colElements.get(key);
                                    
                                    try {
                                        PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                    }
                                    catch(Exception SubStr) {
                                        
                                    }
                                }
                            }
                            
                            objLine.OutputText=PrintLine;
                            objPage.PrintLines.put(Integer.toString(p),objLine);
                            
                        }
                        
                        PrintPages.put(Integer.toString(i),objPage);
                    }
                }
                
            }
            else {
                JOptionPane.showMessageDialog(null,"No Data retrieved for database query");
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void RunReport(String ReportFileName,Connection dbConn) {
        try {
            
            boolean HasNoData=false;
            
            //Load Report
            objReport=new TReport();
            objReport.LoadReportFromFile(ReportFileName);
            
            if(!UseCustomDataProvider) {
                //Open Connection
                objConn=dbConn;
                
                //Replace Input Parameters
                objReport.ReportQuery=UpdateReportQuery(objReport);
                
                //Open ResultSet
                stData=objConn.createStatement();
                
                
                rsData=stData.executeQuery(objReport.ReportQuery);
                rsData.first();
                
                if(rsData.getRow()<=0) {
                    HasNoData=true;
                }
                
            }
            else {
                DataRowCounter=1;
                
                if(Data.getRowCount()<=0) {
                    HasNoData=true;
                }
            }
            
            if(!HasNoData) {
                
                InitializeFiller();
                
                //Explode Printing Pages and Out to system console
                for(int i=1;i<=(PrintPages.size());i++) {
                    TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                    
                    for(int p=1;p<=objPage.PrintLines.size();p++) {
                        
                        if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                            TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                            
                            String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                            
                            if(objLine!=null) {
                                Iterator Keys=objLine.colElements.keySet().iterator();
                                
                                while(Keys.hasNext()) {
                                    String key=(String)Keys.next();
                                    
                                    TElement objElement=(TElement)objLine.colElements.get(key);
                                    
                                    try {
                                        PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                    }
                                    catch(Exception SubStr) {
                                        
                                    }
                                }
                            }
                            
                            objLine.OutputText=PrintLine;
                            objPage.PrintLines.put(Integer.toString(p),objLine);
                            
                        }
                        
                        PrintPages.put(Integer.toString(i),objPage);
                    }
                }
                
            }
            else {
                JOptionPane.showMessageDialog(null,"No Data retrieved for database query");
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void RunReport(String ReportFileName,Connection dbConn,HashMap Parameters) {
        try {
            
            boolean HasNoData=false;
            
            //Load Report
            objReport=new TReport();
            objReport.LoadReportFromFile(ReportFileName);
            
            //Now Replace Parameter Values
            Iterator keys=Parameters.keySet().iterator();
            
            while(keys.hasNext()) {
                String ParamName=(String)keys.next();
                String ParamValue=(String)Parameters.get(ParamName);
                
                for(int i=1;i<=objReport.colParameters.size();i++) {
                    TParameter objParam= ((TParameter)objReport.colParameters.get(Integer.toString(i))) ;
                    if(objParam.ParameterName.equals(ParamName)) {
                        objParam.ParameterValue=ParamValue;
                        objReport.colParameters.put(Integer.toString(i),objParam);
                    }
                }
            }
            
            if(!UseCustomDataProvider) {
                //Open Connection
                objConn=dbConn;
                
                
                //Replace Input Parameters
                objReport.ReportQuery=UpdateReportQuery(objReport);
                
                
                //Open ResultSet
                stData=objConn.createStatement();
                rsData=stData.executeQuery(objReport.ReportQuery);
                rsData.first();
                
                if(rsData.getRow()<=0) {
                    HasNoData=true;
                }
                
            }
            else {
                DataRowCounter=1;
                
                if(Data.getRowCount()==0) {
                    HasNoData=true;
                }
                
            }
            
            if(!HasNoData) {
                
                InitializeFiller();
                
                //Explode Printing Pages and Out to system console
                for(int i=1;i<=(PrintPages.size());i++) {
                    TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                    
                    for(int p=1;p<=objPage.PrintLines.size();p++) {
                        
                        if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                            TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                            
                            String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                            
                            if(objLine!=null) {
                                Iterator Keys=objLine.colElements.keySet().iterator();
                                
                                while(Keys.hasNext()) {
                                    String key=(String)Keys.next();
                                    
                                    TElement objElement=(TElement)objLine.colElements.get(key);
                                    
                                    try {
                                        PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                    }
                                    catch(Exception SubStr) {
                                        
                                    }
                                }
                            }
                            
                            objLine.OutputText=PrintLine;
                            objPage.PrintLines.put(Integer.toString(p),objLine);
                            
                        }
                        
                        PrintPages.put(Integer.toString(i),objPage);
                    }
                }
                
            }
            else {
                JOptionPane.showMessageDialog(null,"No Data retrieved for database query");
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void RunReport(String ReportFileName,HashMap Parameters,TReportWriter.SimpleDataProvider.TTable CustomData) {
        try {
            
            boolean HasNoData=false;
            
            //Load Report
            objReport=new TReport();
            objReport.LoadReportFromFile(ReportFileName);
            
            objReport.colParameters=Parameters;
            
            UseCustomDataProvider=true;
            setDataProvider(CustomData);
            
            if(!UseCustomDataProvider) {
                //Open Connection
                //objConn=dbConn;
                
                //Replace Input Parameters
                objReport.ReportQuery=UpdateReportQuery(objReport);
                
                
                //Open ResultSet
                stData=objConn.createStatement();
                rsData=stData.executeQuery(objReport.ReportQuery);
                rsData.first();
                
                if(rsData.getRow()<=0) {
                    HasNoData=true;
                }
                
            }
            else {
                DataRowCounter=1;
                
                if(Data.getRowCount()==0) {
                    HasNoData=true;
                }
            }
            
            
            if(!HasNoData) {
                
                InitializeFiller();
                
                //Explode Printing Pages and Out to system console
                for(int i=1;i<=(PrintPages.size());i++) {
                    TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                    
                    for(int p=1;p<=objPage.PrintLines.size();p++) {
                        
                        if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                            TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                            
                            String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                            
                            if(objLine!=null) {
                                Iterator Keys=objLine.colElements.keySet().iterator();
                                
                                while(Keys.hasNext()) {
                                    String key=(String)Keys.next();
                                    
                                    TElement objElement=(TElement)objLine.colElements.get(key);
                                    
                                    try {
                                        PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                    }
                                    catch(Exception SubStr) {
                                        
                                    }
                                }
                            }
                            
                            objLine.OutputText=PrintLine;
                            objPage.PrintLines.put(Integer.toString(p),objLine);
                            
                        }
                        
                        PrintPages.put(Integer.toString(i),objPage);
                    }
                }
                
            }
            else {
                JOptionPane.showMessageDialog(null,"No Data retrieved for database query");
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean RunReportToFile(String ReportFileName,Connection dbConn,HashMap Parameters,String OutputFileName) {
        
        try {
            
            RunReport(ReportFileName,dbConn,Parameters);
            
            //Create a new File
            BufferedWriter OutputFile=new BufferedWriter(new FileWriter(new File(OutputFileName)));
            
            for(int i=1;i<=(PrintPages.size());i++) {
                TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                
                for(int p=1;p<=objPage.PrintLines.size();p++) {
                    
                    if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                        TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                        
                        String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                        
                        if(objLine!=null) {
                            Iterator Keys=objLine.colElements.keySet().iterator();
                            
                            while(Keys.hasNext()) {
                                String key=(String)Keys.next();
                                
                                TElement objElement=(TElement)objLine.colElements.get(key);
                                
                                try {
                                    PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                }
                                catch(Exception SubStr) {
                                    
                                }
                            }
                        }
                        
                        objLine.OutputText=PrintLine;
                        
                        OutputFile.write(PrintLine);
                        OutputFile.newLine();
                        
                        objPage.PrintLines.put(Integer.toString(p),objLine);
                        
                    }
                    
                    PrintPages.put(Integer.toString(i),objPage);
                }
                
                OutputFile.write("\f");
            }
            
            OutputFile.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    public boolean RunReportToFile(String ReportFileName,String OutputFileName) {
        
        try {
            
            RunReport(ReportFileName);
            
            //Create a new File
            BufferedWriter OutputFile=new BufferedWriter(new FileWriter(new File(OutputFileName)));
            
            for(int i=1;i<=(PrintPages.size());i++) {
                TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                
                for(int p=1;p<=objPage.PrintLines.size();p++) {
                    
                    if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                        TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                        
                        String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                        
                        if(objLine!=null) {
                            Iterator Keys=objLine.colElements.keySet().iterator();
                            
                            while(Keys.hasNext()) {
                                String key=(String)Keys.next();
                                
                                TElement objElement=(TElement)objLine.colElements.get(key);
                                
                                try {
                                    PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                }
                                catch(Exception SubStr) {
                                    
                                }
                            }
                        }
                        
                        objLine.OutputText=PrintLine;
                        
                        OutputFile.write(PrintLine);
                        OutputFile.newLine();
                        
                        objPage.PrintLines.put(Integer.toString(p),objLine);
                        
                    }
                    
                    PrintPages.put(Integer.toString(i),objPage);
                }
                
                OutputFile.write("\f");
            }
            
            OutputFile.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    public boolean RunReportToFile(String ReportFileName,HashMap Parameters,String OutputFileName,TReportWriter.SimpleDataProvider.TTable CustomData) {
        
        try {
            
            RunReport(ReportFileName,Parameters,CustomData);
            
            //Create a new File
            BufferedWriter OutputFile=new BufferedWriter(new FileWriter(new File(OutputFileName)));
            
            for(int i=1;i<=(PrintPages.size());i++) {
                TReportPage objPage=(TReportPage)PrintPages.get(Integer.toString(i));
                
                for(int p=1;p<=objPage.PrintLines.size();p++) {
                    
                    if(objPage.PrintLines.containsKey(Integer.toString(p))) {
                        TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(p));
                        
                        String PrintLine=TGLOBAL.Replicate(" ", objReport.objReportPage.PageWidth);
                        
                        if(objLine!=null) {
                            Iterator Keys=objLine.colElements.keySet().iterator();
                            
                            while(Keys.hasNext()) {
                                String key=(String)Keys.next();
                                
                                TElement objElement=(TElement)objLine.colElements.get(key);
                                
                                try {
                                    PrintLine=PrintLine.substring(0,objElement.PositionX)+objElement.Content+PrintLine.substring(objElement.PositionX+objElement.Content.length());
                                }
                                catch(Exception SubStr) {
                                    
                                }
                            }
                        }
                        
                        objLine.OutputText=PrintLine;
                        
                        OutputFile.write(PrintLine);
                        OutputFile.newLine();
                        
                        objPage.PrintLines.put(Integer.toString(p),objLine);
                        
                    }
                    
                    PrintPages.put(Integer.toString(i),objPage);
                }
                
                OutputFile.write("\f");
            }
            
            OutputFile.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    private void InitializeFiller() {
        try {
            //Initialize Printing
            PrintPages.clear();
            RunningLine=0;
            RunningPage=0;
            
            //Initialize Variable Values
            for(int i=1;i<=objReport.colVariables.size();i++) {
                TVariable objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                objVariable.VariableValue="";
                
                objReport.colVariables.put(Integer.toString(i),objVariable);
            }
            
            //Add Page and Start Printing
            StartFilling();
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void StartFilling() {
        try {
            
            RunningPage++;
            RunningLine=0;
            DataRowCounter=1;
            
            objPage=new TReportPage();
            objPage.PrintLines.clear();
            
            //If it's a first Page. Fillup Report Header Band
            if(RunningPage==1) {
                
                TBand objBand=getBand(TBand.ReportHeader);
                
                FillBand(objBand);
            }
            
            //Print Page Header
            TBand objBand=getBand(TBand.PageHeader);
            
            FillBand(objBand);
            
            if(RunningPage==1) {
                //Print Detail Bands
                FillDetailBands();
            }
            
            objBand=getBand(TBand.PageFooter);
            
            FillBand(objBand);
            
            EvalPageLevelVariables();
            
            
            //Check whether the last page is blank
            if(objPage.PrintLines.size()>0) {
                boolean IsBlankPage=true;
                
                for(int i=1;i<=objPage.PrintLines.size();i++) {
                    TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(i));
                    
                    if(objLine.colElements.size()>0) {
                        IsBlankPage=false;
                    }
                    
                }
                
                if(!IsBlankPage) {
                    PrintPages.put(Integer.toString(PrintPages.size()+1), objPage);
                }
            }
            
            EvalReportLevelVariables();
            
            objBand=getBand(TBand.ReportFooter);
            
            if(objBand.BandHeight>0) {
                FillBand(objBand);
                
                //Add Page to the list
                PrintPages.put(Integer.toString(PrintPages.size()+1), objPage);
            }
            //            objPage=new TReportPage();
            //            objPage.PrintLines.clear();
            //
            //            EvalReportLevelVariables();
            //
            //            objBand=getBand(TBand.ReportFooter);
            //
            //            FillBand(objBand);
            //
            //            PrintPages.put(Integer.toString(PrintPages.size()+1), objPage);
        }
        catch(Exception e) {
            
        }
        
        
    }
    
    
    private void AdvancePage() {
        try {
            
            TBand objBand=getBand(TBand.PageFooter);
            FillBand(objBand);
            
            //********* Evaluate Page Level Variables ************//
            EvalPageLevelVariables();
            //****************************************************//
            
            PrintPages.put(Integer.toString(PrintPages.size()+1), objPage);
            
            RunningPage++;
            RunningLine=0;
            
            objPage=new TReportPage();
            objPage.PrintLines.clear();
            
            objBand=getBand(TBand.PageHeader);
            
            FillBand(objBand);
            
            ResetPageLevelVariables();
            
        }
        catch(Exception e) {
            
        }
    }
    
    
    
    private TBand getBand(int BandType) {
        TBand objBand=new TBand();
        
        try {
            
            for(int i=1;i<=objReport.colBands.size();i++) {
                if(((TBand)objReport.colBands.get(Integer.toString(i))).BandType==BandType) {
                    objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return objBand;
        
    }
    
    
    private TBand getGroupBand(int BandType,String GroupName) {
        
        TBand objBand=new TBand();
        
        try {
            
            for(int i=1;i<=objReport.colBands.size();i++) {
                if(((TBand)objReport.colBands.get(Integer.toString(i))).BandType==BandType) {
                    if(((TBand)objReport.colBands.get(Integer.toString(i))).BandName.equals(GroupName)) {
                        objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                    }
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return objBand;
    }
    
    
    
    
    private int getGroupCount() {
        int GroupCount=0;
        
        try {
            
            for(int i=1;i<=objReport.colBands.size();i++) {
                if(((TBand)objReport.colBands.get(Integer.toString(i))).BandType==TBand.GroupHeader) {
                    GroupCount++;
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return GroupCount;
    }
    
    
    
    
    private void FillBand(TBand objBand){
        try {
            
            //****** Evaluate Detail Level Variables *********//
            //  Detail Band Variable Evaluation //
            if(objBand.BandType==TBand.Detail) {
                EvalDetailLevelVariables();
            }
            //************************************************//
            
            for(int i=objBand.BandStartRow;i<(objBand.BandStartRow+objBand.BandHeight);i++) {
                
                if(i>0) {
                    //Multiline Band.
                    RunningLine++;
                }
                else {
                    if(colBufferLines.size()>0&&(!DoNotPrintBuffer)) {
                        
                    }
                    else {
                        RunningLine++;
                    }
                }
                
                TReportLine objReportLine;
                objReportLine=new TReportLine();
                TReportLine objPrintedBufferedLine;
                int PrintedBufferedLine=0;
                objPrintedBufferedLine=new TReportLine();
                
                
                //*********=============  Printing Buffered/Wrapped Lines ************==================//
                if(i==0) {
                    if(colBufferLines.size()>0&&(!DoNotPrintBuffer)) {
                        
                        for(int w=2;w<=colBufferLines.size()+1;w++) {
                            
                            RunningLine++;
                            
                            objReportLine=(TReportLine)colBufferLines.get(Integer.toString(w));
                            
                            objPage.PrintLines.put(Integer.toString(objPage.PrintLines.size()+1), objReportLine);
                            
                            //========= Check for the end of Page ================//
                            TBand objFooterBand=getBand(TBand.PageFooter);
                            int EndRow=objReport.objReportPage.PageHeight-objFooterBand.BandHeight;
                            
                            if(RunningLine==EndRow) {
                                DoNotPrintBuffer=true;
                                AdvancePage();
                                DoNotPrintBuffer=false;
                            }
                            
                        }
                        
                        colBufferLines.clear();
                        objReportLine=new TReportLine();
                        RunningLine++;
                    }
                    
                }
                else {
                    if(colBufferLines.size()>0&&(!DoNotPrintBuffer)) {
                        
                        HashMap TempBuffer=new HashMap();
                        
                        for(int l=2;l<=colBufferLines.size()+1;l++) {
                            TempBuffer.put(Integer.toString(l),colBufferLines.get(Integer.toString(l)));
                        }
                        
                        TReportLine tempLine=(TReportLine)colBufferLines.get("2");
                        
                        Iterator tempKeys=tempLine.colElements.keySet().iterator();
                        
                        while(tempKeys.hasNext()) {
                            Object key=tempKeys.next();
                            objReportLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),tempLine.colElements.get(key));
                            
                            TElement objElement=(TElement)tempLine.colElements.get(key);
                        }
                        
                        objPrintedBufferedLine=objReportLine;
                        PrintedBufferedLine=1;
                        
                        colBufferLines.clear();
                        
                        for(int l=3;l<=TempBuffer.size()+1;l++) {
                            colBufferLines.put(Integer.toString(colBufferLines.size()+2),TempBuffer.get(Integer.toString(l)));
                        }
                        
                        TBand objFooterBand=getBand(TBand.PageFooter);
                        int EndRow=objReport.objReportPage.PageHeight-objFooterBand.BandHeight;
                        
                        if(RunningLine==EndRow) {
                            DoNotPrintBuffer=true;
                            AdvancePage();
                            DoNotPrintBuffer=false;
                        }
                    }
                }
                //*****************************************************************************************//
                
                
                
                //**************************************************************************//
                //***** ================ Printing Text Fields ================ ************//
                //*************************************************************************//
                for(int e=1;e<=objBand.colTextFields.size();e++) {
                    
                    TTextField objTextField=(TTextField)objBand.colTextFields.get(Integer.toString(e));
                    
                    if(objTextField.PositionY==i) {
                        
                        objReportLine.LineNo=i;
                        
                        //Fill up the value
                        String ElementText="";
                        
                        
                        boolean PrintToBuffer=false;
                        
                        if(PrintedBufferedLine>0) {
                            
                            Iterator objPE=objPrintedBufferedLine.colElements.keySet().iterator();
                            
                            while(objPE.hasNext()) {
                                TElement objpeElement=(TElement)objPrintedBufferedLine.colElements.get(objPE.next());
                                
                                if(objTextField.PositionX==objpeElement.PositionX) {
                                    //Element Comes below printed content
                                    PrintToBuffer=true;
                                }
                                
                            }
                        }
                        
                        if(objTextField.Text.length()<=objTextField.Width) {
                        }
                        else {
                            ElementText=objTextField.Text.substring(0,objTextField.Width);
                        }
                        
                        if(objTextField.Alignment==TTextField.Left) {
                            ElementText=objTextField.Text.trim();
                        }
                        
                        if(objTextField.Alignment==TTextField.Right) {
                            ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-objTextField.Text.trim().length()))+objTextField.Text.trim();
                        }
                        
                        if(objTextField.Alignment==TTextField.Center) {
                            ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-objTextField.Text.trim().length())/2 )+objTextField.Text.trim()+TGLOBAL.Replicate(" ", (objTextField.Width-objTextField.Text.trim().length())/2 );
                        }
                        
                        TElement objElement=new TElement();
                        objElement.PositionX=objTextField.PositionX;
                        objElement.PositionY=e;
                        objElement.Content=ElementText;
                        
                        
                        if(PrintToBuffer) {
                            TReportLine objLine=new TReportLine();
                            objLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),objElement);
                            colBufferLines.put(Integer.toString(colBufferLines.size()+2),objLine);
                            
                        }
                        else {
                            objReportLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),objElement);
                        }
                        
                        
                    }
                    
                }
                //*********************************************************************************//
                //******** ================== End of Text Fields ======================= *********//
                //********************************************************************************//
                
                
                
                //**************************************************************************//
                //******** ================ Printing DB Fields ================ ************//
                //**************************************************************************//
                for(int e=1;e<=objBand.colDBFields.size();e++) {
                    
                    TDBField objTextField=(TDBField)objBand.colDBFields.get(Integer.toString(e));
                    
                    boolean IsVariable=false;
                    String VariableName="";
                    
                    if(objTextField.PositionY==i) {
                        
                        objReportLine.LineNo=i;
                        
                        //Fill up the value
                        String ElementText="";
                        
                        
                        boolean PrintToBuffer=false;
                        
                        if(PrintedBufferedLine>0) {
                            
                            Iterator objPE=objPrintedBufferedLine.colElements.keySet().iterator();
                            
                            while(objPE.hasNext()) {
                                TElement objpeElement=(TElement)objPrintedBufferedLine.colElements.get(objPE.next());
                                
                                if(objTextField.PositionX==objpeElement.PositionX) {
                                    //Element Comes below printed content
                                    PrintToBuffer=true;
                                }
                                
                            }
                        }
                        
                        //Evaluate the Text Content of the DB Field
                        if(objTextField.Text.startsWith("$F{")) {
                            String FieldName=objTextField.Text.substring(3);
                            FieldName=FieldName.trim().substring(0,FieldName.trim().length()-1);
                            
                            
                            
                            if(UseCustomDataProvider) {
                                try {
                                    if(DataRowCounter>Data.getRowCount()) {
                                        ElementText=Data.Rows(Data.getRowCount()).getValue(FieldName);
                                    }
                                    else {
                                        ElementText=Data.Rows(DataRowCounter).getValue(FieldName);
                                        
                                    }
                                }
                                catch(Exception c) {
                                    System.out.println("Couldn't find "+FieldName);
                                    c.printStackTrace();
                                    ElementText="";
                                }
                                
                            }
                            else {
                                //ElementText=rsData.getObject(FieldName).toString();
                                ElementText=readFieldValue(rsData, FieldName, "");
                            }
                            
                        }
                        
                        //Evaluate Variables
                        if(objTextField.Text.startsWith("$V{")) {
                            IsVariable=true;
                            String FieldName=objTextField.Text.substring(3);
                            FieldName=FieldName.trim().substring(0,FieldName.length()-1);
                            
                            VariableName=FieldName;
                            
                            ElementText=getVariableValue(FieldName);
                        }
                        
                        
                        //Evaluate Parameters
                        if(objTextField.Text.startsWith("$P{")) {
                            IsVariable=false;
                            String FieldName=objTextField.Text.substring(3);
                            FieldName=FieldName.trim().substring(0,FieldName.length()-1);
                            
                            VariableName=FieldName;
                            ElementText=getParameterValue(FieldName);
                        }
                        
                        //Special Query Tag
                        if(objTextField.Text.startsWith("$Q{")) {
                            IsVariable=false;
                            String FieldName=objTextField.Text.trim().substring(3);
                            FieldName=FieldName.trim().substring(0,FieldName.trim().length()-1);
                            
                            //It's Query
                            FieldName=UpdateExpression(FieldName);
                            ElementText=getQueryResult(FieldName);
                        }
                        
                        //Format Value
                        if(!objTextField.FormatString.trim().equals("")) {
                            if(objTextField.DataType.equals("Numeric")) {
                                
                                if(objTextField.ConvertToWords) {
                                    ElementText=(new NumWord()).convertNumToWord(EITLERPGLOBAL.round(objReport.ConvertToDouble(ElementText),2));
                                }
                                else {
                                    DecimalFormat dFormat=new DecimalFormat(objTextField.FormatString);
                                    ElementText=dFormat.format(objReport.ConvertToDouble(ElementText));
                                    
                                    if(objTextField.BlankWhenNull) {
                                        if(TGLOBAL.CDbl(ElementText)==0) {
                                            ElementText="";
                                        }
                                    }
                                }
                                
                            }
                        }
                        
                        
                        if(objTextField.DataType.equals("Date")) {
                            ElementText=TGLOBAL.formatDate(ElementText);
                        }
                        
                        if(ElementText.length()<=objTextField.Width) {
                            
                            ElementText=ElementText+TGLOBAL.Replicate(" ", objTextField.Width-ElementText.length());
                            
                            if(objTextField.Alignment==TTextField.Right) {
                                ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length()))+ElementText.trim();
                            }
                            
                            if(objTextField.Alignment==TTextField.Center) {
                                ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 )+ElementText.trim()+TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 );
                            }
                            
                        }
                        else {
                            
                            HashMap WrappedText=wrapText(ElementText,objTextField.Width);
                            int BufferStart=2;
                            
                            if(PrintToBuffer) {
                                
                                for(int w=1;w<=WrappedText.size();w++) {
                                    
                                    ElementText=(String)WrappedText.get(Integer.toString(w));
                                    
                                    if(objTextField.Alignment==TTextField.Right) {
                                        ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length()))+ElementText.trim();
                                    }
                                    
                                    if(objTextField.Alignment==TTextField.Center) {
                                        ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 )+ElementText.trim()+TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 );
                                    }
                                    
                                    TReportLine objLine=new TReportLine();
                                    
                                    TElement objElement=new TElement();
                                    objElement.PositionX=objTextField.PositionX;
                                    objElement.PositionY=e;
                                    objElement.Content=ElementText;
                                    
                                    objLine.colElements.put(Integer.toString(objLine.colElements.size()+1),objElement);
                                    colBufferLines.put(Integer.toString(colBufferLines.size()+2),objLine);
                                    
                                }
                                
                                
                            }
                            
                            if(!PrintToBuffer) {
                                for(int w=2;w<=WrappedText.size();w++) {
                                    
                                    ElementText=(String)WrappedText.get(Integer.toString(w));
                                    
                                    if(objTextField.Alignment==TTextField.Right) {
                                        ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length()))+ElementText.trim();
                                    }
                                    
                                    if(objTextField.Alignment==TTextField.Center) {
                                        ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 )+ElementText.trim()+TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 );
                                    }
                                    
                                    TReportLine objLine=new TReportLine();
                                    
                                    TElement objElement=new TElement();
                                    objElement.PositionX=objTextField.PositionX;
                                    objElement.PositionY=e;
                                    objElement.Content=ElementText;
                                    
                                    if(colBufferLines.containsKey(Integer.toString(w))) {
                                        objLine=(TReportLine)colBufferLines.get(Integer.toString(w));
                                        objLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),objElement);
                                        colBufferLines.put(Integer.toString(w),objLine);
                                    }
                                    else {
                                        objLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),objElement);
                                        colBufferLines.put(Integer.toString(colBufferLines.size()+2),objLine);
                                    }
                                    
                                }
                            }
                            
                            ElementText=(String)WrappedText.get(Integer.toString(1));
                        }
                        
                        
                        if(objTextField.Alignment==TTextField.Right) {
                            ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length()))+ElementText.trim();
                        }
                        
                        
                        if(objTextField.Alignment==TTextField.Center) {
                            ElementText=TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 )+ElementText.trim()+TGLOBAL.Replicate(" ", (objTextField.Width-ElementText.trim().length())/2 );
                        }
                        
                        TElement objElement=new TElement();
                        objElement.PositionX=objTextField.PositionX;
                        objElement.PositionY=e;
                        objElement.Content=ElementText;
                        
                        if(IsVariable) {
                            TVariable objVar=getVariable(VariableName);
                            
                            if(objVar.EvaluationTime==TReport.EvaluateNow) {
                                
                                objElement.EvaluationPending=false;
                                
                            }
                            else {
                                
                                //This is to be evaluated
                                
                                
                                if(objVar.EvaluationTime==TReport.EvaluateOnGroup) {
                                    if(objBand.BandType!=TBand.GroupFooter) {
                                        objElement.EvaluationPending=true;
                                        if(RunningGroups.size()>0) {
                                            TBand currentGroup=(TBand)RunningGroups.get(Integer.toString(RunningGroups.size()));
                                            objElement.EvaluationTime=TReport.EvaluateOnGroup;
                                            objElement.EvaluationGroup=currentGroup.BandName;
                                            objElement.VariableName=VariableName;
                                        }
                                    }
                                }
                                
                                
                                
                                if(objVar.EvaluationTime==TReport.EvaluateOnPage) {
                                    if(objBand.BandType!=TBand.PageFooter) {
                                        objElement.EvaluationPending=true;
                                        objElement.EvaluationTime=TReport.EvaluateOnPage;
                                        objElement.VariableName=VariableName;
                                    }
                                }
                                
                                
                                if(objVar.EvaluationTime==TReport.EvaluateOnReport) {
                                    if(objBand.BandType!=TBand.ReportFooter) {
                                        objElement.EvaluationPending=true;
                                        objElement.EvaluationTime=TReport.EvaluateOnReport;
                                        objElement.VariableName=VariableName;
                                    }
                                }
                                
                            }
                            
                        }
                        
                        
                        
                        if(PrintToBuffer) {
                            /*TReportLine objNewLine=new TReportLine();
                             
                            if(colBufferLines.containsKey(Integer.toString(i+2)))
                            {
                            objNewLine=(TReportLine)colBufferLines.get(Integer.toString(i+2));
                            objNewLine.colElements.put(Integer.toString(objNewLine.colElements.size()+1),objElement);
                            colBufferLines.put(Integer.toString(i+2), objNewLine);
                            }
                            else
                            {
                            objNewLine.colElements.put(Integer.toString(objNewLine.colElements.size()+1),objElement);
                            colBufferLines.put(Integer.toString(colBufferLines.size()+2), objNewLine);
                            }*/
                            
                        }
                        else {
                            objReportLine.colElements.put(Integer.toString(objReportLine.colElements.size()+1),objElement);
                        }
                    }
                    //*********************************************************************************//
                    //******** ================== End of DB Fields ======================= *********//
                    //********************************************************************************//
                    
                }
                
                
                //=====> Append Prepared Print Line to the Page
                objPage.PrintLines.put(Integer.toString(objPage.PrintLines.size()+1), objReportLine);
                
                //====Check for the End of the Page=====//
                if(objBand.BandType!=TBand.PageFooter&&objBand.BandType!=TBand.ReportFooter) {
                    TBand objFooterBand=getBand(TBand.PageFooter);
                    int EndRow=objReport.objReportPage.PageHeight-objFooterBand.BandHeight;
                    
                    if(RunningLine==EndRow) {
                        DoNotPrintBuffer=true;
                        AdvancePage();
                        DoNotPrintBuffer=false;
                    }
                }
                
            }
            
            if(objBand.BandType==TBand.Detail) {
                ResetDetailLevelVariables();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void FillDetailBand(TBand objBand){
        try {
            boolean Done=false;
            String LastGroupValue="";
            
            if(RunningGroups.size()>0) {
                TBand GroupBand=(TBand)RunningGroups.get(Integer.toString(RunningGroups.size()));
                
                String GroupExpression=GroupBand.GroupExpression;
                
                String FieldName="";
                
                if(GroupExpression.startsWith("$F{")) {
                    FieldName=GroupExpression.substring(3);
                    FieldName=FieldName.substring(0,FieldName.length()-1);
                }
                
                if(UseCustomDataProvider) {
                    LastGroupValue=Data.Rows(DataRowCounter).getValue(FieldName);
                }
                else {
                    LastGroupValue=readFieldValue(rsData, FieldName,"");
                }
            }
            
            //This is where we iterate the resultset
            while(!Done) {
                //Check EOF
                
                if(UseCustomDataProvider) {
                    if(DataRowCounter>Data.getRowCount()) {
                        Done=true;
                        break;
                    }
                }
                else {
                    if(rsData.isAfterLast()) {
                        Done=true;
                        break;
                    }
                }
                
                //Check whether Group is running
                if(RunningGroups.size()>0) {
                    TBand GroupBand=(TBand)RunningGroups.get(Integer.toString(RunningGroups.size()));
                    
                    String GroupExpression=GroupBand.GroupExpression;
                    
                    String FieldName="";
                    
                    if(GroupExpression.startsWith("$F{")) {
                        FieldName=GroupExpression.substring(3);
                        FieldName=FieldName.substring(0,FieldName.length()-1);
                    }
                    
                    String CurrentValue="";
                    
                    if(UseCustomDataProvider) {
                        CurrentValue=Data.Rows(DataRowCounter).getValue(FieldName);
                    }
                    else {
                        //CurrentValue=rsData.getObject(FieldName).toString();
                        CurrentValue=readFieldValue(rsData, FieldName,"");
                    }
                    
                    if(!CurrentValue.equals(LastGroupValue)) {
                        // Group Break Detected //
                        
                        //Group Footer should be printed here
                        if(UseCustomDataProvider) {
                            
                            
                            DataRowCounter--;
                            /*if(DataRowCounter==Data.getRowCount()) {
                                DataRowCounter--;
                            }*/
                        }
                        else {
                            if(!rsData.isLast()) {
                                rsData.previous();
                            }
                        }
                        
                        Done=true;
                        break;
                    }
                    
                    if(UseCustomDataProvider) {
                        LastGroupValue=Data.Rows(DataRowCounter).getValue(FieldName);
                    }
                    else {
                        //LastGroupValue=rsData.getObject(FieldName).toString();
                        LastGroupValue=readFieldValue(rsData, FieldName,"");
                    }
                }
                
                if(!Done) {
                    FillBand(objBand);
                    
                    //Move to next record
                    if(UseCustomDataProvider) {
                        DataRowCounter++;
                    }
                    else {
                        rsData.next();
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private String getVariableValue(String VariableName) {
        TVariable objVariable;
        String Value="";
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.BuiltInVariable&&VariableName.equals("PAGE_NO")) {
                    EvaluateVariable(VariableName);
                }
                
                if(objVariable.VariableName.trim().toLowerCase().equals(VariableName.trim().toLowerCase())) {
                    
                    Value=objVariable.VariableValue;
                }
            }
            
        }
        catch(Exception e) {
        }
        
        return Value;
        
    }
    
    
    private String getParameterValue(String ParameterName) {
        TParameter objParam;
        String Value="";
        
        try {
            for(int i=1;i<=objReport.colParameters.size();i++) {
                
                if(objReport.colParameters.containsKey(ParameterName)) {
                    Value=(String)objReport.colParameters.get(ParameterName);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return Value;
        
    }
    
    private void EvaluateVariable(String VariableName) {
        String VariableValue="";
        
        try {
            
            TVariable objVar=getVariable(VariableName);
            
            String VarValue=EvaluateExpression(objVar.Expression);
            
            if(objVar.Function.toLowerCase().equals("sum")) {
                
                String ExistingValue=getVariableValue(VariableName);
                
                double dExistingValue=objReport.ConvertToDouble(ExistingValue);
                double dVarValue=objReport.ConvertToDouble(VarValue);
                double newValue=TGLOBAL.round(dExistingValue+dVarValue,2);
                
                VarValue=Double.toString(newValue);
            }
            
            setVariableValue(objVar.VariableName, VarValue);
            
            if(objVar.BuiltInVariable) {
                
                if(objVar.VariableName.trim().toLowerCase().equals("date")) {
                    VariableValue=TGLOBAL.getCurrentDate();
                    setVariableValue("DATE", VariableValue);
                }
                
                if(objVar.VariableName.trim().toLowerCase().equals("page_no")) {
                    VariableValue=Integer.toString(RunningPage);
                    setVariableValue("PAGE_NO", VariableValue);
                }
                
                if(objVar.VariableName.trim().toLowerCase().equals("total_pages")) {
                    VariableValue=Integer.toString(RunningPage);
                    setVariableValue("TOTAL_PAGES", VariableValue);
                }
                
            }
            
        }
        catch(Exception e) {
            System.out.println("Error while evaluating Variable "+VariableName);
            e.printStackTrace();
        }
        
    }
    
    private TVariable getVariable(String VariableName) {
        TVariable objVariable=new TVariable();
        
        try {
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.VariableName.trim().toLowerCase().equals(VariableName.trim().toLowerCase())) {
                    return objVariable;
                }
            }
            
            return objVariable;
        }
        catch(Exception e) {
            return objVariable;
        }
        
    }
    
    private void setVariableValue(String VariableName,String Value) {
        TVariable objVariable;
        
        try {
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.VariableName.trim().toLowerCase().equals(VariableName.trim().toLowerCase())) {
                    
                    objVariable.VariableValue=Value;
                    objReport.colVariables.put(Integer.toString(i),objVariable);
                }
            }
            
        }
        catch(Exception e) {
        }
    }
    
    private void EvalDetailLevelVariables() {
        
        TVariable objVariable;
        
        try {
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.EvaluationTime==TReport.EvaluateNow) {
                    EvaluateVariable(objVariable.VariableName);
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void EvalGroupLevelVariables(String GroupName) {
        
        TVariable objVariable;
        
        try {
            
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.EvaluationTime==TReport.EvaluateOnGroup&&objVariable.EvaluationGroup.toLowerCase().equals(GroupName.toLowerCase())) {
                    EvaluateVariable(objVariable.VariableName);
                    
                    String VariableValue=getVariableValue(objVariable.VariableName);
                }
            }
            
            
            
            for(int p=1;p<=PrintPages.size();p++) {
                TReportPage objPageL=(TReportPage)PrintPages.get(Integer.toString(p));
                
                for(int l=1;l<=objPageL.PrintLines.size();l++) {
                    TReportLine objLine=(TReportLine)objPageL.PrintLines.get(Integer.toString(l));
                    
                    Iterator Keys=objLine.colElements.keySet().iterator();
                    
                    while(Keys.hasNext()) {
                        String key=(String)Keys.next();
                        
                        TElement objElement=(TElement)objLine.colElements.get(key);
                        
                        if(objElement.EvaluationPending&&objElement.EvaluationTime==TReport.EvaluateOnGroup&&objElement.EvaluationGroup.trim().toLowerCase().equals(GroupName.trim().toLowerCase())) {
                            String VariableValue=getVariableValue(objElement.VariableName);
                            objElement.Content=VariableValue;
                            objElement.EvaluationPending=false;
                            
                            //Update Report Page
                            objLine.colElements.put(key,objElement);
                            objPageL.PrintLines.put(Integer.toString(l),objLine);
                            PrintPages.put(Integer.toString(p),objPageL);
                        }
                    }
                    
                }
            }
            
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void EvalPageLevelVariables() {
        
        TVariable objVariable;
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                
                
                if(objVariable.EvaluationTime==TReport.EvaluateOnPage||objVariable.BuiltInVariable) {
                    EvaluateVariable(objVariable.VariableName);
                    
                    String VariableValue=getVariableValue(objVariable.VariableName);
                }
                
                
            }
            
            
            for(int l=1;l<=objPage.PrintLines.size();l++) {
                TReportLine objLine=(TReportLine)objPage.PrintLines.get(Integer.toString(l));
                
                Iterator Keys=objLine.colElements.keySet().iterator();
                
                while(Keys.hasNext()) {
                    String key=(String)Keys.next();
                    
                    TElement objElement=(TElement)objLine.colElements.get(key);
                    
                    if(objElement.EvaluationPending&&objElement.EvaluationTime==TReport.EvaluateOnPage) {
                        String VariableValue=getVariableValue(objElement.VariableName);
                        objElement.Content=VariableValue;
                        objElement.EvaluationPending=false;
                        
                        //Update Report Page
                        objLine.colElements.put(key,objElement);
                        objPage.PrintLines.put(Integer.toString(l),objLine);
                    }
                }
                
            }
            
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void FillDetailBands() {
        try {
            
            
            if(UseCustomDataProvider) {
                
                //==============================================================================//
                while(DataRowCounter<=Data.getRowCount() ) {
                    
                    if(DataRowCounter>Data.getRowCount()) {
                        return;
                    }
                    
                    for(int i=1;i<=objReport.colBands.size();i++) {
                        
                        TBand objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                        
                        
                        if(objBand.BandType==TBand.GroupHeader) {
                            //Push the Running Group to stack
                            RunningGroups.put(Integer.toString(RunningGroups.size()+1) ,objBand);
                            
                            FillBand(objBand); //Fill Group Header Band
                        }
                        
                        if(objBand.BandType==TBand.Detail) {
                            //Fill up detail band
                            FillDetailBand(objBand);
                        }
                        
                        
                        if(objBand.BandType==TBand.GroupFooter) {
                            boolean MoveNext=false;
                            
                            
                            //***** Evaluate Variables Here : Group Level *******//
                            //Group Level Variables Evaluation
                            EvalGroupLevelVariables(objBand.BandName);
                            // **************************************************//
                            
                            TBand RunningBand=(TBand)RunningGroups.get(Integer.toString(RunningGroups.size()));
                            
                            //Pop the Running Group from the stack
                            RunningGroups.remove(Integer.toString(RunningGroups.size()));
                            
                            FillBand(objBand); //Fill Group Detail Band
                            
                            ResetGroupLevelVariables(objBand.BandName);
                            
                            //==========================================================================//
                            //Start the Group on new Page if defined
                            if(RunningBand.StartGroupOnNewPage) {
                                
                                TBand objFooterBand=getBand(TBand.PageFooter);
                                int EndRow=objReport.objReportPage.PageHeight-objFooterBand.BandHeight;
                                
                                if(RunningLine<EndRow) {
                                    int RemainingLines=EndRow-RunningLine;
                                    
                                    for(int rlines=1;rlines<=RemainingLines;rlines++) {
                                        
                                        TReportLine objReportLine;
                                        objReportLine=new TReportLine();
                                        
                                        objPage.PrintLines.put(Integer.toString(objPage.PrintLines.size()+1), objReportLine);
                                        
                                        RunningLine++;
                                    }
                                }
                                
                                if(RunningLine==EndRow) {
                                    DoNotPrintBuffer=true;
                                    AdvancePage();
                                    DoNotPrintBuffer=false;
                                }
                                
                            }
                            //==========================================================================//
                            
                            
                            DataRowCounter++;
                        }
                        
                        
                        
                    }
                    
                }
                //==============================================================================//
                
            }
            else {
                
                //==============================================================================//
                while(!rsData.isAfterLast()) {
                    for(int i=1;i<=objReport.colBands.size();i++) {
                        
                        TBand objBand=(TBand)objReport.colBands.get(Integer.toString(i));
                        
                        if(objBand.BandType==TBand.GroupHeader) {
                            
                            //Push the Running Group to stack
                            RunningGroups.put(Integer.toString(RunningGroups.size()+1) ,objBand);
                            
                            FillBand(objBand); //Fill Group Header Band
                            
                        }
                        
                        if(objBand.BandType==TBand.Detail) {
                            //Fill up detail band
                            FillDetailBand(objBand);
                        }
                        
                        
                        if(objBand.BandType==TBand.GroupFooter) {
                            boolean MoveNext=false;
                            
                            if(rsData.isAfterLast()) {
                                rsData.last();
                                MoveNext=true;
                            }
                            
                            //***** Evaluate Variables Here : Group Level *******//
                            //Group Level Variables Evaluation
                            EvalGroupLevelVariables(objBand.BandName);
                            // **************************************************//
                            
                            TBand RunningBand=(TBand)RunningGroups.get(Integer.toString(RunningGroups.size()));
                            
                            //Pop the Running Group from the stack
                            RunningGroups.remove(Integer.toString(RunningGroups.size()));
                            
                            FillBand(objBand); //Fill Group Detail Band
                            
                            ResetGroupLevelVariables(objBand.BandName);
                            
                            //==========================================================================//
                            //Start the Group on new Page if defined
                            if(RunningBand.StartGroupOnNewPage) {
                                
                                TBand objFooterBand=getBand(TBand.PageFooter);
                                int EndRow=objReport.objReportPage.PageHeight-objFooterBand.BandHeight;
                                
                                if(RunningLine<EndRow) {
                                    int RemainingLines=EndRow-RunningLine;
                                    
                                    for(int rlines=1;rlines<=RemainingLines;rlines++) {
                                        
                                        TReportLine objReportLine;
                                        objReportLine=new TReportLine();
                                        
                                        objPage.PrintLines.put(Integer.toString(objPage.PrintLines.size()+1), objReportLine);
                                        
                                        RunningLine++;
                                    }
                                }
                                
                                if(RunningLine==EndRow) {
                                    DoNotPrintBuffer=true;
                                    AdvancePage();
                                    DoNotPrintBuffer=false;
                                }
                                
                            }
                            //==========================================================================//
                            
                            if(!rsData.isAfterLast()) {
                                rsData.next();
                            }
                        } ////
                        
                    }
                    
                }
                //==============================================================================//
                
            }
            
            
        }
        catch(Exception e) {
            
        }
    }
    
    private HashMap wrapText(String text, int len) {
        // return empty array for null text
        if (text == null)
            return new HashMap();
        
        // return text if len is zero or less
        if (len <= 0)
            return new HashMap();
        
        // return text if less than length
        if (text.length() <= len)
            return new HashMap();
        
        char [] chars = text.toCharArray();
        HashMap lines = new HashMap();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();
        
        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);
            
            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    //lines.add(line.toString());
                    lines.put(Integer.toString(lines.size()+1),line.toString());
                    line.delete(0, line.length());
                }
                
                line.append(word);
                word.delete(0, word.length());
            }
        }
        
        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.put(Integer.toString(lines.size()+1),line.toString());
                //lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        
        // handle extra line
        if (line.length() > 0) {
            //lines.add(line.toString());
            lines.put(Integer.toString(lines.size()+1),line.toString());
        }
        
        return lines;
    }
    
    private void ResetGroupLevelVariables(String GroupName) {
        
        TVariable objVariable;
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.ResetOn==TReport.EvaluateOnGroup&&objVariable.ResetGroupName.toLowerCase().equals(GroupName.toLowerCase())) {
                    
                    setVariableValue(objVariable.VariableName,"");
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void ResetDetailLevelVariables() {
        
        TVariable objVariable;
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.ResetOn==TReport.EvaluateNow) {
                    setVariableValue(objVariable.VariableName,"");
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private void ResetPageLevelVariables() {
        
        TVariable objVariable;
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.ResetOn==TReport.EvaluateOnPage) {
                    setVariableValue(objVariable.VariableName,"");
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    private String EvaluateTernaryOperator(String pExpression) {
        try {
            
            //IIF($F{}='',x,y)
            
            
            String returnValue="";
            
            String Expression=pExpression.trim();
            
            if(Expression.equals("$V{RUNNING_BALANCE}<0,'Cr','Dr'")) {
                boolean halt=true;
            }
            String Condition="";
            String LeftOperand="";
            String RightOperand="";
            String Operator="";
            String Argument1="";
            String Argument2="";
            
            Expression=Expression.substring(4,Expression.length()-1);
            
            String[] Parts=Expression.split(",");
            
            Condition=Parts[0];
            Argument1=Parts[1];
            Argument2=Parts[2];
            
            //Check the Operator
            if(Condition.indexOf("==")!=-1) //Numeric Comparison Operator Used
            {
                
                String[] ConditionTokens=Condition.split("==");
                
                LeftOperand=ConditionTokens[0];
                
                if(LeftOperand.startsWith("$")) {
                }
                else {
                    LeftOperand=LeftOperand.trim().substring(1,LeftOperand.trim().length()-1);
                }
                
                RightOperand=ConditionTokens[1];
                
                if(RightOperand.startsWith("$")) {
                }
                else {
                    RightOperand=RightOperand.trim().substring(1,RightOperand.trim().length()-1);
                }
                
                Operator="==";
                
            }
            
            
            if(Condition.indexOf("=")!=-1) //String Comparison Operator Used
            {
                String[] ConditionTokens=Condition.split("=");
                
                LeftOperand=ConditionTokens[0];
                
                if(LeftOperand.startsWith("$")) {
                }
                else {
                    LeftOperand=LeftOperand.trim().substring(1,LeftOperand.trim().length()-1);
                }
                
                RightOperand=ConditionTokens[1];
                
                if(RightOperand.startsWith("$")) {
                }
                else {
                    RightOperand=RightOperand.trim().substring(1,RightOperand.trim().length()-1);
                }
                
                Operator="=";
            }
            
            
            
            if(Condition.indexOf("<")!=-1) //Numeric Comparison Operator Used
            {
                
                String[] ConditionTokens=Condition.split("<");
                
                LeftOperand=ConditionTokens[0];
                
                if(LeftOperand.startsWith("$")) {
                }
                else {
                    LeftOperand=LeftOperand.trim().substring(1,LeftOperand.trim().length()-1);
                }
                
                RightOperand=ConditionTokens[1];
                
                if(RightOperand.startsWith("$")) {
                }
                else {}
                
                Operator="<";
                
            }
            
            //ADD BY MRUGESH START
            if(Condition.indexOf(">")!=-1) //Numeric Comparison Operator Used
            {
                
                String[] ConditionTokens=Condition.split(">");
                
                LeftOperand=ConditionTokens[0];
                
                if(LeftOperand.startsWith("$")) {
                }
                else {
                    LeftOperand=LeftOperand.trim().substring(1,LeftOperand.trim().length()-1);
                }
                
                RightOperand=ConditionTokens[1];
                
                if(RightOperand.startsWith("$")) {
                }
                else {}
                
                Operator=">";
                
            }
            //ADD BY MRUGESH START END
            
            if(Operator.equals("=")) {
                String Value1=EvaluateExpression(LeftOperand);
                String Value2=EvaluateExpression(RightOperand);
                
                if(Value1.equals(Value2)) {
                    returnValue=Argument1;
                }
                else {
                    returnValue=Argument2;
                }
                
            }
            
            
            if(Operator.equals("==")) {
                String Value1=EvaluateExpression(LeftOperand);
                String Value2=EvaluateExpression(RightOperand);
                
                if(UtilFunctions.CDbl(Value1)==UtilFunctions.CDbl(Value2)) {
                    returnValue=Argument1;
                }
                else {
                    returnValue=Argument2;
                }
                
            }
            
            
            if(Operator.equals("<")) {
                String Value1=EvaluateExpression(LeftOperand);
                String Value2=EvaluateExpression(RightOperand);
                
                if(UtilFunctions.CDbl(Value1)<UtilFunctions.CDbl(Value2)) {
                    returnValue=Argument1;
                }
                else {
                    returnValue=Argument2;
                }
                
            }
            
            //ADD BY MRUGESH START
            if(Operator.equals(">")) {
                String Value1=EvaluateExpression(LeftOperand);
                String Value2=EvaluateExpression(RightOperand);
                
                if(UtilFunctions.CDbl(Value1)>UtilFunctions.CDbl(Value2)) {
                    returnValue=Argument1;
                }
                else {
                    returnValue=Argument2;
                }
                
            }
            //ADD BY MRUGESH END
            return returnValue;
            
        }
        catch(Exception e) {
            return "";
        }
    }
    
    private String EvaluateExpression(String Expression) {
        try {
            
            if(Expression.equals("")) {
                
            }
            
            //Replace Fields with Values
            String strExpr=Expression;
            String ExprValue="";
            
            
            if(Expression.trim().startsWith("IIF")) {
                strExpr=EvaluateTernaryOperator(Expression);
            }
            
            boolean Done=false;
            
            while(!Done) {
                int StartPos=strExpr.indexOf("$F{");
                
                if(StartPos!=-1) {
                    int EndPos=strExpr.indexOf("}",StartPos);
                    String Field=strExpr.substring(StartPos,EndPos+1);
                    String FieldName=Field.substring(3,Field.length()-1);
                    String FieldValue=getDataFieldValue(FieldName);
                    strExpr=strExpr.replace(Field, FieldValue);
                    
                    if(strExpr.indexOf("$F{")==-1) {
                        //No value found for this field
                        Done=true;
                    }
                }
                else {
                    Done=true;
                    //No $F{ found now
                }
            }
            
            Done=false;
            
            while(!Done) {
                int StartPos=strExpr.indexOf("$V{");
                
                if(StartPos!=-1) {
                    int EndPos=strExpr.indexOf("}",StartPos);
                    String Field=strExpr.substring(StartPos,EndPos+1);
                    String FieldName=Field.substring(3,Field.length()-1);
                    //EvaluateVariable(FieldName); !!Exceptional Commented Line. To be tested
                    String FieldValue=getVariableValue(FieldName);
                    strExpr=strExpr.replace(Field, FieldValue);
                    
                    if(strExpr.indexOf("$V{")==-1) {
                        //No value found for this field
                        Done=true;
                    }
                    
                }
                else {
                    Done=true;
                    //No $F{ found now
                }
            }
            
            
            Done=false;
            
            while(!Done) {
                int StartPos=strExpr.indexOf("$P{");
                
                if(StartPos!=-1) {
                    int EndPos=strExpr.indexOf("}",StartPos);
                    String Field=strExpr.substring(StartPos,EndPos+1);
                    String FieldName=Field.substring(3,Field.length()-1);
                    //EvaluateVariable(FieldName); !!Exceptional Commented Line. To be tested
                    String FieldValue=getParameterValue(FieldName);
                    strExpr=strExpr.replace(Field, FieldValue);
                    
                    if(strExpr.indexOf("$P{")==-1) {
                        //No value found for this field
                        Done=true;
                    }
                    
                }
                else {
                    Done=true;
                    //No $P{ found now
                }
            }
            
            myParser.parseExpression(strExpr);
            Object result=myParser.getValueAsObject();
            
            if(result!=null) {
                
                double resultValue=objReport.ConvertToDouble(result.toString());
                resultValue=TGLOBAL.round(resultValue,2);
                ExprValue=Double.toString(resultValue);
            }
            else {
                ExprValue=strExpr;
            }
            
            return ExprValue;
        }
        catch(Exception e) {
            e.printStackTrace();
            return "";
        }
        
        
    }
    
    private String getDataFieldValue(String FieldName) {
        String FieldValue="";
        
        try {
            
            if(UseCustomDataProvider) {
                FieldValue=Data.Rows(DataRowCounter).getValue(FieldName);
            }
            else {
                //FieldValue=rsData.getObject(FieldName).toString();
                FieldValue=readFieldValue(rsData, FieldName, "");
            }
        }
        catch(Exception e) {
            FieldValue="";
        }
        
        return FieldValue;
        
    }
    
    
    private void EvalReportLevelVariables() {
        
        TVariable objVariable;
        
        try {
            
            for(int i=1;i<=objReport.colVariables.size();i++) {
                objVariable=(TVariable)objReport.colVariables.get(Integer.toString(i));
                
                if(objVariable.EvaluationTime==TReport.EvaluateOnReport) {
                    EvaluateVariable(objVariable.VariableName);
                    
                    String VariableValue=getVariableValue(objVariable.VariableName);
                    
                }
            }
            
            for(int p=1;p<=PrintPages.size();p++) {
                TReportPage objPageL=(TReportPage)PrintPages.get(Integer.toString(p));
                
                for(int l=1;l<=objPageL.PrintLines.size();l++) {
                    TReportLine objLine=(TReportLine)objPageL.PrintLines.get(Integer.toString(l));
                    
                    Iterator Keys=objLine.colElements.keySet().iterator();
                    
                    while(Keys.hasNext()) {
                        String key=(String)Keys.next();
                        
                        TElement objElement=(TElement)objLine.colElements.get(key);
                        
                        if(objElement.EvaluationPending) {
                            String VariableValue=getVariableValue(objElement.VariableName);
                            objElement.Content=VariableValue;
                            objElement.EvaluationPending=false;
                            
                            //Update Report Page
                            objLine.colElements.put(key,objElement);
                            objPageL.PrintLines.put(Integer.toString(l),objLine);
                            PrintPages.put(Integer.toString(p),objPageL);
                        }
                    }
                    
                }
            }
            
            
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    
    private String UpdateReportQuery(TReport pObjReport) {
        try {
            
            //Replace Fields with Values
            String strExpr=pObjReport.ReportQuery;
            String ExprValue="";
            
            boolean Done=false;
            
            while(!Done) {
                int StartPos=strExpr.indexOf("$P{");
                
                if(StartPos!=-1) {
                    int EndPos=strExpr.indexOf("}",StartPos);
                    String Field=strExpr.substring(StartPos,EndPos+1);
                    String FieldName=Field.substring(3,Field.length()-1);
                    String FieldValue=getParameterValue(FieldName);
                    strExpr=strExpr.replace(Field, FieldValue);
                }
                else {
                    Done=true;
                    //No $P{ found now
                }
            }
            
            objReport.ReportQuery=strExpr;
            
            return strExpr;
        }
        catch(Exception e) {
            e.printStackTrace();
            return "";
        }
        
        
    }
    
    
    private String UpdateExpression(String strSQL) {
        try {
            
            //Replace Fields with Values
            String strExpr=strSQL;
            String ExprValue="";
            
            boolean Done=false;
            
            while(!Done) {
                int StartPos=strExpr.indexOf("$F{");
                
                if(StartPos!=-1) {
                    int EndPos=strExpr.indexOf("}",StartPos);
                    String Field=strExpr.substring(StartPos,EndPos+1);
                    String FieldName=Field.substring(3,Field.length()-1);
                    String FieldValue=getDataFieldValue(FieldName);
                    strExpr=strExpr.replace(Field, FieldValue);
                }
                else {
                    Done=true;
                    //No $F{ found now
                }
            }
            
            return strExpr;
        }
        catch(Exception e) {
            e.printStackTrace();
            return "";
        }
        
    }
    
    private String readFieldValue(ResultSet rsData,String FieldName,String defaultValue) {
        String StringValue="";
        
        try {
            StringValue=rsData.getObject(FieldName).toString();
        }
        catch(Exception e) {
            
        }
        
        return StringValue;
        
    }
    
    private String getQueryResult(String strSQL) {
        String strValue="";
        
        try {
            Statement stTmp=objConn.createStatement();
            ResultSet rsTmp=stTmp.executeQuery(strSQL);
            
            //System.out.println(strSQL);
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strValue=rsTmp.getString(1);
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return strValue;
    }
    
}
