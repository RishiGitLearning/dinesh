/*
 * clsDistinctItemOpening.java
 *
 * Created on June 8, 2009, 7:12 PM
 */

package EITLERP.Stores;

import EITLERP.*;
import EITLERP.Stores.*;
import java.sql.*;
/**
 *
 * @author  nitin
 */
public class clsDistinctItemOpening {
    
    /** Creates a new instance of clsDistinctItemOpening */
    public clsDistinctItemOpening() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String str = "";
            str = "SELECT ITEM_ID,OPENING_QTY,OPENING_RATE,OPENING_VALUE FROM D_COM_OPENING_STOCK_DETAIL "+
                " WHERE ENTRY_NO = 6 AND ITEM_ID LIKE 'RM%' AND COMPANY_ID = 2";
            ResultSet rsTmp = data.getResult(str);
            if (rsTmp.getRow()>0) {
                rsTmp.first();
                while (! rsTmp.isAfterLast()) {
                    String ItemID = rsTmp.getString("ITEM_ID").trim();
                                        
                    str = "SELECT ITEM_ID FROM D_COM_OPENING_STOCK_DETAIL "+
                        "WHERE ENTRY_NO = 0 AND ITEM_ID LIKE 'RM%' AND COMPANY_ID = 2 AND ITEM_ID LIKE '"+ItemID+"%' ";                    
                    if (data.IsRecordExist(str)) {                                        
                        data.Execute("DELETE FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO = 6 AND ITEM_ID LIKE 'RM%' AND COMPANY_ID = 2 AND ITEM_ID LIKE '"+ItemID+"%' ");
                        data.Execute("UPDATE D_COM_OPENING_STOCK_DETAIL SET ENTRY_NO=6 WHERE ENTRY_NO = 0 AND ITEM_ID LIKE 'RM%' AND COMPANY_ID = 2 AND ITEM_ID LIKE '"+ItemID+"%' ");
                    }
                    else {
                        System.out.println("Item ID : " +ItemID);
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
