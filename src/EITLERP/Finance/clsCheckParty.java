/*
 * clsCheckParty.java
 *
 * Created on March 1, 2012, 11:38 AM
 */

package EITLERP.Finance;

import java.util.HashMap;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import java.util.*;
import java.io.*;
import EITLERP.Sales.clsSalesInvoice;
/**
 *
 * @author  root
 */
public class clsCheckParty {
    HashMap ListMainCode = null;
    /** Creates a new instance of clsCheckParty */
    public clsCheckParty() {
        ListMainCode = new HashMap();
        ListMainCode.put("132642", "210027");
        ListMainCode.put("132635", "210027");
        ListMainCode.put("132666", "210027");
        ListMainCode.put("132745", "210027");
        ListMainCode.put("133155", "210027");
        ListMainCode.put("133162", "210027");
        ListMainCode.put("133203", "210027");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        clsCheckParty checkParty = new clsCheckParty();
        checkParty.FindPartyLedger();
        checkParty.FindSupplier();
    }
    
    private void FindPartyLedger() {
        try {
            String SQL = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_GL WHERE IS_SUBSIDAIRY=1 AND APPROVED=1 AND CANCELLED=0 ORDER BY MAIN_ACCOUNT_CODE";
            ResultSet rsGL = data.getResult(SQL,FinanceGlobal.FinURL);
            ResultSet rsMainCode = null;
            rsGL.first();
            while(!rsGL.isAfterLast()) {
                String MainCode = rsGL.getString("MAIN_ACCOUNT_CODE");
                //System.out.println("MainCode : " + MainCode);
                SQL="SELECT ENTRY_NO FROM D_FIN_OPENING_HEADER WHERE ENTRY_DATE<CURDATE() ORDER BY ENTRY_DATE DESC";
                int EntryNo=data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                SQL="SELECT ENTRY_DATE FROM D_FIN_OPENING_HEADER WHERE ENTRY_NO="+EntryNo;
                String OpeningDate=data.getStringValueFromDB(SQL,FinanceGlobal.FinURL);
                SQL="(SELECT DISTINCT MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE FROM D_FIN_OPENING_DETAIL " +
                "WHERE MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE<>'' AND ENTRY_NO="+EntryNo+") " +
                "UNION " +
                "(SELECT DISTINCT B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL_EX B " +
                "WHERE A.CANCELLED=0 AND A.VOUCHER_DATE>'"+OpeningDate+"' AND A.VOUCHER_DATE<=CURDATE() " +
                "AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 ) " +
                "ORDER BY MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE ";
                rsMainCode = data.getResult(SQL,FinanceGlobal.FinURL);
                rsMainCode.first();
                if(rsMainCode.getRow()>0) {
                    while(!rsMainCode.isAfterLast()) {
                        String SubCode = rsMainCode.getString("SUB_ACCOUNT_CODE");
                        if(!data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+SubCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND APPROVED=1 AND CANCELLED=0",FinanceGlobal.FinURL)) {
                            System.out.println("Party Code : " + SubCode + " Main Code : " + MainCode);
                            insertParty(MainCode,SubCode);
                        }
                        
                        rsMainCode.next();
                    }
                }
                rsGL.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("*** Find PartyLedger : Finished ***");
    }
    
     private void FindSupplier() {
        try { 
           String SQL = "SELECT MAIN_ACCOUNT_CODE,SUPPLIER_CODE FROM D_COM_SUPP_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE <>'' ORDER BY SUPP_ID,SUPPLIER_CODE";
           ResultSet rsSuppCode = data.getResult(SQL);
           
           rsSuppCode.first();
            while(!rsSuppCode.isAfterLast()) {
                String []MainCodes=rsSuppCode.getString("MAIN_ACCOUNT_CODE").split(",");
                String SubCode = rsSuppCode.getString("SUPPLIER_CODE");
                String MainCode = "";
                
                for(int i=0;i<MainCodes.length;i++) {
                    MainCode = MainCodes[i];
                    //System.out.println("MainCode : " + MainCode);
                    SQL="SELECT PARTY_CODE FROM D_FIN_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND PARTY_CODE='"+SubCode+"' "; 
                    if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                        System.out.println("MainCode : " + MainCode +" Sub Code: " + SubCode);
                        insertParty(MainCode,SubCode);
                    }
                }
              rsSuppCode.next();
           }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        System.out.println("*** Find PartyLedger : Finished ***");
    }
    
    private void insertParty(String MainCode,String SubCode) {
        if(ListMainCode.containsKey(MainCode)) {
            new clsSales_Party().AddPartyToFinance(SubCode,MainCode, "210027");
        }
        if(MainCode.equals("125019") || MainCode.equals("125033")) {
            new clsSupplier().AddPartyToFinance(SubCode);
            if(data.IsRecordExist("SELECT * FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE ='"+SubCode+"'",clsFinYear.getDBURL(3, EITLERPGLOBAL.FinYearFrom))) {
                new clsSupplier().AddPartyToExternalDB(SubCode, clsFinYear.getDBURL(3, EITLERPGLOBAL.FinYearFrom));
            }
        }
    }
}
