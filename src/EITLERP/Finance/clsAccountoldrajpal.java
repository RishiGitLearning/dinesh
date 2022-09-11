/*
 * clsAccount.java
 *
 * Created on August 10, 2007, 5:41 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import EITLERP.*;
import java.util.*;
import java.sql.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;
import EITLERP.Sales.*;


public class clsAccountoldrajpal {
    
    /** Creates a new instance of clsAccount */
    public clsAccountoldrajpal() {
        
    }
    
    public static String getAccountName(String MainCode,String SubCode) {
        try {
            if(!SubCode.trim().equals("")) //Real Account
            {
                if(data.IsRecordExist("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND TRIM(MAIN_ACCOUNT_CODE)='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL)) {
                    return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND TRIM(MAIN_ACCOUNT_CODE)='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
                }
                else {
                    return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND TRIM(MAIN_ACCOUNT_CODE)='' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
                }
                
            }
            else {
                if(MainCode.indexOf(".")!=-1) {
                    
                    return clsExAccount.getExAccountName(MainCode);
                }
                else {
                    return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"'",FinanceGlobal.FinURL);
                }
            }
        } catch(Exception e) {
        }
        return "";
    }
    
    public static boolean IsValidAccount(String MainCode,String SubCode) {
        try {
            
            if(MainCode.trim().equals("")&&SubCode.trim().equals("")) {
                return false;
            }
            
            if(!SubCode.trim().equals("")) //Real Account
            {
                return data.IsRecordExist("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+SubCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            }
            else {
                return data.IsRecordExist("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL);
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsSubsidairyAccount(String MainCode) {
        try {
            
            if(data.getIntValueFromDB("SELECT IS_SUBSIDAIRY FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' ", FinanceGlobal.FinURL)==1) {
                return true;
            }
        } catch(Exception e) {
            return false;
        }
        return false;
    }
    //Cumulative Single Balance, based on the party code (sub code).
    public static double getClosingBalance(String MainCode,String SubCode,String OnDate,boolean bApproved) {
        String strSQL="";
        double ClosingBalance=0;
        try {
            if(!SubCode.trim().equals("")) {
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                } else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
            else {
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    double a=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
        }
        catch(Exception e) {
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
    }
    
    public static double getClosingBalanceOld(String MainCode,String SubCode,String OnDate,boolean bApproved) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
            else {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                    OpeningDate=data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    double a=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    if(Math.abs(CrTotal) > 0 || DrTotal > 0) {
                        System.out.println("C = " + CrTotal + " D = " + DrTotal);
                    }
                } else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    if(Math.abs(CrTotal) > 0 || DrTotal > 0) {
                        System.out.println("C = " + CrTotal + " D = " + DrTotal);
                    }
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
                
            }
            
        }
        catch(Exception e) {
            
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
        
    }
    
    public static double getAvailableClosingBalance(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
            else {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                    OpeningDate=data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    double a=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
                
            }
        }
        catch(Exception e) {
            
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
        
    }
    
    public static double getOpeningBalance(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                } else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            } else {
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    //strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
        } catch(Exception e) {
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
    }
    
    public static double getAvailableOpeningBalance(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
            else {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    strSQL="SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                    OpeningDate=data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    System.out.println(strSQL);
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    System.out.println(strSQL);
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<'"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<'"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
                
            }
            
        }
        catch(Exception e) {
            
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
        
    }
    
    
    public static double getOpeningBalanceOfDepositParty(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        try {
            //Find Opening as on date
            double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
            int EntryNo=0;
            String OpeningDate="";
            double CrTotal=0,DrTotal=0;
            
            
            strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<'"+OnDate+"' ORDER BY ENTRY_DATE DESC";
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                
                Opening=OpeningDrTotal+OpeningCrTotal;
            }
            
            
            if(!OpeningDate.equals("")) {
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<'"+OnDate+"'";
                DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
            } else {
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<'"+OnDate+"'";
                CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                
                strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND MAIN_ACCOUNT_CODE IN"+MainCode+" AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<'"+OnDate+"'";
                DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
            }
            
            ClosingBalance=Opening+DrTotal+CrTotal;
            
        }
        catch(Exception e) {
            
        }
        
        return EITLERPGLOBAL.round(Math.abs(ClosingBalance),2);
        
    }
    
    
    
    public static double getClosingBalanceAll(String MainCode,String SubCode,String OnDate,boolean bApproved) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+"  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                } else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            } else {
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                } else {
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                ClosingBalance=Opening+DrTotal+CrTotal;
            }
        } catch(Exception e) {
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
    }
    
    public static double getClosingBalanceEx(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT ROUND(IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)),2) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
                
            }
            else {
                
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                
                
                strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                    EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                    OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='C' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE  EFFECT='D' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='' AND ENTRY_NO="+EntryNo;
                    OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                    
                    Opening=OpeningDrTotal+OpeningCrTotal;
                }
                
                
                if(!OpeningDate.equals("")) {
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                else {
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                    CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    
                    strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                    DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                }
                
                ClosingBalance=Opening+DrTotal+CrTotal;
                
            }
            
        }
        catch(Exception e) {
            
        }
        return EITLERPGLOBAL.round(ClosingBalance,2);
        
    }
    
    //Cumulative Single Balance, based on the party code (sub code).
    public static HashMap getClosingBalanceBifurcation(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        double ClosingBalanceAll=0;
        HashMap balance=new HashMap();
        
        try {
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                String strMainCode="",strcond="";
                if (MainCode.trim().equals("")) {
                    strcond="";
                }
                else {
                    strcond=" AND MAIN_ACCOUNT_CODE='"+ MainCode.trim() +"' ";
                }
                ResultSet rsTmp=data.getResult("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' " + strcond + " ",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        strMainCode=rsTmp.getString("MAIN_ACCOUNT_CODE");
                        Opening=0;OpeningCrTotal=0;OpeningDrTotal=0;CrTotal=0;DrTotal=0;
                        
                        
                        //============================= CLOSING BALANCE ==================================//
                        strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                        if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                            EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                            OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                            
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE EFFECT='C' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND ENTRY_NO="+EntryNo;
                            OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE EFFECT='D' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND ENTRY_NO="+EntryNo;
                            OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                            
                            Opening=OpeningDrTotal+OpeningCrTotal;
                        }
                        
                        
                        if(!OpeningDate.equals("")) {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        else {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED=1 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        
                        ClosingBalance=Opening+DrTotal+CrTotal;
                        //===============================================================================//
                        
                        
                        CrTotal=0;
                        DrTotal=0;
                        
                        if(!OpeningDate.equals("")) {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE>'"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        else {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        
                        ClosingBalanceAll=Opening+DrTotal+CrTotal;
                        
                        clsPartyMaster objParty=new clsPartyMaster();
                        objParty.setAttribute("MAIN_ACCOUNT_CODE",strMainCode);
                        objParty.setAttribute("SUB_ACCOUNT_CODE",SubCode);
                        objParty.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(strMainCode,""));
                        objParty.setAttribute("BALANCE",ClosingBalance);
                        objParty.setAttribute("BALANCE_ALL",ClosingBalanceAll);
                        
                        balance.put(Integer.toString(balance.size()+1),objParty);
                        
                        rsTmp.next();
                    }
                }
                
            }
            else {
                
            }
            
        }
        catch(Exception e) {
            
        }
        return balance;
        
    }
    
    
    public static HashMap getClosingBalanceBifurcationAll(String MainCode,String SubCode,String OnDate) {
        String strSQL="";
        double ClosingBalance=0;
        HashMap balance=new HashMap();
        
        try {
            if(!SubCode.trim().equals("")) {
                
                //Find Opening as on date
                double Opening=0,OpeningCrTotal=0,OpeningDrTotal=0;
                int EntryNo=0;
                String OpeningDate="";
                double CrTotal=0,DrTotal=0;
                String strMainCode="";
                
                ResultSet rsTmp=data.getResult("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        strMainCode=rsTmp.getString("MAIN_ACCOUNT_CODE");
                        Opening=0;OpeningCrTotal=0;OpeningDrTotal=0;CrTotal=0;DrTotal=0;
                        
                        
                        //============================= CLOSING BALANCE ==================================//
                        strSQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC";
                        if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                            EntryNo=data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL);
                            OpeningDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE EFFECT='C' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND ENTRY_NO="+EntryNo;
                            OpeningCrTotal=-1*data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_OPENING_DETAIL WHERE EFFECT='D' AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND ENTRY_NO="+EntryNo;
                            OpeningDrTotal=data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL);
                            
                            Opening=OpeningDrTotal+OpeningCrTotal;
                        }
                        
                        
                        if(!OpeningDate.equals("")) {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        else {
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='C' AND VOUCHER_DATE<='"+OnDate+"'";
                            CrTotal=-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                            
                            strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0  AND SUB_ACCOUNT_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+strMainCode+"' AND EFFECT='D' AND VOUCHER_DATE<='"+OnDate+"'";
                            DrTotal=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                        }
                        
                        ClosingBalance=Opening+DrTotal+CrTotal;
                        //===============================================================================//
                        
                        
                        clsPartyMaster objParty=new clsPartyMaster();
                        objParty.setAttribute("MAIN_ACCOUNT_CODE",strMainCode);
                        objParty.setAttribute("SUB_ACCOUNT_CODE",SubCode);
                        objParty.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(strMainCode,""));
                        objParty.setAttribute("BALANCE",ClosingBalance);
                        
                        balance.put(Integer.toString(balance.size()+1),objParty);
                        
                        rsTmp.next();
                    }
                }
                
            }
            else {
                
            }
            
        }
        catch(Exception e) {
            
        }
        return balance;
        
    }
    
    
    public static double getAdvancePaymentAmount(int CompanyID,String MainCode,String SubCode) {
        double AdvanceAmount=0;
        
        try {
            
            if(!SubCode.trim().equals("")) {
                String strSQL="SELECT ROUND(SUM(B.AMOUNT),2) AS TOTAL_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND B.EFFECT='D' AND B.GRN_NO='' ";
                AdvanceAmount=EITLERPGLOBAL.round(data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL),2);
            }
        }
        catch(Exception e) {
            
        }
        
        return AdvanceAmount;
    }
    
    
    public static HashMap getAdvancePaymentDetails(int CompanyID,String PartyCode,String PONo) {
        HashMap List=new HashMap();
        
        try {
            String strSQL="";
            
            if(PONo.trim().equals("")) {
                strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.AMOUNT,A.REMARKS,B.PO_NO,B.PO_DATE,B.GRN_NO,B.GRN_DATE,B.INVOICE_NO,B.INVOICE_DATE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND (B.PO_NO='' OR B.GRN_NO='') ";
            }
            else {
                strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.AMOUNT,B.PO_NO,B.PO_DATE,B.GRN_NO,B.GRN_DATE,B.INVOICE_NO,B.INVOICE_DATE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND B.PO_NO='"+PONo+"' AND B.GRN_NO='' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static HashMap getPartyPayableDetail(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate) {
        HashMap List=new HashMap();
        
        try {
            
            String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' " +
            "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 ";
            
            //             AND A.VOUCHER_NO NOT LIKE 'M%'
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            strSQL = strSQL + " ORDER BY A.VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vGRNNo=UtilFunctions.getString(rsTmp,"GRN_NO","");
                    String DueDate=calculateDueDate(vGRNNo,CompanyID);
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    //                    if(VoucherNo.equals("PJ094001327")) {
                    //                        System.out.println();
                    //                    }
                    int FullyExecutedPO=0;
                    
                    if(!vPONo.trim().equals("")) {
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    //Check whether full payment has been made to this
                    strSQL="SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.GRN_NO='"+vGRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" AND A.VOUCHER_NO NOT LIKE 'M%' ";
                    double AmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    double GRNAmount=clsGRNGen.getGRNTotalAmount(vGRNNo,CompanyURL);
                    
                    if((AmountPaid<GRNAmount)||(GRNAmount==0)||(AmountPaid>=GRNAmount)) {
                        if (UtilFunctions.getDouble(rsTmp,"AMOUNT",0) != AmountPaid || vGRNNo.equals("")) {
                            objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                            objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                            objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                            objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                            objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                            objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                            objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                            objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                            objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                            objItem.setAttribute("AMOUNT_PAID",AmountPaid);
                            objItem.setAttribute("DUE_DATE",DueDate);
                            objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                            objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                            objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                            
                            List.put(Integer.toString(List.size()+1),objItem);
                        }
                    }
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    
    public static HashMap getPartyReceivableDetail(int CompanyID,String MainCode,String PartyCode,String InvoiceNo,String FromDate,String ToDate,String AgentAlpha,String BookCode) {
        HashMap List=new HashMap();
        try {
            String strSQL="SELECT A.COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_TYPE="+FinanceGlobal.TYPE_SALES_JOURNAL+" " +
            "AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
            "AND A.APPROVED=1 AND A.CANCELLED=0 AND (B.MATCHED=0 OR B.MATCHED IS NULL) ";//
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=strSQL+" AND B.INVOICE_NO='"+InvoiceNo+"' ";//AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!MainCode.trim().equals("")) {
                strSQL=strSQL+" AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            if (!BookCode.trim().equals("")) {
                strSQL=strSQL+" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            strSQL+=" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vInvoiceNo=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    String vInvoiceDate=UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00");
                    
                    //Check whether full payment has been made to this invoice
                    strSQL="SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
                    "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO  AND A.CANCELLED=0 " +
                    "AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.INVOICE_NO='"+vInvoiceNo+"' " +
                    "AND B.INVOICE_DATE='"+vInvoiceDate+"' " +
                    "AND B.REF_COMPANY_ID="+CompanyID+" ";
                    if(!MainCode.trim().equals("")) {
                        strSQL=strSQL+" AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' ";
                    }
                    double AmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    double InvoiceAmount=clsSalesInvoice.getInvoiceAmount(vInvoiceNo,vInvoiceDate);
                    
                    if(InvoiceAmount == AmountPaid) {
                        rsTmp.next();
                        continue;
                    }
                    
                    if(AmountPaid<InvoiceAmount) {
                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                        objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                        objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                        objItem.setAttribute("PO_NO","");
                        objItem.setAttribute("PO_DATE","0000-00-00");
                        objItem.setAttribute("GRN_NO","");
                        objItem.setAttribute("GRN_DATE","0000-00-00");
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT_PAID",AmountPaid);
                        objItem.setAttribute("DUE_DATE","0000-00-00");
                        objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                        objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                        objItem.setAttribute("FULLY_EXECUTED_PO",false);
                        
                        List.put(Integer.toString(List.size()+1),objItem);
                    }
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getMainCodeReceivableDetail(int CompanyID,String MainCode,String PartyCode,String InvoiceNo,String FromDate,String ToDate,String AgentAlpha,String BookCode) {
        
        HashMap List=new HashMap();
        
        try {
            
            //String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE  A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.CANCELLED=0  ORDER BY A.VOUCHER_DATE";
            String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_TYPE=5 AND A.APPROVED=1 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND A.CANCELLED=0 ";
            
            if (!PartyCode.trim().equals("")) {
                strSQL=strSQL+" AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ";
            }
            
            if (!MainCode.trim().equals("")) {
                strSQL=strSQL+" AND B.MAIN_ACCOUNT_CODE ='"+MainCode+"' ";
            }
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=strSQL+" AND B.INVOICE_NO='"+InvoiceNo+"' ";//AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            if (!BookCode.trim().equals("")) {
                strSQL=strSQL+" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            strSQL+=" ORDER BY A.VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("GRN_NO","");
                    objItem.setAttribute("GRN_DATE","0000-00-00");
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT_PAID",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("FULLY_EXECUTED_PO",false);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getPartyDebitNotes(int CompanyID,String MainCode,String PartyCode, int VoucherType) {
        
        HashMap List=new HashMap();
        
        try {
            
            String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_TYPE="+VoucherType+" AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
            "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ORDER BY A.VOUCHER_DATE";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            List.clear();
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vInvoiceNo=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    int FullyExecutedPO=0;
                    
                    strSQL= "SELECT * FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL_DOCS B " +
                    "WHERE A.VOUCHER_NO = B.VOUCHER_NO  AND B.DOC_NO = '"+VoucherNo+"' AND A.CANCELLED=0";
                    if (data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                        rsTmp.next();
                        continue;
                    }
                    clsVoucher objItem=new clsVoucher();
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT_PAID",0);
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("FULLY_EXECUTED_PO",false);
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
        } catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getPartyCreditNotes(int CompanyID,String MainCode,String PartyCode,String InvoiceNo,String FromDate,String ToDate,int VoucherType) {
        
        HashMap List=new HashMap();
        
        try {
            String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 " + //A.VOUCHER_TYPE="+VoucherType+" AND 
            "AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
            "AND A.APPROVED=1 ";
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=strSQL+" AND B.INVOICE_NO='"+InvoiceNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            strSQL = strSQL + " ORDER BY A.VOUCHER_DATE ";
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vInvoiceNo=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    int FullyExecutedPO=0;
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("INVOICE_AMOUNT", UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("GRN_NO","");
                    objItem.setAttribute("GRN_DATE","0000-00-00");
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT_PAID",0);
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("FULLY_EXECUTED_PO",false);
                    objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static double getPartyPayableAmount(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate) {
        double PayableAmount=0;
        
        try {
            String strSQL="SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 ";
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PayableAmount=UtilFunctions.getDouble(rsTmp,"TOTAL_AMOUNT",0);
            }
            
        }
        catch(Exception e) {
            
        }
        
        return PayableAmount;
    }
    
    public static double getPartyPayableAmountAll(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate) {
        double PayableAmount=0;
        
        try {
            String strSQL="SELECT SUM(AMOUNT) AS TOTAL_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.CANCELLED=0 ";
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PayableAmount=UtilFunctions.getDouble(rsTmp,"TOTAL_AMOUNT",0);
            }
            
        }
        catch(Exception e) {
            
        }
        
        return PayableAmount;
    }
    
    public static HashMap getPartyPayableDetailMIS(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate,String Approval,String Creation,String DocNo) {
        HashMap List=new HashMap();
        
        try {
            
            String strSQL="SELECT A.VOUCHER_DATE,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C'  AND A.CANCELLED=0 AND A.COMPANY_ID="+CompanyID+" ";
            
            if(!PartyCode.trim().equals("")) {
                strSQL=strSQL+" AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ";
            }
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' ";
            }
            
            if(Creation.trim().equals("C")) {
                if(!FromDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
                }
                
                if(!ToDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
                }
            }
            else {
                if(!FromDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.APPROVED_DATE>='"+FromDate+"' ";
                }
                
                if(!ToDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.APPROVED_DATE<='"+ToDate+"' ";
                }
                
            }
            
            
            if(Approval.trim().equals("A")) {
                strSQL=strSQL+" AND A.APPROVED=1";
            }
            
            if(Approval.trim().equals("U")) {
                strSQL=strSQL+" AND A.APPROVED=0";
            }
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vGRNNo=UtilFunctions.getString(rsTmp,"GRN_NO","");
                    String DueDate=calculateDueDate(vGRNNo,CompanyID);
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    int FullyExecutedPO=0;
                    
                    if(!vPONo.trim().equals("")) {
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'");
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    //Check whether full payment has been made to this
                    strSQL="SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.GRN_NO='"+vGRNNo+"' AND A.COMPANY_ID="+CompanyID+" ";
                    double AmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    double GRNAmount=clsGRNGen.getGRNTotalAmount(vGRNNo,CompanyURL);
                    
                    if(AmountPaid<GRNAmount) {
                        objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                        objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT_PAID",AmountPaid);
                        objItem.setAttribute("DUE_DATE",DueDate);
                        //objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                        objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                        
                        if(UtilFunctions.getInt(rsTmp,"APPROVED",0)==1) {
                            objItem.setAttribute("APPROVED","Y");
                            objItem.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"APPROVED_DATE","0000-00-00")));
                            objItem.setAttribute("WAITING_USER","");
                            objItem.setAttribute("RECEIVED_DATE","0000-00-00");
                            
                        }
                        else {
                            objItem.setAttribute("APPROVED","N");
                            objItem.setAttribute("APPROVED_DATE","0000-00-00");
                            
                            String WaitingUser=clsUser.getUserName(CompanyID,ApprovalFlow.getWaitingUser(CompanyID,clsVoucher.ModuleID, UtilFunctions.getString(rsTmp,"VOUCHER_NO","")));
                            String ReceivedDate=EITLERPGLOBAL.formatDate(ApprovalFlow.getWaitingReceivedDate(CompanyID, clsVoucher.ModuleID, UtilFunctions.getString(rsTmp,"VOUCHER_NO","")));
                            
                            objItem.setAttribute("WAITING_USER",WaitingUser);
                            objItem.setAttribute("RECEIVED_DATE",ReceivedDate);
                        }
                        
                        List.put(Integer.toString(List.size()+1),objItem);
                    }
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static HashMap getPartyPaymentDetailMIS(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate,String Approval,String Creation,String DocNo) {
        HashMap List=new HashMap();
        
        try {
            
            String strSQL="SELECT A.*,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D'  AND A.CANCELLED=0 ";
            
            if(!PartyCode.trim().equals("")) {
                strSQL=strSQL+" AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ";
            }
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(Creation.trim().equals("C")) {
                if(!FromDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
                }
                
                if(!ToDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
                }
            }
            else {
                if(!FromDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.APPROVED_DATE>='"+FromDate+"' ";
                }
                
                if(!ToDate.trim().equals("")) {
                    strSQL=strSQL+" AND A.APPROVED_DATE<='"+ToDate+"' ";
                }
                
            }
            
            
            if(Approval.trim().equals("A")) {
                strSQL=strSQL+" AND A.APPROVED=1";
            }
            
            if(Approval.trim().equals("U")) {
                strSQL=strSQL+" AND A.APPROVED=0";
            }
            
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vGRNNo=UtilFunctions.getString(rsTmp,"GRN_NO","");
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    int FullyExecutedPO=0;
                    
                    if(!vPONo.trim().equals("")) {
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'");
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    //Check whether full payment has been made to this
                    strSQL="SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.GRN_NO='"+vGRNNo+"' AND A.COMPANY_ID="+CompanyID+" ";
                    double AmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    double GRNAmount=clsGRNGen.getGRNTotalAmount(vGRNNo,CompanyURL);
                    
                    if(AmountPaid<GRNAmount) {
                        objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                        objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT_PAID",AmountPaid);
                        objItem.setAttribute("DUE_DATE",calculateDueDate(UtilFunctions.getString(rsTmp,"GRN_NO",""),CompanyID));
                        //objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                        objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                        
                        if(UtilFunctions.getInt(rsTmp,"APPROVED",0)==1) {
                            objItem.setAttribute("APPROVED","Y");
                            objItem.setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"APPROVED_DATE","0000-00-00")));
                            objItem.setAttribute("WAITING_USER","");
                            objItem.setAttribute("RECEIVED_DATE","0000-00-00");
                            
                        }
                        else {
                            objItem.setAttribute("APPROVED","N");
                            objItem.setAttribute("APPROVED_DATE","0000-00-00");
                            
                            String WaitingUser=clsUser.getUserName(CompanyID,ApprovalFlow.getWaitingUser(CompanyID,clsVoucher.ModuleID, UtilFunctions.getString(rsTmp,"VOUCHER_NO","")));
                            String ReceivedDate=EITLERPGLOBAL.formatDate(ApprovalFlow.getWaitingReceivedDate(CompanyID, clsVoucher.ModuleID, UtilFunctions.getString(rsTmp,"VOUCHER_NO","")));
                            
                            objItem.setAttribute("WAITING_USER",WaitingUser);
                            objItem.setAttribute("RECEIVED_DATE",ReceivedDate);
                        }
                        
                        List.put(Integer.toString(List.size()+1),objItem);
                    }
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static HashMap getPartyPaymentDetail(int CompanyID,String PartyCode,String GRNNo,String FromDate,String ToDate) {
        HashMap List=new HashMap();
        
        try {
            String strSQL="SELECT A.*,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.CANCELLED=0 ";
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND REF_COMPANY_ID="+CompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("DUE_DATE",calculateDueDate(UtilFunctions.getString(rsTmp,"GRN_NO",""),CompanyID));
                    objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                    
                    
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    int FullyExecutedPO=0;
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    
                    if(!vPONo.trim().equals("")) {
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
            
        } catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getLedger(String MainCode,String SubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        //OLD
        HashMap List=new HashMap();
        //clsVoucher objItem;
        clsVoucherLedgerItem objItem;
        ResultSet rsOpposite;
        
        try {
            boolean Continue=true;
            String strSQL="";
            String Condition="";
            int VoucherType=0;
            double LineAmount=0;
            /*String SrNo = "";
            String PreviousVoucher = "";*/
            
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            if(!SubCode.trim().equals("")) {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO"; //AND BOOK_CODE<>'17'
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            } else {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            }
            
            double ClosingBalance=0;
            String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
            
            if(OnlyApproved) {
                ClosingBalance=clsAccount.getOpeningBalance(MainCode, SubCode, FromDate);
            }
            else {
                ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, SubCode, FromDate);
            }
            
            
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("COMPANY_ID",0);
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
            objItem.setAttribute("VOUCHER_NO","OPENING");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","Opening Balance");
            objItem.setAttribute("REMARKS"," *** OPENING BALANCE ***");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            if(ClosingBalance<0) { //It's a credit entry
                objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                objItem.setAttribute("DEBIT_AMOUNT",0);
            } else { //It's a debit entry
                objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                objItem.setAttribute("CREDIT_AMOUNT",0);
            }
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("MIR_NO","");
            objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","OPENING");
            objItem.setAttribute("EXECUTED_PO",0);
            
            List.put(Integer.toString(List.size()+1),objItem);
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            //SrNo="";
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String Effect=UtilFunctions.getString(rsTmp,"EFFECT","");
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    VoucherType=UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE",0);
                    LineAmount=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    /*if(!VoucherNo.equals(PreviousVoucher)) {
                        SrNo="";
                        PreviousVoucher = VoucherNo;
                    }*/ 
                    Condition="";
                    
                    if(FromAmount>0) {
                        Condition="AND AMOUNT>="+FromAmount+" ";
                    }
                    
                    if(ToAmount>0) {
                        Condition="AND AMOUNT<="+ToAmount+" ";
                    }
                    
                    Continue=true;
                    
                    if(!Condition.trim().equals("")) {
                        Condition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                        if(!data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE "+Condition,FinanceGlobal.FinURL)) {
                            Continue=false;
                        }
                        
                    }
                    
                    if(Continue) {
                        
                        String Description=UtilFunctions.getString(rsTmp,"REMARKS","");
                        int BlockNo=UtilFunctions.getInt(rsTmp,"BLOCK_NO",0);
                        
                        String vMainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                        String vSubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                        
                        
                        boolean validAccountCodes=true;
                        
                        if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                            validAccountCodes=false;
                        }
                        else {
                            /*if(Effect.equals("C")) {
                                ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            }
                            else {
                                ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            }*/
                        }
                        
                        if(BlockNo>0) {
                            //Find Opposite Account
                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                //Added by Mrugesh 07/02/2011 -- start
                                /*if(!SrNo.equals("")) {
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' AND SR_NO NOT IN ("+SrNo.substring(0,SrNo.length()-1)+") ORDER BY SR_NO",FinanceGlobal.FinURL);
                                } else {
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                }*/
                                
                                if(VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                    //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' AND AMOUNT= "+LineAmount+" ORDER BY SR_NO",FinanceGlobal.FinURL);
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                } else {
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                }
                                
                                //Added by Mrugesh 07/02/2011 -- end
                                //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL); //AND AMOUNT= "+LineAmount+"
                            }
                            else {
                                rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND BLOCK_NO="+BlockNo+" AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                            }
                            
                            rsOpposite.first();
                            if(rsOpposite.getRow()>0) {
                                //while(!rsOpposite.isAfterLast()) {
                                String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                //SrNo =SrNo + Integer.toString(UtilFunctions.getInt(rsOpposite,"SR_NO",0)) + ",";
                                //objItem=new clsVoucher();
                                objItem = new clsVoucherLedgerItem();
                                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                                objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                objItem.setAttribute("REMARKS",Description);
                                objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                                //objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));//COMMENTED BY NISARG
                                //objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOpposite,"CHEQUE_DATE","0000-00-00")));//COMMENTED BY NISARG
                                objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE EFFECT='"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                                //objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                String AcName = clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                if(VoucherType==FinanceGlobal.TYPE_JOURNAL) {
                                    AcName = clsAccount.getAccountName(vMainCode, vSubCode);
                                }
                                /*if(!SubCode.equals("")) {
                                    AcName=clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                } else {
                                }*/
                                
                                objItem.setAttribute("ACCOUNT_NAME", AcName );
                                if(Effect.equals("C")) {
                                    
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE ) {
                                        ClosingBalance-=LineAmount;
                                        objItem.setAttribute("CREDIT_AMOUNT",LineAmount);
                                    }
                                    else {
                                        ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                        objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    }
                                    
                                    
                                    objItem.setAttribute("DEBIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","C");
                                } else {
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                        ClosingBalance+=LineAmount;
                                        objItem.setAttribute("DEBIT_AMOUNT",LineAmount);
                                    }
                                    else {
                                        ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                        objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    }
                                    
                                    objItem.setAttribute("CREDIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","D");
                                }
                                
                                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                
                                //======== Provide Additional Information ==============//
                                String GRNNo=UtilFunctions.getString(rsTmp,"GRN_NO", "");
                                
                                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                                
                                objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                                
                                objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                                objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00")));
                                
                                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                                objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                                objItem.setAttribute("MIR_NO","");
                                objItem.setAttribute("MIR_DATE","0000-00-00");
                                objItem.setAttribute("DUE_DATE","0000-00-00");
                                objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsTmp,"INVOICE_NO","")+UtilFunctions.getString(rsTmp,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                
                                if(!objItem.getAttribute("GRN_NO").getString().trim().equals("")) {
                                    try {
                                        int RefCompanyID=UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0);
                                        String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                                        
                                        ResultSet rsMIR=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                        rsMIR.first();
                                        
                                        if(rsMIR.getRow()>0) {
                                            String MIRNo=rsMIR.getString("MIR_NO");
                                            
                                            objItem.setAttribute("MIR_NO",MIRNo);
                                            
                                            rsMIR=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                            rsMIR.first();
                                            
                                            if(rsMIR.getRow()>0) {
                                                String MIRDate=rsMIR.getString("MIR_DATE");
                                                objItem.setAttribute("MIR_DATE",EITLERPGLOBAL.formatDate(MIRDate));
                                            }
                                        }
                                        
                                        objItem.setAttribute("DUE_DATE",calculateDueDate(GRNNo,RefCompanyID));
                                    } catch(Exception MIR) {
                                    }
                                }
                                //======================================================//
                                
                                
                                //============ Check whether PO is executed ===============//
                                int FullyExecutedPO=0;
                                String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                                int CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                                String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                                //System.out.println(CompanyID);
                                if(!vPONo.trim().equals("")) {
                                    ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                                    try {
                                        if(rsPO.getRow()>0) {
                                            rsPO.first();
                                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                                FullyExecutedPO=1;
                                            }
                                        }
                                    }catch(Exception e) {}
                                }
                                
                                objItem.setAttribute("EXECUTED_PO",FullyExecutedPO);
                                //=========================================================//
                                System.out.println("count="+List.size()+" voucher_date="+UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00") + "partycode="+UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","")+" VOUCHER_NO="+UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                                //Add Entry to the list
                                List.put(Integer.toString(List.size()+1),objItem);
                                rsOpposite.next();
                                //}
                            }
                        }
                    }
                    rsTmp.next();
                }
            }
            //Adding Final Closing Balance entry
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objItem.setAttribute("VOUCHER_NO","CLOSING");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","Closing Balance");
            objItem.setAttribute("REMARKS"," *** CLOSING BALANCE *** ");
            
            if(ClosingBalance<0) {
                objItem.setAttribute("CREDIT_AMOUNT",0);
                objItem.setAttribute("DEBIT_AMOUNT",0);
            } else {
                objItem.setAttribute("DEBIT_AMOUNT",0);
                objItem.setAttribute("CREDIT_AMOUNT",0);
            }
            
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            objItem.setAttribute("MIR_NO","");
            objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","CLOSING");
            objItem.setAttribute("EXECUTED_PO",0);
            
            
            List.put(Integer.toString(List.size()+1),objItem);
            
            //Add a Blank Line
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objItem.setAttribute("VOUCHER_NO","CLOSING");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","");
            objItem.setAttribute("REMARKS","**** CLOSING BALANCE ****");
            objItem.setAttribute("DEBIT_AMOUNT",0);
            objItem.setAttribute("CREDIT_AMOUNT",0);
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            objItem.setAttribute("MIR_NO","");
            objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","");
            
            List.put(Integer.toString(List.size()+1),objItem);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getLedger(String MainCode,String FromSubCode,String ToSubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        HashMap List=new HashMap();
        clsVoucherLedgerItem objItem;
        ResultSet rsOpposite;
        //NOMINAL
        try {
            boolean Continue=true;
            String strSQL="";
            String Condition="";
            int VoucherType=0;
            double LineAmount=0;
            
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            String cond = "";
            if ((!FromSubCode.trim().equals("")) && (ToSubCode.trim().equals(""))) {
                cond = "AND B.SUB_ACCOUNT_CODE >= '"+FromSubCode+"' ";
            }
            else if ((!FromSubCode.trim().equals("")) && (!ToSubCode.trim().equals(""))) {
                cond = "AND B.SUB_ACCOUNT_CODE BETWEEN '"+FromSubCode+"' AND '"+ToSubCode+"' ";
            }
            else if ((FromSubCode.trim().equals("")) && (!ToSubCode.trim().equals(""))) {
                cond = "AND B.SUB_ACCOUNT_CODE <= '"+FromSubCode+"' ";
            }
            
            if ((FromSubCode.trim().equals(""))  && (ToSubCode.trim().equals(""))) {
                if(OnlyApproved) {
                    strSQL="SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE";
                }
                else {
                    strSQL="SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE";
                }
            }
            else {
                if(OnlyApproved) {
                    //strSQL="SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' "+ cond +"  AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                    strSQL = "SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' "+ cond +"  AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" "+
                    "UNION "+
                    "SELECT DISTINCT SUB_ACCOUNT_CODE FROM D_FIN_OPENING_DETAIL "+
                    "WHERE ENTRY_NO = 4 "+
                    "AND MAIN_ACCOUNT_CODE = 125033 "+
                    "AND AMOUNT <>0 "+
                    "AND SUB_ACCOUNT_CODE BETWEEN '601011' and '701025' "+
                    "ORDER BY 1";
                }
                else {
                    strSQL="SELECT DISTINCT B.SUB_ACCOUNT_CODE FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' "+ cond +" AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                }
            }
            ResultSet rsParty = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsParty.first();
            
            if(rsParty.getRow()>0) {
                while(!rsParty.isAfterLast()) {
                    String Party_Code=UtilFunctions.getString(rsParty,"SUB_ACCOUNT_CODE","");
                    
                    double ClosingBalance=0;
                    String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
                    
                    if(OnlyApproved) {
                        ClosingBalance=clsAccount.getOpeningBalance(MainCode, Party_Code, FromDate);
                    }
                    else {
                        ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, Party_Code, FromDate);
                    }
                    
                    
                    //objItem=new clsVoucher();
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("COMPANY_ID",0);
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
                    objItem.setAttribute("VOUCHER_NO","OPENING");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE","");
                    objItem.setAttribute("SUB_ACCOUNT_CODE",Party_Code);
                    objItem.setAttribute("ACCOUNT_NAME","Opening Balance");
                    objItem.setAttribute("REMARKS"," *** OPENING BALANCE ***");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    if(ClosingBalance<0) { //It's a credit entry
                        objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    }
                    else { //It's a debit entry
                        objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    //objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("MIR_NO","");
                    objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","OPENING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    if ((FromSubCode.trim().equals(""))  && (ToSubCode.trim().equals(""))) {
                        if(OnlyApproved) {
                            strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                        } else {
                            strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                        }
                    } else {
                        if(OnlyApproved) {
                            strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+Party_Code+"'  AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                        } else {
                            strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+Party_Code+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                        }
                    }
                    ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            
                            String Effect=UtilFunctions.getString(rsTmp,"EFFECT","");
                            String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                            String pVoucherNo="";
                            
                            if(VoucherNo.length()>11 && VoucherNo.startsWith("M")) {
                                pVoucherNo = VoucherNo.substring(0,1)+VoucherNo.substring(9);
                            } else {
                                pVoucherNo=VoucherNo;
                            }
                            VoucherType=UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE",0);
                            LineAmount=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                            
                            Condition="";
                            
                            if(FromAmount>0) {
                                Condition="AND AMOUNT>="+FromAmount+" ";
                            }
                            
                            if(ToAmount>0) {
                                Condition="AND AMOUNT<="+ToAmount+" ";
                            }
                            
                            Continue=true;
                            
                            if(!Condition.trim().equals("")) {
                                
                                Condition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                                if(!data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE "+Condition,FinanceGlobal.FinURL)) {
                                    Continue=false;
                                }
                                
                            }
                            
                            if(Continue) {
                                
                                
                                //String Description=UtilFunctions.getString(rsTmp,"REMARKS","");
                                String Description=UtilFunctions.getString(rsTmp,"REMARKS","").replace('\n',' ');
                                if(Description.length()>125) {
                                    Description=Description.substring(0,125);
                                }
                                int BlockNo=UtilFunctions.getInt(rsTmp,"BLOCK_NO",0);
                                
                                String vMainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                                String vSubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                                
                                
                                boolean validAccountCodes=true;
                                
                                if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                                    validAccountCodes=false;
                                }
                                else {
                                    /*if(Effect.equals("C")) {
                                        ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                                    }
                                    else {
                                        ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                                    }*/
                                }
                                
                                if(BlockNo>0) {
                                    //Find Opposite Account
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                        rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX A WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT<>'"+Effect+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                    }
                                    else {
                                        rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX A WHERE VOUCHER_NO='"+VoucherNo+"' AND BLOCK_NO="+BlockNo+" AND EFFECT<>'"+Effect+"'",FinanceGlobal.FinURL);
                                    }
                                    
                                    rsOpposite.first();
                                    if(rsOpposite.getRow()>0) {
                                        String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                        
                                        //objItem=new clsVoucher();
                                        objItem = new clsVoucherLedgerItem();
                                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                                        //objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                        objItem.setAttribute("VOUCHER_NO",pVoucherNo);
                                        objItem.setAttribute("REMARKS",Description);
                                        objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                                        //  objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));//COMMENTED BY NISARG
                                        //objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOpposite,"CHEQUE_DATE","0000-00-00")));//COMMENTED BY NISARG
                                        objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                        objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                        String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='"+Effect+"'",FinanceGlobal.FinURL);
                                        
                                        objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                                        //objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                        String AcName=clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                        objItem.setAttribute("ACCOUNT_NAME", AcName );
                                        if(Effect.equals("C")) {
                                            
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                                ClosingBalance-=LineAmount;
                                                objItem.setAttribute("CREDIT_AMOUNT",LineAmount);
                                            } else {
                                                ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                            }
                                            objItem.setAttribute("DEBIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","C");
                                        } else {
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                                ClosingBalance+=LineAmount;
                                                objItem.setAttribute("DEBIT_AMOUNT",LineAmount);
                                            }
                                            else {
                                                ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                            }
                                            
                                            objItem.setAttribute("CREDIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","D");
                                        }
                                        
                                        objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                        
                                        //======== Provide Additional Information ==============//
                                        String GRNNo=UtilFunctions.getString(rsTmp,"GRN_NO", "");
                                        
                                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                                        objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                                        objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                                        objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00")));
                                        
                                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                                        objItem.setAttribute("MIR_NO","");
                                        objItem.setAttribute("MIR_DATE","0000-00-00");
                                        objItem.setAttribute("DUE_DATE","0000-00-00");
                                        objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsTmp,"INVOICE_NO","")+UtilFunctions.getString(rsTmp,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                        
                                        if(!objItem.getAttribute("GRN_NO").getString().trim().equals("")) {
                                            
                                            try {
                                                int RefCompanyID=UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0);
                                                String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                                                
                                                ResultSet rsMIR=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                                rsMIR.first();
                                                
                                                if(rsMIR.getRow()>0) {
                                                    String MIRNo=rsMIR.getString("MIR_NO");
                                                    
                                                    objItem.setAttribute("MIR_NO",MIRNo);
                                                    
                                                    rsMIR=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                                    rsMIR.first();
                                                    
                                                    if(rsMIR.getRow()>0) {
                                                        String MIRDate=rsMIR.getString("MIR_DATE");
                                                        objItem.setAttribute("MIR_DATE",EITLERPGLOBAL.formatDate(MIRDate));
                                                    }
                                                }
                                                
                                                objItem.setAttribute("DUE_DATE",calculateDueDate(GRNNo,RefCompanyID));
                                            } catch(Exception MIR) {
                                            }
                                        }
                                        //======================================================//
                                        
                                        
                                        //============ Check whether PO is executed ===============//
                                        int FullyExecutedPO=0;
                                        String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                                        int CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                                        String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                                        //System.out.println(CompanyID);
                                        if(!vPONo.trim().equals("")) {
                                            
                                            ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                                            rsPO.first();
                                            
                                            if(rsPO.getRow()>0) {
                                                if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                                    FullyExecutedPO=1;
                                                }
                                            }
                                        }
                                        
                                        objItem.setAttribute("EXECUTED_PO",FullyExecutedPO);
                                        //=========================================================//
                                        System.out.println("count="+List.size()+" voucher_date="+UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00") + "partycode="+UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","")+" VOUCHER_NO="+UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                                        
                                        //Add Entry to the list
                                        List.put(Integer.toString(List.size()+1),objItem);
                                    }
                                }
                            }
                            rsTmp.next();
                        }
                        
                    }
                    
                    //Adding Final Closing Balance entry
                    //objItem=new clsVoucher();
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
                    objItem.setAttribute("VOUCHER_NO","CLOSING");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE","");
                    objItem.setAttribute("SUB_ACCOUNT_CODE",Party_Code);
                    objItem.setAttribute("ACCOUNT_NAME","Closing Balance");
                    objItem.setAttribute("REMARKS"," *** CLOSING BALANCE *** ");
                    
                    if(ClosingBalance<0) {
                        //objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    }
                    else {
                        //objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    //                    objItem.setAttribute("INVOICE_NO","");
                    //                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("MIR_NO","");
                    objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","CLOSING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    rsParty.next();
                }
                
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getLedgerLegacy(String MainCode,String SubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        HashMap List=new HashMap();
        clsVoucher objItem;
        ResultSet rsOpposite;
        
        try {
            boolean Continue=true;
            String strSQL="";
            String Condition="";
            
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            if(!SubCode.trim().equals("")) {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID FROM  L_FIN_VOUCHER_HEADER A,L_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                }
                else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID FROM  L_FIN_VOUCHER_HEADER A,L_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                }
            }
            else {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID FROM  L_FIN_VOUCHER_HEADER A,L_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                }
                else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID FROM  L_FIN_VOUCHER_HEADER A,L_FIN_VOUCHER_DETAIL_EX  B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                }
            }
            
            double ClosingBalance=0;
            String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
            
            if(OnlyApproved) {
                ClosingBalance=clsAccount.getOpeningBalance(MainCode, SubCode, FromDate);
            }
            else {
                ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, SubCode, FromDate);
            }
            
            objItem=new clsVoucher();
            objItem.setAttribute("COMPANY_ID",0);
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
            objItem.setAttribute("VOUCHER_NO","OPENING");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","Opening Balance");
            objItem.setAttribute("REMARKS"," *** OPENING BALANCE ***");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            if(ClosingBalance<0) { //It's a credit entry
                objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                objItem.setAttribute("DEBIT_AMOUNT",0);
            }
            else { //It's a debit entry
                objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                objItem.setAttribute("CREDIT_AMOUNT",0);
            }
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("MIR_NO","");
            objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","OPENING");
            objItem.setAttribute("EXECUTED_PO",0);
            
            List.put(Integer.toString(List.size()+1),objItem);
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String Effect=UtilFunctions.getString(rsTmp,"EFFECT","");
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    
                    
                    Condition="";
                    
                    if(FromAmount>0) {
                        Condition="AND AMOUNT>="+FromAmount+" ";
                    }
                    
                    if(ToAmount>0) {
                        Condition="AND AMOUNT<="+ToAmount+" ";
                    }
                    
                    Continue=true;
                    
                    if(!Condition.trim().equals("")) {
                        
                        Condition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                        if(!data.IsRecordExist("SELECT VOUCHER_NO FROM L_FIN_VOUCHER_DETAIL WHERE "+Condition,FinanceGlobal.FinURL)) {
                            Continue=false;
                        }
                        
                    }
                    
                    if(Continue) {
                        
                        
                        String Description=UtilFunctions.getString(rsTmp,"REMARKS","");
                        int BlockNo=UtilFunctions.getInt(rsTmp,"BLOCK_NO",0);
                        
                        String vMainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                        String vSubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                        
                        
                        boolean validAccountCodes=true;
                        
                        if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                            validAccountCodes=false;
                        }
                        else {
                            
                            /*if(Effect.equals("C")) {
                                ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            }
                            else {
                                ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            }*/
                            
                        }
                        
                        if(BlockNo>0) {
                            //Find Opposite Account
                            rsOpposite=data.getResult("SELECT * FROM L_FIN_VOUCHER_DETAIL_EX A WHERE VOUCHER_NO='"+VoucherNo+"' AND BLOCK_NO="+BlockNo+" AND EFFECT<>'"+Effect+"'",FinanceGlobal.FinURL);
                            
                            rsOpposite.first();
                            if(rsOpposite.getRow()>0) {
                                String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                
                                objItem=new clsVoucher();
                                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                                objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                objItem.setAttribute("REMARKS",Description);
                                objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                                objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsOpposite,"CHEQUE_DATE","0000-00-00")));
                                objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                objItem.setAttribute("SUB_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                String AcName=clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                objItem.setAttribute("ACCOUNT_NAME", AcName );
                                if(Effect.equals("C")) {
                                    ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                    objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    objItem.setAttribute("DEBIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","C");
                                }
                                else {
                                    ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                    objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    objItem.setAttribute("CREDIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","D");
                                }
                                
                                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                
                                //======== Provide Additional Information ==============//
                                String GRNNo=UtilFunctions.getString(rsTmp,"GRN_NO", "");
                                
                                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                                objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                                objItem.setAttribute("MIR_NO","");
                                objItem.setAttribute("MIR_DATE","0000-00-00");
                                objItem.setAttribute("DUE_DATE","0000-00-00");
                                objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsTmp,"INVOICE_NO","")+UtilFunctions.getString(rsTmp,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                
                                if(!objItem.getAttribute("GRN_NO").getString().trim().equals("")) {
                                    
                                    try {
                                        
                                        
                                        int RefCompanyID=UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0);
                                        String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                                        
                                        ResultSet rsMIR=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                        rsMIR.first();
                                        
                                        if(rsMIR.getRow()>0) {
                                            String MIRNo=rsMIR.getString("MIR_NO");
                                            
                                            objItem.setAttribute("MIR_NO",MIRNo);
                                            
                                            rsMIR=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                            rsMIR.first();
                                            
                                            if(rsMIR.getRow()>0) {
                                                String MIRDate=rsMIR.getString("MIR_DATE");
                                                objItem.setAttribute("MIR_DATE",EITLERPGLOBAL.formatDate(MIRDate));
                                            }
                                        }
                                        
                                        objItem.setAttribute("DUE_DATE",calculateDueDate(GRNNo,RefCompanyID));
                                    }
                                    catch(Exception MIR) {
                                        
                                    }
                                    
                                }
                                //======================================================//
                                
                                
                                //============ Check whether PO is executed ===============//
                                int FullyExecutedPO=0;
                                String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                                int CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                                String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                                
                                if(!vPONo.trim().equals("")) {
                                    
                                    ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                                    rsPO.first();
                                    
                                    if(rsPO.getRow()>0) {
                                        if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                            FullyExecutedPO=1;
                                        }
                                    }
                                }
                                
                                objItem.setAttribute("EXECUTED_PO",FullyExecutedPO);
                                //=========================================================//
                                
                                
                                //Add Entry to the list
                                List.put(Integer.toString(List.size()+1),objItem);
                            }
                        }
                        
                        
                    }
                    rsTmp.next();
                }
                
                
                //Adding Final Closing Balance entry
                objItem=new clsVoucher();
                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
                objItem.setAttribute("VOUCHER_NO","CLOSING");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE","");
                objItem.setAttribute("SUB_ACCOUNT_CODE","");
                objItem.setAttribute("ACCOUNT_NAME","Closing Balance");
                objItem.setAttribute("REMARKS"," *** CLOSING BALANCE *** ");
                
                if(ClosingBalance<0) {
                    objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                }
                else {
                    objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                }
                
                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("MIR_NO","");
                objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","CLOSING");
                objItem.setAttribute("EXECUTED_PO",0);
                
                
                List.put(Integer.toString(List.size()+1),objItem);
                
                //Add a Blank Line
                objItem=new clsVoucher();
                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
                objItem.setAttribute("VOUCHER_NO","CLOSING");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE","");
                objItem.setAttribute("SUB_ACCOUNT_CODE","");
                objItem.setAttribute("ACCOUNT_NAME","");
                objItem.setAttribute("REMARKS","**** CLOSING BALANCE ****");
                objItem.setAttribute("DEBIT_AMOUNT",0);
                objItem.setAttribute("CREDIT_AMOUNT",0);
                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("MIR_NO","");
                objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","");
                
                List.put(Integer.toString(List.size()+1),objItem);
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static String getLastClosingDate(String OnDate) {
        String theDate="";
        
        try {
            theDate=data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<='"+OnDate+"' ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            
        }
        
        return theDate;
    }
    
    public static String calculateDueDate(String GRNNo,int CompanyID) {
        try {
            String DueDate="";
            String PartyCode="";
            String MIRNo="";
            String MIRDate="";
            int PaymentDays=0;
            
            String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
            ResultSet rsTmp=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+CompanyID,CompanyURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MIRNo=rsTmp.getString("MIR_NO");
                
                rsTmp=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+CompanyID,CompanyURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    PartyCode=rsTmp.getString("SUPP_ID");
                    MIRDate=rsTmp.getString("MIR_DATE");
                    
                    rsTmp=data.getResult("SELECT PAYMENT_DAYS FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+PartyCode+"' AND COMPANY_ID="+CompanyID,CompanyURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PaymentDays=rsTmp.getInt("PAYMENT_DAYS");
                        
                        if(PaymentDays>0) {
                            DueDate=EITLERPGLOBAL.formatDate(EITLERPGLOBAL.addDaysToDate(MIRDate, PaymentDays, "yyyy-MM-dd"));
                        }
                    }
                }
            }
            return DueDate;
        } catch(Exception e) {
            return "";
        }
    }
    
    public static HashMap getExpensePayableDetail(String PartyName,String InvoiceNo,String ExpenseID) {
        HashMap List=new HashMap();
        
        try {
            String strSQL="";
            
            //            strSQL+="SELECT A.*,IF(SUM(B.AMOUNT) IS NULL,0,SUM(B.AMOUNT)) AS PAID_AMOUNT ";
            //            strSQL+=" FROM ";
            //            strSQL+=" D_FIN_EXPENSE_TRANSACTION A ";
            //            strSQL+=" LEFT JOIN D_FIN_VOUCHER_DETAIL_EX B ON (A.DOC_NO=B.GRN_NO AND B.MODULE_ID=63 AND B.EFFECT='D') ";
            //            strSQL+=" WHERE ";
            //            //strSQL+=" A.APPROVED=1 AND " ;
            //            strSQL+=" A.CANCELLED=0 " ;
            
            strSQL += "SELECT A.* " +
            "FROM D_FIN_EXPENSE_TRANSACTION A  "+
            "WHERE A.CANCELLED=0 AND DOC_DATE >= '2008-04-01' ";
            
            if(!PartyName.trim().equals("")) {
                strSQL+=" AND A.PARTY_NAME LIKE '%"+PartyName+"%' ";
            }
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL+=" AND A.INVOICE_NO = '"+InvoiceNo+"' ";
            }
            
            if(!ExpenseID.trim().equals("")) {
                strSQL+=" AND A.EXPENSE_ID='"+ExpenseID+"' ";
            }
            
            strSQL+=" GROUP BY DOC_NO ";
            //            strSQL+=" HAVING IF(PAID_AMOUNT IS NULL,0,PAID_AMOUNT) < IF(A.INVOICE_AMOUNT IS NULL,0,A.INVOICE_AMOUNT) ";
            
            System.out.println(strSQL);
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    String DocNo = UtilFunctions.getString(rsTmp,"DOC_NO","");
                    double InvAmount = UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0);
                    double PaidAmount = data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE GRN_NO='"+DocNo+"' AND MODULE_ID=63 AND EFFECT = 'D' AND CANCELLED=0 ",FinanceGlobal.FinURL);
                    if (PaidAmount < InvAmount) {
                        
                        clsVoucher objItem=new clsVoucher();
                        
                        objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO",""));
                        objItem.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"DOC_DATE","0000-00-00")));
                        objItem.setAttribute("INVOICE_AMOUNT", UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0));
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("PAID_AMOUNT",UtilFunctions.getDouble(rsTmp,"PAID_AMOUNT",0));
                        objItem.setAttribute("AMOUNT",(UtilFunctions.getDouble(rsTmp,"INVOICE_AMOUNT",0)-UtilFunctions.getDouble(rsTmp,"PAID_AMOUNT",0)));
                        objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                        objItem.setAttribute("MODULE_ID",clsExpenseTransaction.ModuleID);
                        
                        List.put(Integer.toString(List.size()+1),objItem);
                    }
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getPartyPaidVouchers(int CompanyID,String Main_Code,String PartyCode,String GRNNo,String FromDate,String ToDate,String PONo,int POCompanyID,String InvoiceNo) {
        HashMap List=new HashMap();
        try {
            
            String strSQL="SELECT A.*,B.*,SUM(B.AMOUNT) AS TOTAL_AMOUNT FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B " +
            "WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE<>'' AND A.CANCELLED=0 ";
            
            if(!Main_Code.trim().equals("")) {
                strSQL=strSQL+" AND B.MAIN_ACCOUNT_CODE='"+Main_Code+"' ";
            }
            
            if(!PartyCode.trim().equals("")) {
                strSQL=strSQL+" AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ";
            }
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND REF_COMPANY_ID="+POCompanyID+" ";
            }
            
            if(!PONo.trim().equals("")) {
                strSQL=strSQL+" AND B.PO_NO='"+PONo+"' AND REF_COMPANY_ID="+POCompanyID+" ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=strSQL+" AND B.INVOICE_NO='"+InvoiceNo+"' ";
            }
            
            strSQL=strSQL+" GROUP BY A.VOUCHER_NO";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    
                    String MainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                    String SubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                    String VoucherNo=objItem.getAttribute("VOUCHER_NO").getString();
                    
                    double DeductionAmount=data.getDoubleValueFromDB("SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='D' AND IS_DEDUCTION=1",FinanceGlobal.FinURL);
                    
                    objItem.setAttribute("PARTY_CODE",SubCode);
                    objItem.setAttribute("PARTY_NAME",clsPartyMaster.getAccountName(MainCode,SubCode));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"TOTAL_AMOUNT",0)-DeductionAmount);
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                    objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00")));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("DUE_DATE",calculateDueDate(UtilFunctions.getString(rsTmp,"GRN_NO",""),CompanyID));
                    objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                    
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    int FullyExecutedPO=0;
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    
                    if(!vPONo.trim().equals("")) {
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static HashMap getPartyUnpaidVouchers(int CompanyID,String Main_Code,String PartyCode,String GRNNo,String FromDate,String ToDate,String PONo, int POCompanyID,String InvoiceNo) {
        HashMap List=new HashMap();
        try {
            String strSQL="SELECT A.CHEQUE_NO,A.CHEQUE_DATE,A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.* FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' AND B.SUB_ACCOUNT_CODE<>'' AND A.CANCELLED=0 ";
            
            if (!Main_Code.trim().equals("")) {
                strSQL=strSQL+" AND B.MAIN_ACCOUNT_CODE='"+Main_Code+"' ";
            }
            
            if(!PartyCode.trim().equals("")) {
                strSQL=strSQL+" AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' ";
            }
            
            if(!GRNNo.trim().equals("")) {
                strSQL=strSQL+" AND B.GRN_NO='"+GRNNo+"' AND B.REF_COMPANY_ID="+POCompanyID+" ";
            }
            
            if(!PONo.trim().equals("")) {
                strSQL=strSQL+" AND B.PO_NO='"+PONo+"' AND B.REF_COMPANY_ID="+POCompanyID+" ";
            }
            
            if(!InvoiceNo.trim().equals("")) {
                strSQL=strSQL+" AND B.INVOICE_NO='"+InvoiceNo+"' ";
            }
            
            if(!FromDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE>='"+FromDate+"' ";
            }
            
            if(!ToDate.trim().equals("")) {
                strSQL=strSQL+" AND A.VOUCHER_DATE<='"+ToDate+"' ";
            }
            
            strSQL=strSQL+" ORDER BY A.VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsVoucher objItem=new clsVoucher();
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vGRNNo=UtilFunctions.getString(rsTmp,"GRN_NO","");
                    String DueDate=calculateDueDate(vGRNNo,CompanyID);
                    String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                    int FullyExecutedPO=0;
                    
                    if(!vPONo.trim().equals("")) {
                        
                        ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                        rsPO.first();
                        
                        if(rsPO.getRow()>0) {
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }
                    
                    
                    //Check whether full payment has been made to this
                    strSQL="SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND B.EFFECT='D' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.GRN_NO='"+vGRNNo+"' AND B.REF_COMPANY_ID="+CompanyID+" ";
                    double AmountPaid;
                    
                    if(PartyCode.trim().equals("")) {
                        AmountPaid=0;
                    }
                    else {
                        AmountPaid=data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL);
                    }
                    
                    
                    double GRNAmount=clsGRNGen.getGRNTotalAmount(vGRNNo,CompanyURL);
                    
                    if((AmountPaid<GRNAmount)) {
                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                        objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                        objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                        objItem.setAttribute("CHEQUE_NO", UtilFunctions.getString(rsTmp,"CHQUE_NO",""));
                        objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHQUE_DATE","0000-00-00")));
                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                        objItem.setAttribute("AMOUNT_PAID",AmountPaid);
                        objItem.setAttribute("DUE_DATE",DueDate);
                        objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                        objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                        objItem.setAttribute("REF_COMPANY_ID",UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0));
                        objItem.setAttribute("FULLY_EXECUTED_PO",FullyExecutedPO);
                        
                        String MainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                        String SubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                        
                        
                        objItem.setAttribute("PARTY_CODE",SubCode);
                        objItem.setAttribute("PARTY_NAME",clsPartyMaster.getAccountName(MainCode,SubCode));
                        List.put(Integer.toString(List.size()+1),objItem);
                    }
                    
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static double getDebtorsUnpaidAmount(String PartyCode,String AsOnDate) {
        try {
            String MainCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
            double ClosingBalance= getAvailableClosingBalance(MainCode, PartyCode, AsOnDate);
            
            if(ClosingBalance<0) {
                return 0;
            } else {
                return ClosingBalance;
            }
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static HashMap getPartyAdvReceivedDetail(int CompanyID,String MainCode,String PartyCode,String AgentAlpha) {
        HashMap List=new HashMap();
        try {
            String strSQL="SELECT A.COMPANY_ID,B.REF_COMPANY_ID,A.APPROVED,A.VOUCHER_NO,A.VOUCHER_DATE,B.SR_NO, B.* " +
            "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.EFFECT='C' " +
            "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' " +
            "AND (B.INVOICE_NO='' OR B.INVOICE_NO LIKE 'DUM%') AND B.MODULE_ID<>"+clsVoucher.DebitNoteModuleID+" AND B.GRN_NO='' " +
            "AND A.APPROVED=1 AND A.CANCELLED=0 AND A.VOUCHER_TYPE IN (6,7,8,9) " +
            "AND (B.MATCHED=0 OR B.MATCHED IS NULL) " +
            "ORDER BY A.VOUCHER_DATE ";
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                    String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    String vInvoiceNo=UtilFunctions.getString(rsTmp,"INVOICE_NO","");
                    String vInvoiceDate=UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00");
                    int SrNo = UtilFunctions.getInt(rsTmp,"SR_NO",0);
                    int FullyExecutedPO=0;
                    
                    clsVoucher objItem = new clsVoucher();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                    objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                    objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                    objItem.setAttribute("AMOUNT", UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("REMARKS", UtilFunctions.getString(rsTmp,"REMARKS",""));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                    objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                    objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                    objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                    objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                    objItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VALUE_DATE","0000-00-00")));
                    objItem.setAttribute("APPROVED",UtilFunctions.getInt(rsTmp,"APPROVED",0));
                    objItem.setAttribute("MODULE_ID",UtilFunctions.getInt(rsTmp,"MODULE_ID",0));
                    objItem.setAttribute("FULLY_EXECUTED_PO",false);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    
                    rsTmp.next();
                }
            }
            
        } catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getMyLedger(String MainCode,String FromSubCode,String ToSubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        // Mrugesh
        HashMap List=new HashMap();
        clsVoucherLedgerItem objItem;
        ResultSet rsOpposite;
        String PartyCondition="";
        boolean OnlyMainCode=false;
        double PartyClosingBalance=0;
        try {
            boolean Continue=true;
            String strParty="";
            String Condition="";
            int VoucherType=0;
            double LineAmount=0;
            
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            if(!FromSubCode.trim().equals("") && !ToSubCode.trim().equals("")) {
                PartyCondition = " AND PARTY_CODE>='"+FromSubCode+"' AND PARTY_CODE<='"+ToSubCode+"' ORDER BY PARTY_CODE";
            } else if(!FromSubCode.trim().equals("") && ToSubCode.trim().equals("")){
                PartyCondition = " AND PARTY_CODE='"+FromSubCode+"' ORDER BY PARTY_CODE";
            } else if(FromSubCode.trim().equals("") && !ToSubCode.trim().equals("")){
                PartyCondition = " AND PARTY_CODE='"+ToSubCode+"' ORDER BY PARTY_CODE";
            } else {
                PartyCondition = " ORDER BY PARTY_CODE";
                OnlyMainCode=true;
            }
            
            strParty = "SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE<>'' "+PartyCondition;
            double MainClosingBalance=0;
            if(OnlyMainCode) {
                String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
                
                if(OnlyApproved) {
                    MainClosingBalance=clsAccount.getOpeningBalance(MainCode, "", FromDate);
                } else {
                    MainClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, "", FromDate);
                }
                
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("COMPANY_ID",0);
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","1");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","OPENING BALANCE-" + MainCode);
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                if(MainClosingBalance<0) { //It's a credit entry
                    objItem.setAttribute("CREDIT_AMOUNT",Math.abs(MainClosingBalance));
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                } else { //It's a debit entry
                    objItem.setAttribute("DEBIT_AMOUNT",MainClosingBalance);
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                }
                objItem.setAttribute("CLOSING_BALANCE",MainClosingBalance);
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("MIR_NO","");
                objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","OPENING");
                objItem.setAttribute("EXECUTED_PO",0);
                
                List.put(Integer.toString(List.size()+1),objItem);
            }
            
            ResultSet rsParty=data.getResult(strParty,FinanceGlobal.FinURL);
            rsParty.first();
            if(rsParty.getRow()>0) {
                while(!rsParty.isAfterLast()) {
                    String PartyCode=UtilFunctions.getString(rsParty,"PARTY_CODE","");
                    PartyClosingBalance = 0;
                    double ClosingBalance=0;
                    String VoucherDate = "";
                    int VoucherMonth = 0;
                    boolean firstVoucher = false;
                    String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
                    
                    if(OnlyApproved) {
                        ClosingBalance=clsAccount.getOpeningBalance(MainCode, PartyCode, FromDate);
                    } else {
                        ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, PartyCode, FromDate);
                    }
                    
                    String SQLPartyTran = "";
                    if(OnlyApproved) {
                        SQLPartyTran="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID " +
                        "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO"; //AND A.BOOK_CODE<>'17'
                    } else {
                        SQLPartyTran="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID " +
                        "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                    }
                    
                    ResultSet rsPartyTran = data.getResult(SQLPartyTran,FinanceGlobal.FinURL);
                    rsPartyTran.first();
                    
                    if(ClosingBalance == 0 ) {
                        if(rsPartyTran.getRow() > 0) {
                            //do nothing
                        } else {
                            rsParty.next();
                            continue;
                        }
                    }
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("COMPANY_ID",0);
                    objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                    objItem.setAttribute("VOUCHER_NO","");
                    objItem.setAttribute("LEGACY_NO","");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                    objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                    objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,PartyCode));
                    objItem.setAttribute("REMARKS","OPENING BALANCE");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    if(ClosingBalance<0) { //It's a credit entry
                        objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    } else { //It's a debit entry
                        objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("MIR_NO","");
                    objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","OPENING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    rsPartyTran.first();
                    if(rsPartyTran.getRow() > 0 ) {
                        firstVoucher=true;
                        while(!rsPartyTran.isAfterLast()) {
                            
                            String Effect=UtilFunctions.getString(rsPartyTran,"EFFECT","");
                            String VoucherNo=UtilFunctions.getString(rsPartyTran,"VOUCHER_NO","");
                            String pVoucherNo="";
                            
                            if(VoucherNo.length()>11 && VoucherNo.startsWith("M")) {
                                pVoucherNo = VoucherNo.substring(0,1)+VoucherNo.substring(9);
                            } else {
                                pVoucherNo=VoucherNo;
                            }
                            VoucherType=UtilFunctions.getInt(rsPartyTran,"VOUCHER_TYPE",0);
                            LineAmount=UtilFunctions.getDouble(rsPartyTran,"AMOUNT",0);
                            VoucherDate = UtilFunctions.getString(rsPartyTran,"VOUCHER_DATE","0000-00-00");
                            if(firstVoucher) {
                                VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                                firstVoucher=false;
                            } else {
                                if(EITLERPGLOBAL.getMonth(VoucherDate)!=VoucherMonth) {
                                    List.put(Integer.toString(List.size()+1),getMonthClosingEntry(PartyCode,ClosingBalance));
                                    VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                                }
                            }
                            String AmountCondition="";
                            
                            if(FromAmount>0) {
                                AmountCondition="AND AMOUNT>="+FromAmount+" ";
                            }
                            
                            if(ToAmount>0) {
                                AmountCondition="AND AMOUNT<="+ToAmount+" ";
                            }
                            
                            Continue=true;
                            
                            if(!AmountCondition.trim().equals("")) {
                                AmountCondition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                                if(!data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE "+AmountCondition,FinanceGlobal.FinURL)) {
                                    Continue=false;
                                }
                            }
                            
                            if(Continue) {
                                
                                String Description=UtilFunctions.getString(rsPartyTran,"REMARKS","").replace('\n',' ');
                                if(Description.length()>125) {
                                    Description=Description.substring(0,125);
                                }
                                int BlockNo=UtilFunctions.getInt(rsPartyTran,"BLOCK_NO",0);
                                
                                String vMainCode=UtilFunctions.getString(rsPartyTran,"MAIN_ACCOUNT_CODE","");
                                String vSubCode=UtilFunctions.getString(rsPartyTran,"SUB_ACCOUNT_CODE","");
                                
                                
                                boolean validAccountCodes=true;
                                
                                if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                                    validAccountCodes=false;
                                } else {
                                    /*if(Effect.equals("C")) {
                                        ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsParty,"AMOUNT",0));
                                    } else {
                                        ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsParty,"AMOUNT",0));
                                    }*/
                                }
                                
                                if(BlockNo>0) {
                                    //Find Opposite Account
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                        //Added by Mrugesh 07/02/2011 -- start
                                        if(VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                            //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' AND AMOUNT= "+LineAmount+" ORDER BY SR_NO",FinanceGlobal.FinURL);
                                            rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                        } else {
                                            rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                        }
                                        //Added by Mrugesh 07/02/2011 -- end
                                        //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT<>'"+Effect+"' ORDER BY SR_NO",FinanceGlobal.FinURL); //AND AMOUNT="+LineAmount+"
                                    } else {
                                        rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND BLOCK_NO="+BlockNo+" AND EFFECT<>'"+Effect+"'",FinanceGlobal.FinURL);
                                    }
                                    
                                    rsOpposite.first();
                                    if(rsOpposite.getRow()>0) {
                                        String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                        
                                        
                                        objItem = new clsVoucherLedgerItem();
                                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsPartyTran,"COMPANY_ID",0));
                                        
                                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"VOUCHER_DATE","0000-00-00")));
                                        //objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                        objItem.setAttribute("VOUCHER_NO",pVoucherNo);
                                        objItem.setAttribute("LEGACY_NO",UtilFunctions.getString(rsPartyTran,"LEGACY_NO",""));
                                        objItem.setAttribute("REMARKS",Description);
                                        objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsPartyTran,"BOOK_CODE",""));
                                        objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                        objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                        
                                        //CHANGE AS ON 24/06/2009 -- START
                                        //String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='"+Effect+"'",FinanceGlobal.FinURL);
                                        //objItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                                        objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                                        //CHANGE AS ON 24/06/2009 -- END
                                        
                                        
                                        String AcName=clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                        objItem.setAttribute("ACCOUNT_NAME", AcName );
                                        if(Effect.equals("C")) {
                                            
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL|| VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                                ClosingBalance-=LineAmount;
                                                objItem.setAttribute("CREDIT_AMOUNT",LineAmount);
                                                MainClosingBalance-=LineAmount;
                                                
                                            }
                                            else {
                                                ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                                MainClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                            }
                                            
                                            
                                            objItem.setAttribute("DEBIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","C");
                                        }
                                        else {
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL|| VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                                ClosingBalance+=LineAmount;
                                                objItem.setAttribute("DEBIT_AMOUNT",LineAmount);
                                                MainClosingBalance+=LineAmount;
                                            }
                                            else {
                                                ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                                MainClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                            }
                                            
                                            objItem.setAttribute("CREDIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","D");
                                        }
                                        
                                        objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                        
                                        //======== Provide Additional Information ==============//
                                        String GRNNo=UtilFunctions.getString(rsPartyTran,"GRN_NO", "");
                                        
                                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsPartyTran,"INVOICE_NO",""));
                                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"INVOICE_DATE","0000-00-00")));
                                        //objItem.setAttribute("LINK_NO",clsSalesInvoice.getAgentAlphaSrNo(UtilFunctions.getString(rsPartyTran,"INVOICE_NO",""), UtilFunctions.getString(rsPartyTran,"INVOICE_DATE","0000-00-00")));
                                        objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsPartyTran,"LINK_NO",""));
                                        objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsPartyTran,"CHEQUE_NO",""));
                                        objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"CHEQUE_DATE","0000-00-00")));
                                        
                                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsPartyTran,"GRN_NO",""));
                                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"GRN_DATE","0000-00-00")));
                                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsPartyTran,"PO_NO",""));
                                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"PO_DATE","0000-00-00")));
                                        objItem.setAttribute("MIR_NO","");
                                        objItem.setAttribute("MIR_DATE","0000-00-00");
                                        objItem.setAttribute("DUE_DATE","0000-00-00");
                                        objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsPartyTran,"INVOICE_NO","")+UtilFunctions.getString(rsPartyTran,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                        
                                        List.put(Integer.toString(List.size()+1),objItem);
                                    }
                                }
                            }
                            
                            rsPartyTran.next();
                        }
                    }
                    //Adding Final Closing Balance entry
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                    objItem.setAttribute("VOUCHER_NO","");
                    objItem.setAttribute("LEGACY_NO","");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                    objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                    objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,PartyCode));
                    objItem.setAttribute("REMARKS","CLOSING BALANCE");
                    
                    if(ClosingBalance<0) {
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    } else {
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    //                    objItem.setAttribute("INVOICE_NO","");
                    //                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("MIR_NO","");
                    objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","CLOSING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    rsParty.next();
                }
            } else {
                
            }
            
            if(OnlyMainCode) {
                //Add a Blank Line
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","2");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","");
                objItem.setAttribute("DEBIT_AMOUNT",0);
                objItem.setAttribute("CREDIT_AMOUNT",0);
                objItem.setAttribute("CLOSING_BALANCE",0);
                objItem.setAttribute("MIR_NO","");
                objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","");
                
                List.put(Integer.toString(List.size()+1),objItem);
                
                //Adding Final Closing Balance entry
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","2");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","CLOSING BALANCE-"+MainCode);
                
                if(MainClosingBalance<0) {
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                } else {
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                }
                objItem.setAttribute("CLOSING_BALANCE",MainClosingBalance);
                
                objItem.setAttribute("MIR_NO","");
                objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","");
                objItem.setAttribute("EXECUTED_PO",0);
                
                List.put(Integer.toString(List.size()+1),objItem);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getMyLedgerSummary(String MainCode, String FromDate, String ToDate, boolean OnlyApproved) {
        HashMap List = new HashMap();
        try {
            
            clsVoucherLedgerItemSummary objItem;
            String strRecords = "SELECT A.VOUCHER_NO,A.VOUCHER_DATE,A.BOOK_CODE,SUBSTRING(A.REMARKS,1,80) AS REMARKS,B.AMOUNT,B.EFFECT, " +
            "C.BOOK_TYPE,A.CHEQUE_NO,A.CHEQUE_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.PO_NO,B.PO_DATE " +
            "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B, D_FIN_BOOK_MASTER C " +
            "WHERE A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND A.VOUCHER_NO=B.VOUCHER_NO AND A.BOOK_CODE=C.BOOK_CODE AND SUBSTRING(B.MAIN_ACCOUNT_CODE,1,6)='"+MainCode+"' " +
            "AND B.SUB_ACCOUNT_CODE='' AND C.BOOK_TYPE IN (1,2) " +
            "ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO,B.AMOUNT";
            
            ResultSet rsRecords = data.getResult(strRecords,FinanceGlobal.FinURL);
            rsRecords.first();
            boolean firstVoucher = true;
            int VoucherMonth=0;
            String VoucherDate = FromDate;
            int Year  = EITLERPGLOBAL.getYear(VoucherDate);
            double ClosingBalance=0,CreditTotal=0,DebitTotal=0;
            int Counter = 0;
            String StartDate=Year+"-"+EITLERPGLOBAL.Padding(Integer.toString(EITLERPGLOBAL.getMonth(VoucherDate)),2,"0")+"-01";
            String EndDate=data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
            
            if(rsRecords.getRow() > 0 ) {
                while(!rsRecords.isAfterLast()) {
                    
                    VoucherDate = UtilFunctions.getString(rsRecords, "VOUCHER_DATE","0000-00-00");
                    Year  = EITLERPGLOBAL.getYear(VoucherDate);
                    
                    if(firstVoucher) {
                        VoucherMonth = EITLERPGLOBAL.getMonth(FromDate);
                        Year  = EITLERPGLOBAL.getYear(FromDate);
                        Counter++;
                        ClosingBalance = clsAccount.getOpeningBalance(MainCode, "", FromDate);
                        objItem = new clsVoucherLedgerItemSummary();
                        objItem.setAttribute("VOUCHER_NO","");
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
                        objItem.setAttribute("BOOK_CODE","");
                        objItem.setAttribute("CHEQUE_NO","");
                        objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                        objItem.setAttribute("INVOICE_NO","");
                        objItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objItem.setAttribute("PO_NO","");
                        objItem.setAttribute("PO_DATE","0000-00-00");
                        objItem.setAttribute("REMARKS","OPENING BALANCE");
                        objItem.setAttribute("EFFECT","");
                        if(ClosingBalance < 0) {
                            objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                            objItem.setAttribute("DEBIT_AMOUNT",0);
                            CreditTotal+=Math.abs(ClosingBalance);
                        } else {
                            objItem.setAttribute("CREDIT_AMOUNT",0);
                            objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                            DebitTotal+=ClosingBalance;
                        }
                        objItem.setAttribute("CLOSING_BALANCE",0);
                        List.put(Integer.toString(Counter),objItem);
                        
                        firstVoucher = false;
                        
                        VoucherDate = UtilFunctions.getString(rsRecords, "VOUCHER_DATE","0000-00-00");
                        Year  = EITLERPGLOBAL.getYear(VoucherDate);
                        
                    } else {
                        StartDate = Year+"-"+EITLERPGLOBAL.Padding(Integer.toString(VoucherMonth),2,"0")+"-01";
                        EndDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
                        if(EITLERPGLOBAL.getMonth(VoucherDate)!=VoucherMonth) {
                            //Book type - 3
                            if(java.sql.Date.valueOf(EndDate).after(java.sql.Date.valueOf(ToDate))) {
                                EndDate = ToDate;
                            }
                            String strGRecords = "SELECT A.BOOK_CODE,SUM(B.AMOUNT) AS TAMOUNT,B.EFFECT " +
                            "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B , D_FIN_BOOK_MASTER C WHERE MONTH(A.VOUCHER_DATE)="+VoucherMonth+" " +
                            "AND YEAR(A.VOUCHER_DATE)="+Year+" AND VOUCHER_DATE>='"+StartDate+"' AND VOUCHER_DATE<='"+EndDate+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO " +
                            "AND A.BOOK_CODE=C.BOOK_CODE AND SUBSTRING(B.MAIN_ACCOUNT_CODE,1,6)='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='' AND C.BOOK_TYPE=3 " +
                            "GROUP BY C.BOOK_CODE,B.EFFECT " +
                            "ORDER BY C.BOOK_CODE,B.EFFECT";
                            ResultSet rsGRecords = data.getResult(strGRecords,FinanceGlobal.FinURL);
                            rsGRecords.first();
                            if(rsGRecords.getRow() > 0) {
                                while(!rsGRecords.isAfterLast()) {
                                    
                                    Counter++;
                                    objItem = new clsVoucherLedgerItemSummary();
                                    objItem.setAttribute("VOUCHER_NO","");
                                    objItem.setAttribute("VOUCHER_DATE",EndDate);
                                    objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsGRecords, "BOOK_CODE", ""));
                                    objItem.setAttribute("CHEQUE_NO","");
                                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                                    objItem.setAttribute("INVOICE_NO","");
                                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                                    objItem.setAttribute("PO_NO","");
                                    objItem.setAttribute("PO_DATE","0000-00-00");
                                    objItem.setAttribute("REMARKS","SUMMARY FROM DAY BOOK");
                                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsGRecords, "EFFECT", ""));
                                    if(UtilFunctions.getString(rsGRecords, "EFFECT", "").equals("C")) {
                                        objItem.setAttribute("CREDIT_AMOUNT", UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                                        objItem.setAttribute("DEBIT_AMOUNT",0);
                                        CreditTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                                    } else {
                                        objItem.setAttribute("CREDIT_AMOUNT",0);
                                        objItem.setAttribute("DEBIT_AMOUNT", UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                                        DebitTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                                    }
                                    objItem.setAttribute("CLOSING_BALANCE",0);
                                    List.put(Integer.toString(Counter),objItem);
                                    
                                    rsGRecords.next();
                                }
                            }
                            
                            Counter++;
                            objItem = new clsVoucherLedgerItemSummary();
                            objItem.setAttribute("VOUCHER_NO","");
                            objItem.setAttribute("VOUCHER_DATE",EndDate);
                            objItem.setAttribute("BOOK_CODE","");
                            objItem.setAttribute("CHEQUE_NO","");
                            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                            objItem.setAttribute("INVOICE_NO","");
                            objItem.setAttribute("INVOICE_DATE","0000-00-00");
                            objItem.setAttribute("PO_NO","");
                            objItem.setAttribute("PO_DATE","0000-00-00");
                            objItem.setAttribute("REMARKS","CLOSING BALANCE");
                            objItem.setAttribute("EFFECT","");
                            objItem.setAttribute("CREDIT_AMOUNT",CreditTotal);
                            objItem.setAttribute("DEBIT_AMOUNT",DebitTotal);
                            ClosingBalance = DebitTotal - CreditTotal;
                            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                            List.put(Integer.toString(Counter),objItem);
                            
                            Counter++;
                            objItem = new clsVoucherLedgerItemSummary();
                            objItem.setAttribute("VOUCHER_NO","");
                            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.addDaysToDate(EndDate,1,"yyyy-MM-dd"));
                            objItem.setAttribute("BOOK_CODE","");
                            objItem.setAttribute("CHEQUE_NO","");
                            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                            objItem.setAttribute("INVOICE_NO","");
                            objItem.setAttribute("INVOICE_DATE","0000-00-00");
                            objItem.setAttribute("PO_NO","");
                            objItem.setAttribute("PO_DATE","0000-00-00");
                            objItem.setAttribute("REMARKS","OPENING BALANCE");
                            objItem.setAttribute("EFFECT","");
                            //ClosingBalance = DebitTotal - CreditTotal;
                            if(ClosingBalance < 0 ) {
                                objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                                objItem.setAttribute("DEBIT_AMOUNT",0);
                            } else {
                                objItem.setAttribute("CREDIT_AMOUNT",0);
                                objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                            }
                            objItem.setAttribute("CLOSING_BALANCE",0);
                            List.put(Integer.toString(Counter),objItem);
                            
                            VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                            Year = EITLERPGLOBAL.getYear(VoucherDate);
                        }
                    }
                    
                    
                    Counter++;
                    objItem = new clsVoucherLedgerItemSummary();
                    objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsRecords, "VOUCHER_NO", ""));
                    objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsRecords, "VOUCHER_DATE","0000-00-00"));
                    objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsRecords, "BOOK_CODE", ""));
                    objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsRecords, "CHEQUE_NO", ""));
                    objItem.setAttribute("CHEQUE_DATE",UtilFunctions.getString(rsRecords, "CHEQUE_DATE","0000-00-00"));
                    objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsRecords, "INVOICE_NO", ""));
                    objItem.setAttribute("INVOICE_DATE",UtilFunctions.getString(rsRecords, "INVOICE_DATE","0000-00-00"));
                    objItem.setAttribute("PO_NO",UtilFunctions.getString(rsRecords, "PO_NO", ""));
                    objItem.setAttribute("PO_DATE",UtilFunctions.getString(rsRecords, "PO_DATE","0000-00-00"));
                    objItem.setAttribute("REMARKS",UtilFunctions.getString(rsRecords, "REMARKS", ""));
                    objItem.setAttribute("EFFECT",UtilFunctions.getString(rsRecords, "EFFECT", ""));
                    if(UtilFunctions.getString(rsRecords, "EFFECT", "").equals("C")) {
                        objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsRecords, "AMOUNT", 0));
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                        CreditTotal+=UtilFunctions.getDouble(rsRecords, "AMOUNT", 0);
                    } else {
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                        objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsRecords, "AMOUNT", 0));
                        DebitTotal+=UtilFunctions.getDouble(rsRecords, "AMOUNT", 0);
                    }
                    objItem.setAttribute("CLOSING_BALANCE",0);
                    List.put(Integer.toString(Counter),objItem);
                    
                    rsRecords.next();
                }
            } else {
                Counter++;
                ClosingBalance = clsAccount.getOpeningBalance(MainCode, "", FromDate);
                objItem = new clsVoucherLedgerItemSummary();
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("REMARKS","OPENING BALANCE");
                objItem.setAttribute("EFFECT","");
                if(ClosingBalance < 0) {
                    objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                    CreditTotal+=Math.abs(ClosingBalance);
                } else {
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                    objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                    DebitTotal+=ClosingBalance;
                }
                objItem.setAttribute("CLOSING_BALANCE",0);
                List.put(Integer.toString(Counter),objItem);
            }
            //======================================================
            if(java.sql.Date.valueOf(EndDate).after(java.sql.Date.valueOf(ToDate))) {
                EndDate = ToDate;
            }
            
            
            if(firstVoucher) {
                VoucherMonth = EITLERPGLOBAL.getMonth(FromDate);
                Year  = EITLERPGLOBAL.getYear(FromDate);
                while(java.sql.Date.valueOf(StartDate).before(java.sql.Date.valueOf(ToDate))) {
                    String strGRecords = "SELECT A.BOOK_CODE,SUM(B.AMOUNT) AS TAMOUNT,B.EFFECT " +
                    "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B , D_FIN_BOOK_MASTER C WHERE MONTH(A.VOUCHER_DATE)="+VoucherMonth+" " +
                    "AND YEAR(A.VOUCHER_DATE)="+Year+" AND VOUCHER_DATE>='"+StartDate+"' AND VOUCHER_DATE<='"+EndDate+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO " +
                    "AND A.BOOK_CODE=C.BOOK_CODE AND SUBSTRING(B.MAIN_ACCOUNT_CODE,1,6)='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='' AND C.BOOK_TYPE=3 " +
                    "GROUP BY C.BOOK_CODE,B.EFFECT " +
                    "ORDER BY C.BOOK_CODE,B.EFFECT ";
                    ResultSet rsGRecords = data.getResult(strGRecords,FinanceGlobal.FinURL);
                    rsGRecords.first();
                    if(rsGRecords.getRow() > 0) {
                        while(!rsGRecords.isAfterLast()) {
                            
                            Counter++;
                            objItem = new clsVoucherLedgerItemSummary();
                            objItem.setAttribute("VOUCHER_NO","");
                            objItem.setAttribute("VOUCHER_DATE",EndDate);
                            objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsGRecords, "BOOK_CODE", ""));
                            objItem.setAttribute("CHEQUE_NO","");
                            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                            objItem.setAttribute("INVOICE_NO","");
                            objItem.setAttribute("INVOICE_DATE","0000-00-00");
                            objItem.setAttribute("PO_NO","");
                            objItem.setAttribute("PO_DATE","0000-00-00");
                            objItem.setAttribute("REMARKS","SUMMARY FROM DAY BOOK");
                            objItem.setAttribute("EFFECT",UtilFunctions.getString(rsGRecords, "EFFECT", ""));
                            if(UtilFunctions.getString(rsGRecords, "EFFECT", "").equals("C")) {
                                objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                                CreditTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                                objItem.setAttribute("DEBIT_AMOUNT",0);
                            } else {
                                objItem.setAttribute("CREDIT_AMOUNT",0);
                                objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                                DebitTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                            }
                            objItem.setAttribute("CLOSING_BALANCE",0);
                            List.put(Integer.toString(Counter),objItem);
                            
                            rsGRecords.next();
                        }
                    }
                    
                    Counter++;
                    objItem = new clsVoucherLedgerItemSummary();
                    objItem.setAttribute("VOUCHER_NO","");
                    objItem.setAttribute("VOUCHER_DATE",EndDate);
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("REMARKS","CLOSING BALANCE");
                    objItem.setAttribute("EFFECT","");
                    objItem.setAttribute("CREDIT_AMOUNT",CreditTotal);
                    objItem.setAttribute("DEBIT_AMOUNT",DebitTotal);
                    ClosingBalance = DebitTotal - CreditTotal;
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    List.put(Integer.toString(Counter),objItem);
                    if(!EndDate.equals(ToDate)) {
                        Counter++;
                        objItem = new clsVoucherLedgerItemSummary();
                        objItem.setAttribute("VOUCHER_NO","");
                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.addDaysToDate(EndDate,1,"yyyy-MM-dd"));
                        objItem.setAttribute("BOOK_CODE","");
                        objItem.setAttribute("CHEQUE_NO","");
                        objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                        objItem.setAttribute("INVOICE_NO","");
                        objItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objItem.setAttribute("PO_NO","");
                        objItem.setAttribute("PO_DATE","0000-00-00");
                        objItem.setAttribute("REMARKS","OPENING BALANCE");
                        objItem.setAttribute("EFFECT","");
                        //ClosingBalance = DebitTotal - CreditTotal;
                        if(ClosingBalance < 0 ) {
                            objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                            objItem.setAttribute("DEBIT_AMOUNT",0);
                        } else {
                            objItem.setAttribute("CREDIT_AMOUNT",0);
                            objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                        }
                        objItem.setAttribute("CLOSING_BALANCE",0);
                        List.put(Integer.toString(Counter),objItem);
                    }
                    
                    VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                    Year = EITLERPGLOBAL.getYear(VoucherDate);
                    
                    StartDate = clsCalcInterest.addMonthToDate(StartDate, 1);
                    EndDate = data.getStringValueFromDB("SELECT LAST_DAY('"+StartDate+"') FROM DUAL");
                    VoucherMonth = EITLERPGLOBAL.getMonth(StartDate);
                    Year  = EITLERPGLOBAL.getYear(StartDate);
                }
            } else {
                String strGRecords = "SELECT A.BOOK_CODE,SUM(B.AMOUNT) AS TAMOUNT,B.EFFECT " +
                "FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B , D_FIN_BOOK_MASTER C WHERE MONTH(A.VOUCHER_DATE)="+VoucherMonth+" " +
                "AND YEAR(A.VOUCHER_DATE)="+Year+" AND VOUCHER_DATE>='"+StartDate+"' AND VOUCHER_DATE<='"+EndDate+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.VOUCHER_NO=B.VOUCHER_NO " +
                "AND A.BOOK_CODE=C.BOOK_CODE AND SUBSTRING(B.MAIN_ACCOUNT_CODE,1,6)='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='' AND C.BOOK_TYPE=3 " +
                "GROUP BY C.BOOK_CODE,B.EFFECT " +
                "ORDER BY C.BOOK_CODE,B.EFFECT ";
                ResultSet rsGRecords = data.getResult(strGRecords,FinanceGlobal.FinURL);
                rsGRecords.first();
                if(rsGRecords.getRow() > 0) {
                    while(!rsGRecords.isAfterLast()) {
                        
                        Counter++;
                        objItem = new clsVoucherLedgerItemSummary();
                        objItem.setAttribute("VOUCHER_NO","");
                        objItem.setAttribute("VOUCHER_DATE",EndDate);
                        objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsGRecords, "BOOK_CODE", ""));
                        objItem.setAttribute("CHEQUE_NO","");
                        objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                        objItem.setAttribute("INVOICE_NO","");
                        objItem.setAttribute("INVOICE_DATE","0000-00-00");
                        objItem.setAttribute("PO_NO","");
                        objItem.setAttribute("PO_DATE","0000-00-00");
                        objItem.setAttribute("REMARKS","SUMMARY FROM DAY BOOK");
                        objItem.setAttribute("EFFECT",UtilFunctions.getString(rsGRecords, "EFFECT", ""));
                        if(UtilFunctions.getString(rsGRecords, "EFFECT", "").equals("C")) {
                            objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                            CreditTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                            objItem.setAttribute("DEBIT_AMOUNT",0);
                        } else {
                            objItem.setAttribute("CREDIT_AMOUNT",0);
                            objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0));
                            DebitTotal+=UtilFunctions.getDouble(rsGRecords, "TAMOUNT", 0);
                        }
                        objItem.setAttribute("CLOSING_BALANCE",0);
                        List.put(Integer.toString(Counter),objItem);
                        
                        rsGRecords.next();
                    }
                }
                
                Counter++;
                objItem = new clsVoucherLedgerItemSummary();
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("VOUCHER_DATE",EndDate);
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("REMARKS","CLOSING BALANCE");
                objItem.setAttribute("EFFECT","");
                objItem.setAttribute("CREDIT_AMOUNT",CreditTotal);
                objItem.setAttribute("DEBIT_AMOUNT",DebitTotal);
                ClosingBalance = DebitTotal - CreditTotal;
                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                
                List.put(Integer.toString(Counter),objItem);
            }
            
            //======================================================
        }catch(Exception e) {
            e.printStackTrace();
        }
        return List;
    }
    
    private static clsVoucherLedgerItem getMonthClosingEntry(String pCode,double pBalance) {
        clsVoucherLedgerItem objBlankItem = new clsVoucherLedgerItem();
        
        objBlankItem.setAttribute("VOUCHER_DATE","0000-00-00");
        objBlankItem.setAttribute("VOUCHER_NO","");
        objBlankItem.setAttribute("LEGACY_DATE","0000-00-00");
        objBlankItem.setAttribute("LEGACY_NO","");
        objBlankItem.setAttribute("BOOK_CODE","");
        objBlankItem.setAttribute("INVOICE_NO","");
        objBlankItem.setAttribute("INVOICE_DATE","0000-00-00");
        objBlankItem.setAttribute("PO_NO","");
        objBlankItem.setAttribute("PO_DATE","0000-00-00");
        objBlankItem.setAttribute("CHEQUE_NO","");
        objBlankItem.setAttribute("CHEQUE_DATE","0000-00-00");
        objBlankItem.setAttribute("BANK_NAME","");
        objBlankItem.setAttribute("MAIN_ACCOUNT_CODE","");
        objBlankItem.setAttribute("SUB_ACCOUNT_CODE",pCode);
        objBlankItem.setAttribute("ACCOUNT_NAME","");
        objBlankItem.setAttribute("REMARKS","*** CR/DR BALANCE ***");
        objBlankItem.setAttribute("DEBIT_AMOUNT",0);
        objBlankItem.setAttribute("CREDIT_AMOUNT",0);
        objBlankItem.setAttribute("CLOSING_BALANCE",pBalance);
        objBlankItem.setAttribute("INVOICE_NO","");
        objBlankItem.setAttribute("INVOICE_DATE","0000-00-00");
        objBlankItem.setAttribute("MIR_NO","");
        objBlankItem.setAttribute("MIR_DATE","0000-00-00");
        objBlankItem.setAttribute("PO_NO","");
        objBlankItem.setAttribute("PO_DATE","0000-00-00");
        objBlankItem.setAttribute("DUE_DATE","0000-00-00");
        objBlankItem.setAttribute("GROUP","");
        return objBlankItem;
    }
    
    /*public static double get09AmountByVoucher(String VoucherNo,String MainCode,String SubCode, double Amount,int VoucherSrNo) {
        double AdvAmount=0;
        try {
            String strSQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
            "AND B.GRN_NO='"+VoucherNo+"' AND B.EFFECT='D' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND REF_VOUCHER_NO="+VoucherSrNo + " " +
            "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.CANCELLED=0 ";
            AdvAmount = EITLERPGLOBAL.round(Amount - data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL),2);
        } catch(Exception e) {
            return AdvAmount;
        }
        return AdvAmount;
    }*/
    
    public static double get09AmountByVoucher(String VoucherNo,String MainCode,String SubCode, double Amount) {
        double AdvAmount=0;
        try {
            String strSQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO " +
            "AND B.GRN_NO='"+VoucherNo+"' AND B.EFFECT='D' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
            "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=0 AND A.CANCELLED=0 ";
            AdvAmount = EITLERPGLOBAL.round(Amount - data.getDoubleValueFromDB(strSQL, FinanceGlobal.FinURL),2);
        } catch(Exception e) {
            return AdvAmount;
        }
        return AdvAmount;
    }
    
    public static double get09AmountByParty(String MainCode,String SubCode,String InvoiceDate) {
        double availableAmount=0;
        double Amount = 0;
        String Condition ="";
        ResultSet rsVoucher = null;
        try {
            if(!InvoiceDate.equals("")) {
                Condition = " AND A.VOUCHER_DATE<='"+InvoiceDate+"' ";
            }
            String strSQL = "SELECT A.VOUCHER_NO,ROUND(B.AMOUNT,2) AS AMOUNT FROM D_FIN_VOUCHER_HEADER A, D_FIN_VOUCHER_DETAIL B " + //,B.SR_NO
            "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_TYPE IN (6,7,8,9) AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
            "AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 AND B.EFFECT='C' " +
            "AND B.INVOICE_NO ='' AND B.MODULE_ID <>65 AND B.GRN_NO ='' AND (B.MATCHED=0 OR B.MATCHED IS NULL) " + Condition; //AND B.GRN_NO =''
            rsVoucher = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            if(rsVoucher.getRow()>0) {
                while(!rsVoucher.isAfterLast()) {
                    //Amount = clsAccount.get09AmountByVoucher(UtilFunctions.getString(rsVoucher, "VOUCHER_NO",""),MainCode,SubCode,UtilFunctions.getDouble(rsVoucher, "AMOUNT",0)); //,UtilFunctions.getInt(rsVoucher, "SR_NO",0)
                    //if(Amount > 0) {
                    availableAmount = EITLERPGLOBAL.round(availableAmount + UtilFunctions.getDouble(rsVoucher, "AMOUNT",0),2);
                    //}
                    rsVoucher.next();
                }
            }
        } catch(Exception e) {
            return availableAmount;
        }
        return availableAmount;
    }
    
    public static String getReasonCodeDesc(String MainAccountCode,String ReasonCode) {
        String ReasonCodeDesc = "";
        int InvoiceType = 0;
        try {
            if(MainAccountCode.equals("210027")) {
                InvoiceType=1;
            } else if(MainAccountCode.equals("210010")) {
                InvoiceType=2;
            } else if(MainAccountCode.equals("210072")) {
                InvoiceType=3;
            }
            ReasonCodeDesc = data.getStringValueFromDB("SELECT REASON_CODE_DESC FROM D_FIN_REASONCODE_MASTER WHERE INVOICE_TYPE="+InvoiceType+" AND REASON_CODE='"+ReasonCode+"' ",FinanceGlobal.FinURL);
        } catch(Exception e) {
            e.printStackTrace();
            return ReasonCodeDesc;
        }
        return ReasonCodeDesc;
    }
    
      
    public static HashMap getTallyLedger(String MainCode,String SubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        //OLD
        HashMap List=new HashMap();
        //clsVoucher objItem;
        clsVoucherLedgerItem objItem;
        ResultSet rsOpposite;
        
        try {
            boolean Continue=true;
            String strSQL="";
            String Condition="";
            int VoucherType=0;
            double LineAmount=0;
            //String SrNo = "";
            String PreviousVoucher = "";
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            if(!SubCode.trim().equals("")) {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO"; //AND BOOK_CODE<>'17'
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            } else {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            }
            
            double ClosingBalance=0;
            String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
            
            if(OnlyApproved) {
                ClosingBalance=clsAccount.getOpeningBalance(MainCode, SubCode, FromDate);
            }
            else {
                ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, SubCode, FromDate);
            }
            
            
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("COMPANY_ID",0);
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(FromDate));
            objItem.setAttribute("VOUCHER_NO","OPENING");
            objItem.setAttribute("LEGACY_NO","");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","Opening Balance");
            objItem.setAttribute("REMARKS"," *** OPENING BALANCE ***");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            if(ClosingBalance<0) { //It's a credit entry
                objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                objItem.setAttribute("DEBIT_AMOUNT",0);
            } else { //It's a debit entry
                objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                objItem.setAttribute("CREDIT_AMOUNT",0);
            }
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            //objItem.setAttribute("MIR_NO","");
            //objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            //objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","OPENING");
            objItem.setAttribute("EXECUTED_PO",0);
            
            List.put(Integer.toString(List.size()+1),objItem);
            
            ResultSet rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            //SrNo="";
            boolean VoucherFound = false;
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String Effect=UtilFunctions.getString(rsTmp,"EFFECT","");
                    String VoucherNo=UtilFunctions.getString(rsTmp,"VOUCHER_NO","");
                    VoucherType=UtilFunctions.getInt(rsTmp,"VOUCHER_TYPE",0);
                    LineAmount=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    
                    Condition="";
                    
                    if(FromAmount>0) {
                        Condition="AND AMOUNT>="+FromAmount+" ";
                    }
                    
                    if(ToAmount>0) {
                        Condition="AND AMOUNT<="+ToAmount+" ";
                    }
                    
                    Continue=true;
                    
                    if(!Condition.trim().equals("")) {
                        Condition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                        if(!data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE "+Condition,FinanceGlobal.FinURL)) {
                            Continue=false;
                        }
                    }
                    
                    if(Continue) {
                        
                        String Description=UtilFunctions.getString(rsTmp,"REMARKS","");
                        int BlockNo=UtilFunctions.getInt(rsTmp,"BLOCK_NO",0);
                        
                        String vMainCode=UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","");
                        String vSubCode=UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","");
                        
                        
                        boolean validAccountCodes=true;
                        
                        if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                            validAccountCodes=false;
                        } else {
                            /*if(Effect.equals("C")) {
                                ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            } else {
                                ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                            }*/
                        }
                        if(VoucherType==FinanceGlobal.TYPE_PJV || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                            VoucherFound=true;
                        } else {
                            VoucherFound=false;
                        }
                        
                        if(VoucherFound) {
                            if(VoucherNo.equals(PreviousVoucher)) {
                                if(Effect.equals("C")) {
                                    ClosingBalance-=LineAmount;
                                } else {
                                    ClosingBalance+=LineAmount;
                                }
                                rsTmp.next();
                                continue;
                            } else {
                                PreviousVoucher = VoucherNo;
                                List = getLedgerList(MainCode,SubCode,VoucherNo,OnlyApproved,ClosingBalance, List);
                                if(Effect.equals("C")) {
                                    ClosingBalance-=LineAmount;
                                } else {
                                    ClosingBalance+=LineAmount;
                                }
                                rsTmp.next();
                                continue;
                            }
                        }
                        if(BlockNo>0) {
                            //Find Opposite Account
                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                //Added by Mrugesh 07/02/2011 -- start
                                if(VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                } else {
                                    rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                }
                                //Added by Mrugesh 07/02/2011 -- end
                            } else {
                                rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND BLOCK_NO="+BlockNo+" AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                            }
                            
                            rsOpposite.first();
                            if(rsOpposite.getRow()>0) {
                                //while(!rsOpposite.isAfterLast()) {
                                String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                
                                objItem = new clsVoucherLedgerItem();
                                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                                objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                objItem.setAttribute("LEGACY_NO",UtilFunctions.getString(rsTmp,"LEGACY_NO",""));
                                objItem.setAttribute("REMARKS",Description);
                                objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                                objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE EFFECT='"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                                objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                                String AcName = clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                objItem.setAttribute("ACCOUNT_NAME", AcName );
                                if(Effect.equals("C")) {
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE ) {
                                        ClosingBalance-=LineAmount;
                                        objItem.setAttribute("CREDIT_AMOUNT",LineAmount);
                                    } else {
                                        ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                        objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    }
                                    objItem.setAttribute("DEBIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","C");
                                } else {
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                        ClosingBalance+=LineAmount;
                                        objItem.setAttribute("DEBIT_AMOUNT",LineAmount);
                                    } else {
                                        ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                        objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                    }
                                    objItem.setAttribute("CREDIT_AMOUNT",0);
                                    objItem.setAttribute("EFFECT","D");
                                }
                                
                                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                
                                //======== Provide Additional Information ==============//
                                String GRNNo=UtilFunctions.getString(rsTmp,"GRN_NO", "");
                                
                                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                                
                                objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                                
                                objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                                objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00")));
                                
                                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                                objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                                //objItem.setAttribute("MIR_NO","");
                                //objItem.setAttribute("MIR_DATE","0000-00-00");
                                //objItem.setAttribute("DUE_DATE","0000-00-00");
                                objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsTmp,"INVOICE_NO","")+UtilFunctions.getString(rsTmp,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                
                                /*if(!objItem.getAttribute("GRN_NO").getString().trim().equals("")) {
                                    try {
                                        int RefCompanyID=UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0);
                                        String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                                 
                                        ResultSet rsMIR=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                        rsMIR.first();
                                 
                                        if(rsMIR.getRow()>0) {
                                            String MIRNo=rsMIR.getString("MIR_NO");
                                 
                                            objItem.setAttribute("MIR_NO",MIRNo);
                                 
                                            rsMIR=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                                            rsMIR.first();
                                 
                                            if(rsMIR.getRow()>0) {
                                                String MIRDate=rsMIR.getString("MIR_DATE");
                                                objItem.setAttribute("MIR_DATE",EITLERPGLOBAL.formatDate(MIRDate));
                                            }
                                        }
                                 
                                        objItem.setAttribute("DUE_DATE",calculateDueDate(GRNNo,RefCompanyID));
                                    } catch(Exception MIR) {
                                    }
                                }*/
                                //======================================================//
                                
                                
                                //============ Check whether PO is executed ===============//
                                int FullyExecutedPO=0;
                                String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                                int CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                                String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                                //System.out.println(CompanyID);
                                if(!vPONo.trim().equals("")) {
                                    ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                                    try {
                                        if(rsPO.getRow()>0) {
                                            rsPO.first();
                                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                                FullyExecutedPO=1;
                                            }
                                        }
                                    }catch(Exception e) {}
                                }
                                
                                objItem.setAttribute("EXECUTED_PO",FullyExecutedPO);
                                //=========================================================//
                                System.out.println("count="+List.size()+" voucher_date="+UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00") + "partycode="+UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","")+" VOUCHER_NO="+UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                                //Add Entry to the list
                                List.put(Integer.toString(List.size()+1),objItem);
                                rsOpposite.next();
                                //}
                            }
                        }
                    }
                    rsTmp.next();
                }
            }
            //Adding Final Closing Balance entry
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objItem.setAttribute("VOUCHER_NO","CLOSING");
            objItem.setAttribute("LEGACY_NO","");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","Closing Balance");
            objItem.setAttribute("REMARKS"," *** CLOSING BALANCE *** ");
            
            if(ClosingBalance<0) {
                objItem.setAttribute("CREDIT_AMOUNT",0);
                objItem.setAttribute("DEBIT_AMOUNT",0);
            } else {
                objItem.setAttribute("DEBIT_AMOUNT",0);
                objItem.setAttribute("CREDIT_AMOUNT",0);
            }
            
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            //objItem.setAttribute("MIR_NO","");
            //objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            //objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","CLOSING");
            objItem.setAttribute("EXECUTED_PO",0);
            
            
            List.put(Integer.toString(List.size()+1),objItem);
            
            //Add a Blank Line
            //objItem=new clsVoucher();
            objItem = new clsVoucherLedgerItem();
            objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objItem.setAttribute("VOUCHER_NO","CLOSING");
            objItem.setAttribute("LEGACY_NO","");
            objItem.setAttribute("BOOK_CODE","");
            objItem.setAttribute("LINK_NO","");
            objItem.setAttribute("INVOICE_NO","");
            objItem.setAttribute("INVOICE_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            objItem.setAttribute("CHEQUE_NO","");
            objItem.setAttribute("CHEQUE_DATE","0000-00-00");
            objItem.setAttribute("BANK_NAME","");
            objItem.setAttribute("MAIN_ACCOUNT_CODE","");
            objItem.setAttribute("SUB_ACCOUNT_CODE","");
            objItem.setAttribute("ACCOUNT_NAME","");
            objItem.setAttribute("REMARKS","**** CLOSING BALANCE ****");
            objItem.setAttribute("DEBIT_AMOUNT",0);
            objItem.setAttribute("CREDIT_AMOUNT",0);
            objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
            //objItem.setAttribute("MIR_NO","");
            //objItem.setAttribute("MIR_DATE","0000-00-00");
            objItem.setAttribute("PO_NO","");
            objItem.setAttribute("PO_DATE","0000-00-00");
            //objItem.setAttribute("DUE_DATE","0000-00-00");
            objItem.setAttribute("GROUP","");
            
            List.put(Integer.toString(List.size()+1),objItem);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
    
    public static HashMap getLedgerList(String MainCode,String SubCode,String VoucherNo,boolean OnlyApproved,double ClosingBalance,HashMap List) {
        try {
            String strSQL="";
            if(!SubCode.trim().equals("")) {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.VOUCHER_NO='"+VoucherNo+"' ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+SubCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            } else {
                if(OnlyApproved) {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.VOUCHER_NO='"+VoucherNo+"' ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                } else {
                    strSQL="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID,B.LINK_NO FROM  D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.CANCELLED=0 AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.VOUCHER_NO='"+VoucherNo+"' ORDER BY A.VOUCHER_DATE,A.VOUCHER_NO, B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.SR_NO";
                }
            }
            ResultSet rsTmp = data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            while(!rsTmp.isAfterLast()) {
                clsVoucherLedgerItem objItem = new clsVoucherLedgerItem();
                String Description=UtilFunctions.getString(rsTmp,"REMARKS","");
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00")));
                objItem.setAttribute("VOUCHER_NO",VoucherNo);
                objItem.setAttribute("LEGACY_NO",UtilFunctions.getInt(rsTmp,"LEGACY_NO",0));
                objItem.setAttribute("REMARKS",Description);
                objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsTmp,"BOOK_CODE",""));
                objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsTmp,"BANK_NAME",""));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""));
                //String PartyCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE EFFECT='"+rsTmp.getString("EFFECT")+"' AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
                objItem.setAttribute("SUB_ACCOUNT_CODE",SubCode);
                String AcName = clsAccount.getAccountName(UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE",""));
                objItem.setAttribute("ACCOUNT_NAME", AcName );
                
                if(rsTmp.getString("EFFECT").equals("C")) {
                    ClosingBalance-=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                    objItem.setAttribute("EFFECT","C");
                } else {
                    ClosingBalance+=UtilFunctions.getDouble(rsTmp,"AMOUNT",0);
                    objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                    objItem.setAttribute("EFFECT","D");
                }
                
                objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                
                //======== Provide Additional Information ==============//
                String GRNNo=UtilFunctions.getString(rsTmp,"GRN_NO", "");
                
                objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsTmp,"INVOICE_NO",""));
                objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"INVOICE_DATE","0000-00-00")));
                
                objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                
                objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHEQUE_DATE","0000-00-00")));
                
                objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsTmp,"GRN_NO",""));
                objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"GRN_DATE","0000-00-00")));
                objItem.setAttribute("PO_NO",UtilFunctions.getString(rsTmp,"PO_NO",""));
                objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PO_DATE","0000-00-00")));
                //objItem.setAttribute("MIR_NO","");
                //objItem.setAttribute("MIR_DATE","0000-00-00");
                //objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsTmp,"INVOICE_NO","")+UtilFunctions.getString(rsTmp,"PO_NO","")+UtilFunctions.getString(rsTmp,"CHEQUE_NO",""));
                
                /*if(!objItem.getAttribute("GRN_NO").getString().trim().equals("")) {
                    try {
                        int RefCompanyID=UtilFunctions.getInt(rsTmp,"REF_COMPANY_ID",0);
                        String CompanyURL=clsFinYear.getDBURL(RefCompanyID,EITLERPGLOBAL.FinYearFrom);
                 
                        ResultSet rsMIR=data.getResult("SELECT DISTINCT(MIR_NO) AS MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND MIR_NO<>'' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                        rsMIR.first();
                 
                        if(rsMIR.getRow()>0) {
                            String MIRNo=rsMIR.getString("MIR_NO");
                 
                            objItem.setAttribute("MIR_NO",MIRNo);
                 
                            rsMIR=data.getResult("SELECT MIR_DATE,SUPP_ID FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"' AND COMPANY_ID="+RefCompanyID,CompanyURL);
                            rsMIR.first();
                 
                            if(rsMIR.getRow()>0) {
                                String MIRDate=rsMIR.getString("MIR_DATE");
                                objItem.setAttribute("MIR_DATE",EITLERPGLOBAL.formatDate(MIRDate));
                            }
                        }
                 
                        objItem.setAttribute("DUE_DATE",calculateDueDate(GRNNo,RefCompanyID));
                    } catch(Exception MIR) {
                    }
                }*/
                //======================================================//
                
                
                //============ Check whether PO is executed ===============//
                int FullyExecutedPO=0;
                String vPONo=UtilFunctions.getString(rsTmp,"PO_NO","");
                int CompanyID=UtilFunctions.getInt(rsTmp,"COMPANY_ID",0);
                String CompanyURL=clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
                //System.out.println(CompanyID);
                if(!vPONo.trim().equals("")) {
                    ResultSet rsPO=data.getResult("SELECT SUM(QTY) AS QTY,SUM(RECD_QTY) AS RECD_QTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+vPONo+"'",CompanyURL);
                    try {
                        if(rsPO.getRow()>0) {
                            rsPO.first();
                            if(rsPO.getDouble("RECD_QTY")>=rsPO.getDouble("QTY")) {
                                FullyExecutedPO=1;
                            }
                        }
                    }catch(Exception e) {}
                }
                
                objItem.setAttribute("EXECUTED_PO",FullyExecutedPO);
                //=========================================================//
                System.out.println("count="+List.size()+" voucher_date="+UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00") + " partycode="+UtilFunctions.getString(rsTmp,"SUB_ACCOUNT_CODE","")+" VOUCHER_NO="+UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                //Add Entry to the list
                List.put(Integer.toString(List.size()+1),objItem);
                rsTmp.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return List;
        }
        return List;
    }
         
    public static HashMap getMyLedgerNew(String MainCode,String FromSubCode,String ToSubCode,String FromDate,String ToDate,boolean OnlyApproved,double FromAmount,double ToAmount,String BookCode) {
        // Mrugesh
        HashMap List=new HashMap();
        clsVoucherLedgerItem objItem;
        ResultSet rsOpposite;
        String PartyCondition="";
        boolean OnlyMainCode=false;
        double PartyClosingBalance=0;
        try {
            boolean Continue=true;
            String strParty="";
            String Condition="";
            int VoucherType=0;
            double LineAmount=0;
            
            if(!BookCode.trim().equals("")) {
                Condition=" AND A.BOOK_CODE='"+BookCode+"' ";
            }
            
            if(!FromSubCode.trim().equals("") && !ToSubCode.trim().equals("")) {
                PartyCondition = " AND PARTY_CODE>='"+FromSubCode+"' AND PARTY_CODE<='"+ToSubCode+"' ORDER BY PARTY_CODE";
            } else if(!FromSubCode.trim().equals("") && ToSubCode.trim().equals("")){
                PartyCondition = " AND PARTY_CODE='"+FromSubCode+"' ORDER BY PARTY_CODE";
            } else if(FromSubCode.trim().equals("") && !ToSubCode.trim().equals("")){
                PartyCondition = " AND PARTY_CODE='"+ToSubCode+"' ORDER BY PARTY_CODE";
            } else {
                PartyCondition = " ORDER BY PARTY_CODE";
                OnlyMainCode=true;
            }
            
            strParty = "SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE<>'' "+PartyCondition;
            double MainClosingBalance=0;
            if(OnlyMainCode) {
                String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
                
                if(OnlyApproved) {
                    MainClosingBalance=clsAccount.getOpeningBalance(MainCode, "", FromDate);
                } else {
                    MainClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, "", FromDate);
                }
                
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("COMPANY_ID",0);
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","1");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","OPENING BALANCE-" + MainCode);
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                if(MainClosingBalance<0) { //It's a credit entry
                    objItem.setAttribute("CREDIT_AMOUNT",Math.abs(MainClosingBalance));
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                } else { //It's a debit entry
                    objItem.setAttribute("DEBIT_AMOUNT",MainClosingBalance);
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                }
                objItem.setAttribute("CLOSING_BALANCE",MainClosingBalance);
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                //objItem.setAttribute("MIR_NO","");
                //objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                //objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","OPENING");
                objItem.setAttribute("EXECUTED_PO",0);
                
                List.put(Integer.toString(List.size()+1),objItem);
            }
            
            ResultSet rsParty=data.getResult(strParty,FinanceGlobal.FinURL);
            rsParty.first();
            if(rsParty.getRow()>0) {
                while(!rsParty.isAfterLast()) {
                    String PartyCode=UtilFunctions.getString(rsParty,"PARTY_CODE","");
                    PartyClosingBalance = 0;
                    double ClosingBalance=0;
                    String VoucherDate = "";
                    int VoucherMonth = 0;
                    boolean firstVoucher = false;
                    String PrevDate=EITLERPGLOBAL.DeductDaysFromDate(java.sql.Date.valueOf(FromDate), 1);
                    
                    if(OnlyApproved) {
                        ClosingBalance=clsAccount.getOpeningBalance(MainCode, PartyCode, FromDate);
                    } else {
                        ClosingBalance=clsAccount.getAvailableOpeningBalance(MainCode, PartyCode, FromDate);
                    }
                    
                    String SQLPartyTran = "";
                    if(OnlyApproved) {
                        SQLPartyTran="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID " +
                        "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO"; //AND A.BOOK_CODE<>'17'
                    } else {
                        SQLPartyTran="SELECT A.*,B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,B.AMOUNT,B.EFFECT,B.BLOCK_NO,B.PO_NO,B.PO_DATE,B.INVOICE_NO,B.INVOICE_DATE,B.GRN_NO,B.GRN_DATE,B.MODULE_ID,B.REF_COMPANY_ID " +
                        "FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>='"+FromDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' " +
                        "AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO "+Condition+" ORDER BY B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.VOUCHER_DATE,A.VOUCHER_NO";
                    }
                    
                    ResultSet rsPartyTran = data.getResult(SQLPartyTran,FinanceGlobal.FinURL);
                    rsPartyTran.first();
                    
                    if(ClosingBalance == 0 ) {
                        if(rsPartyTran.getRow() > 0) {
                            //do nothing
                        } else {
                            rsParty.next();
                            continue;
                        }
                    }
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("COMPANY_ID",0);
                    objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                    objItem.setAttribute("VOUCHER_NO","");
                    objItem.setAttribute("LEGACY_NO","");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                    objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                    objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,PartyCode));
                    objItem.setAttribute("REMARKS","OPENING BALANCE");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    if(ClosingBalance<0) { //It's a credit entry
                        objItem.setAttribute("CREDIT_AMOUNT",Math.abs(ClosingBalance));
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    } else { //It's a debit entry
                        objItem.setAttribute("DEBIT_AMOUNT",ClosingBalance);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    //objItem.setAttribute("MIR_NO","");
                    //objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    //objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","OPENING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    rsPartyTran.first();
                    boolean VoucherFound=false;
                    String PreviousVoucher = "";
                    if(rsPartyTran.getRow() > 0 ) {
                        firstVoucher=true;
                        while(!rsPartyTran.isAfterLast()) {
                            
                            String Effect=UtilFunctions.getString(rsPartyTran,"EFFECT","");
                            String VoucherNo=UtilFunctions.getString(rsPartyTran,"VOUCHER_NO","");
                            String pVoucherNo="";
                            
                            if(VoucherNo.length()>11 && VoucherNo.startsWith("M")) {
                                pVoucherNo = VoucherNo.substring(0,1)+VoucherNo.substring(9);
                            } else {
                                pVoucherNo=VoucherNo;
                            }
                            VoucherType=UtilFunctions.getInt(rsPartyTran,"VOUCHER_TYPE",0);
                            LineAmount=UtilFunctions.getDouble(rsPartyTran,"AMOUNT",0);
                            VoucherDate = UtilFunctions.getString(rsPartyTran,"VOUCHER_DATE","0000-00-00");
                            if(firstVoucher) {
                                VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                                firstVoucher=false;
                            } else {
                                if(EITLERPGLOBAL.getMonth(VoucherDate)!=VoucherMonth) {
                                    List.put(Integer.toString(List.size()+1),getMonthClosingEntry(PartyCode,ClosingBalance));
                                    VoucherMonth = EITLERPGLOBAL.getMonth(VoucherDate);
                                }
                            }
                            
                            String AmountCondition="";
                            
                            if(FromAmount>0) {
                                AmountCondition="AND AMOUNT>="+FromAmount+" ";
                            }
                            
                            if(ToAmount>0) {
                                AmountCondition="AND AMOUNT<="+ToAmount+" ";
                            }
                            
                            Continue=true;
                            
                            if(!AmountCondition.trim().equals("")) {
                                AmountCondition=" VOUCHER_NO='"+VoucherNo+"' "+Condition;
                                if(!data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_DETAIL WHERE "+AmountCondition,FinanceGlobal.FinURL)) {
                                    Continue=false;
                                }
                            }
                            
                            if(Continue) {
                                
                                String Description=UtilFunctions.getString(rsPartyTran,"REMARKS","").replace('\n',' ');
                                if(Description.length()>125) {
                                    Description=Description.substring(0,125);
                                }
                                int BlockNo=UtilFunctions.getInt(rsPartyTran,"BLOCK_NO",0);
                                
                                String vMainCode=UtilFunctions.getString(rsPartyTran,"MAIN_ACCOUNT_CODE","");
                                String vSubCode=UtilFunctions.getString(rsPartyTran,"SUB_ACCOUNT_CODE","");
                                
                                ///////////////////////////////////
                                if(VoucherType==FinanceGlobal.TYPE_PJV || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL) {
                                    VoucherFound=true;
                                } else {
                                    VoucherFound=false;
                                }
                                if(VoucherFound) {
                                    if(VoucherNo.equals(PreviousVoucher)) {
                                        if(Effect.equals("C")) {
                                            ClosingBalance-=LineAmount;
                                        } else {
                                            ClosingBalance+=LineAmount;
                                        }
                                        rsPartyTran.next();
                                        continue;
                                    } else {
                                        PreviousVoucher = VoucherNo;
                                        List = getLedgerList(MainCode,FromSubCode,VoucherNo,OnlyApproved,ClosingBalance, List);
                                        if(Effect.equals("C")) {
                                            ClosingBalance-=LineAmount;
                                        } else {
                                            ClosingBalance+=LineAmount;
                                        }
                                        rsPartyTran.next();
                                        continue;
                                    }
                                }
                                ///////////////////////////////////
                                
                                boolean validAccountCodes=true;
                                
                                if(!clsAccount.IsValidAccount(vMainCode, vSubCode)) {
                                    validAccountCodes=false;
                                } else {
                                    /*if(Effect.equals("C")) {
                                        ClosingBalance=ClosingBalance-(UtilFunctions.getDouble(rsParty,"AMOUNT",0));
                                    } else {
                                        ClosingBalance=ClosingBalance+(UtilFunctions.getDouble(rsParty,"AMOUNT",0));
                                    }*/
                                }
                                
                                if(BlockNo>0) {
                                    //Find Opposite Account
                                    if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                        //Added by Mrugesh 07/02/2011 -- start
                                        /*if(VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                            //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' AND AMOUNT= "+LineAmount+" ORDER BY SR_NO",FinanceGlobal.FinURL);
                                            rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                        } else {*/
                                        rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE EFFECT<>'"+Effect+"' AND VOUCHER_NO='"+VoucherNo+"' ORDER BY SR_NO",FinanceGlobal.FinURL);
                                        //}
                                        //Added by Mrugesh 07/02/2011 -- end
                                        //rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT<>'"+Effect+"' ORDER BY SR_NO",FinanceGlobal.FinURL); //AND AMOUNT="+LineAmount+"
                                    } else {
                                        rsOpposite=data.getResult("SELECT * FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND BLOCK_NO="+BlockNo+" AND EFFECT<>'"+Effect+"'",FinanceGlobal.FinURL);
                                    }
                                    
                                    rsOpposite.first();
                                    if(rsOpposite.getRow()>0) {
                                        String oEffect=UtilFunctions.getString(rsOpposite,"EFFECT","");
                                        
                                        
                                        objItem = new clsVoucherLedgerItem();
                                        objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsPartyTran,"COMPANY_ID",0));
                                        
                                        objItem.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"VOUCHER_DATE","0000-00-00")));
                                        //objItem.setAttribute("VOUCHER_NO",VoucherNo);
                                        objItem.setAttribute("VOUCHER_NO",pVoucherNo);
                                        objItem.setAttribute("LEGACY_NO",UtilFunctions.getString(rsPartyTran,"LEGACY_NO",""));
                                        objItem.setAttribute("REMARKS",Description);
                                        objItem.setAttribute("BOOK_CODE",UtilFunctions.getString(rsPartyTran,"BOOK_CODE",""));
                                        objItem.setAttribute("BANK_NAME",UtilFunctions.getString(rsOpposite,"BANK_NAME",""));
                                        objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""));
                                        
                                        //CHANGE AS ON 24/06/2009 -- START
                                        //String SubAccountCode = data.getStringValueFromDB("SELECT SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+VoucherNo+"' AND EFFECT='"+Effect+"'",FinanceGlobal.FinURL);
                                        //objItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                                        objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                                        //CHANGE AS ON 24/06/2009 -- END
                                        
                                        
                                        String AcName=clsAccount.getAccountName(UtilFunctions.getString(rsOpposite,"MAIN_ACCOUNT_CODE",""), UtilFunctions.getString(rsOpposite,"SUB_ACCOUNT_CODE",""));
                                        objItem.setAttribute("ACCOUNT_NAME", AcName );
                                        if(Effect.equals("C")) {
                                            
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                                ClosingBalance-=LineAmount;
                                                objItem.setAttribute("CREDIT_AMOUNT",LineAmount);
                                                MainClosingBalance-=LineAmount;
                                                
                                            }
                                            else {
                                                ClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("CREDIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                                MainClosingBalance-=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                            }
                                            
                                            
                                            objItem.setAttribute("DEBIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","C");
                                        }
                                        else {
                                            if(VoucherType==FinanceGlobal.TYPE_JOURNAL || VoucherType==FinanceGlobal.TYPE_SALES_JOURNAL || VoucherType==FinanceGlobal.TYPE_CREDIT_NOTE) {
                                                ClosingBalance+=LineAmount;
                                                objItem.setAttribute("DEBIT_AMOUNT",LineAmount);
                                                MainClosingBalance+=LineAmount;
                                            }
                                            else {
                                                ClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                                objItem.setAttribute("DEBIT_AMOUNT",UtilFunctions.getDouble(rsOpposite,"AMOUNT",0));
                                                MainClosingBalance+=UtilFunctions.getDouble(rsOpposite,"AMOUNT",0);
                                            }
                                            
                                            objItem.setAttribute("CREDIT_AMOUNT",0);
                                            objItem.setAttribute("EFFECT","D");
                                        }
                                        
                                        objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                                        
                                        //======== Provide Additional Information ==============//
                                        String GRNNo=UtilFunctions.getString(rsPartyTran,"GRN_NO", "");
                                        
                                        objItem.setAttribute("INVOICE_NO",UtilFunctions.getString(rsPartyTran,"INVOICE_NO",""));
                                        objItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"INVOICE_DATE","0000-00-00")));
                                        //objItem.setAttribute("LINK_NO",clsSalesInvoice.getAgentAlphaSrNo(UtilFunctions.getString(rsPartyTran,"INVOICE_NO",""), UtilFunctions.getString(rsPartyTran,"INVOICE_DATE","0000-00-00")));
                                        objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsPartyTran,"LINK_NO",""));
                                        objItem.setAttribute("CHEQUE_NO",UtilFunctions.getString(rsPartyTran,"CHEQUE_NO",""));
                                        objItem.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"CHEQUE_DATE","0000-00-00")));
                                        
                                        objItem.setAttribute("GRN_NO",UtilFunctions.getString(rsPartyTran,"GRN_NO",""));
                                        objItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"GRN_DATE","0000-00-00")));
                                        objItem.setAttribute("PO_NO",UtilFunctions.getString(rsPartyTran,"PO_NO",""));
                                        objItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsPartyTran,"PO_DATE","0000-00-00")));
                                        //objItem.setAttribute("MIR_NO","");
                                        //objItem.setAttribute("MIR_DATE","0000-00-00");
                                        //objItem.setAttribute("DUE_DATE","0000-00-00");
                                        objItem.setAttribute("GROUP",VoucherNo+UtilFunctions.getString(rsPartyTran,"INVOICE_NO","")+UtilFunctions.getString(rsPartyTran,"PO_NO","")+UtilFunctions.getString(rsOpposite,"CHEQUE_NO",""));
                                        
                                        List.put(Integer.toString(List.size()+1),objItem);
                                    }
                                }
                            }
                            
                            rsPartyTran.next();
                        }
                    }
                    //Adding Final Closing Balance entry
                    objItem = new clsVoucherLedgerItem();
                    objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                    objItem.setAttribute("VOUCHER_NO","");
                    objItem.setAttribute("LEGACY_NO","");
                    objItem.setAttribute("BOOK_CODE","");
                    objItem.setAttribute("LINK_NO","");
                    objItem.setAttribute("INVOICE_NO","");
                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    objItem.setAttribute("CHEQUE_NO","");
                    objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                    objItem.setAttribute("BANK_NAME","");
                    objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                    objItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
                    objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,PartyCode));
                    objItem.setAttribute("REMARKS","CLOSING BALANCE");
                    
                    if(ClosingBalance<0) {
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                    } else {
                        objItem.setAttribute("DEBIT_AMOUNT",0);
                        objItem.setAttribute("CREDIT_AMOUNT",0);
                    }
                    
                    objItem.setAttribute("CLOSING_BALANCE",ClosingBalance);
                    //                    objItem.setAttribute("INVOICE_NO","");
                    //                    objItem.setAttribute("INVOICE_DATE","0000-00-00");
                    //objItem.setAttribute("MIR_NO","");
                    //objItem.setAttribute("MIR_DATE","0000-00-00");
                    objItem.setAttribute("PO_NO","");
                    objItem.setAttribute("PO_DATE","0000-00-00");
                    //objItem.setAttribute("DUE_DATE","0000-00-00");
                    objItem.setAttribute("GROUP","CLOSING");
                    objItem.setAttribute("EXECUTED_PO",0);
                    
                    
                    List.put(Integer.toString(List.size()+1),objItem);
                    rsParty.next();
                }
            } else {
                
            }
            
            if(OnlyMainCode) {
                //Add a Blank Line
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","2");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","");
                objItem.setAttribute("DEBIT_AMOUNT",0);
                objItem.setAttribute("CREDIT_AMOUNT",0);
                objItem.setAttribute("CLOSING_BALANCE",0);
                //objItem.setAttribute("MIR_NO","");
                //objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                //objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","");
                
                List.put(Integer.toString(List.size()+1),objItem);
                
                //Adding Final Closing Balance entry
                objItem = new clsVoucherLedgerItem();
                objItem.setAttribute("VOUCHER_DATE","0000-00-00");
                objItem.setAttribute("VOUCHER_NO","");
                objItem.setAttribute("LEGACY_NO","");
                objItem.setAttribute("BOOK_CODE","");
                objItem.setAttribute("LINK_NO","");
                objItem.setAttribute("INVOICE_NO","");
                objItem.setAttribute("INVOICE_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                objItem.setAttribute("CHEQUE_NO","");
                objItem.setAttribute("CHEQUE_DATE","0000-00-00");
                objItem.setAttribute("BANK_NAME","");
                objItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                objItem.setAttribute("SUB_ACCOUNT_CODE","2");
                objItem.setAttribute("ACCOUNT_NAME",clsAccount.getAccountName(MainCode,""));
                objItem.setAttribute("REMARKS","CLOSING BALANCE-"+MainCode);
                
                if(MainClosingBalance<0) {
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                } else {
                    objItem.setAttribute("DEBIT_AMOUNT",0);
                    objItem.setAttribute("CREDIT_AMOUNT",0);
                }
                objItem.setAttribute("CLOSING_BALANCE",MainClosingBalance);
                
                //objItem.setAttribute("MIR_NO","");
                //objItem.setAttribute("MIR_DATE","0000-00-00");
                objItem.setAttribute("PO_NO","");
                objItem.setAttribute("PO_DATE","0000-00-00");
                //objItem.setAttribute("DUE_DATE","0000-00-00");
                objItem.setAttribute("GROUP","");
                objItem.setAttribute("EXECUTED_PO",0);
                
                List.put(Integer.toString(List.size()+1),objItem);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return List;
    }
   
    
}

