/*
 * clsDeleteRecord.java
 *
 * Created on May 20, 2011, 10:37 AM
 */
package sdml.felt.commonUI;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.*;

import java.text.*;

/**
 *
 * @author 
 */
public class clsDeleteRecord {

    /**
     * Creates a new instance of clsDeleteRecord
     */
    public clsDeleteRecord() {
    }

    public static void main(String[] args) {
        //new clsDeleteRecord().deleteRecordPR();
        //new clsDeleteRecord().deleteRecordIndent();
        //new clsDeleteRecord().deleteRecordPO();
        //new clsDeleteRecord().deleteRecordGRN();
        new clsDeleteRecord().deleteEntryfromallocation();
    }

    private void deleteRecordPR() {
        String SQL = "", dbURL = "jdbc:mysql://200.0.0.227:3306/DINESHMILLSA";
        ResultSet rsPRData = null;
        int eCounter = 0, neCounter = 0;
        try {
            SQL = "SELECT REQ_NO FROM D_INV_REQ_HEADER WHERE REQ_DATE<='2009-03-31' ORDER BY REQ_DATE";
            rsPRData = data.getResult(SQL, dbURL);
            rsPRData.first();
            while (!rsPRData.isAfterLast()) {
                String ReqNo = rsPRData.getString("REQ_NO");
                SQL = "SELECT * FROM D_INV_INDENT_DETAIL WHERE MR_NO='" + ReqNo + "' ";
                if (data.IsRecordExist(SQL, dbURL)) {
                    eCounter++;
                    /*SQL = "SELECT INDENT_NO FROM D_INV_INDENT_DETAIL WHERE MR_NO='"+ReqNo+"' ";
                     String IndentNo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: Req No : " + ReqNo + " Indent No : " + IndentNo);*/
                } else {
                    neCounter++;
                    System.out.println("NOT EXECUTED :: Req No : " + ReqNo);
                    /*data.Execute("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='"+ReqNo+"' AND MODULE_ID IN ('52','64') ",dbURL);
                     data.Execute("DELETE FROM D_INV_REQ_DETAIL_H WHERE REQ_NO='"+ReqNo+"' ",dbURL);
                     data.Execute("DELETE FROM D_INV_REQ_DETAIL WHERE REQ_NO='"+ReqNo+"' ",dbURL);
                     data.Execute("DELETE FROM D_INV_REQ_HEADER_H WHERE REQ_NO='"+ReqNo+"' ",dbURL);
                     data.Execute("DELETE FROM D_INV_REQ_HEADER WHERE REQ_NO='"+ReqNo+"' ",dbURL);*/
                }
                rsPRData.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("eCounter : " + eCounter + " Not Executed Counter : " + neCounter);
        System.out.println("*** Finished ***");
    }

    private void deleteRecordIndent() {
        String SQL = "", dbURL = "jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
        ResultSet rsIndentData = null;
        int inCounter = 0, poCounter = 0, neCounter = 0;
        try {
            SQL = "SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE INDENT_DATE<='2009-03-31' ORDER BY INDENT_DATE";
            rsIndentData = data.getResult(SQL, dbURL);
            rsIndentData.first();
            while (!rsIndentData.isAfterLast()) {
                String IndentNo = rsIndentData.getString("INDENT_NO");
                SQL = "SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='" + IndentNo + "' ";
                if (data.IsRecordExist(SQL, dbURL)) {
                    inCounter++;
                    /*SQL = "SELECT INQUIRY_NO FROM D_PUR_INQUIRY_DETAIL WHERE INDENT_NO='"+IndentNo+"' ";
                     String InquiryNo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: Indent No : " + IndentNo + " Inquiry No : " + InquiryNo);*/
                } else if (data.IsRecordExist("SELECT * FROM D_PUR_PO_DETAIL WHERE INDENT_NO='" + IndentNo + "' ", dbURL)) {
                    poCounter++;
                    /*SQL = "SELECT PO_NO FROM D_PUR_PO_DETAIL WHERE INDENT_NO='"+IndentNo+"' ";
                     String PONo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: Indent No : " + IndentNo + " PO No : " + PONo);*/
                } else {
                    neCounter++;
                    System.out.println("NOT EXECUTED :: Indent No : " + IndentNo);

                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='" + IndentNo + "' AND MODULE_ID='3' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_ITEM_DETAIL_H WHERE INDENT_NO='" + IndentNo + "' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_ITEM_DETAIL WHERE INDENT_NO='" + IndentNo + "' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_DETAIL_H WHERE INDENT_NO='" + IndentNo + "' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_DETAIL WHERE INDENT_NO='" + IndentNo + "' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_HEADER_H WHERE INDENT_NO='" + IndentNo + "' ", dbURL);
                    data.Execute("DELETE FROM D_INV_INDENT_HEADER WHERE INDENT_NO='" + IndentNo + "' ", dbURL);

                }
                rsIndentData.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Inquiry Counter : " + inCounter + " PO Counter : " + poCounter + " Not Executed Counter : " + neCounter);
        System.out.println("*** Finished ***");
    }

    private void deleteRecordPO() {
        String SQL = "", dbURL = "jdbc:mysql://200.0.0.227:3306/DINESHMILLSA";
        ResultSet rsPOData = null;
        int mirCounter = 0, grnCounter = 0, vCounter = 0, neCounter = 0;
        try {
            SQL = "SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_DATE<='2009-03-31' ORDER BY PO_DATE";
            rsPOData = data.getResult(SQL, dbURL);
            rsPOData.first();
            while (!rsPOData.isAfterLast()) {
                String PONo = rsPOData.getString("PO_NO");
                SQL = "SELECT * FROM D_INV_MIR_DETAIL WHERE PO_NO='" + PONo + "' ";
                if (data.IsRecordExist(SQL, dbURL)) {
                    mirCounter++;
                    /*SQL = "SELECT MIR_NO FROM D_INV_MIR_DETAIL WHERE PO_NO='"+PONo+"' ";
                     String MIRNo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: MIR No : " + MIRNo + " PO No : " + PONo);*/
                } else if (data.IsRecordExist("SELECT * FROM D_INV_GRN_DETAIL WHERE PO_NO='" + PONo + "' ", dbURL)) {
                    grnCounter++;
                    /*SQL = "SELECT GRN_NO FROM D_INV_GRN_DETAIL WHERE PO_NO='"+PONo+"' ";
                     String GRNNo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: GRN No : " + GRNNo + " PO No : " + PONo);*/
                } else if (data.IsRecordExist("SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE PO_NO='" + PONo + "' ", FinanceGlobal.FinURL)) {
                    vCounter++;
                    /*SQL = "SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE PO_NO='"+PONo+"' ";
                     String VoucherNo = data.getStringValueFromDB(SQL,dbURL);
                     System.out.println("EXECUTED :: Voucher No : " + VoucherNo + " PO No : " + PONo);*/
                } else {
                    neCounter++;
                    System.out.println("NOT EXECUTED :: PO No : " + PONo);

                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='" + PONo + "' AND MODULE_ID IN ('21','22','23','24','25','26','27') ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_TERMS_H WHERE PO_NO='" + PONo + "' ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_TERMS WHERE PO_NO='" + PONo + "' ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_DETAIL_H WHERE PO_NO='" + PONo + "' ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_DETAIL WHERE PO_NO='" + PONo + "' ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_HEADER_H WHERE PO_NO='" + PONo + "' ", dbURL);
                    data.Execute("DELETE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "' ", dbURL);

                }
                rsPOData.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MIR Counter : " + mirCounter + " GRN Counter : " + grnCounter + " Voucher No : " + vCounter + " Not Executed Counter : " + neCounter);
        System.out.println("*** Finished ***");
    }

    private void deleteRecordGRN() {
        String SQL = "", dbURL = "jdbc:mysql://200.0.0.227:3306/DINESHMILLSA";
        ResultSet rsGRNData = null;
        int mirCounter = 0, grnCounter = 0, vCounter = 0, neCounter = 0;
        try {
            SQL = "SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_DATE<='2000-03-31' ORDER BY GRN_DATE";
            rsGRNData = data.getResult(SQL, dbURL);
            rsGRNData.first();
            while (!rsGRNData.isAfterLast()) {
                String GRNNo = rsGRNData.getString("GRN_NO");
                //                SQL = "SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='"+GRNNo+"' ";
                //                if(data.IsRecordExist(SQL,dbURL)) {
                neCounter++;
                System.out.println("NOT EXECUTED :: GRN No : " + GRNNo);

                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE DOC_NO='" + GRNNo + "' AND MODULE_ID IN ('7','8') ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_LOT_H WHERE GRN_NO='" + GRNNo + "' ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_LOT WHERE GRN_NO='" + GRNNo + "' ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_DETAIL_H WHERE GRN_NO='" + GRNNo + "' ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_HEADER_H WHERE GRN_NO='" + GRNNo + "' ", dbURL);
                data.Execute("DELETE FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' ", dbURL);

                //                }
                rsGRNData.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("MIR Counter : " + mirCounter + " GRN Counter : " + grnCounter + " Voucher No : " + vCounter  +" Not Executed Counter : " + neCounter);
        System.out.println("Not Executed Counter : " + neCounter);
        System.out.println("*** Finished ***");
    }

    private void deleteEntryfromallocation() {
        String SQL = "", dbURL = "jdbc:mysql://200.0.1.101:3306/DINESHMILLS";
        int Counter = 0;
        try {
            SQL = "SELECT DISTINCT INDENT_NO FROM D_COM_STOCK_ALLOCATION";
            ResultSet rsData = data.getResult(SQL, dbURL);
            rsData.first();
            while (!rsData.isAfterLast()) {
                String IndentNo = rsData.getString("INDENT_NO");
                SQL = "SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE INDENT_NO='" + IndentNo + "'";
                if (!data.IsRecordExist(SQL, dbURL)) {
                    Counter++;
                    data.Execute("DELETE FROM FROM D_COM_STOCK_ALLOCATION WHERE INDENT_NO='" + IndentNo + "'", dbURL);
                }
                rsData.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Delete Counter : " + Counter);
        System.out.println("*** Finished ***");
    }
}
