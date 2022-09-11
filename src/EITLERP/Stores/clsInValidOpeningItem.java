/*
 * clsInValidOpeningItem.java
 *
 * Created on June 6, 2009, 4:28 PM
 */

package EITLERP.Stores;

import EITLERP.*;
import EITLERP.Stores.*;
import java.sql.*;

/**
 *
 * @author  nitin
 */
public class clsInValidOpeningItem {
    
    /** Creates a new instance of clsInValidOpeningItem */
    public clsInValidOpeningItem() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String str = "";
            str = "SELECT ITEM_ID,OPENING_QTY,OPENING_VALUE FROM D_COM_OPENING_STOCK_DETAIL E WHERE E.ENTRY_NO = 6 AND E.OPENING_QTY =0 AND E.OPENING_VALUE <> 0";
            ResultSet rsTmp = data.getResult(str);
            if (rsTmp.getRow()>0) {
                rsTmp.first();
                while (! rsTmp.isAfterLast()) {
                    String ItemID = rsTmp.getString("ITEM_ID").trim();
                    double OpnQty = rsTmp.getDouble("OPENING_QTY");
                    double OpnValue = rsTmp.getDouble("OPENING_VALUE");
                    
                    int cnt = 0;
                    str = "SELECT B.ITEM_ID FROM D_INV_GRN_HEADER A , D_INV_GRN_DETAIL B "+
                    "WHERE A.COMPANY_ID=2 AND B.COMPANY_ID=2 AND A.GRN_NO=B.GRN_NO AND B.ITEM_ID='"+ ItemID + "' " +
                    "AND A.GRN_DATE >= '2008-04-01' AND A.GRN_DATE <= '2009-03-31' AND A.APPROVED=1 AND A.CANCELLED=0 AND (B.LANDED_RATE*B.QTY) <> 0";
                    String GRNItemID = data.getStringValueFromDB(str);
                    
                    if (!GRNItemID.trim().equals("")) {
                        cnt ++;
                    }
                    
                    str = "SELECT D.ITEM_CODE FROM D_INV_ISSUE_HEADER C , D_INV_ISSUE_DETAIL D "+
                    "WHERE C.COMPANY_ID=2 AND D.COMPANY_ID=2 AND C.ISSUE_NO=D.ISSUE_NO AND D.ITEM_CODE='"+ ItemID + "' " +
                    "AND C.ISSUE_DATE >= '2008-04-01' AND C.ISSUE_DATE <= '2009-03-31' AND C.APPROVED=1 AND C.CANCELED=0 AND D.QTY <> 0";
                    String IssueItemID = data.getStringValueFromDB(str);
                    
                    if (!IssueItemID.trim().equals("")) {
                        cnt ++;
                    }
                    
                    if (cnt==0) {
                        System.out.println("ItemID= "+ItemID+"       Opening Qty="+OpnQty+"        Opening Value="+OpnValue);
                    }
                    
                    rsTmp.next();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
