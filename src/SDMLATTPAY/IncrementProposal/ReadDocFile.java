/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY.IncrementProposal;

import java.awt.Desktop;
import java.io.*;


/**
 *
 * @author Dharmendra
 */
public class ReadDocFile {

    public static void main(String[] args) {
        try {
            String filePath = "E:\\lic.pdf";
            Desktop dt = Desktop.getDesktop();
            dt.open(new File(filePath));

            //filePath = "E:\\Production.xls";
            //Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
