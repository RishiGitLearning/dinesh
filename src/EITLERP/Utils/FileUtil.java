/*
 * FileUtil.java
 *
 * Created on December 1, 2010, 1:06 PM
 */

package EITLERP.Utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author  root
 */
public class FileUtil {
    
    /** Creates a new instance of FileUtil */
    public FileUtil() {
    }
    public static void main(String[] args) {
        FileUtil util = new FileUtil();
        util.removeLineFromFile("/var/log/squid/access.log", "200.0.0.105");
        util.removeLineFromFile("/var/log/squid/access.log", "200.0.0.227");
    }
    
    public void removeLineFromFile(String file, String lineToRemove) {
        
        try {
            
            File inFile = new File(file);
            
            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            
            String line = null;
            
            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                
                if (!contains(line.trim(),lineToRemove)) {
                    
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            
            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");
            
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean contains(String Line,String lineToRemove) {
        boolean found = false;
        StringBuffer strbuf = new StringBuffer(Line);
        if(strbuf.indexOf(lineToRemove) != -1) {
            return true;
        } else {
            return false;
        }
    }
}
