/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Finance.*;
import EITLERP.*;


/**
 *
 * @author  THANDO
 * @version
 */

public class clsLegacy {
    
    public String LastError="";
    private ResultSet rsErp, rsLegacy, rsErpD, rsLegacyD;
    private Connection Conn;
    private Statement StmtLegacy, StmtLegacyD, StmtErp, StmtErpD;
    
    public boolean Ready = false;
    
    static String FromDate,ToDate="";
    
    public static void main(String args[]) {
        try {
            
            //FromDate = args[0];
            //ToDate = args[1];
            
            FromDate = "2008-09-01";
            ToDate   = "2008-09-30";

            clsLegacy ObjLegacy = new clsLegacy();
            //ObjLegacy.LoadData(EITLERPGLOBAL.gCompanyID);
            
            //ObjLegacy.LegacyToErp(EITLERPGLOBAL.gCompanyID);
            ObjLegacy.ErpToLegacy(EITLERPGLOBAL.gCompanyID);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public boolean LoadData(long pCompanyID) {
        
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            StmtLegacy=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsLegacy=StmtLegacy.executeQuery("SELECT * FROM L_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            
            StmtErp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsErp=StmtErp.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND LEGACY_NO <> '' AND VOUCHER_DATE >='"+FromDate+"' AND VOUCHER_DATE <='"+ToDate+"'");
            
            
            return true;
            
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void  LegacyToErp(long pCompanyID) {
        
        
        
    }
    
    public void  ErpToLegacy(long pCompanyID) {
        try {
            
            
            rsErp=data.getResult("SELECT * FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND LEGACY_NO <> '' AND VOUCHER_DATE >='"+FromDate+"' AND VOUCHER_DATE <='"+ToDate+"'",FinanceGlobal.FinURL);
            rsErp.first();
            String ErpVoucherNo,ErpBookCode,strQry,strVoucherNo;
            
            while (!rsErp.isAfterLast()) {
                strVoucherNo = rsErp.getString("VOUCHER_NO");
                ErpVoucherNo = rsErp.getString("LEGACY_NO");
                ErpBookCode  = rsErp.getString("BOOK_CODE");
                ErpVoucherNo = EITLERPGLOBAL.padLeft(6, ErpVoucherNo, "0");
                
                
                
                strQry ="SELECT COUNT(*) AS COUNT FROM L_FIN_VOUCHER_HEADER WHERE SUBSTRING(VOUCHER_NO,8,6) ='"+ErpVoucherNo+"' AND BOOK_CODE='"+ErpBookCode+"'";
                rsLegacy = data.getResult(strQry,FinanceGlobal.FinURL);
                rsLegacy.first();
                if (rsLegacy.getInt("COUNT")>0) {
                    
                    String strLegacy="",strErp="";
                    
                    strLegacy = " SELECT SUM(AMOUNT) ";
                    strLegacy+= " FROM L_FIN_VOUCHER_HEADER A,L_FIN_VOUCHER_DETAIL B ";
                    strLegacy+= " WHERE A.COMPANY_ID="+pCompanyID+" AND ";
                    strLegacy+= " RIGHT(A.VOUCHER_NO,6) ='"+ErpVoucherNo+"' AND ";
                    strLegacy+= " A.BOOK_CODE='"+ErpBookCode+"' AND ";
                    strLegacy+= " A.COMPANY_ID= B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO ";
                    strLegacy+= " AND B.EFFECT = 'C' ";

                    strErp = " SELECT SUM(AMOUNT) ";
                    strErp+= " FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B ";
                    strErp+= " WHERE A.COMPANY_ID="+pCompanyID+" AND ";
                    strErp+= " A.VOUCHER_NO ='"+strVoucherNo+"' AND ";
                    strErp+= " A.BOOK_CODE='"+ErpBookCode+"' AND ";
                    strErp+= " A.COMPANY_ID = B.COMPANY_ID AND ";
                    strErp+= " A.VOUCHER_NO=B.VOUCHER_NO AND ";
                    strErp+= " B.EFFECT = 'C' ";

                    double LegacyVoucherAmountCredit = data.getDoubleValueFromDB(strLegacy,FinanceGlobal.FinURL);
                    double ErpVoucherAmountCredit = data.getDoubleValueFromDB(strErp,FinanceGlobal.FinURL);
                    
                    if (LegacyVoucherAmountCredit != ErpVoucherAmountCredit) {
                        System.out.println("Mismatch : ERP Voucher No : " + strVoucherNo + " Erp Credit Amount :" + Double.toString(ErpVoucherAmountCredit) + " Legacy Voucher No. "+ErpVoucherNo+" Legacy Credit Amount " + Double.toString(LegacyVoucherAmountCredit));
                    }
                    else
                    {
                        System.out.println("Sucess : ERP Voucher No : " + strVoucherNo + " Erp Credit Amount :" + Double.toString(ErpVoucherAmountCredit) + " Legacy Voucher No. "+ErpVoucherNo+" Legacy Credit Amount " + Double.toString(LegacyVoucherAmountCredit));
                    }
                        
                    
                }
                else {
                    //System.out.println("Voucher No : " + ErpVoucherNo+ ", Book Code = "+ErpBookCode+" Not Found in Legacy");
                }
                
                rsErp.next();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}