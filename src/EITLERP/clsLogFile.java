/*
 * clsLogFile.java
 *
 * Created on April 17, 2010, 11:54 AM
 */

/**
 *
 * @author  root
 */
package EITLERP;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.*;
import java.text.*;

public class clsLogFile {
    
    
    /** Creates a new instance of Log_File */
    public clsLogFile() {
    }
    public void logToFile(String FileNamePath,String Message,int Type) {
        
        try {
            Logger logger = Logger.getLogger("Invoice Log");
            FileHandler fh = new FileHandler(FileNamePath,(8196*8196),1,true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            
            if(Type==1) {
                //logger.info(Message);
            } else if(Type==2) {
                logger.severe(Message);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /*public void logToFileDate(String FileNamePath) {
        DateFormat dateFormatter;
        try {
            FileHandler fh = new FileHandler(FileNamePath,true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            dateFormatter = DateFormat.getDateInstance(DateFormat.FULL,new Locale("en_US"));
            logger.log(Level.INFO,dateFormatter.format(new Date()));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }*/
}
