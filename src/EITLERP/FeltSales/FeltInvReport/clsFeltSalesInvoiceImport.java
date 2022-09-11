/*
 * clsSalesInvoiceImport.java
 *
 * Created on May 15, 2008, 4:03 PM
 */
package EITLERP.FeltSales.FeltInvReport;

/**
 *
 * @author root
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Stores.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import EITLERP.Finance.*;
import javax.swing.JOptionPane;
//import EITLERP.Sales.*;

public class clsFeltSalesInvoiceImport {

    String CurrentDate = EITLERPGLOBAL.getCurrentDateDB();

    /**
     * Creates a new instance of clsSalesInvoiceImport
     */
    public clsFeltSalesInvoiceImport() {

    }

    public static void main(String[] args) {
        //0 - Invoice Type
        //1 - FileName

        // original start 1
        if (args.length < 4) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt, 3 -Filter)  \n2. Line sequential file name \n3. Financial Year From \n4. Post SJ ? (Y or N)");
            return;
        }

        String Type = args[0];
        String FileName = args[1];
        int FinYearFrom = Integer.parseInt(args[2]);
        boolean PostSJ = false;

        if (args[3].equals("Y")) {
            PostSJ = true;
        }

        // original end 1
        // For Testing start 2
        /*String Type="1";
         String FileName="/root/Desktop/stg.STG";
         int FinYearFrom=2012;
         boolean PostSJ=true;*/
        // For Testing end 2
        /*EITLERPGLOBAL.FinYearFrom=FinYearFrom;
         EITLERPGLOBAL.FinYearTo=FinYearFrom+1;*/
        clsFeltSalesInvoiceImport objImport = new clsFeltSalesInvoiceImport();

