/*
 * Scheduler.java
 *
 * Created on June 21, 2006, 4:08 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import java.io.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Utils.*;

public class Scheduler {
    
    public int Hour=0;
    public int Min=0;
    public String AMPM="PM";
    public long Period=0;
    public String FileName="";
    
    /** Creates a new instance of Scheduler */
    public Scheduler() {
        
    }
    
    
    public static void main(String[] args) {
        
        HashMap tasks=new HashMap();
        boolean Done=false;
        String readLine="";
        
        try {
            
            // HH MM AM Period   FileName
            // XX XX XX XXXXXXXX X.....
            // 12 45 78 01234567 9
            
            String FileName="/script/taskschedule.txt";
            BufferedReader  aFile=new BufferedReader(new FileReader(new File(FileName)));
            
            while(!Done) {
                try {
                    readLine=aFile.readLine();
                    
                    
                    Scheduler objSch=new Scheduler();
                    objSch.Hour=Integer.parseInt(readLine.substring(0,2));
                    objSch.Min=Integer.parseInt(readLine.substring(3,5));
                    objSch.AMPM=readLine.substring(6,8);
                    objSch.Period=Long.parseLong(readLine.substring(9,17));
                    objSch.FileName=readLine.substring(18);
                    
                    
                    tasks.put(Integer.toString(tasks.size()+1), objSch);
                }
                catch(Exception f) {
                    Done=true;
                }
            }
            
            aFile.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        
        date.set(
        Calendar.DAY_OF_WEEK,
        EITLERPGLOBAL.getDayOfWeek(EITLERPGLOBAL.getCurrentDay(),EITLERPGLOBAL.getCurrentMonth(),EITLERPGLOBAL.getCurrentYear())
        );
        
        
        for(int i=1;i<=tasks.size();i++) {
            Scheduler objSch=(Scheduler)tasks.get(Integer.toString(i));
            
            date.set(Calendar.HOUR, objSch.Hour);
            date.set(Calendar.MINUTE, objSch.Min);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            
            if(objSch.AMPM.equals("AM")) {
                date.set(Calendar.AM_PM,Calendar.AM);
            }
            else {
                date.set(Calendar.AM_PM,Calendar.PM);
            }
                  
            
            // Schedule to run every Sunday in midnight
            timer.schedule(new ScheduleJob(objSch.FileName),date.getTime(),objSch.Period);
        }
        
        
        
        
    }
}
