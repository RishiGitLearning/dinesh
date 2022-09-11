/*
 * ScheduleJob.java
 *
 * Created on June 21, 2006, 11:33 AM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import java.io.*;
import java.util.*;
import java.awt.*;
import EITLERP.*;


public class ScheduleJob extends TimerTask {
    
    public String ExFile="";
    
    /** Creates a new instance of ScheduleJob */
    public ScheduleJob() {
        
    }
    
    public ScheduleJob(String File) {
        ExFile=File;
    }
    
    
    
    public void run() {
        try {
            
            System.out.println("Starting scheduled process");
            
            String FileName="/script/schlog"+EITLERPGLOBAL.getCurrentDateDB()+EITLERPGLOBAL.getCurrentTime()+".log";
            System.out.println(FileName);
            
            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File(FileName)));
            aFile.write("Starting Process "+ExFile+" on "+EITLERPGLOBAL.getCurrentDate()+EITLERPGLOBAL.getCurrentTime());
            aFile.newLine();
            
            
            String line="",output="";
            boolean ProcessDone=false;
            for(int i=1;i<=10;i++) {
                
                Process p=java.lang.Runtime.getRuntime().exec(ExFile);
                
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                
                ProcessDone=false;
                while (!ProcessDone) {
                    line = input.readLine();
                    
                    if(line==null) {
                        
                    }
                    else {
                        output=line;
                    }
                    
                    try {
                        int exitValue=p.exitValue();
                        ProcessDone=true;
                    }
                    catch(Exception il){}
                }
                
                input.close();
                
                if(output.trim().equals("0")) //Normal Shutdown
                {
                    i=6;
                    aFile.write("Process Completed successfully. ");
                    aFile.newLine();
                    
                    System.out.println("Process Completed");
                }
                else {
                    aFile.write("Process did not completed successfully. Trying again ");
                    aFile.newLine();
                    
                    System.out.println("Process did not completed ...");
                }
            }
            
            
            aFile.close();
            
        }
        catch(Exception e) {
            
        }
    }
    
}
