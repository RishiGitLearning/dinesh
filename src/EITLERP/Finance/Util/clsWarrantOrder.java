/*
 * clsImportDeposit.java
 *
 * Created on January 08, 2008, 1:31 PM
 */

package EITLERP.Finance.Util;

import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.*;

/**
 *
 * @author  Prathmesh Shah
 * DON'T ASK QUESTIONS IF YOU FIND ANY ERRORS. QUESTIONS ARE STRICTLY PROHIBITED.
 */
public class clsWarrantOrder {
    
    /** Creates a new instance of clsSOImport */
    public clsWarrantOrder() {
        System.gc();
    }
    
    public static void main(String args[]) {
        String DocNo = "000009";
        new clsWarrantOrder().changeOrder(DocNo);
        System.out.println("The End");
        
    }
    
    private void changeOrder(String DocNo) {
        try {
            
            String SQL = "SELECT A.*, CASE WHEN C.SCHEME_TYPE=1 THEN C.SCHEME_TYPE+4 " +
            "WHEN C.SCHEME_TYPE=2 THEN C.SCHEME_TYPE+1 " +
            "WHEN C.SCHEME_TYPE=3 THEN C.SCHEME_TYPE+7 ELSE 0 END AS SCHEME_ORDER " +
            "FROM D_FD_INT_CALC_DETAIL A, D_FD_DEPOSIT_MASTER B, D_FD_SCHEME_MASTER C " +
            "WHERE A.RECEIPT_NO=B.RECEIPT_NO AND B.SCHEME_ID=C.SCHEME_ID AND A.DOC_NO='"+DocNo+"' " +
            "ORDER BY SCHEME_ORDER,B.MATURITY_DATE,A.RECEIPT_NO ";
            ResultSet rsWOrder = data.getResult(SQL,FinanceGlobal.FinURL);
            rsWOrder.first();
            HashMap List = new HashMap();
            clsCalcInterestItem objItem = new clsCalcInterestItem();
            long LegacyNo = data.getMaxID(2, "D_FD_INT_CALC_DETAIL", "LEGACY_WARRANT_NO", FinanceGlobal.FinURL);
            int Counter = 0;
            while(!rsWOrder.isAfterLast()) {
                Counter++;
                System.out.println("Counter = " + Counter + " Receipt No = " + UtilFunctions.getString(rsWOrder, "RECEIPT_NO", ""));
                objItem = new clsCalcInterestItem();
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsWOrder, "COMPANY_ID", 0));
                objItem.setAttribute("DOC_NO",UtilFunctions.getString(rsWOrder, "DOC_NO", ""));
                objItem.setAttribute("SR_NO",Counter);
                objItem.setAttribute("RECEIPT_NO",UtilFunctions.getString(rsWOrder, "RECEIPT_NO", ""));
                objItem.setAttribute("INTEREST_DAYS",UtilFunctions.getInt(rsWOrder, "INTEREST_DAYS", 0));
                objItem.setAttribute("INTEREST_RATE",UtilFunctions.getDouble(rsWOrder, "INTEREST_RATE", 0));
                objItem.setAttribute("INT_MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsWOrder, "INT_MAIN_ACCOUNT_CODE", ""));
                objItem.setAttribute("PARTY_CODE",UtilFunctions.getString(rsWOrder, "PARTY_CODE", ""));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsWOrder, "MAIN_ACCOUNT_CODE", ""));
                objItem.setAttribute("INTEREST_AMOUNT",UtilFunctions.getDouble(rsWOrder, "INTEREST_AMOUNT", 0));
                objItem.setAttribute("TDS_AMOUNT",UtilFunctions.getDouble(rsWOrder, "TDS_AMOUNT", 0));
                objItem.setAttribute("NET_INTEREST",UtilFunctions.getDouble(rsWOrder, "NET_INTEREST", 0));
                
                objItem.setAttribute("WARRANT_NO",UtilFunctions.getString(rsWOrder, "WARRANT_NO", ""));
                String WarrantNo = UtilFunctions.getString(rsWOrder, "WARRANT_NO", "");
                if(WarrantNo.equals("0000000")) {
                    objItem.setAttribute("LEGACY_WARRANT_NO",0);
                } else {
                    objItem.setAttribute("LEGACY_WARRANT_NO",LegacyNo);
                    LegacyNo++;
                }
                objItem.setAttribute("WARRANT_DATE",UtilFunctions.getString(rsWOrder, "WARRANT_DATE","0000-00-00"));
                objItem.setAttribute("MICR_NO",UtilFunctions.getInt(rsWOrder, "MICR_NO", 0));
                objItem.setAttribute("WARRANT_CLEAR",UtilFunctions.getString(rsWOrder, "WARRANT_CLEAR", ""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsWOrder, "CREATED_BY", ""));
                objItem.setAttribute("CREATED_DATE",UtilFunctions.getString(rsWOrder, "CREATED_DATE","0000-00-00"));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsWOrder, "MODIFIED_BY", ""));
                objItem.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsWOrder, "MODIFIED_DATE","0000-00-00"));
                objItem.setAttribute("CHANGED",UtilFunctions.getInt(rsWOrder, "CHANGED", 0));
                objItem.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsWOrder, "CHANGED_DATE","0000-00-00"));
                objItem.setAttribute("CANCELLED",UtilFunctions.getInt(rsWOrder, "CHANGED_DATE", 0));
                
                List.put(Integer.toString(Counter), objItem);
                
                rsWOrder.next();
            }
            System.out.println("Size = " + List.size());
            
            data.Execute("DELETE FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
            Connection Conn=data.getConn(FinanceGlobal.FinURL);
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE COMPANY_ID=2 AND DOC_NO='1'");
            
            for(int i=1;i<=List.size();i++) {
                clsCalcInterestItem newObjItem = (clsCalcInterestItem) List.get(Integer.toString(i));
                System.out.println("Counter = " + newObjItem.getAttribute("SR_NO").getInt() + " Receipt No = " + newObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",newObjItem.getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",newObjItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("RECEIPT_NO",newObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.updateInt("INTEREST_DAYS",newObjItem.getAttribute("INTEREST_DAYS").getInt());
                rsTmp.updateDouble("INTEREST_RATE",newObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsTmp.updateString("INT_MAIN_ACCOUNT_CODE",newObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("PARTY_CODE",newObjItem.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",newObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("INTEREST_AMOUNT",newObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                rsTmp.updateDouble("TDS_AMOUNT",newObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsTmp.updateDouble("NET_INTEREST",newObjItem.getAttribute("NET_INTEREST").getDouble());
                rsTmp.updateInt("LEGACY_WARRANT_NO",newObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                rsTmp.updateString("WARRANT_NO",newObjItem.getAttribute("WARRANT_NO").getString());
                rsTmp.updateString("WARRANT_DATE",newObjItem.getAttribute("WARRANT_DATE").getString());
                rsTmp.updateInt("MICR_NO",newObjItem.getAttribute("MICR_NO").getInt());
                rsTmp.updateString("WARRANT_CLEAR",newObjItem.getAttribute("WARRANT_CLEAR").getString());
                rsTmp.updateString("CREATED_BY", newObjItem.getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE", newObjItem.getAttribute("CREATED_DATE").getString());
                rsTmp.updateString("MODIFIED_BY", newObjItem.getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE", newObjItem.getAttribute("MODIFIED_DATE").getString());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",newObjItem.getAttribute("CHANGED_DATE").getString());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}