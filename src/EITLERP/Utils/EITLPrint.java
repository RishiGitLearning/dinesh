/*
 * EITLPrint.java
 *
 * Created on July 14, 2004, 4:37 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;


public class EITLPrint {
    
    /** Creates a new instance of EITLPrint */
    public EITLPrint() {
        
    }
    
    
    public static void PrintTextFile(String pFile) {
        try {
            
            //Print Supported Types
            CheckFlavours();
            
            // Open the image file
            InputStream is = new BufferedInputStream(
            new FileInputStream(pFile));
            
            // Find the default service
            //DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
            //DocFlavor flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            
            // Create the print job
            DocPrintJob job = service.createPrintJob();
            
            Doc doc = new SimpleDoc(is,flavor,null);
            
            // Monitor print job events; for the implementation of PrintJobWatcher,
            // see e702 Determining When a Print Job Has Finished
            //PrintJobWatcher pjDone = new PrintJobWatcher(job);
            
            // Print it
            job.print(doc, null);
            
            // It is now safe to close the input stream
            is.close();
        } catch (PrintException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Printer Exception "+e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IO Exception "+e.getMessage());
        }
        
    }
    
    public static void PrintTextFile(String pFile,DocFlavor theFlavor) {
        try {
            
            //Print Supported Types
            CheckFlavours();
            
            // Open the image file
            InputStream is = new BufferedInputStream(
            new FileInputStream(pFile));
            

            //DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
            // Find the default service
            DocFlavor flavor = theFlavor;
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        
            // Create the print job
            DocPrintJob job = service.createPrintJob();
            
            Doc doc = new SimpleDoc(is,flavor,null);
            
            // Monitor print job events; for the implementation of PrintJobWatcher,
            // see e702 Determining When a Print Job Has Finished
            //PrintJobWatcher pjDone = new PrintJobWatcher(job);
            
            // Print it
            job.print(doc, null);
            
            // It is now safe to close the input stream
            is.close();
        } catch (PrintException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Printer Exception "+e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IO Exception "+e.getMessage());
        }
        
    }
    
    public static void CheckFlavours() {
        try {
            
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            DocFlavor[] flavors = service.getSupportedDocFlavors();
            for (int i = 0; i < flavors.length; i++) {
                System.out.println("Supported Flavors "+flavors[i].hostEncoding);
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public static void main(String[] args)
    {

        PrintRequestAttributeSet attributes=new HashPrintRequestAttributeSet();
        
        

    }
    
}
