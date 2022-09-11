package EITLERP.Finance;


import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.* ;
import java.util.Calendar.* ;
import java.util.*;
import java.text.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPartyInfo{
    
    public String EntryNo,EntryDate,strQuery,AcName ;
    String FromDate = EITLERPGLOBAL.FinFromDateDB ;
    String ToDate = "";
    String FromDate1="";
    String strSQL="";
    public Object[][] ObjMonth = new Object[24][4];
    public Object[][] ObjYear = new Object[1][4];
    public int NoOfRows=0;
    DecimalFormat dFormat=new DecimalFormat("###0.00");
    public Object[][] getMonthDetailCenvat(String MainAccountCode,String SubAccountCode,String FromDatec,String ToDatec,boolean bApproved) {
        
        String fromdate=FromDatec;
        String todate=ToDatec;
        String Query="";
        String Conditions="";
        
        java.sql.Date checkfrom = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(fromdate));
        java.sql.Date checkto   = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(todate));
        
        FromDatec = EITLERPGLOBAL.formatDateDB(FromDatec);
        
        int n=EITLERPGLOBAL.getMonth(FromDatec);
        ToDatec=clsCalcInterest.addMonthToDate(FromDatec,1);
        
        int i=0;
        
        
        while(checkfrom.before(checkto)) {
            
            
            FromDate1 = FromDatec;
            //FromDate1 = EITLERPGLOBAL.addDaysToDate(FromDate, -1, "yyyy-MM-dd");
            ObjMonth[i][0] = getSuffix(Double.toString(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,FromDate1,bApproved)));
            ObjMonth[i][1] = getSuffix(getCreditInfo(MainAccountCode,SubAccountCode,FromDate1,ToDatec,bApproved));
            ObjMonth[i][2] = getSuffix(getDebitInfo(MainAccountCode,SubAccountCode,FromDate1,ToDatec,bApproved));
            ObjMonth[i][3] = getSuffix(Double.toString(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,ToDatec,bApproved)));
            FromDatec =clsCalcInterest.addMonthToDate(FromDatec,1);
            ToDatec = clsCalcInterest.addMonthToDate(FromDatec,1);
            i++;
            checkfrom = java.sql.Date.valueOf(FromDatec);
            checkto = java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(todate));
        }
        
        
        
        return ObjMonth ;
    }
    
    public Object[][] getMonthDetail(String MainAccountCode,String SubAccountCode,boolean bApproved) {
        String Query="";
        String Conditions="";
        ToDate=EITLERPGLOBAL.addMonthToDate(FromDate);
        
        for(int i=0;i<=11;i++) {
            FromDate1 = FromDate ;
            //FromDate1 = EITLERPGLOBAL.addDaysToDate(FromDate, -1, "yyyy-MM-dd");
            ObjMonth[i][0] = getSuffix(dFormat.format(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,FromDate1,bApproved)));
            ObjMonth[i][1] = getSuffix(getCreditInfo(MainAccountCode,SubAccountCode,FromDate1,ToDate,bApproved));
            ObjMonth[i][2] = getSuffix(getDebitInfo(MainAccountCode,SubAccountCode,FromDate1,ToDate,bApproved));
            ObjMonth[i][3] = getSuffix(dFormat.format(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,ToDate,bApproved)));
            FromDate =EITLERPGLOBAL.addDaysToDate(ToDate, 1, "yyyy-MM-dd");
            ToDate = EITLERPGLOBAL.addMonthToDate(FromDate);
        }
        
        return ObjMonth ;
    }
    
    public Object[][] getYearDetail(String MainAccountCode,String SubAccountCode,boolean bApproved) {
        String Query="";
        String Conditions="";
        ToDate=EITLERPGLOBAL.FinToDateDB;
        
        
        FromDate1 = FromDate ;
        //FromDate1 = EITLERPGLOBAL.addDaysToDate(FromDate, -1, "yyyy-MM-dd");
        ObjYear[0][0] = getSuffix(dFormat.format(clsAccount.getClosingBalance(MainAccountCode,SubAccountCode,FromDate1,bApproved)));
        ObjYear[0][1] = getSuffix(getCreditInfo(MainAccountCode,SubAccountCode,FromDate1,ToDate,bApproved));
        ObjYear[0][2] = getSuffix(getDebitInfo(MainAccountCode,SubAccountCode,FromDate1,ToDate,bApproved));
        ObjYear[0][3] = getSuffix(dFormat.format(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,ToDate,bApproved)));
        //FromDate =EITLERPGLOBAL.addDaysToDate(ToDate, 1, "yyyy-MM-dd");
        //ToDate = EITLERPGLOBAL.addMonthToDate(FromDate);
        
        
        return ObjYear ;
    }
    
    public String getSuffix(String ll){
        String jj;
        jj=ll.substring(0,1);
        if(jj.equals("-")) {
            ll=ll.replaceAll("-","");
            return ll.concat("Cr");
        }
        else{
            return ll.concat("Dr");
        }
        
        
    }
    
    public Object[][] getDateDetail(String MainAccountCode,String SubAccountCode,String OpeningDate,boolean bApproved) throws java.sql.SQLException {
        
        
        ResultSet rsDate;
        ToDate=EITLERPGLOBAL.addMonthToDate(OpeningDate);
        strSQL="SELECT DISTINCT(A.VOUCHER_DATE) AS VOUCHERDATE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE  A.VOUCHER_DATE>='"+OpeningDate+"' AND A.VOUCHER_DATE<='"+ToDate+"' AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 AND A.VOUCHER_NO =B.VOUCHER_NO "+getStrQuery(MainAccountCode,SubAccountCode);
        rsDate=data.getResult(strSQL,FinanceGlobal.FinURL);
        rsDate.last();
        NoOfRows=rsDate.getRow();
        Object[][] ObjDate = new Object[NoOfRows+1][6];
        rsDate.first();
        if(rsDate.getRow()>0) {
            
            for(int i=0;i<NoOfRows;i++) {
                ObjDate[i][0]=rsDate.getString("VOUCHERDATE");
                ObjDate[i][1]=getSuffix(dFormat.format(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,EITLERPGLOBAL.addDaysToDate((String)ObjDate[i][0],-1,"yyyy-MM-dd"),bApproved)));
                //strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0"+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='C' AND VOUCHER_DATE='"+(String)ObjDate[i][0]+"'";
                strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_DETAIL_EX A,D_FIN_VOUCHER_HEADER B WHERE A.VOUCHER_NO=B.VOUCHER_NO  AND B.APPROVED="+bApproved+" AND B.CANCELLED=0 AND B.VOUCHER_DATE='"+(String)ObjDate[i][0]+"' "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT = 'D'";
                ObjDate[i][2]=getSuffix(dFormat.format(data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL)));
                strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO  AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='C' AND VOUCHER_DATE='"+(String)ObjDate[i][0]+"'";
                ObjDate[i][3]=getSuffix(dFormat.format(-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL)));
                ObjDate[i][4]=getSuffix(dFormat.format(clsAccount.getClosingBalanceAll(MainAccountCode,SubAccountCode,(String)ObjDate[i][0],bApproved)));
                ObjDate[i][0]=EITLERPGLOBAL.formatDate((String)ObjDate[i][0]);
                rsDate.next();
            }
        }
        return ObjDate;
    }
    
    public Object[][] getVoucherInfo(String MainAccountCode,String SubAccountCode,String OnDate,boolean bApproved)throws java.sql.SQLException {
        String strVoucherSQL;
        int noOfVouchers=0;
        strVoucherSQL="SELECT DISTINCT(A.VOUCHER_NO) AS VOUCHERNO FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B WHERE  A.VOUCHER_DATE='"+OnDate+"'  AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 AND A.VOUCHER_NO =B.VOUCHER_NO "+getStrQuery(MainAccountCode,SubAccountCode);
        ResultSet rsVoucher = data.getResult(strVoucherSQL,FinanceGlobal.FinURL);
        rsVoucher.last();
        Object[][] ObjVoucher = new Object[rsVoucher.getRow()+1][5];
        if(rsVoucher.getRow()>0) {
            noOfVouchers=rsVoucher.getRow();
            
            rsVoucher.first();
            for(int k=0;k<noOfVouchers;k++) {
                ObjVoucher[k][0]=rsVoucher.getString("VOUCHERNO");
                ObjVoucher[k][1]=EITLERPGLOBAL.formatDate(OnDate);
                //strVoucherSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubAccountCode+"' AND EFFECT='D' AND B.VOUCHER_NO='"+(String)ObjVoucher[k][0]+"'";
                strVoucherSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO  AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='D' AND B.VOUCHER_NO='"+(String)ObjVoucher[k][0]+"'";
                ObjVoucher[k][2]=getSuffix(Double.toString(data.getDoubleValueFromDB(strVoucherSQL,FinanceGlobal.FinURL)));
                //strVoucherSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubAccountCode+"' AND EFFECT='C' AND B.VOUCHER_NO='"+(String)ObjVoucher[k][0]+"'";
                strVoucherSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO  AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='C' AND B.VOUCHER_NO='"+(String)ObjVoucher[k][0]+"'";
                ObjVoucher[k][3]=getSuffix(Double.toString(-1*data.getDoubleValueFromDB(strVoucherSQL,FinanceGlobal.FinURL)));
                strVoucherSQL="SELECT BOOK_CODE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+(String)ObjVoucher[k][0]+"'  AND A.APPROVED="+bApproved+" AND A.CANCELLED=0 ";
                ObjVoucher[k][4]=data.getStringValueFromDB(strVoucherSQL,FinanceGlobal.FinURL);
                rsVoucher.next();
            }
        }
        return ObjVoucher;
    }
    
    public String getCreditInfo(String MainAccountCode,String SubAccountCode,String OpeningDate,String OnDate,boolean bApproved) {
        String CrTotal;
        //strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND SUB_ACCOUNT_CODE='"+SubAccountCode+"' AND EFFECT='C' AND VOUCHER_DATE>='"+OpeningDate+"' AND VOUCHER_DATE<='"+OnDate+"'";
        strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='C' AND VOUCHER_DATE >= '"+OpeningDate+"' AND VOUCHER_DATE <= '"+OnDate+"'";
        //CrTotal=Double.toString(-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL));
        CrTotal=dFormat.format(-1*data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL));
        return CrTotal;
        
    }
    
    public String getDebitInfo(String MainAccountCode,String SubAccountCode,String OpeningDate,String OnDate,boolean bApproved) {
        String DrTotal;
        strSQL="SELECT IF(SUM(AMOUNT) IS NULL,0,SUM(AMOUNT)) FROM D_FIN_VOUCHER_HEADER AS A, D_FIN_VOUCHER_DETAIL_EX  AS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.VOUCHER_NO=B.VOUCHER_NO AND A.CANCELLED=0 AND A.APPROVED="+bApproved+" "+getStrQuery(MainAccountCode,SubAccountCode)+" AND EFFECT='D' AND VOUCHER_DATE >= '"+OpeningDate+"' AND VOUCHER_DATE <='"+OnDate+"'";
        //DrTotal=Double.toString(data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL));
        DrTotal=dFormat.format(data.getDoubleValueFromDB(strSQL,FinanceGlobal.FinURL));
        return DrTotal;
    }
    
    public String getStrQuery(String MainAccountCode,String SubAccountCode) {
        
        String strQuery;
        if(!SubAccountCode.trim().equals("")) {
            strQuery=" AND SUB_ACCOUNT_CODE ="+SubAccountCode;
            if(!MainAccountCode.trim().equals("")) {
                strQuery=strQuery+" AND MAIN_ACCOUNT_CODE ="+MainAccountCode;
            }
        }
        else {
            strQuery = " AND MAIN_ACCOUNT_CODE ="+MainAccountCode ;
        }
        
        return strQuery;
    }
}
