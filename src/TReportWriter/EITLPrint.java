/*
 * EITLPrint.java
 *
 * Created on July 14, 2004, 4:37 PM
 */

package TReportWriter;

/**
 *
 * @author  root
 */
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.DocPrintJob;
import javax.print.Doc;
import javax.print.SimpleDoc;
import javax.print.PrintException;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
//import javax.print.event.*;
import javax.swing.JOptionPane;
//import java.awt.*;
//import EITLERP.*;



public class EITLPrint {
    
    /** Creates a new instance of EITLPrint */
    public EITLPrint() {
        
    }
    
    public static String getDefaultPrinterName() {
        try {
            
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            
            return service.getName();
            
        } catch (Exception e) {
            return "";
        }
        
    }
    
    
    
    
    public static void PrintTextFile(String pFile) {
        try {
            //Print Supported Types
            
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
            
            // Open the image file
            InputStream is = new BufferedInputStream(
            new FileInputStream(pFile));
            
            
            //DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
            // Find the default service
            
            //Add flavor
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            
            // Create the print job
            DocPrintJob job = service.createPrintJob();
            
            Doc doc = new SimpleDoc(is,theFlavor,null);
            
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1));
            
            //aset.add(MediaSize.findMedia(10,15,MediaSize.INCH));
            
            // Monitor print job events; for the implementation of PrintJobWatcher,
            // see e702 Determining When a Print Job Has Finished
            //PrintJobWatcher pjDone = new PrintJobWatcher(job);
            
            // Print it
            job.print(doc, aset);
            
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
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static DocFlavor getSupportedFlavor() {
        try {
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            DocFlavor[] flavors = service.getSupportedDocFlavors();
            
            
            for (int i = 0; i < flavors.length; i++) {
                System.out.println(flavors[i].getRepresentationClassName());
                return flavors[i];
            }
            
            return DocFlavor.INPUT_STREAM.AUTOSENSE;
            
        } catch(Exception e) {
            return DocFlavor.INPUT_STREAM.AUTOSENSE;
        }
    }
    
    public static void main(String[] args) {
    }
}
