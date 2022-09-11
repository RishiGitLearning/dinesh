/*
 * clsDeleteFilesInDir.java
 *
 * Created on December 27, 2010, 11:59 AM
 */

package EITLERP.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author  root
 */
public class clsDeleteFilesInDir {
    
    /** Creates a new instance of clsDeleteFilesInDir */
    public clsDeleteFilesInDir() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        for(int Start=0;Start<3;Start++) {
            String strFolder = "";
            if(Start==0) {
                strFolder = "/data/InvoiceLog/Filter/";
            } else if(Start==1) {
                strFolder = "/data/InvoiceLog/Felt/";
            } else if(Start==2) {
                strFolder = "/data/InvoiceLog/Sutting/";
            }
            System.out.println("Deleting from " + strFolder);
            File fLogDir = new File(strFolder);

            if(fLogDir.isDirectory()) {
                File[] aFiles = fLogDir.listFiles();
                for (int i=0;i<aFiles.length;i++) {
                    String strName = aFiles[i].getName();
                    if(strName.endsWith(".log.lck") || strName.endsWith(".log")) {
                        System.out.println("do not delete : " + strName);
                    } else {
                        aFiles[i].delete();
                        System.out.println("delete : " + strName);
                    }
                }
            }
        }
        System.out.println("Finished...");
    }
}