//        if (Type.equals("2")) {
//            objImport.ImportInvoicesFelt(FileName, PostSJ);
//        }
    }

    public static void ImportInvoicesFelt1(boolean pPostSJ, String pPartyCode, String pBaleNo) {

        boolean Done = false;
        boolean canPostSJ = false;
        boolean AutoAdj = false;
//        String Msg = "";
//        String LogFileName = "/data/Inv";
        long Counter = 0;

        try {

            clsFeltSalesInvoice objInvoice = new clsFeltSalesInvoice();

            String dbURL = EITLERPGLOBAL.DatabaseURL;

//            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentDay()), "0", 2);
//            LogFileName = LogFileName + EITLERPGLOBAL.padLeftEx(Integer.toString(EITLERPGLOBAL.getCurrentMonth()), "0", 2);
//            LogFileName = LogFileName + Integer.toString(EITLERPGLOBAL.getCurrentYear()).substring(2); //+ ".log";
//            LogFileName = LogFileName + EITLERPGLOBAL.getCurrentTime() + ".log";

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER LIMIT 1");

            // ---- History Connection ------ //
            Statement stInvoiceHistory = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoiceHistory = stInvoiceHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H LIMIT 1"); // '1' for restricting all data retrieval

//            int Pointer=0;
            clsLogFile log = new clsLogFile();
//            BufferedReader aFile=new BufferedReader(new FileReader(new File(invoiceFile)));
//            Done=false;
            ResultSet rsInvHeader = null;
            //String invHeader = "SELECT * FROM PRODUCTION.FELT_SAL_COMPLETED_INV_HEADER_LIST WHERE INVOICE_DATE = '" + CurrentDate + "'";
            String invHeader = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=1 AND CHECK_POINT_REMARK='' AND BALE_NO = '" + pBaleNo + "' AND PARTY_CODE = '" + pPartyCode + "' ";//INVOICE_DATE = '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND 
            rsInvHeader = data.getResult(invHeader);
            rsInvHeader.first();
            if (rsInvHeader.getRow() > 0) {
                while (!rsInvHeader.isAfterLast()) {

                    try {

                        String gstrPos = "";

//                    String FileRecord=aFile.readLine();
//                    Pointer=0;
                        String InvoiceNo = "FE/" + rsInvHeader.getString("INVOICE_NO");

                        // Added by mrugesh for auto adjustment start
                        String InvNo = InvoiceNo;
                        String InvDate = rsInvHeader.getString("INVOICE_DATE").substring(0, 10);
                        String ChargeCode = rsInvHeader.getString("CHARGE_CODE");
                        String Party_code = rsInvHeader.getString("PARTY_CODE");;
                        String Filler = "";
                        double InvoiceAmount = Math.round(rsInvHeader.getFloat("INVOICE_AMT"));
                        canPostSJ = false;
                        AutoAdj = false;
                        String pBaleDate = rsInvHeader.getString("PACKING_DATE");;
                        // Added by mrugesh for auto adjustment end

//                    Pointer+=6; //MIIN-ACC-SRNO
                        String InvoiceDate = rsInvHeader.getString("INVOICE_DATE").substring(0, 10);
                        System.out.println("invdt : " + InvoiceDate);
                    //InvoiceDate = "20"+InvoiceDate.substring(4)+"-"+InvoiceDate.substring(2,4)+"-"+InvoiceDate.substring(0,2);

                    // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                    /*if(java.sql.Date.valueOf(InvoiceDate).after(java.sql.Date.valueOf(clsDepositMaster.deductDays(CurrentDate,2)))) {
                         continue;
                         }*/
                        // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                        String str = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER "
                                + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                        if (!data.IsRecordExist(str, dbURL)) {

                            //Party_code= FileRecord.substring(Pointer,Pointer+6);
                            System.out.println("Importing Invoice " + InvoiceNo);
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", 2);
                            rsInvoice.updateInt("INVOICE_TYPE", 2);
                            rsInvoice.updateString("INVOICE_NO", InvoiceNo);
                            rsInvoice.updateString("INVOICE_DATE", InvoiceDate);
                            rsInvoice.updateString("BALE_NO", rsInvHeader.getString("BALE_NO"));
                            rsInvoice.updateString("PARTY_CODE", Party_code);
                            rsInvoice.updateString("NO_OF_PIECES", rsInvHeader.getString("NO_OF_PIECES"));
                            rsInvoice.updateString("CHARGE_CODE", rsInvHeader.getString("CHARGE_CODE"));
                            rsInvoice.updateFloat("BAS_AMT", rsInvHeader.getFloat("BAS_AMT"));
                            rsInvoice.updateFloat("DISC_AMT", rsInvHeader.getFloat("DISC_AMT"));
                            rsInvoice.updateFloat("DISC_BAS_AMT", rsInvHeader.getFloat("DISC_BAS_AMT"));
                            rsInvoice.updateFloat("CHEM_TRT_CHG", rsInvHeader.getFloat("CHEM_TRT_CHG"));
                            rsInvoice.updateFloat("PIN_CHG", rsInvHeader.getFloat("PIN_CHG"));
                            rsInvoice.updateFloat("SPIRAL_CHG", rsInvHeader.getFloat("SPIRAL_CHG"));
                            rsInvoice.updateFloat("SEAM_CHG", rsInvHeader.getFloat("SEAM_CHG"));
                            //rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("PIN_CHG") + rsInvHeader.getFloat("SPIRAL_CHG")));
                            //rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG")));
                            rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") - rsInvHeader.getFloat("AOSD_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG")));
                            rsInvoice.updateFloat("EXCISE", rsInvHeader.getFloat("EXCISE"));
                            rsInvoice.updateFloat("INSURANCE_AMT", rsInvHeader.getFloat("INSURANCE_AMT"));
                            rsInvoice.updateFloat("BANK_CHARGES", Float.valueOf(0));
                            rsInvoice.updateFloat("CESS", Float.valueOf(0));
                            rsInvoice.updateFloat("GST_AMT", Float.valueOf(0));
                            rsInvoice.updateFloat("CST2", rsInvHeader.getFloat("CST2"));
                            rsInvoice.updateFloat("CST5", rsInvHeader.getFloat("CST5"));
                            rsInvoice.updateFloat("VAT1", rsInvHeader.getFloat("VAT1"));
                            rsInvoice.updateFloat("VAT4", rsInvHeader.getFloat("VAT4"));
                            rsInvoice.updateFloat("SD_AMT", rsInvHeader.getFloat("SD_AMT"));
                            rsInvoice.updateFloat("NET_AMT", rsInvHeader.getFloat("INVOICE_AMT"));
                            rsInvoice.updateFloat("INVOICE_AMT", Math.round(rsInvHeader.getFloat("INVOICE_AMT")));

                            rsInvoice.updateFloat("AOSD_PER", rsInvHeader.getFloat("AOSD_PER"));
                            rsInvoice.updateFloat("AOSD_AMT", rsInvHeader.getFloat("AOSD_AMT"));

                            NumWord num = new NumWord();
                            String rsInWord = num.convertNumToWord(Math.round(rsInvHeader.getFloat("INVOICE_AMT")));
                            rsInvoice.updateString("INVOICE_AMT_IN_WORD", rsInWord);

                            rsInvoice.updateInt("LOT_NO", rsInvHeader.getInt("LOT_NO"));
                            rsInvoice.updateInt("TRANSPORTER_CODE", rsInvHeader.getInt("TRANSPORTER_CODE"));
                            rsInvoice.updateString("PACKING_DATE", rsInvHeader.getString("PACKING_DATE"));
                            rsInvoice.updateString("GATEPASS_NO", rsInvHeader.getString("GATEPASS_NO"));
                            rsInvoice.updateString("TAX_INV_NO", rsInvHeader.getString("TAX_INV_NO"));
                            rsInvoice.updateString("RETAIL_INV_NO", rsInvHeader.getString("RETAIL_INV_NO"));
                            rsInvoice.updateString("FINYR", EITLERPGLOBAL.FinYearFrom + "-" + EITLERPGLOBAL.FinYearTo);

                            rsInvoice.updateDouble("INV_CRITICAL_LIMIT_AMT", rsInvHeader.getDouble("INV_CRITICAL_LIMIT_AMT"));
                            rsInvoice.updateDouble("CRITICAL_LIMIT_AMT", rsInvHeader.getDouble("CRITICAL_LIMIT_AMT"));

                            //rsInvoice.updateBoolean("APPROVED",true);
//                            rsInvoice.updateString("HSN_CODE", "5911");
                            rsInvoice.updateString("HSN_CODE", "59113290");
                            rsInvoice.updateFloat("IGST_PER", rsInvHeader.getFloat("IGST_PER"));
                            rsInvoice.updateFloat("IGST_AMT", rsInvHeader.getFloat("IGST_AMT"));
                            rsInvoice.updateFloat("CGST_PER", rsInvHeader.getFloat("CGST_PER"));
                            rsInvoice.updateFloat("CGST_AMT", rsInvHeader.getFloat("CGST_AMT"));
                            rsInvoice.updateFloat("SGST_PER", rsInvHeader.getFloat("SGST_PER"));
                            rsInvoice.updateFloat("SGST_AMT", rsInvHeader.getFloat("SGST_AMT"));
                            rsInvoice.updateFloat("GST_COMP_CESS_PER", rsInvHeader.getFloat("GST_COMP_CESS_PER"));
                            rsInvoice.updateFloat("GST_COMP_CESS_AMT", rsInvHeader.getFloat("GST_COMP_CESS_AMT"));
                            rsInvoice.updateString("ADV_DOC_NO", rsInvHeader.getString("ADV_DOC_NO"));
                            rsInvoice.updateString("VEHICLE_NO", rsInvHeader.getString("VEHICLE_NO"));
                            rsInvoice.updateFloat("ADV_RECEIVED_AMT", rsInvHeader.getFloat("ADV_RECEIVED_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_INV_AMT", rsInvHeader.getFloat("ADV_AGN_INV_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_IGST_AMT", rsInvHeader.getFloat("ADV_AGN_IGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_SGST_AMT", rsInvHeader.getFloat("ADV_AGN_SGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_CGST_AMT", rsInvHeader.getFloat("ADV_AGN_CGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_GST_COMP_CESS_AMT", rsInvHeader.getFloat("ADV_AGN_GST_COMP_CESS_AMT"));

//                                int id = 1746;
                            int id = 1677;
                            rsInvoice.updateInt("HIERARCHY_ID", id);

                            rsInvoice.updateBoolean("APPROVED", false);
//                                rsInvoice.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsInvoice.updateString("APPROVED_DATE", "0000-00-00");
                            rsInvoice.updateBoolean("CANCELLED", false);

                            rsInvoice.updateString("CREATED_BY", String.valueOf(EITLERPGLOBAL.gUserID));
                            rsInvoice.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                            rsInvoice.updateBoolean("CHANGED", true);
                            rsInvoice.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                            String DueDate = "0000-00-00";
                            if (ChargeCode.startsWith("02")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010") + 15, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("08")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010") + 6, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("01")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 30, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("04")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010"), "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("07")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("09")) {
                                DueDate = InvDate;
                            }

                            rsInvoice.updateString("DUE_DATE", DueDate);

                            rsInvoice.updateString("DOCUMENT_THROUGH", rsInvHeader.getString("DOCUMENT_THROUGH"));
                            rsInvoice.updateString("PRODUCT_CODE", rsInvHeader.getString("PRODUCT_CODE"));
                            rsInvoice.updateString("PRODUCT_DESC", rsInvHeader.getString("PRODUCT_DESC"));
                            rsInvoice.updateString("PIECE_NO", rsInvHeader.getString("PIECE_NO"));
                            rsInvoice.updateString("MACHINE_NO", rsInvHeader.getString("MACHINE_NO"));
                            rsInvoice.updateString("POSITION_NO", rsInvHeader.getString("POSITION_NO"));
                            rsInvoice.updateString("POSITION_DESC", rsInvHeader.getString("POSITION_DESC"));
                            rsInvoice.updateString("STYLE", rsInvHeader.getString("STYLE"));
                            rsInvoice.updateFloat("LENGTH", rsInvHeader.getFloat("LENGTH"));
                            rsInvoice.updateFloat("WIDTH", rsInvHeader.getFloat("WIDTH"));
                            rsInvoice.updateFloat("GSM", rsInvHeader.getFloat("GSM"));
                            rsInvoice.updateFloat("ACTUAL_WEIGHT", rsInvHeader.getFloat("ACTUAL_WEIGHT"));
                            rsInvoice.updateFloat("SQMTR", rsInvHeader.getFloat("SQMTR"));
                            rsInvoice.updateString("SYN_PER", rsInvHeader.getString("SYN_PER"));
                            rsInvoice.updateDouble("RATE", rsInvHeader.getDouble("RATE"));
                            rsInvoice.updateString("RATE_UNIT", rsInvHeader.getString("RATE_UNIT"));
                            rsInvoice.updateString("TRANSPORTER_NAME", rsInvHeader.getString("TRANSPORTER_NAME"));
                            rsInvoice.updateString("DESP_MODE", rsInvHeader.getString("DESP_MODE"));
                            rsInvoice.updateString("PARTY_NAME", rsInvHeader.getString("PARTY_NAME"));
                            rsInvoice.updateString("GSTIN_NO", rsInvHeader.getString("GSTIN_NO"));
                            rsInvoice.updateString("ADDRESS1", rsInvHeader.getString("ADDRESS1"));
                            rsInvoice.updateString("ADDRESS2", rsInvHeader.getString("ADDRESS2"));
                            rsInvoice.updateString("CITY_NAME", rsInvHeader.getString("CITY_NAME"));
                            rsInvoice.updateString("CITY_ID", rsInvHeader.getString("CITY_ID"));
                            rsInvoice.updateString("DISPATCH_STATION", rsInvHeader.getString("DISPATCH_STATION"));
                            String statecode = rsInvHeader.getString("GSTIN_NO").substring(0, 2);
                            String pos = data.getStringValueFromDB("SELECT STATE_NAME FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_GST_CODE='" + statecode + "'");
                            rsInvoice.updateString("PLACE_OF_SUPPLY", pos);
                            //rsInvoice.updateString("PLACE_OF_SUPPLY", rsInvHeader.getString("PLACE_OF_SUPPLY"));
                            rsInvoice.updateDouble("DISC_PER", rsInvHeader.getDouble("DISC_PER"));
                            rsInvoice.updateString("PINCODE", rsInvHeader.getString("PINCODE"));
                            rsInvoice.updateString("PARTY_CHARGE_CODE", rsInvHeader.getString("PARTY_CHARGE_CODE"));
                            rsInvoice.updateString("PARTY_BANK_NAME", rsInvHeader.getString("PARTY_BANK_NAME"));
                            rsInvoice.updateString("PARTY_BANK_ADDRESS1", rsInvHeader.getString("PARTY_BANK_ADDRESS1"));
                            rsInvoice.updateString("PARTY_BANK_ADDRESS2", rsInvHeader.getString("PARTY_BANK_ADDRESS2"));

                            rsInvoice.updateString("PO_NO", rsInvHeader.getString("PO_NO"));
                            rsInvoice.updateString("PO_DATE", rsInvHeader.getString("PO_DATE"));
                            rsInvoice.updateString("LC_NO", rsInvHeader.getString("LC_NO"));

                            rsInvoice.updateString("CREDIT_DAYS", rsInvHeader.getString("CREDIT_DAYS"));
                            rsInvoice.updateString("PAYMENT_TERMS", rsInvHeader.getString("PAYMENT_TERMS"));
                            
                            rsInvoice.updateDouble("SURCHARGE_PER", rsInvHeader.getDouble("SURCHARGE_PER"));
                            rsInvoice.updateDouble("SURCHARGE_RATE", rsInvHeader.getDouble("SURCHARGE_RATE"));
                            rsInvoice.updateDouble("GROSS_RATE", rsInvHeader.getDouble("GROSS_RATE"));

                            rsInvoice.updateString("MOBILE_NO", rsInvHeader.getString("MOBILE_NO"));
                            rsInvoice.updateString("DELIVERY_MODE", rsInvHeader.getString("DELIVERY_MODE"));
                            rsInvoice.updateString("MATERIAL_CODE", rsInvHeader.getString("MATERIAL_CODE"));
                            rsInvoice.updateFloat("TCS_PER", rsInvHeader.getFloat("TCS_PER"));
                            rsInvoice.updateDouble("TCS_AMT", rsInvHeader.getDouble("TCS_AMT"));

                            String hNo = "";
                            int hundiNo = 0;
                            if (ChargeCode.startsWith("04")) {
                                hNo = clsFirstFree.getNextFreeNo(2, 0, 226, true);
                                hundiNo = Integer.parseInt(hNo);
                                rsInvoice.updateInt("HUNDI_NO", hundiNo);
                            }

                            int trLetNo = 0;
                            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'")) {
                                trLetNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'");
                                rsInvoice.updateInt("TR_LET_NO", trLetNo);
                            } else {
                                trLetNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                                rsInvoice.updateInt("TR_LET_NO", trLetNo + 1);
                            }

                            //rsInvoice.updateFloat("INVOICE_TAXABLE_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG") + rsInvHeader.getFloat("INSURANCE_AMT")));
                            rsInvoice.updateFloat("INVOICE_TAXABLE_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") - rsInvHeader.getFloat("AOSD_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG") + rsInvHeader.getFloat("INSURANCE_AMT")));

                            gstrPos = clsPlaceOfSupply.PlaceOfSupply(statecode);

                            rsInvoice.insertRow();
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '2', 'INSERT : INVOICE HEADER TABLE FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
//                            Msg = "Felt Sales Header Data posted for Invoice No (" + InvNo + ") on date " + InvDate;
//                            log.logToFile(LogFileName, Msg, 2);

                            //DETAILS UPDATION
                            String invDetailUp = "INSERT INTO PRODUCTION.FELT_SAL_INVOICE_DETAIL SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE BALE_NO='" + rsInvHeader.getString("BALE_NO") + "' AND INVOICE_NO='" + rsInvHeader.getString("INVOICE_NO") + "'";
                            data.Execute(invDetailUp);
                            data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_DETAIL SET INVOICE_NO=CONCAT('FE/',INVOICE_NO) WHERE BALE_NO='" + rsInvHeader.getString("BALE_NO") + "' AND INVOICE_NO='" + rsInvHeader.getString("INVOICE_NO") + "'");
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '3', 'INSERT : INVOICE DETAIL TABLE FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

//                            Msg = "Felt Sales Detail Data posted for Invoice No (" + InvNo + ") on date " + InvDate;
//                            log.logToFile(LogFileName, Msg, 2);

                            String cSQL = "SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' ";

                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '4', 'START : CHECK CHARGE CODE OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
                            if (data.IsRecordExist(cSQL)) {
                                
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5', 'CHECK : CHARGE CODE OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
//                                ExternalImportSJ(true, ChargeCode, InvoiceAmount, Party_code, InvNo, InvDate);
                                if (ChargeCode.startsWith("09")) {
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.1', 'CHECKED : CHARGE CODE O9 OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                                    double availableAmount = clsAccount.get09AmountByParty("210010", Party_code, InvDate);

                                    System.out.println("09 AMOUNT : " + availableAmount);

                                    if (InvoiceAmount > availableAmount) {
                                        
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.2', 'CHECKED : O9 BALANCE LESS THAN INVOICE AMOUNT OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                        canPostSJ = false;
                                        AutoAdj = false;
                                        data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
                                        data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
//                                        Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " of party " + Party_code + " with Invoice Amount is (" + InvoiceAmount + ") and advance amount is (" + availableAmount + ") \n"
//                                                + "before invoice date, so invoice not imported.";
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice not Imported ");
                                    } else {
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.2', 'CHECKED : O9 BALANCE SUFFICIENT OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                        canPostSJ = true;
                                        AutoAdj = true;
//                                        Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice Imported ");
                                    }
                                    // Remove the comment for posting 09 prathmesh 03-10-2010 end
                                } else {
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.1', 'CHECKED : CHARGE CODE NOT 09 OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                    canPostSJ = true;
                                    AutoAdj = false;
//                                    Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println("Invoice Imported ");
                                }

                                if (pPostSJ && canPostSJ) {
                                    System.out.println("Posting SJ");
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '6', 'START : SJ POSTING OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                                    objInvoice = (clsFeltSalesInvoice) objInvoice.getObject(2, InvNo, InvDate, dbURL);

                                    if (objInvoice.PostSJTypeFelt(2, InvNo, InvDate, AutoAdj, pBaleNo, pBaleDate)) {                                        
//                                        Msg = "SJ has been posted for Invoice No (" + InvNo + ") on date " + InvDate;
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice " + InvNo + " Posted.");
                                    } else {
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '6', 'ERROR : SJ POSTING FAILED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
//                                        Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println(objInvoice.LastError);
                                        JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                        return;
                                    }
                                }

                                String cSQL1 = "";
                                if (ChargeCode.equals("09")) {
                                    cSQL1 = "SELECT * FROM ( SELECT INV.INVOICE_NO,INV.INVOICE_DATE,INV.INVOICE_AMT,COALESCE(AMT,0) AS AMT,INVOICE_AMT-COALESCE(AMT,0) AS FAMT FROM (SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' AND H.CHARGE_CODE =09) AS INV LEFT JOIN (SELECT D.INVOICE_NO,D.INVOICE_DATE,SUM(COALESCE(AMOUNT,0)) AS AMT FROM FINANCE.D_FIN_VOUCHER_DETAIL D ,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO = D.VOUCHER_NO AND INVOICE_NO = '" + InvNo + "' AND INVOICE_DATE = '" + InvDate + "' AND EFFECT ='C' AND SUB_ACCOUNT_CODE = '" + Party_code + "' AND SUBSTRING(H.VOUCHER_NO,1,2) !='SJ' GROUP BY D.INVOICE_NO,D.INVOICE_DATE ) AS RC ON RC.INVOICE_NO= INV.INVOICE_NO AND RC.INVOICE_DATE = INV.INVOICE_DATE ) SUB WHERE FAMT=0 ";
                                } else {
                                    cSQL1 = "SELECT INV.INVOICE_NO,INV.INVOICE_DATE,INV.INVOICE_AMT,COALESCE(AMT,0) AS AMT,INVOICE_AMT-COALESCE(AMT,0) AS FAMT FROM (SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' ) AS INV LEFT JOIN (SELECT D.INVOICE_NO,D.INVOICE_DATE,SUM(COALESCE(AMOUNT,0)) AS AMT FROM FINANCE.D_FIN_VOUCHER_DETAIL D ,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO = D.VOUCHER_NO AND INVOICE_NO = '" + InvNo + "' AND INVOICE_DATE = '" + InvDate + "' AND EFFECT ='D' AND SUB_ACCOUNT_CODE = '" + Party_code + "' AND SUBSTRING(H.VOUCHER_NO,1,2) ='SJ' GROUP BY D.INVOICE_NO,D.INVOICE_DATE ) AS RC ON RC.INVOICE_NO= INV.INVOICE_NO AND RC.INVOICE_DATE = INV.INVOICE_DATE ";
                                }
                                System.out.println("check SQL 1 for SJ: " + cSQL1);
                                
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7', 'CHECK : SJ AGAINEST INVOICE POSTING OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                
                                if (!data.IsRecordExist(cSQL1)) {
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7.1', 'ERROR : SJ AGAINEST INVOICE POSTING MISMATCHED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                    
                                    data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
                                    data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");

                                    String SJNo = data.getStringValueFromDB("SELECT DISTINCT VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE SUB_ACCOUNT_CODE='" + Party_code + "' AND INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)='SJ'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='" + SJNo + "'");

                                    data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET INVOICE_NO='', INVOICE_DATE='0000-00-00', INVOICE_AMOUNT=0, GRN_NO='', GRN_DATE='0000-00-00', MODULE_ID=0, REF_COMPANY_ID=0 WHERE INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)!='SJ'");
                                    data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='', INVOICE_DATE='0000-00-00', INVOICE_AMOUNT=0, GRN_NO='', GRN_DATE='0000-00-00', MODULE_ID=0, REF_COMPANY_ID=0 WHERE INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)!='SJ'");
                                    
                                    data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET INVOICE_FLG=0 WHERE PKG_BALE_NO='" + pBaleNo + "' AND PKG_BALE_DATE='" + pBaleDate + "' ");
                                    
//                                    Msg = "Invoice data not matched with Finance data for Invoice No (" + InvNo + ") on date " + InvDate;
//                                    log.logToFile(LogFileName, Msg, 2);

                                    JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                    return;
                                } else {
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7.1', 'CHECKED : SJ AGAINEST INVOICE POSTING MATCHED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                }
                                
                            } else {
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '4', 'ERROR : SJ POSTING FAILED FOR "+InvNo+" DATED "+InvDate+" DUE TO HEADER AND DETAIL DATA NOT FOUND ', 'SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = " + Party_code + " AND H.INVOICE_NO = " + InvNo + " AND H.INVOICE_DATE = " + InvDate + " ' ) ");
//                                Msg = "Felt Sales Header not matched with Detail for Invoice No (" + InvNo + ") on date " + InvDate;
//                                log.logToFile(LogFileName, Msg, 2);
                                JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                return;
                            }

                            //INSERT HIERARCHY INTO DOC DATA
                            String DocUpDt = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA SELECT 80,INVOICE_NO,CURDATE(),USER_ID,CASE WHEN CREATOR=1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'',NOW(),'0000-00-00 00:00:00',0,NOW()  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,PRODUCTION.FELT_SAL_INVOICE_HEADER B WHERE B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='"+InvoiceDate+"' AND B.HIERARCHY_ID =1677 AND A.HIERARCHY_ID = B.HIERARCHY_ID ";
                            data.Execute(DocUpDt);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '8', 'DONE : DOC DATA POSTED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
                            //UPDATION OF PIECE REGISTER
                            String pieceUpdate = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_SAL_INVOICE_DETAIL I SET PR_INVOICE_NO='" + InvoiceNo + "',PR_INVOICE_DATE='" + InvoiceDate + "',PR_INVOICE_PARTY='" + Party_code + "' WHERE P.PR_PIECE_NO=I.PIECE_NO AND I.INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(I.INVOICE_DATE,1,10)='" + InvoiceDate + "' ";
                            data.Execute(pieceUpdate);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '9', 'DONE : PIECE REGISTER UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            String strSQL = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_HEADER ";
                            strSQL += "(COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,PAYMENT_TERM_CODE,BALE_NO,TOTAL_GROSS_AMOUNT,TOTAL_NET_AMOUNT, ";
                            strSQL += "NET_AMOUNT,GROSS_WEIGHT,TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,COLUMN_1_AMT,COLUMN_1_CAPTION,COLUMN_3_AMT,COLUMN_3_CAPTION, ";
                            strSQL += "COLUMN_6_AMT,COLUMN_8_PER,COLUMN_8_AMT,COLUMN_8_CAPTION,COLUMN_9_AMT,COLUMN_9_CAPTION,COLUMN_10_CAPTION,COLUMN_11_AMT,COLUMN_12_AMT,COLUMN_13_AMT, ";
                            strSQL += "COLUMN_14_CAPTION,COLUMN_24_AMT,COLUMN_25_AMT,APPROVED,CANCELLED,CHANGED,VAT1,SD_AMT,TOT_INV_SD_AMT,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER, ";
                            strSQL += "SGST_AMT,IGST_PER,IGST_AMT,COLUMN_17_CAPTION,COLUMN_17_AMT) ";
                            //strSQL += "VALUES ";
                            strSQL += "(SELECT COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,CONCAT(SUBSTRING(CHARGE_CODE,2,1),SUBSTRING(DESP_MODE,2,1)),BALE_NO,BAS_AMT,NET_AMT,INVOICE_AMT,ACTUAL_WEIGHT, ";
                            strSQL += "TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,SGST_AMT,'SGST_AMT',CGST_AMT+IGST_AMT,'IGST_CGST_AMT',0,DISC_PER,0,'DISC_PER',INSURANCE_AMT, ";
                            strSQL += "'INS CHRG','BANK CHRG',0,0,0,'GST AMOUNT',CHEM_TRT_CHG,SEAM_CHG,1,CANCELLED,CHANGED,0,0,0,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,IGST_PER,IGST_AMT,'TCS CHARGES',TCS_AMT ";
                            strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
                            strSQL += "WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "') ";

                            System.out.println("Dineshmills SQL : " + strSQL);
                            data.Execute(strSQL);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '10', 'DONE : DINESHMILLS INVOICE HEADER UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            String strSQLdet = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_DETAIL (COMPANY_ID, INVOICE_TYPE, INVOICE_NO, INVOICE_DATE, QUALITY_NO, PIECE_NO, PARTY_CODE, RATE, GROSS_SQ_MTR, GROSS_KG, GROSS_AMOUNT, TRD_DISCOUNT, ADDITIONAL_DUTY, NET_AMOUNT, HSN_CODE) ";
                            //strSQLdet += "VALUES ";
//                            strSQLdet += "(SELECT 2,2,INVOICE_NO,SUBSTRING(INVOICE_DATE, 1, 10),PRODUCT_CODE,PIECE_NO,PARTY_CODE,GROSS_RATE,SQMTR,ACTUAL_WEIGHT,BAS_AMT,DISC_AMT,AOSD_AMT,INVOICE_AMT,5911 ";
                            strSQLdet += "(SELECT 2,2,INVOICE_NO,SUBSTRING(INVOICE_DATE, 1, 10),PRODUCT_CODE,PIECE_NO,PARTY_CODE,GROSS_RATE,SQMTR,ACTUAL_WEIGHT,BAS_AMT,DISC_AMT,AOSD_AMT,INVOICE_AMT,59113290 ";
                            strSQLdet += "FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL ";
                            strSQLdet += "WHERE INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "') ";

                            System.out.println("Dineshmills Detail SQL : " + strSQLdet);
                            data.Execute(strSQLdet);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '11', 'DONE : DINESHMILLS INVOICE DETAIL UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            if (!data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'")) {
                                int trNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                                trNo = trNo + 1;
                                data.Execute("UPDATE DINESHMILLS.D_SAL_TRANSPORTER_MASTER SET TR_LET_NO='" + trNo + "',TR_LET_DATE=CURDATE(),LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "' WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                            }

                            String strGstrSQL = "INSERT INTO FINANCE.D_SAL_GSTR_INVOICE_ERP ";
                            strGstrSQL += "(COMPANY_ID,INPUT_TYPE,WH_CODE,INVOICE_TYPE,PARTY_CODE,GSTIN_NO,INVOICE_NO,INVOICE_DATE, ";
                            strGstrSQL += "INVOICE_VALUE,ITEM_DESC,HSN_CODE,TAXABLE_VALUE,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT, ";
                            strGstrSQL += "SGST_PER,SGST_AMT,STATE_CODE,STATE_NAME,PLACE_OF_SUPPLY,RATE,REV_CHRG,E_COMM_GSTIN_NO, ";
                            strGstrSQL += "STATE_PIN_CODE,APPROVED,CANCELLED) ";

                            strGstrSQL += "(SELECT COMPANY_ID,INVOICE_TYPE,'FLT','Regular',PARTY_CODE,GSTIN_NO, ";
                            strGstrSQL += "INVOICE_NO,INVOICE_DATE,INVOICE_AMT,PRODUCT_DESC,HSN_CODE,INVOICE_TAXABLE_AMT, ";
                            strGstrSQL += "IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT, ";
                            strGstrSQL += "SUBSTRING(GSTIN_NO,1,2),PLACE_OF_SUPPLY,'" + gstrPos + "', ";
                            strGstrSQL += "'12','N','',PINCODE,APPROVED,CANCELLED ";
                            strGstrSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
                            strGstrSQL += "WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "') ";

                            System.out.println("GSTR SQL : " + strGstrSQL);
                            data.Execute(strGstrSQL);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+frmFeltInvPro.gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '12', 'DONE : GSTR INVOICE DATA UPDATED FOR "+InvNo+" DATED "+InvDate+"', '' ) ");

                            //EITLERP.FeltSales.common.JavaMail.SendMail("gaurang@dineshmills.com", "Invoice Detail :/p Charge Code : -" + ChargeCode + "-  Party Code : -" + Party_code + "- Inv No : -" + InvNo + "- Inv Date : -" + InvDate + "- Invoice Amount : -" + InvoiceAmount + "- Invoice Date : -" + InvoiceDate + "- Invoice No : -" + InvoiceNo + "-", "Invoice Detail", "");
//                                ExternalImportSJ(true, ChargeCode, InvoiceAmount, Party_code, InvNo, InvDate);
                            
                            //
                            //Added on 02/11/2021
                            String partyChCode = rsInvHeader.getString("PARTY_CHARGE_CODE");
                            String prodCode = rsInvHeader.getString("PRODUCT_CODE");
                            String machineNo = rsInvHeader.getString("MACHINE_NO");
                            String positionNo = rsInvHeader.getString("POSITION_NO");
                            double invDiscPer = rsInvHeader.getDouble("DISC_PER");
                            double invGrossRate = rsInvHeader.getDouble("GROSS_RATE");
                            if (!partyChCode.startsWith("08")) {
                                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_DATE>='"+EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB())+"' AND INVOICE_DATE<CURDATE() AND PARTY_CODE='" + Party_code + "' AND PRODUCT_CODE='" + prodCode + "' AND MACHINE_NO='" + machineNo + "' AND POSITION_NO='" + positionNo + "' ")) {
                                    double gRate = data.getDoubleValueFromDB("SELECT GROSS_RATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_DATE>='"+EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB())+"' AND INVOICE_DATE<CURDATE() AND PARTY_CODE='" + Party_code + "' AND PRODUCT_CODE='" + prodCode + "' AND MACHINE_NO='" + machineNo + "' AND POSITION_NO='" + positionNo + "' ORDER BY INVOICE_DATE DESC,INVOICE_NO DESC");
                                    double discPer = data.getDoubleValueFromDB("SELECT DISC_PER FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE APPROVED=1 AND CANCELLED=0 AND INVOICE_DATE>='"+EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.getCurrentDateDB())+"' AND INVOICE_DATE<CURDATE() AND PARTY_CODE='" + Party_code + "' AND PRODUCT_CODE='" + prodCode + "' AND MACHINE_NO='" + machineNo + "' AND POSITION_NO='" + positionNo + "' ORDER BY INVOICE_DATE DESC,INVOICE_NO DESC");
                                    if (gRate == invGrossRate && discPer == invDiscPer) {
                                        data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_DATA SET STATUS='F' WHERE MODULE_ID=80 AND DOC_NO='" + InvoiceNo + "' AND DOC_DATE='" + InvoiceDate + "' ");
                                        data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_HEADER SET APPROVED=1, APPROVED_DATE=CURDATE(), MODIFIED_BY=0, MODIFIED_DATE=CURDATE() WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                                        String SJNo = "";
                                        SJNo = data.getStringValueFromDB("SELECT DISTINCT VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' AND VOUCHER_NO LIKE 'SJ%'");
                                        data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_HEADER SET APPROVED=1, APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + SJNo + "'");
                                        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='INVOICED',PR_INVOICE_AMOUNT='" + Math.round(rsInvHeader.getFloat("INVOICE_AMT")) + "' WHERE PR_INVOICE_NO='" + InvoiceNo + "' AND PR_INVOICE_DATE='" + InvoiceDate + "'");
                                        data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP SET APPROVED=1 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ");
                                    }
                                }
                            }
                            
                        }
                    } catch (Exception c) {
                        c.printStackTrace();
//                        Msg = c.getMessage();
//                    log.logToFile(LogFileName, Msg, 2);
                        Done = true;
                    }

                    rsInvHeader.next();
                }
            }

            System.out.println("Finished");
        } catch (Exception e) {
        }
    }

    public static void ExternalImportSJ(boolean pPostSJ, String ChargeCode, double InvoiceAmount, String Party_code, String InvNo, String InvDate) {
        clsFeltSalesInvoice objInvoice = new clsFeltSalesInvoice();
        //String dbURL = "jdbc:mysql://200.0.0.230:3306/DINESHMILLS";
        String dbURL = EITLERPGLOBAL.DatabaseURL;
        boolean Done = false;
        boolean canPostSJ = false;
        boolean AutoAdj = false;

        if (ChargeCode.startsWith("09")) {

            double availableAmount = clsAccount.get09AmountByParty("210010", Party_code, InvDate);

            System.out.println("09 AMOUNT : " + availableAmount);

            if (InvoiceAmount > availableAmount) {
                canPostSJ = false;
                AutoAdj = false;
                data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
                data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
//                Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " of party " + Party_code + " with Invoice Amount is (" + InvoiceAmount + ") and advance amount is (" + availableAmount + ") \n"
//                        + "before invoice date, so invoice not imported.";
//                log.logToFile(LogFileName, Msg, 2);
                System.out.println("Invoice not Imported ");
            } else {
                canPostSJ = true;
                AutoAdj = true;
                //Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                    log.logToFile(LogFileName, Msg, 1);
                System.out.println("Invoice Imported ");
            }
            // Remove the comment for posting 09 prathmesh 03-10-2010 end
        } else {
            canPostSJ = true;
            AutoAdj = false;
            //Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                log.logToFile(LogFileName, Msg, 1);
            System.out.println("Invoice Imported ");
        }

        if (pPostSJ && canPostSJ) {
            System.out.println("Posting SJ");

            objInvoice = (clsFeltSalesInvoice) objInvoice.getObject(2, InvNo, InvDate, dbURL);

            if (objInvoice.PostSJTypeFelt(2, InvNo, InvDate, AutoAdj, "", "")) {
                //Msg = "SJ has been posted for Invoice No (" + InvNo + ") on date " + InvDate;
//                                    log.logToFile(LogFileName, Msg, 1);
                System.out.println("Invoice " + InvNo + " Posted.");
            } else {
                //Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
//                                    log.logToFile(LogFileName, Msg, 2);
                System.out.println(objInvoice.LastError);
                JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                return;
            }
        }

    }
}
