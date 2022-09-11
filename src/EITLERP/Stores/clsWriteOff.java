/*
 * clsWriteOff.java
 *
 * Created on May 15, 2008, 4:03 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  root
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Stores.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import EITLERP.Finance.*;
import EITLERP.Sales.*;

public class clsWriteOff {
    
    /** Creates a new instance of clsSalesInvoiceImport */
    public clsWriteOff() {
        
    }
    
    
    public static void main(String[] args) {
        //0 - File name of the sales party master. in csv format, seperated by ~
        
        
        
        String FileName="/root/Desktop/WRITE_OFF/ANK.csv";
        //String MainCode=args[1];
        //String PartyMasterType=args[2];
        clsWriteOff cwo = new clsWriteOff();
        cwo.WriteOff(FileName);
    }
    
    
    private void WriteOff(final String FileName) {
        
        
        boolean Done=false;
        long Counter=0;
        long LineNo=0;
        long SrNo=0;
        try {
            
            String dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLSA";
            BufferedReader aFile=new BufferedReader(new FileReader(new File(FileName)));
            Counter = 0 ;
            //SrNo = data.getLongValueFromDB("SELECT MAX(SR_NO) FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO=0");
            String ItemID="";
            while(!Done) {
                
                try {
                    
                    String FileRecord=aFile.readLine().trim();
                    String[] ArrRecord=FileRecord.split("~");
                    
                    
                    ItemID=ArrRecord[0].trim();
                    double Qty=EITLERPGLOBAL.round(Double.parseDouble(ArrRecord[1]),3);
                    double Rate=EITLERPGLOBAL.round(Double.parseDouble(ArrRecord[2]),3);
                    //double OriginalValue=EITLERPGLOBAL.round(Double.parseDouble(ArrRecord[3]),3);
                    double Value=EITLERPGLOBAL.round(Double.parseDouble(ArrRecord[3]),3);
                    double OriginalValue = data.getDoubleValueFromDB("SELECT OPENING_VALUE FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO=4 AND ITEM_ID LIKE '"+ItemID+"%'", dbURL);
                    Counter++;
                    System.out.println("ItemID = " + ItemID + " Qty = " + Qty + " Rate = " + Rate + " Value = " + Value + " Original Value = "+ OriginalValue); //
                    String SQL = "";
                    if(data.IsRecordExist("SELECT ITEM_ID FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID LIKE '"+ItemID+"%' AND ENTRY_NO=4", dbURL)) {
                        SQL = "UPDATE D_COM_OPENING_STOCK_DETAIL SET OPENING_QTY="+Qty+",OPENING_RATE="+Rate+"," +
                        "OPENING_VALUE="+Value+",ORIGINAL_VALUE="+OriginalValue+",TMP_ISSUED_QTY=1 WHERE ENTRY_NO=4 AND ITEM_ID LIKE '"+ItemID+"%'";
                        //
                        data.Execute(SQL, dbURL);
                        //                        System.out.println("Found : Item ID = " + ItemID);
                    } else {
                        //                        SrNo++;
                        //                        SQL="INSERT INTO D_COM_OPENING_STOCK_DETAIL (COMPANY_ID,ENTRY_NO,SR_NO,ITEM_ID,OPENING_QTY,OPENING_RATE,OPENING_VALUE) VALUES ("+2+","+0+","+SrNo+",'"+ItemID+"',"+Qty+","+Rate+","+Value+")";
                        //                        data.Execute(SQL);
                        System.out.println("Not Found : Item ID = " + ItemID);
                    }
                    
                    //                    System.out.println(SQL);
                    
                }
                catch(Exception c){
                    System.out.println("ERROR : ItemID = " + ItemID);
                    c.printStackTrace();
                    Done=true;
                }
            }
            
            System.out.println("Completed .... ");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}



