/*
 * clsSalesInvoiceGSTRImport.java
 *
 * Created on May 15, 2008, 4:03 PM
 */
package EITLERP.GSTR;

/**
 *
 * @author root
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Finance.*;
//import EITLERP.Sales.*;

public class clsSalesInvoiceGSTRImport {

    String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();
    
    /**
     * Creates a new instance of clsSalesInvoiceImport
     */
    public clsSalesInvoiceGSTRImport() {

    }

    public static void main(String[] args) {
        //0 - Invoice Type
        //1 - FileName

        // original start 1
        
        if(args.length<2) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt, 3 - Filter, 4 - Trouser Length, 5 - Blanket)  \n2. Line sequential file name ");
            return;
        }
        
        String Type=args[0];
        String FileName=args[1];
        //int FinYearFrom=Integer.parseInt(args[2]);
        
        // original end 1
//        // For Testing start 2
//        String Type = "2";
//        String FileName = "/root/Desktop/gstr1.FLT";
//        int FinYearFrom = 2017;
//        //boolean PostSJ=true;
//
//        // For Testing end 2
        /*EITLERPGLOBAL.FinYearFrom=FinYearFrom;
         EITLERPGLOBAL.FinYearTo=FinYearFrom+1;*/
        clsSalesInvoiceGSTRImport objImport = new clsSalesInvoiceGSTRImport();

        if (Type.equals("1")) {
            objImport.ImportInvoicesSuiting(FileName, Type);
        }
        if (Type.equals("2")) {
            objImport.ImportInvoicesFelt(FileName, Type);
        }
        if (Type.equals("3")) {
            objImport.ImportInvoicesFilter(FileName, Type);
        }
        if (Type.equals("4")) {
            objImport.ImportInvoicesTL(FileName, Type);
        }
        if (Type.equals("5")) {
            objImport.ImportInvoicesBlanket(FileName, Type);
        }
    }

    public void ImportInvoicesSuiting(String invoiceFile, String inputType) {

        boolean Done = false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Sutting/Inv";

        try {

            String dbURL = "jdbc:mysql://200.0.0.227:3306/TEMP_DATABASE";

            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2) + ".log";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM D_SAL_GSTR_INVOICE LIMIT 1");

            clsLogFile log = new clsLogFile();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(invoiceFile)));

            Done = false;

            while (!Done) {

                try {

                    String FileRecord = aFile.readLine();

                    String CompanyID = "2";
                    String InputType = inputType;
                    String whCode = "STG";
                    String invoiceType = "Regular";

                    String PartyCode = FileRecord.substring(0, 6);
                    String GstinNo = FileRecord.substring(6, 21);
                    String InvoiceNo = FileRecord.substring(21, 30);

                    String InvDate = FileRecord.substring(30, 38);
                    String InvoiceDate = InvDate.substring(4, 8) + "-" + InvDate.substring(2, 4) + "-" + InvDate.substring(0, 2);

                    double InvoiceVal = UtilFunctions.CDbl(FileRecord.substring(38, 48)) / 100;
                    String ItemDesc = FileRecord.substring(48, 78);
                    String HSNCode = FileRecord.substring(78, 86);
                    double TaxableVal = UtilFunctions.CDbl(FileRecord.substring(86, 96)) / 100;
                    double IGSTPer = UtilFunctions.CDbl(FileRecord.substring(96, 100)) / 100;
                    double IGSTVal = UtilFunctions.CDbl(FileRecord.substring(100, 110)) / 100;
                    double CGSTPer = UtilFunctions.CDbl(FileRecord.substring(110, 114)) / 100;
                    double CGSTVal = UtilFunctions.CDbl(FileRecord.substring(114, 124)) / 100;
                    double SGSTPer = UtilFunctions.CDbl(FileRecord.substring(124, 128)) / 100;
                    double SGSTVal = UtilFunctions.CDbl(FileRecord.substring(128, 138)) / 100;
                    String PlaceOfSupply = FileRecord.substring(138, 158);
                    String StateCode = PlaceOfSupply.substring(18, 20);
                    String StateName = PlaceOfSupply.substring(0, 17);
                    String SPIN = FileRecord.substring(158, 164);
                    //String RevChrg = FileRecord.substring(164,10);
                    String RevChrg = "N";
                    String POS = PlaceOfSupply(StateCode);

                    String str = "SELECT * FROM D_SAL_GSTR_INVOICE "
                            + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                    if (!data.IsRecordExist(str, dbURL)) {
                        System.out.println("Importing GSTR1 Invoice Data for : " + InvoiceNo);

                        rsInvoice.moveToInsertRow();
                        rsInvoice.updateString("COMPANY_ID", CompanyID.trim());
                        rsInvoice.updateString("INPUT_TYPE", InputType.trim());
                        rsInvoice.updateString("WH_CODE", whCode.trim());
                        rsInvoice.updateString("INVOICE_TYPE", invoiceType.trim());

                        rsInvoice.updateString("PARTY_CODE", PartyCode.trim());
                        rsInvoice.updateString("GSTIN_NO", GstinNo.trim());
                        rsInvoice.updateString("INVOICE_NO", InvoiceNo.trim());
                        rsInvoice.updateString("INVOICE_DATE", InvoiceDate.trim());
                        rsInvoice.updateDouble("INVOICE_VALUE", InvoiceVal);
                        rsInvoice.updateString("ITEM_DESC", ItemDesc.trim());
                        rsInvoice.updateString("HSN_CODE", HSNCode.trim());
                        rsInvoice.updateDouble("TAXABLE_VALUE", TaxableVal);
                        rsInvoice.updateDouble("IGST_PER", IGSTPer);
                        rsInvoice.updateDouble("IGST_AMT", IGSTVal);
                        rsInvoice.updateDouble("CGST_PER", CGSTPer);
                        rsInvoice.updateDouble("CGST_AMT", CGSTVal);
                        rsInvoice.updateDouble("SGST_PER", SGSTPer);
                        rsInvoice.updateDouble("SGST_AMT", SGSTVal);
                        rsInvoice.updateString("STATE_CODE", StateCode.trim());
                        rsInvoice.updateString("STATE_NAME", StateName.trim());
                        rsInvoice.updateString("PLACE_OF_SUPPLY", POS.trim());
                        rsInvoice.updateInt("RATE", 5);
                        rsInvoice.updateString("REV_CHRG", RevChrg.trim());
                        rsInvoice.updateString("STATE_PIN_CODE", SPIN.trim());
                        rsInvoice.updateString("E_COMM_GSTIN_NO", "");
                        rsInvoice.updateBoolean("APPROVED", true);
                        rsInvoice.updateBoolean("CANCELLED", false);

                        rsInvoice.insertRow();

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ")) {
                            data.Execute("UPDATE TEMP_DATABASE.D_SAL_GSTR_INVOICE G,DINESHMILLS.D_SAL_INVOICE_HEADER H SET G.APPROVED=H.APPROVED,G.CANCELLED=H.CANCELLED WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                        }
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done = true;
                }
            }
            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public void ImportInvoicesFelt(String invoiceFile, String inputType) {

        boolean Done = false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Felt/Inv";

        try {

            String dbURL = "jdbc:mysql://200.0.0.227:3306/TEMP_DATABASE";

            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2) + ".log";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM D_SAL_GSTR_INVOICE LIMIT 1");

            clsLogFile log = new clsLogFile();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(invoiceFile)));

            Done = false;

            while (!Done) {

                try {

                    String FileRecord = aFile.readLine();

                    String CompanyID = "2";
                    String InputType = inputType;
                    String whCode = "FLT";
                    String invoiceType = "Regular";

                    String PartyCode = FileRecord.substring(0, 6);
                    String GstinNo = FileRecord.substring(6, 21);
                    String InvoiceNo = FileRecord.substring(21, 30);

                    String InvDate = FileRecord.substring(30, 38);
                    String InvoiceDate = InvDate.substring(4, 8) + "-" + InvDate.substring(2, 4) + "-" + InvDate.substring(0, 2);

                    double InvoiceVal = UtilFunctions.CDbl(FileRecord.substring(38, 48)) / 100;
                    String ItemDesc = FileRecord.substring(48, 78);
                    String HSNCode = FileRecord.substring(78, 86);
                    double TaxableVal = UtilFunctions.CDbl(FileRecord.substring(86, 96)) / 100;
                    double IGSTPer = UtilFunctions.CDbl(FileRecord.substring(96, 100)) / 100;
                    double IGSTVal = UtilFunctions.CDbl(FileRecord.substring(100, 110)) / 100;
                    double CGSTPer = UtilFunctions.CDbl(FileRecord.substring(110, 114)) / 100;
                    double CGSTVal = UtilFunctions.CDbl(FileRecord.substring(114, 124)) / 100;
                    double SGSTPer = UtilFunctions.CDbl(FileRecord.substring(124, 128)) / 100;
                    double SGSTVal = UtilFunctions.CDbl(FileRecord.substring(128, 138)) / 100;
                    String PlaceOfSupply = FileRecord.substring(138, 158);
                    String StateCode = PlaceOfSupply.substring(18, 20);
                    String StateName = PlaceOfSupply.substring(0, 17);
                    String SPIN = FileRecord.substring(158, 164);
                    //String RevChrg = FileRecord.substring(164,10);
                    String RevChrg = "N";
                    String POS = PlaceOfSupply(StateCode);

                    String str = "SELECT * FROM D_SAL_GSTR_INVOICE "
                            + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                    if (!data.IsRecordExist(str, dbURL)) {
                        System.out.println("Importing GSTR1 Invoice Data for : " + InvoiceNo);

                        rsInvoice.moveToInsertRow();
                        rsInvoice.updateString("COMPANY_ID", CompanyID.trim());
                        rsInvoice.updateString("INPUT_TYPE", InputType.trim());
                        rsInvoice.updateString("WH_CODE", whCode.trim());
                        rsInvoice.updateString("INVOICE_TYPE", invoiceType.trim());

                        rsInvoice.updateString("PARTY_CODE", PartyCode.trim());
                        rsInvoice.updateString("GSTIN_NO", GstinNo.trim());
                        rsInvoice.updateString("INVOICE_NO", InvoiceNo.trim());
                        rsInvoice.updateString("INVOICE_DATE", InvoiceDate.trim());
                        rsInvoice.updateDouble("INVOICE_VALUE", InvoiceVal);
                        rsInvoice.updateString("ITEM_DESC", ItemDesc.trim());
                        rsInvoice.updateString("HSN_CODE", HSNCode.trim());
                        rsInvoice.updateDouble("TAXABLE_VALUE", TaxableVal);
                        rsInvoice.updateDouble("IGST_PER", IGSTPer);
                        rsInvoice.updateDouble("IGST_AMT", IGSTVal);
                        rsInvoice.updateDouble("CGST_PER", CGSTPer);
                        rsInvoice.updateDouble("CGST_AMT", CGSTVal);
                        rsInvoice.updateDouble("SGST_PER", SGSTPer);
                        rsInvoice.updateDouble("SGST_AMT", SGSTVal);
                        rsInvoice.updateString("STATE_CODE", StateCode.trim());
                        rsInvoice.updateString("STATE_NAME", StateName.trim());
                        rsInvoice.updateString("PLACE_OF_SUPPLY", POS.trim());
                        rsInvoice.updateInt("RATE", 12);
                        rsInvoice.updateString("REV_CHRG", RevChrg.trim());
                        rsInvoice.updateString("STATE_PIN_CODE", SPIN.trim());
                        rsInvoice.updateString("E_COMM_GSTIN_NO", "");
                        rsInvoice.updateBoolean("APPROVED", true);
                        rsInvoice.updateBoolean("CANCELLED", false);

                        rsInvoice.insertRow();

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ")) {
                            data.Execute("UPDATE TEMP_DATABASE.D_SAL_GSTR_INVOICE G,DINESHMILLS.D_SAL_INVOICE_HEADER H SET G.APPROVED=H.APPROVED,G.CANCELLED=H.CANCELLED WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                        }
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done = true;
                }
            }
            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public void ImportInvoicesFilter(String invoiceFile, String inputType) {

        boolean Done = false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Filter/Inv";

        try {

            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2) + ".log";

            String dbURL = "jdbc:mysql://200.0.0.227:3306/TEMP_DATABASE";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM D_SAL_GSTR_INVOICE LIMIT 1");

            clsLogFile log = new clsLogFile();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(invoiceFile)));

            Done = false;

            while (!Done) {

                try {

                    String FileRecord = aFile.readLine();

                    String CompanyID = "2";
                    String InputType = inputType;
                    String whCode = "FF";
                    String invoiceType = "Regular";

                    String PartyCode = FileRecord.substring(0, 6);
                    String GstinNo = FileRecord.substring(6, 21);
                    String InvoiceNo = FileRecord.substring(21, 30);

                    String InvDate = FileRecord.substring(30, 38);
                    String InvoiceDate = InvDate.substring(4, 8) + "-" + InvDate.substring(2, 4) + "-" + InvDate.substring(0, 2);

                    double InvoiceVal = UtilFunctions.CDbl(FileRecord.substring(38, 48)) / 100;
                    String ItemDesc = FileRecord.substring(48, 78);
                    String HSNCode = FileRecord.substring(78, 86);
                    double TaxableVal = UtilFunctions.CDbl(FileRecord.substring(86, 96)) / 100;
                    double IGSTPer = UtilFunctions.CDbl(FileRecord.substring(96, 100)) / 100;
                    double IGSTVal = UtilFunctions.CDbl(FileRecord.substring(100, 110)) / 100;
                    double CGSTPer = UtilFunctions.CDbl(FileRecord.substring(110, 114)) / 100;
                    double CGSTVal = UtilFunctions.CDbl(FileRecord.substring(114, 124)) / 100;
                    double SGSTPer = UtilFunctions.CDbl(FileRecord.substring(124, 128)) / 100;
                    double SGSTVal = UtilFunctions.CDbl(FileRecord.substring(128, 138)) / 100;
                    String PlaceOfSupply = FileRecord.substring(138, 158);
                    String StateCode = PlaceOfSupply.substring(18, 20);
                    String StateName = PlaceOfSupply.substring(0, 17);
                    String SPIN = FileRecord.substring(158, 164);
                    //String RevChrg = FileRecord.substring(164,10);
                    String RevChrg = "N";
                    String POS = PlaceOfSupply(StateCode);

                    String str = "SELECT * FROM D_SAL_GSTR_INVOICE "
                            + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                    if (!data.IsRecordExist(str, dbURL)) {
                        System.out.println("Importing GSTR1 Invoice Data for : " + InvoiceNo);

                        rsInvoice.moveToInsertRow();
                        rsInvoice.updateString("COMPANY_ID", CompanyID.trim());
                        rsInvoice.updateString("INPUT_TYPE", InputType.trim());
                        rsInvoice.updateString("WH_CODE", whCode.trim());
                        rsInvoice.updateString("INVOICE_TYPE", invoiceType.trim());

                        rsInvoice.updateString("PARTY_CODE", PartyCode.trim());
                        rsInvoice.updateString("GSTIN_NO", GstinNo.trim());
                        rsInvoice.updateString("INVOICE_NO", InvoiceNo.trim());
                        rsInvoice.updateString("INVOICE_DATE", InvoiceDate.trim());
                        rsInvoice.updateDouble("INVOICE_VALUE", InvoiceVal);
                        rsInvoice.updateString("ITEM_DESC", ItemDesc.trim());
                        rsInvoice.updateString("HSN_CODE", HSNCode.trim());
                        rsInvoice.updateDouble("TAXABLE_VALUE", TaxableVal);
                        rsInvoice.updateDouble("IGST_PER", IGSTPer);
                        rsInvoice.updateDouble("IGST_AMT", IGSTVal);
                        rsInvoice.updateDouble("CGST_PER", CGSTPer);
                        rsInvoice.updateDouble("CGST_AMT", CGSTVal);
                        rsInvoice.updateDouble("SGST_PER", SGSTPer);
                        rsInvoice.updateDouble("SGST_AMT", SGSTVal);
                        rsInvoice.updateString("STATE_CODE", StateCode.trim());
                        rsInvoice.updateString("STATE_NAME", StateName.trim());
                        rsInvoice.updateString("PLACE_OF_SUPPLY", POS.trim());
                        rsInvoice.updateInt("RATE", 0);
                        rsInvoice.updateString("REV_CHRG", RevChrg.trim());
                        rsInvoice.updateString("STATE_PIN_CODE", SPIN.trim());
                        rsInvoice.updateString("E_COMM_GSTIN_NO", "");
                        rsInvoice.updateBoolean("APPROVED", true);
                        rsInvoice.updateBoolean("CANCELLED", false);

                        rsInvoice.insertRow();

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ")) {
                            data.Execute("UPDATE TEMP_DATABASE.D_SAL_GSTR_INVOICE G,DINESHMILLS.D_SAL_INVOICE_HEADER H SET G.APPROVED=H.APPROVED,G.CANCELLED=H.CANCELLED WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                        }
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done = true;
                }
            }
            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public void ImportInvoicesTL(String invoiceFile, String inputType) {

        boolean Done = false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/TrouserLength/Inv";

        try {

            String dbURL = "jdbc:mysql://200.0.0.227:3306/TEMP_DATABASE";

            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2) + ".log";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM D_SAL_GSTR_INVOICE LIMIT 1");

            clsLogFile log = new clsLogFile();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(invoiceFile)));

            Done = false;

            while (!Done) {

                try {

                    String FileRecord = aFile.readLine();

                    String CompanyID = "2";
                    String InputType = inputType;
                    String whCode = "TL";
                    String invoiceType = "Regular";

                    String PartyCode = FileRecord.substring(0, 6);
                    String GstinNo = FileRecord.substring(6, 21);
                    String InvoiceNo = FileRecord.substring(21, 30);

                    String InvDate = FileRecord.substring(30, 38);
                    String InvoiceDate = InvDate.substring(4, 8) + "-" + InvDate.substring(2, 4) + "-" + InvDate.substring(0, 2);

                    double InvoiceVal = UtilFunctions.CDbl(FileRecord.substring(38, 48)) / 100;
                    String ItemDesc = FileRecord.substring(48, 78);
                    String HSNCode = FileRecord.substring(78, 86);
                    double TaxableVal = UtilFunctions.CDbl(FileRecord.substring(86, 96)) / 100;
                    double IGSTPer = UtilFunctions.CDbl(FileRecord.substring(96, 100)) / 100;
                    double IGSTVal = UtilFunctions.CDbl(FileRecord.substring(100, 110)) / 100;
                    double CGSTPer = UtilFunctions.CDbl(FileRecord.substring(110, 114)) / 100;
                    double CGSTVal = UtilFunctions.CDbl(FileRecord.substring(114, 124)) / 100;
                    double SGSTPer = UtilFunctions.CDbl(FileRecord.substring(124, 128)) / 100;
                    double SGSTVal = UtilFunctions.CDbl(FileRecord.substring(128, 138)) / 100;
                    String PlaceOfSupply = FileRecord.substring(138, 158);
                    String StateCode = PlaceOfSupply.substring(18, 20);
                    String StateName = PlaceOfSupply.substring(0, 17);
                    String SPIN = FileRecord.substring(158, 164);
                    //String RevChrg = FileRecord.substring(164,10);
                    String RevChrg = "N";
                    String POS = PlaceOfSupply(StateCode);

                    String str = "SELECT * FROM D_SAL_GSTR_INVOICE "
                            + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                    if (!data.IsRecordExist(str, dbURL)) {
                        System.out.println("Importing GSTR1 Invoice Data for : " + InvoiceNo);

                        rsInvoice.moveToInsertRow();
                        rsInvoice.updateString("COMPANY_ID", CompanyID.trim());
                        rsInvoice.updateString("INPUT_TYPE", InputType.trim());
                        rsInvoice.updateString("WH_CODE", whCode.trim());
                        rsInvoice.updateString("INVOICE_TYPE", invoiceType.trim());

                        rsInvoice.updateString("PARTY_CODE", PartyCode.trim());
                        rsInvoice.updateString("GSTIN_NO", GstinNo.trim());
                        rsInvoice.updateString("INVOICE_NO", InvoiceNo.trim());
                        rsInvoice.updateString("INVOICE_DATE", InvoiceDate.trim());
                        rsInvoice.updateDouble("INVOICE_VALUE", InvoiceVal);
                        rsInvoice.updateString("ITEM_DESC", ItemDesc.trim());
                        rsInvoice.updateString("HSN_CODE", HSNCode.trim());
                        rsInvoice.updateDouble("TAXABLE_VALUE", TaxableVal);
                        rsInvoice.updateDouble("IGST_PER", IGSTPer);
                        rsInvoice.updateDouble("IGST_AMT", IGSTVal);
                        rsInvoice.updateDouble("CGST_PER", CGSTPer);
                        rsInvoice.updateDouble("CGST_AMT", CGSTVal);
                        rsInvoice.updateDouble("SGST_PER", SGSTPer);
                        rsInvoice.updateDouble("SGST_AMT", SGSTVal);
                        rsInvoice.updateString("STATE_CODE", StateCode.trim());
                        rsInvoice.updateString("STATE_NAME", StateName.trim());
                        rsInvoice.updateString("PLACE_OF_SUPPLY", POS.trim());
                        rsInvoice.updateInt("RATE", 5);
                        rsInvoice.updateString("REV_CHRG", RevChrg.trim());
                        rsInvoice.updateString("STATE_PIN_CODE", SPIN.trim());
                        rsInvoice.updateString("E_COMM_GSTIN_NO", "");
                        rsInvoice.updateBoolean("APPROVED", true);
                        rsInvoice.updateBoolean("CANCELLED", false);

                        rsInvoice.insertRow();

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ")) {
                            data.Execute("UPDATE TEMP_DATABASE.D_SAL_GSTR_INVOICE G,DINESHMILLS.D_SAL_INVOICE_HEADER H SET G.APPROVED=H.APPROVED,G.CANCELLED=H.CANCELLED WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                        }
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done = true;
                }
            }
            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public void ImportInvoicesBlanket(String invoiceFile, String inputType) {

        boolean Done = false;
        String Msg = "";
        String LogFileName = "/data/InvoiceLog/Blanket/Inv";

        try {

            String dbURL = "jdbc:mysql://200.0.0.227:3306/TEMP_DATABASE";

            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2) + ".log";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM D_SAL_GSTR_INVOICE LIMIT 1");

            clsLogFile log = new clsLogFile();

            BufferedReader aFile = new BufferedReader(new FileReader(new File(invoiceFile)));

            Done = false;

            while (!Done) {

                try {

                    String FileRecord = aFile.readLine();

                    String CompanyID = "2";
                    String InputType = inputType;
                    String whCode = "BKT";
                    String invoiceType = "Regular";

                    String PartyCode = FileRecord.substring(0, 6);
                    String GstinNo = FileRecord.substring(6, 21);
                    String InvoiceNo = FileRecord.substring(21, 30);

                    String InvDate = FileRecord.substring(30, 38);
                    String InvoiceDate = InvDate.substring(4, 8) + "-" + InvDate.substring(2, 4) + "-" + InvDate.substring(0, 2);

                    double InvoiceVal = UtilFunctions.CDbl(FileRecord.substring(38, 48)) / 100;
                    String ItemDesc = FileRecord.substring(48, 78);
                    String HSNCode = FileRecord.substring(78, 86);
                    double TaxableVal = UtilFunctions.CDbl(FileRecord.substring(86, 96)) / 100;
                    double IGSTPer = UtilFunctions.CDbl(FileRecord.substring(96, 100)) / 100;
                    double IGSTVal = UtilFunctions.CDbl(FileRecord.substring(100, 110)) / 100;
                    double CGSTPer = UtilFunctions.CDbl(FileRecord.substring(110, 114)) / 100;
                    double CGSTVal = UtilFunctions.CDbl(FileRecord.substring(114, 124)) / 100;
                    double SGSTPer = UtilFunctions.CDbl(FileRecord.substring(124, 128)) / 100;
                    double SGSTVal = UtilFunctions.CDbl(FileRecord.substring(128, 138)) / 100;
                    String PlaceOfSupply = FileRecord.substring(138, 158);
                    String StateCode = PlaceOfSupply.substring(18, 20);
                    String StateName = PlaceOfSupply.substring(0, 17);
                    String SPIN = FileRecord.substring(158, 164);
                    //String RevChrg = FileRecord.substring(164,10);
                    String RevChrg = "N";
                    String POS = PlaceOfSupply(StateCode);

                    String str = "SELECT * FROM D_SAL_GSTR_INVOICE "
                            + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                    if (!data.IsRecordExist(str, dbURL)) {
                        System.out.println("Importing GSTR1 Invoice Data for : " + InvoiceNo);

                        rsInvoice.moveToInsertRow();
                        rsInvoice.updateString("COMPANY_ID", CompanyID.trim());
                        rsInvoice.updateString("INPUT_TYPE", InputType.trim());
                        rsInvoice.updateString("WH_CODE", whCode.trim());
                        rsInvoice.updateString("INVOICE_TYPE", invoiceType.trim());

                        rsInvoice.updateString("PARTY_CODE", PartyCode.trim());
                        rsInvoice.updateString("GSTIN_NO", GstinNo.trim());
                        rsInvoice.updateString("INVOICE_NO", InvoiceNo.trim());
                        rsInvoice.updateString("INVOICE_DATE", InvoiceDate.trim());
                        rsInvoice.updateDouble("INVOICE_VALUE", InvoiceVal);
                        rsInvoice.updateString("ITEM_DESC", ItemDesc.trim());
                        rsInvoice.updateString("HSN_CODE", HSNCode.trim());
                        rsInvoice.updateDouble("TAXABLE_VALUE", TaxableVal);
                        rsInvoice.updateDouble("IGST_PER", IGSTPer);
                        rsInvoice.updateDouble("IGST_AMT", IGSTVal);
                        rsInvoice.updateDouble("CGST_PER", CGSTPer);
                        rsInvoice.updateDouble("CGST_AMT", CGSTVal);
                        rsInvoice.updateDouble("SGST_PER", SGSTPer);
                        rsInvoice.updateDouble("SGST_AMT", SGSTVal);
                        rsInvoice.updateString("STATE_CODE", StateCode.trim());
                        rsInvoice.updateString("STATE_NAME", StateName.trim());
                        rsInvoice.updateString("PLACE_OF_SUPPLY", POS.trim());
                        rsInvoice.updateInt("RATE", 5);
                        rsInvoice.updateString("REV_CHRG", RevChrg.trim());
                        rsInvoice.updateString("STATE_PIN_CODE", SPIN.trim());
                        rsInvoice.updateString("E_COMM_GSTIN_NO", "");
                        rsInvoice.updateBoolean("APPROVED", true);
                        rsInvoice.updateBoolean("CANCELLED", false);

                        rsInvoice.insertRow();

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ")) {
                            data.Execute("UPDATE TEMP_DATABASE.D_SAL_GSTR_INVOICE G,DINESHMILLS.D_SAL_INVOICE_HEADER H SET G.APPROVED=H.APPROVED,G.CANCELLED=H.CANCELLED WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                        }
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                    Msg = c.getMessage();
                    log.logToFile(LogFileName, Msg, 2);
                    Done = true;
                }
            }
            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public String PlaceOfSupply(String stateCd) {

        String POS = "";
        try {

            if (stateCd.equalsIgnoreCase("01")) {
                POS = "01-Jammu & Kashmir";
            }

            if (stateCd.equalsIgnoreCase("02")) {
                POS = "02-Himachal Pradesh";
            }

            if (stateCd.equalsIgnoreCase("03")) {
                POS = "03-Punjab";
            }

            if (stateCd.equalsIgnoreCase("04")) {
                POS = "04-Chandigarh";
            }

            if (stateCd.equalsIgnoreCase("05")) {
                POS = "05-Uttarakhand";
            }

            if (stateCd.equalsIgnoreCase("06")) {
                POS = "06-Haryana";
            }

            if (stateCd.equalsIgnoreCase("07")) {
                POS = "07-Delhi";
            }

            if (stateCd.equalsIgnoreCase("08")) {
                POS = "08-Rajasthan";
            }

            if (stateCd.equalsIgnoreCase("09")) {
                POS = "09-Uttar Pradesh";
            }

            if (stateCd.equalsIgnoreCase("10")) {
                POS = "10-Bihar";
            }

            if (stateCd.equalsIgnoreCase("11")) {
                POS = "11-Sikkim";
            }

            if (stateCd.equalsIgnoreCase("12")) {
                POS = "12-Arunachal Pradesh";
            }

            if (stateCd.equalsIgnoreCase("13")) {
                POS = "13-Nagaland";
            }

            if (stateCd.equalsIgnoreCase("14")) {
                POS = "14-Manipur";
            }

            if (stateCd.equalsIgnoreCase("15")) {
                POS = "15-Mizoram";
            }

            if (stateCd.equalsIgnoreCase("16")) {
                POS = "16-Tripura";
            }

            if (stateCd.equalsIgnoreCase("17")) {
                POS = "17-Meghalaya";
            }

            if (stateCd.equalsIgnoreCase("18")) {
                POS = "18-Assam";
            }

            if (stateCd.equalsIgnoreCase("19")) {
                POS = "19-West Bengal";
            }

            if (stateCd.equalsIgnoreCase("20")) {
                POS = "20-Jharkhand";
            }

            if (stateCd.equalsIgnoreCase("21")) {
                POS = "21-Odisha";
            }

            if (stateCd.equalsIgnoreCase("22")) {
                POS = "22-Chhattisgarh";
            }

            if (stateCd.equalsIgnoreCase("23")) {
                POS = "23-Madhya Pradesh";
            }

            if (stateCd.equalsIgnoreCase("24")) {
                POS = "24-Gujarat";
            }

            if (stateCd.equalsIgnoreCase("25")) {
                POS = "25-Daman & Diu";
            }

            if (stateCd.equalsIgnoreCase("26")) {
                POS = "26-Dadra & Nagar Haveli";
            }

            if (stateCd.equalsIgnoreCase("27")) {
                POS = "27-Maharashtra";
            }

            if (stateCd.equalsIgnoreCase("28")) {
                POS = "";
            }

            if (stateCd.equalsIgnoreCase("29")) {
                POS = "29-Karnataka";
            }

            if (stateCd.equalsIgnoreCase("30")) {
                POS = "30-Goa";
            }

            if (stateCd.equalsIgnoreCase("31")) {
                POS = "31-Lakshdweep";
            }

            if (stateCd.equalsIgnoreCase("32")) {
                POS = "32-Kerala";
            }

            if (stateCd.equalsIgnoreCase("33")) {
                POS = "33-Tamil Nadu";
            }

            if (stateCd.equalsIgnoreCase("34")) {
                POS = "34-Pondicherry";
            }

            if (stateCd.equalsIgnoreCase("35")) {
                POS = "35-Andaman & Nicobar Islands";
            }

            if (stateCd.equalsIgnoreCase("36")) {
                POS = "36-Telengana";
            }

            if (stateCd.equalsIgnoreCase("37")) {
                POS = "37-Andhra Pradesh";
            }

            if (stateCd.equalsIgnoreCase("98")) {
                POS = "98-Other Territory";
            }

            return POS;

        } catch (Exception ex) {
            return "";
        }
    }

}
